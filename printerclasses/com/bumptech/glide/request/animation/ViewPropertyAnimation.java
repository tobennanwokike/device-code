package com.bumptech.glide.request.animation;

import android.view.View;
import com.bumptech.glide.request.animation.GlideAnimation;

public class ViewPropertyAnimation implements GlideAnimation {
   private final ViewPropertyAnimation.Animator animator;

   public ViewPropertyAnimation(ViewPropertyAnimation.Animator var1) {
      this.animator = var1;
   }

   public boolean animate(Object var1, GlideAnimation.ViewAdapter var2) {
      if(var2.getView() != null) {
         this.animator.animate(var2.getView());
      }

      return false;
   }

   public interface Animator {
      void animate(View var1);
   }
}
