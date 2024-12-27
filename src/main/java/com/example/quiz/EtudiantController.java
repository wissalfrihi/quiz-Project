package com.example.quiz;

import com.example.quiz.model.User;
import com.example.quiz.repositories.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class EtudiantController {
    @FXML
    private ListView<User> etudiants;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private User selectedEtudiant;

    private final UserDAO userDAO = new UserDAO();

    public void initialize() {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        etudiants.setItems(FXCollections.observableArrayList(userDAO.getAllUsersByRole("ETUDIANT")));
    }

    @FXML
    void AddnewEtudiant() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-etudiant-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        selectedEtudiant = etudiants.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() {
        userDAO.deleteUser(selectedEtudiant.getId());
        etudiants.getItems().remove(selectedEtudiant);
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-etudiant-view.fxml"));
        Parent root = loader.load();
        UpdateEtudiantController controller = loader.getController();
        controller.initData(selectedEtudiant);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}