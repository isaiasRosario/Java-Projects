package com.fullsail.orangemusic;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

   // Variables
   FragmentManager fragManager;
   static MusicService musicService;
   static boolean isBound;

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
}
