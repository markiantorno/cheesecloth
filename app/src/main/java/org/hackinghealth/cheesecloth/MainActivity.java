package org.hackinghealth.cheesecloth;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.hackinghealth.cheesecloth.dao.CategoryAssociation;
import org.hackinghealth.cheesecloth.dao.Message;
import org.hackinghealth.cheesecloth.dao.Sender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    final static int [] COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    protected View mRootView;
    protected static Typeface mTf;
    protected static Realm mRealm;
    protected static PieChart mPieChart;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSomeDummyData();
        Log.d("HHHHHHH", "DUMMY DATA INSERTED");

        EasyPermissions.requestPermissions(this, "This app needs SMS to sort thems",
                1001, Manifest.permission.READ_SMS);

        EasyPermissions.requestPermissions(this, "This app needs SMS to sort thems",
                1001, Manifest.permission.READ_CONTACTS);

        mTf = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        mPieChart = new PieChart(this);
        mRealm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());

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
        set.setLabel("Example");

        set.setSliceSpace(2);

        PieData data = new PieData(set);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(25);
        // create a data object with the dataset list

        mPieChart.setData(data);
//        mPieChart.measure(View.MeasureSpec.makeMeasureSpec(250,View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(250,View.MeasureSpec.EXACTLY));
//        mPieChart.layout(0, 0, mPieChart.getMeasuredWidth(), mPieChart.getMeasuredHeight());
//        Debug.waitForDebugger();
//        Bitmap chartBitmap = mPieChart.getChartBitmap();
//        return chartBitmap;

        FrameLayout frame = (FrameLayout) findViewById(R.id.parent_view);
        frame.addView(mPieChart);
    }

    protected void initSomeDummyData() {

//        final Sender mark = Sender.newBuilder()
//                .name("Jackson Pollock")
//                .address("123 Abstract Ave")
//                .build();
//
//        final Message testMessage = Message.newBuilder()
//                .date(new Date())
//                .text("Came up with another painting!")
//                .build();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Sender mark = realm.createObject(Sender.class);
        mark.setName("Jackson Pollock");
        mark.setAddress("miantorno@ehealthinnovation.org");
        Message testMessage = realm.createObject(Message.class);
        testMessage.setSender(mark);
        testMessage.setText("Came up with another painting!");
        realm.commitTransaction();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
