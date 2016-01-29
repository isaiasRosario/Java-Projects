package com.fullsail.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class MapsActivity extends AppCompatActivity implements
   OnMapReadyCallback, LocationSource.OnLocationChangedListener,
   GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

   private GoogleMap mMap;

   LocationManager mLocationManager;
   Location mLocation;

   StorageUtility mStorage = new StorageUtility();
   HashMap<String, MarkerInfo> mMarkerInfo = new HashMap<String, MarkerInfo>();

   ImageView img;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.map_layout);
      setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
         .findFragmentById(R.id.map);
      mapFragment.getMapAsync(this);

      mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

      ArrayList arr = MarkerArray.getInstance().mList;

      mStorage.readData(getApplicationContext(), arr);
      if (arr.isEmpty()) {
         Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
         MarkerArray.getInstance().mList.add(new MarkerInfo("Title Mark 1", "Caption Mark 1", 40, 100, bm));
         MarkerArray.getInstance().mList.add(new MarkerInfo("Title Mark 2", "Caption Mark 2", 40, 105, bm));
      }

      // Add new marker photo to the map floating button
      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            // Intent to open up Add activty screen
            Intent intent = new Intent(MapsActivity.this, AddMarkerActivity.class);
            intent.putExtra("location", mLocation);
            intent.putExtra("click", "button");
            startActivity(intent);
            finish();
         }
      });
   }

   @Override
   public void onMapReady(GoogleMap googleMap) {
      mMap = googleMap;

      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         // TODO: Consider calling
         //    ActivityCompat#requestPermissions
         // here to request the missing permissions, and then overriding
         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
         //                                          int[] grantResults)
         // to handle the case where the user grants the permission. See the documentation
         // for ActivityCompat#requestPermissions for more details.
         return;
      }
      mMap.setMyLocationEnabled(true);
      mMap.setOnMapLongClickListener(this);
      mMap.setOnInfoWindowClickListener(this);
      mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

         // Use default InfoWindow frame
         @Override
         public View getInfoWindow(Marker arg0) {
            return null;
         }

         // Defines the contents of the InfoWindow
         @Override
         public View getInfoContents(Marker marker) {

            // Getting view from the layout file info_window_layout
            View v = getLayoutInflater().inflate(R.layout.window_layout, null);

            // Getting the position from the marker
            LatLng latLng = marker.getPosition();

            // Getting reference to the TextView to set latitude
            TextView title = (TextView) v.findViewById(R.id.textTitle);

            // Getting reference to the TextView to set longitude
            TextView caption = (TextView) v.findViewById(R.id.textCaption);

            // Setting the latitude
            title.setText(mMarkerInfo.get(marker.getId()).getTitle());

            // Setting the longitude
            caption.setText(mMarkerInfo.get(marker.getId()).getCaption());

            img = (ImageView) v.findViewById(R.id.image);

            img.setImageBitmap(mMarkerInfo.get(marker.getId()).getIcon());

            // Returning the view containing InfoWindow contents
            return v;
         }
      });

      mMap.clear();

      for (int i = 0; i < MarkerArray.getInstance().mList.size(); i++) {
         MarkerInfo markerInfo = MarkerArray.getInstance().mList.get(i);
         LatLng latLng = new LatLng(markerInfo.getLat(), markerInfo.getLon());
         Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(markerInfo.getTitle()).snippet(markerInfo.getCaption()));
         // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
         mMarkerInfo.put(marker.getId(), markerInfo);
      }

      ArrayList arr = MarkerArray.getInstance().mList;

      if (!arr.isEmpty()){
         mStorage.saveData(getApplicationContext(), arr);
      }

      Criteria criteria = new Criteria();
      String bestProvider = mLocationManager.getBestProvider(criteria, true);
      mLocation = mLocationManager.getLastKnownLocation(bestProvider);
      if (mLocation != null) {onLocationChanged(mLocation);}

   }

   @Override
   public void onLocationChanged(Location location) {

      LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
      mMap.animateCamera(cameraUpdate);
   }

   @Override
   public void onMapLongClick(LatLng latLng) {

      // Intent to open up Add activty screen
      Intent intent = new Intent(MapsActivity.this, AddMarkerActivity.class);
      intent.putExtra("location", latLng);
      intent.putExtra("click", "map");
      startActivity(intent);
      finish();
   }

   @Override
   public void onInfoWindowClick(Marker marker) {

      img.buildDrawingCache();
      Bitmap bitmap = img.getDrawingCache();

      Intent intent = new Intent(MapsActivity.this, ViewMarkerInfoActivity.class);
      intent.putExtra("title", marker.getTitle());
      intent.putExtra("caption", marker.getSnippet());
      intent.putExtra("location", marker.getPosition());
      intent.putExtra("image", bitmap);
      startActivity(intent);
   }

}