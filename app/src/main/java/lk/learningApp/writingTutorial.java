package lk.learningApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import java.util.Set;

import lk.learningApp.helper.videoUtills.CustomMediaController;
public class writingTutorial extends AppCompatActivity {

    VideoView videoView;
    ImageView backHome;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_tutorial);

        backHome  = findViewById(R.id.btnHome);

        videoView = findViewById(R.id.video);
        MediaController mediaController = new CustomMediaController(this);

        // Disable auto-hide behavior
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.writing));
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