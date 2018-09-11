package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.annotation.NonNull;

class ConfigurationHelperHoneycombMr2 {
   static int getScreenHeightDp(@NonNull Resources var0) {
      return var0.getConfiguration().screenHeightDp;
   }

   static int getScreenWidthDp(@NonNull Resources var0) {
      return var0.getConfiguration().screenWidthDp;
   }

   static int getSmallestScreenWidthDp(@NonNull Resources var0) {
      return var0.getConfiguration().smallestScreenWidthDp;
   }
}
