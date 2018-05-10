package org.ieselcaminas.daniel.viajes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        NotificationManager notManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notIntent= new Intent(context, MapsActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(context, 9876, notIntent, 0);

        int icon = R.drawable.travel;
        Resources res = context.getResources();
        CharSequence textState =res.getString(R.string.textoNotificacionTitulo)+"";
        CharSequence textContent = res.getString(R.string.textoNotificacion)+"";
        long time = System.currentTimeMillis();

        Notification notification = new NotificationCompat.Builder(context, "chanel1")
                .setSmallIcon(icon)
                .setContentTitle(textState)
                .setContentText(textContent)
                .setWhen(time)
                .setContentIntent(contIntent)
                .build();

        if(pref.getBoolean("option2",true)) {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

        } else {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
        }

        notManager.notify(013, notification);




    }
}
