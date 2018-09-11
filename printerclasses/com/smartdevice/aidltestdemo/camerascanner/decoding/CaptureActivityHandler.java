package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.Result;
import com.smartdevice.aidltestdemo.CaptureActivity;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraManager;
import com.smartdevice.aidltestdemo.camerascanner.decoding.DecodeThread;
import com.smartdevice.aidltestdemo.camerascanner.view.ViewfinderResultPointCallback;
import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
   private static final String TAG = CaptureActivityHandler.class.getSimpleName();
   private final CaptureActivity activity;
   private final DecodeThread decodeThread;
   private CaptureActivityHandler.State state;

   public CaptureActivityHandler(CaptureActivity var1, Vector var2, String var3) {
      this.activity = var1;
      this.decodeThread = new DecodeThread(var1, var2, var3, new ViewfinderResultPointCallback(var1.getViewfinderView()));
      this.decodeThread.start();
      this.state = CaptureActivityHandler.State.SUCCESS;
      CameraManager.get().startPreview();
      this.restartPreviewAndDecode();
   }

   public void handleMessage(Message var1) {
      switch(var1.what) {
      case 2131230744:
         if(this.state == CaptureActivityHandler.State.PREVIEW) {
            CameraManager.get().requestAutoFocus(this, 2131230744);
         }
         break;
      case 2131230835:
         this.state = CaptureActivityHandler.State.PREVIEW;
         CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), 2131230834);
         break;
      case 2131230836:
         Log.d(TAG, "Got decode succeeded message");
         this.state = CaptureActivityHandler.State.SUCCESS;
         Bundle var2 = var1.getData();
         Bitmap var4;
         if(var2 == null) {
            var4 = null;
         } else {
            var4 = (Bitmap)var2.getParcelable("barcode_bitmap");
         }

         this.activity.handleDecode((Result)var1.obj, var4);
         break;
      case 2131230872:
         Log.d(TAG, "Got product query message");
         Intent var3 = new Intent("android.intent.action.VIEW", Uri.parse((String)var1.obj));
         var3.addFlags(524288);
         this.activity.startActivity(var3);
         break;
      case 2131230916:
         Log.d(TAG, "Got restart preview message");
         this.restartPreviewAndDecode();
         break;
      case 2131230917:
         Log.d(TAG, "Got return scan result message");
         this.activity.setResult(-1, (Intent)var1.obj);
         this.activity.finish();
      }

   }

   public void quitSynchronously() {
      this.state = CaptureActivityHandler.State.DONE;
      CameraManager.get().stopPreview();
      Message.obtain(this.decodeThread.getHandler(), 2131230900).sendToTarget();

      try {
         this.decodeThread.join();
      } catch (InterruptedException var2) {
         ;
      }

      this.removeMessages(2131230836);
      this.removeMessages(2131230835);
   }

   public void restartPreviewAndDecode() {
      if(this.state == CaptureActivityHandler.State.SUCCESS) {
         this.state = CaptureActivityHandler.State.PREVIEW;
         CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), 2131230834);
         CameraManager.get().requestAutoFocus(this, 2131230744);
         this.activity.drawViewfinder();
      }

   }

   private static enum State {
      DONE,
      PREVIEW,
      SUCCESS;
   }
}
