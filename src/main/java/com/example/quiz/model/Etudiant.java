package com.example.quiz.model;

import java.util.List;

public class Etudiant extends User {
    private static final String ROLE = "ETUDIANT";
    private Group group;

    public Etudiant(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse);
    }

    public Etudiant(){
        super();
    }

    public List<Quizz> getGroupQuizzes() {
        return group.getQuizzs();
    }

    // Méthode pour passer un quiz
}
