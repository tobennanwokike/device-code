package com.bumptech.glide.request.animation;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;

public class NoAnimation implements GlideAnimation {
   private static final NoAnimation NO_ANIMATION = new NoAnimation();
   private static final GlideAnimationFactory NO_ANIMATION_FACTORY = new NoAnimation.NoAnimationFactory();

   public static GlideAnimation get() {
      return NO_ANIMATION;
   }

   public static GlideAnimationFactory getFactory() {
      return NO_ANIMATION_FACTORY;
   }

   public boolean animate(Object var1, GlideAnimation.ViewAdapter var2) {
      return false;
   }

   public static class NoAnimationFactory implements GlideAnimationFactory {
      public GlideAnimation build(boolean var1, boolean var2) {
         return NoAnimation.NO_ANIMATION;
      }
   }
}
