package android.support.v4.widget;

import android.widget.EdgeEffect;

class EdgeEffectCompatLollipop {
   public static boolean onPull(Object var0, float var1, float var2) {
      ((EdgeEffect)var0).onPull(var1, var2);
      return true;
   }
}
