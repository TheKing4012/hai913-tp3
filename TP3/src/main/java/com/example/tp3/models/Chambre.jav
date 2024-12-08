package com.example.tp3.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "room")
public class Chambre {
	@Id
	@GeneratedValue
	@Column(name ="id")
	private long id;
	private int numero;
	private double prix;
	private int nombreLit;
	@ManyToOne
	@JoinColumn(name="hotel_id")
	private Hotel hotel;
	@OneToMany(mappedBy="chambre")
	private List<Reservation> resa;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int roomNumber) {
		this.numero = roomNumber;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double price) {
		this.prix = price;
	}
	public int getNombreLit() {
		return nombreLit;
	}
	public void setNombreLit(int size) {
		this.nombreLit = size;
	}
	
	public Chambre(int numero, double prix, int nombreLit) {
		this.numero = numero;
		this.prix = prix;
		this.nombreLit = nombreLit;
	}
	
	
	public Chambre(int numero, double prix, int nombreLit, Hotel hotel) {
		this.numero = numero;
		this.prix = prix;
		this.nombreLit = nombreLit;
		this.hotel = hotel;
	}
	
	public Chambre() {
		this.numero = 0;
		this.prix = 0;
		this.nombreLit = 0;
	}
	
	@Override
	public String toString() {
		return "Chambre n°" + numero + " (" + nombreLit + " lit(s)) " + prix + "€\n";
	}
	
	
}
