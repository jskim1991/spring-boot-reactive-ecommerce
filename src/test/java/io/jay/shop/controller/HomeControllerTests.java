package io.jay.shop.controller;

import io.jay.shop.domain.Cart;
import io.jay.shop.domain.Item;
import io.jay.shop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(value = HomeController.class)
public class HomeControllerTests {

    @Autowired
    WebTestClient client;

    @MockBean
    CartService mockCartService;

    @Test
    @WithMockUser
    void items_returnItems() {
        when(mockCartService.getItems())
                .thenReturn(Flux.just(new Item("alarm clock", 19.99)));

        client.get()
                .uri("/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("alarm clock")
                .jsonPath("$[0].price").isEqualTo(19.99);
    }

    @Test
    @WithMockUser(username = "jay")
    void cart_returnsCart() {
        when(mockCartService.getCart("jay's Cart"))
                .thenReturn(Mono.just(new Cart("jay's Cart")));

        client.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("jay's Cart")
                .jsonPath("$.cartItems").isArray();
    }
}
