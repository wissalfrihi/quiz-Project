package com.example.quiz;

import com.example.quiz.model.Question;
import com.example.quiz.repositories.GroupDAO;
import com.example.quiz.repositories.QuestionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class QuestionController {
    @FXML
    private ListView<Question> questions;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    private Question selectedQuestion;

    private final QuestionDAO questionDAO = new QuestionDAO();

    public QuestionController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        questions.setItems(FXCollections.observableArrayList(questionDAO.getAllQuestions()));
    }

    @FXML
    void AddnewQuestion() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-question-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        selectedQuestion = questions.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() throws SQLException {
        questionDAO.deleteQuestion(selectedQuestion.getId());
        questions.getItems().remove(selectedQuestion);
    }

    @FXML
    private void onUpdate() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-question-view.fxml"));
        Parent root = loader.load();
        UpdateQuestionController controller = loader.getController();
        controller.initData(selectedQuestion);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}