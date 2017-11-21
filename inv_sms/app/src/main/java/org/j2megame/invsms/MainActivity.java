package org.j2megame.invsms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private TextView mTextView;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private void init(){
        mTextView=(TextView) findViewById(R.id.textView);
        mSMSBroadcastReceiver=new SMSBroadcastReceiver();
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void OnReceived(String message) {
                mTextView.setText(message);
            }
        });
    }
}
