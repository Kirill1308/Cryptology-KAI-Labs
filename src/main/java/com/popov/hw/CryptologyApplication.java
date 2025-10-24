package com.popov.hw;

import com.popov.hw.coordinator.ApplicationCoordinator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CryptologyApplication implements CommandLineRunner {

    private final ApplicationCoordinator coordinator;

    public static void main(String[] args) {
        SpringApplication.run(CryptologyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        coordinator.execute();
    }
}
