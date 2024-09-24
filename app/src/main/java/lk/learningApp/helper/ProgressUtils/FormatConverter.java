package lk.learningApp.helper.ProgressUtils;

public class FormatConverter {

    public static float convertTimeStringToFloat(String timeString) {
        // Split the time string into minutes and seconds
        String[] parts = timeString.split(":");
        if (parts.length != 2) {
            return 0.0f;
        }

        // Parse minutes and seconds as integers
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        // Calculate the total time in minutes and convert to float
        int totalTimeInSeconds = minutes * 60 + seconds;

        return (float) totalTimeInSeconds / 60.0f;
    }
}
