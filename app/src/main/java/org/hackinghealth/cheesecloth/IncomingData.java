package org.hackinghealth.cheesecloth;

/**
 * Created by skimarro on 6/5/17.
 *
 *
 *
 *   Data formatted
 */

public class IncomingData {

    String timeStamp; // time format dd/MM/yy/hh/mm/ss
    String data; // data itself
    String category;
    String location;
    String sender;

    public IncomingData(String timeStamp, String data, String category, String location, String sender){
        this.timeStamp = timeStamp;
        this.data = data;
        this.category = category;
        this.location = location;
        this.sender = sender;
    }

    public IncomingData(String timeStamp){
        this(timeStamp, "", "", "", "");
    }




}
