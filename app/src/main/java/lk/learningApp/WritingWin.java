package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.regex.*;

import lk.learningApp.helper.LevelUtils.CheckLevel;


public class WritingWin extends AppCompatActivity {

    MediaPlayer bgMusic;

    TextView showTime,progressLevel, txtAcu;
    RatingBar rating;
    RelativeLayout navGame;


    String time,LevelName;
    int percentage;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_win);

        showTime= findViewById(R.id.txtTime);
        navGame = findViewById(R.id.backGame);
        progressLevel=findViewById(R.id.txtLev);
        rating  =findViewById(R.id.ratingBar);
        txtAcu  = findViewById(R.id.txtAcu);

        bgMusic    =MediaPlayer.create(WritingWin.this,R.raw.win);

        startMusic();

        if(getIntent()!=null) {
            time=getIntent().getStringExtra("TotalTime");
            LevelName=getIntent().getStringExtra("LevelName");
            percentage = getIntent().getIntExtra("Percentage", 0);
        }

        showTime.setText(time.toString());
        txtAcu.setText(percentage+"%");
        rating.setRating(CheckLevel.calculateRating(percentage));
        if(percentage < 75 ){
            progressLevel.setText("හොඳයි");
        }else {
            progressLevel.setText("ඉතා හොඳයි");
        }


        navGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WritingLevelBoard.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), WritingLevelBoard.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (bgMusic != null && !bgMusic.isPlaying()) {
            bgMusic.start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
    }

    void startMusic(){
        bgMusic.setLooping(true);
        bgMusic.start();
    }
}