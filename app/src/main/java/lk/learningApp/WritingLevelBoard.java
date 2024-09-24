package lk.learningApp;

import static lk.learningApp.helper.LockLevels.lockLevel;
import static lk.learningApp.helper.LockLevels.unlockLevel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.learningApp.helper.LockLevels;
import lk.learningApp.helper.UIUtils.NonSwipeRatingBar;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;

public class WritingLevelBoard extends AppCompatActivity {
    List<NonSwipeRatingBar> ratingBarList = new ArrayList<>(15);
    ImageView mainPage,scoreBoard;
    LinearLayout getStart;
    CardView cw1,cw2,cw3,cw4,cw5,cw6,cw7,cw8,cw9,cw10,cw11,cw12,cw13,cw14,cw15;
    MediaPlayer levelBoardBGMusic;
    private CardView currentlyHighlightedCard = null;
    int selectedLevel = 1;
    String levelName = "level"+selectedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_level_board);
        mainPage = findViewById(R.id.btnHome);
        scoreBoard = findViewById(R.id.btnScore);
        getStart = findViewById(R.id.navigateGame);
        cw1 = findViewById(R.id.cw01);
        cw2 = findViewById(R.id.cw02);
        cw3 = findViewById(R.id.cw03);
        cw4 = findViewById(R.id.cw04);
        cw5 = findViewById(R.id.cw05);
        cw6 = findViewById(R.id.cw06);
        cw7 = findViewById(R.id.cw07);
        cw8 = findViewById(R.id.cw08);
        cw9 = findViewById(R.id.cw09);
        cw10 = findViewById(R.id.cw10);
        cw11 = findViewById(R.id.cw11);
        cw12 = findViewById(R.id.cw12);
        cw13 = findViewById(R.id.cw13);
        cw14 = findViewById(R.id.cw14);
        cw15 = findViewById(R.id.cw15);
        for (int i = 1; i <= 15; i++) {
            String resourceName = String.format("level%02d", i); // Format the resource name as "level01", "level02", etc.
            int resourceId = getResources().getIdentifier(resourceName, "id", getPackageName());
            NonSwipeRatingBar ratingBar = findViewById(resourceId);
            ratingBarList.add(ratingBar);
        }
        lockAllLevels();
        retrieveDataFromDatabase();
        LockLevels.setStars(ratingBarList);
        levelBoardBGMusic = MediaPlayer.create(WritingLevelBoard.this,R.raw.levelboardsound);
        startMusic();
        mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        scoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), ScoreBoard.class);
                startActivity(i);
            }
        });

        View.OnClickListener cardClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentlyHighlightedCard != null) {
                    // Reset the background of the previously highlighted CardView
                    currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(WritingLevelBoard.this, android.R.color.white));
                    currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(WritingLevelBoard.this, R.drawable.levelcorner));
                }

                if (v instanceof CardView) {
                    CardView clickedCard = (CardView) v;
                    levelName = clickedCard.getTag().toString();

                    // Change the background of the clicked CardView
                    clickedCard.setBackground(ContextCompat.getDrawable(WritingLevelBoard.this, R.drawable.levelgame02));

                    // Store the clicked CardView as the currently highlighted CardView
                    currentlyHighlightedCard = clickedCard;
                }
            }
        };

        cw1.setOnClickListener(cardClickListener);
        cw2.setOnClickListener(cardClickListener);
        cw3.setOnClickListener(cardClickListener);
        cw4.setOnClickListener(cardClickListener);
        cw5.setOnClickListener(cardClickListener);
        cw6.setOnClickListener(cardClickListener);
        cw7.setOnClickListener(cardClickListener);
        cw8.setOnClickListener(cardClickListener);
        cw9.setOnClickListener(cardClickListener);
        cw10.setOnClickListener(cardClickListener);
        cw11.setOnClickListener(cardClickListener);
        cw12.setOnClickListener(cardClickListener);
        cw13.setOnClickListener(cardClickListener);
        cw14.setOnClickListener(cardClickListener);
        cw15.setOnClickListener(cardClickListener);
        cw1.performClick();

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LockLevels.getSelectedNumber(levelName) <= (selectedLevel+1)) {
                    System.out.println("selectedLevel : "+levelName);
                    Intent i = new Intent(getApplicationContext(), writing.class);
                    i.putExtra("selectedLevel", levelName);
                    startActivity(i);
                    currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(WritingLevelBoard.this, android.R.color.white));
                    currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(WritingLevelBoard.this, R.drawable.levelcorner));

                } else {
                    Toast.makeText(WritingLevelBoard.this,"Please select Unlocked Level!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (levelBoardBGMusic != null && levelBoardBGMusic.isPlaying()) {
            levelBoardBGMusic.pause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (levelBoardBGMusic != null && !levelBoardBGMusic.isPlaying()) {
            levelBoardBGMusic.start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieveDataFromDatabase();
            }
        },1000);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (levelBoardBGMusic != null) {
            levelBoardBGMusic.release();
            levelBoardBGMusic = null;
        }
    }
    void startMusic(){
        levelBoardBGMusic.setLooping(true);
        levelBoardBGMusic.start();
    }
    private void retrieveDataFromDatabase() {
        String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef = FirebaseFirestore.getInstance().collection("WritingGame").document(userID);
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore student = documentSnapshot.toObject(TotalScore.class);

                if (student != null){
                    HashMap<Integer, Level> levelHashMap = LockLevels.getLevelsMap(student);
                    for (int i = 15; i>=0; i--){
                        if (levelHashMap.containsKey(i)){
                            if (levelHashMap.get(i).isCompleted()){
                                unlockLevels(i);
                                LockLevels.setRatings(i, levelHashMap, ratingBarList);
                                selectedLevel = i;
                                break;
                            }
                        }
                    }
                }else {
                    unlockLevels(0);
                }
            }
        });
    }
    private void unlockLevels(int levelNumber){

        switch (levelNumber) {
            case 15:
                unlockLevel(cw15);
            case 14:
                unlockLevel(cw15);
            case 13:
                unlockLevel(cw14);
            case 12:
                unlockLevel(cw13);
            case 11:
                unlockLevel(cw12);
            case 10:
                unlockLevel(cw11);
            case 9:
                unlockLevel(cw10);
            case 8:
                unlockLevel(cw9);
            case 7:
                unlockLevel(cw8);
            case 6:
                unlockLevel(cw7);
            case 5:
                unlockLevel(cw6);
            case 4:
                unlockLevel(cw5);
            case 3:
                unlockLevel(cw4);
            case 2:
                unlockLevel(cw3);
            case 1:
                unlockLevel(cw2);
            default:
                unlockLevel(cw1);

        }
        switch (levelNumber+1) {
            case 16:
            case 15:
                cw15.performClick();
                break;
            case 14:
                cw14.performClick();
                break;
            case 13:
                cw13.performClick();
                break;
            case 12:
                cw12.performClick();
                break;
            case 11:
                cw11.performClick();
                break;
            case 10:
                cw10.performClick();
                break;
            case 9:
                cw9.performClick();
                break;
            case 8:
                cw8.performClick();
                break;
            case 7:
                cw7.performClick();
                break;
            case 6:
                cw6.performClick();
                break;
            case 5:
                cw5.performClick();
                break;
            case 4:
                cw4.performClick();
                break;
            case 3:
                cw3.performClick();
                break;
            case 2:
                cw2.performClick();
                break;
            case 1:
            default:
                cw1.performClick();
        }
    }
    private void lockAllLevels() {
//        lockLevel(this, cw1);
        lockLevel(this, cw2);
        lockLevel(this, cw3);
        lockLevel(this, cw4);
        lockLevel(this, cw5);
        lockLevel(this, cw6);
        lockLevel(this, cw7);
        lockLevel(this, cw8);
        lockLevel(this, cw9);
        lockLevel(this, cw10);
        lockLevel(this, cw11);
        lockLevel(this, cw12);
        lockLevel(this, cw13);
        lockLevel(this, cw14);
        lockLevel(this, cw15);
    }

}