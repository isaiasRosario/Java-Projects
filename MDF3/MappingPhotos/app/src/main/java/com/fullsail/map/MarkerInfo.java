package com.fullsail.map;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class MarkerInfo implements Serializable {

   private double mLat;
   private double mLon;
   private String mTitle;
   private String mCaption;
   private transient Bitmap mIcon;

   public MarkerInfo(){
      mTitle = mCaption = "";
      mLat = mLon = 0;
      mIcon = null;
   }

   public MarkerInfo(String _title, String _caption, double _lat, double _lon, Bitmap _icon){
      mTitle = _title;
      mCaption = _caption;
      mLat = _lat;
      mLon = _lon;
      mIcon = _icon;
   }

   public double getLat(){
      return mLat;
   }

   public double getLon(){
      return mLon;
   }

   public String getTitle(){
      return mTitle;
   }

   public String getCaption(){
      return mCaption;
   }

   public Bitmap getIcon(){ return mIcon; }

   @Override
   public String toString(){
      return mTitle;
   }
}
