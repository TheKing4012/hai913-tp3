package org.example.tp3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel {

	// Attributes
	private long id;
	private String nom;
	private double nbEtoiles;
	private List<Chambre> chambres;
	private Adresse adresse;
	private List<Reservation> listeReservations;
	private String imagePath;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getNbEtoiles() {
		return nbEtoiles;
	}
	public void setNbEtoiles(double nbEtoiles) {
		this.nbEtoiles = nbEtoiles;
	}
	public List<Chambre> getChambres() {
		return chambres;
	}
	public void setChambres(List<Chambre> chambres) {
		this.chambres = chambres;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public List<Reservation> getListeReservations() {
		return listeReservations;
	}
	public void setListeReservations(List<Reservation> listeReservations) {
		this.listeReservations = listeReservations;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public Hotel(String nom, double nbEtoiles, List<Chambre> chambres, Adresse adresse, List<Reservation> listeReservations,
				 String imagePath) {
		this.nom = nom;
		this.nbEtoiles = nbEtoiles;
		this.chambres = chambres;
		this.adresse = adresse;
		this.listeReservations = listeReservations;
		this.imagePath = imagePath;
	}
	
	public Hotel(String nom, double nbEtoiles, Adresse adresse) {
		this.nom = nom;
		this.nbEtoiles = nbEtoiles;
		this.adresse = adresse;
	}
	
	
	public Hotel() {
		this.nom = "Undefined";
		this.nbEtoiles = 0;
		this.chambres = new ArrayList<>();
		this.adresse = new Adresse();
	}

	@Override
	public String toString() {
		return "Hotel{" +
				"id=" + id +
				", nom='" + nom + '\'' +
				", nbEtoiles=" + nbEtoiles +
				", chambres=" + chambres +
				", adresse=" + adresse +
				", listeReservations=" + listeReservations +
				", imagePath='" + imagePath + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Hotel hotel = (Hotel) o;
		return id == hotel.id && Double.compare(nbEtoiles, hotel.nbEtoiles) == 0 && Objects.equals(nom, hotel.nom) && Objects.equals(chambres, hotel.chambres) && Objects.equals(adresse, hotel.adresse) && Objects.equals(listeReservations, hotel.listeReservations) && Objects.equals(imagePath, hotel.imagePath);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nom, nbEtoiles, chambres, adresse, listeReservations, imagePath);
	}
}
