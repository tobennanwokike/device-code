package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.smartdevice.aidltestdemo.CaptureActivity;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraManager;
import com.smartdevice.aidltestdemo.camerascanner.camera.PlanarYUVLuminanceSource;
import com.smartdevice.aidltestdemo.util.SystemUtil;
import java.util.Hashtable;

final class DecodeHandler extends Handler {
   private static final String TAG = DecodeHandler.class.getSimpleName();
   private final CaptureActivity activity;
   private final MultiFormatReader multiFormatReader = new MultiFormatReader();

   DecodeHandler(CaptureActivity var1, Hashtable var2) {
      this.multiFormatReader.setHints(var2);
      this.activity = var1;
   }

   private void decode(byte[] var1, int var2, int var3) {
      int var4;
      int var5;
      long var9;
      byte[] var11;
      Bundle var12;
      label105: {
         var9 = System.currentTimeMillis();
         var12 = null;
         int var6 = SystemUtil.getScreenOrientent(this.activity);
         if(var6 != 1) {
            var11 = var1;
            var5 = var2;
            var4 = var3;
            if(var6 != 3) {
               break label105;
            }
         }

         var11 = new byte[var1.length];

         for(var4 = 0; var4 < var3; ++var4) {
            for(var5 = 0; var5 < var2; ++var5) {
               var11[var5 * var3 + var3 - var4 - 1] = var1[var4 * var2 + var5];
            }
         }

         var4 = var2;
         var5 = var3;
      }

      PlanarYUVLuminanceSource var20 = CameraManager.get().buildLuminanceSource(var11, var5, var4);
      BinaryBitmap var17 = new BinaryBitmap(new HybridBinarizer(var20));

      Result var18;
      label84: {
         try {
            var18 = this.multiFormatReader.decodeWithState(var17);
            break label84;
         } catch (ReaderException var15) {
            ;
         } finally {
            this.multiFormatReader.reset();
         }

         var18 = var12;
      }

      if(var18 != null) {
         long var7 = System.currentTimeMillis();
         Log.d(TAG, "Found barcode (" + (var7 - var9) + " ms):\n" + var18.toString());
         Message var19 = Message.obtain(this.activity.getHandler(), 2131230836, var18);
         var12 = new Bundle();
         var12.putParcelable("barcode_bitmap", var20.renderCroppedGreyscaleBitmap());
         var19.setData(var12);
         var19.sendToTarget();
      } else {
         Message.obtain(this.activity.getHandler(), 2131230835).sendToTarget();
      }

   }

   public void handleMessage(Message var1) {
      switch(var1.what) {
      case 2131230834:
         this.decode((byte[])((byte[])var1.obj), var1.arg1, var1.arg2);
         break;
      case 2131230900:
         Looper.myLooper().quit();
      }

   }
}
