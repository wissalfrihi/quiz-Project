package com.example.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Cours {
    private int id;
    private String nom;
    private List<User> enseignant = new ArrayList<>();

    public String getNom() {
        return nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<User> getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(List<User> enseignant) {
        this.enseignant = enseignant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String toString() {
        return "id=" + id + ", nom='" + nom + '\'' + ", enseignant=" + enseignant;
    }

}
