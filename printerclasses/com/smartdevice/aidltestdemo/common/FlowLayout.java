package com.smartdevice.aidltestdemo.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
   private List mAllChildViews;
   private List mLineHeight;

   public FlowLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public FlowLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public FlowLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAllChildViews = new ArrayList();
      this.mLineHeight = new ArrayList();
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return new MarginLayoutParams(this.getContext(), var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mAllChildViews.clear();
      this.mLineHeight.clear();
      int var8 = this.getWidth();
      var2 = 0;
      var3 = 0;
      ArrayList var11 = new ArrayList();
      int var7 = this.getChildCount();

      int var6;
      int var9;
      ArrayList var12;
      for(var4 = 0; var4 < var7; var11 = var12) {
         View var13 = this.getChildAt(var4);
         MarginLayoutParams var14 = (MarginLayoutParams)var13.getLayoutParams();
         int var10 = var13.getMeasuredWidth();
         var9 = var13.getMeasuredHeight();
         var5 = var3;
         var12 = var11;
         var6 = var2;
         if(var10 + var2 + var14.leftMargin + var14.rightMargin > var8) {
            this.mLineHeight.add(Integer.valueOf(var3));
            this.mAllChildViews.add(var11);
            var6 = 0;
            var5 = var14.topMargin + var9 + var14.bottomMargin;
            var12 = new ArrayList();
         }

         var2 = var6 + var14.leftMargin + var10 + var14.rightMargin;
         var3 = Math.max(var5, var14.topMargin + var9 + var14.bottomMargin);
         var12.add(var13);
         ++var4;
      }

      this.mLineHeight.add(Integer.valueOf(var3));
      this.mAllChildViews.add(var11);
      var3 = 0;
      var6 = this.mAllChildViews.size();
      var2 = 0;

      while(true) {
         var5 = 0;
         if(var2 >= var6) {
            return;
         }

         List var15 = (List)this.mAllChildViews.get(var2);
         var7 = ((Integer)this.mLineHeight.get(var2)).intValue();

         for(var4 = 0; var4 < var15.size(); ++var4) {
            View var16 = (View)var15.get(var4);
            if(var16.getVisibility() != 8) {
               MarginLayoutParams var17 = (MarginLayoutParams)var16.getLayoutParams();
               var9 = var5 + var17.leftMargin;
               var8 = var3 + var17.topMargin;
               var16.layout(var9, var8, var9 + var16.getMeasuredWidth(), var8 + var16.getMeasuredHeight());
               var5 += var16.getMeasuredWidth() + var17.leftMargin + var17.rightMargin;
            }
         }

         var3 += var7;
         ++var2;
      }
   }

   protected void onMeasure(int var1, int var2) {
      int var11 = MeasureSpec.getSize(var1);
      int var14 = MeasureSpec.getMode(var1);
      int var10 = MeasureSpec.getSize(var2);
      int var12 = MeasureSpec.getMode(var2);
      int var4 = 0;
      int var3 = 0;
      int var5 = 0;
      int var6 = 0;
      int var13 = this.getChildCount();

      int var8;
      for(int var9 = 0; var9 < var13; var3 = var8) {
         View var15 = this.getChildAt(var9);
         this.measureChild(var15, var1, var2);
         MarginLayoutParams var16 = (MarginLayoutParams)this.getLayoutParams();
         var8 = var15.getMeasuredWidth() + var16.leftMargin + var16.rightMargin;
         int var7 = var15.getMeasuredHeight() + var16.topMargin + var16.bottomMargin;
         if(var5 + var8 > var11) {
            var4 = Math.max(var4, var5);
            var5 = var8;
            var3 += var6;
            var6 = var7;
            var7 = var4;
         } else {
            var5 += var8;
            var6 = Math.max(var6, var7);
            var7 = var4;
         }

         var8 = var3;
         var4 = var7;
         if(var9 == var13 - 1) {
            var4 = Math.max(var7, var5);
            var8 = var3 + var6;
         }

         ++var9;
      }

      if(var14 == 1073741824) {
         var4 = var11;
      }

      if(var12 == 1073741824) {
         var3 = var10;
      }

      this.setMeasuredDimension(var4, var3);
      super.onMeasure(var1, var2);
   }
}
