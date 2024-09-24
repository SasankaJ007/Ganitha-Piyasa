package lk.learningApp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class login extends AppCompatActivity {

    EditText useremail,userpassword;
    TextView signupRedirect;
    Button login;
    FirebaseAuth fAuth;
    SharedPreferences sharedPreferences;

    private static final String prefName = "mypref";
    private static final String key_email = "email";
    private static final String key_password = "password";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        useremail     =findViewById(R.id.txtlogin_email);
        userpassword  =findViewById(R.id.txtlogin_password);
        signupRedirect=findViewById(R.id.signupRedirectText);
        login         =findViewById(R.id.login_button);

        fAuth         =FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(prefName,MODE_PRIVATE);

        String email = sharedPreferences.getString(key_email,null);

        if(email != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            },100);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=useremail.getText().toString().trim();
                String password=userpassword.getText().toString().trim();

                if (email.isEmpty() || !isValidEmail(email)) {
                    useremail.setError("නිවැරදි විද්යුත් ලිපිනය ඇතුලත් කරන්න.");
                    return;
                }

                if (password.isEmpty()) {
                    userpassword.setError("මුරපදය ඇතුලත් කරන්න.");
                    return;
                }


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key_email,useremail.getText().toString());
                editor.putString(key_password,userpassword.getText().toString());
                editor.apply();
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
//                            Toast.makeText(login.this,"Authentication Failed:  "+task.getException(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(login.this, "නිවැරදි දත්ත ලබාදී නැවත උත්සාහ කරන්න!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


       signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),signUp.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}