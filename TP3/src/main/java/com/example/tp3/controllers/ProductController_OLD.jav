package com.example.tp3.controllers;

import com.example.tp3.exceptions.ProductNotFoundException;
import com.example.tp3.models.Product;
import com.example.tp3.repositories.ProductRepository;
import com.example.tp3.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
public class ProductController {

	Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductRepository repository;
	@Autowired
	private JWTUtil jwtUtil;  // Injection de JWTUtil
	private static final String uri = "productservice/api";

	// Méthode utilitaire pour vérifier la validité du token JWT
	private boolean isConnected(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			token = token.replace("Bearer ", "");  // Nettoyage du token
			String email = jwtUtil.extractUsername(token);  // Extraction de l'email
			return jwtUtil.validateToken(token, email);  // Validation du token avec l'email
		}
		return false;
	}

	@GetMapping(uri + "/products/top5")
	public ResponseEntity<List<Product>> findTop5ExpensiveProducts(HttpServletRequest request) {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		List<Product> top5Products = repository.findTop5ByOrderByPrixDesc();
		return ResponseEntity.ok(top5Products);
	}


	// Vérifie la connexion avant de retourner la liste des produits
	@GetMapping(uri + "/products")
	public ResponseEntity<List<Product>> getAllProducts(HttpServletRequest request) throws ProductNotFoundException {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping(uri + "/products/count")
	public String count(HttpServletRequest request) {
		if (!isConnected(request)) {
			return "{\"error\": \"Non authentifié\"}";
		}
		return String.format("{\"%s\": %d}", "count", repository.count());
	}

	@GetMapping(uri + "/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable long id, HttpServletRequest request) throws ProductNotFoundException {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
		return ResponseEntity.ok(product);
	}

	@PostMapping(uri + "/products")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Product> createProduct(@RequestBody Product product, HttpServletRequest request) {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		Product savedProduct = repository.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

	@PutMapping(uri + "/products/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct, @PathVariable long id, HttpServletRequest request) throws ProductNotFoundException {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		Product updatedProduct = repository.findById(id).map(product -> {
			product.setNom(newProduct.getNom());
			product.setPrix(newProduct.getPrix());
			product.setDateExp(newProduct.getDateExp());
			return repository.save(product);
		}).orElseThrow(() -> new ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));

		return ResponseEntity.ok(updatedProduct);
	}

	@DeleteMapping(uri + "/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteProduct(@PathVariable long id, HttpServletRequest request) throws ProductNotFoundException {
		if (!isConnected(request)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
		repository.delete(product);
		return ResponseEntity.noContent().build();
	}
}
