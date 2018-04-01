package com.fyp.oli.fyp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private static final String TAG = Home.class.getSimpleName();
    private TextView name, age, heartRate, bloodPressure;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String userID;
    private FloatingActionButton editPage;
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editPage = findViewById(R.id.fab);

        name = findViewById(R.id.home_username);
        age = findViewById(R.id.home_age);
        heartRate = findViewById(R.id.home_heartRate);
        bloodPressure = findViewById(R.id.home_bp);

        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

                // Display newly updated name and email
                name.setText("Name: " + user.name);
                age.setText("Age: " + user.age);
                heartRate.setText("Heart Rate: " + user.heartRate + " BPM");
                bloodPressure.setText("Blood Pressure: " + user.bloodPressure + " mm HG");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        editPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, EditPageActivity.class));
            }
        });

    }
}
