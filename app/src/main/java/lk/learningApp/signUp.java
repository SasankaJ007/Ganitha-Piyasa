package lk.learningApp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class signUp extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText username,userage,useremail,userpassword;
    TextView loginRedirectText;
    Button sign;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.txtname);
        userage = findViewById(R.id.txtage);
        useremail = findViewById(R.id.txtemail);
        userpassword = findViewById(R.id.txtpassword);
        sign = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = useremail.getText().toString().trim();
                String password = userpassword.getText().toString().trim();
                String name = username.getText().toString();
                String age = userage.getText().toString();


                if (name.isEmpty()) {
                    username.setError("නම ඇතුලත් කරන්න.");
                    return;
                }
                if (age.isEmpty()) {
                    userage.setError("වයස ඇතුලත් කරන්න.");
                    return;
                }

                try {
                    int ageValue = Integer.parseInt(age);
                    // Additional checks for age if needed
                } catch (NumberFormatException e) {
                    userage.setError("ඉලක්කම් පමණක් ඇතුලත් කරන්න.");
                    return;
                }
                if (email.isEmpty()) {
                    useremail.setError("විද්යුත් ලිපිනය ඇතුලත් කරන්න.");
                    return;
                }
                if (!isValidEmail(email)) {
                    useremail.setError("නිවැරදි විද්යුත් ලිපිනය ඇතුලත් කරන්න.");
                    return;
                }
                if (password.isEmpty()) {
                    userpassword.setError("මුරපදය ඇතුලත් කරන්න.");
                    return;
                }
                if (!isValidPassword(password)) {
                    userpassword.setError("මුරපදය අවම වශයෙන් අක්ෂර 6 ක් දිග විය යුතුය.");
                    return;
                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(signUp.this, "ඔබ සාර්ථකව ලියාපදිංචි විය.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("student").document(userID);
                        Map<String, Object> student = new HashMap<>();
                        student.put("Name", name);
                        student.put("Age", age);
                        student.put("Email", email);
                        student.put("UserID", userID);

                        documentReference.set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess:  " + userID);
                            }
                        });

                        startActivity(new Intent(getApplicationContext(), login.class));
                    } else {
//                        Toast.makeText(signUp.this, "Error !  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(signUp.this, "ඔබ ලබාදුන් දත්ත පරීක්ෂාකර නැවත උත්සාහ කරන්න!" , Toast.LENGTH_SHORT).show();

                    }


                });
            }

        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });


    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

}