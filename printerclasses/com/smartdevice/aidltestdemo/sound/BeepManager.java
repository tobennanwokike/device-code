package com.smartdevice.aidltestdemo.sound;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import java.io.Closeable;

public class BeepManager implements OnCompletionListener, OnErrorListener, Closeable {
   private static final float BEEP_VOLUME = 0.1F;
   public static final String KEY_PLAY_BEEP = "preferences_play_beep";
   private static final String TAG = BeepManager.class.getSimpleName();
   private static final long VIBRATE_DURATION = 200L;
   public final String KEY_VIBRATE = "preferences_vibrate";
   private final Activity activity;
   private MediaPlayer mediaPlayer;
   private boolean playBeep;
   private boolean vibrate;

   public BeepManager(Activity var1) {
      this.activity = var1;
      this.mediaPlayer = null;
      this.updatePrefs();
   }

   private MediaPlayer buildMediaPlayer(Context param1) {
      // $FF: Couldn't be decompiled
   }

   private static boolean shouldBeep(SharedPreferences var0, Context var1) {
      boolean var3 = var0.getBoolean("preferences_play_beep", true);
      boolean var2 = var3;
      if(var3) {
         var2 = var3;
         if(((AudioManager)var1.getSystemService("audio")).getRingerMode() != 2) {
            var2 = false;
         }
      }

      return var2;
   }

   public void close() {
      synchronized(this){}

      try {
         if(this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
         }
      } finally {
         ;
      }

   }

   public void onCompletion(MediaPlayer var1) {
      var1.seekTo(0);
   }

   public boolean onError(MediaPlayer param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public void playBeepSoundAndVibrate() {
      synchronized(this){}

      try {
         if(this.playBeep && this.mediaPlayer != null) {
            this.mediaPlayer.start();
         }

         if(this.vibrate) {
            ((Vibrator)this.activity.getSystemService("vibrator")).vibrate(200L);
         }
      } finally {
         ;
      }

   }

   public void updatePrefs() {
      synchronized(this){}

      try {
         SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this.activity);
         this.playBeep = shouldBeep(var1, this.activity);
         this.vibrate = var1.getBoolean("preferences_vibrate", false);
         if(this.playBeep && this.mediaPlayer == null) {
            this.activity.setVolumeControlStream(3);
            this.mediaPlayer = this.buildMediaPlayer(this.activity);
         }
      } finally {
         ;
      }

   }
}
