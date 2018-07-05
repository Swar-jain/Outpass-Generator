package com.example.shrikanthravi.outpassgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loading extends AppCompatActivity {
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Users").child(user1.getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = (String)dataSnapshot.child("type").getValue();
                switch(type){
                    case "student":
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(Loading.this, Student.class);
                                startActivity(i);
                                Loading.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        }, 3000);

                        break;
                    case "Warden":
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent j = new Intent(Loading.this, Warden.class);
                                startActivity(j);
                                Loading.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        }, 3000);

                        break;
                    case "security":
                        final Handler handler3 = new Handler();
                        handler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent k = new Intent(Loading.this, security.class);
                                startActivity(k);
                                Loading.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        }, 3000);

                        break;
                    default:
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
