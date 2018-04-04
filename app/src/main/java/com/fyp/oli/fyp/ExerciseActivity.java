package com.fyp.oli.fyp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ExerciseActivity extends BaseActivity {

    private static final String TAG = "ExerciseActivity";
    /**
     * Firestore Database
     */


    /*private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    //private FirebaseRecyclerAdapter<Exercise,ExerciseViewHolder> mAdapter;*/
    private List<Exercise> exerciseList;
    private Context context;
    DatabaseReference databaseReference;

    private FirestoreRecyclerAdapter<Exercise, ExerciseHolder> mAdapter;
    private FirebaseFirestore mDatabase;
    private ListenerRegistration firestoreListener;

    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        mDatabase = FirebaseFirestore.getInstance();
        /**
         * End of Firebase Storage
         */

        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = findViewById(R.id.exercises_list);
        mManager = new LinearLayoutManager(ExerciseActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);*/
        /**
         * Firestore
         */
        mRecycler = findViewById(R.id.exercises_list);
        mManager = new LinearLayoutManager(ExerciseActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);


        //loadExercises();
        final Query exerciseQuery = mDatabase.collection("exercises");

        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(exerciseQuery, Exercise.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Exercise, ExerciseHolder>(options) {
            @Override
            public ExerciseHolder onCreateViewHolder(ViewGroup group, int i) {
                LayoutInflater inflater = LayoutInflater.from(group.getContext());
                return new ExerciseHolder(inflater.inflate(R.layout.item_exercise, group, false));
            }

            @Override
            public void onBindViewHolder(ExerciseHolder holder, int position, Exercise model) {

                String uri = model.getImage();
                Log.e(TAG, "URI = " + uri);

                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getDescr());
                holder.sets.setText("Sets: " + model.getSets());
                holder.reps.setText("Reps: " + model.getReps());
                //holder.setExerciseImage(uri);
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExerciseActivity.this, ExerciseDetail.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        mRecycler.setAdapter(mAdapter);

        fab = findViewById(R.id.addExercise);

        TabHost mTabHost = findViewById(R.id.tabHost);
        mTabHost.setup();

        TabHost.TabSpec mSpec = mTabHost.newTabSpec("All Exercises");
        mSpec.setContent(R.id.tab1);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.all_exercises));
        mTabHost.addTab(mSpec);

        mSpec = mTabHost.newTabSpec("Plan");
        mSpec.setContent(R.id.tab2);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.plan));
        mTabHost.addTab(mSpec);

        mSpec = mTabHost.newTabSpec("Log Workout");
        mSpec.setContent(R.id.tab3);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.logging));
        mTabHost.addTab(mSpec);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ExerciseActivity.this, NewExerciseActivity.class);
                //startActivity(intent);
            }
        });
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
