package org.example.tp3client.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Agence {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	private String agencyName;
	@OneToMany(mappedBy="agency", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Offres> offers;

	@OneToMany(mappedBy="agency", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Client> clients;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public List<Offres> getOffres() {
		return offers;
	}
	public void setOffres(List<Offres> offers) {
		this.offers = offers;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public Agence(String agencyName, List<Offres> offers) {
		this.agencyName = agencyName;
		this.offers = offers;
	}
	public Agence() {
		this.agencyName = "Hotel name";
		this.offers = new ArrayList<Offres>();
	}
	
	public void quitAgency() {
		System.out.println("Thanks for using TripFinder\n Bye bye...");
		System.exit(0);
	}
	@Override
	public String toString() {
		return "Agence [agencyName=" + agencyName + ", offers=" + offers + "]";
	}
	

}
