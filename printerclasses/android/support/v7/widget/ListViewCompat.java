package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class ListViewCompat extends ListView {
   public static final int INVALID_POSITION = -1;
   public static final int NO_POSITION = -1;
   private static final int[] STATE_SET_NOTHING = new int[]{0};
   private Field mIsChildViewEnabled;
   protected int mMotionPosition;
   int mSelectionBottomPadding;
   int mSelectionLeftPadding;
   int mSelectionRightPadding;
   int mSelectionTopPadding;
   private ListViewCompat.GateKeeperDrawable mSelector;
   final Rect mSelectorRect;

   public ListViewCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ListViewCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ListViewCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSelectorRect = new Rect();
      this.mSelectionLeftPadding = 0;
      this.mSelectionTopPadding = 0;
      this.mSelectionRightPadding = 0;
      this.mSelectionBottomPadding = 0;

      try {
         this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
         this.mIsChildViewEnabled.setAccessible(true);
      } catch (NoSuchFieldException var4) {
         var4.printStackTrace();
      }

   }

   protected void dispatchDraw(Canvas var1) {
      this.drawSelectorCompat(var1);
      super.dispatchDraw(var1);
   }

   protected void drawSelectorCompat(Canvas var1) {
      if(!this.mSelectorRect.isEmpty()) {
         Drawable var2 = this.getSelector();
         if(var2 != null) {
            var2.setBounds(this.mSelectorRect);
            var2.draw(var1);
         }
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.setSelectorEnabled(true);
      this.updateSelectorStateCompat();
   }

   public int lookForSelectablePosition(int var1, boolean var2) {
      byte var4 = -1;
      ListAdapter var6 = this.getAdapter();
      int var3 = var4;
      if(var6 != null) {
         if(this.isInTouchMode()) {
            var3 = var4;
         } else {
            int var5 = var6.getCount();
            if(!this.getAdapter().areAllItemsEnabled()) {
               if(var2) {
                  var3 = Math.max(0, var1);

                  while(true) {
                     var1 = var3;
                     if(var3 >= var5) {
                        break;
                     }

                     var1 = var3;
                     if(var6.isEnabled(var3)) {
                        break;
                     }

                     ++var3;
                  }
               } else {
                  var3 = Math.min(var1, var5 - 1);

                  while(true) {
                     var1 = var3;
                     if(var3 < 0) {
                        break;
                     }

                     var1 = var3;
                     if(var6.isEnabled(var3)) {
                        break;
                     }

                     --var3;
                  }
               }

               var3 = var4;
               if(var1 >= 0) {
                  var3 = var4;
                  if(var1 < var5) {
                     var3 = var1;
                  }
               }
            } else {
               var3 = var4;
               if(var1 >= 0) {
                  var3 = var4;
                  if(var1 < var5) {
                     var3 = var1;
                  }
               }
            }
         }
      }

      return var3;
   }

   public int measureHeightOfChildrenCompat(int var1, int var2, int var3, int var4, int var5) {
      var2 = this.getListPaddingTop();
      var3 = this.getListPaddingBottom();
      this.getListPaddingLeft();
      this.getListPaddingRight();
      int var6 = this.getDividerHeight();
      Drawable var12 = this.getDivider();
      ListAdapter var15 = this.getAdapter();
      if(var15 == null) {
         var1 = var2 + var3;
      } else {
         var2 += var3;
         if(var6 <= 0 || var12 == null) {
            var6 = 0;
         }

         var3 = 0;
         View var16 = null;
         int var9 = 0;
         int var11 = var15.getCount();

         int var7;
         for(int var8 = 0; var8 < var11; var9 = var7) {
            int var10 = var15.getItemViewType(var8);
            var7 = var9;
            if(var10 != var9) {
               var16 = null;
               var7 = var10;
            }

            View var13 = var15.getView(var8, var16, this);
            LayoutParams var14 = var13.getLayoutParams();
            LayoutParams var17 = var14;
            if(var14 == null) {
               var17 = this.generateDefaultLayoutParams();
               var13.setLayoutParams(var17);
            }

            if(var17.height > 0) {
               var9 = MeasureSpec.makeMeasureSpec(var17.height, 1073741824);
            } else {
               var9 = MeasureSpec.makeMeasureSpec(0, 0);
            }

            var13.measure(var1, var9);
            var13.forceLayout();
            var9 = var2;
            if(var8 > 0) {
               var9 = var2 + var6;
            }

            var2 = var9 + var13.getMeasuredHeight();
            if(var2 >= var4) {
               if(var5 >= 0 && var8 > var5 && var3 > 0) {
                  var1 = var3;
                  if(var2 != var4) {
                     return var1;
                  }
               }

               var1 = var4;
               return var1;
            }

            var9 = var3;
            if(var5 >= 0) {
               var9 = var3;
               if(var8 >= var5) {
                  var9 = var2;
               }
            }

            ++var8;
            var16 = var13;
            var3 = var9;
         }

         var1 = var2;
      }

      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      switch(var1.getAction()) {
      case 0:
         this.mMotionPosition = this.pointToPosition((int)var1.getX(), (int)var1.getY());
      default:
         return super.onTouchEvent(var1);
      }
   }

   protected void positionSelectorCompat(int param1, View param2) {
      // $FF: Couldn't be decompiled
   }

   protected void positionSelectorLikeFocusCompat(int var1, View var2) {
      boolean var6 = true;
      Drawable var7 = this.getSelector();
      boolean var5;
      if(var7 != null && var1 != -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      if(var5) {
         var7.setVisible(false, false);
      }

      this.positionSelectorCompat(var1, var2);
      if(var5) {
         Rect var8 = this.mSelectorRect;
         float var4 = var8.exactCenterX();
         float var3 = var8.exactCenterY();
         if(this.getVisibility() != 0) {
            var6 = false;
         }

         var7.setVisible(var6, false);
         DrawableCompat.setHotspot(var7, var4, var3);
      }

   }

   protected void positionSelectorLikeTouchCompat(int var1, View var2, float var3, float var4) {
      this.positionSelectorLikeFocusCompat(var1, var2);
      Drawable var5 = this.getSelector();
      if(var5 != null && var1 != -1) {
         DrawableCompat.setHotspot(var5, var3, var4);
      }

   }

   public void setSelector(Drawable var1) {
      ListViewCompat.GateKeeperDrawable var2;
      if(var1 != null) {
         var2 = new ListViewCompat.GateKeeperDrawable(var1);
      } else {
         var2 = null;
      }

      this.mSelector = var2;
      super.setSelector(this.mSelector);
      Rect var3 = new Rect();
      if(var1 != null) {
         var1.getPadding(var3);
      }

      this.mSelectionLeftPadding = var3.left;
      this.mSelectionTopPadding = var3.top;
      this.mSelectionRightPadding = var3.right;
      this.mSelectionBottomPadding = var3.bottom;
   }

   protected void setSelectorEnabled(boolean var1) {
      if(this.mSelector != null) {
         this.mSelector.setEnabled(var1);
      }

   }

   protected boolean shouldShowSelectorCompat() {
      boolean var1;
      if(this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected boolean touchModeDrawsInPressedStateCompat() {
      return false;
   }

   protected void updateSelectorStateCompat() {
      Drawable var1 = this.getSelector();
      if(var1 != null && this.shouldShowSelectorCompat()) {
         var1.setState(this.getDrawableState());
      }

   }

   private static class GateKeeperDrawable extends DrawableWrapper {
      private boolean mEnabled = true;

      public GateKeeperDrawable(Drawable var1) {
         super(var1);
      }

      public void draw(Canvas var1) {
         if(this.mEnabled) {
            super.draw(var1);
         }

      }

      void setEnabled(boolean var1) {
         this.mEnabled = var1;
      }

      public void setHotspot(float var1, float var2) {
         if(this.mEnabled) {
            super.setHotspot(var1, var2);
         }

      }

      public void setHotspotBounds(int var1, int var2, int var3, int var4) {
         if(this.mEnabled) {
            super.setHotspotBounds(var1, var2, var3, var4);
         }

      }

      public boolean setState(int[] var1) {
         boolean var2;
         if(this.mEnabled) {
            var2 = super.setState(var1);
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean setVisible(boolean var1, boolean var2) {
         if(this.mEnabled) {
            var1 = super.setVisible(var1, var2);
         } else {
            var1 = false;
         }

         return var1;
      }
   }
}
