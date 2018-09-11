package com.bumptech.glide.request.target;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class SquaringDrawable extends GlideDrawable {
   private boolean mutated;
   private SquaringDrawable.State state;
   private GlideDrawable wrapped;

   public SquaringDrawable(GlideDrawable var1, int var2) {
      this(new SquaringDrawable.State(var1.getConstantState(), var2), var1, (Resources)null);
   }

   SquaringDrawable(SquaringDrawable.State var1, GlideDrawable var2, Resources var3) {
      this.state = var1;
      if(var2 == null) {
         if(var3 != null) {
            this.wrapped = (GlideDrawable)var1.wrapped.newDrawable(var3);
         } else {
            this.wrapped = (GlideDrawable)var1.wrapped.newDrawable();
         }
      } else {
         this.wrapped = var2;
      }

   }

   public void clearColorFilter() {
      this.wrapped.clearColorFilter();
   }

   public void draw(Canvas var1) {
      this.wrapped.draw(var1);
   }

   @TargetApi(19)
   public int getAlpha() {
      return this.wrapped.getAlpha();
   }

   @TargetApi(11)
   public Callback getCallback() {
      return this.wrapped.getCallback();
   }

   public int getChangingConfigurations() {
      return this.wrapped.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public Drawable getCurrent() {
      return this.wrapped.getCurrent();
   }

   public int getIntrinsicHeight() {
      return this.state.side;
   }

   public int getIntrinsicWidth() {
      return this.state.side;
   }

   public int getMinimumHeight() {
      return this.wrapped.getMinimumHeight();
   }

   public int getMinimumWidth() {
      return this.wrapped.getMinimumWidth();
   }

   public int getOpacity() {
      return this.wrapped.getOpacity();
   }

   public boolean getPadding(Rect var1) {
      return this.wrapped.getPadding(var1);
   }

   public void invalidateSelf() {
      super.invalidateSelf();
      this.wrapped.invalidateSelf();
   }

   public boolean isAnimated() {
      return this.wrapped.isAnimated();
   }

   public boolean isRunning() {
      return this.wrapped.isRunning();
   }

   public Drawable mutate() {
      if(!this.mutated && super.mutate() == this) {
         this.wrapped = (GlideDrawable)this.wrapped.mutate();
         this.state = new SquaringDrawable.State(this.state);
         this.mutated = true;
      }

      return this;
   }

   public void scheduleSelf(Runnable var1, long var2) {
      super.scheduleSelf(var1, var2);
      this.wrapped.scheduleSelf(var1, var2);
   }

   public void setAlpha(int var1) {
      this.wrapped.setAlpha(var1);
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.wrapped.setBounds(var1, var2, var3, var4);
   }

   public void setBounds(Rect var1) {
      super.setBounds(var1);
      this.wrapped.setBounds(var1);
   }

   public void setChangingConfigurations(int var1) {
      this.wrapped.setChangingConfigurations(var1);
   }

   public void setColorFilter(int var1, Mode var2) {
      this.wrapped.setColorFilter(var1, var2);
   }

   public void setColorFilter(ColorFilter var1) {
      this.wrapped.setColorFilter(var1);
   }

   public void setDither(boolean var1) {
      this.wrapped.setDither(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.wrapped.setFilterBitmap(var1);
   }

   public void setLoopCount(int var1) {
      this.wrapped.setLoopCount(var1);
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return this.wrapped.setVisible(var1, var2);
   }

   public void start() {
      this.wrapped.start();
   }

   public void stop() {
      this.wrapped.stop();
   }

   public void unscheduleSelf(Runnable var1) {
      super.unscheduleSelf(var1);
      this.wrapped.unscheduleSelf(var1);
   }

   static class State extends ConstantState {
      private final int side;
      private final ConstantState wrapped;

      State(ConstantState var1, int var2) {
         this.wrapped = var1;
         this.side = var2;
      }

      State(SquaringDrawable.State var1) {
         this(var1.wrapped, var1.side);
      }

      public int getChangingConfigurations() {
         return 0;
      }

      public Drawable newDrawable() {
         return this.newDrawable((Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new SquaringDrawable(this, (GlideDrawable)null, var1);
      }
   }
}
