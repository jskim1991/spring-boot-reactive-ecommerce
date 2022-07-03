package io.jay.shop.controller;

import io.jay.shop.Item;
import io.jay.shop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(HomeController.class)
public class HomeControllerTests {

    @Autowired
    WebTestClient client;

    @MockBean
    CartService mockCartService;

    @Test
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
}
