package com.example.tp3.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Hotel {
	// Attributes
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	private String nom;
	private double nbEtoiles;
	@OneToMany(mappedBy="hotel", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Chambre> chambres;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="address_id", referencedColumnName = "id")
	private Adresse addresse;
	@OneToMany(mappedBy="hotelResa", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Reservation> resa;
	private String imagePath;
	
	public List<Reservation> getResa() {
		return resa;
	}
	public void setResa(List<Reservation> resa) {
		this.resa = resa;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String name) {
		this.nom = name;
	}
	public double getNbEtoiles() {
		return nbEtoiles;
	}
	public void setNbEtoiles(double stars) {
		this.nbEtoiles = stars;
	}
	public List<Chambre> getRooms() {
		return chambres;
	}
	public void setRooms(List<Chambre> chambres) {
		this.chambres = chambres;
	}
	public Adresse getAddresse() {
		return addresse;
	}
	public void setAddresse(Adresse address) {
		this.addresse = address;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imageFolder) {
		this.imagePath = imageFolder;
	}
	
	public Hotel(String nom, double nbEtoiles, List<Chambre> chambres, Adresse addresse, List<Reservation> resa,
				 String imagePath) {
		this.nom = nom;
		this.nbEtoiles = nbEtoiles;
		this.chambres = chambres;
		this.addresse = addresse;
		this.resa = resa;
		this.imagePath = imagePath;
	}
	
	public Hotel(String nom, double nbEtoiles, List<Chambre> chambres, Adresse addresse) {
		this.nom = nom;
		this.nbEtoiles = nbEtoiles;
		this.chambres = chambres;
		this.addresse = addresse;
	}
	
	
	public Hotel() {
		this.nom = "Undefined";
		this.nbEtoiles = 0;
		this.chambres = new ArrayList<Chambre>();
		this.addresse = new Adresse();
		this.resa = new ArrayList<Reservation>();
	}

	@Override
	public String toString() {
		return "Hotel{" +
				"id=" + id +
				", nom='" + nom + '\'' +
				", nbEtoiles=" + nbEtoiles +
				", chambres=" + chambres +
				", addresse=" + addresse +
				", reservation=" + resa +
				", imagePath='" + imagePath + '\'' +
				'}';
	}
}
