package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;

public abstract class Downsampler implements BitmapDecoder {
   public static final Downsampler AT_LEAST;
   public static final Downsampler AT_MOST;
   private static final int MARK_POSITION = 5242880;
   public static final Downsampler NONE;
   private static final Queue OPTIONS_QUEUE;
   private static final String TAG = "Downsampler";
   private static final Set TYPES_THAT_USE_POOL;

   static {
      TYPES_THAT_USE_POOL = EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG);
      OPTIONS_QUEUE = Util.createQueue(0);
      AT_LEAST = new Downsampler() {
         public String getId() {
            return "AT_LEAST.com.bumptech.glide.load.data.bitmap";
         }

         protected int getSampleSize(int var1, int var2, int var3, int var4) {
            return Math.min(var2 / var4, var1 / var3);
         }
      };
      AT_MOST = new Downsampler() {
         public String getId() {
            return "AT_MOST.com.bumptech.glide.load.data.bitmap";
         }

         protected int getSampleSize(int var1, int var2, int var3, int var4) {
            byte var5 = 1;
            var1 = (int)Math.ceil((double)Math.max((float)var2 / (float)var4, (float)var1 / (float)var3));
            var2 = Math.max(1, Integer.highestOneBit(var1));
            byte var6;
            if(var2 < var1) {
               var6 = var5;
            } else {
               var6 = 0;
            }

            return var2 << var6;
         }
      };
      NONE = new Downsampler() {
         public String getId() {
            return "NONE.com.bumptech.glide.load.data.bitmap";
         }

         protected int getSampleSize(int var1, int var2, int var3, int var4) {
            return 0;
         }
      };
   }

   private static Bitmap decodeStream(MarkEnforcingInputStream var0, RecyclableBufferedInputStream var1, Options var2) {
      if(var2.inJustDecodeBounds) {
         var0.mark(5242880);
      } else {
         var1.fixMarkLimit();
      }

      Bitmap var4 = BitmapFactory.decodeStream(var0, (Rect)null, var2);

      try {
         if(var2.inJustDecodeBounds) {
            var0.reset();
         }
      } catch (IOException var3) {
         if(Log.isLoggable("Downsampler", 6)) {
            Log.e("Downsampler", "Exception loading inDecodeBounds=" + var2.inJustDecodeBounds + " sample=" + var2.inSampleSize, var3);
         }
      }

      return var4;
   }

   private Bitmap downsampleWithSize(MarkEnforcingInputStream var1, RecyclableBufferedInputStream var2, Options var3, BitmapPool var4, int var5, int var6, int var7, DecodeFormat var8) {
      Config var9 = getConfig(var1, var8);
      var3.inSampleSize = var7;
      var3.inPreferredConfig = var9;
      if((var3.inSampleSize == 1 || 19 <= VERSION.SDK_INT) && shouldUsePool(var1)) {
         setInBitmap(var3, var4.getDirty((int)Math.ceil((double)var5 / (double)var7), (int)Math.ceil((double)var6 / (double)var7), var9));
      }

      return decodeStream(var1, var2, var3);
   }

   private static Config getConfig(InputStream var0, DecodeFormat var1) {
      Config var17;
      if(var1 != DecodeFormat.ALWAYS_ARGB_8888 && var1 != DecodeFormat.PREFER_ARGB_8888 && VERSION.SDK_INT != 16) {
         boolean var3 = false;
         var0.mark(1024);
         boolean var11 = false;

         boolean var2;
         label143: {
            label144: {
               try {
                  var11 = true;
                  ImageHeaderParser var18 = new ImageHeaderParser(var0);
                  var2 = var18.hasAlpha();
                  var11 = false;
                  break label144;
               } catch (IOException var15) {
                  if(Log.isLoggable("Downsampler", 5)) {
                     StringBuilder var4 = new StringBuilder();
                     Log.w("Downsampler", var4.append("Cannot determine whether the image has alpha or not from header for format ").append(var1).toString(), var15);
                     var11 = false;
                  } else {
                     var11 = false;
                  }
               } finally {
                  if(var11) {
                     try {
                        var0.reset();
                     } catch (IOException var12) {
                        if(Log.isLoggable("Downsampler", 5)) {
                           Log.w("Downsampler", "Cannot reset the input stream", var12);
                        }
                     }

                  }
               }

               try {
                  var0.reset();
               } catch (IOException var13) {
                  var2 = var3;
                  if(Log.isLoggable("Downsampler", 5)) {
                     Log.w("Downsampler", "Cannot reset the input stream", var13);
                     var2 = var3;
                  }
                  break label143;
               }

               var2 = var3;
               break label143;
            }

            var3 = var2;

            try {
               var0.reset();
            } catch (IOException var14) {
               var2 = var2;
               if(Log.isLoggable("Downsampler", 5)) {
                  Log.w("Downsampler", "Cannot reset the input stream", var14);
                  var2 = var3;
               }
               break label143;
            }

            var2 = var2;
         }

         if(var2) {
            var17 = Config.ARGB_8888;
         } else {
            var17 = Config.RGB_565;
         }
      } else {
         var17 = Config.ARGB_8888;
      }

      return var17;
   }

   @TargetApi(11)
   private static Options getDefaultOptions() {
      // $FF: Couldn't be decompiled
   }

   private int getRoundedSampleSize(int var1, int var2, int var3, int var4, int var5) {
      if(var5 == Integer.MIN_VALUE) {
         var5 = var3;
      }

      if(var4 == Integer.MIN_VALUE) {
         var4 = var2;
      }

      if(var1 != 90 && var1 != 270) {
         var1 = this.getSampleSize(var2, var3, var4, var5);
      } else {
         var1 = this.getSampleSize(var3, var2, var4, var5);
      }

      if(var1 == 0) {
         var1 = 0;
      } else {
         var1 = Integer.highestOneBit(var1);
      }

      return Math.max(1, var1);
   }

   private static void releaseOptions(Options param0) {
      // $FF: Couldn't be decompiled
   }

   @TargetApi(11)
   private static void resetOptions(Options var0) {
      var0.inTempStorage = null;
      var0.inDither = false;
      var0.inScaled = false;
      var0.inSampleSize = 1;
      var0.inPreferredConfig = null;
      var0.inJustDecodeBounds = false;
      var0.outWidth = 0;
      var0.outHeight = 0;
      var0.outMimeType = null;
      if(11 <= VERSION.SDK_INT) {
         var0.inBitmap = null;
         var0.inMutable = true;
      }

   }

   @TargetApi(11)
   private static void setInBitmap(Options var0, Bitmap var1) {
      if(11 <= VERSION.SDK_INT) {
         var0.inBitmap = var1;
      }

   }

   private static boolean shouldUsePool(InputStream var0) {
      boolean var1;
      if(19 <= VERSION.SDK_INT) {
         var1 = true;
      } else {
         var0.mark(1024);
         boolean var9 = false;

         boolean var2;
         label129: {
            try {
               var9 = true;
               ImageHeaderParser var3 = new ImageHeaderParser(var0);
               ImageHeaderParser.ImageType var15 = var3.getType();
               var2 = TYPES_THAT_USE_POOL.contains(var15);
               var9 = false;
               break label129;
            } catch (IOException var13) {
               if(Log.isLoggable("Downsampler", 5)) {
                  Log.w("Downsampler", "Cannot determine the image type from header", var13);
                  var9 = false;
               } else {
                  var9 = false;
               }
            } finally {
               if(var9) {
                  try {
                     var0.reset();
                  } catch (IOException var10) {
                     if(Log.isLoggable("Downsampler", 5)) {
                        Log.w("Downsampler", "Cannot reset the input stream", var10);
                     }
                  }

               }
            }

            try {
               var0.reset();
            } catch (IOException var11) {
               if(Log.isLoggable("Downsampler", 5)) {
                  Log.w("Downsampler", "Cannot reset the input stream", var11);
               }
            }

            var1 = false;
            return var1;
         }

         try {
            var0.reset();
         } catch (IOException var12) {
            var1 = var2;
            if(Log.isLoggable("Downsampler", 5)) {
               Log.w("Downsampler", "Cannot reset the input stream", var12);
               var1 = var2;
            }

            return var1;
         }

         var1 = var2;
      }

      return var1;
   }

   public Bitmap decode(InputStream param1, BitmapPool param2, int param3, int param4, DecodeFormat param5) {
      // $FF: Couldn't be decompiled
   }

   public int[] getDimensions(MarkEnforcingInputStream var1, RecyclableBufferedInputStream var2, Options var3) {
      var3.inJustDecodeBounds = true;
      decodeStream(var1, var2, var3);
      var3.inJustDecodeBounds = false;
      return new int[]{var3.outWidth, var3.outHeight};
   }

   protected abstract int getSampleSize(int var1, int var2, int var3, int var4);
}
