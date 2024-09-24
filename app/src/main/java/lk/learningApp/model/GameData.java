package lk.learningApp.model;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    private String uid;
    private int currentLevel;
    private int totalScore;
    private List<Progress> progressList;
    private List<Character> correctAnswers;

    public GameData() {
        progressList = new ArrayList<>();
        correctAnswers = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<Progress> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<Progress> progressList) {
        this.progressList = progressList;
    }

    public List<Character> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Character> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

}