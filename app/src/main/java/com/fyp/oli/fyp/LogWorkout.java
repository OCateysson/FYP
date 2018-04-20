package com.fyp.oli.fyp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LogWorkout extends AppCompatActivity {

    private FloatingActionButton fab;
    private static final String TAG = "ExerciseList";
    private List<Exercise> exerciseList;
    private Context context;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    private FirestoreRecyclerAdapter<Exercise, ExerciseHolder> mAdapter;
    private FirebaseFirestore mDatabase;
    private ListenerRegistration firestoreListener;

    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        fab = findViewById(R.id.fab_log_workout);

        mDatabase = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.workout_list);
        mManager = new LinearLayoutManager(LogWorkout.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogWorkout.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

/*        final Query workoutLogQuery = mDatabase.collection("users").document().collection(auth.getUid())
                .document("workoutlogs").collection("workoutlogs");

        FirestoreRecyclerOptions<WorkoutPlans>*/
    }
}
