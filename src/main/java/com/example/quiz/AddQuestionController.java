package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Question;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.QuestionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class AddQuestionController {
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

    private List<Cours> coursList;

    private Question questionToAdd = new Question();

    private final QuestionDAO questionDAO = new QuestionDAO();

    private final CoursDAO coursDAO = new CoursDAO();

    @FXML
    public void initialize() {
        difficulteField.getItems().setAll(Difficulte.values());

        coursList = coursDAO.getAllCours();
        coursNomsField.getItems().setAll(FXCollections.observableArrayList(coursList.stream().map(Cours::getNom).toList()));
    }

    public AddQuestionController() throws SQLException {
    }


    public void addReponseToList() {
        String reponse = reponseTextField.getText().trim();
        if (!reponse.isEmpty()) {
            reponseListView.getItems().add(reponse);
            questionToAdd.getChoix().add(reponse);
            reponseTextField.clear();
        }
    }

    public void addCorrectResponse() {
        String reponse = reponseListView.getSelectionModel().getSelectedItem();
        if (reponse != null && !reponse.isEmpty() && !questionToAdd.getChoixCorrects().contains(reponse)) {
            questionToAdd.getChoixCorrects().add(reponse);
            System.out.println("reponse correcte ajoutée" + questionToAdd.getChoixCorrects());
        } else {
            questionToAdd.getChoixCorrects().remove(reponse);
            System.out.println("reponse correcte supprimé" + questionToAdd.getChoixCorrects());
        }

        StringBuilder indexes = new StringBuilder();
        for (String correctChoice : questionToAdd.getChoixCorrects()) {
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
        Question question = new Question();
        question.setEnonce(enonceTextField.getText());
        question.setDifficulte((Difficulte) difficulteField.getValue());
        Cours cours = coursList.stream().filter(c -> c.getNom().equals(coursNomsField.getValue())).findFirst().orElse(null);
        question.setCours(cours);
        question.setChoix(questionToAdd.getChoix());
        question.setChoixCorrects(questionToAdd.getChoixCorrects());
        System.out.println(question);
        questionDAO.createQuestion(question);
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
