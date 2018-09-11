package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityEvent;

class AccessibilityEventCompatJellyBean {
   public static int getAction(AccessibilityEvent var0) {
      return var0.getAction();
   }

   public static int getMovementGranularity(AccessibilityEvent var0) {
      return var0.getMovementGranularity();
   }

   public static void setAction(AccessibilityEvent var0, int var1) {
      var0.setAction(var1);
   }

   public static void setMovementGranularity(AccessibilityEvent var0, int var1) {
      var0.setMovementGranularity(var1);
   }
}
