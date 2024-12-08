package com.example.tp3.controllers;

import com.example.tp3.exceptions.HotelNotFoundException;
import com.example.tp3.models.Adresse;
import com.example.tp3.models.Chambre;
import com.example.tp3.models.Hotel;
import com.example.tp3.models.Reservation;
import com.example.tp3.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HotelController {
	
	@Autowired
	private HotelRepository repository;
	private static final String uri = "hotelservice/api";

	@GetMapping(uri + "/hotels")
	public List<Hotel> getAllHotels() {
		return repository.findAll();
	}

	@GetMapping(uri + "/hotels/{id}/count")
	public String getHotelRoomCount(@PathVariable long id) throws HotelNotFoundException {
		Hotel hotel = repository.findById(id).orElseThrow(() -> new HotelNotFoundException("Erreur : impossible de trouver une chambre avec cet ID"));
		return String.format("{\"%s\": %d}", "count", hotel.getRooms().size());
	}
	
	@GetMapping(uri + "/hotels/count")
	public String count() {
		return String.format("{\"%s\": %d}", "count", repository.count());
	}
	
	@GetMapping(uri + "/hotels/{id}")
	public Hotel getHotelById(@PathVariable long id) throws HotelNotFoundException {
		return repository.findById(id).orElseThrow(() -> new HotelNotFoundException("Erreur : impossible de trouver une chambre avec cet ID"));
	}
	
	@RequestMapping(
			  value = uri + "/hotels/search",
			  params = { "ville", "nblit", "nbetoiles", "dateentree", "datedepart", "prix" },
			  method = RequestMethod.GET)
	@ResponseBody
	public Hotel searchHotel(@RequestParam("ville") String ville, @RequestParam("nblit") int nbLit, @RequestParam("nbetoiles") double nbEtoiles,
							 @RequestParam("dateentree") String dateEntree, @RequestParam("datedepart") String dateout, @RequestParam("prix") double price) throws HotelNotFoundException {
		List<Hotel> hotels = repository.findAll();
		Adresse p = hotels.get(0).getAddresse();
		double stars = hotels.get(0).getNbEtoiles();
		LocalDate in = LocalDate.parse(dateEntree);
		LocalDate out = LocalDate.parse(dateout);
		Hotel toReturnHotel = hotels.get(0);
		List<Chambre> newChambres = new ArrayList<Chambre>();
		if((p.getVille().contains(ville)) && stars >= nbEtoiles) {
			for (Chambre chambre : hotels.get(0).getRooms()) {
				double roomPrice = chambre.getPrix();
				int roomSize = chambre.getNombreLit();
				if(roomPrice <= price && roomSize >= nbLit) {
					boolean isOkay = true;
					for (Reservation resa : hotels.get(0).getResa()) {
						if(resa.getRoom().getNumero() == chambre.getNumero()) {
							if((in.isAfter(resa.getIn()) && in.isBefore(resa.getOut()))
								|| (out.isAfter(resa.getIn()) && out.isBefore(resa.getOut()))
								|| ((in.isBefore(resa.getIn()) && out.isAfter(resa.getOut())))
								|| (in.isAfter(resa.getIn()) && out.isBefore(resa.getOut()))
								|| (in.isEqual(resa.getIn()) || out.isEqual(resa.getOut()))) {
								isOkay = false;
								continue;
							}
						}
					}
					if(isOkay) {
						newChambres.add(chambre);
					}
				}
			}
			if(newChambres.size() != 0) {
				toReturnHotel.setRooms(newChambres);
				return toReturnHotel;
			}
		}
		return new Hotel();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(uri + "/hotels")
	public Hotel createHotel(@RequestBody Hotel hotel) {
		return repository.save(hotel);
	}
	
	@PutMapping(uri + "/hotels/{id}")
	public Hotel updateHotel(@RequestBody Hotel newHotel, @PathVariable long id) {
		return repository.findById(id).map(hotel -> {
			Reservation resa = new Reservation();
			if(hotel.getResa() == null) {
				hotel.setResa(new ArrayList<Reservation>());
			}
			resa = newHotel.getResa().get(0);
			resa.setHotelResa(newHotel);
			hotel.getResa().add(resa);				
			repository.save(hotel);
			return hotel;
		}).orElseGet(() -> {
			newHotel.setId(id);
			repository.save(newHotel);
			return newHotel;
		});
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(uri + "/hotels/{id}")
	public void deleteEmployee(@PathVariable long id) throws HotelNotFoundException{
		Hotel hotel = repository.findById(id).orElseThrow(() -> new HotelNotFoundException("Erreur : impossible de trouver un hotel avec cet ID"));
		repository.delete(hotel);
		
	}
}
