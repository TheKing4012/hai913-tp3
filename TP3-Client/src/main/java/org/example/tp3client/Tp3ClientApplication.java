package org.example.tp3client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = {
        "org.example.tp3client.client",
        "org.example.tp3client.cli",
        "org.example.tp3client.models"
})
public class Tp3ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp3ClientApplication.class, args);
    }

}
