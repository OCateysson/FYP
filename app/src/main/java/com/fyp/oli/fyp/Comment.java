package com.fyp.oli.fyp;

/**
 * Created by Oli on 07/02/2018.
 */

public class Comment {

    public String userID;
    public String author;
    public String text;

    public  Comment(){
        // Default constructor required
    }

    public Comment (String userID, String author, String text){
        this.userID = userID;
        this.author = author;
        this.text = text;
    }
}
