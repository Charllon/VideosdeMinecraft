package com.binaryfun.videosdeminecraft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class CheckUpdates extends Service {

    private final static String TAG = "CheckUpdates";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean upd = preferences.getBoolean("pref_notificacoes", true);

        if(upd) {
            Intent mainIntent = new Intent(this, MainActivity.class);

            NotificationManager notificationManager
                    = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification noti = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle("Vídeos de Minecraft")
                    .setContentText("Existem novos vídeos para você assistir!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.icone)
                    .setTicker("Novos vídeos de minecraft!")
                    .setWhen(System.currentTimeMillis())
                    .build();

            notificationManager.notify(1401, noti);

            Log.i(TAG, "Notification created");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
