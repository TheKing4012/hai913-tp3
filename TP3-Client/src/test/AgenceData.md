package org.example.tp3client.data;

import org.example.tp3client.models.Agence;
import org.example.tp3client.models.Client;
import org.example.tp3client.models.Offres;
import org.example.tp3client.repositories.AgenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgenceData {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public CommandLineRunner initDatabase(AgenceRepository repository) {
		return args -> {
			
			Agence a1 = new Agence("Hotel.org", null);
			List<Offres> offers = new ArrayList<Offres>();
			Offres o1 = new Offres(1, "http://localhost:30000/crowne/api/hotels", 10, a1);
			Offres o2 = new Offres(2, "http://localhost:30001/ritz/api/hotels", 15, a1);
			Offres o3 = new Offres(3, "http://localhost:30002/ibis/api/hotels", 5, a1);
			
			offers.add(o1);
			offers.add(o2);
			offers.add(o3);
			
			a1.setOffres(offers);

			Client c1 = new Client("Thomas", "Henri", "password");
			List<Client> clients = new ArrayList<Client>();
			clients.add(c1);
			a1.setClients(clients);
			
			logger.info("Loading database with " + repository.save(a1));
		};
	}
}
