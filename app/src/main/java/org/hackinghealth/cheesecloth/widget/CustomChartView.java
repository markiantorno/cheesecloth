package org.hackinghealth.cheesecloth.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.hackinghealth.cheesecloth.CheeseClothApplication;
import org.hackinghealth.cheesecloth.R;
import org.hackinghealth.cheesecloth.dao.CategoryAssociation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by miantorno on 6/5/17.
 */

public class CustomChartView extends RelativeLayout {

    final int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    protected View mRootView;
    static protected Typeface mTf;
    protected Realm mRealm;

    @BindView(R.id.pie_chart)
    org.hackinghealth.cheesecloth.widget.PieChart mPieChart;

    public CustomChartView(Context context) {
        super(context);
        init();
    }

    public CustomChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mRootView = inflate(getContext(), R.layout.widget_chart, this);
        ButterKnife.bind(mRootView);
        mTf = Typeface.createFromAsset(getContext().getAssets(), "NotoSans-Regular.ttf");

        mRealm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());
        setData();
    }

    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    private void setData() {

        mPieChart = CustomChartView.styleChart(mRealm, mPieChart, true);
        mPieChart.animateY(1400);

    }

    public static org.hackinghealth.cheesecloth.widget.PieChart styleChart(Realm mRealm, org.hackinghealth.cheesecloth.widget.PieChart piechart
    , boolean isWidget) {
        RealmResults<CategoryAssociation> result = mRealm.where(CategoryAssociation.class).findAll();

        HashMap<String, Float> summation = new HashMap<>();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        float lowestVal = 0;
        for (CategoryAssociation cat : result) {
            if (summation.containsKey(cat.getCategory().getName())) {
                summation.put(cat.getCategory().getName(), summation.get(cat.getCategory().getName()) + cat.getWeight());
            } else {
                summation.put(cat.getCategory().getName(), cat.getWeight());
            }
            if (summation.get(cat.getCategory().getName()) < lowestVal) {
                lowestVal = summation.get(cat.getCategory().getName());
            }
        }

        Iterator it = summation.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            entries.add(new PieEntry((Float) pair.getValue() + Math.abs(lowestVal) + 1,
                    (String) pair.getKey()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
//        MyDataSet<CategoryAssociation> set = new MyDataSet<CategoryAssociation>(result, "weight", "cat_name");

        PieDataSet set = new PieDataSet(entries, "Messages");

        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setLabel("");

        set.setSliceSpace(2);

        PieData data = new PieData(set);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(20);
        // create a data object with the dataset list
        piechart.setData(data);
        piechart.setBackgroundColor(Color.TRANSPARENT);
        piechart.setHoleColor(Color.TRANSPARENT);
        piechart.setHoleRadius(20);

        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setEntryLabelTextSize(20);
        piechart.setEntryLabelTypeface(mTf);

        piechart.setTransparentCircleRadius(20);
        piechart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        piechart.setDescription(description);

        if(isWidget) {
            piechart.setDrawEntryLabels(false);
            piechart.setUsePercentValues(true);

            data.setDrawValues(false);
        } else {
            piechart.setDrawEntryLabels(true);
            piechart.setUsePercentValues(true);
            data.setDrawValues(true);
        }
        return piechart;

    }
}
