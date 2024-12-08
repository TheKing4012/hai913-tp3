package org.example.tp3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chambre {

	private long id;
	private int numero;
	private double prix;
	private int nombreLit;
	private Hotel hotel;
	
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getNombreLit() {
		return nombreLit;
	}
	public void setNombreLit(int nombreLit) {
		this.nombreLit = nombreLit;
	}
	
	public Chambre(int numero, double prix, int nombreLit) {
		this.numero = numero;
		this.prix = prix;
		this.nombreLit = nombreLit;
	}
	public Chambre() {
		this.numero = 0;
		this.prix = 0;
		this.nombreLit = 0;
	}

	public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chambre chambre = (Chambre) o;
		return id == chambre.id && numero == chambre.numero && Double.compare(prix, chambre.prix) == 0 && nombreLit == chambre.nombreLit && Objects.equals(hotel, chambre.hotel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, numero, prix, nombreLit, hotel);
	}

	@Override
	public String toString() {
		return "Chambre{" +
				"id=" + id +
				", numero=" + numero +
				", prix=" + prix +
				", nombreLit=" + nombreLit +
				", hotel=" + hotel +
				'}';
	}
}
