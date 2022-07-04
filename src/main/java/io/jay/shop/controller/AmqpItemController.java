package io.jay.shop.controller;

import io.jay.shop.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

import static io.jay.shop.constant.MessageConstants.*;

@RestController
@RequiredArgsConstructor
public class AmqpItemController {

    private final AmqpTemplate template;

    @PreAuthorize("hasRole('INVENTORY')")
    @PostMapping("/amqp/items/add")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.subscribeOn(Schedulers.boundedElastic())
                .flatMap(content -> {
                    return Mono.fromCallable(() -> {
                        template.convertAndSend(EXCHANGE, ROUTING_KEY, content);
                        return ResponseEntity.created(URI.create("/items"))
                                .build();
                    });
                });
    }
}
