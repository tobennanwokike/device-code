package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatColorStateListInflater;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.TypedValue;
import java.util.WeakHashMap;

public final class AppCompatResources {
   private static final String LOG_TAG = "AppCompatResources";
   private static final ThreadLocal TL_TYPED_VALUE = new ThreadLocal();
   private static final Object sColorStateCacheLock = new Object();
   private static final WeakHashMap sColorStateCaches = new WeakHashMap(0);

   private static void addColorStateListToCache(@NonNull Context param0, @ColorRes int param1, @NonNull ColorStateList param2) {
      // $FF: Couldn't be decompiled
   }

   @Nullable
   private static ColorStateList getCachedColorStateList(@NonNull Context param0, @ColorRes int param1) {
      // $FF: Couldn't be decompiled
   }

   public static ColorStateList getColorStateList(@NonNull Context var0, @ColorRes int var1) {
      ColorStateList var2;
      if(VERSION.SDK_INT >= 23) {
         var2 = var0.getColorStateList(var1);
      } else {
         ColorStateList var3 = getCachedColorStateList(var0, var1);
         var2 = var3;
         if(var3 == null) {
            var2 = inflateColorStateList(var0, var1);
            if(var2 != null) {
               addColorStateListToCache(var0, var1, var2);
            } else {
               var2 = ContextCompat.getColorStateList(var0, var1);
            }
         }
      }

      return var2;
   }

   @Nullable
   public static Drawable getDrawable(@NonNull Context var0, @DrawableRes int var1) {
      return AppCompatDrawableManager.get().getDrawable(var0, var1);
   }

   @NonNull
   private static TypedValue getTypedValue() {
      TypedValue var1 = (TypedValue)TL_TYPED_VALUE.get();
      TypedValue var0 = var1;
      if(var1 == null) {
         var0 = new TypedValue();
         TL_TYPED_VALUE.set(var0);
      }

      return var0;
   }

   @Nullable
   private static ColorStateList inflateColorStateList(Context var0, int var1) {
      Object var2 = null;
      ColorStateList var6;
      if(isColorInt(var0, var1)) {
         var6 = (ColorStateList)var2;
      } else {
         Resources var3 = var0.getResources();
         XmlResourceParser var4 = var3.getXml(var1);

         try {
            var6 = AppCompatColorStateListInflater.createFromXml(var3, var4, var0.getTheme());
         } catch (Exception var5) {
            Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", var5);
            var6 = (ColorStateList)var2;
         }
      }

      return var6;
   }

   private static boolean isColorInt(@NonNull Context var0, @ColorRes int var1) {
      boolean var2 = true;
      Resources var3 = var0.getResources();
      TypedValue var4 = getTypedValue();
      var3.getValue(var1, var4, true);
      if(var4.type < 28 || var4.type > 31) {
         var2 = false;
      }

      return var2;
   }

   private static class ColorStateListCacheEntry {
      final Configuration configuration;
      final ColorStateList value;

      ColorStateListCacheEntry(@NonNull ColorStateList var1, @NonNull Configuration var2) {
         this.value = var1;
         this.configuration = var2;
      }
   }
}
