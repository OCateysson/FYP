package com.fyp.oli.fyp;

import java.util.HashMap;
import java.util.Map;

public class Exercise {
    String title;
    String descr;
    String sets;
    String reps;
    String imageurl;

    public Exercise(){}

    public Exercise(String title, String descr, String sets, String reps, String imageurl){
        this.imageurl = imageurl;
        this.title = title;
        this.descr = descr;
        this.sets = sets;
        this.reps = reps;
        //this.imageUrl = imageUrl;
    }

    public void setDesc(){
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }

    public void setTitle(){
        this.title = title;
    }

    public void setSets(){ this.sets = sets;}

    public String getSets(){ return sets; }

    public String getTitle() {
        return title;
    }

    public String getReps() {
        return reps;
    }

    public void setReps() {
        this.reps = reps;
    }

    public String getImage() {
        return imageurl;
    }

    public void setImage() {
        this.imageurl = imageurl;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", this.title);
        result.put("descr", this.descr);
        result.put("imageurl", this.imageurl);
        result.put("sets", this.sets);
        result.put("results", this.reps);
        return result;
    }


}
