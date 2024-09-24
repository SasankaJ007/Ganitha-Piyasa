package lk.learningApp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.model.TotalScore;

public class ScoreBoard extends AppCompatActivity {

    ImageView homePage;
    TextView writing, speaking, formula, counting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        homePage = findViewById(R.id.btnHome);
        speaking = findViewById(R.id.txtSpeaking);
        writing = findViewById(R.id.txtWriting);
        counting = findViewById(R.id.txtCounting);
        formula = findViewById(R.id.txtLogic);


        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDataFromDatabase();
    }

    private void retrieveDataFromDatabase() {
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef1 = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_WRITING_GAME).document(userID);
        documentRef1.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore studentGame = documentSnapshot.toObject(TotalScore.class);
                if (studentGame != null){
                    writing.setText(String.format("%02d", studentGame.getTotalScore()));
                }
            }
        });
        DocumentReference documentRef2 = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_SPEAKING_GAME).document(userID);
        documentRef2.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore studentGame = documentSnapshot.toObject(TotalScore.class);
                if (studentGame != null){
                    speaking.setText(String.format("%02d", studentGame.getTotalScore()));
                }
            }
        });
        DocumentReference documentRef3 = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_COUNTING_GAME).document(userID);
        documentRef3.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore studentGame = documentSnapshot.toObject(TotalScore.class);
                if (studentGame != null){
                    counting.setText(String.format("%02d", studentGame.getTotalScore()));
                }
            }
        });
        DocumentReference documentRef4 = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_LOGIC_GAME).document(userID);
        documentRef4.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore studentGame = documentSnapshot.toObject(TotalScore.class);
                if (studentGame != null){
                    formula.setText(String.format("%02d", studentGame.getTotalScore()));
                }
            }
        });
    }
}