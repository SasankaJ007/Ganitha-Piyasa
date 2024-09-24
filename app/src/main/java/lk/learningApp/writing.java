package lk.learningApp;


import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import lk.learningApp.helper.FirestoreWriter;
import lk.learningApp.helper.LevelUtils.CheckLevel;
import lk.learningApp.helper.UpdateFirebase;
import lk.learningApp.ml.HandWrittenTfLiteModelLite;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;


public class writing extends AppCompatActivity {

    ImageView mainPage, scorePage, imageView;
    LinearLayout check;
    LinearLayout erase,speaker;

    Boolean getTime=false;
    Boolean isStart=false;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    MediaPlayer audio1,audio2,audio3,audio4,audio5,audio6,audio7,audio8,audio9,audio0,audioAdd,audioSum,audioMul,audioDev;

    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    private boolean isTimerRunning = false;
    private int seconds = 0;
    private int totalSeconds = 0;
    String time,totalTime;
    private Handler timerHandler = new Handler();
    private View touchView;
    private final int touchHoldThreshold = 1000; // in milliseconds
    private Handler handler = new Handler();
    boolean result;
    String userAnswer ;
    int currentLevel=1;
    String levelName = "level1";
    int currentCount=1, attemptCount=0, successCount=0;
    String randomNum, randomSymbol, randomMix;

    private static List<String> numbers;
    private static List<String> symbols;
    private static List<String> mix;

    private String displayNumber() {
        if (numbers.size() == 0){
            generateNumbersList();
        }
        String randomNumber = numbers.get(0);
        numbers.remove(0);
        return randomNumber;
    }


    private String displaySymbols() {
        if (symbols.size() == 0){
            generateSymbolList();
        }
        String randomH = symbols.get(0);
        symbols.remove(0);
        return randomH;
    }

    private String displayMix() {
        if (symbols.size() == 0){
            generateMixList();
        }
        String randomM = mix.get(0);
        mix.remove(0);
        return randomM;
    }

