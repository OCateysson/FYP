package com.fyp.oli.fyp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ExerciseHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView descriptionView;
    public ImageView exerciseImageView;
    public TextView setsView;
    public TextView repsView;

    private FirebaseFirestore firestoreDB;
    private Context context;
    public List<Exercise> exerciseList;

    private View mView;

    TextView title, desc, sets, reps;
    ImageView exerciseImage;
    String image;

    ExerciseHolder(View view) {
        super(view);
        mView = view;

        //image = view.findViewById(R.id.exercise_image);
        title = view.findViewById(R.id.eTitle);
        sets = view.findViewById(R.id.exercise_sets);
        desc = view.findViewById(R.id.exercise_description);
        reps = view.findViewById(R.id.exercise_reps);

    }

    /*public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }*/

    /*public void setExerciseImage(String url) {
        exerciseImage = mView.findViewById(R.id.exercise_image);
        RequestOptions requestOptions = new RequestOptions();
        //requestOptions.placeholder(R.drawable.image_placeholder);
        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(url).into(exerciseImage);
        //exerciseImage.set
    }*/
}
