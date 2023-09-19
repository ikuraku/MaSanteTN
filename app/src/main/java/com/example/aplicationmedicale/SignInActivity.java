package com.example.aplicationmedicale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private TextView goToForget,goToSignUp;
    private EditText email,password;
    private Button btnSignIn;
    private CheckBox remember;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final String EMAIL_REGEX =
            "^[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*@"
                    + "[_A-za-z0-9-\\+]+(\\.[_A-za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //Hooks
        goToSignUp = findViewById(R.id.goToSignUp);
        goToForget = findViewById(R.id.goToForget);
        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        btnSignIn = findViewById(R.id.btsSignIn);
        remember = findViewById(R.id.remember);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog (this);

        SharedPreferences preferences = getSharedPreferences("checkBox",MODE_PRIVATE);
        String checkBox = preferences.getString("remember","");

        if (checkBox.equals("true")){
            startActivity(new Intent(SignInActivity.this, PrincipalActivity.class));
        } else if (checkBox.equals("false")){
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isChecked()){
                SharedPreferences preferences = getSharedPreferences("checkBox",MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putString("remember","true");
                editor.apply();
            }else if (!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
            }
            }
        });




        btnSignIn.setOnClickListener(v -> {
           if (!isValidEmail(email.getText().toString().trim())){
               email.setError("email is invalid !");
               email.requestFocus();
           }else if (password.getText().toString().isEmpty() || password.getText().toString().length()<8){
               password.setError("password is invalid !");
               password.requestFocus();
           }else {
               validate(email.getText().toString(),password.getText().toString());
               startActivity(new Intent(SignInActivity.this, PrincipalActivity.class));
           }
        });

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
        goToForget.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPass.class));
        });

    }

    private void validate(String emailValidation, String passwordValidation) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailValidation,passwordValidation).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailValidation();
                }else {
                    Toast.makeText(SignInActivity.this, "Please verify that your data is correct", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void checkEmailValidation() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        boolean isEmailFlag = firebaseUser.isEmailVerified();
        if (isEmailFlag){
            finish();
            startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
        }else {
            Toast.makeText(this,"Please check your email !",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    public static boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}