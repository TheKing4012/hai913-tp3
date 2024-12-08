package com.example.tp3.controllers;
import org.springframework.web.bind.annotation.*;
@org.springframework.web.bind.annotation.RestController
public class ProductController {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(com.example.tp3.controllers.ProductController.class);

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.tp3.repositories.ProductRepository repository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.tp3.utils.JWTUtil jwtUtil;// Injection de JWTUtil


    private static final java.lang.String uri = "productservice/api";

    // Méthode utilitaire pour vérifier la validité du token JWT
    private boolean isConnected(jakarta.servlet.http.HttpServletRequest request) {
        java.lang.String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.replace("Bearer ", "");// Nettoyage du token

            java.lang.String email = jwtUtil.extractUsername(token);// Extraction de l'email

            return jwtUtil.validateToken(token, email);// Validation du token avec l'email

        }
        return false;
    }

    @org.springframework.web.bind.annotation.GetMapping(com.example.tp3.controllers.ProductController.uri + "/products/top5")
    public org.springframework.http.ResponseEntity<java.util.List<com.example.tp3.models.Product>> findTop5ExpensiveProducts(jakarta.servlet.http.HttpServletRequest request) {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a SEARCH operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "findTop5ExpensiveProducts");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        java.util.List<com.example.tp3.models.Product> top5Products = repository.findTop5ByOrderByPrixDesc();
        return org.springframework.http.ResponseEntity.ok(top5Products);
    }

    // Vérifie la connexion avant de retourner la liste des produits
    @org.springframework.web.bind.annotation.GetMapping(com.example.tp3.controllers.ProductController.uri + "/products")
    public org.springframework.http.ResponseEntity<java.util.List<com.example.tp3.models.Product>> getAllProducts(jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a READ operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "getAllProducts");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        return org.springframework.http.ResponseEntity.ok(repository.findAll());
    }

    @org.springframework.web.bind.annotation.GetMapping(com.example.tp3.controllers.ProductController.uri + "/products/count")
    public java.lang.String count(jakarta.servlet.http.HttpServletRequest request) {
        if (!isConnected(request)) {
            return "{\"error\": \"Non authentifié\"}";
        }
        return java.lang.String.format("{\"%s\": %d}", "count", repository.count());
    }

    @org.springframework.web.bind.annotation.GetMapping(com.example.tp3.controllers.ProductController.uri + "/products/{id}")
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> getProductById(@org.springframework.web.bind.annotation.PathVariable
    long id, jakarta.servlet.http.HttpServletRequest request) throws com.example.tp3.exceptions.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a READ operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "getProductById");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product product = repository.findById(id).orElseThrow(() -> new com.example.tp3.exceptions.ProductNotFoundException("Erreur : impossible de trouver un produit avec cet ID"));
        return org.springframework.http.ResponseEntity.ok(product);
    }

    @org.springframework.web.bind.annotation.PostMapping(com.example.tp3.controllers.ProductController.uri + "/products")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> createProduct(@org.springframework.web.bind.annotation.RequestBody
    com.example.tp3.models.Product product, jakarta.servlet.http.HttpServletRequest request) {
        org.slf4j.LoggerFactory.getLogger("com.example.tp3.controllers.ProductController").info("User: {} performed a WRITE operation on method: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "createProduct");
        if (!isConnected(request)) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(null);
        }
        com.example.tp3.models.Product savedProduct = repository.save(product);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(savedProduct);
    }

    @org.springframework.web.bind.annotation.PutMapping(com.example.tp3.controllers.ProductController.uri + "/products/{id}")
    public org.springframework.http.ResponseEntity<com.example.tp3.models.Product> updateProduct(@org.springframework.web.bind.annotation.RequestBody
    com.example.tp3.models.Product newProduct, @org.springframework.web.bind.annotation.PathVariable
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

    @org.springframework.web.bind.annotation.DeleteMapping(com.example.tp3.controllers.ProductController.uri + "/products/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public org.springframework.http.ResponseEntity<java.lang.Void> deleteProduct(@org.springframework.web.bind.annotation.PathVariable
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
