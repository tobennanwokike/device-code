package com.bumptech.glide.request.animation;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface GlideAnimation {
   boolean animate(Object var1, GlideAnimation.ViewAdapter var2);

   public interface ViewAdapter {
      Drawable getCurrentDrawable();

      View getView();

      void setDrawable(Drawable var1);
   }
}
