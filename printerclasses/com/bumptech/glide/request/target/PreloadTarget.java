package com.bumptech.glide.request.target;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

public final class PreloadTarget extends SimpleTarget {
   private PreloadTarget(int var1, int var2) {
      super(var1, var2);
   }

   public static PreloadTarget obtain(int var0, int var1) {
      return new PreloadTarget(var0, var1);
   }

   public void onResourceReady(Object var1, GlideAnimation var2) {
      Glide.clear((Target)this);
   }
}
