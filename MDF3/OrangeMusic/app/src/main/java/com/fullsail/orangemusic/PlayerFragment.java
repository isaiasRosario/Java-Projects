package com.fullsail.orangemusic;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by isaiasrosario on 1/5/16.
 */
public class PlayerFragment extends Fragment {

   // Variables
   Uri uri, artUri;
   TextView tv;
   ImageView overlay;
   FloatingActionButton play;
   String artStr;

   static boolean isPaused;
   boolean isResumed;

   // Static variables shared with other classes
   static int count = 0;
   static ImageView art;
   static SeekBar seekBar;
   static TextView duration;
   static TextView position;
   static MediaPlayer mMediaPlayer;

   public PlayerFragment() {
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);

      // Set view
      View v = inflater.inflate(R.layout.player_main, container, false);

      // Initiate text views and artwork image views and the seek bar
      tv = (TextView) v.findViewById(R.id.TextView);
      tv.setSelected(true);
      art = (ImageView) v.findViewById(R.id.artWork);
      overlay = (ImageView) v.findViewById(R.id.overlay);

      duration = (TextView) v.findViewById(R.id.duration);
      position = (TextView) v.findViewById(R.id.position);
      seekBar = (SeekBar) v.findViewById(R.id.seekBar);

      // If first time opening app then create default track information for text and image
      if (savedInstanceState == null) {

         artStr = "glitch";
         artUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/glitch");
         uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/glitch");

         tv.setText("               " + "Jason Shaw" + ":  " + "Glitch" + "               ");

         art.setImageURI(artUri);
      }

