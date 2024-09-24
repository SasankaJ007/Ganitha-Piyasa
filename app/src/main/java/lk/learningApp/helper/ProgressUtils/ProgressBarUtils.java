package lk.learningApp.helper.ProgressUtils;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import lk.learningApp.model.Level;
import lk.learningApp.model.SpendTime;

public class ProgressBarUtils {
    // Calculate the minimum and maximum values for the Y-axis range (you can adjust these values)
    public static float minY = 0f, maxY = 2.5f;

    public static void setTimeCount(int a, HashMap<Integer, Level> levelHashMap, ArrayList barEntriesList) {
        //a >= 1 and a <= 15
        SpendTime timeData = null;
        for (int i = 15; i > 0; i--){
            if (i <= a){
                timeData = new SpendTime(i, levelHashMap.get(i).getTotalTime());
                barEntriesList.add(new BarEntry(timeData.getKey(), timeData.getTimeFloat()));
            }else {
                barEntriesList.add(new BarEntry(i, 0f));
            }
        }
    }
    // Define a custom ValueFormatter that formats Y-axis values to one digit
    public static ValueFormatter oneDigitValueFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            // Format the value to one decimal place
            return String.format("%.1f", value);
        }
    };

}
