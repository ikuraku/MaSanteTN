package com.example.aplicationmedicale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {

    private EditText fullName,email,phone,cin;
    private Button btnEdit,btnLogOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Hooks
        fullName = findViewById(R.id.fullNameProfile);
        email = findViewById(R.id.emailProfile);
        phone = findViewById(R.id.phoneProfile);
        cin = findViewById(R.id.cinProfile);
        btnEdit = findViewById(R.id.btsEditeProfile);
        btnLogOut = findViewById(R.id.btsLogProfile);
        user = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("User").child(user.getUid());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameS = snapshot.child("fullName").getValue().toString();
                String cinS = snapshot.child("cin").getValue().toString();
                String emailS = snapshot.child("email").getValue().toString();
                String phoneS = snapshot.child("phone").getValue().toString();

                fullName.setText(fullNameS);
                cin.setText(cinS);
                email.setText(emailS);
                phone.setText(phoneS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profileActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

        btnLogOut.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("checkBox",MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("remember","false");
            editor.apply();
            firebaseAuth.signOut();
            startActivity(new Intent(profileActivity.this,SignInActivity.class));
            Toast.makeText(this, "Log out successfully !", Toast.LENGTH_SHORT).show();
            finish();

        });

        btnLogOut.setOnClickListener(v -> {
            fullName.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            cin.setFocusableInTouchMode(true);
            btnEdit.setText("save");
            btnLogOut.setOnClickListener(vv -> {

                String editName = fullName.getText().toString();
                String editPhone = phone.getText().toString();
                String editCin = cin.getText().toString();
                reference.child("fullName").setValue(editName);
                reference.child("phone").setValue(editPhone);
                reference.child("cin").setValue(editCin);
                Toast.makeText(this,"Your data has been changed successfully !",Toast.LENGTH_SHORT).show();
                fullName.setFocusableInTouchMode(false);
                phone.setFocusableInTouchMode(false);
                cin.setFocusableInTouchMode(false);
            });
        });

    }
}