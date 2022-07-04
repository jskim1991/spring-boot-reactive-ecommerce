package io.jay.shop.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

    @Id
    private String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }
}
