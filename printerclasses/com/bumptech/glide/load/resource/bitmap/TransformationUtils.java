package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class TransformationUtils {
   public static final int PAINT_FLAGS = 6;
   private static final String TAG = "TransformationUtils";

   public static Bitmap centerCrop(Bitmap var0, Bitmap var1, int var2, int var3) {
      Bitmap var7;
      if(var1 == null) {
         var7 = null;
      } else {
         if(var1.getWidth() == var2) {
            var7 = var1;
            if(var1.getHeight() == var3) {
               return var7;
            }
         }

         float var4 = 0.0F;
         float var6 = 0.0F;
         Matrix var8 = new Matrix();
         float var5;
         if(var1.getWidth() * var3 > var1.getHeight() * var2) {
            var5 = (float)var3 / (float)var1.getHeight();
            var4 = ((float)var2 - (float)var1.getWidth() * var5) * 0.5F;
         } else {
            var5 = (float)var2 / (float)var1.getWidth();
            var6 = ((float)var3 - (float)var1.getHeight() * var5) * 0.5F;
         }

         var8.setScale(var5, var5);
         var8.postTranslate((float)((int)(var4 + 0.5F)), (float)((int)(var6 + 0.5F)));
         if(var0 == null) {
            var0 = Bitmap.createBitmap(var2, var3, getSafeConfig(var1));
         }

         setAlpha(var1, var0);
         (new Canvas(var0)).drawBitmap(var1, var8, new Paint(6));
         var7 = var0;
      }

      return var7;
   }

   public static Bitmap fitCenter(Bitmap var0, BitmapPool var1, int var2, int var3) {
      Bitmap var9;
      if(var0.getWidth() == var2 && var0.getHeight() == var3) {
         var9 = var0;
         if(Log.isLoggable("TransformationUtils", 2)) {
            Log.v("TransformationUtils", "requested target size matches input, returning input");
            var9 = var0;
         }
      } else {
         float var4 = Math.min((float)var2 / (float)var0.getWidth(), (float)var3 / (float)var0.getHeight());
         int var5 = (int)((float)var0.getWidth() * var4);
         int var6 = (int)((float)var0.getHeight() * var4);
         if(var0.getWidth() == var5 && var0.getHeight() == var6) {
            var9 = var0;
            if(Log.isLoggable("TransformationUtils", 2)) {
               Log.v("TransformationUtils", "adjusted target size matches input, returning input");
               var9 = var0;
            }
         } else {
            Config var8 = getSafeConfig(var0);
            Bitmap var7 = var1.get(var5, var6, var8);
            var9 = var7;
            if(var7 == null) {
               var9 = Bitmap.createBitmap(var5, var6, var8);
            }

            setAlpha(var0, var9);
            if(Log.isLoggable("TransformationUtils", 2)) {
               Log.v("TransformationUtils", "request: " + var2 + "x" + var3);
               Log.v("TransformationUtils", "toFit:   " + var0.getWidth() + "x" + var0.getHeight());
               Log.v("TransformationUtils", "toReuse: " + var9.getWidth() + "x" + var9.getHeight());
               Log.v("TransformationUtils", "minPct:   " + var4);
            }

            Canvas var10 = new Canvas(var9);
            Matrix var11 = new Matrix();
            var11.setScale(var4, var4);
            var10.drawBitmap(var0, var11, new Paint(6));
         }
      }

      return var9;
   }

   public static int getExifOrientationDegrees(int var0) {
      short var1;
      switch(var0) {
      case 3:
      case 4:
         var1 = 180;
         break;
      case 5:
      case 6:
         var1 = 90;
         break;
      case 7:
      case 8:
         var1 = 270;
         break;
      default:
         var1 = 0;
      }

      return var1;
   }

   @Deprecated
   @TargetApi(5)
   public static int getOrientation(String var0) {
      byte var2 = 0;

      int var1;
      try {
         ExifInterface var3 = new ExifInterface(var0);
         var1 = getExifOrientationDegrees(var3.getAttributeInt("Orientation", 0));
      } catch (Exception var4) {
         var1 = var2;
         if(Log.isLoggable("TransformationUtils", 6)) {
            Log.e("TransformationUtils", "Unable to get orientation for image with path=" + var0, var4);
            var1 = var2;
         }
      }

      return var1;
   }

   private static Config getSafeConfig(Bitmap var0) {
      Config var1;
      if(var0.getConfig() != null) {
         var1 = var0.getConfig();
      } else {
         var1 = Config.ARGB_8888;
      }

      return var1;
   }

   static void initializeMatrixForRotation(int var0, Matrix var1) {
      switch(var0) {
      case 2:
         var1.setScale(-1.0F, 1.0F);
         break;
      case 3:
         var1.setRotate(180.0F);
         break;
      case 4:
         var1.setRotate(180.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 5:
         var1.setRotate(90.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 6:
         var1.setRotate(90.0F);
         break;
      case 7:
         var1.setRotate(-90.0F);
         var1.postScale(-1.0F, 1.0F);
         break;
      case 8:
         var1.setRotate(-90.0F);
      }

   }

   @Deprecated
   public static Bitmap orientImage(String var0, Bitmap var1) {
      return rotateImage(var1, getOrientation(var0));
   }

   public static Bitmap rotateImage(Bitmap var0, int var1) {
      Bitmap var3 = var0;
      if(var1 != 0) {
         try {
            Matrix var5 = new Matrix();
            var5.setRotate((float)var1);
            var3 = Bitmap.createBitmap(var0, 0, 0, var0.getWidth(), var0.getHeight(), var5, true);
         } catch (Exception var4) {
            var3 = var0;
            if(Log.isLoggable("TransformationUtils", 6)) {
               Log.e("TransformationUtils", "Exception when trying to orient image", var4);
               var3 = var0;
            }
         }
      }

      return var3;
   }

   public static Bitmap rotateImageExif(Bitmap var0, BitmapPool var1, int var2) {
      Matrix var5 = new Matrix();
      initializeMatrixForRotation(var2, var5);
      if(!var5.isIdentity()) {
         RectF var6 = new RectF(0.0F, 0.0F, (float)var0.getWidth(), (float)var0.getHeight());
         var5.mapRect(var6);
         int var3 = Math.round(var6.width());
         var2 = Math.round(var6.height());
         Config var7 = getSafeConfig(var0);
         Bitmap var4 = var1.get(var3, var2, var7);
         Bitmap var8 = var4;
         if(var4 == null) {
            var8 = Bitmap.createBitmap(var3, var2, var7);
         }

         var5.postTranslate(-var6.left, -var6.top);
         (new Canvas(var8)).drawBitmap(var0, var5, new Paint(6));
         var0 = var8;
      }

      return var0;
   }

   @TargetApi(12)
   public static void setAlpha(Bitmap var0, Bitmap var1) {
      if(VERSION.SDK_INT >= 12 && var1 != null) {
         var1.setHasAlpha(var0.hasAlpha());
      }

   }
}
