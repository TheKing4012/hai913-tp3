package org.example.tp3client.models;


import jakarta.persistence.*;

@Entity
public class Offres {
	@Id
	@GeneratedValue
	private long id;
	private long numHotel;
	private String uri;
	private double amount;
	@ManyToOne
	@JoinColumn(name="agency_id")
	private Agence agence;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public long getNumHotel() {
		return numHotel;
	}
	public void setNumHotel(long numHotel) {
		this.numHotel = numHotel;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Offres(long numHotel, String uri, double amount, Agence agence) {
		this.numHotel = numHotel;
		this.uri = uri;
		this.amount = amount;
		this.agence = agence;
	}
	public Offres() {
		this.numHotel = 0;
		this.uri = "";
		this.amount = 0;
	}
	
	
	@Override
	public String toString() {
		return "Offers [id=" + id + ", numHotel=" + numHotel + ", uri=" + uri + ", amount=" + amount + "]";
	}

		
}
