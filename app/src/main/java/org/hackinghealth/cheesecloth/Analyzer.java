package org.hackinghealth.cheesecloth;

import org.hackinghealth.cheesecloth.widget.VisualActivity;

import java.util.List;

/**
 * Created by skimarro on 6/5/17.
 */

public class Analyzer {

//    static List<VisualActivity.Message> allMessages;
//    static int [] numReceivedPerTime = new int[24];
    static int [] max = new int[2];

    public static void analyze(){
//        for(VisualActivity.Message message : Analyzer.allMessages){
//            numReceivedPerTime[message.g.getHours()] += 1;
//        }

//        Analyzer.max = findMax(Analyzer.numReceivedPerTime);
    }

    private static int [] findMax(int [] arr){
        int max = arr[0];
        int ind = 0;
        for(int i=0; i<arr.length; i++){
            if(arr[i] > max){
                max = arr[i];
                ind = i;
            }
        }
        return new int[]{ind,max};
    }

}
