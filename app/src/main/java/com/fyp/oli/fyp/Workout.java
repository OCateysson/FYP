package com.fyp.oli.fyp;

public class Workout {
    String title, id;
    String descr;
    String sets;
    String reps;
    String comment;

    public Workout(){}

    public Workout(String id, String title, String comment){
        this.id = id;
        this.comment = comment;
        this.title = title;

    }

    public void setId(String id){this.id = this.id;}
    public String getId(){return id;}

    public void setComment(){
        this.comment = comment;
    }

    public String getComment() {
        return comment;
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
}
