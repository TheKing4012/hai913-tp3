package com.example.tp3.data;

import com.example.tp3.models.Product;
import com.example.tp3.models.UserD;
import com.example.tp3.repositories.ProductRepository;
import com.example.tp3.repositories.UserDRepository;
import com.example.tp3.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@Configuration
public class UserDData {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtil jwtUtil; // Injecter JWTUtil

	@Bean
	public CommandLineRunner initUserDatabase(UserDRepository userDRepository) {
		return args -> {
			ArrayList<UserD> userDs = new ArrayList<>();
			// Création de l'utilisateur
			UserD user1 = new UserD("toto", "toto@toto", passwordEncoder.encode("toto"));
			UserD user2 = new UserD("tata", "tata@tata", passwordEncoder.encode("tata"));
			UserD user3 = new UserD("tutu", "tutu@tutu", passwordEncoder.encode("tutu"));
			UserD user4 = new UserD("test", "test@test", passwordEncoder.encode("test"));
			UserD user5 = new UserD("donald", "donald@gmail.com", passwordEncoder.encode("donald"));
			UserD user6 = new UserD("romain", "romain@gmail.com", passwordEncoder.encode("romain"));
			UserD user7 = new UserD("richard", "richard.picole@rpodev.fr", passwordEncoder.encode("richard"));
			UserD user8 = new UserD("magali", "magali@gmail.com", passwordEncoder.encode("magali"));
			UserD user9 = new UserD("sebastien", "sebastien@gmail.com", passwordEncoder.encode("sebastien"));
			UserD user10 = new UserD("quentin", "quentin@gmail.com", passwordEncoder.encode("quentin"));

			userDs.add(user1);
			userDs.add(user2);
			userDs.add(user3);
			userDs.add(user4);
			userDs.add(user5);
			userDs.add(user6);
			userDs.add(user7);
			userDs.add(user8);
			userDs.add(user9);
			userDs.add(user10);

			for (UserD user : userDs) {
				jwtUtil.generateToken(user.getEmail());
				logger.info("Loading database with \n" + userDRepository.save(user));
			}
		};
	}

	@Bean
	public CommandLineRunner initProductDatabase(ProductRepository productRepository) {
		return args -> {
			ArrayList<Product> products = new ArrayList<>();

			Product p1 = new Product("Gel", 10.0, "2024-01-01");
			Product p2 = new Product("PS5", 499.99, "2024-05-15");
			Product p3 = new Product("Energy Drink", 2.38, "2024-03-10");
			Product p4 = new Product("Shampoo", 5.99, "2024-04-20");
			Product p5 = new Product("Laptop", 899.49, "2024-06-30");
			Product p6 = new Product("Headphones", 75.0, "2024-07-25");
			Product p7 = new Product("Notebook", 2.50, "2025-01-01");
			Product p8 = new Product("Backpack", 30.0, "2024-12-31");
			Product p9 = new Product("Mouse", 15.0, "2024-02-14");
			Product p10 = new Product("Keyboard", 25.0, "2024-11-11");


			products.add(p1);
			products.add(p2);
			products.add(p3);
			products.add(p4);
			products.add(p5);
			products.add(p6);
			products.add(p7);
			products.add(p8);
			products.add(p9);
			products.add(p10);

			for (Product product : products) {
				logger.info("Loading database with \n" + productRepository.save(product));
			}
		};
	}
}
