package org.example.tp3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Adresse {
	
	private long id;
	private String ville;
	private String nom;
	private int numero;
	private int codePostal;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(int gps) {
		this.codePostal = gps;
	}
	public Adresse(String ville, String country, String nom, int numero, int codePostal) {
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Adresse adresse = (Adresse) o;
		return id == adresse.id && numero == adresse.numero && codePostal == adresse.codePostal && Objects.equals(ville, adresse.ville) && Objects.equals(nom, adresse.nom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, ville, nom, numero, codePostal);
	}

	@Override
	public String toString() {
		return "Adresse{" +
				"id=" + id +
				", ville='" + ville + '\'' +
				", nom='" + nom + '\'' +
				", numero=" + numero +
				", codePostal=" + codePostal +
				'}';
	}
}
