package com.example.quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("acceuille-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Set the primaryStage for the AcceuilleController
        AcceuilleController acceuilleController = fxmlLoader.getController();
        acceuilleController.setPrimaryStage(stage);

        stage.setTitle("Quizz App!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}