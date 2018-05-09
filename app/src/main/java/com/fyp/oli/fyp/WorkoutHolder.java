package com.fyp.oli.fyp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkoutHolder extends RecyclerView.ViewHolder{
    View mView;

    TextView title, comment;


    WorkoutHolder(View itemView) {
        super(itemView);
        mView = itemView;

        title = itemView.findViewById(R.id.wTitle);
        comment = itemView.findViewById(R.id.wComment);
    }
}
