package com.bumptech.glide.request.animation;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.bumptech.glide.request.animation.DrawableCrossFadeViewAnimation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.NoAnimation;
import com.bumptech.glide.request.animation.ViewAnimation;
import com.bumptech.glide.request.animation.ViewAnimationFactory;

public class DrawableCrossFadeFactory implements GlideAnimationFactory {
   private static final int DEFAULT_DURATION_MS = 300;
   private final ViewAnimationFactory animationFactory;
   private final int duration;
   private DrawableCrossFadeViewAnimation firstResourceAnimation;
   private DrawableCrossFadeViewAnimation secondResourceAnimation;

   public DrawableCrossFadeFactory() {
      this(300);
   }

   public DrawableCrossFadeFactory(int var1) {
      this(new ViewAnimationFactory(new DrawableCrossFadeFactory.DefaultAnimationFactory(var1)), var1);
   }

   public DrawableCrossFadeFactory(Context var1, int var2, int var3) {
      this(new ViewAnimationFactory(var1, var2), var3);
   }

   public DrawableCrossFadeFactory(Animation var1, int var2) {
      this(new ViewAnimationFactory(var1), var2);
   }

   DrawableCrossFadeFactory(ViewAnimationFactory var1, int var2) {
      this.animationFactory = var1;
      this.duration = var2;
   }

   private GlideAnimation getFirstResourceAnimation() {
      if(this.firstResourceAnimation == null) {
         this.firstResourceAnimation = new DrawableCrossFadeViewAnimation(this.animationFactory.build(false, true), this.duration);
      }

      return this.firstResourceAnimation;
   }

   private GlideAnimation getSecondResourceAnimation() {
      if(this.secondResourceAnimation == null) {
         this.secondResourceAnimation = new DrawableCrossFadeViewAnimation(this.animationFactory.build(false, false), this.duration);
      }

      return this.secondResourceAnimation;
   }

   public GlideAnimation build(boolean var1, boolean var2) {
      GlideAnimation var3;
      if(var1) {
         var3 = NoAnimation.get();
      } else if(var2) {
         var3 = this.getFirstResourceAnimation();
      } else {
         var3 = this.getSecondResourceAnimation();
      }

      return var3;
   }

   private static class DefaultAnimationFactory implements ViewAnimation.AnimationFactory {
      private final int duration;

      DefaultAnimationFactory(int var1) {
         this.duration = var1;
      }

      public Animation build() {
         AlphaAnimation var1 = new AlphaAnimation(0.0F, 1.0F);
         var1.setDuration((long)this.duration);
         return var1;
      }
   }
}
