package com.example.feedonomic;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;


public class FeedDisplay extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView current_name, current_email;
    String name, email;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mDatabaseReference, api_databaseReference, temp;
    Button button_rss;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        current_name = header.findViewById(R.id.name_in_drawer);
        current_email = header.findViewById(R.id.email_in_drawer);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        temp = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("password");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReference.keepSynced(true);
        api_databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("api");
        api_databaseReference.keepSynced(true);
        recyclerView = findViewById(R.id.list_api_name);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name = dataSnapshot.child(mUser.getUid()).child("name").getValue(String.class);
                email = dataSnapshot.child(mUser.getUid()).child("email").getValue(String.class);
                current_name.setText(name);
                current_email.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_rss = findViewById(R.id.add_RSS);
        button_rss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ApiAdapter> options =
                new FirebaseRecyclerOptions.Builder<ApiAdapter>()
                .setQuery(api_databaseReference, ApiAdapter.class)
                .build();
        FirebaseRecyclerAdapter<ApiAdapter, ApiAdapter_ViewHolder> adapter =
                new FirebaseRecyclerAdapter<ApiAdapter, ApiAdapter_ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ApiAdapter_ViewHolder apiAdapter_viewHolder, int i, @NonNull final ApiAdapter apiAdapter) {
                        apiAdapter_viewHolder.api_title.setText(apiAdapter.getApi_title());
                        apiAdapter_viewHolder.api_title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //pass data i.e in intent String putextra
                                Intent intent = new Intent(FeedDisplay.this, ListRssnews.class);
                                intent.putExtra("title", (apiAdapter.getApi_title()));
                                intent.putExtra("link", apiAdapter.getApi_link());
                                Log.d("Position",String.valueOf(apiAdapter.getApi_link()));
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ApiAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.api_display, parent, false);
                        ApiAdapter_ViewHolder apiAdapter_viewHolder = new ApiAdapter_ViewHolder(view);
                        return apiAdapter_viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void openDialog() {
        AddDetailsDialog addDetailsDialog = new AddDetailsDialog();
        addDetailsDialog.show(getSupportFragmentManager(), "Add Dialog");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                mAuth.signOut();
                finish();
                Intent myIntent = new Intent(FeedDisplay.this, FirstPage.class);
                startActivity(myIntent);
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ApiAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView api_title;

        public ApiAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            api_title = itemView.findViewById(R.id.api_name);
        }
    }
}
