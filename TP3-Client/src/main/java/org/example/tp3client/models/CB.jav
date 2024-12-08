package org.example.tp3client.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Calendar;
import java.util.Date;

@Entity
public class CB {
    @Id
    @GeneratedValue
    long id;
    int code;
    int cvc;
    String date;

    public CB(int code, int cvc, String date) {
        this.code = code;
        this.cvc = cvc;
        this.date = date;
    }

    public CB() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
