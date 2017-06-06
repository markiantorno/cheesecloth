package org.hackinghealth.cheesecloth.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.hackinghealth.cheesecloth.CheeseClothApplication;
import org.hackinghealth.cheesecloth.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by miantorno on 6/5/17.
 */

public class CustomChartView extends RelativeLayout {

    protected View mRootView;
    protected Typeface mTf;
    protected Realm mRealm;

    @BindView(R.id.radar_chart)
    RadarChart mRadarChart;

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

    }

    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    private void setData() {

//        RealmResults<RealmDemoData> result = mRealm.where(RealmDemoData.class).findAll();
//
//        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
//        RealmRadarDataSet<RealmDemoData> set = new RealmRadarDataSet<RealmDemoData>(result, "yValue"); // stacked entries
//        set.setLabel("Realm RadarDataSet");
//        set.setDrawFilled(true);
//        set.setColor(ColorTemplate.rgb("#009688"));
//        set.setFillColor(ColorTemplate.rgb("#009688"));
//        set.setFillAlpha(130);
//        set.setLineWidth(2f);
//
//        ArrayList<IRadarDataSet> dataSets = new ArrayList<IRadarDataSet>();
//        dataSets.add(set); // add the dataset
//
//        // create a data object with the dataset list
//        RadarData data = new RadarData(dataSets);
//        styleData(data);
//
//        // set data
//        mChart.setData(null);
//        mChart.animateY(1400);
    }



}
