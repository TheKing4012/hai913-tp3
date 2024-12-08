package com.example.tp3.controllers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@RestController
public class ProductController {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProductController.class);

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.tp3.repositories.ProductRepository repository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.tp3.utils.JWTUtil jwtUtil;// Injection de JWTUtil


    private static final String uri = "productservice/api";

    // Méthode utilitaire pour vérifier la validité du token JWT
    private boolean isConnected(jakarta.servlet.http.HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.replace("Bearer ", "");// Nettoyage du token

            String email = jwtUtil.extractUsername(token);// Extraction de l'email

            return jwtUtil.validateToken(token, email);// Validation du token avec l'email

        }
        return false;
    }

    @GetMapping(ProductController.uri + "/products/top5")
    public org.springframework.http.ResponseEntity<java.util.List<com.example.tp3.models.Product>> findTop5ExpensiveProducts(jakarta.servlet.http.HttpServletRequest request) {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a SEARCH operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "findTop5ExpensiveProducts");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        java.util.List<com.example.tp3.models.Product> top5Products = repository.findTop5ByOrderByPrixDesc();
        return org.springframework.http.ResponseEntity.ok(top5Products);
    }

    // Vérifie la connexion avant de retourner la liste des produits
    @GetMapping(ProductController.uri + "/products")
    public org.springframework.http.ResponseEntity<java.util.List<com.example.tp3.models.Product>> getAllProducts(jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a READ operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "getAllProducts");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        return org.springframework.http.ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(ProductController.uri + "/products/count")
    public String count(jakarta.servlet.http.HttpServletRequest request) {
        if (!isConnected(request)) {
            return "{\"error\": \"Non authentifié\"}";
        }
        return String.format("{\"%s\": %d}", "count", repository.count());
    }

    @GetMapping(ProductController.uri + "/products/{id}")
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> getProductById(@PathVariable
    long id, jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a READ operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "getProductById");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product product = repository.findById(id).orElseThrow(() -> new com.example.tp3.exceptions.ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
        return org.springframework.http.ResponseEntity.ok(product);
    }

    @PostMapping(ProductController.uri + "/products")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> createProduct(@RequestBody
    com.example.tp3.models.Product product, jakarta.servlet.http.HttpServletRequest request) {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a WRITE operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "createProduct");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product savedProduct = repository.save(product);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping(ProductController.uri + "/products/{id}")
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> updateProduct(@RequestBody
    com.example.tp3.models.Product newProduct, @PathVariable
    long id, jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a WRITE operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "updateProduct");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product updatedProduct = repository.findById(id).map(product -> {
            product.setNom(newProduct.getNom());
            product.setPrix(newProduct.getPrix());
            product.setDateExp(newProduct.getDateExp());
            return repository.save(product);
        }).orElseThrow(() -> new com.example.tp3.exceptions.ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
        return org.springframework.http.ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(ProductController.uri + "/products/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public org.springframework.http.ResponseEntity<Void> deleteProduct(@PathVariable
    long id, jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a WRITE operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "deleteProduct");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product product = repository.findById(id).orElseThrow(() -> new com.example.tp3.exceptions.ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
        repository.delete(product);
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
