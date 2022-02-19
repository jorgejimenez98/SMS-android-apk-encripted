package com.example.george.secretssms.util;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySMSReceiver extends BroadcastReceiver {
    private final String TAG = MySMSReceiver.class.getSimpleName();
    private final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Ha ocurrido algo... ");
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String str_message = "";
        String format = bundle.getString("format");

        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                if (isVersionM) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                str_message += msgs[i].getOriginatingAddress();
                str_message += ": " + msgs[i].getMessageBody();
                Intent inten = new Intent();
                inten.setAction("android.intent.action.SEND");
                inten.putExtra("sms", str_message);
                context.sendBroadcast(inten);
            }
        }
    }
}

