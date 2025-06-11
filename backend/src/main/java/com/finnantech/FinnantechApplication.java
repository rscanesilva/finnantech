package com.finnantech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.finnantech")
public class FinnantechApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinnantechApplication.class, args);
    }
} 