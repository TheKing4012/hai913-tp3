package com.example.tp3.models;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class Reservation {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	private String client;
	@Column(name = "dateIn")
	private LocalDate in;
	@Column(name = "dateOut")
	private LocalDate out;
	private double amount;
	@ManyToOne
	@JoinColumn(name ="room_id", nullable=true)
	private Chambre chambre;
	@ManyToOne
	@JoinColumn(name="hotel_id", nullable=true)
	private Hotel hotelResa;
	
	
	public void setHotelResa(Hotel hotelResa) {
		this.hotelResa = hotelResa;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public LocalDate getIn() {
		return in;
	}
	public void setIn(LocalDate in) {
		this.in = in;
	}
	public LocalDate getOut() {
		return out;
	}
	public void setOut(LocalDate out) {
		this.out = out;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Chambre getRoom() {
		return chambre;
	}
	public void setRoom(Chambre chambre) {
		this.chambre = chambre;
	}
		
	public Reservation(String client, LocalDate in, LocalDate out, double amount, Chambre chambre, Hotel hotel) {
		this.client = client;
		this.in = in;
		this.out = out;
		this.amount = amount;
		this.chambre = chambre;
		this.hotelResa = hotel;
	}
		
	
	public Reservation() {
		this.client = "null";
		this.in = LocalDate.parse("2000-01-01");
		this.out = LocalDate.parse("2000-01-01");
		this.amount = 0;
		this.chambre = new Chambre();
		this.hotelResa = new Hotel();
	}
	@Override
	public String toString() {
		return "Reservation : " + client + "room n°" + chambre + "\n"+
				"From " + in + " to " + out + "\n";
	}
}
