package com.example.quiz.repositories;

import com.example.quiz.model.Cours;
import com.example.quiz.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/quizz";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    // CRUD operations

    public void addCours(Cours cours) {
        String sql = "INSERT INTO cours (nom) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cours.getNom());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cours.setId(generatedKeys.getInt(1));
            }

            // update the cours_enseignant table
            updateCoursEnseignant(connection, cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCoursEnseignant(Connection connection, Cours cours) throws SQLException{
        String deleteSql = "DELETE FROM cours_enseignant WHERE cours_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, cours.getId());
            deleteStatement.executeUpdate();
        }

        String sql = "INSERT INTO Cours_Enseignant (cours_id, enseignant_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (User enseignant : cours.getEnseignant()) {
                statement.setInt(1, cours.getId());
                statement.setInt(2, enseignant.getId());
                statement.executeUpdate();
            }
        }
    }

    public List<Cours> getAllCours() {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Cours cours = new Cours();
                cours.setId(resultSet.getInt("id"));
                cours.setNom(resultSet.getString("nom"));
                coursList.add(cours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }

    public void updateCours(Cours cours) {
        String sql = "UPDATE cours SET nom = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cours.getNom());
            statement.setInt(2, cours.getId());
            statement.executeUpdate();

            // update the cours_enseignant table
            updateCoursEnseignant(connection, cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteCours(int id) {
        String sql = "DELETE FROM cours WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            // delete from cours_enseignant table
            String deleteCoursEnseignantSql = "DELETE FROM cours_enseignant WHERE cours_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteCoursEnseignantSql)) {
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCoursByName(String nom) {
        String sql = "DELETE FROM cours WHERE nom = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nom);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cours getCoursById(int id) {
        String sql = "SELECT * FROM cours WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cours cours = new Cours();
                    cours.setId(resultSet.getInt("id"));
                    cours.setNom(resultSet.getString("nom"));
                    return cours;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Cours> getAllCoursWithEnseigants(){
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Cours cours = new Cours();
                cours.setId(resultSet.getInt("id"));
                cours.setNom(resultSet.getString("nom"));
                cours.setEnseignant(getEnseignantsForCours(cours.getId()));

                coursList.add(cours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }


    public List<User> getEnseignantsForCours(int coursId) {
        List<User> enseignants = new ArrayList<>();
        String sql = "SELECT u.id, u.firstName, u.lastName, u.email, u.password, u.role " +
                "FROM users u " +
                "JOIN Cours_Enseignant ce ON u.id = ce.enseignant_id " +
                "JOIN Cours c ON ce.cours_id = c.id " +
                "WHERE c.id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User enseignant = new User();
                enseignant.setId(rs.getInt("id"));
                enseignant.setNom(rs.getString("lastName"));
                enseignant.setPrenom(rs.getString("firstName"));
                enseignant.setEmail(rs.getString("email"));
                enseignant.setMotDePasse(rs.getString("password"));
                enseignant.setRole(rs.getString("role"));
                enseignants.add(enseignant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enseignants;
    }

}

/*
CREATE TABLE cours (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nom VARCHAR(255) NOT NULL
);

CREATE TABLE Cours_Enseignant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cours_id INT,
    enseignant_id INT,
    FOREIGN KEY (cours_id) REFERENCES Cours(id),
    FOREIGN KEY (enseignant_id) REFERENCES Enseignant(id)
);
*/
