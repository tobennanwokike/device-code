package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.TintResources;
import android.support.v7.widget.VectorEnabledTintResources;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class TintContextWrapper extends ContextWrapper {
   private static final Object CACHE_LOCK = new Object();
   private static ArrayList sCache;
   private final Resources mResources;
   private final Theme mTheme;

   private TintContextWrapper(@NonNull Context var1) {
      super(var1);
      if(VectorEnabledTintResources.shouldBeUsed()) {
         this.mResources = new VectorEnabledTintResources(this, var1.getResources());
         this.mTheme = this.mResources.newTheme();
         this.mTheme.setTo(var1.getTheme());
      } else {
         this.mResources = new TintResources(this, var1.getResources());
         this.mTheme = null;
      }

   }

   private static boolean shouldWrap(@NonNull Context var0) {
      boolean var2 = false;
      boolean var1 = var2;
      if(!(var0 instanceof TintContextWrapper)) {
         var1 = var2;
         if(!(var0.getResources() instanceof TintResources)) {
            if(var0.getResources() instanceof VectorEnabledTintResources) {
               var1 = var2;
            } else {
               if(VERSION.SDK_INT >= 21) {
                  var1 = var2;
                  if(!VectorEnabledTintResources.shouldBeUsed()) {
                     return var1;
                  }
               }

               var1 = true;
            }
         }
      }

      return var1;
   }

   public static Context wrap(@NonNull Context param0) {
      // $FF: Couldn't be decompiled
   }

   public Resources getResources() {
      return this.mResources;
   }

   public Theme getTheme() {
      Theme var1;
      if(this.mTheme == null) {
         var1 = super.getTheme();
      } else {
         var1 = this.mTheme;
      }

      return var1;
   }

   public void setTheme(int var1) {
      if(this.mTheme == null) {
         super.setTheme(var1);
      } else {
         this.mTheme.applyStyle(var1, true);
      }

   }
}
