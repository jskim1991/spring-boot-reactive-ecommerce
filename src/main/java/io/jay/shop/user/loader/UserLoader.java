package io.jay.shop.user.loader;

import io.jay.shop.user.domain.User;
import io.jay.shop.user.store.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class UserLoader {

    @Bean
    CommandLineRunner initializeUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findAll()
                    .flatMap(userRepository::delete)
                    .blockLast();

            User user = User.builder()
                    .name("jay")
                    .password(passwordEncoder.encode("jay"))
                    .roles(List.of("ROLE_USER"))
                    .build();
            User admin = User.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(List.of("ROLE_USER", "ROLE_INVENTORY"))
                    .build();
            Flux.fromIterable(List.of(user, admin))
                    .flatMap(userRepository::save)
                    .blockLast();
        };
    }
}
