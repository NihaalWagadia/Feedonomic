package com.example.feedonomic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ListRssnews extends AppCompatActivity {

    ArrayList<String> titles;
    ArrayList<String> links;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String string, string_title;
    URL url;
    ArrayList<FeedELement> feedELements;
    RecyclerView recyclerView;
    long startTime = new GregorianCalendar().getTimeInMillis() + 5 * 1000;
    ArrayList<String> published;
    String date = "00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rssnews);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        string = intent.getStringExtra("link");
        string_title = intent.getStringExtra("title");


        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        published = new ArrayList<String>();

        recyclerView = findViewById(R.id.recyclerView);
        new ProcessInBackground().execute();

        Intent alertIntent = new Intent(ListRssnews.this, AutoCheck.class);
        alertIntent.putExtra("link", string);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, alertIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime,
                18000 * 1000, alarmIntent);


    }


    public class ProcessInBackground extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(ListRssnews.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Busy loading news...please wait!");
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... integers) {
            ProcessXml(Getdata());
            return null;
        }

        private void ProcessXml(Document data) {

            if (data != null) {
                feedELements = new ArrayList<>();
                Element root = data.getDocumentElement();
                Node channel = root.getChildNodes().item(0);
                //Will store all child of channel element
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    //store current child
                    Node currentChild = items.item(i);
                    if (currentChild.getNodeName().equalsIgnoreCase("item")) {
                        FeedELement item = new FeedELement();
                        NodeList itemchilds = currentChild.getChildNodes();
                        //for loop will loop through all the item tags
                        for (int j = 0; j < itemchilds.getLength(); j++) {
                            Node current = itemchilds.item(j);
                            if (current.getNodeName().equalsIgnoreCase("title")) {
                                item.setTitle(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("description")) {
                                item.setDescription(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                                item.setPubDate(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("link")) {
                                item.setLink(current.getTextContent());
                            }

                        }
                        feedELements.add(item);

                    }
                }


            }


        }

        public Document Getdata() {
            try {
                url = new URL(string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDoc = builder.parse(inputStream);
                return xmlDoc;
            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            MyAdapter adapter = new MyAdapter(ListRssnews.this, feedELements);
            recyclerView.setLayoutManager(new LinearLayoutManager(ListRssnews.this));
            recyclerView.setAdapter(adapter);
        }


    }
}

