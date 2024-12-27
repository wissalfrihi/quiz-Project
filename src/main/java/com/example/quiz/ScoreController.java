package com.example.quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScoreController {
    @FXML
    private Label yourScore;

    private String score;

    @FXML
    private Label AllnbQuestions;

    private String nbQuestions;

    public void setScore(String score) {
        this.score = score;
        yourScore.setText(this.score);
    }

    public void setNbQuestions(String nbQuestions) {
        this.nbQuestions = nbQuestions;
        this.AllnbQuestions.setText(this.nbQuestions);
    }

}