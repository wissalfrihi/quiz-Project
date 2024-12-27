package com.example.quiz;


import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEnseigantController {
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
        User enseignant = new User();
        enseignant.setPrenom(firstNameField.getText());
        enseignant.setNom(lastNameField.getText());
        enseignant.setEmail(emailField.getText());
        enseignant.setMotDePasse(passwordField.getText());
        enseignant.setRole("ENSEIGNANT");
        userDAO.addUser(enseignant);
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
