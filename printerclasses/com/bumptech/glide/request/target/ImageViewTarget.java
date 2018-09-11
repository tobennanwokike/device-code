package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

public abstract class ImageViewTarget extends ViewTarget implements GlideAnimation.ViewAdapter {
   public ImageViewTarget(ImageView var1) {
      super(var1);
   }

   public Drawable getCurrentDrawable() {
      return ((ImageView)this.view).getDrawable();
   }

   public void onLoadCleared(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }

   public void onLoadFailed(Exception var1, Drawable var2) {
      ((ImageView)this.view).setImageDrawable(var2);
   }

   public void onLoadStarted(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }

   public void onResourceReady(Object var1, GlideAnimation var2) {
      if(var2 == null || !var2.animate(var1, this)) {
         this.setResource(var1);
      }

   }

   public void setDrawable(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }

   protected abstract void setResource(Object var1);
}
