package com.fullsail.map;

import java.util.ArrayList;

/**
 * Created by isaiasrosario on 1/28/16.
 * MDF3 1601 Project 4
 */
public final class MarkerArray {

   private static final MarkerArray instance = new MarkerArray();

   public ArrayList<MarkerInfo> mList = new ArrayList<MarkerInfo>();

   public static MarkerArray getInstance() {
      return instance;
   }

   private MarkerArray() {
   }
}
