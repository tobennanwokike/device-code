package com.bumptech.glide.request.animation;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.NoAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

public class ViewPropertyAnimationFactory implements GlideAnimationFactory {
   private ViewPropertyAnimation animation;
   private final ViewPropertyAnimation.Animator animator;

   public ViewPropertyAnimationFactory(ViewPropertyAnimation.Animator var1) {
      this.animator = var1;
   }

   public GlideAnimation build(boolean var1, boolean var2) {
      Object var3;
      if(!var1 && var2) {
         if(this.animation == null) {
            this.animation = new ViewPropertyAnimation(this.animator);
         }

         var3 = this.animation;
      } else {
         var3 = NoAnimation.get();
      }

      return (GlideAnimation)var3;
   }
}
