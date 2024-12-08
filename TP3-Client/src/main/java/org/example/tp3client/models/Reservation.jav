package org.example.tp3client.models;

import java.time.LocalDate;
import java.util.Objects;


public class Reservation {

	private long id;
	private Client client;
	private LocalDate in;
	private LocalDate out;
	private double amount;
	private Chambre chambre;
	@SuppressWarnings("unused")
	private Hotel hotel;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
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
		
	public Reservation(Client client, LocalDate in, LocalDate out, double amount, Chambre chambre, Hotel hotel) {
		this.client = client;
		this.in = in;
		this.out = out;
		this.amount = amount;
		this.chambre = chambre;
		this.hotel = hotel;
	}
		
	
	public Reservation() {
		this.client = new Client();
		this.in = LocalDate.parse("2000-01-01");
		this.out = LocalDate.parse("2000-01-01");
		this.amount = 0;
		this.hotel = new Hotel();
		this.chambre = new Chambre();
	}


	public static String formRecipe(int size, String element) {
		String display = "";
		String firstLine = "+";
		String middleLine = "|";
		int wSpace = size - 2;

		if(element.equals("footer")) {
			display = "+";
			for(int i = 0; i < wSpace; i++) {
				display = display + "-"; 
			}
			return display + "+" + "\n";
		}

		for(int i = 0; i < wSpace; i++) {
			firstLine = firstLine + "-";
		}
		firstLine = firstLine + "+";
		String endLine = firstLine;
		firstLine = firstLine + "\n";
		wSpace = wSpace - element.length();
		int edges = 0;

		if(wSpace % 2 == 0) {
			edges = wSpace/2;
			String separators = "";
			for(int k = 0; k < edges; k++) {
				separators = separators + " ";
			}
			middleLine = middleLine + separators + element + separators + "|" + "\n";
		} else if (wSpace % 2 != 0) {
			edges = wSpace/2;
			String separators = "";
			for(int k = 0; k < edges; k++) {
				separators = separators + " ";
			}
			middleLine = middleLine + separators + " " + element + separators + "|" + "\n";
		}

		display = firstLine + middleLine + endLine;
		return display;
	}

	public static String adaptiveDisplay(String choice, String element, int size) {
		int wSpace = 0;
		String startLine = "| ";

		if(choice.equals("hotelName")) {
			String endLine = "";
			startLine = startLine + "Hotel : ";
			wSpace = (size - 11) - element.length();
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		} else if(choice.equals("room")) {
			String endLine = "";
			startLine = startLine + "Room : ";
			wSpace = (size - 10) - element.length();
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		} else if (choice.equals("datein")) {
			String endLine = "";
			startLine = startLine + "Arrival Date : ";
			wSpace = (size - 18) - element.length();
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		} else if(choice.equals("dateout")) {
			String endLine = "";
			startLine = startLine + "Departure Date : ";
			wSpace = (size - 20) - element.length();
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		} else if(choice.equals("price")) {
			String endLine = "";
			startLine = startLine + "Price : ";
			wSpace = (size - 11) - element.length();
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		} else if(choice.equals("info")) {
			wSpace = (size - 3) - element.length();
			String endLine = "";
			for(int i = 0; i < wSpace; i++) {
				endLine = endLine + " ";
			}
			endLine = endLine + "|";
			String display = startLine + element + endLine;
			return display;
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reservation that = (Reservation) o;
		return id == that.id && Double.compare(amount, that.amount) == 0 && Objects.equals(client, that.client) && Objects.equals(in, that.in) && Objects.equals(out, that.out) && Objects.equals(chambre, that.chambre) && Objects.equals(hotel, that.hotel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, client, in, out, amount, chambre, hotel);
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"id=" + id +
				", client='" + client + '\'' +
				", in=" + in +
				", out=" + out +
				", amount=" + amount +
				", chambre=" + chambre +
				", hotel=" + hotel +
				'}';
	}
}
