package lk.learningApp;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.squti.androidwaverecorder.WaveConfig;
import com.github.squti.androidwaverecorder.WaveRecorder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.ResponseModels.APIResponse;
import lk.learningApp.ResponseModels.SpeakingResponse;
import lk.learningApp.ResponseModels.TimeResponse;
import lk.learningApp.helper.LevelUtils.CheckLevel;
import lk.learningApp.helper.UpdateFirebase;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import lk.learningApp.helper.FirestoreCounting;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;

//import okhttp3.OkHttpClient;

public class counting extends AppCompatActivity {

    ImageView mainPage,scorePage;
    TextView showNum;
    ImageButton mic;
    LinearLayout check;

    private static int Microphone_Permission_Code = 200;
    boolean isRecording = false;

    Boolean getTime=false;
    Boolean isStart=false;

    private boolean isTimerRunning = false;
    private int seconds = 0;
    private int totalSeconds = 0;
    String time,totalTime;
    private Handler timerHandler = new Handler();
    private final int touchHoldThreshold = 1000; // in milliseconds
    private Handler handler = new Handler();

    private static List<String> hours;
    private static List<String> mins01;
    private static List<String> mins02;
    private static List<String> level_min;
    String userAnswer, correctAnswer;
    int currentLevel=1;
    String levelName="level1";
    int currentCount=1, attemptCount=0, successCount=0;
    String randomHour, randomMin;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private OkHttpClient client;
    private WaveRecorder waveRecorder;


    private static String displayHour() {
        if(hours.size()==0){
            generateHoursList();
        }
        String randomH = hours.get(0);
        hours.remove(0);
        return randomH;
    }

    private static String displayMin1() {
        if(mins01.size()==0){
            generateMinutesList01();
        }
        String randomM1 = mins01.get(0);
        mins01.remove(0);
        return randomM1;
    }

    private static String displayMin2() {
        if(mins02.size()==0){
            generateMinutesList02();
        }
        String randomM2 = mins02.get(0);
        mins02.remove(0);
        return randomM2;
    }

    private static String displayMinLevel() {
        if(level_min.size()==0){
            generateMinutesListMix();
        }
        String randomMin = level_min.get(0);
        level_min.remove(0);
        return randomMin;
    }

