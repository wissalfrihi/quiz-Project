package com.example.quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AcceuilleController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void handleLogin() {
        switchToAnotherScene("login-view.fxml");
    }

    @FXML
    protected void handleSubscribe() {
        switchToAnotherScene("subscribe-view.fxml");
    }

    private void switchToAnotherScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            if (loader.getController() instanceof LoginController) {
                LoginController loginController = loader.getController();
                loginController.setPrimaryStage(primaryStage);
            }

            if (loader.getController() instanceof SubscribeController) {
                SubscribeController subscribeController = loader.getController();
                subscribeController.setPrimaryStage(primaryStage);
            }

            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}