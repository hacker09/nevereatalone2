package com.example.lambui.nevereatalone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseNotificationService extends Service {
    SharedPreferences sharedPreferences;
    public FirebaseDatabase mDatabase;
    FirebaseAuth firebaseAuth;
    Context context;
    static String TAG = "FirebseServicae";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setupNotificationListener();

    }

    private void setupNotificationListener() {
        mDatabase.getReference().child("notifications")
                .child(firebaseAuth.getCurrentUser().getUid())
                .orderByChild("status").equalTo(0)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot != null){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            showNotification(context, notification, dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }



    private void showNotification(Context context, Notification notification, String key) {
        flagNotificationAsSent(key);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getDescription())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(Html.fromHtml(notification.getMessage()
                ))
                .setAutoCancel(true);
        Intent backIntent = new Intent(context, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent(context, ChatActivity.class);

        /*  Use the notification type to switch activity to stack on the main activity*/
        if(notification.getType().equals("chat_view")){
            intent = new Intent(context, ChatActivity.class);
        }

        final PendingIntent pendingIntent = PendingIntent.getActivities(context, 900,
                new Intent[] {backIntent}, PendingIntent.FLAG_ONE_SHOT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    private void flagNotificationAsSent(String key) {
        mDatabase.getReference().child("notifications")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(key)
                .child("status")
                .setValue(1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(getApplicationContext(), FirebaseNotificationService.class));
    }
}
