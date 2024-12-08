package org.example.tp3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    // Attributes
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(nom, product.nom) && Objects.equals(prix, product.prix) && Objects.equals(dateExp, product.dateExp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prix, dateExp);
    }
}
