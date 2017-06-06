package org.hackinghealth.cheesecloth;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import org.hackinghealth.cheesecloth.algo.Classifier;
import org.hackinghealth.cheesecloth.algo.ClassifierKt;
import org.hackinghealth.cheesecloth.dao.CategoryAssociation;
import org.hackinghealth.cheesecloth.dao.CheeseClothDatabaseHelper;
import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;
import org.hackinghealth.cheesecloth.widget.CollectionWidget;

import io.realm.Realm;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;



/**
 * Created by mabushawish on 6/5/17.
 */

public class SmsReceiver extends BroadcastReceiver {

    public static final String action = "android.provider.Telephony.SMS_RECEIVED";
    private String TAG = SmsReceiver.class.getSimpleName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

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
                            .build();

                    saveMessageToDB(message);
                }
            }
        }
    }

    private String getContactName(Context context, String number) {

        String userName = "";
        switch (number) {
            case "1111":
                userName = "Laura Smith";
                break;
            case "2222":
                userName = "HR Dept.";
                break;
            case "3333":
                userName = "Mary York";
                break;
            default:
                userName = "HR Dept.";
                break;

        }

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


        return userName;
    }

    private void saveMessageToDB(final Message message) {
        // SAY YES TO THIS
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(message);
                }
            });

            Classifier classifier = CheeseClothApplication.getClassifier();
            if (classifier != null) {
                CheeseClothApplication.getClassifier().processMessage(message, new Function3<Message, CategoryAssociation[], Double, Unit>() {
                    @Override
                    public Unit invoke(Message message, CategoryAssociation[] categoryAssociations, Double aDouble) {
                        CategoryAssociation highestWeight = null;
                        for (CategoryAssociation ca : categoryAssociations) {
                            if ((highestWeight == null) || (highestWeight.getWeight() < ca.getWeight())) {
                                highestWeight = ca;
                            }
                        }
                        CheeseClothDatabaseHelper.writeCategoryAssociationToDb(highestWeight);
                        return null;
                    }
                });
            } else {
                Log.d("NULL", "CALSSIFIER WASS NULL");
            }

            Intent i = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
            mContext.sendBroadcast(i);

            int widgetIDs[] = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext, CollectionWidget.class));


        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}