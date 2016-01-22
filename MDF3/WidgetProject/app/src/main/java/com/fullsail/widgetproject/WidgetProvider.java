package com.fullsail.widgetproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by isaiasrosario on 1/20/16.
 * MDF3 1601
 */

public class WidgetProvider extends AppWidgetProvider {


   public static final String WIDGET_BUTTON = "com.fullsail.widgetproject.WIDGET_BUTTON";

   @Override
   public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      super.onUpdate(context, appWidgetManager, appWidgetIds);

      // Remote view widget layout setup
      RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

      // Set widget adapter intent
      Intent serviceIntent = new Intent(context, WidgetService.class);
      widgetView.setRemoteAdapter(R.id.widgetListView, serviceIntent);

      // Empty text fpr widget when there is no data
      widgetView.setEmptyView(R.id.widgetListView, R.id.empty);

      // Pending intent to call detail view for widget list item click
      Intent detailIntent = new Intent(context, DetailView.class);
      PendingIntent itemIntent = PendingIntent.getActivity(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      widgetView.setPendingIntentTemplate(R.id.widgetListView, itemIntent);

      // Pending intent to call add view on widget button click
      Intent addIntent = new Intent(WIDGET_BUTTON);
      PendingIntent buttonIntent = PendingIntent.getBroadcast(context, 0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      widgetView.setOnClickPendingIntent(R.id.button, buttonIntent);

      appWidgetManager.updateAppWidget(appWidgetIds, widgetView);


   }

   @Override
   public void onReceive(Context context, Intent intent) {

      if (intent.getAction().equals(WIDGET_BUTTON)) {

         // Open add view on widget add button click
         Intent addView = new Intent(context, AddView.class);
         addView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         context.startActivity(addView);

      } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

         System.out.println("Data Updated!");
      }

      super.onReceive(context, intent);

   }

}
