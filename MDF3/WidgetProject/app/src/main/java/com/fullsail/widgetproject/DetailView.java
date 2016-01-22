package com.fullsail.widgetproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class DetailView extends AppCompatActivity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.detail_view);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      //Setting fragments transactions
      android.app.FragmentManager fragmentManager;
      fragmentManager = getFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.detail_frame, new DetailFragment()).commit();

   }
}
