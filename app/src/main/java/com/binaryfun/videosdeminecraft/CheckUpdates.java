package com.binaryfun.videosdeminecraft;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class CheckUpdates extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public CheckUpdates() {
        super("CheckUpdates");
    }



    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.



        long endTime = System.currentTimeMillis() + 10*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {

                    // ainda botar aqui: se a preferencia de notificacoes for true, procurar novos videos
                    sendNotification("isso veio do service");
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
    }

    // exibir notificacao
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Vídeos de Minecraft")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                                .setContentText(msg)
                        .setSmallIcon(R.drawable.icone);;

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(7015, mBuilder.build());
    }
}