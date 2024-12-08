package com.example.tp3.models;


import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Adresse {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	private String ville;
	private String nom;
	private int numero;
	private int codePostal;
	@OneToOne(mappedBy = "addresse")
	private Hotel hotel;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String city) {
		this.ville = city;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String street) {
		this.nom = street;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int number) {
		this.numero = number;
	}

	public int getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}
	public Adresse(String ville, String nom, int numero, int codePostal) {
		this.ville = ville;
		this.nom = nom;
		this.numero = numero;
		this.codePostal = codePostal;
	}
	
	public Adresse() {
		this.ville = "None";
		this.nom = "None";
		this.numero = 0;
		this.codePostal = 0;
	}
	
	@Override
	public String toString() {
		return "Adresse : " + numero + ", " + nom + ", " + ville + ", " + codePostal + "\n";
	}

	
}
