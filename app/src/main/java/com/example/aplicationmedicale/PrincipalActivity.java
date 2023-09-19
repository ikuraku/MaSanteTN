package com.example.aplicationmedicale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.health.connect.datatypes.Device;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.integrity.v;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView menuIcon;
    private Button btnAddDevice;
    private EditText deviceName,deviceValue;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //hooks
        menuIcon = findViewById(R.id.menuIcon);
        drawerLayout = findViewById(R.id.drawer_principal);
        navigationView = findViewById(R.id.navigation_view_principale);
        btnAddDevice = findViewById(R.id.btnAddDevice);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        deviceName = findViewById(R.id.deviceName);
        deviceValue = findViewById(R.id.deviceValue);
        listDevices = findViewById(R.id.listDevices);



        DatabaseReference deviceReference = firebaseDatabase.getReference().child("Devices");




    }
    private void addDevice(String nameDeviceS, String valueDeviceS) {
        HashMap<String,String> deviceMap = new HashMap<>();
        deviceMap.put("name",nameDeviceS);
        deviceMap.put("value",valueDeviceS);

        reference.child("Devices").push().setValue(deviceMap);

        deviceName.setText("");
        deviceValue.setText("");
        deviceValue.clearFocus();
        deviceName.clearFocus();

        Toast.makeText(this, "New device added successfully !", Toast.LENGTH_SHORT).show();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);

        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else drawerLayout.openDrawer(GravityCompat.START);
    });
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorAppp));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
        }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        super.onBackPressed();
    }
}