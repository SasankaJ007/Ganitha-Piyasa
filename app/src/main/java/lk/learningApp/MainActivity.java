package lk.learningApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.helper.UpdateFirebase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView blog1, blog2, blog3, blog4;

    MediaPlayer bgMusic;

    RelativeLayout progressNavigate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        blog1 = (CardView) findViewById(R.id.cw01);
        blog2 = (CardView) findViewById(R.id.cw02);
        blog3 = (CardView) findViewById(R.id.cw03);
        blog4 = (CardView) findViewById(R.id.cw04);
        progressNavigate=findViewById(R.id.progressReport);


        bgMusic    =MediaPlayer.create(MainActivity.this,R.raw.bgmusic);
        startMusic();

        blog1.setOnClickListener((View.OnClickListener) this);
        blog2.setOnClickListener((View.OnClickListener) this);
        blog3.setOnClickListener((View.OnClickListener) this);
        blog4.setOnClickListener((View.OnClickListener) this);


        progressNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), progress.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cw01:


//                speakingDialog();

                i = new Intent(this, speakingNavigate.class);
                startActivity(i);
                break;

            case R.id.cw02:

//                writingDialog();

                i = new Intent(this, writingNavigate.class);
                startActivity(i);
                break;

            case R.id.cw03:

//                CountingDialog();
                i = new Intent(this, countingNavigate.class);
                startActivity(i);
                break;

            case R.id.cw04:

//                formulaDialog();
                i = new Intent(this, formulaNavigate.class);
                startActivity(i);
                break;
        }

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

    @Override
    protected void onStart() {
        super.onStart();
        //test function for the adding user Spend-time in firebase.
        UpdateFirebase.test();
        UpdateFirebase.updateTotalScore(MainActivity.this, Constants.KEY_COLLECTION_WRITING_GAME);
        UpdateFirebase.updateTotalScore(MainActivity.this, Constants.KEY_COLLECTION_SPEAKING_GAME);
        UpdateFirebase.updateTotalScore(MainActivity.this, Constants.KEY_COLLECTION_COUNTING_GAME);
        UpdateFirebase.updateTotalScore(MainActivity.this, Constants.KEY_COLLECTION_LOGIC_GAME);
    }

    void startMusic(){
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    private void speakingDialog(){
        ConstraintLayout D1 = findViewById(R.id.SpeakingConstraintLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.speaking_navigation_dialog, D1);
        LinearLayout video = view.findViewById(R.id.videoPlay);
        LinearLayout play = view.findViewById(R.id.gamePlay);
        ImageView cancel =view.findViewById(R.id.cancelmage);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        video.findViewById(R.id.videoPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                video.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                video.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        check.setBackgroundColor(ContextCompat.getColor(counting.this, R.drawable.block03but
                        video.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.dialog01bg));
                    }
                }, 100);

            }
        });

        play.findViewById(R.id.gamePlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                startActivity(new Intent(getApplicationContext(),WritingLevelBoard.class));
            }
        });

        cancel.findViewById(R.id.cancelmage).setOnClickListener(new View.OnClickListener() {
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

    private void writingDialog(){
        ConstraintLayout D1 = findViewById(R.id.SpeakingConstraintLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.writing_navigation_dialog, D1);
        LinearLayout video = view.findViewById(R.id.videoPlay);
        LinearLayout play = view.findViewById(R.id.gamePlay);
        ImageView cancel =view.findViewById(R.id.cancelmage);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        video.findViewById(R.id.videoPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                video.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                video.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        check.setBackgroundColor(ContextCompat.getColor(counting.this, R.drawable.block03but
                        video.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.dialog02bg));
                    }
                }, 100);

            }
        });

        play.findViewById(R.id.gamePlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                startActivity(new Intent(getApplicationContext(),WritingLevelBoard.class));
            }
        });

        cancel.findViewById(R.id.cancelmage).setOnClickListener(new View.OnClickListener() {
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

    private void CountingDialog(){
        ConstraintLayout D1 = findViewById(R.id.SpeakingConstraintLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.counting_navigation_dialog, D1);
        LinearLayout video = view.findViewById(R.id.videoPlay);
        LinearLayout play = view.findViewById(R.id.gamePlay);
        ImageView cancel =view.findViewById(R.id.cancelmage);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        video.findViewById(R.id.videoPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                video.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                video.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        check.setBackgroundColor(ContextCompat.getColor(counting.this, R.drawable.block03but
                        video.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.dialog03bg));
                    }
                }, 100);

            }
        });

        play.findViewById(R.id.gamePlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                startActivity(new Intent(getApplicationContext(),CountingLevelBoard.class));
            }
        });

        cancel.findViewById(R.id.cancelmage).setOnClickListener(new View.OnClickListener() {
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

    private void formulaDialog(){
        ConstraintLayout D1 = findViewById(R.id.SpeakingConstraintLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.formula_navigation_dialog, D1);
        LinearLayout video = view.findViewById(R.id.videoPlay);
        LinearLayout play = view.findViewById(R.id.gamePlay);
        ImageView cancel =view.findViewById(R.id.cancelmage);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        video.findViewById(R.id.videoPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                video.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                video.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        check.setBackgroundColor(ContextCompat.getColor(counting.this, R.drawable.block03but
                        video.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.dialog04bg));
                    }
                }, 100);

            }
        });

        play.findViewById(R.id.gamePlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                startActivity(new Intent(getApplicationContext(),FormulaLevelBoard.class));
            }
        });

        cancel.findViewById(R.id.cancelmage).setOnClickListener(new View.OnClickListener() {
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
}