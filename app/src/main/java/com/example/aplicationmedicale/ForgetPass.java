package com.example.aplicationmedicale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPass extends AppCompatActivity {
    private Button goToSignIn,btnForgetPass;
    private EditText email;
    private FirebaseAuth firebaseAuth;
    private static final String EMAIL_REGEX =
            "^[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*@"
                    + "[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        //hooks
        goToSignIn = findViewById(R.id.goToSignIn);
        email = findViewById(R.id.emailForgetPass);
        btnForgetPass= findViewById(R.id.btnResetPass);
        firebaseAuth = FirebaseAuth.getInstance();


        btnForgetPass.setOnClickListener(v -> {
            if (!isValidEmail(email.getText().toString().trim())){
                email.setError("Please enter a valid email address !");
            }else {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful()){
                            Toast.makeText(ForgetPass.this, "Password reset email sent !", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ForgetPass.this,SignInActivity.class));
                        } else {
                            Toast.makeText(ForgetPass.this,"Error!",Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPass.this, SignInActivity.class));
        });

    }

    public static boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}