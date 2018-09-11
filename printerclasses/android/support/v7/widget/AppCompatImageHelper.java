package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class AppCompatImageHelper {
   private final ImageView mView;

   public AppCompatImageHelper(ImageView var1) {
      this.mView = var1;
   }

   boolean hasOverlappingRendering() {
      Drawable var2 = this.mView.getBackground();
      boolean var1;
      if(VERSION.SDK_INT >= 21 && var2 instanceof RippleDrawable) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void loadFromAttributes(AttributeSet param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void setImageResource(int var1) {
      if(var1 != 0) {
         Drawable var2 = AppCompatResources.getDrawable(this.mView.getContext(), var1);
         if(var2 != null) {
            DrawableUtils.fixDrawable(var2);
         }

         this.mView.setImageDrawable(var2);
      } else {
         this.mView.setImageDrawable((Drawable)null);
      }

   }
}
