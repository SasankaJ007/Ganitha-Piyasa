package lk.learningApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.helper.FirestoreLogicGame;
import lk.learningApp.helper.UpdateFirebase;
import lk.learningApp.ml.HandWrittenTfLiteModelLite;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;

public class speak extends AppCompatActivity {

    ImageView mainPage,scorePage,imageView1,imageView2;
    LinearLayout check;
    TextView showLogic;
    LinearLayout erase;
    LinearLayout speaker;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference collectionRef;
    DocumentReference documentRef;
    String userID;

    private float floatStartX = -1, floatStartY = -100,
            floatEndX = -1, floatEndY = -100;

    private Bitmap bitmap1, bitmap2;
    private Canvas canvas1, canvas2;
    private Paint paint1;


    Boolean getTime=false;
    Boolean isStart=false;
    private boolean isTimerRunning = false;
    private int seconds = 0;
    private int totalSeconds = 0;
    String time,totalTime;
    //    private int timerSessions = 0;
    private Handler timerHandler = new Handler();
    private View touchView;
    private final int touchHoldThreshold = 1000; // in milliseconds
    private Handler handler = new Handler();

    private static List<Integer> characters;
    private static List<Integer> numbers;

    int currentLevel=1, correctAnswer;
    int currentCount=1;
    int successCount=0;
    int random1, random2;
    char symbol;
    String levelName="level1";
    int userAnswer;

    MediaPlayer bgMusic,audioNumber01,audioSymbol,audioNumber02;

    private MediaPlayer getMediaPlayer(String symbol){
        int id = 0;
        switch (symbol){
            case "0":
                id = R.raw.zero_audio;
                break;
            case "1":
                id = R.raw.one_audio;
                break;
            case "2":
                id = R.raw.two_audio;
                break;
            case "3":
                id = R.raw.three_audio;
                break;
            case "4":
                id = R.raw.four_audio;
                break;
            case "5":
                id = R.raw.five_audio;
                break;
            case "6":
                id = R.raw.six_audio;
                break;
            case "7":
                id = R.raw.seven_audio;
                break;
            case "8":
                id = R.raw.eight_audio;
                break;
            case "9":
                id = R.raw.nine_audio;
                break;
            case "10":
                id = R.raw.ten_audio;
                break;
            case "11":
                id = R.raw.eleven_audio;
                break;
            case "12":
                id = R.raw.twelve_audio;
                break;
            case "13":
                id = R.raw.thirteen_audio;
                break;
            case "14":
                id = R.raw.fourteen_audio;
                break;
            case "15":
                id = R.raw.fifteen_audio;
                break;
            case "16":
                id = R.raw.sixteen_audio;
                break;
            case "17":
                id = R.raw.seventeen_audio;
                break;
            case "18":
                id = R.raw.eighteen_audio;
                break;
            case "19":
                id = R.raw.nineteen_audio;
                break;
            case "20":
                id = R.raw.twenty_audio;
                break;
            case "21":
                id = R.raw.twentyone_audio;
                break;
            case "22":
                id = R.raw.twentytwo_audio;
                break;
            case "23":
                id = R.raw.twentythree_audio;
                break;
            case "24":
                id = R.raw.twentyfour_audio;
                break;
            case "25":
                id = R.raw.twentyfive_audio;
                break;
            case "26":
                id = R.raw.twentysix_audio;
                break;
            case "27":
                id = R.raw.twentyseven_audio;
                break;
            case "28":
                id = R.raw.twentyeight_audio;
                break;
            case "29":
                id = R.raw.twentynine_audio;
                break;
            case "30":
                id = R.raw.thirty_audio;
                break;
            case "31":
                id = R.raw.thirtyone_audio;
                break;
            case "32":
                id = R.raw.thirtytwo_audio;
                break;
            case "33":
                id = R.raw.thirtythree_audio;
                break;
            case "34":
                id = R.raw.thirtyfour_audio;
                break;
            case "35":
                id = R.raw.thirtyfive_audio;
                break;
            case "36":
                id = R.raw.thirtysix_audio;
                break;
            case "*":
                id = R.raw.gunakirima;
                break;
            case  "/":
                id = R.raw.bedima;
                break;
            case "+":
                id = R.raw.ekatukirima;
                break;
            case "-":
                id = R.raw.adukirima;
                break;
            default:
                id = R.raw.win;
                break;
        }

        return MediaPlayer.create(speak.this, id);
    }

    private static int displayNumber() {
        int randomNumber = characters.get(0);
        characters.remove(0);
        return randomNumber;
    }

