package com.example.tp3.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.example.tp3.controllers",  // Package contenant vos contrôleurs
		"com.example.tp3.configurations",  // Package contenant vos contrôleurs
		"com.example.tp3.services",     // Package contenant vos services
		"com.example.tp3.repositories", // Package contenant vos repositories
		"com.example.tp3.models",       // Package contenant vos modèles
		"com.example.tp3.exceptions",   // Package contenant vos exceptions
		"com.example.tp3.utils",        // Package contenant vos utilitaires
		"com.example.tp3.logger",        // Package contenant vos instruments de logs
		"com.example.tp3.data"        // Package contenant vos instruments de logs
})
@EntityScan(basePackages = "com.example.tp3.models") // Scanner les entités
@EnableJpaRepositories(basePackages = "com.example.tp3.repositories") // Scanner les repositories
public class Tp3Application {

	public static void main(String[] args) {
		SpringApplication.run(Tp3Application.class, args);
	}
}
