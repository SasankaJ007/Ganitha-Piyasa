package lk.learningApp.helper;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreLogicGame {
    String userID;
    FirebaseAuth fAuth;

    public void sendGameDataToFirestore(Object user, FirebaseFirestore firestore, Context context) {
        fAuth       = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectionRef = firestore.collection("LogicGame");
        DocumentReference documentRef = collectionRef.document(userID);

        documentRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Data Added!!");
//                    Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Data Failed to add", Toast.LENGTH_SHORT).show();
                });

    }
}
