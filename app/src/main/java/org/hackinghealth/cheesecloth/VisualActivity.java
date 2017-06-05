package org.hackinghealth.cheesecloth;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisualActivity extends AppCompatActivity {

    private class ValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value * totalNums); // e.g. append a dollar-sign
        }
    }


    public class Message {
        String sender, text, category;
        double urgency;
        Date date;

        public Message(Date date, String category){
            this.date = date;
            this.category = category;
        }
    }

    List <Message> messages;

    final String TITLE = "Categorization of Data";
    final int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    @BindView(R.id.textView2)
    TextView title;

    @BindView(R.id.timebar)
    SeekBar timebar;

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.toggle)
    ToggleButton toggle;


    boolean showPercentages = false;
    List <PieEntry> entries;
    int totalNums;

    List <String> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);
        ButterKnife.bind(this);

        title.setTextSize(30);
        title.setText("23");

        timebar.setProgress(23);
        timebar.setMax(23);
        timebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                title.setText(i+"");
                calculateFromMessages();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {


            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                filters = null;
                calculateFromMessages();
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                 PieEntry pe = (PieEntry) e;
                Log.d("TAG", ((PieEntry) e).getLabel());
                String filter = ((PieEntry) e).getLabel();
                if(filters != null){
                    if(!filters.contains(filter)){
                        filters.add(filter);
                    }
                } else {
                    filters = new ArrayList<String>();
                    filters.add(filter);
                }

                calculateFromMessages();
                pieChart.setSelected(false);

            }

            @Override
            public void onNothingSelected() {
            }
        });

        toggle.setBackgroundColor(Color.BLACK);
        toggle.setTextColor(Color.WHITE);
        toggle.setText("Value ON");
        toggle.setTextOff("Value OFF");
        toggle.setTextOn("Value ON");

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Is the toggle on?
                boolean on = ((ToggleButton) view).isChecked();
                if (on) {
                    showPercentages = true;
                } else {
                    // Disable vibrate
                    showPercentages = false;
                }

                calculateFromMessages();
            }
        });



        this.getValuesFromIntent();
        this.calculateFromMessages();
    }

    private void getValuesFromIntent(){
        this.messages = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i<30; i++){
            Calendar cal = Calendar.getInstance();
            cal.set(2017,5,5, random.nextInt(23), random.nextInt(60), random.nextInt(60));
            Date selDate = cal.getTime();
            Log.d("Date", selDate.toString());

            Message m = new Message(selDate, "Category " + random.nextInt(5));
            this.messages.add(m);
        }

    }


    List <Integer> catVal;
    List <String> catName;
    private void calculateFromMessages(){

        Calendar cal = Calendar.getInstance();
        cal.set(2017,5,5,Integer.parseInt(title.getText().toString()),0,0);
        Date selDate = cal.getTime();
        Log.d("DATE CALC is", selDate.toString());


        int numValues = this.messages.size();
        this.totalNums = numValues;

        this.entries = new ArrayList<>();
        this.catVal = new ArrayList<>();
        this.catName = new ArrayList<>();

        for(int i=0; i<this.messages.size(); i++){


                if (this.messages.get(i).date.compareTo(selDate) <= 0) {
                    if (!this.catName.contains(this.messages.get(i).category)) {
                        this.catName.add(this.messages.get(i).category);
                        this.catVal.add(1);
                    } else {
                        int ind = this.catName.indexOf(this.messages.get(i).category);
                        this.catVal.set(ind, this.catVal.get(ind) + 1);
                    }
                }

        }

        for(int i=0; i<this.catName.size(); i++){
            if(this.filters != null) {
                if (this.catVal.get(i) != 0 && !filters.contains(this.catName.get(i))) {
                    float val = (float) 1.0 * this.catVal.get(i) / numValues;
                    entries.add(new PieEntry(val, this.catName.get(i)));
                }
            } else {
                if (this.catVal.get(i) != 0) {
                    float val = (float) 1.0 * this.catVal.get(i) / numValues;
                    entries.add(new PieEntry(val, this.catName.get(i)));
                }
            }
        }

        PieDataSet set = new PieDataSet(entries, TITLE);
        PieData pd = new PieData(set);
        set.setColors(this.COLORS, 90);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(25);
        set.setDrawValues(!showPercentages);
        set.setValueFormatter(new ValueFormatter());

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15);
        this.pieChart.setData(pd);

        this.pieChart.invalidate();

    }

    public void addNewMessage(Message ... messagess){
        for(Message mk : messagess){
            this.messages.add(mk);
        }
        this.calculateFromMessages();
    }



}
