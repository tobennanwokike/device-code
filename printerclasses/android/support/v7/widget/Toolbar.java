package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.RtlSpacingHelper;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.support.v7.widget.ViewUtils;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
   private static final String TAG = "Toolbar";
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   int mButtonGravity;
   ImageButton mCollapseButtonView;
   private CharSequence mCollapseDescription;
   private Drawable mCollapseIcon;
   private boolean mCollapsible;
   private int mContentInsetEndWithActions;
   private int mContentInsetStartWithNavigation;
   private RtlSpacingHelper mContentInsets;
   private boolean mEatingHover;
   private boolean mEatingTouch;
   View mExpandedActionView;
   private Toolbar.ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
   private int mGravity;
   private final ArrayList mHiddenViews;
   private ImageView mLogoView;
   private int mMaxButtonHeight;
   private MenuBuilder.Callback mMenuBuilderCallback;
   private ActionMenuView mMenuView;
   private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
   private ImageButton mNavButtonView;
   Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;
   private ActionMenuPresenter mOuterActionMenuPresenter;
   private Context mPopupContext;
   private int mPopupTheme;
   private final Runnable mShowOverflowMenuRunnable;
   private CharSequence mSubtitleText;
   private int mSubtitleTextAppearance;
   private int mSubtitleTextColor;
   private TextView mSubtitleTextView;
   private final int[] mTempMargins;
   private final ArrayList mTempViews;
   private int mTitleMarginBottom;
   private int mTitleMarginEnd;
   private int mTitleMarginStart;
   private int mTitleMarginTop;
   private CharSequence mTitleText;
   private int mTitleTextAppearance;
   private int mTitleTextColor;
   private TextView mTitleTextView;
   private ToolbarWidgetWrapper mWrapper;

   public Toolbar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public Toolbar(Context var1, @Nullable AttributeSet var2) {
      this(var1, var2, R.attr.toolbarStyle);
   }

   public Toolbar(Context var1, @Nullable AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mGravity = 8388627;
      this.mTempViews = new ArrayList();
      this.mHiddenViews = new ArrayList();
      this.mTempMargins = new int[2];
      this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
         public boolean onMenuItemClick(MenuItem var1) {
            boolean var2;
            if(Toolbar.this.mOnMenuItemClickListener != null) {
               var2 = Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(var1);
            } else {
               var2 = false;
            }

            return var2;
         }
      };
      this.mShowOverflowMenuRunnable = new Runnable() {
         public void run() {
            Toolbar.this.showOverflowMenu();
         }
      };
      TintTypedArray var7 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, R.styleable.Toolbar, var3, 0);
      this.mTitleTextAppearance = var7.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
      this.mSubtitleTextAppearance = var7.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
      this.mGravity = var7.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
      this.mButtonGravity = var7.getInteger(R.styleable.Toolbar_buttonGravity, 48);
      int var4 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
      var3 = var4;
      if(var7.hasValue(R.styleable.Toolbar_titleMargins)) {
         var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, var4);
      }

      this.mTitleMarginBottom = var3;
      this.mTitleMarginTop = var3;
      this.mTitleMarginEnd = var3;
      this.mTitleMarginStart = var3;
      var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
      if(var3 >= 0) {
         this.mTitleMarginStart = var3;
      }

      var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
      if(var3 >= 0) {
         this.mTitleMarginEnd = var3;
      }

      var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
      if(var3 >= 0) {
         this.mTitleMarginTop = var3;
      }

      var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
      if(var3 >= 0) {
         this.mTitleMarginBottom = var3;
      }

      this.mMaxButtonHeight = var7.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
      var4 = var7.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
      var3 = var7.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
      int var5 = var7.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
      int var6 = var7.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
      this.ensureContentInsets();
      this.mContentInsets.setAbsolute(var5, var6);
      if(var4 != Integer.MIN_VALUE || var3 != Integer.MIN_VALUE) {
         this.mContentInsets.setRelative(var4, var3);
      }

      this.mContentInsetStartWithNavigation = var7.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
      this.mContentInsetEndWithActions = var7.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
      this.mCollapseIcon = var7.getDrawable(R.styleable.Toolbar_collapseIcon);
      this.mCollapseDescription = var7.getText(R.styleable.Toolbar_collapseContentDescription);
      CharSequence var8 = var7.getText(R.styleable.Toolbar_title);
      if(!TextUtils.isEmpty(var8)) {
         this.setTitle(var8);
      }

      var8 = var7.getText(R.styleable.Toolbar_subtitle);
      if(!TextUtils.isEmpty(var8)) {
         this.setSubtitle(var8);
      }

      this.mPopupContext = this.getContext();
      this.setPopupTheme(var7.getResourceId(R.styleable.Toolbar_popupTheme, 0));
      Drawable var9 = var7.getDrawable(R.styleable.Toolbar_navigationIcon);
      if(var9 != null) {
         this.setNavigationIcon(var9);
      }

      var8 = var7.getText(R.styleable.Toolbar_navigationContentDescription);
      if(!TextUtils.isEmpty(var8)) {
         this.setNavigationContentDescription(var8);
      }

      var9 = var7.getDrawable(R.styleable.Toolbar_logo);
      if(var9 != null) {
         this.setLogo(var9);
      }

      var8 = var7.getText(R.styleable.Toolbar_logoDescription);
      if(!TextUtils.isEmpty(var8)) {
         this.setLogoDescription(var8);
      }

      if(var7.hasValue(R.styleable.Toolbar_titleTextColor)) {
         this.setTitleTextColor(var7.getColor(R.styleable.Toolbar_titleTextColor, -1));
      }

      if(var7.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
         this.setSubtitleTextColor(var7.getColor(R.styleable.Toolbar_subtitleTextColor, -1));
      }

      var7.recycle();
   }

   private void addCustomViewsWithGravity(List var1, int var2) {
      boolean var3 = true;
      if(ViewCompat.getLayoutDirection(this) != 1) {
         var3 = false;
      }

      int var5 = this.getChildCount();
      int var4 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      var1.clear();
      Toolbar.LayoutParams var6;
      View var7;
      if(var3) {
         for(var2 = var5 - 1; var2 >= 0; --var2) {
            var7 = this.getChildAt(var2);
            var6 = (Toolbar.LayoutParams)var7.getLayoutParams();
            if(var6.mViewType == 0 && this.shouldLayout(var7) && this.getChildHorizontalGravity(var6.gravity) == var4) {
               var1.add(var7);
            }
         }
      } else {
         for(var2 = 0; var2 < var5; ++var2) {
            var7 = this.getChildAt(var2);
            var6 = (Toolbar.LayoutParams)var7.getLayoutParams();
            if(var6.mViewType == 0 && this.shouldLayout(var7) && this.getChildHorizontalGravity(var6.gravity) == var4) {
               var1.add(var7);
            }
         }
      }

   }

   private void addSystemView(View var1, boolean var2) {
      android.view.ViewGroup.LayoutParams var3 = var1.getLayoutParams();
      Toolbar.LayoutParams var4;
      if(var3 == null) {
         var4 = this.generateDefaultLayoutParams();
      } else if(!this.checkLayoutParams(var3)) {
         var4 = this.generateLayoutParams(var3);
      } else {
         var4 = (Toolbar.LayoutParams)var3;
      }

      var4.mViewType = 1;
      if(var2 && this.mExpandedActionView != null) {
         var1.setLayoutParams(var4);
         this.mHiddenViews.add(var1);
      } else {
         this.addView(var1, var4);
      }

   }

   private void ensureContentInsets() {
      if(this.mContentInsets == null) {
         this.mContentInsets = new RtlSpacingHelper();
      }

   }

   private void ensureLogoView() {
      if(this.mLogoView == null) {
         this.mLogoView = new AppCompatImageView(this.getContext());
      }

   }

   private void ensureMenu() {
      this.ensureMenuView();
      if(this.mMenuView.peekMenu() == null) {
         MenuBuilder var1 = (MenuBuilder)this.mMenuView.getMenu();
         if(this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
         }

         this.mMenuView.setExpandedActionViewsExclusive(true);
         var1.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
      }

   }

   private void ensureMenuView() {
      if(this.mMenuView == null) {
         this.mMenuView = new ActionMenuView(this.getContext());
         this.mMenuView.setPopupTheme(this.mPopupTheme);
         this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
         this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388613 | this.mButtonGravity & 112;
         this.mMenuView.setLayoutParams(var1);
         this.addSystemView(this.mMenuView, false);
      }

   }

   private void ensureNavButtonView() {
      if(this.mNavButtonView == null) {
         this.mNavButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R.attr.toolbarNavigationButtonStyle);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         this.mNavButtonView.setLayoutParams(var1);
      }

   }

   private int getChildHorizontalGravity(int var1) {
      int var3 = ViewCompat.getLayoutDirection(this);
      int var2 = GravityCompat.getAbsoluteGravity(var1, var3) & 7;
      var1 = var2;
      switch(var2) {
      case 2:
      case 4:
      default:
         if(var3 == 1) {
            var1 = 5;
         } else {
            var1 = 3;
         }
      case 1:
      case 3:
      case 5:
         return var1;
      }
   }

   private int getChildTop(View var1, int var2) {
      Toolbar.LayoutParams var7 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var4 = var1.getMeasuredHeight();
      if(var2 > 0) {
         var2 = (var4 - var2) / 2;
      } else {
         var2 = 0;
      }

      switch(this.getChildVerticalGravity(var7.gravity)) {
      case 48:
         var2 = this.getPaddingTop() - var2;
         break;
      case 80:
         var2 = this.getHeight() - this.getPaddingBottom() - var4 - var7.bottomMargin - var2;
         break;
      default:
         int var5 = this.getPaddingTop();
         int var6 = this.getPaddingBottom();
         var2 = this.getHeight();
         int var3 = (var2 - var5 - var6 - var4) / 2;
         if(var3 < var7.topMargin) {
            var2 = var7.topMargin;
         } else {
            var4 = var2 - var6 - var4 - var3 - var5;
            var2 = var3;
            if(var4 < var7.bottomMargin) {
               var2 = Math.max(0, var3 - (var7.bottomMargin - var4));
            }
         }

         var2 += var5;
      }

      return var2;
   }

   private int getChildVerticalGravity(int var1) {
      int var2 = var1 & 112;
      var1 = var2;
      switch(var2) {
      default:
         var1 = this.mGravity & 112;
      case 16:
      case 48:
      case 80:
         return var1;
      }
   }

   private int getHorizontalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return MarginLayoutParamsCompat.getMarginStart(var2) + MarginLayoutParamsCompat.getMarginEnd(var2);
   }

   private MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.getContext());
   }

   private int getVerticalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return var2.topMargin + var2.bottomMargin;
   }

   private int getViewListMeasuredWidth(List var1, int[] var2) {
      int var6 = var2[0];
      int var5 = var2[1];
      int var3 = 0;
      int var7 = var1.size();

      for(int var4 = 0; var4 < var7; ++var4) {
         View var11 = (View)var1.get(var4);
         Toolbar.LayoutParams var10 = (Toolbar.LayoutParams)var11.getLayoutParams();
         var6 = var10.leftMargin - var6;
         var5 = var10.rightMargin - var5;
         int var9 = Math.max(0, var6);
         int var8 = Math.max(0, var5);
         var6 = Math.max(0, -var6);
         var5 = Math.max(0, -var5);
         var3 += var11.getMeasuredWidth() + var9 + var8;
      }

      return var3;
   }

   private boolean isChildOrHidden(View var1) {
      boolean var2;
      if(var1.getParent() != this && !this.mHiddenViews.contains(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private static boolean isCustomView(View var0) {
      boolean var1;
      if(((Toolbar.LayoutParams)var0.getLayoutParams()).mViewType == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private int layoutChildLeft(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var6 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var5 = var6.leftMargin - var3[0];
      var2 += Math.max(0, var5);
      var3[0] = Math.max(0, -var5);
      var4 = this.getChildTop(var1, var4);
      var5 = var1.getMeasuredWidth();
      var1.layout(var2, var4, var2 + var5, var1.getMeasuredHeight() + var4);
      return var2 + var6.rightMargin + var5;
   }

   private int layoutChildRight(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var6 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var5 = var6.rightMargin - var3[1];
      var2 -= Math.max(0, var5);
      var3[1] = Math.max(0, -var5);
      var5 = this.getChildTop(var1, var4);
      var4 = var1.getMeasuredWidth();
      var1.layout(var2 - var4, var5, var2, var1.getMeasuredHeight() + var5);
      return var2 - (var6.leftMargin + var4);
   }

   private int measureChildCollapseMargins(View var1, int var2, int var3, int var4, int var5, int[] var6) {
      MarginLayoutParams var10 = (MarginLayoutParams)var1.getLayoutParams();
      int var7 = var10.leftMargin - var6[0];
      int var9 = var10.rightMargin - var6[1];
      int var8 = Math.max(0, var7) + Math.max(0, var9);
      var6[0] = Math.max(0, -var7);
      var6[1] = Math.max(0, -var9);
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var8 + var3, var10.width), getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var10.topMargin + var10.bottomMargin + var5, var10.height));
      return var1.getMeasuredWidth() + var8;
   }

   private void measureChildConstrained(View var1, int var2, int var3, int var4, int var5, int var6) {
      MarginLayoutParams var8 = (MarginLayoutParams)var1.getLayoutParams();
      int var7 = getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var8.leftMargin + var8.rightMargin + var3, var8.width);
      var3 = getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var8.topMargin + var8.bottomMargin + var5, var8.height);
      var4 = MeasureSpec.getMode(var3);
      var2 = var3;
      if(var4 != 1073741824) {
         var2 = var3;
         if(var6 >= 0) {
            if(var4 != 0) {
               var6 = Math.min(MeasureSpec.getSize(var3), var6);
            }

            var2 = MeasureSpec.makeMeasureSpec(var6, 1073741824);
         }
      }

      var1.measure(var7, var2);
   }

   private void postShowOverflowMenu() {
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
      this.post(this.mShowOverflowMenuRunnable);
   }

   private boolean shouldCollapse() {
      boolean var4 = false;
      boolean var3;
      if(!this.mCollapsible) {
         var3 = var4;
      } else {
         int var2 = this.getChildCount();
         int var1 = 0;

         while(true) {
            if(var1 >= var2) {
               var3 = true;
               break;
            }

            View var5 = this.getChildAt(var1);
            if(this.shouldLayout(var5) && var5.getMeasuredWidth() > 0) {
               var3 = var4;
               if(var5.getMeasuredHeight() > 0) {
                  break;
               }
            }

            ++var1;
         }
      }

      return var3;
   }

   private boolean shouldLayout(View var1) {
      boolean var2;
      if(var1 != null && var1.getParent() == this && var1.getVisibility() != 8) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void addChildrenForExpandedActionView() {
      for(int var1 = this.mHiddenViews.size() - 1; var1 >= 0; --var1) {
         this.addView((View)this.mHiddenViews.get(var1));
      }

      this.mHiddenViews.clear();
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean canShowOverflowMenu() {
      boolean var1;
      if(this.getVisibility() == 0 && this.mMenuView != null && this.mMenuView.isOverflowReserved()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if(super.checkLayoutParams(var1) && var1 instanceof Toolbar.LayoutParams) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void collapseActionView() {
      MenuItemImpl var1;
      if(this.mExpandedMenuPresenter == null) {
         var1 = null;
      } else {
         var1 = this.mExpandedMenuPresenter.mCurrentExpandedItem;
      }

      if(var1 != null) {
         var1.collapseActionView();
      }

   }

   public void dismissPopupMenus() {
      if(this.mMenuView != null) {
         this.mMenuView.dismissPopupMenus();
      }

   }

   void ensureCollapseButtonView() {
      if(this.mCollapseButtonView == null) {
         this.mCollapseButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R.attr.toolbarNavigationButtonStyle);
         this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
         this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         var1.mViewType = 2;
         this.mCollapseButtonView.setLayoutParams(var1);
         this.mCollapseButtonView.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               Toolbar.this.collapseActionView();
            }
         });
      }

   }

   protected Toolbar.LayoutParams generateDefaultLayoutParams() {
      return new Toolbar.LayoutParams(-2, -2);
   }

   public Toolbar.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new Toolbar.LayoutParams(this.getContext(), var1);
   }

   protected Toolbar.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      Toolbar.LayoutParams var2;
      if(var1 instanceof Toolbar.LayoutParams) {
         var2 = new Toolbar.LayoutParams((Toolbar.LayoutParams)var1);
      } else if(var1 instanceof ActionBar.LayoutParams) {
         var2 = new Toolbar.LayoutParams((ActionBar.LayoutParams)var1);
      } else if(var1 instanceof MarginLayoutParams) {
         var2 = new Toolbar.LayoutParams((MarginLayoutParams)var1);
      } else {
         var2 = new Toolbar.LayoutParams(var1);
      }

      return var2;
   }

   public int getContentInsetEnd() {
      int var1;
      if(this.mContentInsets != null) {
         var1 = this.mContentInsets.getEnd();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getContentInsetEndWithActions() {
      int var1;
      if(this.mContentInsetEndWithActions != Integer.MIN_VALUE) {
         var1 = this.mContentInsetEndWithActions;
      } else {
         var1 = this.getContentInsetEnd();
      }

      return var1;
   }

   public int getContentInsetLeft() {
      int var1;
      if(this.mContentInsets != null) {
         var1 = this.mContentInsets.getLeft();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getContentInsetRight() {
      int var1;
      if(this.mContentInsets != null) {
         var1 = this.mContentInsets.getRight();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getContentInsetStart() {
      int var1;
      if(this.mContentInsets != null) {
         var1 = this.mContentInsets.getStart();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getContentInsetStartWithNavigation() {
      int var1;
      if(this.mContentInsetStartWithNavigation != Integer.MIN_VALUE) {
         var1 = this.mContentInsetStartWithNavigation;
      } else {
         var1 = this.getContentInsetStart();
      }

      return var1;
   }

   public int getCurrentContentInsetEnd() {
      boolean var1 = false;
      if(this.mMenuView != null) {
         MenuBuilder var2 = this.mMenuView.peekMenu();
         if(var2 != null && var2.hasVisibleItems()) {
            var1 = true;
         } else {
            var1 = false;
         }
      }

      int var3;
      if(var1) {
         var3 = Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
      } else {
         var3 = this.getContentInsetEnd();
      }

      return var3;
   }

   public int getCurrentContentInsetLeft() {
      int var1;
      if(ViewCompat.getLayoutDirection(this) == 1) {
         var1 = this.getCurrentContentInsetEnd();
      } else {
         var1 = this.getCurrentContentInsetStart();
      }

      return var1;
   }

   public int getCurrentContentInsetRight() {
      int var1;
      if(ViewCompat.getLayoutDirection(this) == 1) {
         var1 = this.getCurrentContentInsetStart();
      } else {
         var1 = this.getCurrentContentInsetEnd();
      }

      return var1;
   }

   public int getCurrentContentInsetStart() {
      int var1;
      if(this.getNavigationIcon() != null) {
         var1 = Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
      } else {
         var1 = this.getContentInsetStart();
      }

      return var1;
   }

   public Drawable getLogo() {
      Drawable var1;
      if(this.mLogoView != null) {
         var1 = this.mLogoView.getDrawable();
      } else {
         var1 = null;
      }

      return var1;
   }

   public CharSequence getLogoDescription() {
      CharSequence var1;
      if(this.mLogoView != null) {
         var1 = this.mLogoView.getContentDescription();
      } else {
         var1 = null;
      }

      return var1;
   }

   public Menu getMenu() {
      this.ensureMenu();
      return this.mMenuView.getMenu();
   }

   @Nullable
   public CharSequence getNavigationContentDescription() {
      CharSequence var1;
      if(this.mNavButtonView != null) {
         var1 = this.mNavButtonView.getContentDescription();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   public Drawable getNavigationIcon() {
      Drawable var1;
      if(this.mNavButtonView != null) {
         var1 = this.mNavButtonView.getDrawable();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   public Drawable getOverflowIcon() {
      this.ensureMenu();
      return this.mMenuView.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   public CharSequence getSubtitle() {
      return this.mSubtitleText;
   }

   public CharSequence getTitle() {
      return this.mTitleText;
   }

   public int getTitleMarginBottom() {
      return this.mTitleMarginBottom;
   }

   public int getTitleMarginEnd() {
      return this.mTitleMarginEnd;
   }

   public int getTitleMarginStart() {
      return this.mTitleMarginStart;
   }

   public int getTitleMarginTop() {
      return this.mTitleMarginTop;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public DecorToolbar getWrapper() {
      if(this.mWrapper == null) {
         this.mWrapper = new ToolbarWidgetWrapper(this, true);
      }

      return this.mWrapper;
   }

   public boolean hasExpandedActionView() {
      boolean var1;
      if(this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hideOverflowMenu() {
      boolean var1;
      if(this.mMenuView != null && this.mMenuView.hideOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void inflateMenu(@MenuRes int var1) {
      this.getMenuInflater().inflate(var1, this.getMenu());
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean isOverflowMenuShowPending() {
      boolean var1;
      if(this.mMenuView != null && this.mMenuView.isOverflowMenuShowPending()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isOverflowMenuShowing() {
      boolean var1;
      if(this.mMenuView != null && this.mMenuView.isOverflowMenuShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean isTitleTruncated() {
      boolean var4 = false;
      boolean var3;
      if(this.mTitleTextView == null) {
         var3 = var4;
      } else {
         Layout var5 = this.mTitleTextView.getLayout();
         var3 = var4;
         if(var5 != null) {
            int var2 = var5.getLineCount();
            int var1 = 0;

            while(true) {
               var3 = var4;
               if(var1 >= var2) {
                  break;
               }

               if(var5.getEllipsisCount(var1) > 0) {
                  var3 = true;
                  break;
               }

               ++var1;
            }
         }
      }

      return var3;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionMasked(var1);
      if(var2 == 9) {
         this.mEatingHover = false;
      }

      if(!this.mEatingHover) {
         boolean var3 = super.onHoverEvent(var1);
         if(var2 == 9 && !var3) {
            this.mEatingHover = true;
         }
      }

      if(var2 == 10 || var2 == 3) {
         this.mEatingHover = false;
      }

      return true;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      boolean var8;
      if(ViewCompat.getLayoutDirection(this) == 1) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var12 = this.getWidth();
      int var14 = this.getHeight();
      int var11 = this.getPaddingLeft();
      int var13 = this.getPaddingRight();
      int var10 = this.getPaddingTop();
      int var15 = this.getPaddingBottom();
      int var7 = var12 - var13;
      int[] var19 = this.mTempMargins;
      var19[1] = 0;
      var19[0] = 0;
      var4 = ViewCompat.getMinimumHeight(this);
      int var6;
      if(var4 >= 0) {
         var6 = Math.min(var4, var5 - var3);
      } else {
         var6 = 0;
      }

      var4 = var11;
      var3 = var7;
      if(this.shouldLayout(this.mNavButtonView)) {
         if(var8) {
            var3 = this.layoutChildRight(this.mNavButtonView, var7, var19, var6);
            var4 = var11;
         } else {
            var4 = this.layoutChildLeft(this.mNavButtonView, var11, var19, var6);
            var3 = var7;
         }
      }

      var5 = var4;
      var2 = var3;
      if(this.shouldLayout(this.mCollapseButtonView)) {
         if(var8) {
            var2 = this.layoutChildRight(this.mCollapseButtonView, var3, var19, var6);
            var5 = var4;
         } else {
            var5 = this.layoutChildLeft(this.mCollapseButtonView, var4, var19, var6);
            var2 = var3;
         }
      }

      var4 = var5;
      var3 = var2;
      if(this.shouldLayout(this.mMenuView)) {
         if(var8) {
            var4 = this.layoutChildLeft(this.mMenuView, var5, var19, var6);
            var3 = var2;
         } else {
            var3 = this.layoutChildRight(this.mMenuView, var2, var19, var6);
            var4 = var5;
         }
      }

      var5 = this.getCurrentContentInsetLeft();
      var2 = this.getCurrentContentInsetRight();
      var19[0] = Math.max(0, var5 - var4);
      var19[1] = Math.max(0, var2 - (var12 - var13 - var3));
      var4 = Math.max(var4, var5);
      var5 = Math.min(var3, var12 - var13 - var2);
      var3 = var4;
      var2 = var5;
      if(this.shouldLayout(this.mExpandedActionView)) {
         if(var8) {
            var2 = this.layoutChildRight(this.mExpandedActionView, var5, var19, var6);
            var3 = var4;
         } else {
            var3 = this.layoutChildLeft(this.mExpandedActionView, var4, var19, var6);
            var2 = var5;
         }
      }

      var4 = var3;
      var5 = var2;
      if(this.shouldLayout(this.mLogoView)) {
         if(var8) {
            var5 = this.layoutChildRight(this.mLogoView, var2, var19, var6);
            var4 = var3;
         } else {
            var4 = this.layoutChildLeft(this.mLogoView, var3, var19, var6);
            var5 = var2;
         }
      }

      boolean var16 = this.shouldLayout(this.mTitleTextView);
      var1 = this.shouldLayout(this.mSubtitleTextView);
      var2 = 0;
      Toolbar.LayoutParams var17;
      if(var16) {
         var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
         var2 = 0 + var17.topMargin + this.mTitleTextView.getMeasuredHeight() + var17.bottomMargin;
      }

      int var9 = var2;
      if(var1) {
         var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
         var9 = var2 + var17.topMargin + this.mSubtitleTextView.getMeasuredHeight() + var17.bottomMargin;
      }

      label179: {
         if(!var16) {
            var3 = var4;
            var2 = var5;
            if(!var1) {
               break label179;
            }
         }

         TextView var23;
         if(var16) {
            var23 = this.mTitleTextView;
         } else {
            var23 = this.mSubtitleTextView;
         }

         TextView var18;
         if(var1) {
            var18 = this.mSubtitleTextView;
         } else {
            var18 = this.mTitleTextView;
         }

         var17 = (Toolbar.LayoutParams)var23.getLayoutParams();
         Toolbar.LayoutParams var24 = (Toolbar.LayoutParams)var18.getLayoutParams();
         boolean var20;
         if((!var16 || this.mTitleTextView.getMeasuredWidth() <= 0) && (!var1 || this.mSubtitleTextView.getMeasuredWidth() <= 0)) {
            var20 = false;
         } else {
            var20 = true;
         }

         switch(this.mGravity & 112) {
         case 48:
            var2 = this.getPaddingTop() + var17.topMargin + this.mTitleMarginTop;
            break;
         case 80:
            var2 = var14 - var15 - var24.bottomMargin - this.mTitleMarginBottom - var9;
            break;
         default:
            var3 = (var14 - var10 - var15 - var9) / 2;
            if(var3 < var17.topMargin + this.mTitleMarginTop) {
               var2 = var17.topMargin + this.mTitleMarginTop;
            } else {
               var9 = var14 - var15 - var9 - var3 - var10;
               var2 = var3;
               if(var9 < var17.bottomMargin + this.mTitleMarginBottom) {
                  var2 = Math.max(0, var3 - (var24.bottomMargin + this.mTitleMarginBottom - var9));
               }
            }

            var2 += var10;
         }

         int var22;
         if(var8) {
            if(var20) {
               var3 = this.mTitleMarginStart;
            } else {
               var3 = 0;
            }

            var3 -= var19[1];
            var5 -= Math.max(0, var3);
            var19[1] = Math.max(0, -var3);
            var22 = var5;
            var10 = var2;
            if(var16) {
               var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
               var22 = var5 - this.mTitleTextView.getMeasuredWidth();
               var10 = var2 + this.mTitleTextView.getMeasuredHeight();
               this.mTitleTextView.layout(var22, var2, var5, var10);
               var22 -= this.mTitleMarginEnd;
               var10 += var17.bottomMargin;
            }

            var9 = var5;
            if(var1) {
               var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
               var2 = var10 + var17.topMargin;
               var10 = this.mSubtitleTextView.getMeasuredWidth();
               var9 = var2 + this.mSubtitleTextView.getMeasuredHeight();
               this.mSubtitleTextView.layout(var5 - var10, var2, var5, var9);
               var9 = var5 - this.mTitleMarginEnd;
               var2 = var17.bottomMargin;
            }

            var3 = var4;
            var2 = var5;
            if(var20) {
               var2 = Math.min(var22, var9);
               var3 = var4;
            }
         } else {
            if(var20) {
               var3 = this.mTitleMarginStart;
            } else {
               var3 = 0;
            }

            var22 = var3 - var19[0];
            var3 = var4 + Math.max(0, var22);
            var19[0] = Math.max(0, -var22);
            var4 = var3;
            var10 = var2;
            if(var16) {
               var17 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
               var4 = var3 + this.mTitleTextView.getMeasuredWidth();
               var10 = var2 + this.mTitleTextView.getMeasuredHeight();
               this.mTitleTextView.layout(var3, var2, var4, var10);
               var4 += this.mTitleMarginEnd;
               var10 += var17.bottomMargin;
            }

            var9 = var3;
            if(var1) {
               var17 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
               var10 += var17.topMargin;
               var2 = var3 + this.mSubtitleTextView.getMeasuredWidth();
               var9 = var10 + this.mSubtitleTextView.getMeasuredHeight();
               this.mSubtitleTextView.layout(var3, var10, var2, var9);
               var9 = var2 + this.mTitleMarginEnd;
               var2 = var17.bottomMargin;
            }

            var2 = var5;
            if(var20) {
               var3 = Math.max(var4, var9);
               var2 = var5;
            }
         }
      }

      this.addCustomViewsWithGravity(this.mTempViews, 3);
      var5 = this.mTempViews.size();

      for(var4 = 0; var4 < var5; ++var4) {
         var3 = this.layoutChildLeft((View)this.mTempViews.get(var4), var3, var19, var6);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 5);
      var7 = this.mTempViews.size();
      byte var21 = 0;
      var4 = var2;

      for(var2 = var21; var2 < var7; ++var2) {
         var4 = this.layoutChildRight((View)this.mTempViews.get(var2), var4, var19, var6);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 1);
      var2 = this.getViewListMeasuredWidth(this.mTempViews, var19);
      var5 = var11 + (var12 - var11 - var13) / 2 - var2 / 2;
      var7 = var5 + var2;
      if(var5 < var3) {
         var2 = var3;
      } else {
         var2 = var5;
         if(var7 > var4) {
            var2 = var5 - (var7 - var4);
         }
      }

      var4 = this.mTempViews.size();

      for(var3 = 0; var3 < var4; ++var3) {
         var2 = this.layoutChildLeft((View)this.mTempViews.get(var3), var2, var19, var6);
      }

      this.mTempViews.clear();
   }

   protected void onMeasure(int var1, int var2) {
      int var6 = 0;
      int var5 = 0;
      int[] var14 = this.mTempMargins;
      byte var8;
      byte var9;
      if(ViewUtils.isLayoutRtl(this)) {
         var9 = 1;
         var8 = 0;
      } else {
         var9 = 0;
         var8 = 1;
      }

      int var7 = 0;
      if(this.shouldLayout(this.mNavButtonView)) {
         this.measureChildConstrained(this.mNavButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var7 = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mNavButtonView);
         var6 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mNavButtonView));
         var5 = ViewUtils.combineMeasuredStates(0, ViewCompat.getMeasuredState(this.mNavButtonView));
      }

      int var3 = var5;
      int var4 = var6;
      if(this.shouldLayout(this.mCollapseButtonView)) {
         this.measureChildConstrained(this.mCollapseButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var7 = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mCollapseButtonView);
         var4 = Math.max(var6, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mCollapseButtonView));
         var3 = ViewUtils.combineMeasuredStates(var5, ViewCompat.getMeasuredState(this.mCollapseButtonView));
      }

      var5 = this.getCurrentContentInsetStart();
      int var10 = 0 + Math.max(var5, var7);
      var14[var9] = Math.max(0, var5 - var7);
      var7 = 0;
      var6 = var3;
      var5 = var4;
      if(this.shouldLayout(this.mMenuView)) {
         this.measureChildConstrained(this.mMenuView, var1, var10, var2, 0, this.mMaxButtonHeight);
         var7 = this.mMenuView.getMeasuredWidth() + this.getHorizontalMargins(this.mMenuView);
         var5 = Math.max(var4, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins(this.mMenuView));
         var6 = ViewUtils.combineMeasuredStates(var3, ViewCompat.getMeasuredState(this.mMenuView));
      }

      var3 = this.getCurrentContentInsetEnd();
      int var16 = var10 + Math.max(var3, var7);
      var14[var8] = Math.max(0, var3 - var7);
      int var15 = var16;
      var4 = var6;
      var3 = var5;
      if(this.shouldLayout(this.mExpandedActionView)) {
         var15 = var16 + this.measureChildCollapseMargins(this.mExpandedActionView, var1, var16, var2, 0, var14);
         var3 = Math.max(var5, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
         var4 = ViewUtils.combineMeasuredStates(var6, ViewCompat.getMeasuredState(this.mExpandedActionView));
      }

      var5 = var15;
      var6 = var4;
      var7 = var3;
      if(this.shouldLayout(this.mLogoView)) {
         var5 = var15 + this.measureChildCollapseMargins(this.mLogoView, var1, var15, var2, 0, var14);
         var7 = Math.max(var3, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins(this.mLogoView));
         var6 = ViewUtils.combineMeasuredStates(var4, ViewCompat.getMeasuredState(this.mLogoView));
      }

      var10 = this.getChildCount();
      var4 = 0;
      var15 = var7;
      var3 = var6;

      for(var7 = var5; var4 < var10; var15 = var16) {
         View var13 = this.getChildAt(var4);
         var5 = var7;
         var6 = var3;
         var16 = var15;
         if(((Toolbar.LayoutParams)var13.getLayoutParams()).mViewType == 0) {
            if(!this.shouldLayout(var13)) {
               var16 = var15;
               var6 = var3;
               var5 = var7;
            } else {
               var5 = var7 + this.measureChildCollapseMargins(var13, var1, var7, var2, 0, var14);
               var16 = Math.max(var15, var13.getMeasuredHeight() + this.getVerticalMargins(var13));
               var6 = ViewUtils.combineMeasuredStates(var3, ViewCompat.getMeasuredState(var13));
            }
         }

         ++var4;
         var7 = var5;
         var3 = var6;
      }

      var6 = 0;
      var5 = 0;
      int var12 = this.mTitleMarginTop + this.mTitleMarginBottom;
      int var11 = this.mTitleMarginStart + this.mTitleMarginEnd;
      var4 = var3;
      if(this.shouldLayout(this.mTitleTextView)) {
         this.measureChildCollapseMargins(this.mTitleTextView, var1, var7 + var11, var2, var12, var14);
         var6 = this.mTitleTextView.getMeasuredWidth() + this.getHorizontalMargins(this.mTitleTextView);
         var5 = this.mTitleTextView.getMeasuredHeight() + this.getVerticalMargins(this.mTitleTextView);
         var4 = ViewUtils.combineMeasuredStates(var3, ViewCompat.getMeasuredState(this.mTitleTextView));
      }

      var16 = var4;
      var10 = var5;
      var3 = var6;
      if(this.shouldLayout(this.mSubtitleTextView)) {
         var3 = Math.max(var6, this.measureChildCollapseMargins(this.mSubtitleTextView, var1, var7 + var11, var2, var5 + var12, var14));
         var10 = var5 + this.mSubtitleTextView.getMeasuredHeight() + this.getVerticalMargins(this.mSubtitleTextView);
         var16 = ViewUtils.combineMeasuredStates(var4, ViewCompat.getMeasuredState(this.mSubtitleTextView));
      }

      var4 = Math.max(var15, var10);
      var10 = this.getPaddingLeft();
      var15 = this.getPaddingRight();
      var5 = this.getPaddingTop();
      var6 = this.getPaddingBottom();
      var3 = ViewCompat.resolveSizeAndState(Math.max(var7 + var3 + var10 + var15, this.getSuggestedMinimumWidth()), var1, -16777216 & var16);
      var1 = ViewCompat.resolveSizeAndState(Math.max(var4 + var5 + var6, this.getSuggestedMinimumHeight()), var2, var16 << 16);
      if(this.shouldCollapse()) {
         var1 = 0;
      }

      this.setMeasuredDimension(var3, var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if(!(var1 instanceof Toolbar.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         Toolbar.SavedState var2 = (Toolbar.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         MenuBuilder var3;
         if(this.mMenuView != null) {
            var3 = this.mMenuView.peekMenu();
         } else {
            var3 = null;
         }

         if(var2.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && var3 != null) {
            MenuItem var4 = var3.findItem(var2.expandedMenuItemId);
            if(var4 != null) {
               MenuItemCompat.expandActionView(var4);
            }
         }

         if(var2.isOverflowOpen) {
            this.postShowOverflowMenu();
         }
      }

   }

   public void onRtlPropertiesChanged(int var1) {
      boolean var2 = true;
      if(VERSION.SDK_INT >= 17) {
         super.onRtlPropertiesChanged(var1);
      }

      this.ensureContentInsets();
      RtlSpacingHelper var3 = this.mContentInsets;
      if(var1 != 1) {
         var2 = false;
      }

      var3.setDirection(var2);
   }

   protected Parcelable onSaveInstanceState() {
      Toolbar.SavedState var1 = new Toolbar.SavedState(super.onSaveInstanceState());
      if(this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
         var1.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
      }

      var1.isOverflowOpen = this.isOverflowMenuShowing();
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionMasked(var1);
      if(var2 == 0) {
         this.mEatingTouch = false;
      }

      if(!this.mEatingTouch) {
         boolean var3 = super.onTouchEvent(var1);
         if(var2 == 0 && !var3) {
            this.mEatingTouch = true;
         }
      }

      if(var2 == 1 || var2 == 3) {
         this.mEatingTouch = false;
      }

      return true;
   }

   void removeChildrenForExpandedActionView() {
      for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
         View var2 = this.getChildAt(var1);
         if(((Toolbar.LayoutParams)var2.getLayoutParams()).mViewType != 2 && var2 != this.mMenuView) {
            this.removeViewAt(var1);
            this.mHiddenViews.add(var2);
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setCollapsible(boolean var1) {
      this.mCollapsible = var1;
      this.requestLayout();
   }

   public void setContentInsetEndWithActions(int var1) {
      int var2 = var1;
      if(var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if(var2 != this.mContentInsetEndWithActions) {
         this.mContentInsetEndWithActions = var2;
         if(this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetStartWithNavigation(int var1) {
      int var2 = var1;
      if(var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if(var2 != this.mContentInsetStartWithNavigation) {
         this.mContentInsetStartWithNavigation = var2;
         if(this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetsAbsolute(int var1, int var2) {
      this.ensureContentInsets();
      this.mContentInsets.setAbsolute(var1, var2);
   }

   public void setContentInsetsRelative(int var1, int var2) {
      this.ensureContentInsets();
      this.mContentInsets.setRelative(var1, var2);
   }

   public void setLogo(@DrawableRes int var1) {
      this.setLogo(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setLogo(Drawable var1) {
      if(var1 != null) {
         this.ensureLogoView();
         if(!this.isChildOrHidden(this.mLogoView)) {
            this.addSystemView(this.mLogoView, true);
         }
      } else if(this.mLogoView != null && this.isChildOrHidden(this.mLogoView)) {
         this.removeView(this.mLogoView);
         this.mHiddenViews.remove(this.mLogoView);
      }

      if(this.mLogoView != null) {
         this.mLogoView.setImageDrawable(var1);
      }

   }

   public void setLogoDescription(@StringRes int var1) {
      this.setLogoDescription(this.getContext().getText(var1));
   }

   public void setLogoDescription(CharSequence var1) {
      if(!TextUtils.isEmpty(var1)) {
         this.ensureLogoView();
      }

      if(this.mLogoView != null) {
         this.mLogoView.setContentDescription(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setMenu(MenuBuilder var1, ActionMenuPresenter var2) {
      if(var1 != null || this.mMenuView != null) {
         this.ensureMenuView();
         MenuBuilder var3 = this.mMenuView.peekMenu();
         if(var3 != var1) {
            if(var3 != null) {
               var3.removeMenuPresenter(this.mOuterActionMenuPresenter);
               var3.removeMenuPresenter(this.mExpandedMenuPresenter);
            }

            if(this.mExpandedMenuPresenter == null) {
               this.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
            }

            var2.setExpandedActionViewsExclusive(true);
            if(var1 != null) {
               var1.addMenuPresenter(var2, this.mPopupContext);
               var1.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
            } else {
               var2.initForMenu(this.mPopupContext, (MenuBuilder)null);
               this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, (MenuBuilder)null);
               var2.updateMenuView(true);
               this.mExpandedMenuPresenter.updateMenuView(true);
            }

            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setPresenter(var2);
            this.mOuterActionMenuPresenter = var2;
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
      if(this.mMenuView != null) {
         this.mMenuView.setMenuCallbacks(var1, var2);
      }

   }

   public void setNavigationContentDescription(@StringRes int var1) {
      CharSequence var2;
      if(var1 != 0) {
         var2 = this.getContext().getText(var1);
      } else {
         var2 = null;
      }

      this.setNavigationContentDescription(var2);
   }

   public void setNavigationContentDescription(@Nullable CharSequence var1) {
      if(!TextUtils.isEmpty(var1)) {
         this.ensureNavButtonView();
      }

      if(this.mNavButtonView != null) {
         this.mNavButtonView.setContentDescription(var1);
      }

   }

   public void setNavigationIcon(@DrawableRes int var1) {
      this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setNavigationIcon(@Nullable Drawable var1) {
      if(var1 != null) {
         this.ensureNavButtonView();
         if(!this.isChildOrHidden(this.mNavButtonView)) {
            this.addSystemView(this.mNavButtonView, true);
         }
      } else if(this.mNavButtonView != null && this.isChildOrHidden(this.mNavButtonView)) {
         this.removeView(this.mNavButtonView);
         this.mHiddenViews.remove(this.mNavButtonView);
      }

      if(this.mNavButtonView != null) {
         this.mNavButtonView.setImageDrawable(var1);
      }

   }

   public void setNavigationOnClickListener(OnClickListener var1) {
      this.ensureNavButtonView();
      this.mNavButtonView.setOnClickListener(var1);
   }

   public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(@Nullable Drawable var1) {
      this.ensureMenu();
      this.mMenuView.setOverflowIcon(var1);
   }

   public void setPopupTheme(@StyleRes int var1) {
      if(this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if(var1 == 0) {
            this.mPopupContext = this.getContext();
         } else {
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
         }
      }

   }

   public void setSubtitle(@StringRes int var1) {
      this.setSubtitle(this.getContext().getText(var1));
   }

   public void setSubtitle(CharSequence var1) {
      if(!TextUtils.isEmpty(var1)) {
         if(this.mSubtitleTextView == null) {
            Context var2 = this.getContext();
            this.mSubtitleTextView = new AppCompatTextView(var2);
            this.mSubtitleTextView.setSingleLine();
            this.mSubtitleTextView.setEllipsize(TruncateAt.END);
            if(this.mSubtitleTextAppearance != 0) {
               this.mSubtitleTextView.setTextAppearance(var2, this.mSubtitleTextAppearance);
            }

            if(this.mSubtitleTextColor != 0) {
               this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor);
            }
         }

         if(!this.isChildOrHidden(this.mSubtitleTextView)) {
            this.addSystemView(this.mSubtitleTextView, true);
         }
      } else if(this.mSubtitleTextView != null && this.isChildOrHidden(this.mSubtitleTextView)) {
         this.removeView(this.mSubtitleTextView);
         this.mHiddenViews.remove(this.mSubtitleTextView);
      }

      if(this.mSubtitleTextView != null) {
         this.mSubtitleTextView.setText(var1);
      }

      this.mSubtitleText = var1;
   }

   public void setSubtitleTextAppearance(Context var1, @StyleRes int var2) {
      this.mSubtitleTextAppearance = var2;
      if(this.mSubtitleTextView != null) {
         this.mSubtitleTextView.setTextAppearance(var1, var2);
      }

   }

   public void setSubtitleTextColor(@ColorInt int var1) {
      this.mSubtitleTextColor = var1;
      if(this.mSubtitleTextView != null) {
         this.mSubtitleTextView.setTextColor(var1);
      }

   }

   public void setTitle(@StringRes int var1) {
      this.setTitle(this.getContext().getText(var1));
   }

   public void setTitle(CharSequence var1) {
      if(!TextUtils.isEmpty(var1)) {
         if(this.mTitleTextView == null) {
            Context var2 = this.getContext();
            this.mTitleTextView = new AppCompatTextView(var2);
            this.mTitleTextView.setSingleLine();
            this.mTitleTextView.setEllipsize(TruncateAt.END);
            if(this.mTitleTextAppearance != 0) {
               this.mTitleTextView.setTextAppearance(var2, this.mTitleTextAppearance);
            }

            if(this.mTitleTextColor != 0) {
               this.mTitleTextView.setTextColor(this.mTitleTextColor);
            }
         }

         if(!this.isChildOrHidden(this.mTitleTextView)) {
            this.addSystemView(this.mTitleTextView, true);
         }
      } else if(this.mTitleTextView != null && this.isChildOrHidden(this.mTitleTextView)) {
         this.removeView(this.mTitleTextView);
         this.mHiddenViews.remove(this.mTitleTextView);
      }

      if(this.mTitleTextView != null) {
         this.mTitleTextView.setText(var1);
      }

      this.mTitleText = var1;
   }

   public void setTitleMargin(int var1, int var2, int var3, int var4) {
      this.mTitleMarginStart = var1;
      this.mTitleMarginTop = var2;
      this.mTitleMarginEnd = var3;
      this.mTitleMarginBottom = var4;
      this.requestLayout();
   }

   public void setTitleMarginBottom(int var1) {
      this.mTitleMarginBottom = var1;
      this.requestLayout();
   }

   public void setTitleMarginEnd(int var1) {
      this.mTitleMarginEnd = var1;
      this.requestLayout();
   }

   public void setTitleMarginStart(int var1) {
      this.mTitleMarginStart = var1;
      this.requestLayout();
   }

   public void setTitleMarginTop(int var1) {
      this.mTitleMarginTop = var1;
      this.requestLayout();
   }

   public void setTitleTextAppearance(Context var1, @StyleRes int var2) {
      this.mTitleTextAppearance = var2;
      if(this.mTitleTextView != null) {
         this.mTitleTextView.setTextAppearance(var1, var2);
      }

   }

   public void setTitleTextColor(@ColorInt int var1) {
      this.mTitleTextColor = var1;
      if(this.mTitleTextView != null) {
         this.mTitleTextView.setTextColor(var1);
      }

   }

   public boolean showOverflowMenu() {
      boolean var1;
      if(this.mMenuView != null && this.mMenuView.showOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private class ExpandedActionViewMenuPresenter implements MenuPresenter {
      MenuItemImpl mCurrentExpandedItem;
      MenuBuilder mMenu;

      public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         if(Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
         }

         Toolbar.this.removeView(Toolbar.this.mExpandedActionView);
         Toolbar.this.removeView(Toolbar.this.mCollapseButtonView);
         Toolbar.this.mExpandedActionView = null;
         Toolbar.this.addChildrenForExpandedActionView();
         this.mCurrentExpandedItem = null;
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(false);
         return true;
      }

      public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         Toolbar.this.ensureCollapseButtonView();
         if(Toolbar.this.mCollapseButtonView.getParent() != Toolbar.this) {
            Toolbar.this.addView(Toolbar.this.mCollapseButtonView);
         }

         Toolbar.this.mExpandedActionView = var2.getActionView();
         this.mCurrentExpandedItem = var2;
         if(Toolbar.this.mExpandedActionView.getParent() != Toolbar.this) {
            Toolbar.LayoutParams var3 = Toolbar.this.generateDefaultLayoutParams();
            var3.gravity = 8388611 | Toolbar.this.mButtonGravity & 112;
            var3.mViewType = 2;
            Toolbar.this.mExpandedActionView.setLayoutParams(var3);
            Toolbar.this.addView(Toolbar.this.mExpandedActionView);
         }

         Toolbar.this.removeChildrenForExpandedActionView();
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(true);
         if(Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
         }

         return true;
      }

      public boolean flagActionItems() {
         return false;
      }

      public int getId() {
         return 0;
      }

      public MenuView getMenuView(ViewGroup var1) {
         return null;
      }

      public void initForMenu(Context var1, MenuBuilder var2) {
         if(this.mMenu != null && this.mCurrentExpandedItem != null) {
            this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
         }

         this.mMenu = var2;
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public void onRestoreInstanceState(Parcelable var1) {
      }

      public Parcelable onSaveInstanceState() {
         return null;
      }

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         return false;
      }

      public void setCallback(MenuPresenter.Callback var1) {
      }

      public void updateMenuView(boolean var1) {
         if(this.mCurrentExpandedItem != null) {
            boolean var4 = false;
            boolean var3 = var4;
            if(this.mMenu != null) {
               int var5 = this.mMenu.size();
               int var2 = 0;

               while(true) {
                  var3 = var4;
                  if(var2 >= var5) {
                     break;
                  }

                  if(this.mMenu.getItem(var2) == this.mCurrentExpandedItem) {
                     var3 = true;
                     break;
                  }

                  ++var2;
               }
            }

            if(!var3) {
               this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
         }

      }
   }

   public static class LayoutParams extends ActionBar.LayoutParams {
      static final int CUSTOM = 0;
      static final int EXPANDED = 2;
      static final int SYSTEM = 1;
      int mViewType;

      public LayoutParams(int var1) {
         this(-2, -1, var1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.mViewType = 0;
         this.gravity = 8388627;
      }

      public LayoutParams(int var1, int var2, int var3) {
         super(var1, var2);
         this.mViewType = 0;
         this.gravity = var3;
      }

      public LayoutParams(@NonNull Context var1, AttributeSet var2) {
         super(var1, var2);
         this.mViewType = 0;
      }

      public LayoutParams(ActionBar.LayoutParams var1) {
         super(var1);
         this.mViewType = 0;
      }

      public LayoutParams(Toolbar.LayoutParams var1) {
         super((ActionBar.LayoutParams)var1);
         this.mViewType = 0;
         this.mViewType = var1.mViewType;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.mViewType = 0;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.mViewType = 0;
         this.copyMarginsFromCompat(var1);
      }

      void copyMarginsFromCompat(MarginLayoutParams var1) {
         this.leftMargin = var1.leftMargin;
         this.topMargin = var1.topMargin;
         this.rightMargin = var1.rightMargin;
         this.bottomMargin = var1.bottomMargin;
      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public Toolbar.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new Toolbar.SavedState(var1, var2);
         }

         public Toolbar.SavedState[] newArray(int var1) {
            return new Toolbar.SavedState[var1];
         }
      });
      int expandedMenuItemId;
      boolean isOverflowOpen;

      public SavedState(Parcel var1) {
         this(var1, (ClassLoader)null);
      }

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.expandedMenuItemId = var1.readInt();
         boolean var3;
         if(var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isOverflowOpen = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.expandedMenuItemId);
         byte var3;
         if(this.isOverflowOpen) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         var1.writeInt(var3);
      }
   }
}
