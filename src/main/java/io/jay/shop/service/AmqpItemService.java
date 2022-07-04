package io.jay.shop.service;

import io.jay.shop.Item;
import io.jay.shop.constant.MessageConstants;
import io.jay.shop.mongo.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AmqpItemService {

    private final ItemRepository itemRepository;

    @RabbitListener(
            ackMode = "MANUAL",
            queues = MessageConstants.QUEUE
    )
    public Mono<Void> processNewItems(Item item) {
        return this.itemRepository.save(item)
                .log()
                .then();
    }
}
