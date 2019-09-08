package com.example.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i=0;i<appWidgetIds.length;i++) {
            RemoteViews views=new RemoteViews(context.getPackageName()  ,R.layout.ingredient_widget);

            Intent serviceIntent=new Intent(context,IngredientWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.ingredient_list_widget,serviceIntent);
            views.setEmptyView(R.id.ingredient_list_widget,R.id.default_widget_img);

            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.ingredient_list_widget);
        }
    }

}