    private static int numbersForMulSub() {
        int randomNumber = numbers.get(0);
        numbers.remove(0);
        return randomNumber;
    }
    void logicShowInLevels(String levelName) {

        switch (levelName) {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
                displayAddition();

                break;

            case "level5":
            case "level6":
            case "level7":
            case "level8":
                displaySubstraction();

                break;

            case "level9":
            case "level10":
            case "level11":
            case "level12":
                displayMultiplication();

                break;

            case "level13":
            case "level14":
            case "level15":
                displayDivition();

                break;


        }
    }

    boolean checkCondition(int userAnswer)
    {
        boolean res = false;

        if (userAnswer != -1){
            if (userAnswer == correctAnswer){
                res = true;
                Toast.makeText(speak.this, "Your Result is Correct", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(speak.this, "Your Result is "+ userAnswer +"\nExpected : "+ correctAnswer, Toast.LENGTH_SHORT).show();
        }else {
            res = false;
            Toast.makeText(speak.this, "Please Draw Again", Toast.LENGTH_SHORT).show();
        }

        return res;
    }

    void roundsAndLevels(String levelName)
    {
        boolean res;

        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":

                switch (currentCount)
                {
                    case 1:
                        displayAddition();
                        res = checkCondition(userAnswer);
                        if (res)
                        {
                            Toast.makeText(this, "win round 1", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 1", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        displayAddition();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 2", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 2", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        displayAddition();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 3", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 3", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();

                        checkSuccessCount();
                        break;

                }

                break;

            case "level5":
            case "level6":
            case "level7":
            case "level8":

                switch (currentCount)
                {

                    case 1:
                        displaySubstraction();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 1", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 1", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        displaySubstraction();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 2", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 2", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        displaySubstraction();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 3", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 3", Toast.LENGTH_SHORT).show();

                        }
                        currentCount++;
                        clearBlackBoard();

                        checkSuccessCount();
                        break;

                }

                break;

            case "level9":
            case "level10":
            case "level11":
            case "level12":

                switch (currentCount)
                {

                    case 1:
                        displayMultiplication();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 1", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 1", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        displayMultiplication();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 2", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 2", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        displayMultiplication();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 3", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 3", Toast.LENGTH_SHORT).show();

                        }
                        currentCount++;
                        clearBlackBoard();

                        checkSuccessCount();
                        break;

                }

                break;

            case "level13":
            case "level14":
            case "level15":

                switch (currentCount)
                {

                    case 1:
                        displayDivition();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 1", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 1", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        displayDivition();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 2", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 2", Toast.LENGTH_SHORT).show();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        displayDivition();
                        res = checkCondition(userAnswer);
                        if (res == true)
                        {
                            Toast.makeText(this, "win round 3", Toast.LENGTH_SHORT).show();
                            successCount++;
                        }else{
                            Toast.makeText(this, "lose round 3", Toast.LENGTH_SHORT).show();

                        }
                        currentCount++;
                        clearBlackBoard();

                        checkSuccessCount();
                        break;

                }

