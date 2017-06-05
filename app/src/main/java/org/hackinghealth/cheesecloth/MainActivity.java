package org.hackinghealth.cheesecloth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;

import java.util.Date;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSomeDummyData();
        Log.d("HHHHHHH", "DUMMY DATA INSERTED");
    }

    protected void initSomeDummyData() {

//        final Sender mark = Sender.newBuilder()
//                .name("Jackson Pollock")
//                .address("123 Abstract Ave")
//                .build();
//
//        final Message testMessage = Message.newBuilder()
//                .date(new Date())
//                .text("Came up with another painting!")
//                .build();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Sender mark = realm.createObject(Sender.class);
        mark.setName("Jackson Pollock");
        mark.setAddress("miantorno@ehealthinnovation.org");
        Message testMessage = realm.createObject(Message.class);
        testMessage.setSender(mark);
        testMessage.setText("Came up with another painting!");
        realm.commitTransaction();
    }
}
