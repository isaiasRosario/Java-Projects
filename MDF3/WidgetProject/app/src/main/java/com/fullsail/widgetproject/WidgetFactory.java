package com.fullsail.widgetproject;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by isaiasrosario on 1/20/16.
 * MDF3 1601
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {


   private static final int ID_CONSTANT = 0x0101;

   ArrayList<CarObject> mCarList;
   Context mContext;

   public WidgetFactory(Context _context) {
      mContext = _context;
      mCarList = new ArrayList<>();
   }

   @Override
   public void onCreate() {

   }

   @Override
   public void onDataSetChanged() {
      // call read data method
      readData();
   }

   @Override
   public void onDestroy() {
      mCarList.clear();
      ;
      mCarList = null;

   }

   @Override
   public int getCount() {
      return mCarList.size();
   }

   @Override
   public RemoteViews getViewAt(int position) {

      // Set up widget list view items
      CarObject car = mCarList.get(position);
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
      remoteViews.setTextViewText(R.id.makeTextViewWidget, car.getMake());
      remoteViews.setTextViewText(R.id.modelTextViewWidget, "  " + car.getModel());

      Intent intent = new Intent();
      intent.putExtra(DetailFragment.EXTRA_ITEM, car);
      remoteViews.setOnClickFillInIntent(R.id.widget_list_item, intent);

      return remoteViews;
   }

   @Override
   public RemoteViews getLoadingView() {
      return null;
   }

   @Override
   public int getViewTypeCount() {
      return 1;
   }

   @Override
   public long getItemId(int position) {
      return ID_CONSTANT + position;
   }

   @Override
   public boolean hasStableIds() {
      return true;
   }

   // Read saved data and set array list for updating the widget list
   void readData() {
      try {
         FileInputStream fis = new FileInputStream(new File(mContext.getFilesDir(), "myfile"));
         ObjectInputStream ois = new ObjectInputStream(fis);
         mCarList = (ArrayList) ois.readObject();
         ois.close();
         fis.close();

      } catch (IOException ioe) {
         ioe.printStackTrace();

      } catch (ClassNotFoundException c) {
         c.printStackTrace();
      }
   }
}
