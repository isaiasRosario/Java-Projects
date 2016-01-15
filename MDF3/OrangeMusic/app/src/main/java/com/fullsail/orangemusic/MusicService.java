package com.fullsail.orangemusic;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

/**
 * Created by isaiasrosario on 1/7/16.
 */

public class MusicService extends Service {

   // Variables
   static final int REQUEST_CODE = 0;
   static final int NOTIFICATION_ID = 1;
   static int seek = 0;
   static ArrayList<Track> songs = new ArrayList<>();
   static ObjectAnimator anim;

   // On create method when service is started
   @Override
   public void onCreate() {
      super.onCreate();

      //Add track objects to create an array of tracks
      if (songs.isEmpty()) {
         songs.add(new Track("Jason Shaw", "Glitch", "glitch", "glitch"));
         songs.add(new Track("Deadelus", "Make it Drums", "drums", "drums"));
         songs.add(new Track("Podington Bear", "Pizzi", "pizzi", "pizzi"));
         songs.add(new Track("Jason Staczek", "Butterballs", "butter", "butter"));
         songs.add(new Track("PC Noise", "Picture this pedicured", "noise", "noise"));
      }
   }

   // My local binder service method
   public class MyLocalBinder extends Binder {
      public MusicService getService() {
         return MusicService.this;
      }
   }

   // Set up binder intent method
   @Override
   public IBinder onBind(Intent intent) {
      return new MyLocalBinder();
   }

   // Play track method
   public void startPlay() {

      PlayerFragment.seekBar.setMax(PlayerFragment.mMediaPlayer.getDuration());
      // If player is not playing then play music
      if (!PlayerFragment.mMediaPlayer.isPlaying()) {
         PlayerFragment.mMediaPlayer.start();
         PlayerFragment.mMediaPlayer.seekTo(seek);


         // Rotate the artwork 360 degrees based on track duratoin
         final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
         rotate.setDuration(PlayerFragment.mMediaPlayer.getDuration());
         PlayerFragment.art.clearAnimation();
         PlayerFragment.art.startAnimation(rotate);

         // Call send notification method
         sendNotification();
      }
   }

   // Pause track method
   public void pausePlay() {
      // If player is playing then pause music
      if (PlayerFragment.mMediaPlayer.isPlaying()) {
         PlayerFragment.mMediaPlayer.pause();
         seek = PlayerFragment.mMediaPlayer.getCurrentPosition();
         PlayerFragment.art.clearAnimation();
      }
      // Close notification message
      stopForeground(true);
   }

   // Destroy method when service is closed
   @Override
   public void onDestroy() {
      if (PlayerFragment.mMediaPlayer != null) {
         PlayerFragment.mMediaPlayer.release();
         PlayerFragment.mMediaPlayer = null;
      }
      // Close notification message
      stopForeground(true);
   }

   // Create notification method for the current cong playing
   public void sendNotification() {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
      builder.setSmallIcon(R.drawable.small_icon)
         .setContentTitle(songs.get(PlayerFragment.count).getArtist())
         .setContentText(songs.get(PlayerFragment.count).getName())
         .setWhen(System.currentTimeMillis())
         .setOngoing(true);
      PlayerFragment.art.buildDrawingCache();
      Bitmap b = PlayerFragment.art.getDrawingCache();
      builder.setLargeIcon(b);
      NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
      bigStyle.bigText(songs.get(PlayerFragment.count).getArtist());
      bigStyle.setBigContentTitle(songs.get(PlayerFragment.count).getName());
      bigStyle.setSummaryText("Orange Slice Music");
      builder.setStyle(bigStyle);

      Intent startIntent = new Intent(this, MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this, REQUEST_CODE, startIntent, 0);
      builder.setContentIntent(contentIntent);
      Notification notification = builder.build();
      NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      startForeground(NOTIFICATION_ID, notification);
      notificationManager.notify(NOTIFICATION_ID, notification);
   }

   // Method to get miutes and seconds for each track to display track length in time
   static String getTimeString(long millis) {
      StringBuffer buf = new StringBuffer();

//      int hours = (int) (millis / (1000 * 60 * 60));
      int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
      int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

//      .append(String.format("%02d", hours))
//         .append(":")
      buf
         .append(String.format("%02d", minutes))
         .append(":")
         .append(String.format("%02d", seconds));

      return buf.toString();
   }
}
