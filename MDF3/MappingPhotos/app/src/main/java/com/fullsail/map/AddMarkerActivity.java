package com.fullsail.map;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class AddMarkerActivity extends AppCompatActivity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.add_frame_layout);

      FragmentManager fragManager;
      fragManager = getFragmentManager();
      fragManager.beginTransaction().replace(R.id.addFrame, new AddMarkerFragment()).commit();
   }

   @Override
   public void onBackPressed() {
      super.onBackPressed();
      Intent intent = new Intent(this, MapsActivity.class);
      startActivity(intent);
      finish();
   }
}
