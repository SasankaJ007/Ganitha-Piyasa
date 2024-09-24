package lk.learningApp;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import lk.learningApp.R;
import lk.learningApp.helper.videoUtills.CustomMediaController;

public class FormulaTutorial extends AppCompatActivity {

    private VideoView videoView;
    private FrameLayout videoFrameLayout;
    private GestureDetectorCompat gestureDetector;
    private ImageView backHome;
    private String[] videoList = {"adding", "subtraction", "multiply", "divide"}; // Add your video resource names here
    private int currentVideoIndex = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_tutorial);

        backHome = findViewById(R.id.btnHome);
        videoView = findViewById(R.id.video);
        videoFrameLayout = findViewById(R.id.videoFrameLayout);
        MediaController mediaController = new CustomMediaController(this);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousVideo();
            }
        });
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        playVideo(currentVideoIndex); // Play the first video in the list
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.show();
            }
        },300);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Check if there are more videos in the playlist
                if (currentVideoIndex < videoList.length - 1) {
                    currentVideoIndex++;
                    playVideo(currentVideoIndex);
                } else {
                    // You've reached the end of the playlist, you can handle this as needed.
                }
            }
        });
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void playVideo(int index) {
        String videoResource = videoList[index];
        int videoId = getResources().getIdentifier(videoResource, "raw", getPackageName());
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoId));
        videoView.start();
    }
    private void playNextVideo() {
        if (currentVideoIndex < videoList.length - 1) {
            currentVideoIndex++;
            playVideo(currentVideoIndex);
        }else {
            currentVideoIndex = 0;
        }
    }

    private void playPreviousVideo() {
        if (currentVideoIndex > 0) {
            currentVideoIndex--;
            playVideo(currentVideoIndex);
        }else {
            currentVideoIndex = videoList.length - 1;
        }
    }
}
