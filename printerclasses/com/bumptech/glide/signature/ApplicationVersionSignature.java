package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.StringSignature;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ApplicationVersionSignature {
   private static final ConcurrentHashMap PACKAGE_NAME_TO_KEY = new ConcurrentHashMap();

   public static Key obtain(Context var0) {
      String var3 = var0.getPackageName();
      Key var2 = (Key)PACKAGE_NAME_TO_KEY.get(var3);
      Key var1 = var2;
      if(var2 == null) {
         Key var4 = obtainVersionSignature(var0);
         var2 = (Key)PACKAGE_NAME_TO_KEY.putIfAbsent(var3, var4);
         var1 = var2;
         if(var2 == null) {
            var1 = var4;
         }
      }

      return var1;
   }

   private static Key obtainVersionSignature(Context var0) {
      Object var1 = null;

      PackageInfo var3;
      try {
         var3 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
      } catch (NameNotFoundException var2) {
         var2.printStackTrace();
         var3 = (PackageInfo)var1;
      }

      String var4;
      if(var3 != null) {
         var4 = String.valueOf(var3.versionCode);
      } else {
         var4 = UUID.randomUUID().toString();
      }

      return new StringSignature(var4);
   }

   static void reset() {
      PACKAGE_NAME_TO_KEY.clear();
   }
}
