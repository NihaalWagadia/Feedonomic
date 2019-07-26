package com.example.feedonomic;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            setContentView(R.layout.activity_first_page);

        } else {
            Intent intent = new Intent(FirstPage.this, FeedDisplay.class);
            startActivity(intent);
            finish();
        }

    }

    public void directLogin(View v) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void directRegister(View v) {
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }
}
