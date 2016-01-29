package com.fullsail.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class ViewMarkerInfoFragment extends android.app.Fragment {

   TextView mTitle, mCaption, mLat, mLon;
   ImageView mImage;

   @Override
   public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

      super.onCreateView(inflater, container, savedInstanceState);

      // View set up
      View v = inflater.inflate(R.layout.view_fragment, container, false);

      // Initiate text views and set up the ids
      mTitle = (TextView)v.findViewById(R.id.viewTitle);
      mCaption = (TextView)v.findViewById(R.id.viewCaption);
      mLat = (TextView)v.findViewById(R.id.viewLat);
      mLon = (TextView)v.findViewById(R.id.viewLon);
      mImage = (ImageView)v.findViewById(R.id.viewImage);

      return v;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      // Getting intent values passed over from the Map Marker Window Info
      Intent intent = getActivity().getIntent();
      String title = intent.getStringExtra("title");
      String caption = intent.getStringExtra("caption");
      Bitmap image = intent.getParcelableExtra("image");
      LatLng location = intent.getParcelableExtra("location");
      String cityState = "No City Found";

      //get address location form the location provided and show it in a textview
      Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
      List<Address> addresses = null;
      try {
         addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
         String address = addresses.get(0).getAddressLine(0);
         String city = addresses.get(0).getAddressLine(1);

         cityState = "\nLocation: " + city + "\n\n";
      } catch (IOException e) {
         e.printStackTrace();
      }

      // Set text view text to values passed from the Map Marker Window Info
      mTitle.setText("\nTitle: " + title);
      mCaption.setText("Caption: " + caption);
      mLat.setText(cityState + "Latitude: " + location.latitude);
      mLon.setText("Longitude: " + location.longitude);
      mImage.setImageBitmap(image);
   }
}
