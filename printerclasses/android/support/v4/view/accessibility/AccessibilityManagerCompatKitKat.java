package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener;

class AccessibilityManagerCompatKitKat {
   public static boolean addTouchExplorationStateChangeListener(AccessibilityManager var0, Object var1) {
      return var0.addTouchExplorationStateChangeListener((TouchExplorationStateChangeListener)var1);
   }

   public static Object newTouchExplorationStateChangeListener(final AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge var0) {
      return new TouchExplorationStateChangeListener() {
         public void onTouchExplorationStateChanged(boolean var1) {
            var0.onTouchExplorationStateChanged(var1);
         }
      };
   }

   public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager var0, Object var1) {
      return var0.removeTouchExplorationStateChangeListener((TouchExplorationStateChangeListener)var1);
   }

   interface TouchExplorationStateChangeListenerBridge {
      void onTouchExplorationStateChanged(boolean var1);
   }

   public static class TouchExplorationStateChangeListenerWrapper implements TouchExplorationStateChangeListener {
      final Object mListener;
      final AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge mListenerBridge;

      public TouchExplorationStateChangeListenerWrapper(Object var1, AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge var2) {
         this.mListener = var1;
         this.mListenerBridge = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if(this != var1) {
            if(var1 != null && this.getClass() == var1.getClass()) {
               AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper var3 = (AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper)var1;
               if(this.mListener == null) {
                  if(var3.mListener != null) {
                     var2 = false;
                  }
               } else {
                  var2 = this.mListener.equals(var3.mListener);
               }
            } else {
               var2 = false;
            }
         }

         return var2;
      }

      public int hashCode() {
         int var1;
         if(this.mListener == null) {
            var1 = 0;
         } else {
            var1 = this.mListener.hashCode();
         }

         return var1;
      }

      public void onTouchExplorationStateChanged(boolean var1) {
         this.mListenerBridge.onTouchExplorationStateChanged(var1);
      }
   }
}
