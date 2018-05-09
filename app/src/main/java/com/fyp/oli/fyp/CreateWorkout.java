package com.fyp.oli.fyp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.nfc.Tag;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateWorkout extends LogWorkout {

    private static final String TAG = "ExerciseList";
    private List<Exercise> exerciseList;
    private FirestoreRecyclerAdapter<Exercise, ExerciseHolder> mAdapter;
    private FirebaseFirestore mDatabase;
    private Query workoutLogQuery;
    private ListenerRegistration firestoreListener;
    private String n;
    private  String uid;

    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;
    private FloatingActionButton fab;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE
                                | ActionBar.DISPLAY_SHOW_CUSTOM);

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

        fab = findViewById(R.id.fab_add_exercise_to_workout);
        mDatabase = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.workout_exercise_list);
        mManager = new LinearLayoutManager(CreateWorkout.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateWorkout.this, ChooseExercises.class);
                intent.putExtra("workoutname", n);
                startActivity(intent);
            }
        });


        workoutLogQuery = mDatabase.collection("users").document(uid).collection("workoutlogs")
                .document(""+n).collection("exercises");

        Log.e(TAG, "Query = " + workoutLogQuery);
        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(workoutLogQuery, Exercise.class)
                .build();

        /*
            Loading the exercises into the exerciseList variable
         */
        firestoreListener = mDatabase.collection("exercises").document(uid).collection("workoutlogs")
                .document(""+n).collection("exercises")
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
                String uri = model.getImage();
                Log.e(TAG, "URI = " + uri);

                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getDescr());
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);
                holder.sets.setText("Sets: " + model.getSets());
                holder.reps.setText("Reps: " + model.getReps());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.finishWorkout) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View promptsView  = inflater.inflate(R.layout.prompt_comment,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(promptsView);

            final EditText comment = promptsView.findViewById(R.id.promptComment);
            //String c = comment.getText().toString();


            builder.setCancelable(false)
                    .setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,String> wn = new HashMap<>();
                                    wn.put("comment", comment.getText().toString());
                                    wn.put("title",n);

                                    mDatabase.collection("users").document(uid)
                                            .collection("workoutlogs")
                                            .document(""+n).set(wn);
                                    Toast.makeText(CreateWorkout.this, comment.getText().toString(),Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(CreateWorkout.this, ExerciseActivity.class);
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
        if (id == R.id.cancelWorkout) {
            Intent intent = new Intent(this, ExerciseActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
