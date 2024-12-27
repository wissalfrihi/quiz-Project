package com.example.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Quizz {
    private int id;
    private List<Question> questions = new ArrayList<>();
    private Cours cours;
    private int nombreQuestions;
    private Difficulte difficulte;

    public Quizz(List<Question> questions, Cours cours, int nombreQuestions, Difficulte difficulte) {
        this.questions = questions;
        this.cours = cours;
        this.nombreQuestions = nombreQuestions;
        this.difficulte = difficulte;
    }

    public Quizz(){
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Cours getCours() {
        return cours;
    }

    public int getNombreQuestions() {
        return nombreQuestions;
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public void setNombreQuestions(int nombreQuestions) {
        this.nombreQuestions = nombreQuestions;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public String toString() {
        return "Quizz{" +
                "id=" + id +
                ", questions=" + questions +
                ", cours=" + cours +
                ", nombreQuestions=" + nombreQuestions +
                ", difficulte=" + difficulte +
                '}';
    }
}
