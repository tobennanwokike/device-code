package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.FocusStrategy;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
   private static final String DEFAULT_CLASS_NAME = "android.view.View";
   public static final int HOST_ID = -1;
   public static final int INVALID_ID = Integer.MIN_VALUE;
   private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
   private static final FocusStrategy.BoundsAdapter NODE_ADAPTER = new FocusStrategy.BoundsAdapter() {
      public void obtainBounds(AccessibilityNodeInfoCompat var1, Rect var2) {
         var1.getBoundsInParent(var2);
      }
   };
   private static final FocusStrategy.CollectionAdapter SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter() {
      public AccessibilityNodeInfoCompat get(SparseArrayCompat var1, int var2) {
         return (AccessibilityNodeInfoCompat)var1.valueAt(var2);
      }

      public int size(SparseArrayCompat var1) {
         return var1.size();
      }
   };
   private int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
   private final View mHost;
   private int mHoveredVirtualViewId = Integer.MIN_VALUE;
   private int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
   private final AccessibilityManager mManager;
   private ExploreByTouchHelper.MyNodeProvider mNodeProvider;
   private final int[] mTempGlobalRect = new int[2];
   private final Rect mTempParentRect = new Rect();
   private final Rect mTempScreenRect = new Rect();
   private final Rect mTempVisibleRect = new Rect();

   public ExploreByTouchHelper(View var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("View may not be null");
      } else {
         this.mHost = var1;
         this.mManager = (AccessibilityManager)var1.getContext().getSystemService("accessibility");
         var1.setFocusable(true);
         if(ViewCompat.getImportantForAccessibility(var1) == 0) {
            ViewCompat.setImportantForAccessibility(var1, 1);
         }

      }
   }

   private boolean clearAccessibilityFocus(int var1) {
      boolean var2;
      if(this.mAccessibilityFocusedVirtualViewId == var1) {
         this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
         this.mHost.invalidate();
         this.sendEventForVirtualView(var1, 65536);
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean clickKeyboardFocusedVirtualView() {
      boolean var1;
      if(this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE && this.onPerformActionForVirtualView(this.mKeyboardFocusedVirtualViewId, 16, (Bundle)null)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private AccessibilityEvent createEvent(int var1, int var2) {
      AccessibilityEvent var3;
      switch(var1) {
      case -1:
         var3 = this.createEventForHost(var2);
         break;
      default:
         var3 = this.createEventForChild(var1, var2);
      }

      return var3;
   }

   private AccessibilityEvent createEventForChild(int var1, int var2) {
      AccessibilityEvent var5 = AccessibilityEvent.obtain(var2);
      AccessibilityRecordCompat var3 = AccessibilityEventCompat.asRecord(var5);
      AccessibilityNodeInfoCompat var4 = this.obtainAccessibilityNodeInfo(var1);
      var3.getText().add(var4.getText());
      var3.setContentDescription(var4.getContentDescription());
      var3.setScrollable(var4.isScrollable());
      var3.setPassword(var4.isPassword());
      var3.setEnabled(var4.isEnabled());
      var3.setChecked(var4.isChecked());
      this.onPopulateEventForVirtualView(var1, var5);
      if(var5.getText().isEmpty() && var5.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
      } else {
         var3.setClassName(var4.getClassName());
         var3.setSource(this.mHost, var1);
         var5.setPackageName(this.mHost.getContext().getPackageName());
         return var5;
      }
   }

   private AccessibilityEvent createEventForHost(int var1) {
      AccessibilityEvent var2 = AccessibilityEvent.obtain(var1);
      ViewCompat.onInitializeAccessibilityEvent(this.mHost, var2);
      return var2;
   }

   @NonNull
   private AccessibilityNodeInfoCompat createNodeForChild(int var1) {
      AccessibilityNodeInfoCompat var4 = AccessibilityNodeInfoCompat.obtain();
      var4.setEnabled(true);
      var4.setFocusable(true);
      var4.setClassName("android.view.View");
      var4.setBoundsInParent(INVALID_PARENT_BOUNDS);
      var4.setBoundsInScreen(INVALID_PARENT_BOUNDS);
      this.onPopulateNodeForVirtualView(var1, var4);
      if(var4.getText() == null && var4.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
      } else {
         var4.getBoundsInParent(this.mTempParentRect);
         if(this.mTempParentRect.equals(INVALID_PARENT_BOUNDS)) {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
         } else {
            int var2 = var4.getActions();
            if((var2 & 64) != 0) {
               throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else if((var2 & 128) != 0) {
               throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else {
               var4.setPackageName(this.mHost.getContext().getPackageName());
               var4.setSource(this.mHost, var1);
               var4.setParent(this.mHost);
               if(this.mAccessibilityFocusedVirtualViewId == var1) {
                  var4.setAccessibilityFocused(true);
                  var4.addAction(128);
               } else {
                  var4.setAccessibilityFocused(false);
                  var4.addAction(64);
               }

               boolean var3;
               if(this.mKeyboardFocusedVirtualViewId == var1) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               if(var3) {
                  var4.addAction(2);
               } else if(var4.isFocusable()) {
                  var4.addAction(1);
               }

               var4.setFocused(var3);
               if(this.intersectVisibleToUser(this.mTempParentRect)) {
                  var4.setVisibleToUser(true);
                  var4.setBoundsInParent(this.mTempParentRect);
               }

               var4.getBoundsInScreen(this.mTempScreenRect);
               if(this.mTempScreenRect.equals(INVALID_PARENT_BOUNDS)) {
                  this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                  var4.getBoundsInParent(this.mTempScreenRect);
                  this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                  var4.setBoundsInScreen(this.mTempScreenRect);
               }

               return var4;
            }
         }
      }
   }

   @NonNull
   private AccessibilityNodeInfoCompat createNodeForHost() {
      AccessibilityNodeInfoCompat var4 = AccessibilityNodeInfoCompat.obtain(this.mHost);
      ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, var4);
      ArrayList var3 = new ArrayList();
      this.getVisibleVirtualViews(var3);
      if(var4.getChildCount() > 0 && var3.size() > 0) {
         throw new RuntimeException("Views cannot have both real and virtual children");
      } else {
         int var1 = 0;

         for(int var2 = var3.size(); var1 < var2; ++var1) {
            var4.addChild(this.mHost, ((Integer)var3.get(var1)).intValue());
         }

         return var4;
      }
   }

   private SparseArrayCompat getAllNodes() {
      ArrayList var3 = new ArrayList();
      this.getVisibleVirtualViews(var3);
      SparseArrayCompat var2 = new SparseArrayCompat();

      for(int var1 = 0; var1 < var3.size(); ++var1) {
         var2.put(var1, this.createNodeForChild(var1));
      }

      return var2;
   }

   private void getBoundsInParent(int var1, Rect var2) {
      this.obtainAccessibilityNodeInfo(var1).getBoundsInParent(var2);
   }

   private static Rect guessPreviouslyFocusedRect(@NonNull View var0, int var1, @NonNull Rect var2) {
      int var4 = var0.getWidth();
      int var3 = var0.getHeight();
      switch(var1) {
      case 17:
         var2.set(var4, 0, var4, var3);
         break;
      case 33:
         var2.set(0, var3, var4, var3);
         break;
      case 66:
         var2.set(-1, 0, -1, var3);
         break;
      case 130:
         var2.set(0, -1, var4, -1);
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var2;
   }

   private boolean intersectVisibleToUser(Rect var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if(var1 != null) {
         if(var1.isEmpty()) {
            var2 = var3;
         } else {
            var2 = var3;
            if(this.mHost.getWindowVisibility() == 0) {
               ViewParent var4 = this.mHost.getParent();

               while(true) {
                  if(!(var4 instanceof View)) {
                     var2 = var3;
                     if(var4 != null) {
                        var2 = var3;
                        if(this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                           var2 = var1.intersect(this.mTempVisibleRect);
                        }
                     }
                     break;
                  }

                  View var5 = (View)var4;
                  var2 = var3;
                  if(ViewCompat.getAlpha(var5) <= 0.0F) {
                     break;
                  }

                  var2 = var3;
                  if(var5.getVisibility() != 0) {
                     break;
                  }

                  var4 = var5.getParent();
               }
            }
         }
      }

      return var2;
   }

   private static int keyToDirection(int var0) {
      short var1;
      switch(var0) {
      case 19:
         var1 = 33;
         break;
      case 20:
      default:
         var1 = 130;
         break;
      case 21:
         var1 = 17;
         break;
      case 22:
         var1 = 66;
      }

      return var1;
   }

   private boolean moveFocus(int var1, @Nullable Rect var2) {
      SparseArrayCompat var6 = this.getAllNodes();
      int var3 = this.mKeyboardFocusedVirtualViewId;
      AccessibilityNodeInfoCompat var5;
      if(var3 == Integer.MIN_VALUE) {
         var5 = null;
      } else {
         var5 = (AccessibilityNodeInfoCompat)var6.get(var3);
      }

      AccessibilityNodeInfoCompat var8;
      switch(var1) {
      case 1:
      case 2:
         boolean var4;
         if(ViewCompat.getLayoutDirection(this.mHost) == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInRelativeDirection(var6, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var1, var4, false);
         break;
      case 17:
      case 33:
      case 66:
      case 130:
         Rect var7 = new Rect();
         if(this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
            this.getBoundsInParent(this.mKeyboardFocusedVirtualViewId, var7);
         } else if(var2 != null) {
            var7.set(var2);
         } else {
            guessPreviouslyFocusedRect(this.mHost, var1, var7);
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInAbsoluteDirection(var6, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var7, var1);
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      if(var8 == null) {
         var1 = Integer.MIN_VALUE;
      } else {
         var1 = var6.keyAt(var6.indexOfValue(var8));
      }

      return this.requestKeyboardFocusForVirtualView(var1);
   }

   private boolean performActionForChild(int var1, int var2, Bundle var3) {
      boolean var4;
      switch(var2) {
      case 1:
         var4 = this.requestKeyboardFocusForVirtualView(var1);
         break;
      case 2:
         var4 = this.clearKeyboardFocusForVirtualView(var1);
         break;
      case 64:
         var4 = this.requestAccessibilityFocus(var1);
         break;
      case 128:
         var4 = this.clearAccessibilityFocus(var1);
         break;
      default:
         var4 = this.onPerformActionForVirtualView(var1, var2, var3);
      }

      return var4;
   }

   private boolean performActionForHost(int var1, Bundle var2) {
      return ViewCompat.performAccessibilityAction(this.mHost, var1, var2);
   }

   private boolean requestAccessibilityFocus(int var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if(this.mManager.isEnabled()) {
         if(!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            var2 = var3;
         } else {
            var2 = var3;
            if(this.mAccessibilityFocusedVirtualViewId != var1) {
               if(this.mAccessibilityFocusedVirtualViewId != Integer.MIN_VALUE) {
                  this.clearAccessibilityFocus(this.mAccessibilityFocusedVirtualViewId);
               }

               this.mAccessibilityFocusedVirtualViewId = var1;
               this.mHost.invalidate();
               this.sendEventForVirtualView(var1, '耀');
               var2 = true;
            }
         }
      }

      return var2;
   }

   private void updateHoveredVirtualView(int var1) {
      if(this.mHoveredVirtualViewId != var1) {
         int var2 = this.mHoveredVirtualViewId;
         this.mHoveredVirtualViewId = var1;
         this.sendEventForVirtualView(var1, 128);
         this.sendEventForVirtualView(var2, 256);
      }

   }

   public final boolean clearKeyboardFocusForVirtualView(int var1) {
      boolean var2 = false;
      if(this.mKeyboardFocusedVirtualViewId == var1) {
         this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
         this.onVirtualViewKeyboardFocusChanged(var1, false);
         this.sendEventForVirtualView(var1, 8);
         var2 = true;
      }

      return var2;
   }

   public final boolean dispatchHoverEvent(@NonNull MotionEvent var1) {
      boolean var4 = true;
      boolean var5 = false;
      boolean var3 = var5;
      if(this.mManager.isEnabled()) {
         if(!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            var3 = var5;
         } else {
            switch(var1.getAction()) {
            case 7:
            case 9:
               int var2 = this.getVirtualViewAt(var1.getX(), var1.getY());
               this.updateHoveredVirtualView(var2);
               if(var2 != Integer.MIN_VALUE) {
                  var3 = var4;
               } else {
                  var3 = false;
               }
               break;
            case 8:
            default:
               var3 = var5;
               break;
            case 10:
               var3 = var5;
               if(this.mAccessibilityFocusedVirtualViewId != Integer.MIN_VALUE) {
                  this.updateHoveredVirtualView(Integer.MIN_VALUE);
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   public final boolean dispatchKeyEvent(@NonNull KeyEvent var1) {
      boolean var7 = false;
      boolean var6 = false;
      boolean var5 = var6;
      if(var1.getAction() != 1) {
         int var2 = var1.getKeyCode();
         switch(var2) {
         case 19:
         case 20:
         case 21:
         case 22:
            var5 = var6;
            if(KeyEventCompat.hasNoModifiers(var1)) {
               int var3 = keyToDirection(var2);
               int var4 = var1.getRepeatCount();
               var2 = 0;
               var6 = var7;

               while(true) {
                  var5 = var6;
                  if(var2 >= var4 + 1) {
                     break;
                  }

                  var5 = var6;
                  if(!this.moveFocus(var3, (Rect)null)) {
                     break;
                  }

                  var6 = true;
                  ++var2;
               }
            }
            break;
         case 23:
         case 66:
            var5 = var6;
            if(KeyEventCompat.hasNoModifiers(var1)) {
               var5 = var6;
               if(var1.getRepeatCount() == 0) {
                  this.clickKeyboardFocusedVirtualView();
                  var5 = true;
               }
            }
            break;
         case 61:
            if(KeyEventCompat.hasNoModifiers(var1)) {
               var5 = this.moveFocus(2, (Rect)null);
            } else {
               var5 = var6;
               if(KeyEventCompat.hasModifiers(var1, 1)) {
                  var5 = this.moveFocus(1, (Rect)null);
               }
            }
            break;
         default:
            var5 = var6;
         }
      }

      return var5;
   }

   public final int getAccessibilityFocusedVirtualViewId() {
      return this.mAccessibilityFocusedVirtualViewId;
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      if(this.mNodeProvider == null) {
         this.mNodeProvider = new ExploreByTouchHelper.MyNodeProvider();
      }

      return this.mNodeProvider;
   }

   @Deprecated
   public int getFocusedVirtualView() {
      return this.getAccessibilityFocusedVirtualViewId();
   }

   public final int getKeyboardFocusedVirtualViewId() {
      return this.mKeyboardFocusedVirtualViewId;
   }

   protected abstract int getVirtualViewAt(float var1, float var2);

   protected abstract void getVisibleVirtualViews(List var1);

   public final void invalidateRoot() {
      this.invalidateVirtualView(-1, 1);
   }

   public final void invalidateVirtualView(int var1) {
      this.invalidateVirtualView(var1, 0);
   }

   public final void invalidateVirtualView(int var1, int var2) {
      if(var1 != Integer.MIN_VALUE && this.mManager.isEnabled()) {
         ViewParent var3 = this.mHost.getParent();
         if(var3 != null) {
            AccessibilityEvent var4 = this.createEvent(var1, 2048);
            AccessibilityEventCompat.setContentChangeTypes(var4, var2);
            ViewParentCompat.requestSendAccessibilityEvent(var3, this.mHost, var4);
         }
      }

   }

   @NonNull
   AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int var1) {
      AccessibilityNodeInfoCompat var2;
      if(var1 == -1) {
         var2 = this.createNodeForHost();
      } else {
         var2 = this.createNodeForChild(var1);
      }

      return var2;
   }

   public final void onFocusChanged(boolean var1, int var2, @Nullable Rect var3) {
      if(this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
         this.clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
      }

      if(var1) {
         this.moveFocus(var2, var3);
      }

   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      super.onInitializeAccessibilityEvent(var1, var2);
      this.onPopulateEventForHost(var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      super.onInitializeAccessibilityNodeInfo(var1, var2);
      this.onPopulateNodeForHost(var2);
   }

   protected abstract boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3);

   protected void onPopulateEventForHost(AccessibilityEvent var1) {
   }

   protected void onPopulateEventForVirtualView(int var1, AccessibilityEvent var2) {
   }

   protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat var1) {
   }

   protected abstract void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2);

   protected void onVirtualViewKeyboardFocusChanged(int var1, boolean var2) {
   }

   boolean performAction(int var1, int var2, Bundle var3) {
      boolean var4;
      switch(var1) {
      case -1:
         var4 = this.performActionForHost(var2, var3);
         break;
      default:
         var4 = this.performActionForChild(var1, var2, var3);
      }

      return var4;
   }

   public final boolean requestKeyboardFocusForVirtualView(int var1) {
      boolean var2 = false;
      if((this.mHost.isFocused() || this.mHost.requestFocus()) && this.mKeyboardFocusedVirtualViewId != var1) {
         if(this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
         }

         this.mKeyboardFocusedVirtualViewId = var1;
         this.onVirtualViewKeyboardFocusChanged(var1, true);
         this.sendEventForVirtualView(var1, 8);
         var2 = true;
      }

      return var2;
   }

   public final boolean sendEventForVirtualView(int var1, int var2) {
      boolean var4 = false;
      boolean var3 = var4;
      if(var1 != Integer.MIN_VALUE) {
         if(!this.mManager.isEnabled()) {
            var3 = var4;
         } else {
            ViewParent var5 = this.mHost.getParent();
            var3 = var4;
            if(var5 != null) {
               AccessibilityEvent var6 = this.createEvent(var1, var2);
               var3 = ViewParentCompat.requestSendAccessibilityEvent(var5, this.mHost, var6);
            }
         }
      }

      return var3;
   }

   private class MyNodeProvider extends AccessibilityNodeProviderCompat {
      public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
         return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(var1));
      }

      public AccessibilityNodeInfoCompat findFocus(int var1) {
         if(var1 == 2) {
            var1 = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId;
         } else {
            var1 = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
         }

         AccessibilityNodeInfoCompat var2;
         if(var1 == Integer.MIN_VALUE) {
            var2 = null;
         } else {
            var2 = this.createAccessibilityNodeInfo(var1);
         }

         return var2;
      }

      public boolean performAction(int var1, int var2, Bundle var3) {
         return ExploreByTouchHelper.this.performAction(var1, var2, var3);
      }
   }
}
