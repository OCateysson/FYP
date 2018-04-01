package com.fyp.oli.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private String userID;
    private TextView wText;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private DatabaseReference mDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wText = findViewById(R.id.welcomeText);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();

        /*
         Database Stuff
         */

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        /*
           Getting User's Name from Database
         */
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
                wText.setText("Welcome, " + user.name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //getUserData();


        ImageButton hButton = findViewById(R.id.home_button);
        hButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        ImageButton eButton = findViewById(R.id.exerciseButton);
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
               // startActivity(intent);
            }
        });

        ImageButton fButton = findViewById(R.id.forumButton);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(MainActivity.this, ForumFragment.class);
                //startActivity(intent);
            }
        });

        ImageButton lButton = findViewById(R.id.logoutButton);
        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Logout?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(R.string.logout_message,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                builder1.setNegativeButton(R.string.cancel_message,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    /* private void getUserData() {

         mFirebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);

                 if(user ==null){
                     Log.e(TAG, "No user data.");
                     return;
                 }
                 Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);
                 wText.setText("Welcomne, " + user.name.toString());

             }

             @Override
             public void onCancelled(DatabaseError error) {
                 // Failed to read value
                 Log.e(TAG, "Failed to read user", error.toException());
             }
         });
     }
 */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        //auth.signOut();*/
    }
}