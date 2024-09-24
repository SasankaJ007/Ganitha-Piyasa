package lk.learningApp;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.squti.androidwaverecorder.WaveConfig;
import com.github.squti.androidwaverecorder.WaveRecorder;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.cloud.speech.v1.RecognitionAudio;
//import com.google.cloud.speech.v1.RecognitionConfig;
//import com.google.cloud.speech.v1.RecognizeResponse;
//import com.google.cloud.speech.v1.SpeechClient;
//import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
//import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.logging.type.HttpRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.ResponseModels.APIResponse;
import lk.learningApp.ResponseModels.SpeakingResponse;
import lk.learningApp.helper.FirestoreSpeacker;
import lk.learningApp.helper.LevelUtils.CheckLevel;
import lk.learningApp.helper.UpdateFirebase;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.protobuf.ByteString;

public class speaking extends AppCompatActivity {
    ImageView mainPage,scorePage;
    TextView showNum;
    LinearLayout check;
    ImageButton userInput;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private static int Microphone_Permission_Code = 200;
    private WaveRecorder waveRecorder;
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
    private static List<String> numbers;
    String levelName="level1";
    int currentCount=1, attemptCount=0, successCount=0;
    String randomNum, userAnswer;

    private OkHttpClient client;
    private String displayNumber() {
        if (numbers.size() == 0){
            generateNumbersList();
        }
        String randomNumber = numbers.get(0);
        numbers.remove(0);
        return randomNumber;
    }
    private void generateNumbersList() {
        String[] num = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for (int i=0; i<10; i++) {
            numbers.add(num[i]);
        }
    }
    void randomNumberDisplay(){
        randomNum = displayNumber();
        showNum.setText(String.valueOf(randomNum));
    }

    boolean checkCondition() {
        return userAnswer.equals(randomNum);
    }

    void roundsAndLevels() {
        boolean res = checkCondition();
        attemptCount++;
        switch (currentCount)
        {
            case 1:
                randomNumberDisplay();
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
                randomNumberDisplay();
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
                randomNumberDisplay();
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
        roundsAndLevels();
        levelDataGet(levelName,false, CheckLevel.calculateRating(percentage));
        Intent i = new Intent(this, SpeakingLose.class);
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
        Intent i = new Intent(this, SpeakingWin.class);
        i.putExtra("TotalTime",time);
        i.putExtra("Percentage", percentage);
        i.putExtra("LevelName",levelName);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);

        client =  new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS) // Increase the read timeout
                .writeTimeout(60, TimeUnit.SECONDS) // Increase the write timeout
                .build();

        mainPage = findViewById(R.id.btnHome);
        showNum  = findViewById(R.id.speakTextView);
        check    = findViewById(R.id.checkBlock1);
        scorePage=findViewById(R.id.btnScore);
        userInput=findViewById(R.id.btnSpeaking);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        if(getIntent()!=null) {
            levelName=getIntent().getStringExtra("selectedLevel");
            refreshLevelIfNotCreated();
        }

        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

        numbers = new ArrayList<>();
        generateNumbersList();

        Collections.shuffle(numbers);
        randomNumberDisplay();

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


        userInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
                        cancelTimer();
                        break;
                }
                return true;

            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isStart){
                    Toast.makeText(speaking.this, "පිළිතුරුක් ලබා දෙන්න!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    stopTime();
                    getTime=false;
                    if (isRecording){
                        stopRecording();
                    }
                    isStart = false;
                    sendAPIRequest();
                }

                check.setBackgroundColor(ContextCompat.getColor(speaking.this, android.R.color.darker_gray));
                check.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check.setBackgroundColor(ContextCompat.getColor(speaking.this, R.color.block1color
                        ));
                    }
                }, 100);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (waveRecorder != null){
            waveRecorder.stopRecording();
            waveRecorder = null;
        }
        finish();
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
                .url(Constants.BASE_URL +"/speech/") // Replace with your API endpoint
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
//                            System.out.println("Speaking Response Successful!!" + responseBody);
                            SpeakingResponse speakingResponse = new Gson().fromJson(responseBody, SpeakingResponse.class);
                            userAnswer= speakingResponse.getNumber();
                            System.out.println("Correct Answer : " + randomNum);
                            System.out.println("User Answer : " + userAnswer);
                            roundsAndLevels();
                        }
                    });
                } else {
                    // Handle API error
                    System.out.println("Response error!!");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(speaking.this, "Speaking Response Failed!!", Toast.LENGTH_SHORT).show();
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

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                seconds++;
                totalSeconds++; // Increment the total time
                int minutes = seconds / 60;
                int remainderSeconds = seconds % 60;
                time = String.format("%02d:%02d", minutes, remainderSeconds);
                // Update the total time TextView
                int totalMinutes = totalSeconds / 60;
                int totalRemainderSeconds = totalSeconds % 60;
                totalTime = String.format("%02d:%02d", totalMinutes, totalRemainderSeconds);
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
        File file = new File(musicDirectory, "speak" + ".wav");
        return file.getPath();
    }

    private void showRound01dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_success_round01_dialogbox, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_success_round01_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_success_round03_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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

    private void errorDialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(speaking.this).inflate(R.layout.speaking_error_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speaking.this);
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
    }

    private void performTouchAndHoldAction() {
        YoYo.with(Techniques.Swing)
                .duration(1000)
                .playOn(findViewById(R.id.btnSpeaking));
    }

    private void levelDataGet(String levelName,boolean isWin, int successCount) {
        FirestoreSpeacker firestoreWriter=new FirestoreSpeacker();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("SpeakingGame").document(userID);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                        break;

                }

            }
        });
    }

    private void refreshLevelIfNotCreated(){
        FirestoreSpeacker firestoreWriter=new FirestoreSpeacker();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("SpeakingGame").document(userID);
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
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level2":
                            if(student.getLevel2()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel2(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level3":
                            if(student.getLevel3()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel3(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level4":
                            if(student.getLevel4()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel4(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level5":
                            if(student.getLevel5()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel5(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level6":
                            if(student.getLevel6()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel6(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level7":
                            if(student.getLevel7()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel7(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level8":
                            if(student.getLevel8()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel8(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level9":
                            if(student.getLevel9()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel9(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level10":
                            if(student.getLevel10()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel10(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level11":
                            if(student.getLevel11()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel11(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level12":
                            if(student.getLevel12()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel12(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level13":
                            if(student.getLevel13()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel13(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level14":
                            if(student.getLevel14()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel14(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                            }
                            break;
                        case  "level15":
                            if(student.getLevel15()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel15(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
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

                    firestoreWriter.sendGameDataToFirestore(student,fStore,speaking.this);
                }
            }
        });
        UpdateFirebase.updateTotalScore(speaking.this, Constants.KEY_COLLECTION_SPEAKING_GAME);
    }

    @Override
    protected void onPause() {
        if (waveRecorder != null)
            waveRecorder.pauseRecording();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (waveRecorder != null)
            waveRecorder.resumeRecording();
        super.onResume();
    }
}