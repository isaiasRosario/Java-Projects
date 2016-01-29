package com.fullsail.map;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class ViewMarkerInfoActivity extends AppCompatActivity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.view_frame_layout);

      FragmentManager fragManager;
      fragManager = getFragmentManager();
      fragManager.beginTransaction().replace(R.id.viewFrame, new ViewMarkerInfoFragment()).commit();
   }

   @Override
   public void onBackPressed() {
      super.onBackPressed();
      finish();
   }
}
