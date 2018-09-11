package android.support.v7.widget;

import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.ThemeUtils;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class DrawableUtils {
   public static final Rect INSETS_NONE = new Rect();
   private static final String TAG = "DrawableUtils";
   private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
   private static Class sInsetsClazz;

   static {
      if(VERSION.SDK_INT >= 18) {
         try {
            sInsetsClazz = Class.forName("android.graphics.Insets");
         } catch (ClassNotFoundException var1) {
            ;
         }
      }

   }

   public static boolean canSafelyMutateDrawable(@NonNull Drawable var0) {
      boolean var4 = false;
      boolean var3;
      if(VERSION.SDK_INT < 15 && var0 instanceof InsetDrawable) {
         var3 = var4;
      } else {
         if(VERSION.SDK_INT < 15) {
            var3 = var4;
            if(var0 instanceof GradientDrawable) {
               return var3;
            }
         }

         if(VERSION.SDK_INT < 17) {
            var3 = var4;
            if(var0 instanceof LayerDrawable) {
               return var3;
            }
         }

         if(var0 instanceof DrawableContainer) {
            ConstantState var5 = var0.getConstantState();
            if(var5 instanceof DrawableContainerState) {
               Drawable[] var6 = ((DrawableContainerState)var5).getChildren();
               int var2 = var6.length;

               for(int var1 = 0; var1 < var2; ++var1) {
                  var3 = var4;
                  if(!canSafelyMutateDrawable(var6[var1])) {
                     return var3;
                  }
               }
            }
         } else {
            if(var0 instanceof DrawableWrapper) {
               var3 = canSafelyMutateDrawable(((DrawableWrapper)var0).getWrappedDrawable());
               return var3;
            }

            if(var0 instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
               var3 = canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper)var0).getWrappedDrawable());
               return var3;
            }

            if(var0 instanceof ScaleDrawable) {
               var3 = canSafelyMutateDrawable(((ScaleDrawable)var0).getDrawable());
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   static void fixDrawable(@NonNull Drawable var0) {
      if(VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         fixVectorDrawableTinting(var0);
      }

   }

   private static void fixVectorDrawableTinting(Drawable var0) {
      int[] var1 = var0.getState();
      if(var1 != null && var1.length != 0) {
         var0.setState(ThemeUtils.EMPTY_STATE_SET);
      } else {
         var0.setState(ThemeUtils.CHECKED_STATE_SET);
      }

      var0.setState(var1);
   }

   public static Rect getOpticalBounds(Drawable param0) {
      // $FF: Couldn't be decompiled
   }

   static Mode parseTintMode(int var0, Mode var1) {
      Mode var2 = var1;
      switch(var0) {
      case 3:
         var2 = Mode.SRC_OVER;
      case 4:
      case 6:
      case 7:
      case 8:
      case 10:
      case 11:
      case 12:
      case 13:
         break;
      case 5:
         var2 = Mode.SRC_IN;
         break;
      case 9:
         var2 = Mode.SRC_ATOP;
         break;
      case 14:
         var2 = Mode.MULTIPLY;
         break;
      case 15:
         var2 = Mode.SCREEN;
         break;
      case 16:
         var2 = var1;
         if(VERSION.SDK_INT >= 11) {
            var2 = Mode.valueOf("ADD");
         }
         break;
      default:
         var2 = var1;
      }

      return var2;
   }
}
