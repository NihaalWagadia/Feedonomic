package com.example.feedonomic;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotifyService extends IntentService {

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;

    public NotifyService() {
        super("Notify Service");
    }

    public void onCreate() {
        super.onCreate();
        Log.d("timer", "It has started");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("password");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < 15; i++) {
            Log.v("timer", "i= " + i);
            databaseReference.setValue(i);

            try {
                Thread.sleep(2000);

            } catch (Exception e) {

            }
        }
    }
}
