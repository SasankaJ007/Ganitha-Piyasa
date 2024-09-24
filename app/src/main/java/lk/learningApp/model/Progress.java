package lk.learningApp.model;

public class Progress {
    private String symbol;
    private int showedTimes,correctTimes;

    public Progress() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getShowedTimes() {
        return showedTimes;
    }

    public void setShowedTimes(int showedTimes) {
        this.showedTimes = showedTimes;
    }

    public int getCorrectTimes() {
        return correctTimes;
    }

    public void setCorrectTimes(int correctTimes) {
        this.correctTimes = correctTimes;
    }

}
