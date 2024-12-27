package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.repositories.CoursDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class CoursController {
    @FXML
    private ListView<Cours> coursList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private Cours selectedCours;

    private final CoursDAO coursDAO = new CoursDAO();

    @FXML
    public void initialize() {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        coursList.setItems(FXCollections.observableArrayList(coursDAO.getAllCoursWithEnseigants()));
    }

    @FXML
    void AddnewCours() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-cours-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        selectedCours = coursList.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() {
        coursDAO.deleteCours(selectedCours.getId());
        coursList.getItems().remove(selectedCours);
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-cours-view.fxml"));
        Parent root = loader.load();
        UpdateCoursController controller = loader.getController();
        controller.initData(selectedCours);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}