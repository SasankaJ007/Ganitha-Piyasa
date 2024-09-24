package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class FormulaLose extends AppCompatActivity {

    MediaPlayer bgMusic;
    TextView showTime, txtAcu;
    RelativeLayout navGame;

    String time;
    int percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_lose);

        if(getIntent()!=null) {
            time=getIntent().getStringExtra("TotalTime");
            percentage = getIntent().getIntExtra("Percentage", 0);
        }

        showTime= findViewById(R.id.txtTime);
        navGame = findViewById(R.id.backGame);
        txtAcu = findViewById(R.id.txtAcu);

        bgMusic = MediaPlayer.create(FormulaLose.this, R.raw.lose);
        startMusic();

        showTime.setText(time.toString());
        txtAcu.setText(percentage+"%");

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