    void levelsRandomNumber(String levelName)
    {

        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
            case "level5":

                randomHoursDisplay();
                break;

            case "level6":
            case "level7":
            case "level8":
            case "level9":
            case "level10":
                randomMinDisplay();
                break;

            case "level11":
            case "level12":
            case "level13":
            case "level14":
            case "level15":
                randomHoursMinDisplay();
                break;

        }

    }

    boolean checkCondition() {
        return correctAnswer.equals(userAnswer);
    }

    void roundsAndLevels(String levelName)
    {
        boolean res = checkCondition();
        attemptCount++;
        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
            case "level5":
                switch (currentCount)
                {
                    case 1:
                        randomHoursDisplay();
                        if (res)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();
                        }
                        currentCount++;
                        break;

                    case 2:
                        randomHoursDisplay();
                        if (res)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();
                        }
                        currentCount++;
                        break;

                    case 3:
                        randomHoursDisplay();
                        if (res)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        break;
                }
                break;



            case "level6":
            case "level7":
            case "level8":
            case "level9":
            case "level10":

                switch (currentCount)
                {

                    case 1:
                        randomMinDisplay();
                        if (res)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();
                        }
                        currentCount++;
                        break;

                    case 2:
                        randomMinDisplay();
                        if (res)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();
                        }
                        currentCount++;
                        break;

                    case 3:
                        randomMinDisplay();
                        if (res)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        break;

                }
                break;
            case "level11":
            case "level12":
            case "level13":
            case "level14":
            case "level15":
                switch (currentCount)
                {
                    case 1:
                        randomHoursMinDisplay();
                        if (res)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();
                        }
                        currentCount++;
                        break;

                    case 2:
                        randomHoursMinDisplay();
                        if (res)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();
                        }
                        currentCount++;
                        break;

                    case 3:
                        randomHoursMinDisplay();
                        if (res)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        break;
                }
                break;
        }

    }

    public void checkSuccessCount(){
        System.out.println("Success Count : "+ successCount);
        System.out.println("current Count : "+ currentCount);
        System.out.println("Attempt Count : "+ attemptCount);
        int percentage = CheckLevel.calculatePercentage(successCount, attemptCount);
        if(successCount == 3){
            if(attemptCount > 6) {
                reset(percentage);
            } else {
                win(percentage);
            }
        } else if (attemptCount > 6) {
            reset(percentage);
        } else {
            currentCount = 3;
            isTimerRunning = false;
            timerHandler.removeCallbacks(timerRunnable);
            seconds = 0;
        }
    }

    public void reset(int percentage) {
        currentCount = 1;
        successCount = 0;
        attemptCount = 0;
        resetAllTime();
        roundsAndLevels(levelName);
        levelDataGet(levelName,false, CheckLevel.calculateRating(percentage));

        Intent i = new Intent(this, CountingLose.class);
        i.putExtra("TotalTime",totalTime);
        i.putExtra("Percentage", percentage);
        i.putExtra("LevelName",levelName);
        startActivity(i);
        finish();
    }

    public void win(int percentage) {
        String time=totalTime;
        levelDataGet(levelName,true, CheckLevel.calculateRating(percentage));
        resetAllTime();

        Intent i = new Intent(this, CountingWin.class);
        i.putExtra("TotalTime",time);
        i.putExtra("Percentage", percentage);
        i.putExtra("LevelName",levelName);
        startActivity(i);
        finish();
    }
    void randomHoursDisplay(){
        randomHour = displayHour();
        randomMin  = displayMin1();
        correctAnswer = randomHour+"."+randomMin;
        showNum.setText(correctAnswer);
    }

    void randomMinDisplay(){
        randomHour = displayHour();
        randomMin  =displayMin2();
        correctAnswer = randomHour+"."+randomMin;
        showNum.setText(correctAnswer);
    }

    void randomHoursMinDisplay(){
        randomHour = displayHour();
        randomMin  = displayMinLevel();
        correctAnswer = randomHour+"."+randomMin;
        showNum.setText(correctAnswer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);
        client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS) // Increase the read timeout
                .writeTimeout(60, TimeUnit.SECONDS) // Increase the write timeout
                .build();
        mainPage = findViewById(R.id.btnHome);
        scorePage = findViewById(R.id.btnScore);
        mic = findViewById(R.id.btnCounting);
        showNum = findViewById(R.id.countTextView);
        check = findViewById(R.id.checkBlock1);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        if(getIntent()!=null) {
            currentLevel=getIntent().getIntExtra("Level",1);
            levelName=getIntent().getStringExtra("selectedLevel");
            System.out.println("Level : "+ levelName);
            refreshLevelIfNotCreated();
        }

        hours = new ArrayList<>();
        generateHoursList();

        mins01 = new ArrayList<>();
        generateMinutesList01();

        mins02 = new ArrayList<>();
        generateMinutesList02();

        level_min = new ArrayList<>();
        generateMinutesListMix();

        Collections.shuffle(hours);
        Collections.shuffle(mins01);
        Collections.shuffle(mins02);
        Collections.shuffle(level_min);
        levelsRandomNumber(levelName);
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

        mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scorePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScoreBoard.class));
            }
        });

        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start timer when user touches down
                        startTimer();
                        btnRecordPress(v);
                        if(!getTime){
                            getTime=true;
                            startTime();
                            resetTime();
                        }else{
                        }

                        if(!isStart){
                            isStart=true;
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // User released touch, cancel timer
                        cancelTimer();
                        break;
                }
                return true;

            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopTime();
                getTime=false;
                if(!isStart){
                    Toast.makeText(counting.this, "පිළිතුරුක් ලබා දෙන්න!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (isRecording){
                        stopRecording();
                    }
                    isStart = false;
                    sendAPIRequest();
                }
                check.setBackgroundColor(ContextCompat.getColor(counting.this, android.R.color.darker_gray));
                check.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check.setBackground(ContextCompat.getDrawable(counting.this,R.drawable.block03but));
                    }
                }, 100);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static void generateHoursList() {
        for (int i = 0; i < 12; i++) {
            String[] hr = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            hours.add(hr[i]);
        }
    }
    private static void generateMinutesList01(){
        String[] min1 = {"00", "30"};
        for (int i=0; i<2; i++)
        {
            mins01.add(min1[i]);
        }
    }
    private static void generateMinutesList02(){
        String[] min2 = {"15", "45"};
        for (int i=0; i<2; i++)
        {
            mins02.add(min2[i]);
        }
    }
    private static void generateMinutesListMix(){
        String[] mi = { "00","15", "30", "45"};
        for (int i=0; i<4; i++)
        {
            level_min.add(mi[i]);
        }
    }
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                seconds++;
                totalSeconds++; // Increment the total time
                int minutes = seconds / 60;
                int remainderSeconds = seconds % 60;
                time = String.format("%02d:%02d", minutes, remainderSeconds);
