package io.jay.shop.loader;

import io.jay.shop.Item;
import io.jay.shop.mongo.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class ItemLoader {

    @Bean
    CommandLineRunner initializeItems(ItemRepository repository) {
        return args -> {
            repository.findAll()
                    .flatMap(repository::delete)
                    .blockLast();

            List<Item> items = List.of(
                    new Item("alarm clock", 19.99),
                    new Item("Smart TV", 249.99)
            );
            Flux.fromIterable(items)
                    .flatMap(repository::save)
                    .blockLast();
        };
    }
}
