package org.hackinghealth.cheesecloth.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import org.hackinghealth.cheesecloth.CheeseClothApplication;
import org.hackinghealth.cheesecloth.R;
import org.hackinghealth.cheesecloth.dao.Message;

import io.realm.Realm;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);
//        Realm realm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());
//        Message fetched = realm.where(Message.class).findFirst();
//        if (fetched != null) {
//
//            views.setTextViewText(R.id.info_field, fetched.getSender().getName() + "\n" + fetched.getText());
//        } else {
//            Log.d("TEST TEST", "NULL NULL");
//        }
//        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);

//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }

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

