package org.example.tp3client.models;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue
    private long id;
    private String nom;
    private String prenom;
    @ManyToOne
    @JoinColumn(name="cb_id")
    private CB cb;

    @ManyToOne
    @JoinColumn(name="agency_id")
    private Agence agence;

    private String motDePasse;

    public Client(String nom, String prenom, CB cb) {
        this.nom = nom;
        this.prenom = prenom;
        this.cb = cb;
    }

    public Client(String nom, String prenom, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
    }

    public Client() {

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public CB getCb() {
        return cb;
    }

    public void setCb(CB cb) {
        this.cb = cb;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
