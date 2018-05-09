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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseExercises extends AppCompatActivity {

    private static final String TAG = "ExerciseList";
    private List<Exercise> exerciseList;
    final Context mContext = this;
    DatabaseReference databaseReference;

    private FirestoreRecyclerAdapter<Exercise, ExerciseHolder> mAdapter;
    private FirebaseFirestore mDatabase;
    private ListenerRegistration firestoreListener;
    private FirebaseAuth auth;
    private int addition = 0;
    private String n, c;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;
    private int count = 0, wCount = 0;
    Map<String,String> ex;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exercises);


        fab = findViewById(R.id.addToWorkout);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                n= null;
            } else {
                n= extras.getString("workoutname");
            }
        } else {
            n= (String) savedInstanceState.getSerializable("workoutname");
        }

        mDatabase = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.choose_exercise_list);
        mManager = new LinearLayoutManager(ChooseExercises.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mDatabase.collection("users").document(uid)
                .collection("workoutlogs").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            wCount = task.getResult().size();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //loadExercises();
        final Query exerciseQuery = mDatabase.collection("exercises");

        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(exerciseQuery, Exercise.class)
                .build();

        /*
            Loading the exercises into the exerciseList variable
         */
        firestoreListener = mDatabase.collection("exercises")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        exerciseList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Exercise exercise = doc.toObject(Exercise.class);
                            exercise.setId(doc.getId());
                            //.setId(doc.getId());
                            exerciseList.add(exercise);
                        }
                    }
                });

        mAdapter = new FirestoreRecyclerAdapter<Exercise, ExerciseHolder>(options) {
            @Override
            public ExerciseHolder onCreateViewHolder(ViewGroup group, int i) {
                LayoutInflater inflater = LayoutInflater.from(group.getContext());
                return new ExerciseHolder(inflater.inflate(R.layout.item_exercise, group, false));
            }


            @Override
            public void onBindViewHolder(final ExerciseHolder holder, final int position, final Exercise model) {
                final Exercise exercise = exerciseList.get(position);
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //Log.e(TAG, "URI = " + uri);
                mDatabase.collection("users").document(uid)
                        .collection("workoutlogs")
                        .document(""+n)
                        .collection("exercises").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    count = task.getResult().size();
                                    /*for (DocumentSnapshot document : task.getResult()) {
                                        count++;
                                    }*/
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getDescr());
                holder.sets.setText("Sets: " + model.getSets());
                holder.reps.setText("Reps: " + model.getReps());
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View promptsView  = inflater.inflate(R.layout.prompt_sets_reps,null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setView(promptsView);
                        final EditText wName = promptsView.findViewById(R.id.promptSets);
                        final EditText wReps = promptsView.findViewById(R.id.promptReps);

                        builder.setCancelable(false)
                                .setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ex = new HashMap<>();


                                                Log.e(TAG, "count = " + count +"\nwCount = " + wCount);

                                                ex.put("title", exercise.getTitle());
                                                ex.put("description", exercise.getDescr());
                                                ex.put("imageurl", exercise.getImage());
                                                ex.put("sets", wName.getText().toString());
                                                ex.put("reps", wReps.getText().toString());

                                                mDatabase.collection("users").document(uid)
                                                        .collection("workoutlogs")
                                                        .document(""+n)
                                                        .collection("exercises")
                                                        .document("exercise"+count)
                                                        .set(ex)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.e(TAG, "Exercise Added to Workout");
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent intent = new Intent(ChooseExercises.this, CreateWorkout.class);
                                                        intent.putExtra("workoutname", n);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
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
