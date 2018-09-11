package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompatApi21;
import android.support.v4.content.ContextCompatApi23;
import android.support.v4.content.ContextCompatApi24;
import android.support.v4.content.ContextCompatHoneycomb;
import android.support.v4.content.ContextCompatJellybean;
import android.support.v4.content.ContextCompatKitKat;
import android.support.v4.os.BuildCompat;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
   private static final String DIR_ANDROID = "Android";
   private static final String DIR_OBB = "obb";
   private static final String TAG = "ContextCompat";
   private static final Object sLock = new Object();
   private static TypedValue sTempValue;

   private static File buildPath(File var0, String... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         if(var0 == null) {
            var0 = new File(var4);
         } else if(var4 != null) {
            var0 = new File(var0, var4);
         }
      }

      return var0;
   }

   public static int checkSelfPermission(@NonNull Context var0, @NonNull String var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("permission is null");
      } else {
         return var0.checkPermission(var1, Process.myPid(), Process.myUid());
      }
   }

   public static Context createDeviceProtectedStorageContext(Context var0) {
      if(BuildCompat.isAtLeastN()) {
         var0 = ContextCompatApi24.createDeviceProtectedStorageContext(var0);
      } else {
         var0 = null;
      }

      return var0;
   }

   private static File createFilesDir(File param0) {
      // $FF: Couldn't be decompiled
   }

   public static File getCodeCacheDir(Context var0) {
      File var1;
      if(VERSION.SDK_INT >= 21) {
         var1 = ContextCompatApi21.getCodeCacheDir(var0);
      } else {
         var1 = createFilesDir(new File(var0.getApplicationInfo().dataDir, "code_cache"));
      }

      return var1;
   }

   @ColorInt
   public static final int getColor(Context var0, @ColorRes int var1) {
      if(VERSION.SDK_INT >= 23) {
         var1 = ContextCompatApi23.getColor(var0, var1);
      } else {
         var1 = var0.getResources().getColor(var1);
      }

      return var1;
   }

   public static final ColorStateList getColorStateList(Context var0, @ColorRes int var1) {
      ColorStateList var2;
      if(VERSION.SDK_INT >= 23) {
         var2 = ContextCompatApi23.getColorStateList(var0, var1);
      } else {
         var2 = var0.getResources().getColorStateList(var1);
      }

      return var2;
   }

   public static File getDataDir(Context var0) {
      File var1;
      if(BuildCompat.isAtLeastN()) {
         var1 = ContextCompatApi24.getDataDir(var0);
      } else {
         String var2 = var0.getApplicationInfo().dataDir;
         if(var2 != null) {
            var1 = new File(var2);
         } else {
            var1 = null;
         }
      }

      return var1;
   }

   public static final Drawable getDrawable(Context param0, @DrawableRes int param1) {
      // $FF: Couldn't be decompiled
   }

   public static File[] getExternalCacheDirs(Context var0) {
      File[] var2;
      if(VERSION.SDK_INT >= 19) {
         var2 = ContextCompatKitKat.getExternalCacheDirs(var0);
      } else {
         File[] var1 = new File[]{var0.getExternalCacheDir()};
         var2 = var1;
      }

      return var2;
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      File[] var3;
      if(VERSION.SDK_INT >= 19) {
         var3 = ContextCompatKitKat.getExternalFilesDirs(var0, var1);
      } else {
         File[] var2 = new File[]{var0.getExternalFilesDir(var1)};
         var3 = var2;
      }

      return var3;
   }

   public static final File getNoBackupFilesDir(Context var0) {
      File var1;
      if(VERSION.SDK_INT >= 21) {
         var1 = ContextCompatApi21.getNoBackupFilesDir(var0);
      } else {
         var1 = createFilesDir(new File(var0.getApplicationInfo().dataDir, "no_backup"));
      }

      return var1;
   }

   public static File[] getObbDirs(Context var0) {
      int var1 = VERSION.SDK_INT;
      File[] var3;
      if(var1 >= 19) {
         var3 = ContextCompatKitKat.getObbDirs(var0);
      } else {
         File var4;
         if(var1 >= 11) {
            var4 = ContextCompatHoneycomb.getObbDir(var0);
         } else {
            var4 = buildPath(Environment.getExternalStorageDirectory(), new String[]{"Android", "obb", var0.getPackageName()});
         }

         File[] var2 = new File[]{var4};
         var3 = var2;
      }

      return var3;
   }

   public static boolean isDeviceProtectedStorage(Context var0) {
      boolean var1;
      if(BuildCompat.isAtLeastN()) {
         var1 = ContextCompatApi24.isDeviceProtectedStorage(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean startActivities(Context var0, Intent[] var1) {
      return startActivities(var0, var1, (Bundle)null);
   }

   public static boolean startActivities(Context var0, Intent[] var1, Bundle var2) {
      boolean var4 = true;
      int var3 = VERSION.SDK_INT;
      if(var3 >= 16) {
         ContextCompatJellybean.startActivities(var0, var1, var2);
      } else if(var3 >= 11) {
         ContextCompatHoneycomb.startActivities(var0, var1);
      } else {
         var4 = false;
      }

      return var4;
   }

   public static void startActivity(Context var0, Intent var1, @Nullable Bundle var2) {
      if(VERSION.SDK_INT >= 16) {
         ContextCompatJellybean.startActivity(var0, var1, var2);
      } else {
         var0.startActivity(var1);
      }

   }
}
