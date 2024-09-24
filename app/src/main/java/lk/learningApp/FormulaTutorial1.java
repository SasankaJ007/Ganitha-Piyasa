package lk.learningApp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FormulaTutorial1 extends AppCompatActivity {

    VideoView videoView1, videoView2, videoView3, videoView4;
    ImageView backHome;
    VideoView[] videoViews;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_tutorial);

        backHome  = findViewById(R.id.btnHome);

//        videoView1 = findViewById(R.id.co);
//        videoView2 = findViewById(R.id.video2);
//        videoView3 = findViewById(R.id.video3);
//        videoView4 = findViewById(R.id.video4);

        videoViews = new VideoView[] {videoView1, videoView2, videoView3, videoView4};
        // Set up URIs for all VideoViews
        int[] videoResources = {
                R.raw.writing,
                R.raw.speaking,
                R.raw.writing,
                R.raw.counting
        };

        // Set up MediaControllers and URIs for all VideoViews
        for (int i = 0; i < videoViews.length; i++) {
            MediaController mediaController = new MediaController(this);
            mediaController.setMediaPlayer(videoViews[i]);

            // Set the MediaController to be always visible
            mediaController.setAnchorView(videoViews[i]);
            videoViews[i].setMediaController(mediaController);

            // Set the URI for each video
            videoViews[i].setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoResources[i]));
        }

        // Start the first video
        videoView1.start();

        // Add click listener to go back home
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add an OnClickListener for each VideoView to control playback
        for (int i = 0; i < videoViews.length; i++) {
            final int index = i;
            videoViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < videoViews.length; j++) {
                        if (j != index) {
                            videoViews[j].pause();
                        }
                    }
                    VideoView videoView = (VideoView) v;
                    videoView.start();
                }
            });
        }
    }
}
