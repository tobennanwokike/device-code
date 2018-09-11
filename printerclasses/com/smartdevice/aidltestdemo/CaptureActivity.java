package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.TextView;
import com.google.zxing.Result;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraManager;
import com.smartdevice.aidltestdemo.camerascanner.decoding.CaptureActivityHandler;
import com.smartdevice.aidltestdemo.camerascanner.decoding.InactivityTimer;
import com.smartdevice.aidltestdemo.camerascanner.view.ViewfinderView;
import java.io.IOException;
import java.util.Vector;

public class CaptureActivity extends Activity implements Callback {
   private static final float BEEP_VOLUME = 0.1F;
   private static final long VIBRATE_DURATION = 200L;
   private final OnCompletionListener beepListener = new OnCompletionListener() {
      public void onCompletion(MediaPlayer var1) {
         var1.seekTo(0);
      }
   };
   private String characterSet;
   private Vector decodeFormats;
   private CaptureActivityHandler handler;
   private boolean hasSurface;
   private InactivityTimer inactivityTimer;
   private MediaPlayer mediaPlayer;
   private boolean playBeep;
   private TextView txtResult;
   private boolean vibrate;
   private ViewfinderView viewfinderView;

   private void initBeepSound() {
      if(this.playBeep && this.mediaPlayer == null) {
         this.setVolumeControlStream(3);
         this.mediaPlayer = new MediaPlayer();
         this.mediaPlayer.setAudioStreamType(3);
         this.mediaPlayer.setOnCompletionListener(this.beepListener);
         AssetFileDescriptor var1 = this.getResources().openRawResourceFd(2131492864);

         try {
            this.mediaPlayer.setDataSource(var1.getFileDescriptor(), var1.getStartOffset(), var1.getLength());
            var1.close();
            this.mediaPlayer.setVolume(0.1F, 0.1F);
            this.mediaPlayer.prepare();
         } catch (IOException var2) {
            this.mediaPlayer = null;
         }
      }

   }

   private void initCamera(SurfaceHolder var1) {
      try {
         CameraManager.get().openDriver(var1);
      } catch (IOException var2) {
         return;
      } catch (RuntimeException var3) {
         return;
      }

      if(this.handler == null) {
         this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.characterSet);
      }

   }

   private void playBeepSoundAndVibrate() {
      if(this.playBeep && this.mediaPlayer != null) {
         this.mediaPlayer.start();
      }

      if(this.vibrate) {
         ((Vibrator)this.getSystemService("vibrator")).vibrate(200L);
      }

   }

   public void continuePreview() {
      if(this.handler != null) {
         this.handler.restartPreviewAndDecode();
      }

   }

   public void drawViewfinder() {
      this.viewfinderView.drawViewfinder();
   }

   public Handler getHandler() {
      return this.handler;
   }

   public ViewfinderView getViewfinderView() {
      return this.viewfinderView;
   }

   public void handleDecode(Result var1, Bitmap var2) {
      this.inactivityTimer.onActivity();
      this.playBeepSoundAndVibrate();
      this.txtResult.setText(var1.getBarcodeFormat().toString() + ":" + var1.getText());
      this.continuePreview();
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361830);
      CameraManager.init(this.getApplication());
      this.viewfinderView = (ViewfinderView)this.findViewById(2131230984);
      this.txtResult = (TextView)this.findViewById(2131230981);
      this.hasSurface = false;
      this.inactivityTimer = new InactivityTimer(this);
   }

   protected void onDestroy() {
      this.inactivityTimer.shutdown();
      super.onDestroy();
   }

   protected void onPause() {
      super.onPause();
      if(this.handler != null) {
         this.handler.quitSynchronously();
         this.handler = null;
      }

      CameraManager.get().closeDriver();
   }

   protected void onResume() {
      super.onResume();
      SurfaceHolder var1 = ((SurfaceView)this.findViewById(2131230895)).getHolder();
      if(this.hasSurface) {
         this.initCamera(var1);
      } else {
         var1.addCallback(this);
         var1.setType(3);
      }

      this.decodeFormats = null;
      this.characterSet = null;
      this.playBeep = true;
      if(((AudioManager)this.getSystemService("audio")).getRingerMode() != 2) {
         this.playBeep = false;
      }

      this.initBeepSound();
      this.vibrate = true;
   }

   public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
   }

   public void surfaceCreated(SurfaceHolder var1) {
      if(!this.hasSurface) {
         this.hasSurface = true;
         this.initCamera(var1);
      }

   }

   public void surfaceDestroyed(SurfaceHolder var1) {
      this.hasSurface = false;
   }
}
