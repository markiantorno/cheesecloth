package org.hackinghealth.cheesecloth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;

import java.util.Date;

/**
 * Created by mabushawish on 6/5/17.
 */

public class SmsReceiver extends BroadcastReceiver {

    public static final String action = "android.provider.Telephony.SMS_RECEIVED";
    private String TAG = SmsReceiver.class.getSimpleName();

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
                for (SmsMessage sms : messages) {

                    String strMessageFrom = sms.getDisplayOriginatingAddress();
                    String strMessageBody = sms.getDisplayMessageBody();
                    long timestampMillis = sms.getTimestampMillis();

                    String contactName = getContactName(context, strMessageFrom);

                    Sender sender = Sender.newBuilder().name(contactName)
                            .address(strMessageFrom)
                            .build();

                    Message message = Message.newBuilder().sender(sender)
                            .text(strMessageBody)
                            .date(new Date(timestampMillis))
                            .build();
                }
            }
        }
    }

    private String getContactName(Context context, String number) {

        String name = number;

        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.v(TAG, "Started uploadcontactphoto: Contact Found @ " + number);
                Log.v(TAG, "Started uploadcontactphoto: Contact name  = " + name);
            } else {
                Log.v(TAG, "Contact Not Found @ " + number + " returning number as name");

            }
            cursor.close();
        }

        System.out.println("name = " + name);
        return name;
    }
}
