package com.fyp.oli.fyp;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowWorkout extends AppCompatActivity {

    private static final String TAG = "ExerciseList";
    private List<Exercise> exerciseList;
    String workoutName;
    private FirestoreRecyclerAdapter<Exercise, ExerciseHolder> mAdapter;
    private ListenerRegistration firestoreListener;

    private FirebaseFirestore mDatabase;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_workout);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.exercises_list);
        mManager = new LinearLayoutManager(ShowWorkout.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Bundle bundle = getIntent().getExtras();

        final Query exerciseQuery = mDatabase.collection("users").document(uid).collection("" + workoutName);

        workoutName = bundle.getString("name");

        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(exerciseQuery, Exercise.class)
                .build();

        firestoreListener = mDatabase.collection("users").document(uid).collection("" + workoutName)
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
                String uri = model.getImage();
                Log.e(TAG, "URI = " + uri);

                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getDescr());
                holder.sets.setText("Sets: " + model.getSets());
                holder.reps.setText("Reps: " + model.getReps());
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);
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
