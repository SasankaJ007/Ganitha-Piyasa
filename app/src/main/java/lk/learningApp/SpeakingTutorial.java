package lk.learningApp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import lk.learningApp.helper.videoUtills.CustomMediaController;

public class SpeakingTutorial extends AppCompatActivity {

    VideoView videoView;
    ImageView backHome;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_tutorial);

        backHome  = findViewById(R.id.btnHome);

        videoView = findViewById(R.id.video);
        MediaController mediaController=new CustomMediaController(this);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.speaking));
        videoView.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.show();
            }
        },300);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}