package com.fyp.oli.fyp;

import android.media.Image;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExerciseDetail extends AppCompatActivity {

    private static final String TAG = "ExerciseDetail";

    private FirebaseFirestore exerciseRef;
    private String exerciseKey;
    public static final String EXTRA_EXERCISE_KEY = "exercise_key";

    String mTitle, mDescr, mSets, mReps, mURI;
    TextView exTitle, eDesc, eSets, eReps;
    ImageView eImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        Bundle bundle = getIntent().getExtras();

        mURI = bundle.getString("ExercisePic");
        mTitle = bundle.getString("ExerciseTitle");
        mDescr = bundle.getString("ExerciseDescr");
        mSets = bundle.getString("ExerciseSets");
        mReps = bundle.getString("ExerciseReps");

        eImage = findViewById(R.id.fExerciseImage);
        Glide.with(getApplicationContext())
                .load(mURI)
                .apply(new RequestOptions().override(200, 200))
                .into(eImage);

        exerciseRef = FirebaseFirestore.getInstance();
        exerciseRef.collection("exercises").get().equals(exerciseKey);

        Log.e(TAG, "Point = " + exerciseRef );
        exTitle = findViewById(R.id.exerciseTitle);
        eDesc = findViewById(R.id.exerciseDescription);
        eSets = findViewById(R.id.exerciseSets);
        eReps = findViewById(R.id.exerciseReps);

        exTitle.setText(mTitle);
        eDesc.setText("\n" + mDescr);
        eSets.setText("Sets: " +mSets);
        eReps.setText("Reps: " + mReps);
    }

    @Override
    public void onStart(){
        super.onStart();

        ValueEventListener exerciseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);

                exTitle.setText(exercise.title);
                eDesc.setText("\n" + exercise.descr);
                eSets.setText("Sets: " + exercise.sets);
                eReps.setText("Reps: " +exercise.reps);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //exerciseRef.
    }
}
