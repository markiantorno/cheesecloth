package org.hackinghealth.cheesecloth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by mabushawish on 6/5/17.
 */

public class SmsReceiver extends BroadcastReceiver {


    public static final String action = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("context = " + context);
        // TODO Auto-generated method stub
        if (intent.getAction().equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage message : messages) {

                    String strMessageFrom = message.getDisplayOriginatingAddress();
                    String strMessageBody = message.getDisplayMessageBody();

                    Toast.makeText(context, "From : " + strMessageFrom + "\nBody : " + strMessageBody, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
