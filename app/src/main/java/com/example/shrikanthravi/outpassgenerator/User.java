package com.example.shrikanthravi.outpassgenerator;
/**
 * Created by Shrikanth Ravi on 22-02-2017.
 */ public class User {
    public String name;
    public String email;
    public int OutLeft;
    public String Reason;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email,int OutLeft,String Reason) {
        this.name = name;
        this.email = email;
        this.OutLeft = OutLeft;
        this.Reason = Reason;
    }
}
