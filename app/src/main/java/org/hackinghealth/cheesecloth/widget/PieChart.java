package org.hackinghealth.cheesecloth.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by miantorno on 6/6/17.
 */

public class PieChart extends com.github.mikephil.charting.charts.PieChart {

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Bitmap getChartBitmap() {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
//        Drawable bgDrawable = getBackground();
//        if (bgDrawable != null)
//            // has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        canvas.drawColor(Color.TRANSPARENT);
//
//        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.TRANSPARENT);
        // draw the view on the canvas
        draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }
}
