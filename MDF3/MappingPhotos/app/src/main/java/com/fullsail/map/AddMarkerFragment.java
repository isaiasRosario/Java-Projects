package com.fullsail.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class AddMarkerFragment extends android.app.Fragment implements View.OnClickListener{
   EditText mTitle, mCaption, mLat, mLon;
   ImageView mImage;
   String mCurrentPhotoPath;
   double lat, lon;

   static final int REQUEST_TAKE_PHOTO = 1;
   static final int PASS_MARKER_INFO = 2;

   Bitmap mBitmap;

   @Override
   public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

      super.onCreateView(inflater, container, savedInstanceState);

      // View set up
      View v = inflater.inflate(R.layout.add_fragment, container, false);

      Button button = (Button)v.findViewById(R.id.addButton);
      button.setOnClickListener(this);

      mImage = (ImageView)v.findViewById(R.id.addImage);

      mTitle = (EditText)v.findViewById(R.id.addTitle);
      mCaption = (EditText)v.findViewById(R.id.addCaption);
      mLat = (EditText)v.findViewById(R.id.addLat);
      mLon = (EditText)v.findViewById(R.id.addLon);

      dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);

      return v;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      Intent intent = getActivity().getIntent();
      String click = intent.getStringExtra("click");

      if (click.equals("button")){
         Location location = intent.getParcelableExtra("location");
         mLat.setText(String.valueOf(location.getLatitude()));
         lat = location.getLatitude();
         mLon.setText(String.valueOf(location.getLongitude()));
         lon = location.getLongitude();
      }

      if (click.equals("map")){
         LatLng location = intent.getParcelableExtra("location");
         mLat.setText(String.valueOf(location.latitude));
         lat = location.latitude;
         mLon.setText(String.valueOf(location.longitude));
         lon = location.longitude;
      }
   }

   @Override
   public void onClick(View v) {

      MarkerArray.getInstance().mList.add(new MarkerInfo(mTitle.getText().toString(), mCaption.getText().toString(), lat, lon, mBitmap));

      Intent intent = new Intent(getActivity(), MapsActivity.class);
      startActivity(intent);
      getActivity().finish();

   }

   private File getAlbumDir() {
      File folderDir = null;

      if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

         File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
         // Creating our own folder in the default directory.
         folderDir = new File(imageDir, "Mappping Photos");

         if (folderDir != null) {
            if (! folderDir.mkdirs()) {
               if (! folderDir.exists()){
                  Log.d("CameraSample", "failed to create directory");
                  return null;
               }
            }
         }

      } else {
         Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
      }

      return folderDir;
   }

   private File createImageFile() throws IOException {
      // Create an image file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = "IMG_" + timeStamp + "_";
      File albumF = getAlbumDir();
      File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
      return imageF;
   }

   private File setUpPhotoFile() throws IOException {

      File f = createImageFile();
      mCurrentPhotoPath = f.getAbsolutePath();

      return f;
   }

   private void dispatchTakePictureIntent(int actionCode) {

      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      switch(actionCode) {
         case REQUEST_TAKE_PHOTO:
            File f = null;

            try {
               f = setUpPhotoFile();
               mCurrentPhotoPath = f.getAbsolutePath();
               takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
               e.printStackTrace();
               f = null;
               mCurrentPhotoPath = null;
            }
            break;

         default:
            break;
      } // switch

      startActivityForResult(takePictureIntent, actionCode);
   }

   private void galleryAddPic() {
      Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
      File f = new File(mCurrentPhotoPath);
      Uri contentUri = Uri.fromFile(f);
      mediaScanIntent.setData(contentUri);
      getActivity().sendBroadcast(mediaScanIntent);
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {

      switch(requestCode) {
         case REQUEST_TAKE_PHOTO:
            if(resultCode == Activity.RESULT_OK){

               galleryAddPic();

               //Convert file path into bitmap image using below line.
               Bitmap yourSelectedImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
               mBitmap = yourSelectedImage;

               //put bitmapimage in your imageview
               mImage.setImageBitmap(yourSelectedImage);

               mCurrentPhotoPath = null;
            }
            break;
      }
   }
}
