package lk.learningApp.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.learningApp.R;
import lk.learningApp.helper.UIUtils.NonSwipeRatingBar;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;

public class LockLevels {
    // Lock the CardView
    public static void lockLevel(Context context, CardView cardView) {
        // Disable clicks on the CardView
        cardView.setClickable(false);

        // Create a new LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        ImageView imageView = new ImageView(context);

        // Set a tag on the LinearLayout (you can use any tag value you want)
        linearLayout.setTag("lockLayout");

        // Set the background color of the LinearLayout
        linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.lock_background));

        // Set the layout parameters for the LinearLayout to center it within the CardView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);

        // Add the ImageView to the LinearLayout (make sure 'imageView' is defined and initialized earlier in your code)
        imageView.setImageResource(R.drawable.lock);
        imageView.setPadding(40,40,40,40);
        linearLayout.addView(imageView);

        // Add the LinearLayout to the CardView
        cardView.addView(linearLayout);
    }
    // Unlock the CardView
    public static void unlockLevel(CardView cardView) {
        // Enable clicks on the CardView
        cardView.setClickable(true);

        // Remove the LinearLayout from the CardView
        LinearLayout linearLayout = cardView.findViewWithTag("lockLayout");
        if (linearLayout != null) {
            cardView.removeView(linearLayout);
        }
    }
    public static HashMap<Integer, Level> getLevelsMap(TotalScore student) {
        HashMap<Integer, Level> levelHashMap = new HashMap<>();

        if (student.getLevel1() != null) {
            levelHashMap.put(1, student.getLevel1());
        }
        if (student.getLevel2() != null) {
            levelHashMap.put(2, student.getLevel2());
        }
        if (student.getLevel3() != null) {
            levelHashMap.put(3, student.getLevel3());
        }
        if (student.getLevel4() != null) {
            levelHashMap.put(4, student.getLevel4());
        }
        if (student.getLevel5() != null) {
            levelHashMap.put(5, student.getLevel5());
        }
        if (student.getLevel6() != null) {
            levelHashMap.put(6, student.getLevel6());
        }
        if (student.getLevel7() != null) {
            levelHashMap.put(7, student.getLevel7());
        }
        if (student.getLevel8() != null) {
            levelHashMap.put(8, student.getLevel8());
        }
        if (student.getLevel9() != null) {
            levelHashMap.put(9, student.getLevel9());
        }
        if (student.getLevel10() != null) {
            levelHashMap.put(10, student.getLevel10());
        }
        if (student.getLevel11() != null) {
            levelHashMap.put(11, student.getLevel11());
        }
        if (student.getLevel12() != null) {
            levelHashMap.put(12, student.getLevel12());
        }
        if (student.getLevel13() != null) {
            levelHashMap.put(13, student.getLevel13());
        }
        if (student.getLevel14() != null) {
            levelHashMap.put(14, student.getLevel14());
        }
        if (student.getLevel15() != null) {
            levelHashMap.put(15, student.getLevel15());
        }
        return levelHashMap;
    }
    public static int getSelectedNumber(String input) {
        // Define a regular expression pattern to match "level" followed by digits
        Pattern pattern = Pattern.compile("level(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String digits = matcher.group(1); // Extract the digits
            int number = Integer.parseInt(digits); // Convert digits to an integer
            return number; // Replace and return
        } else {
            // No match found, return the original string
            return 17;
        }
    }
    public static void setRatings(int a, HashMap<Integer, Level> levelHashMap, List<NonSwipeRatingBar> ratingBarList) {
        //a >= 1 and a <= 15
        for (int i = a; i > 0; i--){
            float count = levelHashMap.get(i).getRating();
            ratingBarList.get(i-1).setStars(count);
        }
    }
    public static void setStars(List<NonSwipeRatingBar> ratingBarList) {
        for (NonSwipeRatingBar ratingBar : ratingBarList) {
            ratingBar.setStars(0f);
            ratingBar.setCanChange(false);
        }
    }


}
