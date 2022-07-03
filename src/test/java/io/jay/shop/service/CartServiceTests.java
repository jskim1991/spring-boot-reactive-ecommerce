package io.jay.shop.service;

import io.jay.shop.Cart;
import io.jay.shop.CartItem;
import io.jay.shop.Item;
import io.jay.shop.mongo.CartRepository;
import io.jay.shop.mongo.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartServiceTests {

    ItemRepository mockItemRepository;
    CartRepository mockCartRepository;
    CartService cartService;

    @BeforeEach
    void setup() {
        mockItemRepository = mock(ItemRepository.class);
        mockCartRepository = mock(CartRepository.class);
        cartService = new CartService(mockItemRepository, mockCartRepository);
    }

    @Test
    void getItems_returnsItems() {
        when(mockItemRepository.findAll())
                .thenReturn(Flux.just(new Item("alarm clock", 19.99)));

        cartService.getItems()
                .as(StepVerifier::create)
                .assertNext(item -> {
                    assertThat(item.getName(), equalTo("alarm clock"));
                    assertThat(item.getPrice(), equalTo(19.99));
                })
                .verifyComplete();
    }

    @Test
    void getItems_callsRepository() {
        when(mockItemRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(cartService.getItems())
                .verifyComplete();

        verify(mockItemRepository).findAll();
    }

    @Test
    void addItemToCart_onEmptyCart_returnsOneCartItem() {
        Item alarmClock = new Item("alarm clock", 19.99);
        when(mockItemRepository.findById("item id"))
                .thenReturn(Mono.just(alarmClock));

        when(mockCartRepository.findById("My cart"))
                .thenReturn(Mono.empty());

        Cart sampleCart = new Cart("My cart");
        sampleCart.setCartItems(List.of(new CartItem(alarmClock)));
        when(mockCartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(sampleCart));

        cartService.addItemToCart("item id")
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    List<CartItem> cartItems = cart.getCartItems();
                    assertThat(cartItems.size(), equalTo(1));
                    assertThat(cartItems.get(0).getQuantity(), equalTo(1));
                    Item item = cartItems.get(0).getItem();
                    assertThat(item.getName(), equalTo("alarm clock"));
                    assertThat(item.getPrice(), equalTo(19.99));
                })
                .verifyComplete();
    }

    @Test
    void addItemToCart_toExistingCartItem_incrementsQuantity() {
        Item alarmClock = new Item("alarm clock", 19.99);
        alarmClock.setId("item id");
        when(mockItemRepository.findById("item id"))
                .thenReturn(Mono.just(alarmClock));


        Cart sampleCart = new Cart("My cart");
        sampleCart.setCartItems(List.of(new CartItem(alarmClock)));
        when(mockCartRepository.findById("My cart"))
                .thenReturn(Mono.just(sampleCart));

        Cart updatedCart = new Cart("My cart");
        CartItem updatedCartItem = new CartItem(alarmClock);
        updatedCartItem.increment();
        updatedCart.setCartItems(List.of(updatedCartItem));
        when(mockCartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(updatedCart));

        cartService.addItemToCart("item id")
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    List<CartItem> cartItems = cart.getCartItems();
                    assertThat(cartItems.size(), equalTo(1));
                    assertThat(cartItems.get(0).getQuantity(), equalTo(2));
                    Item item = cartItems.get(0).getItem();
                    assertThat(item.getName(), equalTo("alarm clock"));
                    assertThat(item.getPrice(), equalTo(19.99));
                })
                .verifyComplete();
    }
}
