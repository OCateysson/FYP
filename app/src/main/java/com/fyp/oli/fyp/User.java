package com.fyp.oli.fyp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Oli on 30/01/2018.
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String password;
    public String age, heartRate, bloodPressure;

    public User(){}

    public User(String name, String email, String age, String password, String heartRate, String bloodPressure){
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
    }
}
