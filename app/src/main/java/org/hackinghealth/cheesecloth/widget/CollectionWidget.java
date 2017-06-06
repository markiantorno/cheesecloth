package org.hackinghealth.cheesecloth.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.hackinghealth.cheesecloth.*;
import org.hackinghealth.cheesecloth.dao.CategoryAssociation;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {

    public static String ACTION = "MyACTION";

    final static int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    protected View mRootView;
    protected static Typeface mTf;
    protected static Realm mRealm;
    protected static org.hackinghealth.cheesecloth.widget.PieChart mPieChart;
    protected Context mContext;

    public static final String WIDGET_IDS_KEY = "mywidgetproviderwidgetids";
    public static final String WIDGET_DATA_KEY = "mywidgetproviderwidgetdata";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else super.onReceive(context, intent);
    }

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

        Intent intent = new Intent(context, org.hackinghealth.cheesecloth.VisualActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.chart_field, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d("POOOOOOOOOOOP", "POOOOOOOOOOOOOOOOOOOOOOOOOOOP");

        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        init(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


        System.out.println("appWidgetId = ");
        Intent intent = new Intent(context, VisualActivity.class);
        intent.setAction("MyACTION");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);
        views.setOnClickPendingIntent(R.id.chart_field, pendingIntent);
  //      appWidgetManager.updateAppWidget(appWidgetId, views);



    }


    protected void init(final Context context) {
        mRealm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());

        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<CategoryAssociation> query = mRealm.where(CategoryAssociation.class);

        final RealmResults<CategoryAssociation> contents = query.findAllAsync();

        contents.addChangeListener(new RealmChangeListener<RealmResults<CategoryAssociation>>() {
            @Override
            public void onChange(RealmResults<CategoryAssociation> element) {
//                update(context);
            }
        });
    }

    public static void update(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context, CollectionWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }

    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    private static Bitmap setData() {

        mPieChart = CustomChartView.styleChart(mRealm, mPieChart, true);

        mPieChart.measure(View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY));
        mPieChart.layout(0, 0, mPieChart.getMeasuredWidth(), mPieChart.getMeasuredHeight());

        Bitmap chartBitmap = mPieChart.getChartBitmap();
        chartBitmap.setHasAlpha(true);

        return chartBitmap;
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

