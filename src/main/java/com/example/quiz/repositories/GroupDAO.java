package com.example.quiz.repositories;

import com.example.quiz.model.Cours;
import com.example.quiz.model.Difficulte;
import com.example.quiz.model.Group;
import com.example.quiz.model.Quizz;
import com.example.quiz.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/quizz";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    // CRUD operations

    public void addGroup(Group group) {
        String sql = "INSERT INTO groupe (nom) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, group.getNom());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                group.setId(generatedKeys.getInt(1));
            }

            // Update the jointure tables
            updateGroupEnseignants(connection, group);
            updateGroupEtudiants(connection, group);
            updateGroupQuizzes(connection, group);
            updateGroupCours(connection, group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGroupEnseignants(Connection connection, Group group) throws SQLException {
        String deleteSql = "DELETE FROM groupe_enseignant WHERE groupe_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, group.getId());
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO groupe_enseignant (groupe_id, enseignant_id) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (User enseignant : group.getEnseignants()) {
                insertStatement.setInt(1, group.getId());
                insertStatement.setInt(2, enseignant.getId());
                insertStatement.executeUpdate();
            }
        }
    }

    private void updateGroupEtudiants(Connection connection, Group group) throws SQLException {
        String deleteSql = "DELETE FROM groupe_etudiant WHERE groupe_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, group.getId());
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO groupe_etudiant (groupe_id, etudiant_id) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (User etudiant : group.getEtudiants()) {
                insertStatement.setInt(1, group.getId());
                insertStatement.setInt(2, etudiant.getId());
                insertStatement.executeUpdate();
            }
        }
    }

    private void updateGroupQuizzes(Connection connection, Group group) throws SQLException {
        String deleteSql = "DELETE FROM groupe_quiz WHERE groupe_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, group.getId());
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO groupe_quiz (groupe_id, quiz_id) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Quizz quizz : group.getQuizzs()) {
                insertStatement.setInt(1, group.getId());
                insertStatement.setInt(2, quizz.getId());
                insertStatement.executeUpdate();
            }
        }
    }

    private void updateGroupCours(Connection connection, Group group) throws SQLException {
        String deleteSql = "DELETE FROM group_cours WHERE group_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, group.getId());
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO group_cours (group_id, cours_id) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Cours cours : group.getCoursList()) {
                insertStatement.setInt(1, group.getId());
                insertStatement.setInt(2, cours.getId());
                insertStatement.executeUpdate();
            }
        }
    }

    public List<Group> getAllGroups() {
        List<Group> groupList = new ArrayList<>();
        String sql = "SELECT * FROM groupe";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("id"));
                group.setNom(resultSet.getString("nom"));
                // You may want to fetch associated enseignants, etudiants, and quizzes here
                group.setEnseignants(getEnseignantsForGroup(group.getId()));
                group.setEtudiants(getEtudiantsForGroup(group.getId()));
                group.setQuizzs(getQuizzesForGroup(group.getId()));
                group.setCoursList(getCoursForGroup(group.getId()));
                groupList.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    // todo fetch associated enseignants, etudiants, and quizzes
    public List<User> getEnseignantsForGroup(int groupId) {
        List<User> enseignants = new ArrayList<>();
        String sql = "SELECT e.id, e.firstName, e.lastName, e.email FROM users e " +
                "LEFT JOIN groupe_enseignant ge ON e.id = ge.enseignant_id " +
                "WHERE ge.groupe_id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User enseignant = new User();
                enseignant.setId(resultSet.getInt("id"));
                enseignant.setNom(resultSet.getString("lastName"));
                enseignant.setPrenom(resultSet.getString("firstName"));
                enseignant.setEmail(resultSet.getString("email"));
                enseignant.setRole("ENSEIGNANT");
                enseignants.add(enseignant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enseignants;
    }

    public List<User> getEtudiantsForGroup(int groupId) {
        List<User> etudiants = new ArrayList<>();
        String sql = "SELECT e.id, e.lastName, e.firstName, e.email FROM users e " +
                "JOIN groupe_etudiant ge ON e.id = ge.etudiant_id " +
                "WHERE ge.groupe_id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User etudiant = new User();
                etudiant.setId(resultSet.getInt("id"));
                etudiant.setNom(resultSet.getString("lastName"));
                etudiant.setPrenom(resultSet.getString("firstName"));
                etudiant.setEmail(resultSet.getString("email"));
                etudiant.setRole("ETUDIANT");
                etudiants.add(etudiant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etudiants;
    }

    public List<Quizz> getQuizzesForGroup(int groupId) {
        List<Quizz> quizzes = new ArrayList<>();
        String sql = "SELECT q.id, q.nombre_questions, q.difficulte FROM quizz q " +
                "JOIN groupe_quiz gq ON q.id = gq.quiz_id " +
                "WHERE gq.groupe_id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Quizz quizz = new Quizz();
                quizz.setId(resultSet.getInt("id"));
                quizz.setNombreQuestions(resultSet.getInt("nombre_questions"));
                quizz.setDifficulte(Difficulte.valueOf(resultSet.getString("difficulte")));
                quizzes.add(quizz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    public List<Cours> getCoursForGroup(int groupId) {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT c.id, c.nom FROM cours c " +
                "JOIN group_cours gc ON c.id = gc.cours_id " +
                "WHERE gc.group_id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
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

    public Group getGroupByUserId(int userId) {
        Group group = new Group();
        String sql = "SELECT g.id, g.nom FROM groupe g " +
                "JOIN groupe_etudiant ge ON g.id = ge.groupe_id " +
                "WHERE ge.etudiant_id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                group.setId(resultSet.getInt("id"));
                group.setNom(resultSet.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public void updateGroup(Group group) {
        String sql = "UPDATE groupe SET nom = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, group.getNom());
            statement.setInt(2, group.getId());
            statement.executeUpdate();

            // update the jointure tables
            updateGroupEnseignants(connection, group);
            updateGroupEtudiants(connection, group);
            updateGroupQuizzes(connection, group);
            updateGroupCours(connection, group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup(int groupId) {
        String sql = "DELETE FROM groupe WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void associateEnseignantWithGroup(int groupId, int enseignantId) {
        String sql = "INSERT INTO groupe_enseignant (groupe_id, enseignant_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, enseignantId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void associateEtudiantWithGroup(int groupId, int etudiantId) {
        String sql = "INSERT INTO groupe_etudiant (groupe_id, etudiant_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, etudiantId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addQuizToGroup(int groupId, int quizId) {
        String sql = "INSERT INTO groupe_quiz (groupe_id, quiz_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, quizId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/*
CREATE TABLE group_cours (
        group_id INT,
        cours_id INT,
        PRIMARY KEY (group_id, cours_id),
FOREIGN KEY (group_id) REFERENCES groupe(id),
FOREIGN KEY (cours_id) REFERENCES cours(id)
        );
*/
