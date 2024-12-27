package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage primaryStage;

    private User user;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    protected void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userDAO.getUserByEmailAndPassword(email, password);
        if (user != null) {
            System.out.println("User logged in successfully with role " + user.getRole());
            this.user = user;
            switchToAnotherScene("dashboard-view.fxml");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private void switchToAnotherScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Set the primaryStage for the controller of the new scene
            if (loader.getController() instanceof DashboardController) {
                DashboardController dashboardController = loader.getController();
                dashboardController.initData(user);

                dashboardController.setPrimaryStage(primaryStage);
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
