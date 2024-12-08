package com.example.tp3.models;

import jakarta.persistence.*;


@Entity
public class UserD {
    // Attributes
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    private String nom;
    private String email;
    private String password;
    private String token;

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
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

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
                '}';
    }
}
