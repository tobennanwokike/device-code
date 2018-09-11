package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.DrawerLayoutCompatApi21;
import android.support.v4.widget.DrawerLayoutImpl;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup implements DrawerLayoutImpl {
   private static final boolean ALLOW_EDGE_LOCK = false;
   static final boolean CAN_HIDE_DESCENDANTS;
   private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
   private static final int DEFAULT_SCRIM_COLOR = -1728053248;
   private static final int DRAWER_ELEVATION = 10;
   static final DrawerLayout.DrawerLayoutCompatImpl IMPL;
   static final int[] LAYOUT_ATTRS;
   public static final int LOCK_MODE_LOCKED_CLOSED = 1;
   public static final int LOCK_MODE_LOCKED_OPEN = 2;
   public static final int LOCK_MODE_UNDEFINED = 3;
   public static final int LOCK_MODE_UNLOCKED = 0;
   private static final int MIN_DRAWER_MARGIN = 64;
   private static final int MIN_FLING_VELOCITY = 400;
   private static final int PEEK_DELAY = 160;
   private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_IDLE = 0;
   public static final int STATE_SETTLING = 2;
   private static final String TAG = "DrawerLayout";
   private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
   private final DrawerLayout.ChildAccessibilityDelegate mChildAccessibilityDelegate;
   private boolean mChildrenCanceledTouch;
   private boolean mDisallowInterceptRequested;
   private boolean mDrawStatusBarBackground;
   private float mDrawerElevation;
   private int mDrawerState;
   private boolean mFirstLayout;
   private boolean mInLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private Object mLastInsets;
   private final DrawerLayout.ViewDragCallback mLeftCallback;
   private final ViewDragHelper mLeftDragger;
   @Nullable
   private DrawerLayout.DrawerListener mListener;
   private List mListeners;
   private int mLockModeEnd;
   private int mLockModeLeft;
   private int mLockModeRight;
   private int mLockModeStart;
   private int mMinDrawerMargin;
   private final ArrayList mNonDrawerViews;
   private final DrawerLayout.ViewDragCallback mRightCallback;
   private final ViewDragHelper mRightDragger;
   private int mScrimColor;
   private float mScrimOpacity;
   private Paint mScrimPaint;
   private Drawable mShadowEnd;
   private Drawable mShadowLeft;
   private Drawable mShadowLeftResolved;
   private Drawable mShadowRight;
   private Drawable mShadowRightResolved;
   private Drawable mShadowStart;
   private Drawable mStatusBarBackground;
   private CharSequence mTitleLeft;
   private CharSequence mTitleRight;

   static {
      boolean var1 = true;
      LAYOUT_ATTRS = new int[]{16842931};
      boolean var0;
      if(VERSION.SDK_INT >= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      CAN_HIDE_DESCENDANTS = var0;
      if(VERSION.SDK_INT >= 21) {
         var0 = var1;
      } else {
         var0 = false;
      }

      SET_DRAWER_SHADOW_FROM_ELEVATION = var0;
      if(VERSION.SDK_INT >= 21) {
         IMPL = new DrawerLayout.DrawerLayoutCompatImplApi21();
      } else {
         IMPL = new DrawerLayout.DrawerLayoutCompatImplBase();
      }

   }

   public DrawerLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DrawerLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public DrawerLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mChildAccessibilityDelegate = new DrawerLayout.ChildAccessibilityDelegate();
      this.mScrimColor = -1728053248;
      this.mScrimPaint = new Paint();
      this.mFirstLayout = true;
      this.mLockModeLeft = 3;
      this.mLockModeRight = 3;
      this.mLockModeStart = 3;
      this.mLockModeEnd = 3;
      this.mShadowStart = null;
      this.mShadowEnd = null;
      this.mShadowLeft = null;
      this.mShadowRight = null;
      this.setDescendantFocusability(262144);
      float var5 = this.getResources().getDisplayMetrics().density;
      this.mMinDrawerMargin = (int)(64.0F * var5 + 0.5F);
      float var4 = 400.0F * var5;
      this.mLeftCallback = new DrawerLayout.ViewDragCallback(3);
      this.mRightCallback = new DrawerLayout.ViewDragCallback(5);
      this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
      this.mLeftDragger.setEdgeTrackingEnabled(1);
      this.mLeftDragger.setMinVelocity(var4);
      this.mLeftCallback.setDragger(this.mLeftDragger);
      this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
      this.mRightDragger.setEdgeTrackingEnabled(2);
      this.mRightDragger.setMinVelocity(var4);
      this.mRightCallback.setDragger(this.mRightDragger);
      this.setFocusableInTouchMode(true);
      ViewCompat.setImportantForAccessibility(this, 1);
      ViewCompat.setAccessibilityDelegate(this, new DrawerLayout.AccessibilityDelegate());
      ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
      if(ViewCompat.getFitsSystemWindows(this)) {
         IMPL.configureApplyInsets(this);
         this.mStatusBarBackground = IMPL.getDefaultStatusBarBackground(var1);
      }

      this.mDrawerElevation = 10.0F * var5;
      this.mNonDrawerViews = new ArrayList();
   }

   static String gravityToString(int var0) {
      String var1;
      if((var0 & 3) == 3) {
         var1 = "LEFT";
      } else if((var0 & 5) == 5) {
         var1 = "RIGHT";
      } else {
         var1 = Integer.toHexString(var0);
      }

      return var1;
   }

   private static boolean hasOpaqueBackground(View var0) {
      boolean var2 = false;
      Drawable var3 = var0.getBackground();
      boolean var1 = var2;
      if(var3 != null) {
         var1 = var2;
         if(var3.getOpacity() == -1) {
            var1 = true;
         }
      }

      return var1;
   }

   private boolean hasPeekingDrawer() {
      int var2 = this.getChildCount();
      int var1 = 0;

      boolean var3;
      while(true) {
         if(var1 >= var2) {
            var3 = false;
            break;
         }

         if(((DrawerLayout.LayoutParams)this.getChildAt(var1).getLayoutParams()).isPeeking) {
            var3 = true;
            break;
         }

         ++var1;
      }

      return var3;
   }

   private boolean hasVisibleDrawer() {
      boolean var1;
      if(this.findVisibleDrawer() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   static boolean includeChildForAccessibility(View var0) {
      boolean var1;
      if(ViewCompat.getImportantForAccessibility(var0) != 4 && ViewCompat.getImportantForAccessibility(var0) != 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean mirror(Drawable var1, int var2) {
      boolean var3;
      if(var1 != null && DrawableCompat.isAutoMirrored(var1)) {
         DrawableCompat.setLayoutDirection(var1, var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private Drawable resolveLeftShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if(var1 == 0) {
         if(this.mShadowStart != null) {
            this.mirror(this.mShadowStart, var1);
            var2 = this.mShadowStart;
            return var2;
         }
      } else if(this.mShadowEnd != null) {
         this.mirror(this.mShadowEnd, var1);
         var2 = this.mShadowEnd;
         return var2;
      }

      var2 = this.mShadowLeft;
      return var2;
   }

   private Drawable resolveRightShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if(var1 == 0) {
         if(this.mShadowEnd != null) {
            this.mirror(this.mShadowEnd, var1);
            var2 = this.mShadowEnd;
            return var2;
         }
      } else if(this.mShadowStart != null) {
         this.mirror(this.mShadowStart, var1);
         var2 = this.mShadowStart;
         return var2;
      }

      var2 = this.mShadowRight;
      return var2;
   }

   private void resolveShadowDrawables() {
      if(!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         this.mShadowLeftResolved = this.resolveLeftShadow();
         this.mShadowRightResolved = this.resolveRightShadow();
      }

   }

   private void updateChildrenImportantForAccessibility(View var1, boolean var2) {
      int var4 = this.getChildCount();

      for(int var3 = 0; var3 < var4; ++var3) {
         View var5 = this.getChildAt(var3);
         if((var2 || this.isDrawerView(var5)) && (!var2 || var5 != var1)) {
            ViewCompat.setImportantForAccessibility(var5, 4);
         } else {
            ViewCompat.setImportantForAccessibility(var5, 1);
         }
      }

   }

   public void addDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if(var1 != null) {
         if(this.mListeners == null) {
            this.mListeners = new ArrayList();
         }

         this.mListeners.add(var1);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      if(this.getDescendantFocusability() != 393216) {
         int var6 = this.getChildCount();
         boolean var5 = false;

         int var4;
         View var7;
         for(var4 = 0; var4 < var6; ++var4) {
            var7 = this.getChildAt(var4);
            if(this.isDrawerView(var7)) {
               if(this.isDrawerOpen(var7)) {
                  var5 = true;
                  var7.addFocusables(var1, var2, var3);
               }
            } else {
               this.mNonDrawerViews.add(var7);
            }
         }

         if(!var5) {
            int var8 = this.mNonDrawerViews.size();

            for(var4 = 0; var4 < var8; ++var4) {
               var7 = (View)this.mNonDrawerViews.get(var4);
               if(var7.getVisibility() == 0) {
                  var7.addFocusables(var1, var2, var3);
               }
            }
         }

         this.mNonDrawerViews.clear();
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if(this.findOpenDrawer() == null && !this.isDrawerView(var1)) {
         ViewCompat.setImportantForAccessibility(var1, 1);
      } else {
         ViewCompat.setImportantForAccessibility(var1, 4);
      }

      if(!CAN_HIDE_DESCENDANTS) {
         ViewCompat.setAccessibilityDelegate(var1, this.mChildAccessibilityDelegate);
      }

   }

   void cancelChildViewTouch() {
      if(!this.mChildrenCanceledTouch) {
         long var3 = SystemClock.uptimeMillis();
         MotionEvent var5 = MotionEvent.obtain(var3, var3, 3, 0.0F, 0.0F, 0);
         int var2 = this.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            this.getChildAt(var1).dispatchTouchEvent(var5);
         }

         var5.recycle();
         this.mChildrenCanceledTouch = true;
      }

   }

   boolean checkDrawerViewAbsoluteGravity(View var1, int var2) {
      boolean var3;
      if((this.getDrawerViewAbsoluteGravity(var1) & var2) == var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if(var1 instanceof DrawerLayout.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void closeDrawer(int var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if(var3 == null) {
         throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(var1));
      } else {
         this.closeDrawer(var3, var2);
      }
   }

   public void closeDrawer(View var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(View var1, boolean var2) {
      if(!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
      } else {
         DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if(this.mFirstLayout) {
            var3.onScreen = 0.0F;
            var3.openState = 0;
         } else if(var2) {
            var3.openState |= 4;
            if(this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, -var1.getWidth(), var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 0.0F);
            this.updateDrawerState(var3.gravity, 0, var1);
            var1.setVisibility(4);
         }

         this.invalidate();
      }
   }

   public void closeDrawers() {
      this.closeDrawers(false);
   }

   void closeDrawers(boolean var1) {
      boolean var2 = false;
      int var5 = this.getChildCount();

      boolean var4;
      for(int var3 = 0; var3 < var5; var2 = var4) {
         View var6 = this.getChildAt(var3);
         DrawerLayout.LayoutParams var7 = (DrawerLayout.LayoutParams)var6.getLayoutParams();
         var4 = var2;
         if(this.isDrawerView(var6)) {
            if(var1 && !var7.isPeeking) {
               var4 = var2;
            } else {
               int var8 = var6.getWidth();
               if(this.checkDrawerViewAbsoluteGravity(var6, 3)) {
                  var2 |= this.mLeftDragger.smoothSlideViewTo(var6, -var8, var6.getTop());
               } else {
                  var2 |= this.mRightDragger.smoothSlideViewTo(var6, this.getWidth(), var6.getTop());
               }

               var7.isPeeking = false;
               var4 = var2;
            }
         }

         ++var3;
      }

      this.mLeftCallback.removeCallbacks();
      this.mRightCallback.removeCallbacks();
      if(var2) {
         this.invalidate();
      }

   }

   public void computeScroll() {
      int var3 = this.getChildCount();
      float var1 = 0.0F;

      for(int var2 = 0; var2 < var3; ++var2) {
         var1 = Math.max(var1, ((DrawerLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).onScreen);
      }

      this.mScrimOpacity = var1;
      if(this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void dispatchOnDrawerClosed(View var1) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if((var3.openState & 1) == 1) {
         var3.openState = 0;
         if(this.mListeners != null) {
            for(int var2 = this.mListeners.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerClosed(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, false);
         if(this.hasWindowFocus()) {
            var1 = this.getRootView();
            if(var1 != null) {
               var1.sendAccessibilityEvent(32);
            }
         }
      }

   }

   void dispatchOnDrawerOpened(View var1) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if((var3.openState & 1) == 0) {
         var3.openState = 1;
         if(this.mListeners != null) {
            for(int var2 = this.mListeners.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerOpened(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, true);
         if(this.hasWindowFocus()) {
            this.sendAccessibilityEvent(32);
         }
      }

   }

   void dispatchOnDrawerSlide(View var1, float var2) {
      if(this.mListeners != null) {
         for(int var3 = this.mListeners.size() - 1; var3 >= 0; --var3) {
            ((DrawerLayout.DrawerListener)this.mListeners.get(var3)).onDrawerSlide(var1, var2);
         }
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      int var13 = this.getHeight();
      boolean var15 = this.isContentView(var2);
      int var6 = 0;
      byte var9 = 0;
      int var7 = this.getWidth();
      int var12 = var1.save();
      int var8 = var7;
      int var18;
      if(var15) {
         int var14 = this.getChildCount();
         var8 = 0;

         int var10;
         for(var6 = var9; var8 < var14; var7 = var10) {
            View var17 = this.getChildAt(var8);
            var18 = var6;
            var10 = var7;
            if(var17 != var2) {
               var18 = var6;
               var10 = var7;
               if(var17.getVisibility() == 0) {
                  var18 = var6;
                  var10 = var7;
                  if(hasOpaqueBackground(var17)) {
                     var18 = var6;
                     var10 = var7;
                     if(this.isDrawerView(var17)) {
                        if(var17.getHeight() < var13) {
                           var10 = var7;
                           var18 = var6;
                        } else {
                           int var11;
                           if(this.checkDrawerViewAbsoluteGravity(var17, 3)) {
                              var11 = var17.getRight();
                              var18 = var6;
                              var10 = var7;
                              if(var11 > var6) {
                                 var18 = var11;
                                 var10 = var7;
                              }
                           } else {
                              var11 = var17.getLeft();
                              var18 = var6;
                              var10 = var7;
                              if(var11 < var7) {
                                 var10 = var11;
                                 var18 = var6;
                              }
                           }
                        }
                     }
                  }
               }
            }

            ++var8;
            var6 = var18;
         }

         var1.clipRect(var6, 0, var7, this.getHeight());
         var8 = var7;
      }

      boolean var16 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var12);
      if(this.mScrimOpacity > 0.0F && var15) {
         var18 = (int)((float)((this.mScrimColor & -16777216) >>> 24) * this.mScrimOpacity);
         var7 = this.mScrimColor;
         this.mScrimPaint.setColor(var18 << 24 | var7 & 16777215);
         var1.drawRect((float)var6, 0.0F, (float)var8, (float)this.getHeight(), this.mScrimPaint);
      } else {
         float var5;
         if(this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 3)) {
            var6 = this.mShadowLeftResolved.getIntrinsicWidth();
            var7 = var2.getRight();
            var8 = this.mLeftDragger.getEdgeSize();
            var5 = Math.max(0.0F, Math.min((float)var7 / (float)var8, 1.0F));
            this.mShadowLeftResolved.setBounds(var7, var2.getTop(), var7 + var6, var2.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0F * var5));
            this.mShadowLeftResolved.draw(var1);
         } else if(this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 5)) {
            var6 = this.mShadowRightResolved.getIntrinsicWidth();
            var18 = var2.getLeft();
            var8 = this.getWidth();
            var7 = this.mRightDragger.getEdgeSize();
            var5 = Math.max(0.0F, Math.min((float)(var8 - var18) / (float)var7, 1.0F));
            this.mShadowRightResolved.setBounds(var18 - var6, var2.getTop(), var18, var2.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0F * var5));
            this.mShadowRightResolved.draw(var1);
         }
      }

      return var16;
   }

   View findDrawerWithGravity(int var1) {
      int var3 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      int var2 = this.getChildCount();
      var1 = 0;

      View var4;
      while(true) {
         if(var1 >= var2) {
            var4 = null;
            break;
         }

         var4 = this.getChildAt(var1);
         if((this.getDrawerViewAbsoluteGravity(var4) & 7) == (var3 & 7)) {
            break;
         }

         ++var1;
      }

      return var4;
   }

   View findOpenDrawer() {
      int var2 = this.getChildCount();
      int var1 = 0;

      View var3;
      while(true) {
         if(var1 >= var2) {
            var3 = null;
            break;
         }

         var3 = this.getChildAt(var1);
         if((((DrawerLayout.LayoutParams)var3.getLayoutParams()).openState & 1) == 1) {
            break;
         }

         ++var1;
      }

      return var3;
   }

   View findVisibleDrawer() {
      int var2 = this.getChildCount();
      int var1 = 0;

      View var3;
      while(true) {
         if(var1 >= var2) {
            var3 = null;
            break;
         }

         var3 = this.getChildAt(var1);
         if(this.isDrawerView(var3) && this.isDrawerVisible(var3)) {
            break;
         }

         ++var1;
      }

      return var3;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new DrawerLayout.LayoutParams(-1, -1);
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new DrawerLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      DrawerLayout.LayoutParams var2;
      if(var1 instanceof DrawerLayout.LayoutParams) {
         var2 = new DrawerLayout.LayoutParams((DrawerLayout.LayoutParams)var1);
      } else if(var1 instanceof MarginLayoutParams) {
         var2 = new DrawerLayout.LayoutParams((MarginLayoutParams)var1);
      } else {
         var2 = new DrawerLayout.LayoutParams(var1);
      }

      return var2;
   }

   public float getDrawerElevation() {
      float var1;
      if(SET_DRAWER_SHADOW_FROM_ELEVATION) {
         var1 = this.mDrawerElevation;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public int getDrawerLockMode(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      switch(var1) {
      case 3:
         if(this.mLockModeLeft != 3) {
            var1 = this.mLockModeLeft;
            return var1;
         }

         if(var2 == 0) {
            var1 = this.mLockModeStart;
         } else {
            var1 = this.mLockModeEnd;
         }

         if(var1 != 3) {
            return var1;
         }
         break;
      case 5:
         if(this.mLockModeRight != 3) {
            var1 = this.mLockModeRight;
            return var1;
         }

         if(var2 == 0) {
            var1 = this.mLockModeEnd;
         } else {
            var1 = this.mLockModeStart;
         }

         if(var1 != 3) {
            return var1;
         }
         break;
      case 8388611:
         if(this.mLockModeStart != 3) {
            var1 = this.mLockModeStart;
            return var1;
         }

         if(var2 == 0) {
            var1 = this.mLockModeLeft;
         } else {
            var1 = this.mLockModeRight;
         }

         if(var1 != 3) {
            return var1;
         }
         break;
      case 8388613:
         if(this.mLockModeEnd != 3) {
            var1 = this.mLockModeEnd;
            return var1;
         }

         if(var2 == 0) {
            var1 = this.mLockModeRight;
         } else {
            var1 = this.mLockModeLeft;
         }

         if(var1 != 3) {
            return var1;
         }
      }

      var1 = 0;
      return var1;
   }

   public int getDrawerLockMode(View var1) {
      if(!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         return this.getDrawerLockMode(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity);
      }
   }

   @Nullable
   public CharSequence getDrawerTitle(int var1) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      CharSequence var2;
      if(var1 == 3) {
         var2 = this.mTitleLeft;
      } else if(var1 == 5) {
         var2 = this.mTitleRight;
      } else {
         var2 = null;
      }

      return var2;
   }

   int getDrawerViewAbsoluteGravity(View var1) {
      return GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
   }

   float getDrawerViewOffset(View var1) {
      return ((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen;
   }

   public Drawable getStatusBarBackgroundDrawable() {
      return this.mStatusBarBackground;
   }

   boolean isContentView(View var1) {
      boolean var2;
      if(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isDrawerOpen(int var1) {
      View var3 = this.findDrawerWithGravity(var1);
      boolean var2;
      if(var3 != null) {
         var2 = this.isDrawerOpen(var3);
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isDrawerOpen(View var1) {
      boolean var2 = true;
      if(!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         if((((DrawerLayout.LayoutParams)var1.getLayoutParams()).openState & 1) != 1) {
            var2 = false;
         }

         return var2;
      }
   }

   boolean isDrawerView(View var1) {
      int var2 = GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(var1));
      boolean var3;
      if((var2 & 3) != 0) {
         var3 = true;
      } else if((var2 & 5) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isDrawerVisible(int var1) {
      View var3 = this.findDrawerWithGravity(var1);
      boolean var2;
      if(var3 != null) {
         var2 = this.isDrawerVisible(var3);
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isDrawerVisible(View var1) {
      if(!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         boolean var2;
         if(((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen > 0.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   void moveDrawerToOffset(View var1, float var2) {
      float var3 = this.getDrawerViewOffset(var1);
      int var4 = var1.getWidth();
      int var5 = (int)((float)var4 * var3);
      var4 = (int)((float)var4 * var2) - var5;
      if(!this.checkDrawerViewAbsoluteGravity(var1, 3)) {
         var4 = -var4;
      }

      var1.offsetLeftAndRight(var4);
      this.setDrawerViewOffset(var1, var2);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if(this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
         int var2 = IMPL.getTopInset(this.mLastInsets);
         if(var2 > 0) {
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), var2);
            this.mStatusBarBackground.draw(var1);
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var7 = false;
      int var4 = MotionEventCompat.getActionMasked(var1);
      boolean var9 = this.mLeftDragger.shouldInterceptTouchEvent(var1);
      boolean var8 = this.mRightDragger.shouldInterceptTouchEvent(var1);
      boolean var6 = false;
      boolean var5 = false;
      boolean var11;
      switch(var4) {
      case 0:
         float var2 = var1.getX();
         float var3 = var1.getY();
         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         var11 = var6;
         if(this.mScrimOpacity > 0.0F) {
            View var10 = this.mLeftDragger.findTopChildUnder((int)var2, (int)var3);
            var11 = var6;
            if(var10 != null) {
               var11 = var6;
               if(this.isContentView(var10)) {
                  var11 = true;
               }
            }
         }

         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         break;
      case 1:
      case 3:
         this.closeDrawers(true);
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         var11 = var5;
         break;
      case 2:
         var11 = var5;
         if(this.mLeftDragger.checkTouchSlop(3)) {
            this.mLeftCallback.removeCallbacks();
            this.mRightCallback.removeCallbacks();
            var11 = var5;
         }
         break;
      default:
         var11 = var5;
      }

      if(var9 | var8 || var11 || this.hasPeekingDrawer() || this.mChildrenCanceledTouch) {
         var7 = true;
      }

      return var7;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if(var1 == 4 && this.hasVisibleDrawer()) {
         var2.startTracking();
         var3 = true;
      } else {
         var3 = super.onKeyDown(var1, var2);
      }

      return var3;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3;
      if(var1 == 4) {
         View var4 = this.findVisibleDrawer();
         if(var4 != null && this.getDrawerLockMode(var4) == 0) {
            this.closeDrawers();
         }

         if(var4 != null) {
            var3 = true;
         } else {
            var3 = false;
         }
      } else {
         var3 = super.onKeyUp(var1, var2);
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mInLayout = true;
      int var11 = var4 - var2;
      int var10 = this.getChildCount();

      for(var4 = 0; var4 < var10; ++var4) {
         View var15 = this.getChildAt(var4);
         if(var15.getVisibility() != 8) {
            DrawerLayout.LayoutParams var16 = (DrawerLayout.LayoutParams)var15.getLayoutParams();
            if(this.isContentView(var15)) {
               var15.layout(var16.leftMargin, var16.topMargin, var16.leftMargin + var15.getMeasuredWidth(), var16.topMargin + var15.getMeasuredHeight());
            } else {
               int var12 = var15.getMeasuredWidth();
               int var13 = var15.getMeasuredHeight();
               float var6;
               int var7;
               if(this.checkDrawerViewAbsoluteGravity(var15, 3)) {
                  var7 = -var12 + (int)((float)var12 * var16.onScreen);
                  var6 = (float)(var12 + var7) / (float)var12;
               } else {
                  var7 = var11 - (int)((float)var12 * var16.onScreen);
                  var6 = (float)(var11 - var7) / (float)var12;
               }

               boolean var8;
               if(var6 != var16.onScreen) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               switch(var16.gravity & 112) {
               case 16:
                  int var14 = var5 - var3;
                  int var9 = (var14 - var13) / 2;
                  if(var9 < var16.topMargin) {
                     var2 = var16.topMargin;
                  } else {
                     var2 = var9;
                     if(var9 + var13 > var14 - var16.bottomMargin) {
                        var2 = var14 - var16.bottomMargin - var13;
                     }
                  }

                  var15.layout(var7, var2, var7 + var12, var2 + var13);
                  break;
               case 80:
                  var2 = var5 - var3;
                  var15.layout(var7, var2 - var16.bottomMargin - var15.getMeasuredHeight(), var7 + var12, var2 - var16.bottomMargin);
                  break;
               default:
                  var15.layout(var7, var16.topMargin, var7 + var12, var16.topMargin + var13);
               }

               if(var8) {
                  this.setDrawerViewOffset(var15, var6);
               }

               byte var17;
               if(var16.onScreen > 0.0F) {
                  var17 = 0;
               } else {
                  var17 = 4;
               }

               if(var15.getVisibility() != var17) {
                  var15.setVisibility(var17);
               }
            }
         }
      }

      this.mInLayout = false;
      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var5;
      int var6;
      int var8;
      label92: {
         var8 = MeasureSpec.getMode(var1);
         int var7 = MeasureSpec.getMode(var2);
         int var3 = MeasureSpec.getSize(var1);
         int var4 = MeasureSpec.getSize(var2);
         if(var8 == 1073741824) {
            var6 = var4;
            var5 = var3;
            if(var7 == 1073741824) {
               break label92;
            }
         }

         if(!this.isInEditMode()) {
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
         }

         if(var8 != Integer.MIN_VALUE && var8 == 0) {
            var3 = 300;
         }

         if(var7 == Integer.MIN_VALUE) {
            var5 = var3;
            var6 = var4;
         } else {
            var6 = var4;
            var5 = var3;
            if(var7 == 0) {
               var6 = 300;
               var5 = var3;
            }
         }
      }

      this.setMeasuredDimension(var5, var6);
      boolean var17;
      if(this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this)) {
         var17 = true;
      } else {
         var17 = false;
      }

      int var10 = ViewCompat.getLayoutDirection(this);
      boolean var15 = false;
      boolean var16 = false;
      int var11 = this.getChildCount();

      for(var8 = 0; var8 < var11; ++var8) {
         View var13 = this.getChildAt(var8);
         if(var13.getVisibility() != 8) {
            DrawerLayout.LayoutParams var14 = (DrawerLayout.LayoutParams)var13.getLayoutParams();
            if(var17) {
               int var9 = GravityCompat.getAbsoluteGravity(var14.gravity, var10);
               if(ViewCompat.getFitsSystemWindows(var13)) {
                  IMPL.dispatchChildInsets(var13, this.mLastInsets, var9);
               } else {
                  IMPL.applyMarginInsets(var14, this.mLastInsets, var9);
               }
            }

            if(this.isContentView(var13)) {
               var13.measure(MeasureSpec.makeMeasureSpec(var5 - var14.leftMargin - var14.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(var6 - var14.topMargin - var14.bottomMargin, 1073741824));
            } else {
               if(!this.isDrawerView(var13)) {
                  throw new IllegalStateException("Child " + var13 + " at index " + var8 + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
               }

               if(SET_DRAWER_SHADOW_FROM_ELEVATION && ViewCompat.getElevation(var13) != this.mDrawerElevation) {
                  ViewCompat.setElevation(var13, this.mDrawerElevation);
               }

               int var12 = this.getDrawerViewAbsoluteGravity(var13) & 7;
               boolean var18;
               if(var12 == 3) {
                  var18 = true;
               } else {
                  var18 = false;
               }

               if(var18 && var15 || !var18 && var16) {
                  throw new IllegalStateException("Child drawer has absolute gravity " + gravityToString(var12) + " but this " + "DrawerLayout" + " already has a " + "drawer view along that edge");
               }

               if(var18) {
                  var15 = true;
               } else {
                  var16 = true;
               }

               var13.measure(getChildMeasureSpec(var1, this.mMinDrawerMargin + var14.leftMargin + var14.rightMargin, var14.width), getChildMeasureSpec(var2, var14.topMargin + var14.bottomMargin, var14.height));
            }
         }
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if(!(var1 instanceof DrawerLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         DrawerLayout.SavedState var2 = (DrawerLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if(var2.openDrawerGravity != 0) {
            View var3 = this.findDrawerWithGravity(var2.openDrawerGravity);
            if(var3 != null) {
               this.openDrawer(var3);
            }
         }

         if(var2.lockModeLeft != 3) {
            this.setDrawerLockMode(var2.lockModeLeft, 3);
         }

         if(var2.lockModeRight != 3) {
            this.setDrawerLockMode(var2.lockModeRight, 5);
         }

         if(var2.lockModeStart != 3) {
            this.setDrawerLockMode(var2.lockModeStart, 8388611);
         }

         if(var2.lockModeEnd != 3) {
            this.setDrawerLockMode(var2.lockModeEnd, 8388613);
         }
      }

   }

   public void onRtlPropertiesChanged(int var1) {
      this.resolveShadowDrawables();
   }

   protected Parcelable onSaveInstanceState() {
      DrawerLayout.SavedState var5 = new DrawerLayout.SavedState(super.onSaveInstanceState());
      int var4 = this.getChildCount();

      for(int var1 = 0; var1 < var4; ++var1) {
         DrawerLayout.LayoutParams var6 = (DrawerLayout.LayoutParams)this.getChildAt(var1).getLayoutParams();
         boolean var2;
         if(var6.openState == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         boolean var3;
         if(var6.openState == 2) {
            var3 = true;
         } else {
            var3 = false;
         }

         if(var2 || var3) {
            var5.openDrawerGravity = var6.gravity;
            break;
         }
      }

      var5.lockModeLeft = this.mLockModeLeft;
      var5.lockModeRight = this.mLockModeRight;
      var5.lockModeStart = this.mLockModeStart;
      var5.lockModeEnd = this.mLockModeEnd;
      return var5;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.mLeftDragger.processTouchEvent(var1);
      this.mRightDragger.processTouchEvent(var1);
      float var2;
      float var3;
      switch(var1.getAction() & 255) {
      case 0:
         var3 = var1.getX();
         var2 = var1.getY();
         this.mInitialMotionX = var3;
         this.mInitialMotionY = var2;
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         break;
      case 1:
         var2 = var1.getX();
         var3 = var1.getY();
         boolean var6 = true;
         View var7 = this.mLeftDragger.findTopChildUnder((int)var2, (int)var3);
         boolean var5 = var6;
         if(var7 != null) {
            var5 = var6;
            if(this.isContentView(var7)) {
               var2 -= this.mInitialMotionX;
               var3 -= this.mInitialMotionY;
               int var4 = this.mLeftDragger.getTouchSlop();
               var5 = var6;
               if(var2 * var2 + var3 * var3 < (float)(var4 * var4)) {
                  var7 = this.findOpenDrawer();
                  var5 = var6;
                  if(var7 != null) {
                     if(this.getDrawerLockMode(var7) == 2) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }
                  }
               }
            }
         }

         this.closeDrawers(var5);
         this.mDisallowInterceptRequested = false;
      case 2:
      default:
         break;
      case 3:
         this.closeDrawers(true);
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
      }

      return true;
   }

   public void openDrawer(int var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if(var3 == null) {
         throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(var1));
      } else {
         this.openDrawer(var3, var2);
      }
   }

   public void openDrawer(View var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(View var1, boolean var2) {
      if(!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
      } else {
         DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if(this.mFirstLayout) {
            var3.onScreen = 1.0F;
            var3.openState = 1;
            this.updateChildrenImportantForAccessibility(var1, true);
         } else if(var2) {
            var3.openState |= 2;
            if(this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, 0, var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth() - var1.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 1.0F);
            this.updateDrawerState(var3.gravity, 0, var1);
            var1.setVisibility(0);
         }

         this.invalidate();
      }
   }

   public void removeDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if(var1 != null && this.mListeners != null) {
         this.mListeners.remove(var1);
      }

   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      super.requestDisallowInterceptTouchEvent(var1);
      this.mDisallowInterceptRequested = var1;
      if(var1) {
         this.closeDrawers(true);
      }

   }

   public void requestLayout() {
      if(!this.mInLayout) {
         super.requestLayout();
      }

   }

   public void setChildInsets(Object var1, boolean var2) {
      this.mLastInsets = var1;
      this.mDrawStatusBarBackground = var2;
      if(!var2 && this.getBackground() == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.requestLayout();
   }

   public void setDrawerElevation(float var1) {
      this.mDrawerElevation = var1;

      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         View var3 = this.getChildAt(var2);
         if(this.isDrawerView(var3)) {
            ViewCompat.setElevation(var3, this.mDrawerElevation);
         }
      }

   }

   @Deprecated
   public void setDrawerListener(DrawerLayout.DrawerListener var1) {
      if(this.mListener != null) {
         this.removeDrawerListener(this.mListener);
      }

      if(var1 != null) {
         this.addDrawerListener(var1);
      }

      this.mListener = var1;
   }

   public void setDrawerLockMode(int var1) {
      this.setDrawerLockMode(var1, 3);
      this.setDrawerLockMode(var1, 5);
   }

   public void setDrawerLockMode(int var1, int var2) {
      int var3 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      switch(var2) {
      case 3:
         this.mLockModeLeft = var1;
         break;
      case 5:
         this.mLockModeRight = var1;
         break;
      case 8388611:
         this.mLockModeStart = var1;
         break;
      case 8388613:
         this.mLockModeEnd = var1;
      }

      if(var1 != 0) {
         ViewDragHelper var4;
         if(var3 == 3) {
            var4 = this.mLeftDragger;
         } else {
            var4 = this.mRightDragger;
         }

         var4.cancel();
      }

      View var5;
      switch(var1) {
      case 1:
         var5 = this.findDrawerWithGravity(var3);
         if(var5 != null) {
            this.closeDrawer(var5);
         }
         break;
      case 2:
         var5 = this.findDrawerWithGravity(var3);
         if(var5 != null) {
            this.openDrawer(var5);
         }
      }

   }

   public void setDrawerLockMode(int var1, View var2) {
      if(!this.isDrawerView(var2)) {
         throw new IllegalArgumentException("View " + var2 + " is not a " + "drawer with appropriate layout_gravity");
      } else {
         this.setDrawerLockMode(var1, ((DrawerLayout.LayoutParams)var2.getLayoutParams()).gravity);
      }
   }

   public void setDrawerShadow(@DrawableRes int var1, int var2) {
      this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), var1), var2);
   }

   public void setDrawerShadow(Drawable var1, int var2) {
      if(!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         if((var2 & 8388611) == 8388611) {
            this.mShadowStart = var1;
         } else if((var2 & 8388613) == 8388613) {
            this.mShadowEnd = var1;
         } else if((var2 & 3) == 3) {
            this.mShadowLeft = var1;
         } else {
            if((var2 & 5) != 5) {
               return;
            }

            this.mShadowRight = var1;
         }

         this.resolveShadowDrawables();
         this.invalidate();
      }

   }

   public void setDrawerTitle(int var1, CharSequence var2) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      if(var1 == 3) {
         this.mTitleLeft = var2;
      } else if(var1 == 5) {
         this.mTitleRight = var2;
      }

   }

   void setDrawerViewOffset(View var1, float var2) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if(var2 != var3.onScreen) {
         var3.onScreen = var2;
         this.dispatchOnDrawerSlide(var1, var2);
      }

   }

   public void setScrimColor(@ColorInt int var1) {
      this.mScrimColor = var1;
      this.invalidate();
   }

   public void setStatusBarBackground(int var1) {
      Drawable var2;
      if(var1 != 0) {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.mStatusBarBackground = var2;
      this.invalidate();
   }

   public void setStatusBarBackground(Drawable var1) {
      this.mStatusBarBackground = var1;
      this.invalidate();
   }

   public void setStatusBarBackgroundColor(@ColorInt int var1) {
      this.mStatusBarBackground = new ColorDrawable(var1);
      this.invalidate();
   }

   void updateDrawerState(int var1, int var2, View var3) {
      var1 = this.mLeftDragger.getViewDragState();
      int var4 = this.mRightDragger.getViewDragState();
      byte var6;
      if(var1 != 1 && var4 != 1) {
         if(var1 != 2 && var4 != 2) {
            var6 = 0;
         } else {
            var6 = 2;
         }
      } else {
         var6 = 1;
      }

      if(var3 != null && var2 == 0) {
         DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var3.getLayoutParams();
         if(var5.onScreen == 0.0F) {
            this.dispatchOnDrawerClosed(var3);
         } else if(var5.onScreen == 1.0F) {
            this.dispatchOnDrawerOpened(var3);
         }
      }

      if(var6 != this.mDrawerState) {
         this.mDrawerState = var6;
         if(this.mListeners != null) {
            for(var2 = this.mListeners.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerStateChanged(var6);
            }
         }
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

      private void addChildrenForAccessibility(AccessibilityNodeInfoCompat var1, ViewGroup var2) {
         int var4 = var2.getChildCount();

         for(int var3 = 0; var3 < var4; ++var3) {
            View var5 = var2.getChildAt(var3);
            if(DrawerLayout.includeChildForAccessibility(var5)) {
               var1.addChild(var5);
            }
         }

      }

      private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat var1, AccessibilityNodeInfoCompat var2) {
         Rect var3 = this.mTmpRect;
         var2.getBoundsInParent(var3);
         var1.setBoundsInParent(var3);
         var2.getBoundsInScreen(var3);
         var1.setBoundsInScreen(var3);
         var1.setVisibleToUser(var2.isVisibleToUser());
         var1.setPackageName(var2.getPackageName());
         var1.setClassName(var2.getClassName());
         var1.setContentDescription(var2.getContentDescription());
         var1.setEnabled(var2.isEnabled());
         var1.setClickable(var2.isClickable());
         var1.setFocusable(var2.isFocusable());
         var1.setFocused(var2.isFocused());
         var1.setAccessibilityFocused(var2.isAccessibilityFocused());
         var1.setSelected(var2.isSelected());
         var1.setLongClickable(var2.isLongClickable());
         var1.addAction(var2.getActions());
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         boolean var4;
         if(var2.getEventType() == 32) {
            List var5 = var2.getText();
            View var6 = DrawerLayout.this.findVisibleDrawer();
            if(var6 != null) {
               int var3 = DrawerLayout.this.getDrawerViewAbsoluteGravity(var6);
               CharSequence var7 = DrawerLayout.this.getDrawerTitle(var3);
               if(var7 != null) {
                  var5.add(var7);
               }
            }

            var4 = true;
         } else {
            var4 = super.dispatchPopulateAccessibilityEvent(var1, var2);
         }

         return var4;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(DrawerLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         if(DrawerLayout.CAN_HIDE_DESCENDANTS) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
         } else {
            AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain(var2);
            super.onInitializeAccessibilityNodeInfo(var1, var3);
            var2.setSource(var1);
            ViewParent var4 = ViewCompat.getParentForAccessibility(var1);
            if(var4 instanceof View) {
               var2.setParent((View)var4);
            }

            this.copyNodeInfoNoChildren(var2, var3);
            var3.recycle();
            this.addChildrenForAccessibility(var2, (ViewGroup)var1);
         }

         var2.setClassName(DrawerLayout.class.getName());
         var2.setFocusable(false);
         var2.setFocused(false);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         boolean var4;
         if(!DrawerLayout.CAN_HIDE_DESCENDANTS && !DrawerLayout.includeChildForAccessibility(var2)) {
            var4 = false;
         } else {
            var4 = super.onRequestSendAccessibilityEvent(var1, var2, var3);
         }

         return var4;
      }
   }

   final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         if(!DrawerLayout.includeChildForAccessibility(var1)) {
            var2.setParent((View)null);
         }

      }
   }

   interface DrawerLayoutCompatImpl {
      void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3);

      void configureApplyInsets(View var1);

      void dispatchChildInsets(View var1, Object var2, int var3);

      Drawable getDefaultStatusBarBackground(Context var1);

      int getTopInset(Object var1);
   }

   static class DrawerLayoutCompatImplApi21 implements DrawerLayout.DrawerLayoutCompatImpl {
      public void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3) {
         DrawerLayoutCompatApi21.applyMarginInsets(var1, var2, var3);
      }

      public void configureApplyInsets(View var1) {
         DrawerLayoutCompatApi21.configureApplyInsets(var1);
      }

      public void dispatchChildInsets(View var1, Object var2, int var3) {
         DrawerLayoutCompatApi21.dispatchChildInsets(var1, var2, var3);
      }

      public Drawable getDefaultStatusBarBackground(Context var1) {
         return DrawerLayoutCompatApi21.getDefaultStatusBarBackground(var1);
      }

      public int getTopInset(Object var1) {
         return DrawerLayoutCompatApi21.getTopInset(var1);
      }
   }

   static class DrawerLayoutCompatImplBase implements DrawerLayout.DrawerLayoutCompatImpl {
      public void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3) {
      }

      public void configureApplyInsets(View var1) {
      }

      public void dispatchChildInsets(View var1, Object var2, int var3) {
      }

      public Drawable getDefaultStatusBarBackground(Context var1) {
         return null;
      }

      public int getTopInset(Object var1) {
         return 0;
      }
   }

   public interface DrawerListener {
      void onDrawerClosed(View var1);

      void onDrawerOpened(View var1);

      void onDrawerSlide(View var1, float var2);

      void onDrawerStateChanged(int var1);
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int FLAG_IS_CLOSING = 4;
      private static final int FLAG_IS_OPENED = 1;
      private static final int FLAG_IS_OPENING = 2;
      public int gravity;
      boolean isPeeking;
      float onScreen;
      int openState;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.gravity = 0;
      }

      public LayoutParams(int var1, int var2, int var3) {
         this(var1, var2);
         this.gravity = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         this.gravity = 0;
         TypedArray var3 = var1.obtainStyledAttributes(var2, DrawerLayout.LAYOUT_ATTRS);
         this.gravity = var3.getInt(0, 0);
         var3.recycle();
      }

      public LayoutParams(DrawerLayout.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public DrawerLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new DrawerLayout.SavedState(var1, var2);
         }

         public DrawerLayout.SavedState[] newArray(int var1) {
            return new DrawerLayout.SavedState[var1];
         }
      });
      int lockModeEnd;
      int lockModeLeft;
      int lockModeRight;
      int lockModeStart;
      int openDrawerGravity = 0;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.openDrawerGravity = var1.readInt();
         this.lockModeLeft = var1.readInt();
         this.lockModeRight = var1.readInt();
         this.lockModeStart = var1.readInt();
         this.lockModeEnd = var1.readInt();
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.openDrawerGravity);
         var1.writeInt(this.lockModeLeft);
         var1.writeInt(this.lockModeRight);
         var1.writeInt(this.lockModeStart);
         var1.writeInt(this.lockModeEnd);
      }
   }

   public abstract static class SimpleDrawerListener implements DrawerLayout.DrawerListener {
      public void onDrawerClosed(View var1) {
      }

      public void onDrawerOpened(View var1) {
      }

      public void onDrawerSlide(View var1, float var2) {
      }

      public void onDrawerStateChanged(int var1) {
      }
   }

   private class ViewDragCallback extends ViewDragHelper.Callback {
      private final int mAbsGravity;
      private ViewDragHelper mDragger;
      private final Runnable mPeekRunnable = new Runnable() {
         public void run() {
            ViewDragCallback.this.peekDrawer();
         }
      };

      ViewDragCallback(int var2) {
         this.mAbsGravity = var2;
      }

      private void closeOtherDrawer() {
         byte var1 = 3;
         if(this.mAbsGravity == 3) {
            var1 = 5;
         }

         View var2 = DrawerLayout.this.findDrawerWithGravity(var1);
         if(var2 != null) {
            DrawerLayout.this.closeDrawer(var2);
         }

      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         if(DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            var2 = Math.max(-var1.getWidth(), Math.min(var2, 0));
         } else {
            var3 = DrawerLayout.this.getWidth();
            var2 = Math.max(var3 - var1.getWidth(), Math.min(var2, var3));
         }

         return var2;
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         int var2;
         if(DrawerLayout.this.isDrawerView(var1)) {
            var2 = var1.getWidth();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         View var3;
         if((var1 & 1) == 1) {
            var3 = DrawerLayout.this.findDrawerWithGravity(3);
         } else {
            var3 = DrawerLayout.this.findDrawerWithGravity(5);
         }

         if(var3 != null && DrawerLayout.this.getDrawerLockMode(var3) == 0) {
            this.mDragger.captureChildView(var3, var2);
         }

      }

      public boolean onEdgeLock(int var1) {
         return false;
      }

      public void onEdgeTouched(int var1, int var2) {
         DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
      }

      public void onViewCaptured(View var1, int var2) {
         ((DrawerLayout.LayoutParams)var1.getLayoutParams()).isPeeking = false;
         this.closeOtherDrawer();
      }

      public void onViewDragStateChanged(int var1) {
         DrawerLayout.this.updateDrawerState(this.mAbsGravity, var1, this.mDragger.getCapturedView());
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         var3 = var1.getWidth();
         float var6;
         if(DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            var6 = (float)(var3 + var2) / (float)var3;
         } else {
            var6 = (float)(DrawerLayout.this.getWidth() - var2) / (float)var3;
         }

         DrawerLayout.this.setDrawerViewOffset(var1, var6);
         byte var7;
         if(var6 == 0.0F) {
            var7 = 4;
         } else {
            var7 = 0;
         }

         var1.setVisibility(var7);
         DrawerLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         var3 = DrawerLayout.this.getDrawerViewOffset(var1);
         int var5 = var1.getWidth();
         int var4;
         if(DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            if(var2 <= 0.0F && (var2 != 0.0F || var3 <= 0.5F)) {
               var4 = -var5;
            } else {
               var4 = 0;
            }
         } else {
            var4 = DrawerLayout.this.getWidth();
            if(var2 < 0.0F || var2 == 0.0F && var3 > 0.5F) {
               var4 -= var5;
            }
         }

         this.mDragger.settleCapturedViewAt(var4, var1.getTop());
         DrawerLayout.this.invalidate();
      }

      void peekDrawer() {
         int var2 = 0;
         int var3 = this.mDragger.getEdgeSize();
         boolean var1;
         if(this.mAbsGravity == 3) {
            var1 = true;
         } else {
            var1 = false;
         }

         View var4;
         if(var1) {
            var4 = DrawerLayout.this.findDrawerWithGravity(3);
            if(var4 != null) {
               var2 = -var4.getWidth();
            }

            var2 += var3;
         } else {
            var4 = DrawerLayout.this.findDrawerWithGravity(5);
            var2 = DrawerLayout.this.getWidth() - var3;
         }

         if(var4 != null && (var1 && var4.getLeft() < var2 || !var1 && var4.getLeft() > var2) && DrawerLayout.this.getDrawerLockMode(var4) == 0) {
            DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var4.getLayoutParams();
            this.mDragger.smoothSlideViewTo(var4, var2, var4.getTop());
            var5.isPeeking = true;
            DrawerLayout.this.invalidate();
            this.closeOtherDrawer();
            DrawerLayout.this.cancelChildViewTouch();
         }

      }

      public void removeCallbacks() {
         DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
      }

      public void setDragger(ViewDragHelper var1) {
         this.mDragger = var1;
      }

      public boolean tryCaptureView(View var1, int var2) {
         boolean var3;
         if(DrawerLayout.this.isDrawerView(var1) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(var1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
