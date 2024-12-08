package org.example.tp3client.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp3client.models.Agence;
import org.example.tp3client.models.Client;
import org.example.tp3client.models.Hotel;
import org.example.tp3client.models.Offres;
import org.example.tp3client.repositories.AgenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AgencyController {
	@Autowired
	private RestTemplate proxy;
	@Autowired
	private AgenceRepository repository;
	private static final String uri = "hotelorg/api";
	
	@GetMapping(uri + "/agence/count")
	public int getCountHotels() {
		int x = 0;
		Agence agence = repository.findAll().get(0);
		ArrayList<String> URIS = new ArrayList<>();
		for (Offres offer: agence.getOffres()) {
			URIS.add(offer.getUri());
		}
		for (String uri : URIS) {
			for (Offres offer: agence.getOffres()) {
				URIS.add(offer.getUri());
			}
			try {
				String uriCount = uri + "/count";
				ObjectMapper mapper = new ObjectMapper();
				String countStr = proxy.getForObject(uriCount, String.class);
				long count = (int)mapper.readValue(countStr, Map.class).get("count");
				x += count;						
			}
			catch (Exception e) {
				continue;
			}
		}
		return x;
	}

	@RequestMapping(
			value = uri + "/agency/connect",
			params = { "nom", "mdp"},
			method = RequestMethod.GET)
	@ResponseBody
	public Client tryConnectClient(@RequestParam("nom") String nom, @RequestParam("mdp") String mdp) {
		Agence agence = repository.findAll().get(0);
		for(Client client : agence.getClients()) {
			if(client.getNom().equalsIgnoreCase(nom)) {
				if(client.getMotDePasse().equals(mdp)) {
					return client;
				}
			}
		}
		return null;
	}
	
	
	@PutMapping(uri + "/agence/resa/{id}")
	public Hotel updateHotel(@RequestBody Hotel newHotel, @PathVariable long id) {
		Agence agence = repository.findAll().get(0);
		HashMap<Long, String> URIS = new HashMap<Long, String>();
		for (Offres offer: agence.getOffres()) {
			URIS.put(offer.getNumHotel(), offer.getUri());
		}
		String uri = URIS.get(id);
		proxy.put(uri + "/" + String.valueOf(id), newHotel);
		return newHotel;
	}
	
	@GetMapping(uri + "/agence")
	public List<Agence> getAllAgencies() {
		return repository.findAll();
	}
	
	@GetMapping(uri + "/agence/hotels")
	public List<Hotel> getAllHotels() {
		Agence agence = repository.findAll().get(0);
		ArrayList<String> URIS = new ArrayList<>();
		for (Offres offer: agence.getOffres()) {
			URIS.add(offer.getUri());
		}
		List<Hotel> hotels = new ArrayList<>();
		for (String uri : URIS) {
			try {
				Hotel[] hotel = proxy.getForObject(uri, Hotel[].class);
				for (Hotel hotel2 : hotel) {
					hotels.add(hotel2);
				}
			}
			catch (Exception e) {
				continue;
			}
			
		}
		return hotels;
	}
	
	@RequestMapping(
			  value = uri + "/agency/search",
			  params = { "position", "size", "rating", "datein", "dateout", "price" }, 
			  method = RequestMethod.GET)
	@ResponseBody
	public List<Hotel> searchHotel(@RequestParam("position") String position, @RequestParam("size") int size, @RequestParam("rating") double rating,
								   @RequestParam("datein") String datein, @RequestParam("dateout") String dateout, @RequestParam("price") double price) {
		Agence agence = repository.findAll().get(0);
		List<Hotel> toReturnHotels = new ArrayList<>();
		HashMap<String, Double> URIS = new HashMap<>();
		for (Offres offer: agence.getOffres()) {
			URIS.put(offer.getUri(), offer.getAmount());
		}
		Map<String, String> params = new HashMap<>();
		params.put("position", position);
		params.put("datein", datein);
		params.put("dateout", dateout);
		params.put("size", String.valueOf(size));
		params.put("rating", String.valueOf(rating));
		params.put("price", String.valueOf(price));
		for (String uri : URIS.keySet()) {
			try {
				String url = uri + "/search?position={position}&size={size}&rating={rating}&datein={datein}&dateout={dateout}&price={price}";
				Hotel returnedHotel = proxy.getForObject(url, Hotel.class, params);
				if(!returnedHotel.getNom().equals("Undefined")) {
					returnedHotel.setImagePath(returnedHotel.getImagePath()  + URIS.get(uri).toString() +"d");
					toReturnHotels.add(returnedHotel);						
				}
			}
			catch (Exception e) {
				continue;
			}
		}
	return toReturnHotels;
	}
}
