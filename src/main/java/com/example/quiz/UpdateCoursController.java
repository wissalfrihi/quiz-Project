package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.User;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class UpdateCoursController {
    @FXML
    private TextField NomCours;

    @FXML
    private ComboBox enseigantsEmailsField;

    @FXML
    private ListView<String> enseigantsTodAddListView;

    private List<User> enseignants = new ArrayList<>();

    private List<User> enseignantsToAdd = new ArrayList<>();


    private Cours selectedCours;

    private final CoursDAO coursDAO = new CoursDAO();
    private final UserDAO userDAO = new UserDAO();


    public void initData(Cours selectedCours) {
        this.selectedCours = selectedCours;
        initialize();
    }

    @FXML
    public void initialize() {
        if (selectedCours != null) {
            enseignants = userDAO.getAllUsersByRole("ENSEIGNANT");
            enseignantsToAdd = selectedCours.getEnseignant();
            enseigantsEmailsField.getItems().setAll(FXCollections.observableArrayList(enseignants.stream().map(User::getEmail).toList()));
            enseigantsTodAddListView.getItems().setAll(FXCollections.observableArrayList(enseignantsToAdd.stream().map(User::getEmail).toList()));
            NomCours.setText(selectedCours.getNom());
        }
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
    public void updateEnseigantsList(){
        enseignantsToAdd.remove(enseignantsToAdd.get(enseigantsTodAddListView.getSelectionModel().getSelectedIndex()));
        enseigantsTodAddListView.getItems().clear();
        for (User enseignant : enseignantsToAdd) {
            enseigantsTodAddListView.getItems().add(enseignant.getEmail());
        }
    }

    @FXML
    public void ok(){
        Cours cours = new Cours();
        cours.setId(selectedCours.getId());
        cours.setNom(NomCours.getText());
        cours.setEnseignant(enseignantsToAdd);
        coursDAO.updateCours(cours);
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
