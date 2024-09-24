package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class CountingLose extends AppCompatActivity {

    MediaPlayer bgMusic;

    TextView showTime, txtAcu;
    RelativeLayout navGame;

    String time;
    int percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting_lose);

        showTime= findViewById(R.id.txtTime);
        navGame = findViewById(R.id.backGame);
        txtAcu = findViewById(R.id.txtAcu);

        if(getIntent()!=null) {
            time=getIntent().getStringExtra("TotalTime");
            percentage = getIntent().getIntExtra("Percentage", 0);
        }

        showTime.setText(time.toString());
        txtAcu.setText(percentage+"%");

        bgMusic = MediaPlayer.create(CountingLose.this, R.raw.lose);
        startMusic();

        navGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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