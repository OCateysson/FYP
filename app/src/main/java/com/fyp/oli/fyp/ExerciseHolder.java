package com.fyp.oli.fyp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ExerciseHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView title, desc, sets, reps;
    ImageView imageView;
    String image;

    ExerciseHolder(View itemView) {
        super(itemView);
        mView = itemView;

        imageView = itemView.findViewById(R.id.exercise_image);
        title = itemView.findViewById(R.id.eTitle);
        sets = itemView.findViewById(R.id.exercise_sets);
        desc = itemView.findViewById(R.id.exercise_description);
        reps = itemView.findViewById(R.id.exercise_reps);
    }
}
