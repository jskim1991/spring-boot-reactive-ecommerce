package io.jay.shop.controller;

import io.jay.shop.Cart;
import io.jay.shop.Item;
import io.jay.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final CartService cartService;

    @GetMapping("/items")
    Flux<Item> items() {
        return cartService.getItems();
    }

    @GetMapping("/items/search")
    Flux<Item> search(String name, Double price, boolean useAnd) {
        return cartService.searchItems(name, price == null ? 0.0 : price, useAnd);
    }

    @GetMapping("/cart")
    Mono<Cart> cart() {
        return cartService.getCart();
    }

    @PostMapping("/cart/add/{id}")
    Mono<Cart> addToCart(@PathVariable String id) {
        return cartService.addItemToCart(id);
    }
}
