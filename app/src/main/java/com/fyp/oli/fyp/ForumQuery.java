package com.fyp.oli.fyp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ForumQuery  extends PostListForum {

    public ForumQuery() {}

    public Query getQueryPosts(DatabaseReference databaseReference) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

        // get the 100 most recent posts
        Query recentPostsQuery = mDatabase
                .limitToFirst(100);

        return recentPostsQuery;
    }
}
