package com.smartdevice.aidltestdemo.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;

public class SystemUtil {
   public static int getScreenOrientent(Context var0) {
      Configuration var2 = var0.getResources().getConfiguration();
      byte var1 = 0;
      if(var2.orientation == 2) {
         var1 = 0;
      } else if(var2.orientation == 1) {
         var1 = 1;
      } else if(var2.hardKeyboardHidden == 1) {
         var1 = 2;
      } else if(var2.hardKeyboardHidden == 2) {
         var1 = 3;
      }

      return var1;
   }

   public static boolean isCameraCanUse() {
      boolean var0 = true;
      Camera var1 = null;

      label17: {
         Camera var2;
         try {
            var2 = Camera.open();
         } catch (Exception var3) {
            var0 = false;
            break label17;
         }

         var1 = var2;
      }

      if(var0) {
         var1.release();
      }

      return var0;
   }

   public static void startActivityForName(Context var0, String var1) {
      try {
         Class var4 = Class.forName(var1);
         Intent var2 = new Intent(var0, var4);
         var0.startActivity(var2);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }
}
