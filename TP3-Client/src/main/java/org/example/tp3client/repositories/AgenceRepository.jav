package org.example.tp3client.repositories;


import org.example.tp3client.models.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenceRepository extends JpaRepository<Agence, Long> {
	
}
