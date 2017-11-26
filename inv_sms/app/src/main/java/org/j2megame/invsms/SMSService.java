package org.j2megame.invsms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wupei on 2017/11/25.
 */

public class SMSService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        SMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void OnReceived(String message) {
                Weixin.send(new Weixin.Message(message));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = new Notification.Builder(this.getApplicationContext())
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
