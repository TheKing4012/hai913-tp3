package org.example.tp3client.functions;


import org.example.tp3client.exceptions.ReservationException;
import org.example.tp3client.models.*;

import java.io.BufferedReader;
import java.time.LocalDate;

public class MainFunctions {
	
	@SuppressWarnings("unused")
	public static Reservation makeReservation(BufferedReader reader, LocalDate in, LocalDate out, Chambre room, Hotel hotel, double amount) throws ReservationException {
		Reservation resa = null;
		try {
			System.out.println("Nom : ");
			String nom = reader.readLine();
			System.out.println("Prenom : ");
			String prenom = reader.readLine();
			System.out.println("Numero Carte : ");
			int code = Integer.parseInt(reader.readLine());
			System.out.println("CVC : ");
			int cvc = Integer.parseInt(reader.readLine());
			System.out.println("Expiration date (yyyy-mm-dd) : ");
			String date = reader.readLine();

			CB cb = new CB(code, cvc, date);
			Client client = new Client(nom, prenom, cb);
			resa = new Reservation(client, in, out, amount, room, hotel);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return resa;
	}
}
