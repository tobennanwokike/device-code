package android.support.v7.widget;

import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.DropDownListView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public abstract class ForwardingListener implements OnTouchListener {
   private int mActivePointerId;
   private Runnable mDisallowIntercept;
   private boolean mForwarding;
   private final int mLongPressTimeout;
   private final float mScaledTouchSlop;
   final View mSrc;
   private final int mTapTimeout;
   private final int[] mTmpLocation = new int[2];
   private Runnable mTriggerLongPress;

   public ForwardingListener(View var1) {
      this.mSrc = var1;
      var1.setLongClickable(true);
      if(VERSION.SDK_INT >= 12) {
         this.addDetachListenerApi12(var1);
      } else {
         this.addDetachListenerBase(var1);
      }

      this.mScaledTouchSlop = (float)ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      this.mTapTimeout = ViewConfiguration.getTapTimeout();
      this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
   }

   private void addDetachListenerApi12(View var1) {
      var1.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
         public void onViewAttachedToWindow(View var1) {
         }

         public void onViewDetachedFromWindow(View var1) {
            ForwardingListener.this.onDetachedFromWindow();
         }
      });
   }

   private void addDetachListenerBase(View var1) {
      var1.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
         boolean mIsAttached;

         {
            this.mIsAttached = ViewCompat.isAttachedToWindow(ForwardingListener.this.mSrc);
         }

         public void onGlobalLayout() {
            boolean var1 = this.mIsAttached;
            this.mIsAttached = ViewCompat.isAttachedToWindow(ForwardingListener.this.mSrc);
            if(var1 && !this.mIsAttached) {
               ForwardingListener.this.onDetachedFromWindow();
            }

         }
      });
   }

   private void clearCallbacks() {
      if(this.mTriggerLongPress != null) {
         this.mSrc.removeCallbacks(this.mTriggerLongPress);
      }

      if(this.mDisallowIntercept != null) {
         this.mSrc.removeCallbacks(this.mDisallowIntercept);
      }

   }

   private void onDetachedFromWindow() {
      this.mForwarding = false;
      this.mActivePointerId = -1;
      if(this.mDisallowIntercept != null) {
         this.mSrc.removeCallbacks(this.mDisallowIntercept);
      }

   }

   private boolean onTouchForwarded(MotionEvent var1) {
      boolean var4 = true;
      boolean var5 = false;
      View var6 = this.mSrc;
      ShowableListMenu var7 = this.getPopup();
      boolean var3 = var5;
      if(var7 != null) {
         if(!var7.isShowing()) {
            var3 = var5;
         } else {
            DropDownListView var8 = (DropDownListView)var7.getListView();
            var3 = var5;
            if(var8 != null) {
               var3 = var5;
               if(var8.isShown()) {
                  MotionEvent var10 = MotionEvent.obtainNoHistory(var1);
                  this.toGlobalMotionEvent(var6, var10);
                  this.toLocalMotionEvent(var8, var10);
                  var3 = var8.onForwardedEvent(var10, this.mActivePointerId);
                  var10.recycle();
                  int var2 = MotionEventCompat.getActionMasked(var1);
                  boolean var9;
                  if(var2 != 1 && var2 != 3) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  if(var3 && var9) {
                     var3 = var4;
                  } else {
                     var3 = false;
                  }
               }
            }
         }
      }

      return var3;
   }

   private boolean onTouchObserved(MotionEvent var1) {
      boolean var4 = false;
      View var5 = this.mSrc;
      boolean var3;
      if(!var5.isEnabled()) {
         var3 = var4;
      } else {
         switch(MotionEventCompat.getActionMasked(var1)) {
         case 0:
            this.mActivePointerId = var1.getPointerId(0);
            if(this.mDisallowIntercept == null) {
               this.mDisallowIntercept = new ForwardingListener.DisallowIntercept();
            }

            var5.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
            if(this.mTriggerLongPress == null) {
               this.mTriggerLongPress = new ForwardingListener.TriggerLongPress();
            }

            var5.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
            var3 = var4;
            break;
         case 1:
         case 3:
            this.clearCallbacks();
            var3 = var4;
            break;
         case 2:
            int var2 = var1.findPointerIndex(this.mActivePointerId);
            var3 = var4;
            if(var2 >= 0) {
               var3 = var4;
               if(!pointInView(var5, var1.getX(var2), var1.getY(var2), this.mScaledTouchSlop)) {
                  this.clearCallbacks();
                  var5.getParent().requestDisallowInterceptTouchEvent(true);
                  var3 = true;
               }
            }
            break;
         default:
            var3 = var4;
         }
      }

      return var3;
   }

   private static boolean pointInView(View var0, float var1, float var2, float var3) {
      boolean var4;
      if(var1 >= -var3 && var2 >= -var3 && var1 < (float)(var0.getRight() - var0.getLeft()) + var3 && var2 < (float)(var0.getBottom() - var0.getTop()) + var3) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   private boolean toGlobalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)var3[0], (float)var3[1]);
      return true;
   }

   private boolean toLocalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)(-var3[0]), (float)(-var3[1]));
      return true;
   }

   public abstract ShowableListMenu getPopup();

   protected boolean onForwardingStarted() {
      ShowableListMenu var1 = this.getPopup();
      if(var1 != null && !var1.isShowing()) {
         var1.show();
      }

      return true;
   }

   protected boolean onForwardingStopped() {
      ShowableListMenu var1 = this.getPopup();
      if(var1 != null && var1.isShowing()) {
         var1.dismiss();
      }

      return true;
   }

   void onLongPress() {
      this.clearCallbacks();
      View var3 = this.mSrc;
      if(var3.isEnabled() && !var3.isLongClickable() && this.onForwardingStarted()) {
         var3.getParent().requestDisallowInterceptTouchEvent(true);
         long var1 = SystemClock.uptimeMillis();
         MotionEvent var4 = MotionEvent.obtain(var1, var1, 3, 0.0F, 0.0F, 0);
         var3.onTouchEvent(var4);
         var4.recycle();
         this.mForwarding = true;
      }

   }

   public boolean onTouch(View var1, MotionEvent var2) {
      boolean var5 = false;
      boolean var6 = this.mForwarding;
      boolean var3;
      if(var6) {
         if(!this.onTouchForwarded(var2) && this.onForwardingStopped()) {
            var3 = false;
         } else {
            var3 = true;
         }
      } else {
         boolean var4;
         if(this.onTouchObserved(var2) && this.onForwardingStarted()) {
            var4 = true;
         } else {
            var4 = false;
         }

         var3 = var4;
         if(var4) {
            long var7 = SystemClock.uptimeMillis();
            MotionEvent var9 = MotionEvent.obtain(var7, var7, 3, 0.0F, 0.0F, 0);
            this.mSrc.onTouchEvent(var9);
            var9.recycle();
            var3 = var4;
         }
      }

      this.mForwarding = var3;
      if(!var3) {
         var3 = var5;
         if(!var6) {
            return var3;
         }
      }

      var3 = true;
      return var3;
   }

   private class DisallowIntercept implements Runnable {
      public void run() {
         ViewParent var1 = ForwardingListener.this.mSrc.getParent();
         if(var1 != null) {
            var1.requestDisallowInterceptTouchEvent(true);
         }

      }
   }

   private class TriggerLongPress implements Runnable {
      public void run() {
         ForwardingListener.this.onLongPress();
      }
   }
}