    void showRandomNumber(String levelName)
    {

        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
            case "level5":
                randomNumberDisplay();
                break;

            case "level6":
            case "level7":
            case "level8":
            case "level9":
            case "level10":
                randomSymbolsDisplay();
                break;

            case "level11":
            case "level12":
            case "level13":
            case "level14":
            case "level15":
                randomMIXDisplay();
                break;

        }

    }

    void playRandomAudio(String levelName)
    {

        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
            case "level5":
                playNumAudio();
                break;

            case "level6":
            case "level7":
            case "level8":
            case "level9":
            case "level10":
                playSymAudio();
                break;

            case "level11":
            case "level12":
            case "level13":
            case "level14":
            case "level15":
                playMixAudio();
                break;
        }
    }

    void checkCondition(String levelName)
    {
        switch(levelName)
        {
            case "level1":
            case "level2":
            case "level3":
            case "level4":
            case "level5":
                result= numberGameLogic(userAnswer);
                break;

            case "level6":
            case "level7":
            case "level8":
            case "level9":
            case "level10":
                result= symoblsGameLogic(userAnswer);
                break;

            case "level11":
            case "level12":
            case "level13":
            case "level14":
            case "level15":
                result= mixGameLogic(userAnswer);
                break;
        }
    }

    void roundsAndLevels(String levelName)
    {
        boolean res;
        attemptCount++;
        System.out.println(attemptCount);

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
                        res = numberGameLogic(userAnswer);
                        randomNumberDisplay();
                        if (res)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        res =numberGameLogic(userAnswer);
                        randomNumberDisplay();
                        if (res == true)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        res =numberGameLogic(userAnswer);
                        randomNumberDisplay();
                        if (res == true)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        clearBlackBoard();

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
                        res = symoblsGameLogic(userAnswer);
                        randomSymbolsDisplay();
                        if (res == true)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();                       }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        res = symoblsGameLogic(userAnswer);
                        randomSymbolsDisplay();
                        if (res == true)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();                       }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        res = symoblsGameLogic(userAnswer);
                        randomSymbolsDisplay();
                        if (res == true)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        clearBlackBoard();

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
                        res = mixGameLogic(userAnswer);
                        randomMIXDisplay();
                        if (res == true)
                        {
                            showRound01dialog();
                            successCount++;
                        }else{
                            errorDialogD1();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 2:
                        res = mixGameLogic(userAnswer);
                        randomMIXDisplay();
                        if (res == true)
                        {
                            showRound02dialog();
                            successCount++;
                        }else{
                            errorDialogD2();
                        }
                        currentCount++;
                        clearBlackBoard();
                        break;

                    case 3:
                        res = mixGameLogic(userAnswer);
                        randomMIXDisplay();
                        if (res == true)
                        {
                            showRound03dialog();
                            successCount++;
                        }else{
                            errorDialog();
                        }
                        currentCount++;
                        clearBlackBoard();
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
        checkCondition(levelName);
        roundsAndLevels(levelName);
        levelDataGet(levelName,false, CheckLevel.calculateRating(percentage));
        Intent i = new Intent(this, WritingLose.class);
        i.putExtra("TotalTime",totalTime);
        i.putExtra("Percentage", percentage);
        i.putExtra("LevelName",levelName);
        startActivity(i);
        finish();
    }

    public void win(int percentage) {
        String time = totalTime;
        levelDataGet(levelName,true, CheckLevel.calculateRating(percentage));
        resetAllTime();
        Intent i = new Intent(this, WritingWin.class);
        i.putExtra("TotalTime", time);
        i.putExtra("Percentage", percentage);
        i.putExtra("LevelName", levelName);
        startActivity(i);
        finish();
    }

    void randomNumberDisplay(){
        randomNum = displayNumber();
//        showNum.setText(String.valueOf(randomNum));
    }

    void playNumAudio(){

        if (Integer.parseInt(randomNum) == 1) {
            audio1.setLooping(false);
            audio1.start();

        } else if (Integer.parseInt(randomNum) == 2) {
            audio2.setLooping(false);
            audio2.start();

        } else if (Integer.parseInt(randomNum) == 3) {
            audio3.setLooping(false);
            audio3.start();

        } else if (Integer.parseInt(randomNum) == 4) {
            audio4.setLooping(false);
            audio4.start();

        } else if (Integer.parseInt(randomNum) == 5) {
            audio5.setLooping(false);
            audio5.start();

        } else if (Integer.parseInt(randomNum) == 6) {
            audio6.setLooping(false);
            audio6.start();

        } else if (Integer.parseInt(randomNum) == 7) {
            audio7.setLooping(false);
            audio7.start();

        } else if (Integer.parseInt(randomNum) == 8) {
            audio8.setLooping(false);
            audio8.start();

        } else if (Integer.parseInt(randomNum) == 9) {
            audio9.setLooping(false);
            audio9.start();

        } else if (Integer.parseInt(randomNum) == 0) {
            audio0.setLooping(false);
            audio0.start();
        }
    }

    void randomSymbolsDisplay(){
        randomSymbol = displaySymbols();
//        showNum.setText(String.valueOf(randomSymbol));
    }

    void playSymAudio(){
        if (randomSymbol=="+") {
            audioAdd.setLooping(false);
            audioAdd.start();

        }
        else if (randomSymbol=="-") {
            audioSum.setLooping(false);
            audioSum.start();

        }
        else if (randomSymbol=="*") {
            audioMul.setLooping(false);
            audioMul.start();

        }
        else if (randomSymbol=="/") {
            audioDev.setLooping(false);
            audioDev.start();

        }
    }

    void randomMIXDisplay(){
        randomMix = displayMix();
//        showNum.setText(String.valueOf(randomMix));
    }

    void playMixAudio(){
        System.out.println("RANDOM MIX : " + randomMix);
        switch (randomMix) {
            case "+":
                audioAdd.setLooping(false);
                audioAdd.start();
                break;
            case "-":
                audioSum.setLooping(false);
                audioSum.start();
                break;
            case "*":
                audioMul.setLooping(false);
                audioMul.start();
                break;
            case "/":
                audioDev.setLooping(false);
                audioDev.start();
                break;
            case "1":
                audio1.setLooping(false);
                audio1.start();
                break;
            case "2":
                audio2.setLooping(false);
                audio2.start();
                break;
            case "3":
                audio3.setLooping(false);
                audio3.start();
                break;
            case "4":
                audio4.setLooping(false);
                audio4.start();
                break;
            case "5":
                audio5.setLooping(false);
                audio5.start();
                break;
            case "6":
                audio6.setLooping(false);
                audio6.start();
                break;
            case "7":
                audio7.setLooping(false);
                audio7.start();
                break;
            case "8":
                audio8.setLooping(false);
                audio8.start();
                break;
            case "9":
                audio9.setLooping(false);
                audio9.start();
                break;
            case "0":
                audio0.setLooping(false);
                audio0.start();
                break;
            default:
                // Handle the case when randomMix doesn't match any of the cases
        }

    }

    boolean numberGameLogic(String userAnswer)
    {
        return randomNum.equals(userAnswer);
    }

    boolean symoblsGameLogic(String userAnswer)
    {
        return randomSymbol.equals(userAnswer);
    }

    boolean mixGameLogic(String userAnswer)
    {
        return randomMix.equals(userAnswer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        mainPage = findViewById(R.id.btnHome);
        imageView = findViewById(R.id.writingView);
        check = findViewById(R.id.checkBlock1);
        scorePage=findViewById(R.id.btnScore);
        erase=findViewById(R.id.btnErase);
        speaker=findViewById(R.id.btnWriting);

        audio1    =MediaPlayer.create(writing.this,R.raw.one);
        audio2    =MediaPlayer.create(writing.this,R.raw.two);
        audio3    =MediaPlayer.create(writing.this,R.raw.three);
        audio4    =MediaPlayer.create(writing.this,R.raw.four);
        audio5    =MediaPlayer.create(writing.this,R.raw.five);
        audio6    =MediaPlayer.create(writing.this,R.raw.six);
        audio7    =MediaPlayer.create(writing.this,R.raw.seven);
        audio8    =MediaPlayer.create(writing.this,R.raw.eight);
        audio9    =MediaPlayer.create(writing.this,R.raw.nine);
        audio0    =MediaPlayer.create(writing.this,R.raw.zero);
        audioAdd=MediaPlayer.create(writing.this,R.raw.adding);
        audioSum=MediaPlayer.create(writing.this,R.raw.sum);
        audioMul=MediaPlayer.create(writing.this,R.raw.mul);
        audioDev=MediaPlayer.create(writing.this,R.raw.div);

        fAuth       =FirebaseAuth.getInstance();
        fStore      =FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        numbers = new ArrayList<>();
        generateNumbersList();

        symbols = new ArrayList<>();
        generateSymbolList();

        mix = new ArrayList<>();
        generateMixList();

        Collections.shuffle(numbers);
        Collections.shuffle(symbols);
        Collections.shuffle(mix);

        if(getIntent()!=null) {
            levelName=getIntent().getStringExtra("selectedLevel");

            refreshLevelIfNotCreated();

        }
        showRandomNumber(levelName);

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
                if(!isStart || bitmap == null){
                    Toast.makeText(writing.this, "පිළිතුරුක් ලබා දෙන්න!", Toast.LENGTH_SHORT).show();
                    return;
                }
                isStart = false;
                drawPaintSketchImage();
                userAnswer = classifyImage(bitmap);
                bitmap = null;
                stopTime();
                getTime=false;
                checkCondition(levelName);
                roundsAndLevels(levelName);

                check.setBackgroundColor(ContextCompat.getColor(writing.this, android.R.color.darker_gray));
                // You might want to reset the background color after a short delay to indicate the click effect
                check.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check.setBackgroundColor(ContextCompat.getColor(writing.this, R.color.block2color));
                    }
                }, 100);

            }
        });

        speaker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTimer();
                        playRandomAudio(levelName);
                        if(!getTime){
                            getTime=true;
                            resetTime();
                            startTime();
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

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearBlackBoard();

                erase.setBackground(ContextCompat.getDrawable(writing.this, R.drawable.clickerasebg));
                erase.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erase.setBackground(ContextCompat.getDrawable(writing.this, R.drawable.erasebg));

                    }
                }, 100);

            }
        });
    }

    private void generateSymbolList() {
        String[] sy = {"+", "-", "*", "/"};
        for (int i=0; i<4; i++)
        {
            symbols.add(sy[i]);
        }
    }
    private void generateNumbersList() {
        String[] num = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for (int i=0; i<10; i++)
        {
            numbers.add(num[i]);
        }
    }
    private void generateMixList(){
        String[] min = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0","+", "-", "*", "/"};
        for (int i=0; i<14; i++)
        {
            mix.add(min[i]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void clearBlackBoard(){
        if (canvas != null) {
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            imageView.setImageBitmap(null);
        }
    }

    private void performTouchAndHoldAction() {
        YoYo.with(Techniques.Swing)
                .duration(1000)
                .playOn(findViewById(R.id.btnWriting));

    }

    private void showRound01dialog(){
        ConstraintLayout D1 = findViewById(R.id.SuccessRound01ConstraintLayout);
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_success_round01_dialogbox, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_success_round01_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_success_round03_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_error_d1, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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
        View view = LayoutInflater.from(writing.this).inflate(R.layout.writing_error_dialogbox, D1);
        Button  ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(writing.this);
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

    public String classifyImage(Bitmap image) {
        try {
            HandWrittenTfLiteModelLite model = HandWrittenTfLiteModelLite.newInstance(writing.this);

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
                    // Uncomment these two lines if that array size {1, 150, 150, 3} like this
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.F / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.F / 1));
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
            boolean isHaveProbability = false;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPossibility = i;
                    isHaveProbability = true;
                }
            }
            System.out.println("result : "+ Constants.resultMappedClass[maxPossibility]);
            model.close();
            if (!isHaveProbability && confidences[0] == 0)
                return "-1";
            return Constants.resultMappedClass[maxPossibility];
        } catch (IOException e) {
            // TODO Handle the exception
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return "-1";
        }
    }

    public void drawPaintSketchImage() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

        }
        canvas.drawLine(floatStartX,
                floatStartY - 270,
                floatEndX,
                floatEndY - 270,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }

        return super.onTouchEvent(event);
    }

    private void levelDataGet(String levelName,boolean isWin, int successCount) {
        FirestoreWriter firestoreWriter=new FirestoreWriter();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("WritingGame").document(userID);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
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
                        firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                        break;

                }

            }
        });
    }

    private void refreshLevelIfNotCreated(){
        FirestoreWriter firestoreWriter=new FirestoreWriter();
        String userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("WritingGame").document(userID);
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
                                level.setRating(0);

                                student.setLevel1(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level2":
                            if(student.getLevel2()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel2(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level3":
                            if(student.getLevel3()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel3(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level4":
                            if(student.getLevel4()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel4(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level5":
                            if(student.getLevel5()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel5(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level6":
                            if(student.getLevel6()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel6(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level7":
                            if(student.getLevel7()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel7(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level8":
                            if(student.getLevel8()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel8(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level9":
                            if(student.getLevel9()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel9(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level10":
                            if(student.getLevel10()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel10(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level11":
                            if(student.getLevel11()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel11(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level12":
                            if(student.getLevel12()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);


                                student.setLevel12(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level13":
                            if(student.getLevel13()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel13(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level14":
                            if(student.getLevel14()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel14(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;
                        case  "level15":
                            if(student.getLevel15()==null)
                            {
                                student.setTotalScore(0);

                                Level level=new Level();
                                level.setLevelName(levelName);
                                level.setRating(0);

                                student.setLevel15(level);
                                firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                            }
                            break;

                    }

                }else{

                    TotalScore student = new TotalScore();
                    student.setTotalScore(0);

                    Level level=new Level();
                    level.setLevelName(levelName);
                    level.setRating(0);

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

                    firestoreWriter.sendGameDataToFirestore(student,fStore,writing.this);
                }
            }
        });
        UpdateFirebase.updateTotalScore(writing.this, Constants.KEY_COLLECTION_WRITING_GAME);
    }

}