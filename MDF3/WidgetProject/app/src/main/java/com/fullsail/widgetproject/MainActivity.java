package com.fullsail.widgetproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main_activity);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      // Create list fragment transaction
      FragmentManager fragmentManager;
      fragmentManager = getFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.list_frame, new ListFragment()).commit();

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // Intent to open up Add activty screen
            Intent intent = new Intent(MainActivity.this, AddView.class);
            intent.putExtra("key", "addView");
            startActivity(intent);
            finish();
         }
      });
   }
}
