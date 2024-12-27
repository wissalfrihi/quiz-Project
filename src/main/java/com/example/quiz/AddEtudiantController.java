package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEtudiantController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private final UserDAO userDAO = new UserDAO();

        @FXML
    public void ok() {
        User etudiant = new User();
        etudiant.setPrenom(firstNameField.getText());
        etudiant.setNom(lastNameField.getText());
        etudiant.setEmail(emailField.getText());
        etudiant.setMotDePasse(passwordField.getText());
        etudiant.setRole("ETUDIANT");
        userDAO.addUser(etudiant);
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
