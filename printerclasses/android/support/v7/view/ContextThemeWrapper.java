package android.support.v7.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources.Theme;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.appcompat.R;
import android.view.LayoutInflater;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class ContextThemeWrapper extends ContextWrapper {
   private LayoutInflater mInflater;
   private Theme mTheme;
   private int mThemeResource;

   public ContextThemeWrapper(Context var1, @StyleRes int var2) {
      super(var1);
      this.mThemeResource = var2;
   }

   public ContextThemeWrapper(Context var1, Theme var2) {
      super(var1);
      this.mTheme = var2;
   }

   private void initializeTheme() {
      boolean var1;
      if(this.mTheme == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      if(var1) {
         this.mTheme = this.getResources().newTheme();
         Theme var2 = this.getBaseContext().getTheme();
         if(var2 != null) {
            this.mTheme.setTo(var2);
         }
      }

      this.onApplyThemeResource(this.mTheme, this.mThemeResource, var1);
   }

   public Object getSystemService(String var1) {
      Object var2;
      if("layout_inflater".equals(var1)) {
         if(this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.getBaseContext()).cloneInContext(this);
         }

         var2 = this.mInflater;
      } else {
         var2 = this.getBaseContext().getSystemService(var1);
      }

      return var2;
   }

   public Theme getTheme() {
      Theme var1;
      if(this.mTheme != null) {
         var1 = this.mTheme;
      } else {
         if(this.mThemeResource == 0) {
            this.mThemeResource = R.style.Theme_AppCompat_Light;
         }

         this.initializeTheme();
         var1 = this.mTheme;
      }

      return var1;
   }

   public int getThemeResId() {
      return this.mThemeResource;
   }

   protected void onApplyThemeResource(Theme var1, int var2, boolean var3) {
      var1.applyStyle(var2, true);
   }

   public void setTheme(int var1) {
      if(this.mThemeResource != var1) {
         this.mThemeResource = var1;
         this.initializeTheme();
      }

   }
}
