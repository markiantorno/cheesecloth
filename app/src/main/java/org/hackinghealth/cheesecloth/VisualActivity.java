package org.hackinghealth.cheesecloth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;
import org.w3c.dom.Text;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.dift.ui.SwipeToAction;

public class VisualActivity extends AppCompatActivity {


    /*
        Formatting numbers displayed on the chart
     */
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

    List<Message> messages;

    final String TITLE = "Categorization of Data";
    final int[] COLORS = new int[]{Color.GREEN, Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.CYAN};

//    final int[] temp = new int[]{
//            Color.argb(255, 255, 66, 66),
//            Color.argb(255, 244, 110, 66),
//            Color.argb(255, 244, 155, 66),
//            Color.argb(255, 244, 212, 66),
//            Color.argb(255, 200, 244, 66),
//            Color.argb(255, 122, 244, 66),
//    };

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.visuallistview)
    RecyclerView listview;

    SwipeToAction swipeToAction;


    boolean showPercentages = false;
    List<PieEntry> entries;
    int totalNums;

    List<String> filters;

    String selected;
    YourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);
        ButterKnife.bind(this);

        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                filters = null;
                calculateFromMessages();
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

                selected = ((PieEntry) e).getLabel();
                Log.d("SELECTED", selected);

                final List<Message> filteredMessages = new ArrayList<>();
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getCategory().equals(selected)) {
                        filteredMessages.add(messages.get(i));
                    }
                }

                Collections.sort(filteredMessages, new CustomComparator());

                adapter = new YourAdapter(getBaseContext(), filteredMessages);
                listview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                listview.setAdapter(adapter);
                System.out.println("filteredMessages.size() = " + filteredMessages.size());

            }

            @Override
            public void onNothingSelected() {
                selected = "";
            }
        });

        this.getValuesFromIntent();
        this.calculateFromMessages();


        final List<Message> filteredMessages = new ArrayList<>();

        for (int i = 0; i < this.messages.size(); i++) {
            if (this.messages.get(i).getCategory().equals(selected)) {
                filteredMessages.add(this.messages.get(i));
            }
        }

        Collections.sort(filteredMessages, new CustomComparator());

        this.adapter = new YourAdapter(this, filteredMessages);
        listview.setAdapter(adapter);


        swipeToAction = new SwipeToAction(listview, new SwipeToAction.SwipeListener<Message>() {
            @Override
            public boolean swipeLeft(final Message itemData) {

                return true;
            }

            @Override
            public boolean swipeRight(Message itemData) {
                final int pos = removeMessage(itemData);
                return true;
            }

            @Override
            public void onClick(Message itemData) {
                //  displaySnackbar(itemData.getTitle() + " clicked", null, null);
            }

            @Override
            public void onLongClick(Message itemData) {
                //displaySnackbar(itemData.getTitle() + " long clicked", null, null);
            }
        });


    }

    private int removeMessage(Message book) {
        int pos = messages.indexOf(book);
        messages.remove(book);
        adapter.notifyItemRemoved(pos);
        // adapter.notifyDataSetChanged();
        calculateFromMessages();
        return pos;
    }

    public class CustomComparator implements Comparator<Message> {
        @Override
        public int compare(Message o1, Message o2) {
            return o1.getUrgency() < o2.getUrgency() ? 1 : -1;
        }
    }

    private void getValuesFromIntent() {
        this.messages = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 5, 5, random.nextInt(23), random.nextInt(60), random.nextInt(60));
            Date selDate = cal.getTime();
            Log.d("Date", selDate.toString());

            Message m = Message.newBuilder().text("Message " + i + " goes here").sender(Sender.newBuilder().name("Steven").build()).build();
            m.setDate(selDate);

            this.messages.add(m);
        }

    }

    List<Integer> catVal;
    List<String> catName;


    /*
        Given populated Messages, calculate the values necessary
     */
    private void calculateFromMessages() {

        Calendar cal = Calendar.getInstance();
        // cal.set(2017, 5, 5, Integer.parseInt(title.getText().toString()), 0, 0);
        cal.set(2017, 5, 5, 23, 0, 0);

        Date selDate = cal.getTime();


        int numValues = this.messages.size();
        int toSub = 0;
        if (this.filters != null) {
            for (String str : this.filters) {
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getCategory().equals(str) || this.messages.get(i).getDate().compareTo(selDate) > 0) {
                        toSub++;
                    }
                }
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                if (this.messages.get(i).getDate().compareTo(selDate) > 0) {
                    toSub++;
                }
            }
        }


        numValues -= toSub;

        this.totalNums = numValues;

        this.entries = new ArrayList<>();
        this.catVal = new ArrayList<>();
        this.catName = new ArrayList<>();
        for (int i = 0; i < this.messages.size(); i++) {

            if (this.messages.get(i).getDate().compareTo(selDate) <= 0) {
                if (!this.catName.contains(this.messages.get(i).getCategory())) {
                    this.catName.add(this.messages.get(i).getCategory());
                    this.catVal.add(1);
                } else {
                    int ind = this.catName.indexOf(this.messages.get(i).getCategory());
                    this.catVal.set(ind, this.catVal.get(ind) + 1);
                }
            }

        }

        for (int i = 0; i < this.catName.size(); i++) {
            if (this.filters != null) {
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

        Map<String, Double> urgency = new HashMap<String, Double>();
        for (int i = 0; i < this.messages.size(); i++) {
            if (!urgency.containsKey(this.messages.get(i).getCategory())) {
                urgency.put(this.messages.get(i).getCategory(), this.messages.get(i).getUrgency());
            } else {
                urgency.put(this.messages.get(i).getCategory(), urgency.get(this.messages.get(i).getCategory()) + this.messages.get(i).getUrgency());

            }
        }


        /*
            Sort the entries so color coded - by urgency
         */
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int j = 0; j < this.entries.size() - 1; j++) {
                for (int i = j + 1; i < this.entries.size(); i++) {
                    if (urgency.get(this.entries.get(j).getLabel()) <= urgency.get(this.entries.get(i).getLabel())) {
                        PieEntry temp = this.entries.get(j);
                        this.entries.set(j, this.entries.get(i));
                        this.entries.set(i, temp);
                        sorted = false;
                    }
                }
            }
        }


        PieDataSet set = new PieDataSet(entries, TITLE);
        PieData pd = new PieData(set);
        set.setColors(this.COLORS, 255);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(15);
        set.setDrawValues(!showPercentages);
        set.setValueFormatter(new ValueFormatter());

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setDrawCenterText(!showPercentages);
        //pieChart.setCenterText( "");
        pieChart.setCenterText(numValues + "");
        pieChart.setCenterTextSize(25);
        pieChart.getLegend().setEnabled(false);
        Description dd = new Description();
        dd.setText("");
        pieChart.setHoleRadius(25);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(25);


        pieChart.setDescription(dd);
        this.pieChart.setData(pd);

        this.pieChart.invalidate();

    }

    public void addNewMessage(Message... messagess) {
        for (Message mk : messagess) {
            this.messages.add(mk);
        }
        this.calculateFromMessages();
    }


}
