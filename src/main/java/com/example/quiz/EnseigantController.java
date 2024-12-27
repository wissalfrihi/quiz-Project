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

public class EnseigantController {
    @FXML
    private ListView<User> enseignants;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private User selectedEnseignant;

    private final UserDAO userDAO = new UserDAO();

    public void initialize() {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        enseignants.setItems(FXCollections.observableArrayList(userDAO.getAllUsersByRole("ENSEIGNANT")));
    }

    @FXML
    void AddnewEnseigant() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-enseigant-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        selectedEnseignant = enseignants.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() {
        userDAO.deleteUser(selectedEnseignant.getId());
        enseignants.getItems().remove(selectedEnseignant);
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-enseigant-view.fxml"));
        Parent root = loader.load();
        UpdateEnseigantController controller = loader.getController();
        controller.initData(selectedEnseignant);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}