package com.fyp.oli.fyp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ExerciseActivity extends TabActivity {

    private static final String TAG = "ExerciseActivity";
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

        /**
         * Firestore
         */
        //listExercises();

        fab = findViewById(R.id.addExercise);

        TabHost mTabHost = findViewById(android.R.id.tabhost);
        TabHost.TabSpec mSpec;
        mTabHost.setup();

        mSpec = mTabHost.newTabSpec("All Exercises");
        Intent intent1 = new Intent(this, ExerciseList.class);
        mSpec.setContent(intent1);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.all_exercises));
        mTabHost.addTab(mSpec);

        mSpec = mTabHost.newTabSpec("Plan");
        Intent intent2 = new Intent(this, WorkoutPlans.class);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.plan));
        mSpec.setContent(intent2);
        mTabHost.addTab(mSpec);

        mSpec = mTabHost.newTabSpec("Log Workout");
        Intent intent3 = new Intent(this, LogWorkout.class);
        mSpec.setIndicator("", getResources().getDrawable(R.drawable.plan));
        mSpec.setContent(intent3);
        mTabHost.addTab(mSpec);

        mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
    }

}
