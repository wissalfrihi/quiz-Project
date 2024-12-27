package com.example.quiz.model;

import java.util.List;

public class Enseignant extends User {
    private static final String ROLE = "ENSEIGNANT";
    private List<Question> questions;

    public Enseignant(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse);
    }

    public Enseignant(){
        super();
    }

    public Quizz genererQuizz(Cours cours, Difficulte difficulte, int nombreQuestions) {
        List<Question> questionsQuizz = questions.stream()
                .filter(question -> question.getCours().equals(cours) && question.getDifficulte().equals(difficulte))
                .limit(nombreQuestions)
                .toList();

        return new Quizz(questionsQuizz, cours, nombreQuestions, difficulte);
    }

}
