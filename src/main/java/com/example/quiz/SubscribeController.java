package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SubscribeController {
    private Stage primaryStage;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    protected void initialize() {
        // Initialize the choice box with options
        ObservableList<String> roles = FXCollections.observableArrayList("ADMINISTRATEUR", "ENSEIGNANT", "ETUDIANT");
        roleChoiceBox.setItems(roles);
    }

    @FXML
    protected void subscribe() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue(); // Get selected role from choice box

        User user = new User(firstName, lastName, email, password, role);

        boolean success = subscribeUser(user);

        if (success) {
            System.out.println("User subscribed successfully.");
        } else {
            System.out.println("Failed to subscribe user.");
        }
    }

    private boolean subscribeUser(User user) {
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.addUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    protected void handleLogin() {
        switchToAnotherScene("login-view.fxml");
    }

    private void switchToAnotherScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Set the primaryStage for the controller of the new scene
            if (loader.getController() instanceof LoginController) {
                LoginController loginController = loader.getController();
                loginController.setPrimaryStage(primaryStage);
            }

            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}

/*
CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        firstName VARCHAR(50),
lastName VARCHAR(50),
email VARCHAR(100),
password VARCHAR(100),
role VARCHAR(50)
);
*/

