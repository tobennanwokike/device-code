package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

class ConfigurationHelperGingerbread {
   static int getDensityDpi(@NonNull Resources var0) {
      return var0.getDisplayMetrics().densityDpi;
   }

   static int getScreenHeightDp(@NonNull Resources var0) {
      DisplayMetrics var1 = var0.getDisplayMetrics();
      return (int)((float)var1.heightPixels / var1.density);
   }

   static int getScreenWidthDp(@NonNull Resources var0) {
      DisplayMetrics var1 = var0.getDisplayMetrics();
      return (int)((float)var1.widthPixels / var1.density);
   }

   static int getSmallestScreenWidthDp(@NonNull Resources var0) {
      return Math.min(getScreenWidthDp(var0), getScreenHeightDp(var0));
   }
}
