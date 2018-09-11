package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityWindowInfo;

class AccessibilityWindowInfoCompatApi24 {
   public static Object getAnchor(Object var0) {
      return ((AccessibilityWindowInfo)var0).getAnchor();
   }

   public static CharSequence getTitle(Object var0) {
      return ((AccessibilityWindowInfo)var0).getTitle();
   }
}
