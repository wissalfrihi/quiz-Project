package com.example.quiz;

import com.example.quiz.model.Group;
import com.example.quiz.repositories.GroupDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class GroupController {
    @FXML
    private ListView<Group> groups;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private Group selectedGroup;

    private final GroupDAO groupDAO = new GroupDAO();

    @FXML
    public void initialize() {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        groups.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
    }

    @FXML
    void AddnewGroup() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-group-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        selectedGroup = groups.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() {
        groupDAO.deleteGroup(selectedGroup.getId());
        groups.getItems().remove(selectedGroup);
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-group-view.fxml"));
        Parent root = loader.load();
        UpdateGroupController controller = loader.getController();
        controller.initData(selectedGroup);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}