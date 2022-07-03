package io.jay.shop.mongo;

import io.jay.shop.Item;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, String>
        , ReactiveQueryByExampleExecutor<Item> {
}
