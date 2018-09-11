package android.support.v4.view;

import android.view.View;

class ViewPropertyAnimatorCompatLollipop {
   public static void translationZ(View var0, float var1) {
      var0.animate().translationZ(var1);
   }

   public static void translationZBy(View var0, float var1) {
      var0.animate().translationZBy(var1);
   }

   public static void z(View var0, float var1) {
      var0.animate().z(var1);
   }

   public static void zBy(View var0, float var1) {
      var0.animate().zBy(var1);
   }
}
