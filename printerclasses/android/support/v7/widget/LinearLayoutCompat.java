package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
   public static final int HORIZONTAL = 0;
   private static final int INDEX_BOTTOM = 2;
   private static final int INDEX_CENTER_VERTICAL = 0;
   private static final int INDEX_FILL = 3;
   private static final int INDEX_TOP = 1;
   public static final int SHOW_DIVIDER_BEGINNING = 1;
   public static final int SHOW_DIVIDER_END = 4;
   public static final int SHOW_DIVIDER_MIDDLE = 2;
   public static final int SHOW_DIVIDER_NONE = 0;
   public static final int VERTICAL = 1;
   private static final int VERTICAL_GRAVITY_COUNT = 4;
   private boolean mBaselineAligned;
   private int mBaselineAlignedChildIndex;
   private int mBaselineChildTop;
   private Drawable mDivider;
   private int mDividerHeight;
   private int mDividerPadding;
   private int mDividerWidth;
   private int mGravity;
   private int[] mMaxAscent;
   private int[] mMaxDescent;
   private int mOrientation;
   private int mShowDividers;
   private int mTotalLength;
   private boolean mUseLargestChild;
   private float mWeightSum;

   public LinearLayoutCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public LinearLayoutCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mBaselineAligned = true;
      this.mBaselineAlignedChildIndex = -1;
      this.mBaselineChildTop = 0;
      this.mGravity = 8388659;
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.LinearLayoutCompat, var3, 0);
      var3 = var5.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
      if(var3 >= 0) {
         this.setOrientation(var3);
      }

      var3 = var5.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
      if(var3 >= 0) {
         this.setGravity(var3);
      }

      boolean var4 = var5.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
      if(!var4) {
         this.setBaselineAligned(var4);
      }

      this.mWeightSum = var5.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
      this.mBaselineAlignedChildIndex = var5.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
      this.mUseLargestChild = var5.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
      this.setDividerDrawable(var5.getDrawable(R.styleable.LinearLayoutCompat_divider));
      this.mShowDividers = var5.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
      this.mDividerPadding = var5.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
      var5.recycle();
   }

   private void forceUniformHeight(int var1, int var2) {
      int var4 = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);

      for(int var3 = 0; var3 < var1; ++var3) {
         View var6 = this.getVirtualChildAt(var3);
         if(var6.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var7 = (LinearLayoutCompat.LayoutParams)var6.getLayoutParams();
            if(var7.height == -1) {
               int var5 = var7.width;
               var7.width = var6.getMeasuredWidth();
               this.measureChildWithMargins(var6, var2, 0, var4, 0);
               var7.width = var5;
            }
         }
      }

   }

   private void forceUniformWidth(int var1, int var2) {
      int var4 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);

      for(int var3 = 0; var3 < var1; ++var3) {
         View var7 = this.getVirtualChildAt(var3);
         if(var7.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var6 = (LinearLayoutCompat.LayoutParams)var7.getLayoutParams();
            if(var6.width == -1) {
               int var5 = var6.height;
               var6.height = var7.getMeasuredHeight();
               this.measureChildWithMargins(var7, var4, 0, var2, 0);
               var6.height = var5;
            }
         }
      }

   }

   private void setChildFrame(View var1, int var2, int var3, int var4, int var5) {
      var1.layout(var2, var3, var2 + var4, var3 + var5);
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof LinearLayoutCompat.LayoutParams;
   }

   void drawDividersHorizontal(Canvas var1) {
      int var4 = this.getVirtualChildCount();
      boolean var5 = ViewUtils.isLayoutRtl(this);

      int var2;
      LinearLayoutCompat.LayoutParams var6;
      View var7;
      for(var2 = 0; var2 < var4; ++var2) {
         var7 = this.getVirtualChildAt(var2);
         if(var7 != null && var7.getVisibility() != 8 && this.hasDividerBeforeChildAt(var2)) {
            var6 = (LinearLayoutCompat.LayoutParams)var7.getLayoutParams();
            int var3;
            if(var5) {
               var3 = var7.getRight() + var6.rightMargin;
            } else {
               var3 = var7.getLeft() - var6.leftMargin - this.mDividerWidth;
            }

            this.drawVerticalDivider(var1, var3);
         }
      }

      if(this.hasDividerBeforeChildAt(var4)) {
         var7 = this.getVirtualChildAt(var4 - 1);
         if(var7 == null) {
            if(var5) {
               var2 = this.getPaddingLeft();
            } else {
               var2 = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
            }
         } else {
            var6 = (LinearLayoutCompat.LayoutParams)var7.getLayoutParams();
            if(var5) {
               var2 = var7.getLeft() - var6.leftMargin - this.mDividerWidth;
            } else {
               var2 = var7.getRight() + var6.rightMargin;
            }
         }

         this.drawVerticalDivider(var1, var2);
      }

   }

   void drawDividersVertical(Canvas var1) {
      int var3 = this.getVirtualChildCount();

      int var2;
      LinearLayoutCompat.LayoutParams var4;
      View var5;
      for(var2 = 0; var2 < var3; ++var2) {
         var5 = this.getVirtualChildAt(var2);
         if(var5 != null && var5.getVisibility() != 8 && this.hasDividerBeforeChildAt(var2)) {
            var4 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            this.drawHorizontalDivider(var1, var5.getTop() - var4.topMargin - this.mDividerHeight);
         }
      }

      if(this.hasDividerBeforeChildAt(var3)) {
         var5 = this.getVirtualChildAt(var3 - 1);
         if(var5 == null) {
            var2 = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
         } else {
            var4 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            var2 = var5.getBottom() + var4.bottomMargin;
         }

         this.drawHorizontalDivider(var1, var2);
      }

   }

   void drawHorizontalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, var2, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + var2);
      this.mDivider.draw(var1);
   }

   void drawVerticalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(var2, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + var2, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
      this.mDivider.draw(var1);
   }

   protected LinearLayoutCompat.LayoutParams generateDefaultLayoutParams() {
      LinearLayoutCompat.LayoutParams var1;
      if(this.mOrientation == 0) {
         var1 = new LinearLayoutCompat.LayoutParams(-2, -2);
      } else if(this.mOrientation == 1) {
         var1 = new LinearLayoutCompat.LayoutParams(-1, -2);
      } else {
         var1 = null;
      }

      return var1;
   }

   public LinearLayoutCompat.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new LinearLayoutCompat.LayoutParams(this.getContext(), var1);
   }

   protected LinearLayoutCompat.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new LinearLayoutCompat.LayoutParams(var1);
   }

   public int getBaseline() {
      int var1 = -1;
      if(this.mBaselineAlignedChildIndex < 0) {
         var1 = super.getBaseline();
      } else {
         if(this.getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
         }

         View var5 = this.getChildAt(this.mBaselineAlignedChildIndex);
         int var3 = var5.getBaseline();
         if(var3 == -1) {
            if(this.mBaselineAlignedChildIndex != 0) {
               throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn\'t know how to get its baseline.");
            }
         } else {
            int var2 = this.mBaselineChildTop;
            var1 = var2;
            if(this.mOrientation == 1) {
               int var4 = this.mGravity & 112;
               var1 = var2;
               if(var4 != 48) {
                  switch(var4) {
                  case 16:
                     var1 = var2 + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                     break;
                  case 80:
                     var1 = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                     break;
                  default:
                     var1 = var2;
                  }
               }
            }

            var1 = ((LinearLayoutCompat.LayoutParams)var5.getLayoutParams()).topMargin + var1 + var3;
         }
      }

      return var1;
   }

   public int getBaselineAlignedChildIndex() {
      return this.mBaselineAlignedChildIndex;
   }

   int getChildrenSkipCount(View var1, int var2) {
      return 0;
   }

   public Drawable getDividerDrawable() {
      return this.mDivider;
   }

   public int getDividerPadding() {
      return this.mDividerPadding;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public int getDividerWidth() {
      return this.mDividerWidth;
   }

   int getLocationOffset(View var1) {
      return 0;
   }

   int getNextLocationOffset(View var1) {
      return 0;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getShowDividers() {
      return this.mShowDividers;
   }

   View getVirtualChildAt(int var1) {
      return this.getChildAt(var1);
   }

   int getVirtualChildCount() {
      return this.getChildCount();
   }

   public float getWeightSum() {
      return this.mWeightSum;
   }

   protected boolean hasDividerBeforeChildAt(int var1) {
      boolean var2 = true;
      if(var1 == 0) {
         if((this.mShowDividers & 1) == 0) {
            var2 = false;
         }
      } else if(var1 == this.getChildCount()) {
         if((this.mShowDividers & 4) == 0) {
            var2 = false;
         }
      } else if((this.mShowDividers & 2) != 0) {
         boolean var3 = false;
         --var1;

         while(true) {
            var2 = var3;
            if(var1 < 0) {
               break;
            }

            if(this.getChildAt(var1).getVisibility() != 8) {
               var2 = true;
               break;
            }

            --var1;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isBaselineAligned() {
      return this.mBaselineAligned;
   }

   public boolean isMeasureWithLargestChildEnabled() {
      return this.mUseLargestChild;
   }

   void layoutHorizontal(int var1, int var2, int var3, int var4) {
      boolean var18 = ViewUtils.isLayoutRtl(this);
      int var7 = this.getPaddingTop();
      int var10 = var4 - var2;
      int var12 = this.getPaddingBottom();
      int var11 = this.getPaddingBottom();
      int var9 = this.getVirtualChildCount();
      var2 = this.mGravity;
      int var13 = this.mGravity;
      boolean var17 = this.mBaselineAligned;
      int[] var20 = this.mMaxAscent;
      int[] var21 = this.mMaxDescent;
      switch(GravityCompat.getAbsoluteGravity(var2 & 8388615, ViewCompat.getLayoutDirection(this))) {
      case 1:
         var1 = this.getPaddingLeft() + (var3 - var1 - this.mTotalLength) / 2;
         break;
      case 5:
         var1 = this.getPaddingLeft() + var3 - var1 - this.mTotalLength;
         break;
      default:
         var1 = this.getPaddingLeft();
      }

      var4 = 0;
      byte var5 = 1;
      if(var18) {
         var4 = var9 - 1;
         var5 = -1;
      }

      var2 = 0;

      for(var3 = var1; var2 < var9; var3 = var1) {
         int var14 = var4 + var5 * var2;
         View var22 = this.getVirtualChildAt(var14);
         int var6;
         if(var22 == null) {
            var1 = var3 + this.measureNullChild(var14);
            var6 = var2;
         } else {
            var1 = var3;
            var6 = var2;
            if(var22.getVisibility() != 8) {
               int var16 = var22.getMeasuredWidth();
               int var15 = var22.getMeasuredHeight();
               byte var23 = -1;
               LinearLayoutCompat.LayoutParams var19 = (LinearLayoutCompat.LayoutParams)var22.getLayoutParams();
               var6 = var23;
               if(var17) {
                  var6 = var23;
                  if(var19.height != -1) {
                     var6 = var22.getBaseline();
                  }
               }

               int var8 = var19.gravity;
               var1 = var8;
               if(var8 < 0) {
                  var1 = var13 & 112;
               }

               switch(var1 & 112) {
               case 16:
                  var1 = (var10 - var7 - var11 - var15) / 2 + var7 + var19.topMargin - var19.bottomMargin;
                  break;
               case 48:
                  var8 = var7 + var19.topMargin;
                  var1 = var8;
                  if(var6 != -1) {
                     var1 = var8 + (var20[1] - var6);
                  }
                  break;
               case 80:
                  var8 = var10 - var12 - var15 - var19.bottomMargin;
                  var1 = var8;
                  if(var6 != -1) {
                     var1 = var22.getMeasuredHeight();
                     var1 = var8 - (var21[2] - (var1 - var6));
                  }
                  break;
               default:
                  var1 = var7;
               }

               var6 = var3;
               if(this.hasDividerBeforeChildAt(var14)) {
                  var6 = var3 + this.mDividerWidth;
               }

               var3 = var6 + var19.leftMargin;
               this.setChildFrame(var22, var3 + this.getLocationOffset(var22), var1, var16, var15);
               var1 = var3 + var19.rightMargin + var16 + this.getNextLocationOffset(var22);
               var6 = var2 + this.getChildrenSkipCount(var22, var14);
            }
         }

         var2 = var6 + 1;
      }

   }

   void layoutVertical(int var1, int var2, int var3, int var4) {
      int var5 = this.getPaddingLeft();
      int var10 = var3 - var1;
      int var9 = this.getPaddingRight();
      int var6 = this.getPaddingRight();
      int var8 = this.getVirtualChildCount();
      var1 = this.mGravity;
      int var7 = this.mGravity;
      switch(var1 & 112) {
      case 16:
         var1 = this.getPaddingTop() + (var4 - var2 - this.mTotalLength) / 2;
         break;
      case 80:
         var1 = this.getPaddingTop() + var4 - var2 - this.mTotalLength;
         break;
      default:
         var1 = this.getPaddingTop();
      }

      for(var2 = 0; var2 < var8; var1 = var3) {
         View var14 = this.getVirtualChildAt(var2);
         if(var14 == null) {
            var3 = var1 + this.measureNullChild(var2);
            var4 = var2;
         } else {
            var3 = var1;
            var4 = var2;
            if(var14.getVisibility() != 8) {
               int var12 = var14.getMeasuredWidth();
               int var11 = var14.getMeasuredHeight();
               LinearLayoutCompat.LayoutParams var13 = (LinearLayoutCompat.LayoutParams)var14.getLayoutParams();
               var4 = var13.gravity;
               var3 = var4;
               if(var4 < 0) {
                  var3 = var7 & 8388615;
               }

               switch(GravityCompat.getAbsoluteGravity(var3, ViewCompat.getLayoutDirection(this)) & 7) {
               case 1:
                  var3 = (var10 - var5 - var6 - var12) / 2 + var5 + var13.leftMargin - var13.rightMargin;
                  break;
               case 5:
                  var3 = var10 - var9 - var12 - var13.rightMargin;
                  break;
               default:
                  var3 = var5 + var13.leftMargin;
               }

               var4 = var1;
               if(this.hasDividerBeforeChildAt(var2)) {
                  var4 = var1 + this.mDividerHeight;
               }

               var1 = var4 + var13.topMargin;
               this.setChildFrame(var14, var3, var1 + this.getLocationOffset(var14), var12, var11);
               var3 = var1 + var13.bottomMargin + var11 + this.getNextLocationOffset(var14);
               var4 = var2 + this.getChildrenSkipCount(var14, var2);
            }
         }

         var2 = var4 + 1;
      }

   }

   void measureChildBeforeLayout(View var1, int var2, int var3, int var4, int var5, int var6) {
      this.measureChildWithMargins(var1, var3, var4, var5, var6);
   }

   void measureHorizontal(int var1, int var2) {
      this.mTotalLength = 0;
      int var13 = 0;
      int var8 = 0;
      int var6 = 0;
      int var10 = 0;
      boolean var7 = true;
      float var3 = 0.0F;
      int var21 = this.getVirtualChildCount();
      int var23 = MeasureSpec.getMode(var1);
      int var22 = MeasureSpec.getMode(var2);
      boolean var9 = false;
      boolean var14 = false;
      if(this.mMaxAscent == null || this.mMaxDescent == null) {
         this.mMaxAscent = new int[4];
         this.mMaxDescent = new int[4];
      }

      int[] var28 = this.mMaxAscent;
      int[] var29 = this.mMaxDescent;
      var28[3] = -1;
      var28[2] = -1;
      var28[1] = -1;
      var28[0] = -1;
      var29[3] = -1;
      var29[2] = -1;
      var29[1] = -1;
      var29[0] = -1;
      boolean var26 = this.mBaselineAligned;
      boolean var27 = this.mUseLargestChild;
      boolean var17;
      if(var23 == 1073741824) {
         var17 = true;
      } else {
         var17 = false;
      }

      int var12 = Integer.MIN_VALUE;

      int var11;
      int var15;
      int var16;
      int var18;
      int var19;
      LinearLayoutCompat.LayoutParams var30;
      View var31;
      boolean var37;
      for(var11 = 0; var11 < var21; var12 = var16) {
         var31 = this.getVirtualChildAt(var11);
         if(var31 == null) {
            this.mTotalLength += this.measureNullChild(var11);
            var16 = var12;
         } else if(var31.getVisibility() == 8) {
            var11 += this.getChildrenSkipCount(var31, var11);
            var16 = var12;
         } else {
            if(this.hasDividerBeforeChildAt(var11)) {
               this.mTotalLength += this.mDividerWidth;
            }

            var30 = (LinearLayoutCompat.LayoutParams)var31.getLayoutParams();
            var3 += var30.weight;
            if(var23 == 1073741824 && var30.width == 0 && var30.weight > 0.0F) {
               if(var17) {
                  this.mTotalLength += var30.leftMargin + var30.rightMargin;
               } else {
                  var15 = this.mTotalLength;
                  this.mTotalLength = Math.max(var15, var30.leftMargin + var15 + var30.rightMargin);
               }

               if(var26) {
                  var15 = MeasureSpec.makeMeasureSpec(0, 0);
                  var31.measure(var15, var15);
                  var37 = var14;
                  var16 = var12;
               } else {
                  var37 = true;
                  var16 = var12;
               }
            } else {
               var16 = Integer.MIN_VALUE;
               var15 = var16;
               if(var30.width == 0) {
                  var15 = var16;
                  if(var30.weight > 0.0F) {
                     var15 = 0;
                     var30.width = -2;
                  }
               }

               if(var3 == 0.0F) {
                  var16 = this.mTotalLength;
               } else {
                  var16 = 0;
               }

               this.measureChildBeforeLayout(var31, var11, var1, var16, var2, 0);
               if(var15 != Integer.MIN_VALUE) {
                  var30.width = var15;
               }

               var18 = var31.getMeasuredWidth();
               if(var17) {
                  this.mTotalLength += var30.leftMargin + var18 + var30.rightMargin + this.getNextLocationOffset(var31);
               } else {
                  var15 = this.mTotalLength;
                  this.mTotalLength = Math.max(var15, var15 + var18 + var30.leftMargin + var30.rightMargin + this.getNextLocationOffset(var31));
               }

               var16 = var12;
               var37 = var14;
               if(var27) {
                  var16 = Math.max(var18, var12);
                  var37 = var14;
               }
            }

            boolean var38 = false;
            boolean var35 = var9;
            var14 = var38;
            if(var22 != 1073741824) {
               var35 = var9;
               var14 = var38;
               if(var30.height == -1) {
                  var35 = true;
                  var14 = true;
               }
            }

            int var33 = var30.topMargin + var30.bottomMargin;
            var18 = var31.getMeasuredHeight() + var33;
            var19 = ViewUtils.combineMeasuredStates(var8, ViewCompat.getMeasuredState(var31));
            if(var26) {
               int var20 = var31.getBaseline();
               if(var20 != -1) {
                  if(var30.gravity < 0) {
                     var8 = this.mGravity;
                  } else {
                     var8 = var30.gravity;
                  }

                  var8 = ((var8 & 112) >> 4 & -2) >> 1;
                  var28[var8] = Math.max(var28[var8], var20);
                  var29[var8] = Math.max(var29[var8], var18 - var20);
               }
            }

            var13 = Math.max(var13, var18);
            if(var7 && var30.height == -1) {
               var7 = true;
            } else {
               var7 = false;
            }

            if(var30.weight > 0.0F) {
               if(!var14) {
                  var33 = var18;
               }

               var10 = Math.max(var10, var33);
            } else {
               if(!var14) {
                  var33 = var18;
               }

               var6 = Math.max(var6, var33);
            }

            var11 += this.getChildrenSkipCount(var31, var11);
            var8 = var19;
            var9 = var35;
            var14 = var37;
         }

         ++var11;
      }

      if(this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var21)) {
         this.mTotalLength += this.mDividerWidth;
      }

      label268: {
         if(var28[1] == -1 && var28[0] == -1 && var28[2] == -1) {
            var11 = var13;
            if(var28[3] == -1) {
               break label268;
            }
         }

         var11 = Math.max(var13, Math.max(var28[3], Math.max(var28[0], Math.max(var28[1], var28[2]))) + Math.max(var29[3], Math.max(var29[0], Math.max(var29[1], var29[2]))));
      }

      if(var27 && (var23 == Integer.MIN_VALUE || var23 == 0)) {
         this.mTotalLength = 0;

         for(var13 = 0; var13 < var21; ++var13) {
            var31 = this.getVirtualChildAt(var13);
            if(var31 == null) {
               this.mTotalLength += this.measureNullChild(var13);
            } else if(var31.getVisibility() == 8) {
               var13 += this.getChildrenSkipCount(var31, var13);
            } else {
               var30 = (LinearLayoutCompat.LayoutParams)var31.getLayoutParams();
               if(var17) {
                  this.mTotalLength += var30.leftMargin + var12 + var30.rightMargin + this.getNextLocationOffset(var31);
               } else {
                  var15 = this.mTotalLength;
                  this.mTotalLength = Math.max(var15, var15 + var12 + var30.leftMargin + var30.rightMargin + this.getNextLocationOffset(var31));
               }
            }
         }
      }

      this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
      int var24 = ViewCompat.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), var1, 0);
      var13 = (var24 & 16777215) - this.mTotalLength;
      int var36;
      if(!var14 && (var13 == 0 || var3 <= 0.0F)) {
         var16 = Math.max(var6, var10);
         var37 = var7;
         var13 = var16;
         var36 = var8;
         var6 = var11;
         if(var27) {
            var37 = var7;
            var13 = var16;
            var36 = var8;
            var6 = var11;
            if(var23 != 1073741824) {
               var10 = 0;

               while(true) {
                  var37 = var7;
                  var13 = var16;
                  var36 = var8;
                  var6 = var11;
                  if(var10 >= var21) {
                     break;
                  }

                  View var40 = this.getVirtualChildAt(var10);
                  if(var40 != null && var40.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var40.getLayoutParams()).weight > 0.0F) {
                     var40.measure(MeasureSpec.makeMeasureSpec(var12, 1073741824), MeasureSpec.makeMeasureSpec(var40.getMeasuredHeight(), 1073741824));
                  }

                  ++var10;
               }
            }
         }
      } else {
         label327: {
            if(this.mWeightSum > 0.0F) {
               var3 = this.mWeightSum;
            }

            var28[3] = -1;
            var28[2] = -1;
            var28[1] = -1;
            var28[0] = -1;
            var29[3] = -1;
            var29[2] = -1;
            var29[1] = -1;
            var29[0] = -1;
            var11 = -1;
            this.mTotalLength = 0;
            var36 = 0;

            float var5;
            for(var10 = var6; var36 < var21; var3 = var5) {
               View var41 = this.getVirtualChildAt(var36);
               boolean var39 = var7;
               var15 = var10;
               var19 = var8;
               var16 = var13;
               var18 = var11;
               var5 = var3;
               if(var41 != null) {
                  if(var41.getVisibility() == 8) {
                     var5 = var3;
                     var18 = var11;
                     var16 = var13;
                     var19 = var8;
                     var15 = var10;
                     var39 = var7;
                  } else {
                     LinearLayoutCompat.LayoutParams var42 = (LinearLayoutCompat.LayoutParams)var41.getLayoutParams();
                     var5 = var42.weight;
                     var12 = var8;
                     var6 = var13;
                     float var4 = var3;
                     if(var5 > 0.0F) {
                        var6 = (int)((float)var13 * var5 / var3);
                        var4 = var3 - var5;
                        var12 = var13 - var6;
                        var15 = getChildMeasureSpec(var2, this.getPaddingTop() + this.getPaddingBottom() + var42.topMargin + var42.bottomMargin, var42.height);
                        if(var42.width == 0 && var23 == 1073741824) {
                           if(var6 <= 0) {
                              var6 = 0;
                           }

                           var41.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), var15);
                        } else {
                           var13 = var41.getMeasuredWidth() + var6;
                           var6 = var13;
                           if(var13 < 0) {
                              var6 = 0;
                           }

                           var41.measure(MeasureSpec.makeMeasureSpec(var6, 1073741824), var15);
                        }

                        var8 = ViewUtils.combineMeasuredStates(var8, ViewCompat.getMeasuredState(var41) & -16777216);
                        var6 = var12;
                        var12 = var8;
                     }

                     if(var17) {
                        this.mTotalLength += var41.getMeasuredWidth() + var42.leftMargin + var42.rightMargin + this.getNextLocationOffset(var41);
                     } else {
                        var8 = this.mTotalLength;
                        this.mTotalLength = Math.max(var8, var41.getMeasuredWidth() + var8 + var42.leftMargin + var42.rightMargin + this.getNextLocationOffset(var41));
                     }

                     boolean var34;
                     if(var22 != 1073741824 && var42.height == -1) {
                        var34 = true;
                     } else {
                        var34 = false;
                     }

                     var15 = var42.topMargin + var42.bottomMargin;
                     var13 = var41.getMeasuredHeight() + var15;
                     var11 = Math.max(var11, var13);
                     if(var34) {
                        var8 = var15;
                     } else {
                        var8 = var13;
                     }

                     var10 = Math.max(var10, var8);
                     if(var7 && var42.height == -1) {
                        var7 = true;
                     } else {
                        var7 = false;
                     }

                     var39 = var7;
                     var15 = var10;
                     var19 = var12;
                     var16 = var6;
                     var18 = var11;
                     var5 = var4;
                     if(var26) {
                        int var25 = var41.getBaseline();
                        var39 = var7;
                        var15 = var10;
                        var19 = var12;
                        var16 = var6;
                        var18 = var11;
                        var5 = var4;
                        if(var25 != -1) {
                           if(var42.gravity < 0) {
                              var8 = this.mGravity;
                           } else {
                              var8 = var42.gravity;
                           }

                           var8 = ((var8 & 112) >> 4 & -2) >> 1;
                           var28[var8] = Math.max(var28[var8], var25);
                           var29[var8] = Math.max(var29[var8], var13 - var25);
                           var39 = var7;
                           var15 = var10;
                           var19 = var12;
                           var16 = var6;
                           var18 = var11;
                           var5 = var4;
                        }
                     }
                  }
               }

               ++var36;
               var7 = var39;
               var10 = var15;
               var8 = var19;
               var13 = var16;
               var11 = var18;
            }

            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            if(var28[1] == -1 && var28[0] == -1 && var28[2] == -1) {
               var37 = var7;
               var13 = var10;
               var36 = var8;
               var6 = var11;
               if(var28[3] == -1) {
                  break label327;
               }
            }

            var6 = Math.max(var11, Math.max(var28[3], Math.max(var28[0], Math.max(var28[1], var28[2]))) + Math.max(var29[3], Math.max(var29[0], Math.max(var29[1], var29[2]))));
            var36 = var8;
            var13 = var10;
            var37 = var7;
         }
      }

      int var32 = var6;
      if(!var37) {
         var32 = var6;
         if(var22 != 1073741824) {
            var32 = var13;
         }
      }

      this.setMeasuredDimension(-16777216 & var36 | var24, ViewCompat.resolveSizeAndState(Math.max(var32 + this.getPaddingTop() + this.getPaddingBottom(), this.getSuggestedMinimumHeight()), var2, var36 << 16));
      if(var9) {
         this.forceUniformHeight(var21, var1);
      }

   }

   int measureNullChild(int var1) {
      return 0;
   }

   void measureVertical(int var1, int var2) {
      this.mTotalLength = 0;
      int var8 = 0;
      int var9 = 0;
      int var6 = 0;
      int var11 = 0;
      boolean var7 = true;
      float var3 = 0.0F;
      int var18 = this.getVirtualChildCount();
      int var19 = MeasureSpec.getMode(var1);
      int var20 = MeasureSpec.getMode(var2);
      boolean var10 = false;
      boolean var14 = false;
      int var21 = this.mBaselineAlignedChildIndex;
      boolean var22 = this.mUseLargestChild;
      int var12 = Integer.MIN_VALUE;

      int var13;
      int var15;
      int var16;
      int var17;
      View var23;
      LinearLayoutCompat.LayoutParams var24;
      int var30;
      for(var13 = 0; var13 < var18; var12 = var16) {
         var23 = this.getVirtualChildAt(var13);
         if(var23 == null) {
            this.mTotalLength += this.measureNullChild(var13);
            var16 = var12;
         } else if(var23.getVisibility() == 8) {
            var13 += this.getChildrenSkipCount(var23, var13);
            var16 = var12;
         } else {
            if(this.hasDividerBeforeChildAt(var13)) {
               this.mTotalLength += this.mDividerHeight;
            }

            var24 = (LinearLayoutCompat.LayoutParams)var23.getLayoutParams();
            var3 += var24.weight;
            boolean var31;
            if(var20 == 1073741824 && var24.height == 0 && var24.weight > 0.0F) {
               var30 = this.mTotalLength;
               this.mTotalLength = Math.max(var30, var24.topMargin + var30 + var24.bottomMargin);
               var31 = true;
               var16 = var12;
            } else {
               var16 = Integer.MIN_VALUE;
               var15 = var16;
               if(var24.height == 0) {
                  var15 = var16;
                  if(var24.weight > 0.0F) {
                     var15 = 0;
                     var24.height = -2;
                  }
               }

               if(var3 == 0.0F) {
                  var16 = this.mTotalLength;
               } else {
                  var16 = 0;
               }

               this.measureChildBeforeLayout(var23, var13, var1, 0, var2, var16);
               if(var15 != Integer.MIN_VALUE) {
                  var24.height = var15;
               }

               var17 = var23.getMeasuredHeight();
               var15 = this.mTotalLength;
               this.mTotalLength = Math.max(var15, var15 + var17 + var24.topMargin + var24.bottomMargin + this.getNextLocationOffset(var23));
               var16 = var12;
               var31 = var14;
               if(var22) {
                  var16 = Math.max(var17, var12);
                  var31 = var14;
               }
            }

            if(var21 >= 0 && var21 == var13 + 1) {
               this.mBaselineChildTop = this.mTotalLength;
            }

            if(var13 < var21 && var24.weight > 0.0F) {
               throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won\'t work.  Either remove the weight, or don\'t set mBaselineAlignedChildIndex.");
            }

            boolean var32 = false;
            boolean var29 = var10;
            var14 = var32;
            if(var19 != 1073741824) {
               var29 = var10;
               var14 = var32;
               if(var24.width == -1) {
                  var29 = true;
                  var14 = true;
               }
            }

            var17 = var24.leftMargin + var24.rightMargin;
            int var28 = var23.getMeasuredWidth() + var17;
            var8 = Math.max(var8, var28);
            var9 = ViewUtils.combineMeasuredStates(var9, ViewCompat.getMeasuredState(var23));
            if(var7 && var24.width == -1) {
               var7 = true;
            } else {
               var7 = false;
            }

            if(var24.weight > 0.0F) {
               if(var14) {
                  var28 = var17;
               }

               var11 = Math.max(var11, var28);
            } else {
               if(!var14) {
                  var17 = var28;
               }

               var6 = Math.max(var6, var17);
            }

            var13 += this.getChildrenSkipCount(var23, var13);
            var10 = var29;
            var14 = var31;
         }

         ++var13;
      }

      if(this.mTotalLength > 0 && this.hasDividerBeforeChildAt(var18)) {
         this.mTotalLength += this.mDividerHeight;
      }

      if(var22 && (var20 == Integer.MIN_VALUE || var20 == 0)) {
         this.mTotalLength = 0;

         for(var13 = 0; var13 < var18; ++var13) {
            View var34 = this.getVirtualChildAt(var13);
            if(var34 == null) {
               this.mTotalLength += this.measureNullChild(var13);
            } else if(var34.getVisibility() == 8) {
               var13 += this.getChildrenSkipCount(var34, var13);
            } else {
               LinearLayoutCompat.LayoutParams var33 = (LinearLayoutCompat.LayoutParams)var34.getLayoutParams();
               var15 = this.mTotalLength;
               this.mTotalLength = Math.max(var15, var15 + var12 + var33.topMargin + var33.bottomMargin + this.getNextLocationOffset(var34));
            }
         }
      }

      this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
      var17 = ViewCompat.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), var2, 0);
      var13 = (var17 & 16777215) - this.mTotalLength;
      int var26;
      if(!var14 && (var13 == 0 || var3 <= 0.0F)) {
         var16 = Math.max(var6, var11);
         var14 = var7;
         var6 = var16;
         var13 = var9;
         var11 = var8;
         if(var22) {
            var14 = var7;
            var6 = var16;
            var13 = var9;
            var11 = var8;
            if(var20 != 1073741824) {
               var15 = 0;

               while(true) {
                  var14 = var7;
                  var6 = var16;
                  var13 = var9;
                  var11 = var8;
                  if(var15 >= var18) {
                     break;
                  }

                  var23 = this.getVirtualChildAt(var15);
                  if(var23 != null && var23.getVisibility() != 8 && ((LinearLayoutCompat.LayoutParams)var23.getLayoutParams()).weight > 0.0F) {
                     var23.measure(MeasureSpec.makeMeasureSpec(var23.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var12, 1073741824));
                  }

                  ++var15;
               }
            }
         }
      } else {
         float var4;
         if(this.mWeightSum > 0.0F) {
            var4 = this.mWeightSum;
         } else {
            var4 = var3;
         }

         this.mTotalLength = 0;
         var30 = 0;

         for(var11 = var8; var30 < var18; var4 = var3) {
            var23 = this.getVirtualChildAt(var30);
            boolean var25;
            if(var23.getVisibility() == 8) {
               var3 = var4;
               var8 = var13;
               var13 = var6;
               var25 = var7;
            } else {
               var24 = (LinearLayoutCompat.LayoutParams)var23.getLayoutParams();
               float var5 = var24.weight;
               var12 = var9;
               var8 = var13;
               var3 = var4;
               if(var5 > 0.0F) {
                  var8 = (int)((float)var13 * var5 / var4);
                  var3 = var4 - var5;
                  var12 = var13 - var8;
                  var15 = getChildMeasureSpec(var1, this.getPaddingLeft() + this.getPaddingRight() + var24.leftMargin + var24.rightMargin, var24.width);
                  if(var24.height == 0 && var20 == 1073741824) {
                     if(var8 <= 0) {
                        var8 = 0;
                     }

                     var23.measure(var15, MeasureSpec.makeMeasureSpec(var8, 1073741824));
                  } else {
                     var13 = var23.getMeasuredHeight() + var8;
                     var8 = var13;
                     if(var13 < 0) {
                        var8 = 0;
                     }

                     var23.measure(var15, MeasureSpec.makeMeasureSpec(var8, 1073741824));
                  }

                  var9 = ViewUtils.combineMeasuredStates(var9, ViewCompat.getMeasuredState(var23) & -256);
                  var8 = var12;
                  var12 = var9;
               }

               var13 = var24.leftMargin + var24.rightMargin;
               var15 = var23.getMeasuredWidth() + var13;
               var11 = Math.max(var11, var15);
               boolean var27;
               if(var19 != 1073741824 && var24.width == -1) {
                  var27 = true;
               } else {
                  var27 = false;
               }

               if(var27) {
                  var9 = var13;
               } else {
                  var9 = var15;
               }

               var13 = Math.max(var6, var9);
               if(var7 && var24.width == -1) {
                  var25 = true;
               } else {
                  var25 = false;
               }

               var26 = this.mTotalLength;
               this.mTotalLength = Math.max(var26, var23.getMeasuredHeight() + var26 + var24.topMargin + var24.bottomMargin + this.getNextLocationOffset(var23));
               var9 = var12;
            }

            ++var30;
            var7 = var25;
            var6 = var13;
            var13 = var8;
         }

         this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
         var13 = var9;
         var14 = var7;
      }

      var26 = var11;
      if(!var14) {
         var26 = var11;
         if(var19 != 1073741824) {
            var26 = var6;
         }
      }

      this.setMeasuredDimension(ViewCompat.resolveSizeAndState(Math.max(var26 + this.getPaddingLeft() + this.getPaddingRight(), this.getSuggestedMinimumWidth()), var1, var13), var17);
      if(var10) {
         this.forceUniformWidth(var18, var2);
      }

   }

   protected void onDraw(Canvas var1) {
      if(this.mDivider != null) {
         if(this.mOrientation == 1) {
            this.drawDividersVertical(var1);
         } else {
            this.drawDividersHorizontal(var1);
         }
      }

   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if(VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      if(VERSION.SDK_INT >= 14) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(LinearLayoutCompat.class.getName());
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if(this.mOrientation == 1) {
         this.layoutVertical(var2, var3, var4, var5);
      } else {
         this.layoutHorizontal(var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if(this.mOrientation == 1) {
         this.measureVertical(var1, var2);
      } else {
         this.measureHorizontal(var1, var2);
      }

   }

   public void setBaselineAligned(boolean var1) {
      this.mBaselineAligned = var1;
   }

   public void setBaselineAlignedChildIndex(int var1) {
      if(var1 >= 0 && var1 < this.getChildCount()) {
         this.mBaselineAlignedChildIndex = var1;
      } else {
         throw new IllegalArgumentException("base aligned child index out of range (0, " + this.getChildCount() + ")");
      }
   }

   public void setDividerDrawable(Drawable var1) {
      boolean var2 = false;
      if(var1 != this.mDivider) {
         this.mDivider = var1;
         if(var1 != null) {
            this.mDividerWidth = var1.getIntrinsicWidth();
            this.mDividerHeight = var1.getIntrinsicHeight();
         } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
         }

         if(var1 == null) {
            var2 = true;
         }

         this.setWillNotDraw(var2);
         this.requestLayout();
      }

   }

   public void setDividerPadding(int var1) {
      this.mDividerPadding = var1;
   }

   public void setGravity(int var1) {
      if(this.mGravity != var1) {
         int var2 = var1;
         if((8388615 & var1) == 0) {
            var2 = var1 | 8388611;
         }

         var1 = var2;
         if((var2 & 112) == 0) {
            var1 = var2 | 48;
         }

         this.mGravity = var1;
         this.requestLayout();
      }

   }

   public void setHorizontalGravity(int var1) {
      var1 &= 8388615;
      if((this.mGravity & 8388615) != var1) {
         this.mGravity = this.mGravity & -8388616 | var1;
         this.requestLayout();
      }

   }

   public void setMeasureWithLargestChildEnabled(boolean var1) {
      this.mUseLargestChild = var1;
   }

   public void setOrientation(int var1) {
      if(this.mOrientation != var1) {
         this.mOrientation = var1;
         this.requestLayout();
      }

   }

   public void setShowDividers(int var1) {
      if(var1 != this.mShowDividers) {
         this.requestLayout();
      }

      this.mShowDividers = var1;
   }

   public void setVerticalGravity(int var1) {
      var1 &= 112;
      if((this.mGravity & 112) != var1) {
         this.mGravity = this.mGravity & -113 | var1;
         this.requestLayout();
      }

   }

   public void setWeightSum(float var1) {
      this.mWeightSum = Math.max(0.0F, var1);
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface DividerMode {
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int gravity = -1;
      public float weight;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.weight = 0.0F;
      }

      public LayoutParams(int var1, int var2, float var3) {
         super(var1, var2);
         this.weight = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.LinearLayoutCompat_Layout);
         this.weight = var3.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
         this.gravity = var3.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
         var3.recycle();
      }

      public LayoutParams(LinearLayoutCompat.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface OrientationMode {
   }
}
