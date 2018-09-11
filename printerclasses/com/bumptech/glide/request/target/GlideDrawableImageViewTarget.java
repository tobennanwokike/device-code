package com.bumptech.glide.request.target;

import android.widget.ImageView;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SquaringDrawable;

public class GlideDrawableImageViewTarget extends ImageViewTarget {
   private static final float SQUARE_RATIO_MARGIN = 0.05F;
   private int maxLoopCount;
   private GlideDrawable resource;

   public GlideDrawableImageViewTarget(ImageView var1) {
      this(var1, -1);
   }

   public GlideDrawableImageViewTarget(ImageView var1, int var2) {
      super(var1);
      this.maxLoopCount = var2;
   }

   public void onResourceReady(GlideDrawable var1, GlideAnimation var2) {
      Object var5 = var1;
      if(!var1.isAnimated()) {
         float var4 = (float)((ImageView)this.view).getWidth() / (float)((ImageView)this.view).getHeight();
         float var3 = (float)var1.getIntrinsicWidth() / (float)var1.getIntrinsicHeight();
         var5 = var1;
         if(Math.abs(var4 - 1.0F) <= 0.05F) {
            var5 = var1;
            if(Math.abs(var3 - 1.0F) <= 0.05F) {
               var5 = new SquaringDrawable(var1, ((ImageView)this.view).getWidth());
            }
         }
      }

      super.onResourceReady(var5, var2);
      this.resource = (GlideDrawable)var5;
      ((GlideDrawable)var5).setLoopCount(this.maxLoopCount);
      ((GlideDrawable)var5).start();
   }

   public void onStart() {
      if(this.resource != null) {
         this.resource.start();
      }

   }

   public void onStop() {
      if(this.resource != null) {
         this.resource.stop();
      }

   }

   protected void setResource(GlideDrawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }
}
