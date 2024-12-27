package com.example.quiz.model;

public class Administrateur extends User {
    private static final String ROLE = "ADMINISTRATEUR";
    public Administrateur(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse, "ADMINISTRATEUR");
    }

    public void affecterEnseignantACours(Enseignant enseignant, Cours cours) {
        cours.getEnseignant().add(enseignant);
    }

    public void affecterEnseignantAGroupe(Enseignant enseignant, Group group) {
        group.getEnseignants().add(enseignant);
    }

    public void affecterEtudiantAGroupe(Etudiant etudiant, Group group) {
        group.getEtudiants().add(etudiant);
    }

    // Méthodes pour la gestion des cours, groupes, enseignants et étudiants
}


