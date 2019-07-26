package com.example.feedonomic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Intent.getIntent;

public class AutoCheck extends BroadcastReceiver {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String string;
    int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "New Article Alert", "Your feed has been updated", "Alert");

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, FeedDisplay.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());
    }


}