//                timerTextView.setText(time);

                // Update the total time TextView
                int totalMinutes = totalSeconds / 60;
                int totalRemainderSeconds = totalSeconds % 60;
                totalTime = String.format("%02d:%02d", totalMinutes, totalRemainderSeconds);
//                totalTimeTextView.setText(totalTime);

                timerHandler.postDelayed(this, 1000);
            }
        }
    };
    private void startTime() {
        isTimerRunning = true;
        timerHandler.post(timerRunnable);

    }

    private void stopTime() {
        isTimerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);

        getTime = true;

    }
    private void resetTime() {
        seconds=0;
    }

    private void resetAllTime() {
        isTimerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
        seconds = 0;
        totalSeconds = 0;
    }


    private void sendAPIRequest() {
        // Get the audio file
        File audioFile = new File(getOutputFilePath());
        // Create a request body with the audio data

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", "audio.wav", RequestBody.create(audioFile, MediaType.parse("audio/wav")))
                .build();

        Request request = new Request.Builder()
                .url(Constants.BASE_URL +"/time/") // Replace with your API endpoint
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Counting Response Successful!!" + responseBody);
                            APIResponse timeResponse = new Gson().fromJson(responseBody, APIResponse.class);
                            userAnswer = timeResponse.getNumber();
                            System.out.println("User Answer : " + userAnswer);
                            System.out.println("Correct Answer : " + correctAnswer);
                            if (userAnswer == null){
                                Toast.makeText(counting.this, "පිළිතුරුක් ලබා දෙන්න!", Toast.LENGTH_SHORT).show();
                            }else{
                                roundsAndLevels(levelName);
                            }
                        }
                    });
                } else {
                    // Handle API error
                    System.out.println("Response error!!");
                    System.out.println(response.headers());
                    System.out.println(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(counting.this, "Counting Response Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

    private void showRound01dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.counting_success_round01, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showRound02dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.counting_success_round01, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showRound03dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.counting_success_round03, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSuccessCount();
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkSuccessCount();
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void errorDialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.countiong_error, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSuccessCount();
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkSuccessCount();
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void errorDialogD1(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.counting_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void errorDialogD2(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(counting.this).inflate(R.layout.counting_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(counting.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void btnRecordPress(View view) {

        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        waveRecorder = new WaveRecorder(getOutputFilePath());
        waveRecorder.setNoiseSuppressorActive(true);
        waveRecorder.startRecording();
        isRecording = true;
    }
    private void stopRecording() {
        if (waveRecorder != null) {
            waveRecorder.stopRecording();
            waveRecorder = null;
            isRecording = false;
        }
    }

    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else{
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, Microphone_Permission_Code);
        }
    }

    private String getOutputFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "count" + ".wav");
        return file.getPath();
    }

    private void startTimer() {
        handler.postDelayed(timer, touchHoldThreshold);
    }

    Runnable timer= () -> {
        performTouchAndHoldAction();
        startTimer();
    };

    private void cancelTimer() {
        // Cancel timer if needed
        handler.removeCallbacks(timer);
        handler.removeCallbacksAndMessages(null);
        Log.d("TAG", "onTouch: timer");

    }

    private void performTouchAndHoldAction() {
        YoYo.with(Techniques.Swing)
                .duration(1000)
                .playOn(findViewById(R.id.btnCounting));
    }

    private void levelDataGet(String levelName,boolean isWin, int successCount) {
        FirestoreCounting firestoreWriter=new FirestoreCounting();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("CountingGame").document(userID);
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore student = documentSnapshot.toObject(TotalScore.class);
                Level level;
                switch (levelName.toLowerCase())
                {
                    case "level1":
                        if(student.getLevel1()!=null)
                        {
                            level=student.getLevel1();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel1(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level2":
                        if(student.getLevel2()!=null)
                        {
                            level=student.getLevel2();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel2(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level3":
                        if(student.getLevel3()!=null)
                        {
                            level=student.getLevel3();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel3(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level4":
                        if(student.getLevel4()!=null)
                        {
                            level=student.getLevel4();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel4(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level5":
                        if(student.getLevel5()!=null)
                        {
                            level=student.getLevel5();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel5(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level6":
                        if(student.getLevel6()!=null)
                        {
                            level=student.getLevel6();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel6(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level7":
                        if(student.getLevel7()!=null)
                        {
                            level=student.getLevel7();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel7(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level8":
                        if(student.getLevel8()!=null)
                        {
                            level=student.getLevel8();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel8(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level9":
                        if(student.getLevel9()!=null)
                        {
                            level=student.getLevel9();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel9(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level10":
                        if(student.getLevel10()!=null)
                        {
                            level=student.getLevel10();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel10(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level11":
                        if(student.getLevel11()!=null)
                        {
                            level=student.getLevel11();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel11(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level12":
                        if(student.getLevel12()!=null)
                        {
                            level=student.getLevel12();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel12(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level13":
                        if(student.getLevel13()!=null)
                        {
                            level=student.getLevel13();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel13(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level14":
                        if(student.getLevel14()!=null)
                        {
                            level=student.getLevel14();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel14(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;
                    case "level15":
                        if(student.getLevel15()!=null)
                        {
                            level=student.getLevel15();
                        }else{
                            level=new Level();
                        }

                        if(!level.isCompleted())
                        {
                            level.setCompleted(isWin?true:false);
                        }


                        level.setRating(successCount);
                        level.setLevelName(levelName);
                        level.setTotalTime(totalTime);

                        student.setLevel15(level);
                        firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                        break;

                }

            }
        });
    }

    private void refreshLevelIfNotCreated(){
        FirestoreCounting firestoreWriter=new FirestoreCounting();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("CountingGame").document(userID);
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    TotalScore student = documentSnapshot.toObject(TotalScore.class);

                    switch (levelName){
                        case  "level1":
                            if(student.getLevel1()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel1(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level2":
                            if(student.getLevel2()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel2(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level3":
                            if(student.getLevel3()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel3(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level4":
                            if(student.getLevel4()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel4(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level5":
                            if(student.getLevel5()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel5(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level6":
                            if(student.getLevel6()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel6(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level7":
                            if(student.getLevel7()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel7(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level8":
                            if(student.getLevel8()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel8(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level9":
                            if(student.getLevel9()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel9(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level10":
                            if(student.getLevel10()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel10(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level11":
                            if(student.getLevel11()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel11(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level12":
                            if(student.getLevel12()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel12(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level13":
                            if(student.getLevel13()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel13(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level14":
                            if(student.getLevel14()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel14(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;
                        case  "level15":
                            if(student.getLevel15()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel15(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                            }
                            break;

                    }

                }else{

                    TotalScore student = new TotalScore();
                    student.setTotalScore(0);

                    Level level=new Level();
                    level.setLevelName(levelName);


                    switch (levelName){
                        case  "level1":
                            student.setLevel1(level);
                            break;
                        case  "level2":
                            student.setLevel2(level);
                            break;
                        case  "level3":
                            student.setLevel3(level);
                            break;
                        case  "level4":
                            student.setLevel4(level);
                            break;
                        case  "level5":
                            student.setLevel5(level);
                            break;
                        case  "level6":
                            student.setLevel6(level);
                            break;
                        case  "level7":
                            student.setLevel7(level);
                            break;
                        case  "level8":
                            student.setLevel8(level);
                            break;
                        case  "level9":
                            student.setLevel9(level);
                            break;
                        case  "level10":
                            student.setLevel10(level);
                            break;
                        case  "level11":
                            student.setLevel11(level);
                            break;
                        case  "level12":
                            student.setLevel12(level);
                            break;
                        case  "level13":
                            student.setLevel13(level);
                            break;
                        case  "level14":
                            student.setLevel14(level);
                            break;
                        case  "level15":
                            student.setLevel15(level);
                            break;
                    }

                    firestoreWriter.sendGameDataToFirestore(student,fStore,counting.this);
                }
            }
        });
        UpdateFirebase.updateTotalScore(counting.this, Constants.KEY_COLLECTION_COUNTING_GAME);

    }
}