package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class ButtonBarLayout extends LinearLayout {
   private static final int ALLOW_STACKING_MIN_HEIGHT_DP = 320;
   private boolean mAllowStacking;
   private int mLastWidthSize = -1;

   public ButtonBarLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      boolean var3;
      if(ConfigurationHelper.getScreenHeightDp(this.getResources()) >= 320) {
         var3 = true;
      } else {
         var3 = false;
      }

      TypedArray var4 = var1.obtainStyledAttributes(var2, R.styleable.ButtonBarLayout);
      this.mAllowStacking = var4.getBoolean(R.styleable.ButtonBarLayout_allowStacking, var3);
      var4.recycle();
   }

   private boolean isStacked() {
      boolean var1 = true;
      if(this.getOrientation() != 1) {
         var1 = false;
      }

      return var1;
   }

   private void setStacked(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      this.setOrientation(var2);
      if(var1) {
         var2 = 5;
      } else {
         var2 = 80;
      }

      this.setGravity(var2);
      View var3 = this.findViewById(R.id.spacer);
      if(var3 != null) {
         if(var1) {
            var2 = 8;
         } else {
            var2 = 4;
         }

         var3.setVisibility(var2);
      }

      for(int var4 = this.getChildCount() - 2; var4 >= 0; --var4) {
         this.bringChildToFront(this.getChildAt(var4));
      }

   }

   protected void onMeasure(int var1, int var2) {
      boolean var6 = false;
      boolean var7 = false;
      int var8 = MeasureSpec.getSize(var1);
      if(this.mAllowStacking) {
         if(var8 > this.mLastWidthSize && this.isStacked()) {
            this.setStacked(false);
         }

         this.mLastWidthSize = var8;
      }

      boolean var3 = false;
      int var4;
      if(!this.isStacked() && MeasureSpec.getMode(var1) == 1073741824) {
         var4 = MeasureSpec.makeMeasureSpec(var8, Integer.MIN_VALUE);
         var3 = true;
      } else {
         var4 = var1;
      }

      super.onMeasure(var4, var2);
      boolean var5 = var3;
      if(this.mAllowStacking) {
         var5 = var3;
         if(!this.isStacked()) {
            boolean var9;
            if(VERSION.SDK_INT >= 11) {
               var9 = var7;
               if((ViewCompat.getMeasuredWidthAndState(this) & -16777216) == 16777216) {
                  var9 = true;
               }
            } else {
               int var10 = 0;
               var4 = 0;

               for(int var11 = this.getChildCount(); var4 < var11; ++var4) {
                  var10 += this.getChildAt(var4).getMeasuredWidth();
               }

               var9 = var6;
               if(this.getPaddingLeft() + var10 + this.getPaddingRight() > var8) {
                  var9 = true;
               }
            }

            var5 = var3;
            if(var9) {
               this.setStacked(true);
               var5 = true;
            }
         }
      }

      if(var5) {
         super.onMeasure(var1, var2);
      }

   }

   public void setAllowStacking(boolean var1) {
      if(this.mAllowStacking != var1) {
         this.mAllowStacking = var1;
         if(!this.mAllowStacking && this.getOrientation() == 1) {
            this.setStacked(false);
         }

         this.requestLayout();
      }

   }
}
