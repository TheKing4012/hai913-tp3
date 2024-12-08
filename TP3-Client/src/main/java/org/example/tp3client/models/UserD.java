package org.example.tp3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserD {

    // Attributes
    private long id;
    private String nom;
    private String email;
    private String password;
    private String token;  // Ajout du champ pour le token

    // Getters et Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;  // Getter pour le token
    }

    public void setToken(String token) {
        this.token = token;  // Setter pour le token
    }

    // Constructeurs et autres méthodes...


    public UserD(String nom, String email, String password) {
        this.nom = nom;
        this.email = email;
        this.password = password;
    }


    public UserD() {
        this.nom = "Undefined";
        this.email = "Undefined";
        this.password = "Undefined";
    }

    @Override
    public String toString() {
        return "UserD{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserD userD = (UserD) o;
        return id == userD.id && Objects.equals(nom, userD.getNom()) && Objects.equals(email, userD.email) && Objects.equals(password, userD.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, email, password);
    }

}
