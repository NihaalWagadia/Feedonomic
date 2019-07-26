package com.example.feedonomic;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ToStoreData extends AppCompatActivity {
    String a, b, c;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_store_data);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReference.keepSynced(true);
        Intent myIntent = getIntent();

        if (myIntent != null) {
            a = myIntent.getStringExtra("name");
            b = myIntent.getStringExtra("email");
            c = myIntent.getStringExtra("password");
        }

        if (c != null) {
            UserLoginData userLoginData = new UserLoginData(a, b, c, mUser.getUid());
            mDatabaseReference.child(mUser.getUid()).setValue(userLoginData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("String1", "You're now a registered user!");

                            } else {
                                Log.d("String2", "FAILEDD!");

                            }

                        }
                    });
        } else {
            UserLoginData userLoginData = new UserLoginData(mUser.getDisplayName(), mUser.getEmail(), "null", mUser.getUid());
            mDatabaseReference.child(mUser.getUid()).setValue(userLoginData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "You're now a registered user!", Toast.LENGTH_SHORT).show();
                                Log.d("String3", "You're now a registered user!");

                            } else {
//                                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                                Log.d("String4", "FAILEDD!");
                            }

                        }
                    });
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(ToStoreData.this, FeedDisplay.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }
}
