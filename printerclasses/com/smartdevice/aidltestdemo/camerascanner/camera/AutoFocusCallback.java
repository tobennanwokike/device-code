package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback {
   private static final long AUTOFOCUS_INTERVAL_MS = 1500L;
   private static final String TAG = AutoFocusCallback.class.getSimpleName();
   private Handler autoFocusHandler;
   private int autoFocusMessage;

   public void onAutoFocus(boolean var1, Camera var2) {
      if(this.autoFocusHandler != null) {
         Message var3 = this.autoFocusHandler.obtainMessage(this.autoFocusMessage, Boolean.valueOf(var1));
         this.autoFocusHandler.sendMessageDelayed(var3, 1500L);
         this.autoFocusHandler = null;
      } else {
         Log.d(TAG, "Got auto-focus callback, but no handler for it");
      }

   }

   void setHandler(Handler var1, int var2) {
      this.autoFocusHandler = var1;
      this.autoFocusMessage = var2;
   }
}
