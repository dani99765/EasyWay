package org.ieselcaminas.daniel.viajes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class NotificationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        int hora = b.getInt("alarm");
       AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
       intent = new Intent(this, NotificationReceiver.class);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), hora, pendingIntent);

       return START_STICKY;
    }
}