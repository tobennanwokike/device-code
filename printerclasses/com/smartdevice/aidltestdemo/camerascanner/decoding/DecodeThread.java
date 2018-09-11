package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.os.Handler;
import android.os.Looper;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.smartdevice.aidltestdemo.CaptureActivity;
import com.smartdevice.aidltestdemo.camerascanner.decoding.DecodeFormatManager;
import com.smartdevice.aidltestdemo.camerascanner.decoding.DecodeHandler;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

final class DecodeThread extends Thread {
   public static final String BARCODE_BITMAP = "barcode_bitmap";
   private final CaptureActivity activity;
   private Handler handler;
   private final CountDownLatch handlerInitLatch;
   private final Hashtable hints;

   DecodeThread(CaptureActivity var1, Vector var2, String var3, ResultPointCallback var4) {
      Vector var5;
      label15: {
         super();
         this.activity = var1;
         this.handlerInitLatch = new CountDownLatch(1);
         this.hints = new Hashtable(3);
         if(var2 != null) {
            var5 = var2;
            if(!var2.isEmpty()) {
               break label15;
            }
         }

         var5 = new Vector();
         var5.addAll(DecodeFormatManager.ONE_D_FORMATS);
         var5.addAll(DecodeFormatManager.QR_CODE_FORMATS);
         var5.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
      }

      this.hints.put(DecodeHintType.POSSIBLE_FORMATS, var5);
      if(var3 != null) {
         this.hints.put(DecodeHintType.CHARACTER_SET, var3);
      }

      this.hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, var4);
   }

   Handler getHandler() {
      try {
         this.handlerInitLatch.await();
      } catch (InterruptedException var2) {
         ;
      }

      return this.handler;
   }

   public void run() {
      Looper.prepare();
      this.handler = new DecodeHandler(this.activity, this.hints);
      this.handlerInitLatch.countDown();
      Looper.loop();
   }
}
