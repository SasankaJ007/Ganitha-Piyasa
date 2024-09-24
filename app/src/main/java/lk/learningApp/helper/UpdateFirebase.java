package lk.learningApp.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lk.learningApp.LocalDatabase.Constants;
import lk.learningApp.model.Level;
import lk.learningApp.model.TotalScore;
import lk.learningApp.writing;

public class UpdateFirebase {

    public static void updateTotalScore(Context context, String CollectionPath){
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef = FirebaseFirestore.getInstance().collection(CollectionPath).document(userID);
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                TotalScore WritingGame = documentSnapshot.toObject(TotalScore.class);
                if (WritingGame != null){
                    HashMap<Integer, Level> levelHashMap = LockLevels.getLevelsMap(WritingGame);
                    int totalScore = 0;
                    for (Level l : levelHashMap.values()) {
                        totalScore += l.getRating();
                    }
                    WritingGame.setTotalScore(totalScore);
                    sendGameDataToFirestore(WritingGame, FirebaseFirestore.getInstance(), context, CollectionPath);
                }
            } else {
//                System.out.println("Document Not Found");
            }
        });
    }
    public static void sendGameDataToFirestore(Object user, FirebaseFirestore firestore, Context context, String CollectionPath) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectionRef = firestore.collection(CollectionPath);
        DocumentReference documentRef = collectionRef.document(userID);

        documentRef.set(user)
                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Data Failed to add", Toast.LENGTH_SHORT).show();
                });

    }
    public static void test(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Data to add to the collection
        Map<String, Object> data = new HashMap<>();
        data.put("1", "00:35");
        data.put("2", "01:23");
        data.put("3", "00:49");
        data.put("4", "02:05");
        data.put("5", "00:45");
        data.put("6", "01:47");
        data.put("7", "01:51");
        data.put("8", "00:59");
        data.put("9", "01:20");
        data.put("10", "01:15");
        data.put("11", "01:30");
        data.put("12", "01:52");
        data.put("13", "01:53");
        data.put("14", "01:24");
        data.put("15", "00:55");
        // Add data to Firestore
        db.collection(Constants.KEY_COLLECTION_STUDENT_SPEND_TIME).document(Constants.KEY_COLLECTION_COUNTING_GAME)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                        Log.d("TAG", "Data added to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error adding data
                        Log.w("TAG", "Error adding data to Firestore", e);
                    }
                });
    }
}
