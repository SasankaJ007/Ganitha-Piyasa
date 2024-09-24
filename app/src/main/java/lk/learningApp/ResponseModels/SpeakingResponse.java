package lk.learningApp.ResponseModels;

public class SpeakingResponse {
    private String Number;

    // Default constructor required for Jackson JSON serialization/deserialization
    public SpeakingResponse() {
    }

    // Getter for "Number"
//    public String getNumber() {
//        return Number;
//    }

    public String getNumber() {
        return Number.substring(0, Number.length() - 2);
    }
}
