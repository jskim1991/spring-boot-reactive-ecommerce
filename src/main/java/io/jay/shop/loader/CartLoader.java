package io.jay.shop.loader;

import io.jay.shop.store.CartRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CartLoader {

    @Bean
    CommandLineRunner resetCart(CartRepository repository) {
        return args -> {
            repository.findAll()
                    .flatMap(repository::delete)
                    .blockLast();
        };
    }
}
