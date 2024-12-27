package com.example.quiz;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quizz;
import com.example.quiz.repositories.QuestionDAO;
import com.example.quiz.repositories.QuizzDAO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class QuizzController {
    @FXML
    private ListView<Quizz> quizzs;

    @FXML
    private Button deleteButton;

    @FXML
    private Button versPDFbutton;
    private Quizz selectedQuizz;

    private final QuizzDAO quizzDAO = new QuizzDAO();

    public QuizzController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        deleteButton.setDisable(true);
        versPDFbutton.setDisable(true);

        quizzs.setItems(FXCollections.observableArrayList(quizzDAO.getAllQuizz()));
    }

    @FXML
    void AddnewQuizz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("add-quizz-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void onItemClicked() {
        deleteButton.setDisable(false);
        versPDFbutton.setDisable(false);
        selectedQuizz = quizzs.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onDelete() throws SQLException {
        quizzDAO.deleteQuizz(selectedQuizz.getId());
        quizzs.getItems().remove(selectedQuizz);
    }

    public void generatePdf(Quizz quizz) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/com/example/quiz/PDF/quiz_" + System.currentTimeMillis() + ".pdf"));
            document.open();

            // Add title
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Quiz: " + quizz.getCours().getNom(), font);
            document.add(title);

            // Add questions
            for (Question question : quizz.getQuestions()) {
                Paragraph questionParagraph = new Paragraph(question.getEnonce(), font);
                document.add(questionParagraph);

                // Add choices
                for (String choice : question.getChoix()) {
                    Paragraph choiceParagraph = new Paragraph(choice, font);
                    document.add(choiceParagraph);
                }
            }

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generatePDF() {
        generatePdf(selectedQuizz);
    }

}