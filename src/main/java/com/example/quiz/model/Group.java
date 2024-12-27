package com.example.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private int id;
    private String nom;
    private List<User> enseignants = new ArrayList<>();
    private List<User> etudiants = new ArrayList<>();
    private List<Cours> coursList = new ArrayList<>();

    private List<Quizz> Quizzes = new ArrayList<>();

    public String getNom() {
        return nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<User> getEnseignants() {
        return enseignants;
    }

    public List<User> getEtudiants() {
        return etudiants;
    }

    public List<Quizz> getQuizzs() {
        return Quizzes;
    }

    public List<Cours> getCoursList() {
        return coursList;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEnseignants(List<User> enseignants) {
        this.enseignants = enseignants;
    }

    public void setEtudiants(List<User> etudiants) {
        this.etudiants = etudiants;
    }

    public void setQuizzs(List<Quizz> Quizzes) {
        this.Quizzes = Quizzes;
    }

    public void setCoursList(List<Cours> coursList) {
        this.coursList = coursList;
    }

    public String toString() {
        return
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", enseignants=" + enseignants +
                ", etudiants=" + etudiants +
                ", coursList=" + coursList
                ;

    }

}