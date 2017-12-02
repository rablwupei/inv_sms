package org.j2megame.invsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
/**
 * 配置广播接收者:
 *  <receiver android:name=".SMSBroadcastReceiver">
 *     <intent-filter android:priority="1000">
 *         <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
 *     </intent-filter>
 *  </receiver>
 *
 *  注意:
 *  <intent-filter android:priority="1000">表示:
 *  设置此广播接收者的级别为最高
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private MessageListener _messageListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        StringBuilder sb = new StringBuilder();
        String lastSender = "";
        for (Object pdu : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String content = smsMessage.getMessageBody();
            if (!lastSender.equals(sender)) {
                if (sb.length() > 0) {
                    _messageListener.OnReceived(sb.toString());
                    sb.delete(0, sb.length());
                }
                lastSender = sender;
                sb.append("[").append(sender).append("] ");
            }
            sb.append(content);
        }
        _messageListener.OnReceived(sb.toString());
    }

    public interface MessageListener {
        void OnReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        _messageListener = messageListener;
    }
}