package com.bumptech.glide.request.animation;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;

public class DrawableCrossFadeViewAnimation implements GlideAnimation {
   private final GlideAnimation defaultAnimation;
   private final int duration;

   public DrawableCrossFadeViewAnimation(GlideAnimation var1, int var2) {
      this.defaultAnimation = var1;
      this.duration = var2;
   }

   public boolean animate(Drawable var1, GlideAnimation.ViewAdapter var2) {
      boolean var3 = true;
      Drawable var4 = var2.getCurrentDrawable();
      if(var4 != null) {
         TransitionDrawable var5 = new TransitionDrawable(new Drawable[]{var4, var1});
         var5.setCrossFadeEnabled(true);
         var5.startTransition(this.duration);
         var2.setDrawable(var5);
      } else {
         this.defaultAnimation.animate(var1, var2);
         var3 = false;
      }

      return var3;
   }
}
