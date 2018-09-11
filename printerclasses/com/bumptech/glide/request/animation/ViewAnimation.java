package com.bumptech.glide.request.animation;

import android.view.View;
import android.view.animation.Animation;
import com.bumptech.glide.request.animation.GlideAnimation;

public class ViewAnimation implements GlideAnimation {
   private final ViewAnimation.AnimationFactory animationFactory;

   ViewAnimation(ViewAnimation.AnimationFactory var1) {
      this.animationFactory = var1;
   }

   public boolean animate(Object var1, GlideAnimation.ViewAdapter var2) {
      View var3 = var2.getView();
      if(var3 != null) {
         var3.clearAnimation();
         var3.startAnimation(this.animationFactory.build());
      }

      return false;
   }

   interface AnimationFactory {
      Animation build();
   }
}
