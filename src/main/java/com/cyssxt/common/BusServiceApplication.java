package com.cyssxt.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Random;

@SpringBootApplication
public class BusServiceApplication {

    public static void main(String[] args) {
        System.out.println((3/4)*10);
//        SpringApplication.run(BusServiceApplication.class, args);
    }

}
