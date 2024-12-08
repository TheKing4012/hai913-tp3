package com.example.tp3.repositories;

import com.example.tp3.models.UserD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Ajout explicite de l'annotation (bien que Spring Data JPA le gère automatiquement)
public interface UserDRepository extends JpaRepository<UserD, Long> {
    Optional<UserD> findByEmail(String email);
    boolean existsByEmail(String email);
}
