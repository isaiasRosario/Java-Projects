package com.fullsail.widgetproject;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {

   ListView mListView;
   ArrayAdapter<CarObject> mAdapter;
   ArrayList<CarObject> mCars = new ArrayList<>();

   @Override
   public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      super.onCreateView(inflater, container, savedInstanceState);

      // Set view
      View view = inflater.inflate(R.layout.list_fragment, container, false);

      // Check if there is saved data in the inter storage
      dataRead(getActivity());

      // Initiate new mAdapter view settings
      mAdapter = new ArrayAdapter<CarObject>(container.getContext(),
         android.R.layout.simple_list_item_1, mCars) {
         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
            CarObject user = (CarObject) mCars.get(position);
            View view = super.getView(position, convertView, parent);
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setTextSize(20);
            text.setText(user.getMake() + " " + user.getModel());
            return view;
         }
      };

      // Initiate list view layout
      mListView = (ListView) view.findViewById(R.id.listView);
      mListView.setOnItemClickListener(this);

      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      // get intrent extras from addview form
      Intent intent = getActivity().getIntent();
      String make = intent.getStringExtra("make");
      String model = intent.getStringExtra("model");
      String color = intent.getStringExtra("color");
      String doors = intent.getStringExtra("doors");

      if (make != null) {
         // add new car object and update/save arrraylist
         mCars.add(new CarObject(make, model, color, doors));
         dataSave(getActivity(), mCars);

         //Refresh data and update widget
         AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
         ComponentName thisAppWidget = new ComponentName(getActivity().getPackageName(), WidgetProvider.class.getName());
         int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
         appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

         String extra = intent.getStringExtra("key");

         //Check if intent came from app or widget after add
         if (extra != null) {
            System.out.println("===  " + extra);
         } else {
            System.out.println(extra);
            getActivity().finish();
         }

      }

      mAdapter.notifyDataSetChanged();
      mListView.setAdapter(mAdapter);
   }

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      CarObject car = (CarObject) mAdapter.getItem(position);

      Intent intent = new Intent(getActivity(), DetailView.class);
      intent.putExtra(DetailFragment.EXTRA_ITEM, car);
      startActivity(intent);
   }

   // Save data to internal storage method
   public void dataSave(Context context, ArrayList _save) {

      try {
         FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "myfile"));

         ObjectOutputStream oos = new ObjectOutputStream(fos);
         oos.writeObject(_save);
         oos.close();
         fos.close();

         System.out.println("FILE SAVED!");
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }

   // Read saved data from internal storage method
   public void dataRead(Context context) {

      try {
         FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), "myfile"));
         ObjectInputStream ois = new ObjectInputStream(fis);
         mCars = (ArrayList) ois.readObject();
         ois.close();
         fis.close();

         System.out.println("FILE READ!");
      } catch (IOException ioe) {
         ioe.printStackTrace();

      } catch (ClassNotFoundException c) {
         c.printStackTrace();
      }
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

   }
}
