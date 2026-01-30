package com.helmes.sectorsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SectorsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SectorsApiApplication.class, args);
    }
}
