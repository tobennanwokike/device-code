package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraConfigurationManager;

final class PreviewCallback implements android.hardware.Camera.PreviewCallback {
   private static final String TAG = PreviewCallback.class.getSimpleName();
   private final CameraConfigurationManager configManager;
   private Handler previewHandler;
   private int previewMessage;
   private final boolean useOneShotPreviewCallback;

   PreviewCallback(CameraConfigurationManager var1, boolean var2) {
      this.configManager = var1;
      this.useOneShotPreviewCallback = var2;
   }

   public void onPreviewFrame(byte[] var1, Camera var2) {
      Point var3 = this.configManager.getCameraResolution();
      if(!this.useOneShotPreviewCallback) {
         var2.setPreviewCallback((android.hardware.Camera.PreviewCallback)null);
      }

      if(this.previewHandler != null) {
         this.previewHandler.obtainMessage(this.previewMessage, var3.x, var3.y, var1).sendToTarget();
         this.previewHandler = null;
      } else {
         Log.d(TAG, "Got preview callback, but no handler for it");
      }

   }

   void setHandler(Handler var1, int var2) {
      this.previewHandler = var1;
      this.previewMessage = var2;
   }
}
