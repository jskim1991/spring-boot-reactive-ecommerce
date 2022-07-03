package io.jay.shop.controller;

import io.jay.shop.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AmqpItemController {

    private final AmqpTemplate template;

    @PostMapping("/ampq/items/add")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.subscribeOn(Schedulers.boundedElastic())
                .flatMap(content -> {
                    return Mono.fromCallable(() -> {
                        template.convertAndSend("ecommerce-app", "new-items", content);
                        return ResponseEntity.created(URI.create("/items"))
                                .build();
                    });
                });
    }
}
