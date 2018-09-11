package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.PointerIcon;

class PointerIconCompatApi24 {
   public static Object create(Bitmap var0, float var1, float var2) {
      return PointerIcon.create(var0, var1, var2);
   }

   public static Object getSystemIcon(Context var0, int var1) {
      return PointerIcon.getSystemIcon(var0, var1);
   }

   public static Object load(Resources var0, int var1) {
      return PointerIcon.load(var0, var1);
   }
}
