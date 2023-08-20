package com.example.aplicationmedicale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicationmedicale.models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextView goToSignIn;
    private EditText fullName,email,password,phone,cin;
    private Button btnSignUp;
    private FirebaseAuth firebaseAuth;
    private String  fullNameSt,emailSt,passwordSt,phoneSt,cinSt;
    private static final String EMAIL_REGEX =
            "^[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*@"
            + "[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks
        goToSignIn = findViewById(R.id.goToSignIn);
        fullName = findViewById(R.id.fullNameSignUp);
        email = findViewById(R.id.emailSignUp);
        password = findViewById(R.id.passwordSignUp);
        phone = findViewById(R.id.phoneSignUp);
        cin = findViewById(R.id.cinSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        firebaseAuth = FirebaseAuth.getInstance();

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
        btnSignUp.setOnClickListener(v -> {
            if(validate()){
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendEmailVerification();

                        }else {
                            Toast.makeText(SignUpActivity.this,"Register filed !",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(SignUpActivity.this,"Registration is done ! Please check your email address !",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                    }else {
                        Toast.makeText(SignUpActivity.this,"Registration failed !",Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Users");
        UserProfile userProfile = new UserProfile(fullNameSt,emailSt,cinSt,phoneSt);
        myRef.child(""+firebaseAuth.getUid()).setValue(userProfile);



    }

    private boolean validate() {
        boolean result = false;
        fullNameSt = fullName.getText().toString();
        emailSt = email.getText().toString();
        passwordSt = password.getText().toString();
        phoneSt = phone.getText().toString();
        cinSt = cin.getText().toString();
        if (fullNameSt.isEmpty() || fullNameSt.length()<=7){
            fullName.setError("FullName is invalid !");
        }
        else  if (!isValidEmail(emailSt)){
            email.setError("email is invalid !");
        }else  if (phoneSt.isEmpty() || phoneSt.length()!=8){
            phone.setError("phone number is invalid !");
        }else  if (cinSt.isEmpty() || cinSt.length()!=8){
            cin.setError("cin is invalid !");
        }else  if (passwordSt.isEmpty() || passwordSt.length()<8){
            password.setError("Password is invalid !");
        }else {
            result = true;
        }

        return result;
    }
    public static boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    }
