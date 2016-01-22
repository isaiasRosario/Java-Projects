package com.fullsail.widgetproject;

import java.io.Serializable;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class CarObject implements Serializable {
   private String mMake;
   private String mModel;
   private String mColor;
   private String mDoors;

   public CarObject() {
      mMake = mModel = mColor = mDoors = "";
   }

   public CarObject(String _make, String _model, String _color, String _doors) {
      mMake = _make;
      mModel = _model;
      mColor = _color;
      mDoors = _doors;
   }

   public String getMake() {
      return mMake;
   }

   public String getModel() {
      return mModel;
   }

   public String getColor() {
      return mColor;
   }

   public String getDoors() {
      return mDoors;
   }

   @Override
   public String toString() {
      return mMake;

   }
}
