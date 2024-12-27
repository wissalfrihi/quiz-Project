package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UpdateEtudiantController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private User selectedEtudiant;

    private final UserDAO userDAO = new UserDAO();

    public void initData(User selectedEtudiant) {
        this.selectedEtudiant = selectedEtudiant;
        initialize();
    }

    @FXML
    public void initialize() {
        if (selectedEtudiant != null) {
            firstNameField.setText(selectedEtudiant.getPrenom());
            lastNameField.setText(selectedEtudiant.getNom());
            emailField.setText(selectedEtudiant.getEmail());
            passwordField.setText(selectedEtudiant.getMotDePasse());
        }
    }

        @FXML
    public void ok(){
        User etudiant = new User();
        etudiant.setId(selectedEtudiant.getId());
        etudiant.setPrenom(firstNameField.getText());
        etudiant.setNom(lastNameField.getText());
        etudiant.setEmail(emailField.getText());
        etudiant.setMotDePasse(passwordField.getText());
        etudiant.setRole("ETUDIANT");
        userDAO.updateUser(etudiant);
        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
