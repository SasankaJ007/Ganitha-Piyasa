package lk.learningApp.model;

import java.util.Locale;

import lk.learningApp.helper.ProgressUtils.FormatConverter;

public class SpendTime {
    private int key;
    private String timeString;

    // Default constructor
    public SpendTime() {
        // Required for Firestore to deserialize objects
    }
    public SpendTime(int key, String timeString) {
        this.key = key;
        this.timeString = timeString;
    }
    public int getKey() {
        return key;
    }
    public String getTimeString() {
        return timeString;
    }

    public float getTimeFloat() {
        float floatValue = FormatConverter.convertTimeStringToFloat(timeString);
        String formattedTimeString = String.format(Locale.US, "%.1f", floatValue);
        return Float.parseFloat(formattedTimeString);
//        return FormatConverter.convertTimeStringToFloat(timeString);
    }
}
