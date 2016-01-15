package com.fullsail.orangemusic;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by isaiasrosario on 1/7/16.
 */

public class MainActivity extends AppCompatActivity {

   // Variables
   FragmentManager fragManager;
   static MusicService musicService;
   static boolean isBound;



   // Check to see if repeat is on variable
   public static boolean isLoop;

   @Override
   public void onConfigurationChanged(Configuration newConfig){
      super.onConfigurationChanged(newConfig);

//      if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//         setContentView(R.layout.activity_main);
//         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//         setSupportActionBar(toolbar);
//         fragManager = getFragmentManager();
//         fragManager.beginTransaction().replace(R.id.player_frame_layout, new PlayerFragment()).commit();
//
//      }else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//         setContentView(R.layout.activity_main);
//         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//         setSupportActionBar(toolbar);
//         fragManager = getFragmentManager();
//         fragManager.beginTransaction().replace(R.id.player_frame_layout, new PlayerFragment()).commit();
//
//      }
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      //Set up fragment with fragment manager
      if (savedInstanceState == null) {
         fragManager = getFragmentManager();
         fragManager.beginTransaction().replace(R.id.player_frame_layout, new PlayerFragment()).commit();
      }

      // Runs a thread in the background that updates the seekbar with the current track position
      Thread t = new Thread() {

         @Override
         public void run() {
            try {
               while (!isInterrupted()) {
                  Thread.sleep(10);
                  runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                        if (PlayerFragment.mMediaPlayer != null) {
                           PlayerFragment.position.setText(MusicService.getTimeString(
                              PlayerFragment.mMediaPlayer.getCurrentPosition()));

                           PlayerFragment.duration.setText(MusicService.getTimeString(
                              PlayerFragment.mMediaPlayer.getDuration() - PlayerFragment.mMediaPlayer.getCurrentPosition()));

                           PlayerFragment.seekBar.setProgress(PlayerFragment.mMediaPlayer.getCurrentPosition());
                        }
                     }
                  });
               }
            } catch (InterruptedException e) {
               System.out.println(e);
            }
         }
      };
      t.start();

      //Start MusicService class intent
      Intent intent = new Intent(this, MusicService.class);
      startService(intent);
   }

   // Creat new ServiceConncetion
   private ServiceConnection connection = new ServiceConnection() {
      // When connected to service
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
         musicService = ((MusicService.MyLocalBinder) service).getService();
         isBound = true;
      }

      // When disconnected from service
      @Override
      public void onServiceDisconnected(ComponentName name) {
         musicService = null;
         isBound = false;
      }
   };

   // On activity start bind the service
   @Override
   protected void onStart() {
      super.onStart();
      doBindingService();
   }

   // When back button is pressed move task to the back
   @Override
   public void onBackPressed() {
      moveTaskToBack(true);
   }

   // Bind service connection method
   private void doUnbidingService() {
      if (isBound) {
         unbindService(connection);
         isBound = false;
      }
   }

   //unBind service connection method
   private void doBindingService() {
      if (!isBound) {
         Intent bindIntent = new Intent(this, MusicService.class);
         isBound = bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
      }
   }

   // On destroy method when app is fully closed it will unbind service and stop intent
   @Override
   protected void onDestroy() {
      super.onDestroy();
      doUnbidingService();
      Intent intentStop = new Intent(this, MusicService.class);
      stopService(intentStop);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      Drawable newIcon = (Drawable)menu.getItem(0).getIcon();
      if (!isLoop){
         System.out.println("Repeat is off");
         isLoop = false;
         newIcon.mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
         menu.getItem(0).setIcon(newIcon);
      }else {
         System.out.println("Repeat is on");
         isLoop = true;
         newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
         menu.getItem(0).setIcon(newIcon);
      }
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      switch (id){
         case R.id.action_loop:
            Drawable newIcon = (Drawable)item.getIcon();
            if (isLoop){
               System.out.println("Repeat is off");
               isLoop = false;
               newIcon.mutate().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
               item.setIcon(newIcon);
            }else {
               System.out.println("Repeat is on");
               isLoop = true;
               newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
               item.setIcon(newIcon);
            }
      }

      return super.onOptionsItemSelected(item);
   }
}
