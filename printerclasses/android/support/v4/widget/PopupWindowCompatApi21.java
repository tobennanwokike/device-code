package android.support.v4.widget;

import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

class PopupWindowCompatApi21 {
   private static final String TAG = "PopupWindowCompatApi21";
   private static Field sOverlapAnchorField;

   static {
      try {
         sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
         sOverlapAnchorField.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", var1);
      }

   }

   static boolean getOverlapAnchor(PopupWindow var0) {
      boolean var1;
      if(sOverlapAnchorField != null) {
         try {
            var1 = ((Boolean)sOverlapAnchorField.get(var0)).booleanValue();
            return var1;
         } catch (IllegalAccessException var2) {
            Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", var2);
         }
      }

      var1 = false;
      return var1;
   }

   static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      if(sOverlapAnchorField != null) {
         try {
            sOverlapAnchorField.set(var0, Boolean.valueOf(var1));
         } catch (IllegalAccessException var2) {
            Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", var2);
         }
      }

   }
}
