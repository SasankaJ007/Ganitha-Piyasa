package lk.learningApp.ResponseModels;

public class APIResponse {
    private String Number;

    // Default constructor required for Jackson JSON serialization/deserialization
    public APIResponse() {
    }

    // Getter for "Number"
    public String getNumber() {
        return Number;
    }
}
