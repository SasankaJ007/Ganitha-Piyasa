package lk.learningApp.ResponseModels;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeResponse {
    private String Time;

    // Default constructor (required for Jackson JSON serialization/deserialization)
    public TimeResponse() {
    }

    // Getter for "Time"
    public String getTime() {
        String str = convertToHHMM(Time);
        if (str.length() > 5){
            Time =  str.substring(0, str.length() - 1);
        }else {
            Time = str;
        }
        return Time;
    }
    public String convertToHHMM(String timeFormat) {
        // Split the time string into hours and minutes
        String[] parts = timeFormat.split("\\.");
        int hours = Integer.parseInt(parts[0]);
        int minutes = (int) (Double.parseDouble(parts[1]) * 10);
        // Format the hours and minutes to "HH" and "MM" with leading zeros
        DecimalFormat df = new DecimalFormat("00");
        String formattedHours = df.format(hours);
        String formattedMinutes = df.format(minutes);

        // Combine and return the formatted time as "HH.MM"
        return formattedHours + "." + formattedMinutes;
    }
}
