package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.helper.LockLevels;
import lk.learningApp.helper.ProgressUtils.ProgressBarUtils;
import lk.learningApp.helper.UIUtils.NonSwipeRatingBar;
import lk.learningApp.model.Level;
import lk.learningApp.model.SpendTime;
import lk.learningApp.model.TotalScore;

public class progress extends AppCompatActivity {

    ImageView back;
    BarChart barChart, barChartStudent;
    BarData barData, barDataStudent;
    BarDataSet barDataSet, barDataSetStudent;
    ArrayList defaultBarEntriesArrayList = new ArrayList<>(), studentBarEntriesArrayList = new ArrayList<>();
    ProgressBar timeChartProgressBar, timeChartStudentProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        back = findViewById(R.id.btnHome);
        barChartStudent = findViewById(R.id.timeChartStudent);
        barChart = findViewById(R.id.timeChart);
        timeChartProgressBar = findViewById(R.id.timeChartProgressBar);
        timeChartStudentProgressBar = findViewById(R.id.timeChartStudentProgressBar);

        String[] type= new String[]{ "ඉලක්කම් කියවන්න","ඉලක්කම් ලියන්න","කාලය මනින්න","ගණිත කර්ම"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                type
        );
        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.filled_expose);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setText("ඉලක්කම් කියවන්න" , false);
        retrieveDefaultDataFromDatabase(Constants.KEY_COLLECTION_SPEAKING_GAME);
        retrieveStudentDataFromDatabase(Constants.KEY_COLLECTION_SPEAKING_GAME);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeChartProgressBar.setVisibility(View.VISIBLE);

                String selectedItem = autoCompleteTextView.getText().toString();

                switch (selectedItem) {
                    case "ඉලක්කම් ලියන්න":
                        retrieveDefaultDataFromDatabase(Constants.KEY_COLLECTION_WRITING_GAME);
                        retrieveStudentDataFromDatabase(Constants.KEY_COLLECTION_WRITING_GAME);
                        break;
                    case "කාලය මනින්න":
                        retrieveDefaultDataFromDatabase(Constants.KEY_COLLECTION_COUNTING_GAME);
                        retrieveStudentDataFromDatabase(Constants.KEY_COLLECTION_COUNTING_GAME);
                        break;
                    case "ගණිත කර්ම":
                        retrieveDefaultDataFromDatabase(Constants.KEY_COLLECTION_LOGIC_GAME);
                        retrieveStudentDataFromDatabase(Constants.KEY_COLLECTION_LOGIC_GAME);
                        break;
                    case "ඉලක්කම් කියවන්න":
                    default:
                        retrieveDefaultDataFromDatabase(Constants.KEY_COLLECTION_SPEAKING_GAME);
                        retrieveStudentDataFromDatabase(Constants.KEY_COLLECTION_SPEAKING_GAME);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void retrieveDefaultDataFromDatabase(String gameName) {
        DocumentReference documentRef = FirebaseFirestore.getInstance()
                .collection(Constants.KEY_COLLECTION_STUDENT_SPEND_TIME)
                .document(gameName);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> dataMap = documentSnapshot.getData();
                List<SpendTime> timeDataList = new ArrayList<>();
                if (dataMap != null) {
                    for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                        int key = Integer.parseInt(entry.getKey());
                        String timeString = entry.getValue().toString();
                        SpendTime timeData = new SpendTime (key, timeString);
                        timeDataList.add(timeData);
                    }
                    if (timeDataList != null){
                        if (defaultBarEntriesArrayList != null)
                            defaultBarEntriesArrayList.clear();
                        for (SpendTime spendTime : timeDataList) {
                            defaultBarEntriesArrayList.add(new BarEntry(spendTime.getKey(), spendTime.getTimeFloat()));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDetailsToDefaultValueBar();
                            }
                        });
                    }
                    else {
                        timeChartProgressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    timeChartProgressBar.setVisibility(View.GONE);
                }
            } else {
                // Handle the case where the document doesn't exist
                timeChartProgressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred during the document retrieval
            timeChartProgressBar.setVisibility(View.GONE);
        });
    }

    private void retrieveStudentDataFromDatabase(String gameName) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef = FirebaseFirestore.getInstance()
                .collection(gameName)
                .document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore student = documentSnapshot.toObject(TotalScore.class);

                if (student != null){
                    HashMap<Integer, Level> levelHashMap = LockLevels.getLevelsMap(student);
                    studentBarEntriesArrayList.clear();
                    int i = 15;
                    for (; i>=0; i--){
                        if (levelHashMap.containsKey(i)){
                            if (levelHashMap.get(i).isCompleted()){
                                break;
                            }
                        }
                    }
                    ProgressBarUtils.setTimeCount(i, levelHashMap, studentBarEntriesArrayList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDetailsToStudentValueBar();
                        }
                    });
                }else {
                    timeChartStudentProgressBar.setVisibility(View.GONE);
                }
            } else {
                // Handle the case where the document doesn't exist
                timeChartStudentProgressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred during the document retrieval
            timeChartStudentProgressBar.setVisibility(View.GONE);
        });
    }

    private void setDetailsToDefaultValueBar() {
        barDataSet = new BarDataSet(defaultBarEntriesArrayList, " අපේක්ෂිත මට්ටම");
        barData = new BarData(barDataSet);
        //i want to clear the chart view here
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(9f);
        barChart.getDescription().setEnabled(false);
        // Apply the custom ValueFormatter to the Y-axis of barChart
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setValueFormatter(ProgressBarUtils.oneDigitValueFormatter);
        leftYAxis.setAxisMinimum(ProgressBarUtils.minY);
        leftYAxis.setAxisMaximum(ProgressBarUtils.maxY);
        barDataSet.setValueFormatter(ProgressBarUtils.oneDigitValueFormatter);
        barChart.invalidate();
        timeChartProgressBar.setVisibility(View.GONE);
    }
    private void setDetailsToStudentValueBar() {
        barDataSetStudent = new BarDataSet(studentBarEntriesArrayList, " ඔබගේ මට්ටම");
        barDataStudent = new BarData(barDataSetStudent);
        barChartStudent.setData(barDataStudent);
        barDataSetStudent.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetStudent.setValueTextColor(Color.BLACK);
        barDataSetStudent.setValueTextSize(9f);
        barChartStudent.getDescription().setEnabled(false);
        // Apply the custom ValueFormatter to the Y-axis of barChartStudent
        YAxis leftYAxisStudent = barChartStudent.getAxisLeft();
        leftYAxisStudent.setValueFormatter(ProgressBarUtils.oneDigitValueFormatter);
        leftYAxisStudent.setAxisMinimum(ProgressBarUtils.minY);
        leftYAxisStudent.setAxisMaximum(ProgressBarUtils.maxY);
        barDataStudent.setValueFormatter(ProgressBarUtils.oneDigitValueFormatter);
        barChartStudent.invalidate();
        timeChartStudentProgressBar.setVisibility(View.GONE);
    }

}