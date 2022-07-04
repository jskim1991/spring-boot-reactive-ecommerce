package io.jay.shop.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest(properties = {"spring.mongodb.embedded.version=5.0.6"})
public class ItemRepositoryTests {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void findAll() {
        itemRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
}
