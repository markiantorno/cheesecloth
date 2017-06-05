package org.hackinghealth.cheesecloth;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisualActivity extends AppCompatActivity {

    List <IncomingData> dataPoints;

    final String TITLE = "Categorization of Data";
    String [] categoryNames = new String[]{"Category #1", "Category #2", "Category #3", "Category #4"};
    final int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    int [] categoryValues = new int[]{2,10,6, 3};
    int sumValues;

    @BindView(R.id.textView2)
    TextView title;

    @BindView(R.id.timebar)
    SeekBar timebar;

    @BindView(R.id.pieChart)
    PieChart pieChart;



    List <PieEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);
        ButterKnife.bind(this);


        this.getValuesFromIntent();
        this.title.setText("PieChart");
        this.calculateValues();
    }

    private void getValuesFromIntent(){
        // get values here
        this.dataPoints = new ArrayList<>();
        this.dataPoints.add(new IncomingData("05/06/17/33/22/2"));
        this.dataPoints.add(new IncomingData("05/06/17/37/31/6"));
        this.dataPoints.add(new IncomingData("05/06/17/15/55/10"));
        this.dataPoints.add(new IncomingData("05/06/17/27/27/15"));
        this.dataPoints.add(new IncomingData("05/06/17/45/16/22"));



    }

    @OnClick(R.id.textView2)
    public void onTouchView(){
        this.addToCategory(1,1);
    }

    private void addToCategory(int cat, int num){
        this.categoryValues[cat] += num;
        this.calculateValues();
    }

    private void calculateValues(){
        for(int nums : this.categoryValues){
            this.sumValues += nums;
        }

        this.entries = new ArrayList<>();
        for(int i=0; i<this.categoryNames.length; i++){
            if(this.categoryValues[i] != 0) {
                float val = (float) 1.0 * this.categoryValues[i] / this.sumValues;
                entries.add(new PieEntry(val, this.categoryNames[i]));
            }
        }

        PieDataSet set = new PieDataSet(entries, TITLE);
        PieData pd = new PieData(set);
        set.setColors(this.COLORS, 90);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(25);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15);
        this.pieChart.setData(pd);
        this.pieChart.invalidate();

    }

}
