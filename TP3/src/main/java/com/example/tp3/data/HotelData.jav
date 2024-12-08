package com.example.tp3.data;

import com.example.tp3.models.Adresse;
import com.example.tp3.models.Chambre;
import com.example.tp3.models.Hotel;
import com.example.tp3.repositories.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HotelData {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public CommandLineRunner initDatabase(HotelRepository repository) {
		return args -> {
			Hotel hotel =  new Hotel("Red Hotel", 2.5, null, null, null, "https://static.nationsglory.fr/N4Ny5_NNNN.jpg");
			List<Chambre> chambres = new ArrayList<>();
			Chambre chambre1 = new Chambre(1, 36, 4, hotel);
			Chambre chambre2 = new Chambre(2, 27, 2, hotel);
			Chambre chambre3 = new Chambre(3, 18, 1, hotel);
			Chambre chambre4 = new Chambre(4, 23, 1, hotel);
			chambres.add(chambre1);
			chambres.add(chambre2);
			chambres.add(chambre3);
			chambres.add(chambre4);

			Adresse adresse = new Adresse("Montpellier", "Avenue de l'Observatoire", 54, 34000);
			hotel.setAddresse(adresse);
			hotel.setRooms(chambres);

			logger.info("Loading database with \n" + repository.save(hotel));
		};
	}
}
