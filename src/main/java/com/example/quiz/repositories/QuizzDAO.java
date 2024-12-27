package com.example.quiz.repositories;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Question;
import com.example.quiz.model.Quizz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizzDAO {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/quizz";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    private final Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

    public QuizzDAO() throws SQLException {
    }


    public void createQuizz(Quizz quizz) throws SQLException {
        // Insert into quizz table
        String insertQuizzSql = "INSERT INTO quizz (nombre_questions, cours_id, difficulte) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuizzSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, quizz.getNombreQuestions());
            statement.setInt(2, quizz.getCours().getId());
            statement.setString(3, quizz.getDifficulte().toString());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quizz.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to create Quizz, no ID obtained.");
                }
            }
        }

        // Insert into quizz_question table to associate questions with the quizz
        String insertQuizzQuestionSql = "INSERT INTO quizz_question (quizz_id, question_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuizzQuestionSql)) {
            for (Question question : quizz.getQuestions()) {
                statement.setInt(1, quizz.getId());
                statement.setInt(2, question.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public Quizz getQuizzById(int id) throws SQLException {
        String selectQuizzSql = "SELECT * FROM quizz WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuizzSql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToQuizz(resultSet);
                }
            }
        }
        return null;
    }

    public List<Quizz> getAllQuizz() throws SQLException {
        List<Quizz> quizzList = new ArrayList<>();
        String selectAllQuizzSql = "SELECT * FROM quizz";
        try (PreparedStatement statement = connection.prepareStatement(selectAllQuizzSql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Quizz quizz = mapResultSetToQuizz(resultSet);
                quizzList.add(quizz);
            }
        }
        return quizzList;
    }

    public void updateQuizz(Quizz quizz) throws SQLException {
        String updateQuizzSql = "UPDATE quizz SET nombre_questions = ?, cours_id = ?, difficulte = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuizzSql)) {
            statement.setInt(1, quizz.getNombreQuestions());
            statement.setInt(2, quizz.getCours().getId());
            statement.setString(3, quizz.getDifficulte().toString());
            statement.setInt(4, quizz.getId());
            statement.executeUpdate();
        }

        // Update the associations with questions
        String deleteQuizzQuestionSql = "DELETE FROM quizz_question WHERE quizz_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuizzQuestionSql)) {
            statement.setInt(1, quizz.getId());
            statement.executeUpdate();
        }

        String insertQuizzQuestionSql = "INSERT INTO quizz_question (quizz_id, question_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuizzQuestionSql)) {
            for (Question question : quizz.getQuestions()) {
                statement.setInt(1, quizz.getId());
                statement.setInt(2, question.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void deleteQuizz(int id) throws SQLException {
        // Delete from quizz_question table first to maintain referential integrity
        String deleteQuizzQuestionSql = "DELETE FROM quizz_question WHERE quizz_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuizzQuestionSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

        // Then delete from the quizz table
        String deleteQuizzSql = "DELETE FROM quizz WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuizzSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Quizz mapResultSetToQuizz(ResultSet resultSet) throws SQLException {
        Quizz quizz = new Quizz();
        quizz.setId(resultSet.getInt("id"));
        quizz.setNombreQuestions(resultSet.getInt("nombre_questions"));
        quizz.setDifficulte(Difficulte.valueOf(resultSet.getString("difficulte")));

        // Fetch associated cours
        int coursId = resultSet.getInt("cours_id");
        CoursDAO coursDAO = new CoursDAO();
        Cours cours = coursDAO.getCoursById(coursId);
        quizz.setCours(cours);

        // Fetch associated questions
        QuestionDAO questionDAO = new QuestionDAO();
        List<Question> questions = questionDAO.getQuestionsByQuizId(quizz.getId());

        quizz.setQuestions(questions);

        return quizz;
    }
}

/*
CREATE TABLE quizz_question (
        quizz_id INT,
        question_id INT,
        PRIMARY KEY (quizz_id, question_id),
FOREIGN KEY (quizz_id) REFERENCES quizz(id),
FOREIGN KEY (question_id) REFERENCES question(id)
        );

CREATE TABLE quizz (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_questions INT,
    cours_id INT,
    difficulte ENUM('EASY', 'MEDIUM', 'HARD'),
    FOREIGN KEY (cours_id) REFERENCES cours(id)
);

        */

