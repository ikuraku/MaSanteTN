package com.example.aplicationmedicale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText fullName,email,phoneNumber,cin;
    private Button btnEdit,btnLogOut;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        //hooks
        fullName = findViewById(R.id.fullNameProfil);
        email = findViewById(R.id.emailProfil);
        phoneNumber = findViewById(R.id.phoneNumberProfil);
        cin = findViewById(R.id.cinProfil);
        btnEdit = findViewById(R.id.btnEditProfil);
        btnLogOut = findViewById(R.id.btnLogOut);
        menuIcon = findViewById(R.id.menuIconProfil);
        drawerLayout = findViewById(R.id.drawer_profile);
        navigationView = findViewById(R.id.navigation_view_profil);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        user = firebaseAuth.getCurrentUser();



            reference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String fullNameS = snapshot.child("fullName").getValue().toString();
                    String emailS = snapshot.child("email").getValue().toString();
                    String phoneS = snapshot.child("phone").getValue().toString();
                    String cinS = snapshot.child("cin").getValue().toString();

                    fullName.setText(fullNameS);
                    email.setText(emailS);
                    phoneNumber.setText(phoneS);
                    cin.setText(cinS);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Error !", Toast.LENGTH_SHORT).show();

                }
            });

            btnLogOut.setOnClickListener(v -> {
                SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                Toast.makeText(this, "Log out successfully !", Toast.LENGTH_SHORT).show();
                finish();
            });

            btnEdit.setOnClickListener(v -> {

                fullName.setFocusableInTouchMode(true);
                phoneNumber.setFocusableInTouchMode(true);
                cin.setFocusableInTouchMode(true);
                btnEdit.setText("save");

                btnEdit.setOnClickListener(vv -> {
                    String editName = fullName.getText().toString();
                    String editPhone = phoneNumber.getText().toString();
                    String editCin = cin.getText().toString();

                    reference.child("fullName").setValue(editName);
                    reference.child("phone").setValue(editPhone);
                    reference.child("cin").setValue(editCin);
                    Toast.makeText(this, "Your data has been changed successfully!", Toast.LENGTH_SHORT).show();
                    fullName.setFocusableInTouchMode(false);
                    phoneNumber.setFocusableInTouchMode(false);
                    cin.setFocusableInTouchMode(false);
                });


            });


        }


        @Override
        public void onBackPressed () {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else
                super.onBackPressed();
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
