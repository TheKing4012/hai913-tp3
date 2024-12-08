package org.example.tp3client.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp3client.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class UserServiceCLI extends AbstractMain implements CommandLineRunner {
    @Autowired
    private RestTemplate proxy;

    public static IntegerInputProcessor inputProcessor;

    private UserD client;
    private String jwtToken; // Token JWT pour l'authentification

    public boolean isConnected() {
        return client != null && jwtToken != null;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader inputReader;
        String userInput = "";
        try {
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            setTestServiceUrl(inputReader);
            do {
                menu();
                userInput = inputReader.readLine();
                processUserInput(inputReader, userInput, proxy);

            } while (!userInput.equals(QUIT));
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void menu() {
        System.out.println("Menu principal de l'application");
        System.out.println("0. Quitter");
        if (isConnected()) {
            System.out.println("1. Lister les produits");
            System.out.println("2. Chercher un produit");
            System.out.println("3. Ajouter un produit");
            System.out.println("4. Supprimer un produit");
            System.out.println("5. Modifier un produit");
            System.out.println("6. Afficher les produits les plus chers");
        } else {
            System.out.println("1. Se connecter");
            System.out.println("2. Créer un compte");
            System.out.println("3. Actions Préparées");
        }
    }

    private void processUserInput(BufferedReader reader, String userInput, RestTemplate proxy) {
        Map<String, String> params = new HashMap<>();
        inputProcessor = new IntegerInputProcessor(reader);
        try {
            switch (userInput) {

                case "1":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products";
                        System.out.println("Liste des produits:");

                        // Création du header avec le token JWT
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + jwtToken);

                        // Création de l'entité HTTP avec les headers
                        HttpEntity<String> entity = new HttpEntity<>(headers);

                        // Utilisation du RestTemplate pour faire la requête GET avec les headers
                        RestTemplate restTemplate = new RestTemplate();
                        Product[] products = restTemplate.exchange(url, HttpMethod.GET, entity, Product[].class).getBody();

                        // Affichage des produits
                        Arrays.asList(products).forEach(System.out::println);
                    } else {
                        System.out.println("Veuillez entrer votre mail client:");
                        String mail = reader.readLine();
                        System.out.println("Veuillez entrer votre mot de passe:");
                        String mdp = reader.readLine();

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setEmail(mail);
                        loginRequest.setPassword(mdp);

                        String url = "http://localhost:30007/userservice/api/connect";
                        try {
                            UserD result = proxy.postForObject(url, loginRequest, UserD.class);
                            if (result != null) {
                                this.client = result;
                                // Stocker le token pour les futures requêtes
                                this.jwtToken = result.getToken();
                                System.out.println("Connexion réussie !");
                                System.out.println("Token: " + jwtToken);
                            } else {
                                System.out.println("Échec de la connexion, veuillez réessayer");
                            }
                        } catch (HttpClientErrorException e) {
                            System.err.println("Erreur de connexion: " + e.getMessage());
                        }
                    }
                    break;

                case "2":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products";
                        System.out.println("Veuillez entrer l'id de votre produit:");
                        String id = reader.readLine();
                        url = url + "/" + id;

                        // Ajout du token dans l'en-tête Authorization
                        String authHeader = "Bearer " + jwtToken;
                        Product product = proxy.getForObject(url, Product.class, Collections.singletonMap("Authorization", authHeader));
                        System.out.println("Produit trouvé:");
                        System.out.println(product);
                    } else {
                        // Créer un compte
                        System.out.println("Veuillez entrer votre pseudonyme:");
                        String pseudo = reader.readLine();
                        System.out.println("Veuillez entrer votre email:");
                        String email = reader.readLine();
                        System.out.println("Veuillez entrer votre mot de passe:");
                        String password = reader.readLine();

                        // Créer un objet UserD avec l'email et le mot de passe
                        UserD newUser = new UserD(pseudo, email, password);

                        // URL du serveur pour créer un compte
                        String url = "http://localhost:30007/userservice/api/create";

                        try {
                            // Envoi de la requête POST avec l'objet newUser dans le corps
                            UserD result = proxy.postForObject(url, newUser, UserD.class);

                            if (result != null) {
                                System.out.println("Compte créé avec succès !");
                            } else {
                                System.out.println("Erreur lors de la création du compte !");
                            }
                        } catch (HttpClientErrorException e) {
                            System.err.println("Erreur lors de la création du compte: " + e.getMessage());
                        }
                    }
                    break;

                case "3":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products";
                        Product p = new Product();
                        System.out.println("Veuillez entrer le nom du produit:");
                        p.setNom(reader.readLine());
                        System.out.println("Veuillez entrer le prix du produit:");
                        p.setPrix(Double.valueOf(reader.readLine()));
                        System.out.println("Veuillez entrer la date d'expiration du produit (YYYY-MM-DD):");
                        p.setDateExp(reader.readLine());

                        // Création des en-têtes avec le JWT
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + jwtToken);

                        // Entité HTTP contenant les en-têtes et le produit à ajouter
                        HttpEntity<Product> entity = new HttpEntity<>(p, headers);

                        // Appel de l'API pour créer le produit
                        RestTemplate restTemplate = new RestTemplate();
                        Product response = restTemplate.postForObject(url, entity, Product.class);

                        System.out.println("Le produit a été ajouté: " + response);
                    } else {
                        String userServiceUrl = "http://localhost:30007/userservice/api/connect";
                        String productServiceUrl = "http://localhost:30007/productservice/api/products";
                        String top5ProductsUrl = productServiceUrl + "/top5";

                        // Liste des utilisateurs à connecter
                        List<UserD> users = List.of(
                                new UserD("toto", "toto@toto", "toto"),
                                new UserD("tata", "tata@tata", "tata"),
                                new UserD("tutu", "tutu@tutu", "tutu"),
                                new UserD("test", "test@test", "test"),
                                new UserD("donald", "donald@gmail.com", "donald"),
                                new UserD("romain", "romain@gmail.com", "romain"),
                                new UserD("richard", "richard.picole@rpodev.fr", "richard"),
                                new UserD("magali", "magali@gmail.com", "magali"),
                                new UserD("sebastien", "sebastien@gmail.com", "sebastien"),
                                new UserD("quentin", "quentin@gmail.com", "quentin")
                        );

                        // Map pour stocker les tokens des utilisateurs
                        Map<String, String> userTokens = new HashMap<>();

                        // Connexion des utilisateurs et collecte des tokens
                        for (UserD user : users) {
                            try {
                                LoginRequest loginRequest = new LoginRequest();
                                loginRequest.setEmail(user.getEmail());
                                loginRequest.setPassword(user.getPassword());

                                ResponseEntity<UserD> response = proxy.postForEntity(userServiceUrl, loginRequest, UserD.class);
                                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getToken() != null) {
                                    String token = response.getBody().getToken();
                                    userTokens.put(user.getEmail(), token);
                                    System.out.println("Utilisateur connecté : " + user.getEmail());
                                } else {
                                    System.out.println("Échec de connexion pour : " + user.getEmail());
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors de la connexion de " + user.getEmail() + ": " + e.getMessage());
                            }
                        }

                        // Requêtes avec variation des opérations
                        Random random = new Random();

                        for (Map.Entry<String, String> entry : userTokens.entrySet()) {
                            String email = entry.getKey();
                            String token = entry.getValue();

                            // Décision aléatoire pour chaque type d'opération
                            boolean doGetProducts = random.nextBoolean();
                            boolean doAddProduct = random.nextBoolean();
                            boolean doGetTop5 = random.nextBoolean();

                            // Récupération des produits pour cet utilisateur
                            if (doGetProducts) {
                                try {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.set("Authorization", "Bearer " + token);

                                    HttpEntity<String> entity = new HttpEntity<>(headers);
                                    ResponseEntity<Product[]> response = proxy.exchange(productServiceUrl, HttpMethod.GET, entity, Product[].class);

                                    System.out.println("Produits récupérés pour " + email + ":");
                                    if (response.getBody() != null) {
                                        for (Product product : response.getBody()) {
                                            System.out.println(product);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.err.println("Erreur pour " + email + " lors de la récupération des produits : " + e.getMessage());
                                }
                            }

                            // Ajout d'un produit
                            if (doAddProduct) {
                                try {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.set("Authorization", "Bearer " + token);
                                    headers.setContentType(MediaType.APPLICATION_JSON);

                                    Product newProduct = new Product("ProduitTest-" + email, 50.0, "2025-12-31");
                                    HttpEntity<Product> entity = new HttpEntity<>(newProduct, headers);

                                    ResponseEntity<Product> response = proxy.postForEntity(productServiceUrl, entity, Product.class);
                                    if (response.getStatusCode() == HttpStatus.CREATED) {
                                        System.out.println("Produit ajouté pour " + email + ": " + response.getBody());
                                    }
                                } catch (Exception e) {
                                    System.err.println("Erreur pour " + email + " lors de l'ajout d'un produit : " + e.getMessage());
                                }
                            }

                            // Recherche des 5 produits les plus chers
                            if (doGetTop5) {
                                try {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.set("Authorization", "Bearer " + token);

                                    HttpEntity<String> entity = new HttpEntity<>(headers);
                                    ResponseEntity<Product[]> response = proxy.exchange(top5ProductsUrl, HttpMethod.GET, entity, Product[].class);

                                    System.out.println("Top 5 produits les plus chers pour " + email + ":");
                                    if (response.getBody() != null) {
                                        for (Product product : response.getBody()) {
                                            System.out.println(product);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.err.println("Erreur pour " + email + " lors de la recherche des produits les plus chers : " + e.getMessage());
                                }
                            }
                        }
                    }
                    break;


                case "4":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products";
                        System.out.println("Veuillez entrer l'id de votre produit:");
                        String id = reader.readLine();
                        url = url + "/" + id;

                        // Création des en-têtes avec le JWT
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + jwtToken);

                        // Entité HTTP contenant uniquement les en-têtes
                        HttpEntity<Void> entity = new HttpEntity<>(headers);

                        // Appel de l'API pour supprimer le produit
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

                        System.out.println("Le produit a été supprimé.");
                    } else {
                        System.out.println("Veuillez vous connecter avant de continuer.");
                    }
                    break;


                case "5":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products";
                        Product p = new Product();
                        System.out.println("Veuillez entrer l'id de votre produit:");
                        String id = reader.readLine();
                        url = url + "/" + id;
                        p.setId(Integer.valueOf(id));
                        System.out.println("Veuillez entrer le nom du produit:");
                        p.setNom(reader.readLine());
                        System.out.println("Veuillez entrer le prix du produit:");
                        p.setPrix(Double.valueOf(reader.readLine()));
                        System.out.println("Veuillez entrer la date d'expiration du produit (YYYY-MM-DD):");
                        p.setDateExp(reader.readLine());

                        // Création des en-têtes avec le JWT
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + jwtToken);

                        // Entité HTTP contenant les en-têtes et le produit à modifier
                        HttpEntity<Product> entity = new HttpEntity<>(p, headers);

                        // Appel de l'API pour mettre à jour le produit
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

                        System.out.println("Le produit a été modifié.");
                    } else {
                        System.out.println("Veuillez vous connecter avant de continuer.");
                    }
                    break;


                case "6":
                    if (isConnected()) {
                        String url = "http://localhost:30007/productservice/api/products/top5";

                        // Création des en-têtes avec le JWT
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + jwtToken);

                        // Entité HTTP contenant les en-têtes
                        HttpEntity<String> entity = new HttpEntity<>(headers);

                        // Appel de l'API pour récupérer les 5 produits les plus chers
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<List<Product>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                                new ParameterizedTypeReference<List<Product>>() {});

                        // Vérification de la réponse et affichage des produits
                        if (response.getStatusCode() == HttpStatus.OK) {
                            List<Product> top5Products = response.getBody();
                            System.out.println("Top 5 produits les plus chers :");
                            if (top5Products != null) {
                                for (Product product : top5Products) {
                                    System.out.println(product);
                                }
                            }
                        } else {
                            System.out.println("Erreur lors de la récupération des produits : " + response.getStatusCode());
                        }

                    } else {
                        System.out.println("Veuillez vous connecter avant de continuer.");
                    }
                    break;


                case QUIT:
                    System.out.println("Revenez bientot nous voir !");
                    return;
                default:
                    System.err.println("Erreur: veuillez réessayer");
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            System.err.println(e.getMessage());
        }
    }
}
