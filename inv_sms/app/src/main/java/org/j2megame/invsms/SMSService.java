package org.j2megame.invsms;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by wupei on 2017/11/25.
 */

public class SMSService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        SMSReceiver smsReceiver = new SMSReceiver();
        smsReceiver.setOnReceivedMessageListener(new SMSReceiver.MessageListener() {
            @Override
            public void OnReceived(String message) {
                Weixin.send(new Weixin.Message(message));
            }
        });
        IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, smsFilter);

        IntentFilter tickFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        TickReceiver tickReceiver = new TickReceiver();
        registerReceiver(tickReceiver, tickFilter);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("***服务");
        builder.setContentText("请勿关闭，***");
        startForeground(1, builder.getNotification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