      // Play floating action button with onclick listener that calls the togglePlayPause method
      play = (FloatingActionButton) getActivity().findViewById(R.id.play_pause_fab);
      play.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            togglePlayPause();
         }
      });

      // Play next track floating action button with on click listener that calls playNext method
      FloatingActionButton next = (FloatingActionButton) getActivity().findViewById(R.id.skip_next_fab);
      next.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            playNext();
         }
      });

      // Play prvious track floating action button with on click listener that calls playBack method
      FloatingActionButton backButton = (FloatingActionButton) getActivity().findViewById(R.id.skip_back_fab);
      backButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            playBack();
         }
      });

      return v;
   }

   // Activity created methos when fragment opens.
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      System.out.println("onAcivityPlayerFrag");
   }

   // Call onresume method when activity is resumed after the notification is pressed
   @Override
   public void onResume() {
      super.onResume();

      if (mMediaPlayer != null && !isPaused) {
         isResumed = true;

         seekBar.setMax(mMediaPlayer.getDuration());
         seekBar.setProgress(mMediaPlayer.getCurrentPosition());

         // Sets up artwork for the art ImageView
         String artStr = MusicService.songs.get(count).getArtwork();
         Uri artUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/" + artStr);
         art.setImageURI(artUri);

         // Rotate the artwork 360 degrees based on track duratoin
         final Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
         rotate.setDuration(PlayerFragment.mMediaPlayer.getDuration());
         art.startAnimation(rotate);

         mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
               complete();
            }

         });

         play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));

      } else {
         System.out.println("not resumed");
      }
   }

   // Toggle between play and pause method and checks to see if player is paused or playing
   private void togglePlayPause() {
      if (mMediaPlayer == null) {
         mMediaPlayer = new MediaPlayer();
      }

      if (isResumed || mMediaPlayer.isPlaying()) {
         isResumed = false;

         MainActivity.musicService.pausePlay();
         isPaused = true;
         play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));
      } else {

         try {
            if (isPaused) {
               isPaused = false;
            } else {
               mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }

         if (MainActivity.isBound) {
            seekBar.setMax(mMediaPlayer.getDuration());
            seekBar.setProgress(mMediaPlayer.getCurrentPosition());
            MainActivity.musicService.startPlay();


            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               public void onCompletion(MediaPlayer mp) {
                  complete();
               }

            });

            play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
         }
      }
   }

   // Play next track method and loops through playlist when it hits the last track
   void playNext() {
      count++;
      if (count < MusicService.songs.size()) {
         next();
         if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            if (MainActivity.isBound) {
               position.setText(MusicService.getTimeString(
                  mMediaPlayer.getCurrentPosition()));

               MainActivity.musicService.startPlay();

               play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
            }
         }

      } else {
         count = 0;
         next();
         if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            if (MainActivity.isBound) {
               position.setText(MusicService.getTimeString(
                  mMediaPlayer.getCurrentPosition()));

               MainActivity.musicService.startPlay();

               play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
            }
         }

      }// check for number of count
   }

   // Play previous track method and loops through playlist when it hits the last track
   void playBack() {
      if (count == 0) {
         count = 4;

         back();
         if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            if (MainActivity.isBound) {
               position.setText(MusicService.getTimeString(
                  mMediaPlayer.getCurrentPosition()));

               MainActivity.musicService.startPlay();

               play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
            }
         }

      } else {
         count--;
         back();
         if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            if (MainActivity.isBound) {
               position.setText(MusicService.getTimeString(
                  mMediaPlayer.getCurrentPosition()));

               MainActivity.musicService.startPlay();

               play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));
            }
         }

      }
   }

   // Sets up next track artwork and song information
   void next() {

      String artistStr = MusicService.songs.get(count).getArtist();
      String nameStr = MusicService.songs.get(count).getName();
      tv.setText("               " + artistStr + ":  " + nameStr + "                         ");

      String uriStr = MusicService.songs.get(count).getUri();
      uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + uriStr);
      String artStr = MusicService.songs.get(count).getArtwork();
      Uri artUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/" + artStr);
      art.setImageURI(artUri);
   }

   // Sets up previuos track artwork and song information
   void back() {

      String artistStr = MusicService.songs.get(count).getArtist();
      String nameStr = MusicService.songs.get(count).getName();
      tv.setText("               " + artistStr + ":  " + nameStr + "                         ");

      String uriStr = MusicService.songs.get(count).getUri();
      uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + uriStr);
      String artStr = MusicService.songs.get(count).getArtwork();
      Uri artUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/" + artStr);
      art.setImageURI(artUri);
   }

   // Method that is called when each song is finished then goes to the next track
   // if its the last track on the playlist it will the stop music player
   void complete() {
      if (count < MusicService.songs.size() - 1) {
         count++;
         next();
         if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            position.setText(MusicService.getTimeString(
               mMediaPlayer.getCurrentPosition()));

            MainActivity.musicService.startPlay();

            play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_pause));

         }
      } else {
         if (mMediaPlayer != null) {
            MainActivity.musicService.pausePlay();
            art.clearAnimation();
            count = 0;
            next();
            mMediaPlayer.reset();
            MusicService.seek = 0;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String uriStr = MusicService.songs.get(count).getUri();
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + uriStr);
            try {
               mMediaPlayer.setDataSource(getActivity(), uri);
               mMediaPlayer.prepare();
            } catch (IOException e) {
               e.printStackTrace();
            }

            isPaused = true;
            play.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_media_play));

         }
      }
   }

   // ****************
   // *******  Working on adding sound cloud api for the music data in the near future.
   // ****************
   //      new DownloadImageTask(art).execute("https://i1.sndcdn.com/artworks-000141748082-iluj2w-large.jpg");
   //      mMediaPlayer.setDataSource("https://api.soundcloud.com/tracks/240241506/stream?client_id=f609f86b1f00788171c3e71a87ae38b8");
   private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
      ImageView bmImage;

      public DownloadImageTask(ImageView bmImage) {
         this.bmImage = bmImage;
      }

      protected Bitmap doInBackground(String... urls) {
         String urldisplay = urls[0];
         Bitmap img = null;
         try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            img = BitmapFactory.decodeStream(in);
         } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
         }
         return img;
      }

      protected void onPostExecute(Bitmap result) {
         bmImage.setImageBitmap(result);
      }
   }
}