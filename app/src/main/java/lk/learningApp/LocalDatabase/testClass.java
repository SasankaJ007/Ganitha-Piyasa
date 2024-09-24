package lk.learningApp.LocalDatabase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import lk.learningApp.R;

public class testClass extends AppCompatActivity { // Here is the test class for how to use Local database.
    private PreferenceManager preferenceManager; // define the preferenceManager as Global variable to use this in your custom functions in the java class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is basic implementation of Java class with AppCompatActivity
        setContentView(R.layout.activity_writing);
        // Initiate the preferenceManager instance with its Public constructor.
        preferenceManager = new PreferenceManager(getApplicationContext());

        //now you can save and retrieve methods from this instance

        //save details to local database =>
        preferenceManager.putBoolean("isLevelCompleted", true); // you can use this Constants class to save this string as final string to use in the place that you want
        preferenceManager.putBoolean(Constants.KEY_IS_LEVEL_COMPLETE, false); // Instead, you can use this constants class final String variable to avoid string name mismatch and code is more readable.
        preferenceManager.putString(Constants.KEY_DURATION_FOR_WRITING, "12:35 sec"); // this is how put strings you can follow these steps to other  formats like Int & Long

        //retrieve details from local database =>
        boolean isLevelCompleted2 = preferenceManager.getBoolean("isLevelCompleted"); // this is how you retrieve dat without Constant class
        boolean isLevelCompleted = preferenceManager.getBoolean(Constants.KEY_IS_LEVEL_COMPLETE); // Using Constant class you can do it easily
        String time = preferenceManager.getString(Constants.KEY_DURATION_FOR_WRITING); // this is how get strings from localDatabase & you can follow these steps to other formats like Int & Long

    }
}
