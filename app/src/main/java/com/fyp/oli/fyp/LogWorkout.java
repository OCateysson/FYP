package com.fyp.oli.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
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
    private List<Workout> workoutList;
    final Context mContext = this;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    private FirestoreRecyclerAdapter<Workout, WorkoutHolder> mAdapter;
    private FirebaseFirestore mDatabase;
    DocumentReference ref;
    private ListenerRegistration firestoreListener;

    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        fab = findViewById(R.id.fab_log_workout);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.workout_list);
        mManager = new LinearLayoutManager(LogWorkout.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View promptsView  = inflater.inflate(R.layout.prompt_name,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(promptsView);
                final EditText wName = promptsView.findViewById(R.id.promptWorkoutName);

                builder.setCancelable(false)
                        .setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LogWorkout.this, ChooseExercises.class);
                                        intent.putExtra("workoutname", wName.getText().toString());
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Query workoutLogQuery = mDatabase.collection("users").document(uid).collection("workoutlogs");

        Log.e(TAG, "Query = " + workoutLogQuery);
        FirestoreRecyclerOptions<Workout> options = new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutLogQuery, Workout.class)
                .build();

        /*
            Loading the exercises into the exerciseList variable
         */
        mDatabase.collection("exercises").document(uid).collection("workoutlogs")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            workoutList= new ArrayList<>();

                            for(DocumentSnapshot doc : task.getResult()){
                                Workout w = doc.toObject(Workout.class);
                                w.setId(doc.getId());
                                workoutList.add(w);
                            }
                            //do something with list of pojos retrieved

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mAdapter = new FirestoreRecyclerAdapter<Workout, WorkoutHolder>(options) {
            @Override
            public WorkoutHolder onCreateViewHolder(ViewGroup group, int i) {
                LayoutInflater inflater = LayoutInflater.from(group.getContext());
                return new WorkoutHolder(inflater.inflate(R.layout.item_workout_log, group, false));
            }

            @Override
            public void onBindViewHolder(final WorkoutHolder holder, final int position, final Workout model) {
                holder.title.setText(model.getTitle());
                holder.comment.setText(model.getComment());

                
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

