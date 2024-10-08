package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class speakingNavigate extends AppCompatActivity {

    LinearLayout video,game;
    ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_navigate);

        video = findViewById(R.id.videoPlay);
        game = findViewById(R.id.gamePlay);
        cancel =findViewById(R.id.cancelmage);


        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpeakingLevelBoard.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(speakingNavigate.this, SpeakingTutorial.class));
            }
        });
    }

}