package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Resource;

public abstract class DrawableResource implements Resource {
   protected final Drawable drawable;

   public DrawableResource(Drawable var1) {
      if(var1 == null) {
         throw new NullPointerException("Drawable must not be null!");
      } else {
         this.drawable = var1;
      }
   }

   public final Drawable get() {
      return this.drawable.getConstantState().newDrawable();
   }
}
