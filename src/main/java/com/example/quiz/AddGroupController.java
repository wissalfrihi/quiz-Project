package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Group;
import com.example.quiz.model.User;
import com.example.quiz.repositories.CoursDAO;
import com.example.quiz.repositories.GroupDAO;
import com.example.quiz.repositories.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddGroupController {
    @FXML
    private TextField NomGroup;

    @FXML
    private ComboBox enseigantsEmailsField;

    @FXML
    private ListView<String> enseigantsTodAddListView;

    @FXML
    private ComboBox etudiantsEmailsField;

    @FXML
    private ListView<String> etudiantsTodAddListView;

    @FXML
    private ComboBox coursNomField;

    @FXML
    private ListView<String> coursTodAddListView;

    private List<User> enseignants = new ArrayList<>();
    private List<User> etudiants = new ArrayList<>();
    private List<Cours> coursList = new ArrayList<>();

    private List<User> enseignantsToAdd = new ArrayList<>();
    private List<User> etudiantsToAdd = new ArrayList<>();
    private List<Cours> coursListToAdd = new ArrayList<>();

    private final GroupDAO groupDAO = new GroupDAO();
    private final UserDAO userDAO = new UserDAO();
    private final CoursDAO coursDAO = new CoursDAO();


    @FXML
    public void ok() {
        Group group = new Group();
        group.setNom(NomGroup.getText());
        group.setEnseignants(enseignantsToAdd);
        group.setEtudiants(etudiantsToAdd);
        group.setCoursList(coursListToAdd);

        groupDAO.addGroup(group);
        close();
    }

    @FXML
    public void initialize() {
        enseignants = userDAO.getAllUsersByRole("ENSEIGNANT");
        enseigantsEmailsField.getItems().setAll(FXCollections.observableArrayList(enseignants.stream().map(User::getEmail).toList()));

        etudiants = userDAO.getAllUsersByRole("ETUDIANT");
        etudiantsEmailsField.getItems().setAll(FXCollections.observableArrayList(etudiants.stream().map(User::getEmail).toList()));

        coursList = coursDAO.getAllCours();
        coursNomField.getItems().setAll(FXCollections.observableArrayList(coursList.stream().map(Cours::getNom).toList()));
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
    public void addEtudiantToList() {
        String email = etudiantsEmailsField.getValue().toString();
        User etudiant = etudiants.stream().filter(
                e -> e.getEmail().equals(email) && !etudiantsToAdd.contains(e)
        ).findFirst().orElse(null);
        if (etudiant != null) {
            etudiantsToAdd.add(etudiant);
            etudiantsTodAddListView.getItems().add(etudiant.getEmail());
        }
    }

    @FXML
    public void addCoursToList() {
        String nom = coursNomField.getValue().toString();
        Cours cours = coursList.stream().filter(
                e -> e.getNom().equals(nom) && !coursListToAdd.contains(e)
        ).findFirst().orElse(null);
        if (cours != null) {
            coursListToAdd.add(cours);
            coursTodAddListView.getItems().add(cours.getNom());
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
    public void updateEtudiantsList(){
        etudiantsToAdd.remove(etudiantsToAdd.get(etudiantsTodAddListView.getSelectionModel().getSelectedIndex()));
        etudiantsTodAddListView.getItems().clear();
        for (User enseignant : etudiantsToAdd) {
            etudiantsTodAddListView.getItems().add(enseignant.getEmail());
        }
    }

    @FXML
    public void updateCoursList(){
        coursListToAdd.remove(coursListToAdd.get(coursTodAddListView.getSelectionModel().getSelectedIndex()));
        coursTodAddListView.getItems().clear();
        for (Cours cours : coursListToAdd) {
            coursTodAddListView.getItems().add(cours.getNom());
        }
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) NomGroup.getScene().getWindow();
        stage.close();
    }
}
