package com.example.aplicationmedicale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    private TextView goToForget,goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //Hooks
        goToSignUp = findViewById(R.id.goToSignUp);
        goToForget = findViewById(R.id.goToForget);

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
        goToForget.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPass.class));
        });







    }
}