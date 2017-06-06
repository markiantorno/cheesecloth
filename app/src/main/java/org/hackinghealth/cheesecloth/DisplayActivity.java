package org.hackinghealth.cheesecloth;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by skimarro on 6/5/17.
 */

public class DisplayActivity extends AppCompatActivity {


    ListView listview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        listview = (ListView) findViewById(R.id.listview);

        final List <Message> messages = new ArrayList<>();



        for(int i=0; i<25; i++){
            messages.add(Message.newBuilder().text("Message " + i +" goes here").sender(Sender.newBuilder().name("Steven").build()).build());
       }

        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i=0; i<messages.size()-1; i++){
                for(int j=i+1; j<messages.size(); j++){
                    if(messages.get(i).getUrgency() < messages.get(j).getUrgency()){
                        Message temp = messages.get(i) ;
                        messages.set(i, messages.get(j));
                        messages.set(j, temp);
                        sorted = false;
                    }
                }
            }
        }


        final YourAdapter adapter = new YourAdapter(this, messages);
        //listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                messages.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });


    }

}



