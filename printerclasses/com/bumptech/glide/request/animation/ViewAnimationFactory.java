package com.bumptech.glide.request.animation;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.NoAnimation;
import com.bumptech.glide.request.animation.ViewAnimation;

public class ViewAnimationFactory implements GlideAnimationFactory {
   private final ViewAnimation.AnimationFactory animationFactory;
   private GlideAnimation glideAnimation;

   public ViewAnimationFactory(Context var1, int var2) {
      this((ViewAnimation.AnimationFactory)(new ViewAnimationFactory.ResourceAnimationFactory(var1, var2)));
   }

   public ViewAnimationFactory(Animation var1) {
      this((ViewAnimation.AnimationFactory)(new ViewAnimationFactory.ConcreteAnimationFactory(var1)));
   }

   ViewAnimationFactory(ViewAnimation.AnimationFactory var1) {
      this.animationFactory = var1;
   }

   public GlideAnimation build(boolean var1, boolean var2) {
      GlideAnimation var3;
      if(!var1 && var2) {
         if(this.glideAnimation == null) {
            this.glideAnimation = new ViewAnimation(this.animationFactory);
         }

         var3 = this.glideAnimation;
      } else {
         var3 = NoAnimation.get();
      }

      return var3;
   }

   private static class ConcreteAnimationFactory implements ViewAnimation.AnimationFactory {
      private final Animation animation;

      public ConcreteAnimationFactory(Animation var1) {
         this.animation = var1;
      }

      public Animation build() {
         return this.animation;
      }
   }

   private static class ResourceAnimationFactory implements ViewAnimation.AnimationFactory {
      private final int animationId;
      private final Context context;

      public ResourceAnimationFactory(Context var1, int var2) {
         this.context = var1.getApplicationContext();
         this.animationId = var2;
      }

      public Animation build() {
         return AnimationUtils.loadAnimation(this.context, this.animationId);
      }
   }
}
