package com.example.quiz;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Question;
import com.example.quiz.model.Quizz;
import com.example.quiz.model.User;
import com.example.quiz.repositories.GroupDAO;
import com.example.quiz.repositories.QuizzDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

public class EtudiantQuizzController {
    @FXML
    private ListView<Quizz> quizzs;

    @FXML
    private Button passerQuizzButton;
    private Quizz selectedQuizz;

    private User etudiant;

    private final QuizzDAO quizzDAO = new QuizzDAO();

    private final GroupDAO groupDAO = new GroupDAO();

    public EtudiantQuizzController() throws SQLException {
    }

    public void initData(User etudiant) throws SQLException {
        this.etudiant = etudiant;
        initialize();
    }

    @FXML
    public void initialize() throws SQLException {
       if (etudiant != null) {
           passerQuizzButton.setDisable(true);

           List<Cours> groupCours = groupDAO.getAllGroups().stream().filter(
                           g -> g.getEtudiants().stream().anyMatch(e -> e.getId() == etudiant.getId())
           ).map(g -> g.getCoursList()).flatMap(List::stream)
                   .distinct()
                   .toList();
           System.out.println(groupCours);
           List<Quizz> quizzList = quizzDAO.getAllQuizz();
           List<Quizz> etudiantQuizzList =  quizzList.stream().filter(
                q -> groupCours.stream().anyMatch(c -> c.getId() == q.getCours().getId())
           ).toList();
           quizzs.setItems(FXCollections.observableArrayList(etudiantQuizzList));        }
    }

    @FXML
    public void onItemClicked() {
        selectedQuizz = quizzs.getSelectionModel().getSelectedItem();
        if (selectedQuizz != null) {
            passerQuizzButton.setDisable(false);
        }
    }

    @FXML
    private void passerQuizz() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("passer-quizz-view.fxml"));
        Parent root = loader.load();
        PasserQuizzController passerQuizzController = loader.getController();
        passerQuizzController.setSelectedQuizz(selectedQuizz);
        passerQuizzController.initData(selectedQuizz);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}