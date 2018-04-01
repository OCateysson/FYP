package com.fyp.oli.fyp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPageActivity extends AppCompatActivity {
    private static final String TAG = EditPageActivity.class.getSimpleName();
    private TextInputEditText nameText, ageText, hRateText, bPressureText;
    String name,age,hRate,bPressure;
    private Button updateButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        nameText = findViewById(R.id.edit_name);
        ageText = findViewById(R.id.age_edit);
        hRateText = findViewById(R.id.hr_edit);
        bPressureText =findViewById(R.id.bp_edit);
        updateButton = findViewById(R.id.editHomePage_button);


        /**
         * Database Stuff
         */
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameText.getText().toString().trim();
                age = ageText.getText().toString().trim();
                hRate = hRateText.getText().toString().trim();
                bPressure = bPressureText.getText().toString().trim();

                updateUser(name,age,hRate,bPressure);
                startActivity(new Intent(EditPageActivity.this, Home.class));
            }
        });
    }

    private void updateUser(String name, String age, String hRate, String bPressure) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        if(!TextUtils.isEmpty(name)){
            mDatabase.child(uid).child("name").setValue(name);
            Log.e(TAG, "User's name is changed! " + name);
        }

        if(!TextUtils.isEmpty(hRate)){
            mDatabase.child(uid).child("heartRate").setValue(hRate);
            Log.e(TAG, "User's name is changed! " + hRate);

        }

        if(!TextUtils.isEmpty(age)){
            mDatabase.child(uid).child("age").setValue(age);
            Log.e(TAG, "User's name is changed! " + age);

        }

        if(!TextUtils.isEmpty(bPressure)){
            mDatabase.child(uid).child("bloodPressure").setValue(bPressure);
            Log.e(TAG, "User's name is changed! " + bPressure);
        }

        Toast.makeText(EditPageActivity.this, "Data has been Successfully Updated!",
                Toast.LENGTH_SHORT).show();
    }
}
