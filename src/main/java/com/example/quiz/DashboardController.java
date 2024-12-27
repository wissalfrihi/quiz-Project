package com.example.quiz;

import com.example.quiz.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardController {
    private User user;

    @FXML
    private AnchorPane contentArea;

    private Stage primaryStage;

    @FXML
    private Button coursButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button questionButton;
    @FXML
    private Button quizzButton;
    @FXML
    private Button passerQuizzButton;
    @FXML
    private Button enseignantButton;
    @FXML
    private Button etudiantButton;


    public void initData(User user){
        this.user = user;
        initialize();
    }
    @FXML
    public void initialize() {
        if (user != null && user.getRole().equals("ETUDIANT")) {
            coursButton.setDisable(true);
            groupButton.setDisable(true);
            questionButton.setDisable(true);
            quizzButton.setDisable(true);
            enseignantButton.setDisable(true);
            etudiantButton.setDisable(true);
            passerQuizzButton.setDisable(false);
        } else if (user != null && user.getRole().equals("ENSEIGNANT")) {
            coursButton.setDisable(true);
            groupButton.setDisable(true);
            questionButton.setDisable(false);
            quizzButton.setDisable(false);
            enseignantButton.setDisable(true);
            etudiantButton.setDisable(true);
            passerQuizzButton.setDisable(true);
        } else if (user != null && user.getRole().equals("ADMINISTRATEUR")) {
            coursButton.setDisable(false);
            groupButton.setDisable(false);
            questionButton.setDisable(false);
            quizzButton.setDisable(false);
            enseignantButton.setDisable(false);
            etudiantButton.setDisable(false);
            passerQuizzButton.setDisable(false);
        }
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    protected void toCours() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("cours-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toGroup() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("group-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toQuestion() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("question-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toQuizz() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("quizz-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toPasserQuizz() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("etudiant-quizz-view.fxml"));
        Parent fxml = loader.load();
        EtudiantQuizzController etudiantQuizzController = loader.getController();
        etudiantQuizzController.initData(user);
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toEnseigant() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("enseignants-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    protected void toEtudiant() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("etudiant-view.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}