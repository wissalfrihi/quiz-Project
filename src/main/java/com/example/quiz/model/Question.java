package com.example.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String enonce;
    private List<String> choix = new ArrayList<>();

    private List<String> choixCorrects = new ArrayList<>();

    private Cours cours;
    private Difficulte difficulte;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnonce() {
        return enonce;
    }

    public List<String> getChoix() {
        return choix;
    }

    public List<String> getChoixCorrects() {
        return choixCorrects;
    }

    public Cours getCours() {
        return cours;
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public void setChoix(List<String> choix) {
        this.choix = choix;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public void setChoixCorrects(List<String> choixCorrects) {
        this.choixCorrects = choixCorrects;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String toString() {
        return
                "enonce='" + enonce + '\'' +
                ", choix=" + choix +
                ", cours=" + cours +
                ", difficulte=" + difficulte;
    }

}
