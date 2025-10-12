package com.tqk.stationeryecommercebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StationeryEcommerceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StationeryEcommerceBackendApplication.class, args);
    }

}
