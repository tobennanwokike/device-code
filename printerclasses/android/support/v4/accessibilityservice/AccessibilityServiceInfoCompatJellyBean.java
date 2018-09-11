package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;

class AccessibilityServiceInfoCompatJellyBean {
   public static String loadDescription(AccessibilityServiceInfo var0, PackageManager var1) {
      return var0.loadDescription(var1);
   }
}
