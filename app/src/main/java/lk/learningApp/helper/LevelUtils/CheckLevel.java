package lk.learningApp.helper.LevelUtils;

public class CheckLevel {
    public static int calculatePercentage(int successCount, int attemptCount) {
        if (attemptCount <= 0) {
            return 0; // To avoid division by zero error
        }
        double percentage = ((double) successCount / attemptCount) * 100;
        // Ensure the result is within the range [0, 100]
        if (percentage < 0) {
            return 0;
        } else if (percentage > 100) {
            return 100;
        } else {
            return (int) percentage;
        }
    }
    public static int calculateRating(int percentage) {
        if (percentage < 20) {
            return 0;
        } else if (percentage < 50) {
            return 1;
        } else if (percentage < 70) {
            return 2;
        } else {
            return 3;
        }
    }
}
