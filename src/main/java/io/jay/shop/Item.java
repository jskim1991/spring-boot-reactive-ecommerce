package io.jay.shop;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Item {

    @Id
    private String id;
    private String name;
    private double price;

    public Item() {

    }

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