                break;

        }


    }
    public void checkSuccessCount(){
        if(currentCount>=3)
        {
            if(successCount>=2){
                Toast.makeText(this, "You Win bro", Toast.LENGTH_SHORT).show();
                win();
            }else{
                Toast.makeText(this, "let's try again", Toast.LENGTH_SHORT).show();

                reset();
            }
        }else {
            checkCondition(userAnswer);
            roundsAndLevels(levelName);
        }
    }

    public void reset()
    {
        currentCount=1;
        checkCondition(userAnswer);
        roundsAndLevels(levelName);

        levelDataGet(levelName,false);

        Intent i = new Intent(this, FormulaLose.class);
        i.putExtra("TotalTime",totalTime);
        i.putExtra("LevelName",levelName);
        startActivity(i);
    }
    public void win()
    {
        String time=totalTime;
        levelDataGet(levelName,true);
        resetAllTime();

        Intent i = new Intent(this, WritingWin.class);
        i.putExtra("TotalTime",time);
        i.putExtra("Rating",String.valueOf(successCount));
        i.putExtra("LevelName",levelName);
        startActivity(i);


    }

    void displayAddition(){
        random1 = displayNumber();
        random2 = displayNumber();
        correctAnswer = random1 + random2;
        symbol = '+';
        showLogic.setText(String.valueOf(random1)+"+"+String.valueOf(random2));
    }

    void displaySubstraction(){
        random1 = displayNumber();
        random2 = displayNumber();
        if (random2 >= random1) {
            // Swap the values of random1 and random2
            int temp = random1;
            random1 = random2;
            random2 = temp;
        }
        symbol = '-';
        correctAnswer = random1 - random2;
        showLogic.setText(String.valueOf(random1)+"-"+String.valueOf(random2));
    }

    void displayMultiplication(){
        random1 = numbersForMulSub();
        random2 = numbersForMulSub();
        correctAnswer = random1 * random2;
        symbol = 'x';
        showLogic.setText(String.valueOf(random1)+"x"+String.valueOf(random2));
    }

    void displayDivition(){
        correctAnswer = numbersForMulSub();
        random2 = numbersForMulSub();
        random1 = correctAnswer*random2;
        symbol = '/';
        showLogic.setText(random1+"÷"+String.valueOf(random2));


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        mainPage  = findViewById(R.id.btnHome);
        imageView1 = findViewById(R.id.answerView1);
        imageView2 = findViewById(R.id.answerView2);
        scorePage =findViewById(R.id.btnScore);
        check     =findViewById(R.id.checkBlock4);
        showLogic = findViewById(R.id.showLogic);
        erase     =findViewById(R.id.btnErase);
        speaker   =findViewById(R.id.btnLogic);
        audioNumber01 = MediaPlayer.create(speak.this, R.raw.win);
        audioNumber02 = audioSymbol = audioNumber01;
        AudioPlayerListeners();

        fAuth       =FirebaseAuth.getInstance();
        fStore      =FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        if(getIntent()!=null) {
            currentLevel=getIntent().getIntExtra("Level",1);
            levelName=getIntent().getStringExtra("selectedLevel");

            refreshLevelIfNotCreated();
        }

        characters = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            characters.add(i);
        }

        numbers = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            numbers.add(i);
        }

        Collections.shuffle(characters);
        Collections.shuffle(numbers );

        logicShowInLevels(levelName);

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

        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!isStart){
                    Toast.makeText(speak.this, "First you have to give the answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                stopTime();
                getTime=false;

                drawPaintSketchImage();
                String a = classifyImage(bitmap1);
                String b = classifyImage(bitmap2);

                System.out.println("answer 01 : "+ a +"\nanswer 02 : "+b);


               userAnswer = concatResult(a,b);
               roundsAndLevels(levelName);


//                if (userAnswer != -1){
//                    if (userAnswer == correctAnswer){
//                        Toast.makeText(speak.this, "Your Result is Correct", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(speak.this, "Your Result is "+ userAnswer +"\nExpected : "+ correctAnswer, Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(speak.this, "Please Draw Again", Toast.LENGTH_SHORT).show();
//                }


                check.setBackgroundColor(ContextCompat.getColor(speak.this, android.R.color.darker_gray));
                // You might want to reset the background color after a short delay to indicate the click effect
                check.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check.setBackgroundColor(ContextCompat.getColor(speak.this, R.color.block4color
                        ));

                    }
                }, 100);

            }
        });

        speaker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start timer when user touches down
                        startTimer();
                        playDisplayedFunction();

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

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearBlackBoard();


                erase.setBackground(ContextCompat.getDrawable(speak.this, R.drawable.clickerasebg));
                // You might want to reset the background color after a short delay to indicate the click effect
                erase.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erase.setBackground(ContextCompat.getDrawable(speak.this, R.drawable.erasebg));

                    }
                }, 100);

            }
        });
    }

    private void playDisplayedFunction() {
        audioNumber01 = getMediaPlayer(String.valueOf(random1));
        audioSymbol = getMediaPlayer(String.valueOf(symbol));
        audioNumber02 = getMediaPlayer(String.valueOf(random2));
        audioNumber01.start();
        AudioPlayerListeners();
    }
    private void AudioPlayerListeners(){

        audioNumber01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release(); // Release mediaPlayer to free up resources
                // Delay the start of the second audio file for 500 milliseconds (0.5 second)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        audioSymbol.start();
                    }
                }, 0);
            }
        });
        audioSymbol.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release(); // Release mediaPlayer to free up resources
                // Delay the start of the second audio file for 500 milliseconds (0.5 second)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        audioNumber02.start();
                    }
                }, 0);
            }
        });
        audioNumber02.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();  // Release mediaPlayer to free up resources
            }
        });
    }

    private int concatResult(String a, String b) {
        // Concatenate the strings
        String result = a + b;
        // Parse the result back to an integer if needed
        if (result.trim().equals(""))
            return -1;
        return Integer.parseInt(result);
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
//        timerSessions = 0;


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
        YoYo.with(Techniques. Swing)
                .duration(1000)
                .playOn(findViewById(R.id.btnLogic));

//        Log.d("TAG", "onTouch: work2");

    }

    private void clearBlackBoard(){
        if (canvas1 != null) {
            canvas1.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            imageView1.setImageBitmap(null);
        }
        if (canvas2 != null) {
            canvas2.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            imageView2.setImageBitmap(null);
        }
    }

    private void showStar01dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(speak.this).inflate(R.layout.formula_star01_dialogbox, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speak.this);
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

    private void showStar02dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(speak.this).inflate(R.layout.formula_star02_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speak.this);
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

    private void showStar03dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(speak.this).inflate(R.layout.formula_star03_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speak.this);
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
        View view = LayoutInflater.from(speak.this).inflate(R.layout.formula_error_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(speak.this);
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
    public String classifyImage(Bitmap image) {
        try {
            HandWrittenTfLiteModelLite model = HandWrittenTfLiteModelLite.newInstance(speak.this);

            // Resize the image to the preferred size (150x150)
            int imageSize = 150;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] vals = new int[imageSize * imageSize];
            resizedBitmap.getPixels(vals, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = vals[pixel++];
                    byteBuffer.putFloat((val & 0xFF) * (1.F / 1));
                }
            }

            //load the image to tensorflow
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            HandWrittenTfLiteModelLite.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            int maxPossibility = 0;
            float maxConfidence = 0;

            for (int i = 2; i < confidences.length; i++) {
                if (i==5 || i==10)
                    continue;
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPossibility = i;
                }
            }
            model.close();
            if (maxPossibility == 0)
                return "";
            return Constants.resultMappedClass[maxPossibility];
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return "";
        }

    }
    public void drawPaintSketchImage(){

        // First ImageView (imageView1)
        if (bitmap1 == null){
            bitmap1 = Bitmap.createBitmap(imageView1.getWidth(),
                    imageView1.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas1 = new Canvas(bitmap1);
        }
        if (paint1 == null) {
            paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setStrokeWidth(20);
        }

        // Draw on the first ImageView
        canvas1.drawLine(floatStartX,
                floatStartY - 750,
                floatEndX,
                floatEndY - 750,
                paint1);
        imageView1.setImageBitmap(bitmap1);

        // Second ImageView (imageView2)
        if (bitmap2 == null){
            bitmap2 = Bitmap.createBitmap(imageView2.getWidth(),
                    imageView2.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas2 = new Canvas(bitmap2);
        }

// Draw on the second ImageView
        canvas2.drawLine(floatStartX - 550,
                floatStartY - 750,
                floatEndX - 550,
                floatEndY - 750,
                paint1);

        imageView2.setImageBitmap(bitmap2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }

        return super.onTouchEvent(event);
    }

    private void levelDataGet(String levelName,boolean isWin) {
        FirestoreLogicGame firestoreWriter=new FirestoreLogicGame();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("LogicGame").document(userID);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                        break;

                }

            }
        });
        UpdateFirebase.updateTotalScore(speak.this, Constants.KEY_COLLECTION_SPEAKING_GAME);
    }

    private void refreshLevelIfNotCreated(){
        FirestoreLogicGame firestoreWriter=new FirestoreLogicGame();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("LogicGame").document(userID);
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
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level2":
                            if(student.getLevel2()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel2(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level3":
                            if(student.getLevel3()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel3(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level4":
                            if(student.getLevel4()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel4(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level5":
                            if(student.getLevel5()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel5(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level6":
                            if(student.getLevel6()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel6(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level7":
                            if(student.getLevel7()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel7(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level8":
                            if(student.getLevel8()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel8(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level9":
                            if(student.getLevel9()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel9(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level10":
                            if(student.getLevel10()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel10(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level11":
                            if(student.getLevel11()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel11(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level12":
                            if(student.getLevel12()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);


                                student.setLevel12(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level13":
                            if(student.getLevel13()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel13(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level14":
                            if(student.getLevel14()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel14(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                            }
                            break;
                        case  "level15":
                            if(student.getLevel15()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);

                                student.setLevel15(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
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

                    firestoreWriter.sendGameDataToFirestore(student,fStore,speak.this);
                }
            }
        });

        UpdateFirebase.updateTotalScore(speak.this, Constants.KEY_COLLECTION_SPEAKING_GAME);
    }

}