package com.example.quiz.repositories;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/quizz";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    private Connection connection;

    public QuestionDAO() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public void createQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO questions (enonce, choix, choixCorrects, coursId, difficulte) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        String choix = String.join(";", question.getChoix());
        String choixCorrects = String.join(";", question.getChoixCorrects());


        statement.setString(1, question.getEnonce());
        statement.setString(2, choix);
        statement.setString(3, choixCorrects);
        statement.setInt(4, question.getCours().getId()); // Assuming toString() method of Cours class gives appropriate string representation
        statement.setString(5, question.getDifficulte().toString()); // Assuming toString() method of Difficulte class gives appropriate string representation
        statement.executeUpdate();
    }

    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE questions SET enonce = ?, choix = ?, choixCorrects = ?, coursId = ?, difficulte = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        String choix = String.join(";", question.getChoix());
        String choixCorrects = String.join(";", question.getChoixCorrects());

        statement.setString(1, question.getEnonce());
        statement.setString(2, choix);
        statement.setString(3, choixCorrects);
        statement.setInt(4, question.getCours().getId()); // Assuming toString() method of Cours class gives appropriate string representation
        statement.setString(5, question.getDifficulte().toString()); // Assuming toString() method of Difficulte class gives appropriate string representation
        statement.setInt(6, question.getId());
        statement.executeUpdate();
    }

    // Read operation: Retrieve all questions from the database
    public List<Question> getAllQuestions() throws SQLException {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT q.*, c.nom AS coursNom FROM questions q LEFT JOIN cours c ON q.coursId = c.id";
    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
        Question question = new Question();
        question.setId(resultSet.getInt("id"));
        question.setEnonce(resultSet.getString("enonce"));
        question.setChoix(List.of(resultSet.getString("choix").split(";")));
        question.setChoixCorrects(List.of(resultSet.getString("choixCorrects").split(";")));
        Cours cours = new Cours();
        cours.setId(resultSet.getInt("coursId"));
        cours.setNom(resultSet.getString("coursNom"));
        question.setCours(cours);
        question.setDifficulte(Difficulte.valueOf(resultSet.getString("difficulte")));
        questions.add(question);
    }
    return questions;
    }

    public List<Question> getQuestionsByCoursIdAndDifficulte(int coursId, Difficulte difficulte) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, c.nom AS coursNom FROM questions q LEFT JOIN cours c ON q.coursId = c.id WHERE q.coursId = ? AND q.difficulte = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, coursId);
        statement.setString(2, difficulte.toString());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Question question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setEnonce(resultSet.getString("enonce"));
            question.setChoix(List.of(resultSet.getString("choix").split(";")));
            question.setChoixCorrects(List.of(resultSet.getString("choixCorrects").split(";")));
            Cours cours = new Cours();
            cours.setId(resultSet.getInt("coursId"));
            cours.setNom(resultSet.getString("coursNom"));
            question.setCours(cours);
            question.setDifficulte(Difficulte.valueOf(resultSet.getString("difficulte")));
            questions.add(question);
        }
        return questions;
    }



    // Delete operation: Delete a Question from the database
    public void deleteQuestion(int id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    public List<Question> getQuestionsByQuizId(int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, c.nom AS coursNom FROM questions q " +
                "LEFT JOIN cours c ON q.coursId = c.id " +
                "JOIN quizz_question qq ON q.id = qq.question_id " +
                "WHERE qq.quizz_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, quizId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Question question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setEnonce(resultSet.getString("enonce"));
            question.setChoix(List.of(resultSet.getString("choix").split(";")));
            question.setChoixCorrects(List.of(resultSet.getString("choixCorrects").split(";")));
            Cours cours = new Cours();
            cours.setId(resultSet.getInt("coursId"));
            cours.setNom(resultSet.getString("coursNom"));
            question.setCours(cours);
            question.setDifficulte(Difficulte.valueOf(resultSet.getString("difficulte")));
            questions.add(question);
        }
        return questions;
    }
}

/*
CREATE TABLE questions (
        id INT AUTO_INCREMENT PRIMARY KEY,
        enonce TEXT,
        choix TEXT,
        choixCorrects TEXT,
        coursId INT,
        difficulte VARCHAR(255)
);
);
*/

