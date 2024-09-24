package lk.learningApp.model;

import java.util.ArrayList;

public class User {
    public String levelName;
    public int rating;
    public int spendTime;
    public boolean lockedLevel;

    public boolean isLockedLevel() {
        return lockedLevel;
    }

    public void setLockedLevel(boolean lockedLevel) {
        this.lockedLevel = lockedLevel;
    }

    public String getLevelName() {
        return levelName="1";
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotalScore() {
        return spendTime;
    }

    public void setTotalScore(int totalScore) {
        this.spendTime = totalScore;
    }
    
}
