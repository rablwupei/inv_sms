package org.j2megame.invsms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wupei on 2017/11/25.
 */

public class SMSService extends Service {

    private SMSBroadcastReceiver _smsBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        SMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void OnReceived(String message) {
                Weixin.send(new Weixin.Message(message));
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
