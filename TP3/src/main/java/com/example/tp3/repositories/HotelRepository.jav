package com.example.tp3.repositories;

import com.example.tp3.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long>{
	
}
