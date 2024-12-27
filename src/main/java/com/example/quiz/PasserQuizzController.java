package com.example.quiz;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quizz;
import com.example.quiz.repositories.QuestionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PasserQuizzController {

    @FXML
    private Label enonceLabelField;

    @FXML
    private ListView<String> reponseListView;

    @FXML
    private Label reponseLabel;

    private Quizz selectedQuizz;

    private Question selectedQuestion;

    private List<String> reponses = new ArrayList<>();

    private List<String> choixReponse = new ArrayList<>();
    private List<String> correctReponses = new ArrayList<>();

    private int globalIndex = 0;

    private int score = 0;
    private final QuestionDAO questionDAO = new QuestionDAO();

    public PasserQuizzController() throws SQLException {
    }

    public void initData(Quizz selectedQuizz) {
        this.selectedQuestion = selectedQuizz.getQuestions().get(globalIndex);
        initialize();
    }

    @FXML
    public void initialize() {
        choixReponse.clear();
        if (selectedQuestion != null) {
            enonceLabelField.setText(selectedQuestion.getEnonce());
            reponseListView.getItems().setAll(selectedQuestion.getChoix());

            StringBuilder indexes = new StringBuilder();
            for (String correctChoice : selectedQuestion.getChoixCorrects()) {
                int index = reponseListView.getItems().indexOf(correctChoice);
                indexes.append(index).append(", ");
            }

            // Remove the trailing comma and space
            if (indexes.length() > 0) {
                indexes.setLength(indexes.length() - 2);
            }

            correctReponses = Arrays.asList(indexes.toString().split(", "));
        }
    }

    public void nextQuestion() {
        int index = selectedQuizz.getQuestions().indexOf(selectedQuestion);
        if (index < selectedQuizz.getQuestions().size() - 1) {
            selectedQuestion = selectedQuizz.getQuestions().get(index + 1);
            initialize();
        }
    }

    public void previousQuesiton() {
        int index = selectedQuizz.getQuestions().indexOf(selectedQuestion);
        if (index > 0) {
            selectedQuestion = selectedQuizz.getQuestions().get(index - 1);
            initialize();
        }
    }

    public double calculateScore() {
        int nbCorrectResponses = 0;
        if (compareLists(reponses, correctReponses)) {
            nbCorrectResponses++;
        }

        score = nbCorrectResponses;

        return score;
    }

    public boolean compareLists(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        List<String> sortedList1 = new ArrayList<>(list1);
        List<String> sortedList2 = new ArrayList<>(list2);

        Collections.sort(sortedList1);
        Collections.sort(sortedList2);

        return sortedList1.equals(sortedList2);
    }

    public void addCorrectResponse() {
        String reponse = reponseListView.getSelectionModel().getSelectedItem();
        if (!choixReponse.contains(reponse)) {
            choixReponse.add(reponse);
        } else {
            choixReponse.remove(reponse);
        }

        StringBuilder indexes = new StringBuilder();
        for (String choice : choixReponse) {
            int index = reponseListView.getItems().indexOf(choice);
            indexes.append(index).append(", ");
        }

        // Remove the trailing comma and space
        if (indexes.length() > 0) {
            indexes.setLength(indexes.length() - 2);
        }

        reponseLabel.setText(indexes.toString());
        reponses = Arrays.asList(indexes.toString().split(", "));
    }

    @FXML
    public void ok() throws IOException {
        calculateScore();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("score-view.fxml"));
        Parent root = loader.load();
        ScoreController scoreController = loader.getController();
        scoreController.setScore(score + "");
        scoreController.setNbQuestions(selectedQuizz.getQuestions().size() + "");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) enonceLabelField.getScene().getWindow();
        stage.close();
    }

    public void setSelectedQuizz(Quizz selectedQuizz) {
        this.selectedQuizz = selectedQuizz;
    }
}
