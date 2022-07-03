package io.jay.shop.service;

import io.jay.shop.Cart;
import io.jay.shop.CartItem;
import io.jay.shop.Item;
import io.jay.shop.mongo.CartRepository;
import io.jay.shop.mongo.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }

    public Mono<Cart> getCart() {
        return cartRepository.findById("My cart")
                .defaultIfEmpty(new Cart("My cart"));
    }

    public Mono<Cart> addItemToCart(String itemId) {
        return cartRepository.findById("My cart")
                .defaultIfEmpty(new Cart("My cart"))
                .flatMap(cart -> {
                    return cart.getCartItems().stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                            .findAny()
                            .map(cartItem -> {
                                cartItem.increment();
                                return Mono.just(cart);
                            })
                            .orElseGet(() -> {
                                return this.itemRepository.findById(itemId)
                                        .map(item -> new CartItem(item))
                                        .map(cartItem -> {
                                            cart.getCartItems().add(cartItem);
                                            return cart;
                                        });
                            });
                })
                .flatMap(cartRepository::save);
    }

    public Flux<Item> searchItems(String name, double price, boolean useAnd) {
        Item item = new Item(name, price);

        ExampleMatcher matcher = useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny();
        matcher = matcher
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Item> probe = Example.of(item, matcher);

        return itemRepository.findAll(probe);
    }
}
