package com.fullsail.orangemusic;

import java.io.Serializable;

/**
 * Created by isaiasrosario on 1/8/16.
 */

// Create Track object and implements serializable class to save data
public class Track implements Serializable {
   private String mArtist;
   private String mName;
   private String mArtwork;
   private String mUri;

   public Track() {
      mArtist = mName = mUri = mArtwork = "";
   }

   public Track(String _artist, String _name, String _uri, String _artwork) {
      mArtist = _artist;
      mName = _name;
      mArtwork = _artwork;
      mUri = _uri;
   }

   public String getArtist() {
      return mArtist;
   }

   public String getName() {
      return mName;
   }

   public String getArtwork() {
      return mArtwork;
   }

   public String getUri() {
      return mUri;
   }

   @Override
   public String toString() {
      return mArtist;

   }
}