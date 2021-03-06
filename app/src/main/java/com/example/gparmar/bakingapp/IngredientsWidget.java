package com.example.gparmar.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.IngredientTable;
import com.example.gparmar.bakingapp.service.WidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gparmar on 19/06/17.
 */
public class IngredientsWidget extends AppWidgetProvider {
    private static final String TAG = "IngredientsWidget";
    public static final String WIDGET_IDS_KEY = "mywidgetproviderwidgetids";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, boolean dataChanged) {


        Log.d(TAG, "Came into updateAppWidget ");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent svcIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.ingredients_list, svcIntent);
        //setting an empty view in case of no data
        views.setEmptyView(R.id.ingredients_list, R.id.empty_view);
        if (dataChanged) {
            Log.d(TAG, "Came into updateAppWidget with dataChanged");
            //If data is changed then to update the list we need to call this:
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_list);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, false);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Came into onReceive");
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            for (int i = 0; i < ids.length; i++) {
                updateAppWidget(context, AppWidgetManager.getInstance(context), ids[i], true);
            }
        } else {
            super.onReceive(context, intent);
        }
    }
}

