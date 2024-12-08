package com.example.tp3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Product {
    // Attributes
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    private String nom;
    private Double prix;
    private String dateExp;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Double getPrix() {
        return prix;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    public String getDateExp() {
        return dateExp;
    }
    public void setDateExp(String dateExp) {
        this.dateExp = dateExp;
    }

    public Product(String nom, Double prix, String dateExp) {
        this.nom = nom;
        this.prix = prix;
        this.dateExp = dateExp;
    }


    public Product() {
        this.nom = "Undefined";
        this.prix = 0.0;
        this.dateExp = "Undefined";
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix='" + prix + '\'' +
                ", dateExp='" + dateExp + '\'' +
                '}';
    }
}
