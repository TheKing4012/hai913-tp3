package com.example.tp3.repositories;

import com.example.tp3.models.Product;
import com.example.tp3.models.UserD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findTop5ByOrderByPrixDesc();
}
