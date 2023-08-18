package com.example.aplicationmedicale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ForgetPass extends AppCompatActivity {
    private TextView goToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        //hooks
        goToSignIn = findViewById(R.id.goToSignIn);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPass.this, SignInActivity.class));
        });

    }
}