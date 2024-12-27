package com.example.quiz;

import com.example.quiz.model.Cours;

import com.example.quiz.model.Enseignant;
import com.example.quiz.model.User;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddCoursController {
    @FXML
    private TextField NomCours;

    @FXML
    private ComboBox enseigantsEmailsField;

    @FXML
    private ListView<String> enseigantsTodAddListView;

    private List<User> enseignants = new ArrayList<>();

    private List<User> enseignantsToAdd = new ArrayList<>();

    private final CoursDAO coursDAO = new CoursDAO();

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        enseignants = userDAO.getAllUsersByRole("ENSEIGNANT");
        enseigantsEmailsField.getItems().setAll(FXCollections.observableArrayList(enseignants.stream().map(User::getEmail).toList()));
    }

    @FXML
    public void addEnseignantToList() {
        String email = enseigantsEmailsField.getValue().toString();
        User enseignant = enseignants.stream().filter(
                e -> e.getEmail().equals(email) && !enseignantsToAdd.contains(e)
        ).findFirst().orElse(null);
        if (enseignant != null) {
            enseignantsToAdd.add(enseignant);
            enseigantsTodAddListView.getItems().add(enseignant.getEmail());
        }
    }

    @FXML
    public void ok() {
        Cours cours = new Cours();
        cours.setNom(NomCours.getText());
        cours.setEnseignant(enseignantsToAdd);
        coursDAO.addCours(cours);
        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) NomCours.getScene().getWindow();
        stage.close();
    }
}
