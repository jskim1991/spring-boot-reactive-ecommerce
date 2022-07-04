package io.jay.shop.user;

import io.jay.shop.domain.Item;
import io.jay.shop.store.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {"spring.mongodb.embedded.version=5.0.6"})
@AutoConfigureWebTestClient
public class SecurityIT {

    @Autowired
    WebTestClient client;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @WithMockUser(username = "jay")
    void addingItem_withoutProperRole_returnsForbidden() {
        client.post()
                .uri("/amqp/items/add")
                .bodyValue(new Item("iPhone", 999.87))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", roles = "INVENTORY")
    void addingItem_withProperRole_returnsCreated() {
        Item item = new Item("iPhone", 999.87);
        client.post()
                .uri("/amqp/items/add")
                .bodyValue(item)
                .exchange()
                .expectStatus()
                .isCreated();

        itemRepository.findAll(Example.of(item))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
