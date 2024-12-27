package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Question;
import com.example.quiz.model.Quizz;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.QuestionDAO;
import com.example.quiz.repositories.QuizzDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class AddQuizzController {
    @FXML
    private TextField nombreQuestionTextField;

    @FXML
    private ComboBox difficulteField;

    @FXML
    private ComboBox coursNomsField;

    private List<Cours> coursList;

    private final QuizzDAO quizzDAO = new QuizzDAO();

    private final QuestionDAO questionDAO = new QuestionDAO();

    private final CoursDAO coursDAO = new CoursDAO();

    @FXML
    public void initialize() {
        difficulteField.getItems().setAll(Difficulte.values());

        coursList = coursDAO.getAllCours();
        coursNomsField.getItems().setAll(FXCollections.observableArrayList(coursList.stream().map(Cours::getNom).toList()));
    }

    public AddQuizzController() throws SQLException {
    }

    @FXML
    public void ok() throws SQLException {
        Quizz quizz = new Quizz();
        quizz.setNombreQuestions(Integer.parseInt(nombreQuestionTextField.getText()));
        quizz.setDifficulte((Difficulte) difficulteField.getValue());

        Cours cours = coursList.stream().filter(c -> c.getNom().equals(coursNomsField.getValue())).findFirst().orElse(null);
        quizz.setCours(cours);

        List<Question> questions = questionDAO.getQuestionsByCoursIdAndDifficulte(cours.getId(), quizz.getDifficulte());
        if (questions.size() < quizz.getNombreQuestions()) {
            quizz.setNombreQuestions(questions.size());
        }
        questions = questions.subList(0, quizz.getNombreQuestions());
        quizz.setQuestions(questions);

        System.out.println(quizz);
        quizzDAO.createQuizz(quizz);
        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) nombreQuestionTextField.getScene().getWindow();
        stage.close();
    }
}
