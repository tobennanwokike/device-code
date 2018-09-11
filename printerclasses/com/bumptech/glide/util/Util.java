package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Looper;
import android.os.Build.VERSION;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class Util {
   private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
   private static final char[] SHA_1_CHARS = new char[40];
   private static final char[] SHA_256_CHARS = new char[64];

   public static void assertBackgroundThread() {
      if(!isOnBackgroundThread()) {
         throw new IllegalArgumentException("YOu must call this method on a background thread");
      }
   }

   public static void assertMainThread() {
      if(!isOnMainThread()) {
         throw new IllegalArgumentException("You must call this method on the main thread");
      }
   }

   private static String bytesToHex(byte[] var0, char[] var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2] & 255;
         var1[var2 * 2] = HEX_CHAR_ARRAY[var3 >>> 4];
         var1[var2 * 2 + 1] = HEX_CHAR_ARRAY[var3 & 15];
      }

      return new String(var1);
   }

   public static Queue createQueue(int var0) {
      return new ArrayDeque(var0);
   }

   public static int getBitmapByteSize(int var0, int var1, Config var2) {
      return var0 * var1 * getBytesPerPixel(var2);
   }

   @TargetApi(19)
   public static int getBitmapByteSize(Bitmap var0) {
      int var1;
      if(VERSION.SDK_INT >= 19) {
         try {
            var1 = var0.getAllocationByteCount();
            return var1;
         } catch (NullPointerException var3) {
            ;
         }
      }

      var1 = var0.getHeight() * var0.getRowBytes();
      return var1;
   }

   private static int getBytesPerPixel(Config var0) {
      Config var2 = var0;
      if(var0 == null) {
         var2 = Config.ARGB_8888;
      }

      byte var1;
      switch(null.$SwitchMap$android$graphics$Bitmap$Config[var2.ordinal()]) {
      case 1:
         var1 = 1;
         break;
      case 2:
      case 3:
         var1 = 2;
         break;
      default:
         var1 = 4;
      }

      return var1;
   }

   @Deprecated
   public static int getSize(Bitmap var0) {
      return getBitmapByteSize(var0);
   }

   public static List getSnapshot(Collection var0) {
      ArrayList var1 = new ArrayList(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      return var1;
   }

   public static boolean isOnBackgroundThread() {
      boolean var0;
      if(!isOnMainThread()) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isOnMainThread() {
      boolean var0;
      if(Looper.myLooper() == Looper.getMainLooper()) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private static boolean isValidDimension(int var0) {
      boolean var1;
      if(var0 <= 0 && var0 != Integer.MIN_VALUE) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isValidDimensions(int var0, int var1) {
      boolean var2;
      if(isValidDimension(var0) && isValidDimension(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static String sha1BytesToHex(byte[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String sha256BytesToHex(byte[] param0) {
      // $FF: Couldn't be decompiled
   }
}
