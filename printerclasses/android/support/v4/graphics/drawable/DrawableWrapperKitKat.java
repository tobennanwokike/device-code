package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableWrapperGingerbread;
import android.support.v4.graphics.drawable.DrawableWrapperHoneycomb;

class DrawableWrapperKitKat extends DrawableWrapperHoneycomb {
   DrawableWrapperKitKat(Drawable var1) {
      super(var1);
   }

   DrawableWrapperKitKat(DrawableWrapperGingerbread.DrawableWrapperState var1, Resources var2) {
      super(var1, var2);
   }

   public boolean isAutoMirrored() {
      return this.mDrawable.isAutoMirrored();
   }

   @NonNull
   DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
      return new DrawableWrapperKitKat.DrawableWrapperStateKitKat(this.mState, (Resources)null);
   }

   public void setAutoMirrored(boolean var1) {
      this.mDrawable.setAutoMirrored(var1);
   }

   private static class DrawableWrapperStateKitKat extends DrawableWrapperGingerbread.DrawableWrapperState {
      DrawableWrapperStateKitKat(@Nullable DrawableWrapperGingerbread.DrawableWrapperState var1, @Nullable Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(@Nullable Resources var1) {
         return new DrawableWrapperKitKat(this, var1);
      }
   }
}
