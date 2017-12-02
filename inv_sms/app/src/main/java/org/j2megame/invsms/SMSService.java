package org.j2megame.invsms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setContentTitle("***服务");
        builder.setContentText("请勿关闭，***");
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.getNotification();
        startForeground(1,notification);//启动前台服务
    }

    SMSBroadcastReceiver _receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (_receiver == null) {
            _receiver = new SMSBroadcastReceiver();
            _receiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                @Override
                public void OnReceived(String message) {
                    Weixin.send(new Weixin.Message(message));
                }
            });
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(_receiver, filter);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);//停止前台服务
        unregisterReceiver(_receiver);
        super.onDestroy();
    }
}
