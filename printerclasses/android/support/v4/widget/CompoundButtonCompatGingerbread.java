package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TintableCompoundButton;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

class CompoundButtonCompatGingerbread {
   private static final String TAG = "CompoundButtonCompatGingerbread";
   private static Field sButtonDrawableField;
   private static boolean sButtonDrawableFieldFetched;

   static Drawable getButtonDrawable(CompoundButton var0) {
      if(!sButtonDrawableFieldFetched) {
         try {
            sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
            sButtonDrawableField.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            Log.i("CompoundButtonCompatGingerbread", "Failed to retrieve mButtonDrawable field", var2);
         }

         sButtonDrawableFieldFetched = true;
      }

      Drawable var4;
      if(sButtonDrawableField != null) {
         try {
            var4 = (Drawable)sButtonDrawableField.get(var0);
            return var4;
         } catch (IllegalAccessException var3) {
            Log.i("CompoundButtonCompatGingerbread", "Failed to get button drawable via reflection", var3);
            sButtonDrawableField = null;
         }
      }

      var4 = null;
      return var4;
   }

   static ColorStateList getButtonTintList(CompoundButton var0) {
      ColorStateList var1;
      if(var0 instanceof TintableCompoundButton) {
         var1 = ((TintableCompoundButton)var0).getSupportButtonTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   static Mode getButtonTintMode(CompoundButton var0) {
      Mode var1;
      if(var0 instanceof TintableCompoundButton) {
         var1 = ((TintableCompoundButton)var0).getSupportButtonTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   static void setButtonTintList(CompoundButton var0, ColorStateList var1) {
      if(var0 instanceof TintableCompoundButton) {
         ((TintableCompoundButton)var0).setSupportButtonTintList(var1);
      }

   }

   static void setButtonTintMode(CompoundButton var0, Mode var1) {
      if(var0 instanceof TintableCompoundButton) {
         ((TintableCompoundButton)var0).setSupportButtonTintMode(var1);
      }

   }
}
