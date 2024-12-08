package org.example.tp3client.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp3client.exceptions.ReservationException;
import org.example.tp3client.functions.MainFunctions;
import org.example.tp3client.models.Chambre;
import org.example.tp3client.models.Client;
import org.example.tp3client.models.Hotel;
import org.example.tp3client.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class HotelServiceCLI extends AbstractMain implements CommandLineRunner {
    @Autowired
    private RestTemplate proxy;
    private static Map<String, String> URIS;
    private static String URI_HOTEL;
    private static String URI_HOTEL_ID;

    public static IntegerInputProcessor inputProcessor;

    private Client client;

    public boolean isConnected() {
        return client == null;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader inputReader;
        String userInput = "";
        try {
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            setTestServiceUrl(inputReader);
            URI_HOTEL = "hotels";
            URI_HOTEL_ID = URI_HOTEL + "/{id}";
            URIS = new HashMap<String, String>();
            URIS.put(SERVICE_URL1 + "hotels", SERVICE_URL1 + URI_HOTEL_ID);
            URIS.put(SERVICE_URL2 + "hotels", SERVICE_URL2 + URI_HOTEL_ID);
            URIS.put(SERVICE_URL3 + "hotels", SERVICE_URL3 + URI_HOTEL_ID);
            do {
                menu();
                userInput = inputReader.readLine();
                processUserInput(inputReader, userInput, proxy);

            } while (!userInput.equals(QUIT));
            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void menu() {
        System.out.println("Menu principal de l'application");
        System.out.println("0. Quitter");
        System.out.println("1. Voir tous les hotels partenaires");
        System.out.println("2. Rechercher un hotel");
        System.out.println("3. Voir toutes les agences");
        System.out.println("4. Se connecter");
    }

    private void processUserInput(BufferedReader reader, String userInput, RestTemplate proxy) {
        Map<String, String> params = new HashMap<>();
        inputProcessor = new IntegerInputProcessor(reader);
        try {
            switch(userInput) {
                case "1":
                    int x = 0;
                    for (String uri : URIS.keySet()) {
                        try {
                            String uriCount = uri + "/count";
                            ObjectMapper mapper = new ObjectMapper();
                            String countStr = proxy.getForObject(uriCount, String.class);
                            long count = (int)mapper.readValue(countStr, Map.class).get("count");
                            x += count;
                        }
                        catch (Exception e) {
                            continue;
                        }
                    }
                    System.out.println(String.format("Il y a %d hotels partenaires:", x));
                    for (String uri : URIS.keySet()) {
                        try {
                            Hotel[] hotels = proxy.getForObject(uri, Hotel[].class);
                            Arrays.asList(hotels).forEach(System.out::println);
                        }

                        catch (Exception e) {
                            continue;
                        }
                    }
                    break;

                case "2":
                    System.out.println("Dans quelle ville souhaitez vous partir ?\n");
                    String position = reader.readLine();
                    System.out.println("\nCombien d'étoiles souhaitez vous pour votre hotel ? ");
                    double nbEtoiles = Double.parseDouble(reader.readLine());
                    System.out.println("\nQuel prix recherchez vous ? ");
                    double prix = Double.parseDouble(reader.readLine());
                    System.out.println("\nDate d'arrivée: ");
                    System.out.println("Annee");
                    int annee = inputProcessor.process();
                    System.out.println("Mois");
                    int mois = inputProcessor.process();
                    System.out.println("Jour");
                    int jour = inputProcessor.process();
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
                    String dateDebut = LocalDate.of(annee, mois, jour).format(formatter);
                    System.out.println("\nDate de départ: ");
                    System.out.println("Annee");
                    annee = inputProcessor.process();
                    System.out.println("Mois");
                    mois = inputProcessor.process();
                    System.out.println("Jour");
                    jour = inputProcessor.process();
                    String dateDepart = LocalDate.of(annee, mois, jour).format(formatter);
                    System.out.println("\nNombre de personnes: ");
                    int nbLit = Integer.parseInt(reader.readLine());
                    params.put("adresse", position);
                    params.put("datedebut", dateDebut);
                    params.put("datefin", dateDepart);
                    params.put("size", String.valueOf(nbLit));
                    params.put("nbetoiles", String.valueOf(nbEtoiles));
                    params.put("prix", String.valueOf(prix));

                    List<Hotel> resultHotel = new ArrayList<>();
                    int cpt = 1;
                    ArrayList<String> uriList = new ArrayList<>();
                    System.out.println("Results:\n");
                    for (String uri : URIS.keySet()) {
                        try {
                            String url = uri + "/search?adresse={adresse}&nblit={nblit}&nbetoiles={nbetoiles}&datedebut={datedebut}&datefin={datefin}&prix={prix}";
                            Hotel returnedHotel = proxy.getForObject(url, Hotel.class, params);
                            if(!returnedHotel.getNom().equals("Undefined")) {
                                uriList.add(uri);
                                resultHotel.add(returnedHotel);
                                System.out.println("Hotel n°"+ String.valueOf(cpt));
                                cpt++;
                                System.out.println(returnedHotel.toString());
                                for (Chambre chambre: returnedHotel.getChambres()) {
                                    System.out.println(chambre.toString());
                                }
                                System.out.println();
                            }
                        }
                        catch (Exception e) {
                            continue;
                        }
                    }

                    System.out.println("Voulez-vous réserver une de ces chambres ?\n");
                    String reponse = reader.readLine();
                    if(reponse.equalsIgnoreCase("oui")) {
                        if (this.isConnected()) {
                            int hotelChoice = -2;
                            int roomChoice = 0;
                            while (hotelChoice == -2) {
                                System.out.println("Numero de l'hotel (-1 pour quitter): ");
                                hotelChoice = Integer.parseInt(reader.readLine());
                                if (hotelChoice == -1) {
                                    System.out.println("Retour au menu principal");
                                    break;
                                } else if (hotelChoice > resultHotel.size() || hotelChoice <= -1) {
                                    System.err.println("Mauvais numero d'hotel veuillez réessayer");
                                    hotelChoice = -2;
                                } else {
                                    System.out.println("Numero de chambre : ");
                                    roomChoice = Integer.parseInt(reader.readLine());
                                }
                            }
                            LocalDate ind = LocalDate.parse(dateDebut);
                            LocalDate outd = LocalDate.parse(dateDepart);
                            if (hotelChoice != 0 && roomChoice != 0) {
                                try {
                                    Hotel selectedHotel = resultHotel.get(hotelChoice - 1);
                                    Chambre selectedRoom = selectedHotel.getChambres().get(roomChoice - 1);
                                    Reservation resa = MainFunctions.makeReservation(reader, ind, outd, selectedRoom, selectedHotel, selectedRoom.getPrix());
                                    selectedHotel.setListeReservations(new ArrayList<Reservation>());
                                    selectedHotel.getListeReservations().add(resa);
                                    params.put("id", String.valueOf(selectedHotel.getId()));
                                    String uriID = URIS.get(uriList.get(hotelChoice - 1));
                                    proxy.put(uriID, selectedHotel, params);
                                    System.out.println("Votre réservation vient d'etre pris en compte !\n");

                                } catch (ReservationException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Veuillez vous connecter avant de faire une réservation !\n");
                        }
                    }

                    break;

                case "3":
                    String url = "http://localhost:30090/hotelorg/api" + "/agency/connect";
                    System.out.println("Veuillez entrer votre nom client:");
                    String nom = reader.readLine();
                    System.out.println("Veuillez entrer votre mot de passe:");
                    String mdp = reader.readLine();

                    params.put("nom", nom);
                    params.put("mdp", mdp);
                    System.out.println("Tentative connection a votre compte");
                    System.out.println(url);
                    Client resultat = proxy.getForObject(url, Client.class, params);

                    if(resultat != null) {
                        this.client = resultat;
                        System.out.println("Connexion reussie !");
                    } else {
                        System.out.println("Echec lors du processus de connexion veuillez réessayer");
                    }

                case QUIT:
                    System.out.println("Revenez bientot nous voir !");
                    return;
                default:
                    System.err.println("Erreur: veuillez réessayer");
                    return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (HttpClientErrorException e) {
            System.err.println(e.getMessage());
        }
    }
}
