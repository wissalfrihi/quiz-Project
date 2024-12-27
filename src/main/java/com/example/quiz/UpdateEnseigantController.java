package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UpdateEnseigantController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private User selectedEnseignant;

    private final UserDAO userDAO = new UserDAO();

    public void initData(User selectedEnseignant) {
        this.selectedEnseignant = selectedEnseignant;
        initialize();
    }

    @FXML
    public void initialize() {
        if (selectedEnseignant != null) {
            firstNameField.setText(selectedEnseignant.getPrenom());
            lastNameField.setText(selectedEnseignant.getNom());
            emailField.setText(selectedEnseignant.getEmail());
            passwordField.setText(selectedEnseignant.getMotDePasse());
        }
    }

        @FXML
    public void ok(){
        User enseignant = new User();
        enseignant.setId(selectedEnseignant.getId());
        enseignant.setPrenom(firstNameField.getText());
        enseignant.setNom(lastNameField.getText());
        enseignant.setEmail(emailField.getText());
        enseignant.setMotDePasse(passwordField.getText());
        enseignant.setRole("ENSEIGNANT");
        userDAO.updateUser(enseignant);
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
