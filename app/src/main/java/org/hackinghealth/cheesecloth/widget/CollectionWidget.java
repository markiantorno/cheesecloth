package org.hackinghealth.cheesecloth.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Debug;
import android.view.View;
import android.widget.RemoteViews;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.hackinghealth.cheesecloth.CheeseClothApplication;
import org.hackinghealth.cheesecloth.MyDataSet;
import org.hackinghealth.cheesecloth.R;
import org.hackinghealth.cheesecloth.dao.CategoryAssociation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {

    final static int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    protected View mRootView;
    protected static Typeface mTf;
    protected static Realm mRealm;
    protected static org.hackinghealth.cheesecloth.widget.PieChart mPieChart;
    protected Context mContext;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);
        mTf = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
        mPieChart = new org.hackinghealth.cheesecloth.widget.PieChart(context);

//        Realm realm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());
//        Message fetched = realm.where(Message.class).findFirst();
//        if (fetched != null) {
//
//            views.setTextViewText(R.id.info_field, fetched.getSender().getName() + "\n" + fetched.getText());
//        } else {
//            Log.d("TEST TEST", "NULL NULL");
//        }
//        // Instruct the widget manager to update the widget
        views.setImageViewBitmap(R.id.chart_field, setData());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        init();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    protected void init() {
        mRealm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());
    }

    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    private static Bitmap setData() {

        mPieChart = CustomChartView.styleChart(mRealm, mPieChart);

        mPieChart.measure(View.MeasureSpec.makeMeasureSpec(1000,View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1000,View.MeasureSpec.EXACTLY));
        mPieChart.layout(0, 0, mPieChart.getMeasuredWidth(), mPieChart.getMeasuredHeight());



        Bitmap chartBitmap = mPieChart.getChartBitmap();
        chartBitmap.setHasAlpha(true);

        return chartBitmap;


//        this.pieChart.invalidate();

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


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

