package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Group;
import com.example.quiz.model.Question;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.GroupDAO;
import com.example.quiz.repositories.QuestionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UpdateQuestionController {
    @FXML
    private TextField reponseTextField;

    @FXML
    private TextField enonceTextField;

    @FXML
    private ListView<String> reponseListView;

    @FXML
    private Label choixCorrectsLabel;

    @FXML
    private ComboBox difficulteField;

    @FXML
    private ComboBox coursNomsField;

    private Question selectedQuestion;

    private Question questionToUpdate = new Question();

    private List<Cours> coursList;

    private final CoursDAO coursDAO = new CoursDAO();

    private final QuestionDAO questionDAO = new QuestionDAO();

    public UpdateQuestionController() throws SQLException {
    }

    public void initData(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
        initialize();
    }

    @FXML
    public void initialize() {
        if (selectedQuestion != null) {
            enonceTextField.setText(selectedQuestion.getEnonce());
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

            choixCorrectsLabel.setText(indexes.toString());

            coursList = coursDAO.getAllCours();
            coursNomsField.getItems().setAll(FXCollections.observableArrayList(coursList.stream().map(Cours::getNom).toList()));
            coursNomsField.setValue(selectedQuestion.getCours().getNom());

            difficulteField.getItems().setAll(Difficulte.values());
            difficulteField.setValue(selectedQuestion.getDifficulte());
        }
    }

    public void addReponseToList() {
        String reponse = reponseTextField.getText().trim();
        if (!reponse.isEmpty()) {
            reponseListView.getItems().add(reponse);
            List<String> choix = new ArrayList<>(selectedQuestion.getChoix());
            choix.add(reponse);
            selectedQuestion.setChoix(choix);
            reponseTextField.clear();
        }
    }

    public void addCorrectResponse() {
        List<String> choixCorrects = new ArrayList<>(selectedQuestion.getChoixCorrects());
        String reponse = reponseListView.getSelectionModel().getSelectedItem();
        if (reponse != null && !reponse.isEmpty() &&!selectedQuestion.getChoixCorrects().contains(reponse)) {
            choixCorrects.add(reponse);
        } else {
            choixCorrects.remove(reponse);
        }
        selectedQuestion.setChoixCorrects(choixCorrects);

        StringBuilder indexes = new StringBuilder();
        for (String correctChoice : selectedQuestion.getChoixCorrects()) {
            int index = reponseListView.getItems().indexOf(correctChoice);
            indexes.append(index).append(", ");
        }

        // Remove the trailing comma and space
        if (indexes.length() > 0) {
            indexes.setLength(indexes.length() - 2);
        }

        choixCorrectsLabel.setText(indexes.toString());

    }

    @FXML
    public void ok() throws SQLException {
        selectedQuestion.setEnonce(enonceTextField.getText());
        selectedQuestion.setDifficulte((Difficulte) difficulteField.getValue());
        selectedQuestion.setCours(coursList.stream().filter(c -> c.getNom().equals(coursNomsField.getValue())).findFirst().orElse(null));
        selectedQuestion.setChoix(selectedQuestion.getChoix());
        selectedQuestion.setChoixCorrects(selectedQuestion.getChoixCorrects());
        System.out.println(selectedQuestion);
        questionDAO.updateQuestion(selectedQuestion);

        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) enonceTextField.getScene().getWindow();
        stage.close();
    }
}
