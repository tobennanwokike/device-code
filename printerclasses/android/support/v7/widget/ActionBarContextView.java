package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AbsActionBarView;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class ActionBarContextView extends AbsActionBarView {
   private static final String TAG = "ActionBarContextView";
   private View mClose;
   private int mCloseItemLayout;
   private View mCustomView;
   private CharSequence mSubtitle;
   private int mSubtitleStyleRes;
   private TextView mSubtitleView;
   private CharSequence mTitle;
   private LinearLayout mTitleLayout;
   private boolean mTitleOptional;
   private int mTitleStyleRes;
   private TextView mTitleView;

   public ActionBarContextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionBarContextView(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.actionModeStyle);
   }

   public ActionBarContextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.ActionMode, var3, 0);
      ViewCompat.setBackground(this, var4.getDrawable(R.styleable.ActionMode_background));
      this.mTitleStyleRes = var4.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
      this.mSubtitleStyleRes = var4.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
      this.mContentHeight = var4.getLayoutDimension(R.styleable.ActionMode_height, 0);
      this.mCloseItemLayout = var4.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
      var4.recycle();
   }

   private void initTitle() {
      byte var4 = 8;
      if(this.mTitleLayout == null) {
         LayoutInflater.from(this.getContext()).inflate(R.layout.abc_action_bar_title_item, this);
         this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
         this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
         this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
         if(this.mTitleStyleRes != 0) {
            this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
         }

         if(this.mSubtitleStyleRes != 0) {
            this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
         }
      }

      this.mTitleView.setText(this.mTitle);
      this.mSubtitleView.setText(this.mSubtitle);
      boolean var1;
      if(!TextUtils.isEmpty(this.mTitle)) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2;
      if(!TextUtils.isEmpty(this.mSubtitle)) {
         var2 = true;
      } else {
         var2 = false;
      }

      TextView var5 = this.mSubtitleView;
      byte var3;
      if(var2) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      byte var6;
      LinearLayout var7;
      label37: {
         var5.setVisibility(var3);
         var7 = this.mTitleLayout;
         if(!var1) {
            var6 = var4;
            if(!var2) {
               break label37;
            }
         }

         var6 = 0;
      }

      var7.setVisibility(var6);
      if(this.mTitleLayout.getParent() == null) {
         this.addView(this.mTitleLayout);
      }

   }

   public void closeMode() {
      if(this.mClose == null) {
         this.killMode();
      }

   }

   protected LayoutParams generateDefaultLayoutParams() {
      return new MarginLayoutParams(-1, -2);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return new MarginLayoutParams(this.getContext(), var1);
   }

   public CharSequence getSubtitle() {
      return this.mSubtitle;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public boolean hideOverflowMenu() {
      boolean var1;
      if(this.mActionMenuPresenter != null) {
         var1 = this.mActionMenuPresenter.hideOverflowMenu();
      } else {
         var1 = false;
      }

      return var1;
   }

   public void initForMode(final ActionMode var1) {
      if(this.mClose == null) {
         this.mClose = LayoutInflater.from(this.getContext()).inflate(this.mCloseItemLayout, this, false);
         this.addView(this.mClose);
      } else if(this.mClose.getParent() == null) {
         this.addView(this.mClose);
      }

      this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new OnClickListener() {
         public void onClick(View var1x) {
            var1.finish();
         }
      });
      MenuBuilder var3 = (MenuBuilder)var1.getMenu();
      if(this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.dismissPopupMenus();
      }

      this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext());
      this.mActionMenuPresenter.setReserveOverflow(true);
      LayoutParams var2 = new LayoutParams(-2, -1);
      var3.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
      this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      ViewCompat.setBackground(this.mMenuView, (Drawable)null);
      this.addView(this.mMenuView, var2);
   }

   public boolean isOverflowMenuShowing() {
      boolean var1;
      if(this.mActionMenuPresenter != null) {
         var1 = this.mActionMenuPresenter.isOverflowMenuShowing();
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isTitleOptional() {
      return this.mTitleOptional;
   }

   public void killMode() {
      this.removeAllViews();
      this.mCustomView = null;
      this.mMenuView = null;
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if(this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.hideOverflowMenu();
         this.mActionMenuPresenter.hideSubMenus();
      }

   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if(VERSION.SDK_INT >= 14) {
         if(var1.getEventType() == 32) {
            var1.setSource(this);
            var1.setClassName(this.getClass().getName());
            var1.setPackageName(this.getContext().getPackageName());
            var1.setContentDescription(this.mTitle);
         } else {
            super.onInitializeAccessibilityEvent(var1);
         }
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var1 = ViewUtils.isLayoutRtl(this);
      int var6;
      if(var1) {
         var6 = var4 - var2 - this.getPaddingRight();
      } else {
         var6 = this.getPaddingLeft();
      }

      int var7 = this.getPaddingTop();
      int var8 = var5 - var3 - this.getPaddingTop() - this.getPaddingBottom();
      var3 = var6;
      if(this.mClose != null) {
         var3 = var6;
         if(this.mClose.getVisibility() != 8) {
            MarginLayoutParams var9 = (MarginLayoutParams)this.mClose.getLayoutParams();
            if(var1) {
               var3 = var9.rightMargin;
            } else {
               var3 = var9.leftMargin;
            }

            if(var1) {
               var5 = var9.leftMargin;
            } else {
               var5 = var9.rightMargin;
            }

            var3 = next(var6, var3, var1);
            var3 = next(var3 + this.positionChild(this.mClose, var3, var7, var8, var1), var5, var1);
         }
      }

      var5 = var3;
      if(this.mTitleLayout != null) {
         var5 = var3;
         if(this.mCustomView == null) {
            var5 = var3;
            if(this.mTitleLayout.getVisibility() != 8) {
               var5 = var3 + this.positionChild(this.mTitleLayout, var3, var7, var8, var1);
            }
         }
      }

      if(this.mCustomView != null) {
         this.positionChild(this.mCustomView, var5, var7, var8, var1);
      }

      if(var1) {
         var2 = this.getPaddingLeft();
      } else {
         var2 = var4 - var2 - this.getPaddingRight();
      }

      if(this.mMenuView != null) {
         ActionMenuView var10 = this.mMenuView;
         if(!var1) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.positionChild(var10, var2, var7, var8, var1);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if(MeasureSpec.getMode(var1) != 1073741824) {
         throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)");
      } else if(MeasureSpec.getMode(var2) == 0) {
         throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
      } else {
         int var6 = MeasureSpec.getSize(var1);
         int var3;
         if(this.mContentHeight > 0) {
            var3 = this.mContentHeight;
         } else {
            var3 = MeasureSpec.getSize(var2);
         }

         int var7 = this.getPaddingTop() + this.getPaddingBottom();
         var1 = var6 - this.getPaddingLeft() - this.getPaddingRight();
         int var5 = var3 - var7;
         int var4 = MeasureSpec.makeMeasureSpec(var5, Integer.MIN_VALUE);
         var2 = var1;
         if(this.mClose != null) {
            var1 = this.measureChildView(this.mClose, var1, var4, 0);
            MarginLayoutParams var9 = (MarginLayoutParams)this.mClose.getLayoutParams();
            var2 = var1 - (var9.leftMargin + var9.rightMargin);
         }

         var1 = var2;
         if(this.mMenuView != null) {
            var1 = var2;
            if(this.mMenuView.getParent() == this) {
               var1 = this.measureChildView(this.mMenuView, var2, var4, 0);
            }
         }

         var2 = var1;
         if(this.mTitleLayout != null) {
            var2 = var1;
            if(this.mCustomView == null) {
               if(this.mTitleOptional) {
                  var2 = MeasureSpec.makeMeasureSpec(0, 0);
                  this.mTitleLayout.measure(var2, var4);
                  int var8 = this.mTitleLayout.getMeasuredWidth();
                  boolean var11;
                  if(var8 <= var1) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  var2 = var1;
                  if(var11) {
                     var2 = var1 - var8;
                  }

                  LinearLayout var12 = this.mTitleLayout;
                  byte var10;
                  if(var11) {
                     var10 = 0;
                  } else {
                     var10 = 8;
                  }

                  var12.setVisibility(var10);
               } else {
                  var2 = this.measureChildView(this.mTitleLayout, var1, var4, 0);
               }
            }
         }

         if(this.mCustomView != null) {
            LayoutParams var13 = this.mCustomView.getLayoutParams();
            if(var13.width != -2) {
               var1 = 1073741824;
            } else {
               var1 = Integer.MIN_VALUE;
            }

            if(var13.width >= 0) {
               var2 = Math.min(var13.width, var2);
            }

            if(var13.height != -2) {
               var4 = 1073741824;
            } else {
               var4 = Integer.MIN_VALUE;
            }

            if(var13.height >= 0) {
               var5 = Math.min(var13.height, var5);
            }

            this.mCustomView.measure(MeasureSpec.makeMeasureSpec(var2, var1), MeasureSpec.makeMeasureSpec(var5, var4));
         }

         if(this.mContentHeight <= 0) {
            var2 = 0;
            var5 = this.getChildCount();

            for(var3 = 0; var3 < var5; var2 = var1) {
               var4 = this.getChildAt(var3).getMeasuredHeight() + var7;
               var1 = var2;
               if(var4 > var2) {
                  var1 = var4;
               }

               ++var3;
            }

            this.setMeasuredDimension(var6, var2);
         } else {
            this.setMeasuredDimension(var6, var3);
         }

      }
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
   }

   public void setCustomView(View var1) {
      if(this.mCustomView != null) {
         this.removeView(this.mCustomView);
      }

      this.mCustomView = var1;
      if(var1 != null && this.mTitleLayout != null) {
         this.removeView(this.mTitleLayout);
         this.mTitleLayout = null;
      }

      if(var1 != null) {
         this.addView(var1);
      }

      this.requestLayout();
   }

   public void setSubtitle(CharSequence var1) {
      this.mSubtitle = var1;
      this.initTitle();
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.initTitle();
   }

   public void setTitleOptional(boolean var1) {
      if(var1 != this.mTitleOptional) {
         this.requestLayout();
      }

      this.mTitleOptional = var1;
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   public boolean showOverflowMenu() {
      boolean var1;
      if(this.mActionMenuPresenter != null) {
         var1 = this.mActionMenuPresenter.showOverflowMenu();
      } else {
         var1 = false;
      }

      return var1;
   }
}
