package com.fyp.oli.fyp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oli on 07/02/2018.
 */
@IgnoreExtraProperties
public class Post {
    public String userID, author, title, body;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post(){
        //Default constructor required
    }

    public Post(String userID, String author, String title, String body){
        this.userID = userID;
        this.author = author;
        this.title = title;
        this.body = body;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userID);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
