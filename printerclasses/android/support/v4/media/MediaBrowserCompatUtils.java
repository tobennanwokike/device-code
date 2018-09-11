package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class MediaBrowserCompatUtils {
   public static boolean areSameOptions(Bundle var0, Bundle var1) {
      boolean var2 = true;
      if(var0 != var1) {
         if(var0 == null) {
            if(var1.getInt("android.media.browse.extra.PAGE", -1) != -1 || var1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
               var2 = false;
            }
         } else if(var1 == null) {
            if(var0.getInt("android.media.browse.extra.PAGE", -1) != -1 || var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
               var2 = false;
            }
         } else if(var0.getInt("android.media.browse.extra.PAGE", -1) != var1.getInt("android.media.browse.extra.PAGE", -1) || var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) != var1.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
            var2 = false;
         }
      }

      return var2;
   }

   public static boolean hasDuplicatedItems(Bundle var0, Bundle var1) {
      boolean var7 = true;
      int var4;
      if(var0 == null) {
         var4 = -1;
      } else {
         var4 = var0.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var2;
      if(var1 == null) {
         var2 = -1;
      } else {
         var2 = var1.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var5;
      if(var0 == null) {
         var5 = -1;
      } else {
         var5 = var0.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      int var3;
      if(var1 == null) {
         var3 = -1;
      } else {
         var3 = var1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      if(var4 != -1 && var5 != -1) {
         var4 = var5 * var4;
         var5 = var4 + var5 - 1;
      } else {
         var4 = 0;
         var5 = Integer.MAX_VALUE;
      }

      if(var2 != -1 && var3 != -1) {
         int var6 = var3 * var2;
         var2 = var6 + var3 - 1;
         var3 = var6;
      } else {
         var3 = 0;
         var2 = Integer.MAX_VALUE;
      }

      if((var4 > var3 || var3 > var5) && (var4 > var2 || var2 > var5)) {
         var7 = false;
      }

      return var7;
   }
}
