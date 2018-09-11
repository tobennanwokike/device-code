package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.appcompat.R;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import java.lang.ref.WeakReference;

class AlertController {
   ListAdapter mAdapter;
   private int mAlertDialogLayout;
   private final OnClickListener mButtonHandler = new OnClickListener() {
      public void onClick(View var1) {
         Message var2;
         if(var1 == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonPositiveMessage);
         } else if(var1 == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonNegativeMessage);
         } else if(var1 == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonNeutralMessage);
         } else {
            var2 = null;
         }

         if(var2 != null) {
            var2.sendToTarget();
         }

         AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
      }
   };
   Button mButtonNegative;
   Message mButtonNegativeMessage;
   private CharSequence mButtonNegativeText;
   Button mButtonNeutral;
   Message mButtonNeutralMessage;
   private CharSequence mButtonNeutralText;
   private int mButtonPanelLayoutHint = 0;
   private int mButtonPanelSideLayout;
   Button mButtonPositive;
   Message mButtonPositiveMessage;
   private CharSequence mButtonPositiveText;
   int mCheckedItem = -1;
   private final Context mContext;
   private View mCustomTitleView;
   final AppCompatDialog mDialog;
   Handler mHandler;
   private Drawable mIcon;
   private int mIconId = 0;
   private ImageView mIconView;
   int mListItemLayout;
   int mListLayout;
   ListView mListView;
   private CharSequence mMessage;
   private TextView mMessageView;
   int mMultiChoiceItemLayout;
   NestedScrollView mScrollView;
   int mSingleChoiceItemLayout;
   private CharSequence mTitle;
   private TextView mTitleView;
   private View mView;
   private int mViewLayoutResId;
   private int mViewSpacingBottom;
   private int mViewSpacingLeft;
   private int mViewSpacingRight;
   private boolean mViewSpacingSpecified = false;
   private int mViewSpacingTop;
   private final Window mWindow;

   public AlertController(Context var1, AppCompatDialog var2, Window var3) {
      this.mContext = var1;
      this.mDialog = var2;
      this.mWindow = var3;
      this.mHandler = new AlertController.ButtonHandler(var2);
      TypedArray var4 = var1.obtainStyledAttributes((AttributeSet)null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
      this.mAlertDialogLayout = var4.getResourceId(R.styleable.AlertDialog_android_layout, 0);
      this.mButtonPanelSideLayout = var4.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
      this.mListLayout = var4.getResourceId(R.styleable.AlertDialog_listLayout, 0);
      this.mMultiChoiceItemLayout = var4.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
      this.mSingleChoiceItemLayout = var4.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
      this.mListItemLayout = var4.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
      var4.recycle();
      var2.supportRequestWindowFeature(1);
   }

   static boolean canTextInput(View var0) {
      boolean var3 = true;
      if(!var0.onCheckIsTextEditor()) {
         if(!(var0 instanceof ViewGroup)) {
            var3 = false;
         } else {
            ViewGroup var4 = (ViewGroup)var0;
            int var1 = var4.getChildCount();

            int var2;
            do {
               if(var1 <= 0) {
                  var3 = false;
                  break;
               }

               var2 = var1 - 1;
               var1 = var2;
            } while(!canTextInput(var4.getChildAt(var2)));
         }
      }

      return var3;
   }

   static void manageScrollIndicators(View var0, View var1, View var2) {
      byte var4 = 0;
      byte var3;
      if(var1 != null) {
         if(ViewCompat.canScrollVertically(var0, -1)) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var1.setVisibility(var3);
      }

      if(var2 != null) {
         if(ViewCompat.canScrollVertically(var0, 1)) {
            var3 = var4;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
      }

   }

   @Nullable
   private ViewGroup resolvePanel(@Nullable View var1, @Nullable View var2) {
      ViewGroup var4;
      if(var1 == null) {
         var1 = var2;
         if(var2 instanceof ViewStub) {
            var1 = ((ViewStub)var2).inflate();
         }

         var4 = (ViewGroup)var1;
      } else {
         if(var2 != null) {
            ViewParent var3 = var2.getParent();
            if(var3 instanceof ViewGroup) {
               ((ViewGroup)var3).removeView(var2);
            }
         }

         var2 = var1;
         if(var1 instanceof ViewStub) {
            var2 = ((ViewStub)var1).inflate();
         }

         var4 = (ViewGroup)var2;
      }

      return var4;
   }

   private int selectContentView() {
      int var1;
      if(this.mButtonPanelSideLayout == 0) {
         var1 = this.mAlertDialogLayout;
      } else if(this.mButtonPanelLayoutHint == 1) {
         var1 = this.mButtonPanelSideLayout;
      } else {
         var1 = this.mAlertDialogLayout;
      }

      return var1;
   }

   private void setScrollIndicators(ViewGroup var1, final View var2, int var3, int var4) {
      final View var5 = this.mWindow.findViewById(R.id.scrollIndicatorUp);
      View var6 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
      if(VERSION.SDK_INT >= 23) {
         ViewCompat.setScrollIndicators(var2, var3, var4);
         if(var5 != null) {
            var1.removeView(var5);
         }

         if(var6 != null) {
            var1.removeView(var6);
         }
      } else {
         var2 = var5;
         if(var5 != null) {
            var2 = var5;
            if((var3 & 1) == 0) {
               var1.removeView(var5);
               var2 = null;
            }
         }

         var5 = var6;
         if(var6 != null) {
            var5 = var6;
            if((var3 & 2) == 0) {
               var1.removeView(var6);
               var5 = null;
            }
         }

         if(var2 != null || var5 != null) {
            if(this.mMessage != null) {
               this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                  public void onScrollChange(NestedScrollView var1, int var2x, int var3, int var4, int var5x) {
                     AlertController.manageScrollIndicators(var1, var2, var5);
                  }
               });
               this.mScrollView.post(new Runnable() {
                  public void run() {
                     AlertController.manageScrollIndicators(AlertController.this.mScrollView, var2, var5);
                  }
               });
            } else if(this.mListView != null) {
               this.mListView.setOnScrollListener(new OnScrollListener() {
                  public void onScroll(AbsListView var1, int var2x, int var3, int var4) {
                     AlertController.manageScrollIndicators(var1, var2, var5);
                  }

                  public void onScrollStateChanged(AbsListView var1, int var2x) {
                  }
               });
               this.mListView.post(new Runnable() {
                  public void run() {
                     AlertController.manageScrollIndicators(AlertController.this.mListView, var2, var5);
                  }
               });
            } else {
               if(var2 != null) {
                  var1.removeView(var2);
               }

               if(var5 != null) {
                  var1.removeView(var5);
               }
            }
         }
      }

   }

   private void setupButtons(ViewGroup var1) {
      boolean var3 = false;
      int var2 = 0;
      this.mButtonPositive = (Button)var1.findViewById(16908313);
      this.mButtonPositive.setOnClickListener(this.mButtonHandler);
      if(TextUtils.isEmpty(this.mButtonPositiveText)) {
         this.mButtonPositive.setVisibility(8);
      } else {
         this.mButtonPositive.setText(this.mButtonPositiveText);
         this.mButtonPositive.setVisibility(0);
         var2 = 0 | 1;
      }

      this.mButtonNegative = (Button)var1.findViewById(16908314);
      this.mButtonNegative.setOnClickListener(this.mButtonHandler);
      if(TextUtils.isEmpty(this.mButtonNegativeText)) {
         this.mButtonNegative.setVisibility(8);
      } else {
         this.mButtonNegative.setText(this.mButtonNegativeText);
         this.mButtonNegative.setVisibility(0);
         var2 |= 2;
      }

      this.mButtonNeutral = (Button)var1.findViewById(16908315);
      this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
      if(TextUtils.isEmpty(this.mButtonNeutralText)) {
         this.mButtonNeutral.setVisibility(8);
      } else {
         this.mButtonNeutral.setText(this.mButtonNeutralText);
         this.mButtonNeutral.setVisibility(0);
         var2 |= 4;
      }

      if(var2 != 0) {
         var3 = true;
      }

      if(!var3) {
         var1.setVisibility(8);
      }

   }

   private void setupContent(ViewGroup var1) {
      this.mScrollView = (NestedScrollView)this.mWindow.findViewById(R.id.scrollView);
      this.mScrollView.setFocusable(false);
      this.mScrollView.setNestedScrollingEnabled(false);
      this.mMessageView = (TextView)var1.findViewById(16908299);
      if(this.mMessageView != null) {
         if(this.mMessage != null) {
            this.mMessageView.setText(this.mMessage);
         } else {
            this.mMessageView.setVisibility(8);
            this.mScrollView.removeView(this.mMessageView);
            if(this.mListView != null) {
               var1 = (ViewGroup)this.mScrollView.getParent();
               int var2 = var1.indexOfChild(this.mScrollView);
               var1.removeViewAt(var2);
               var1.addView(this.mListView, var2, new LayoutParams(-1, -1));
            } else {
               var1.setVisibility(8);
            }
         }
      }

   }

   private void setupCustomContent(ViewGroup var1) {
      boolean var2 = false;
      View var3;
      if(this.mView != null) {
         var3 = this.mView;
      } else if(this.mViewLayoutResId != 0) {
         var3 = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, var1, false);
      } else {
         var3 = null;
      }

      if(var3 != null) {
         var2 = true;
      }

      if(!var2 || !canTextInput(var3)) {
         this.mWindow.setFlags(131072, 131072);
      }

      if(var2) {
         FrameLayout var4 = (FrameLayout)this.mWindow.findViewById(R.id.custom);
         var4.addView(var3, new LayoutParams(-1, -1));
         if(this.mViewSpacingSpecified) {
            var4.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
         }

         if(this.mListView != null) {
            ((android.widget.LinearLayout.LayoutParams)var1.getLayoutParams()).weight = 0.0F;
         }
      } else {
         var1.setVisibility(8);
      }

   }

   private void setupTitle(ViewGroup var1) {
      boolean var2 = false;
      if(this.mCustomTitleView != null) {
         LayoutParams var3 = new LayoutParams(-1, -2);
         var1.addView(this.mCustomTitleView, 0, var3);
         this.mWindow.findViewById(R.id.title_template).setVisibility(8);
      } else {
         this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
         if(!TextUtils.isEmpty(this.mTitle)) {
            var2 = true;
         }

         if(var2) {
            this.mTitleView = (TextView)this.mWindow.findViewById(R.id.alertTitle);
            this.mTitleView.setText(this.mTitle);
            if(this.mIconId != 0) {
               this.mIconView.setImageResource(this.mIconId);
            } else if(this.mIcon != null) {
               this.mIconView.setImageDrawable(this.mIcon);
            } else {
               this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
               this.mIconView.setVisibility(8);
            }
         } else {
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            var1.setVisibility(8);
         }
      }

   }

   private void setupView() {
      View var6 = this.mWindow.findViewById(R.id.parentPanel);
      View var8 = var6.findViewById(R.id.topPanel);
      View var5 = var6.findViewById(R.id.contentPanel);
      View var4 = var6.findViewById(R.id.buttonPanel);
      ViewGroup var18 = (ViewGroup)var6.findViewById(R.id.customPanel);
      this.setupCustomContent(var18);
      View var10 = var18.findViewById(R.id.topPanel);
      View var9 = var18.findViewById(R.id.contentPanel);
      View var7 = var18.findViewById(R.id.buttonPanel);
      ViewGroup var19 = this.resolvePanel(var10, var8);
      ViewGroup var16 = this.resolvePanel(var9, var5);
      ViewGroup var14 = this.resolvePanel(var7, var4);
      this.setupContent(var16);
      this.setupButtons(var14);
      this.setupTitle(var19);
      boolean var1;
      if(var18 != null && var18.getVisibility() != 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var3;
      if(var19 != null && var19.getVisibility() != 8) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var2;
      if(var14 != null && var14.getVisibility() != 8) {
         var2 = true;
      } else {
         var2 = false;
      }

      if(!var2 && var16 != null) {
         var4 = var16.findViewById(R.id.textSpacerNoButtons);
         if(var4 != null) {
            var4.setVisibility(0);
         }
      }

      if(var3 && this.mScrollView != null) {
         this.mScrollView.setClipToPadding(true);
      }

      if(!var1) {
         Object var15;
         if(this.mListView != null) {
            var15 = this.mListView;
         } else {
            var15 = this.mScrollView;
         }

         if(var15 != null) {
            byte var11;
            if(var3) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            byte var13;
            if(var2) {
               var13 = 2;
            } else {
               var13 = 0;
            }

            this.setScrollIndicators(var16, (View)var15, var11 | var13, 3);
         }
      }

      ListView var17 = this.mListView;
      if(var17 != null && this.mAdapter != null) {
         var17.setAdapter(this.mAdapter);
         int var12 = this.mCheckedItem;
         if(var12 > -1) {
            var17.setItemChecked(var12, true);
            var17.setSelection(var12);
         }
      }

   }

   public Button getButton(int var1) {
      Button var2;
      switch(var1) {
      case -3:
         var2 = this.mButtonNeutral;
         break;
      case -2:
         var2 = this.mButtonNegative;
         break;
      case -1:
         var2 = this.mButtonPositive;
         break;
      default:
         var2 = null;
      }

      return var2;
   }

   public int getIconAttributeResId(int var1) {
      TypedValue var2 = new TypedValue();
      this.mContext.getTheme().resolveAttribute(var1, var2, true);
      return var2.resourceId;
   }

   public ListView getListView() {
      return this.mListView;
   }

   public void installContent() {
      int var1 = this.selectContentView();
      this.mDialog.setContentView(var1);
      this.setupView();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if(this.mScrollView != null && this.mScrollView.executeKeyEvent(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3;
      if(this.mScrollView != null && this.mScrollView.executeKeyEvent(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void setButton(int var1, CharSequence var2, android.content.DialogInterface.OnClickListener var3, Message var4) {
      Message var5 = var4;
      if(var4 == null) {
         var5 = var4;
         if(var3 != null) {
            var5 = this.mHandler.obtainMessage(var1, var3);
         }
      }

      switch(var1) {
      case -3:
         this.mButtonNeutralText = var2;
         this.mButtonNeutralMessage = var5;
         break;
      case -2:
         this.mButtonNegativeText = var2;
         this.mButtonNegativeMessage = var5;
         break;
      case -1:
         this.mButtonPositiveText = var2;
         this.mButtonPositiveMessage = var5;
         break;
      default:
         throw new IllegalArgumentException("Button does not exist");
      }

   }

   public void setButtonPanelLayoutHint(int var1) {
      this.mButtonPanelLayoutHint = var1;
   }

   public void setCustomTitle(View var1) {
      this.mCustomTitleView = var1;
   }

   public void setIcon(int var1) {
      this.mIcon = null;
      this.mIconId = var1;
      if(this.mIconView != null) {
         if(var1 != 0) {
            this.mIconView.setVisibility(0);
            this.mIconView.setImageResource(this.mIconId);
         } else {
            this.mIconView.setVisibility(8);
         }
      }

   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      this.mIconId = 0;
      if(this.mIconView != null) {
         if(var1 != null) {
            this.mIconView.setVisibility(0);
            this.mIconView.setImageDrawable(var1);
         } else {
            this.mIconView.setVisibility(8);
         }
      }

   }

   public void setMessage(CharSequence var1) {
      this.mMessage = var1;
      if(this.mMessageView != null) {
         this.mMessageView.setText(var1);
      }

   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      if(this.mTitleView != null) {
         this.mTitleView.setText(var1);
      }

   }

   public void setView(int var1) {
      this.mView = null;
      this.mViewLayoutResId = var1;
      this.mViewSpacingSpecified = false;
   }

   public void setView(View var1) {
      this.mView = var1;
      this.mViewLayoutResId = 0;
      this.mViewSpacingSpecified = false;
   }

   public void setView(View var1, int var2, int var3, int var4, int var5) {
      this.mView = var1;
      this.mViewLayoutResId = 0;
      this.mViewSpacingSpecified = true;
      this.mViewSpacingLeft = var2;
      this.mViewSpacingTop = var3;
      this.mViewSpacingRight = var4;
      this.mViewSpacingBottom = var5;
   }

   public static class AlertParams {
      public ListAdapter mAdapter;
      public boolean mCancelable;
      public int mCheckedItem = -1;
      public boolean[] mCheckedItems;
      public final Context mContext;
      public Cursor mCursor;
      public View mCustomTitleView;
      public boolean mForceInverseBackground;
      public Drawable mIcon;
      public int mIconAttrId = 0;
      public int mIconId = 0;
      public final LayoutInflater mInflater;
      public String mIsCheckedColumn;
      public boolean mIsMultiChoice;
      public boolean mIsSingleChoice;
      public CharSequence[] mItems;
      public String mLabelColumn;
      public CharSequence mMessage;
      public android.content.DialogInterface.OnClickListener mNegativeButtonListener;
      public CharSequence mNegativeButtonText;
      public android.content.DialogInterface.OnClickListener mNeutralButtonListener;
      public CharSequence mNeutralButtonText;
      public OnCancelListener mOnCancelListener;
      public OnMultiChoiceClickListener mOnCheckboxClickListener;
      public android.content.DialogInterface.OnClickListener mOnClickListener;
      public OnDismissListener mOnDismissListener;
      public OnItemSelectedListener mOnItemSelectedListener;
      public OnKeyListener mOnKeyListener;
      public AlertController.OnPrepareListViewListener mOnPrepareListViewListener;
      public android.content.DialogInterface.OnClickListener mPositiveButtonListener;
      public CharSequence mPositiveButtonText;
      public boolean mRecycleOnMeasure = true;
      public CharSequence mTitle;
      public View mView;
      public int mViewLayoutResId;
      public int mViewSpacingBottom;
      public int mViewSpacingLeft;
      public int mViewSpacingRight;
      public boolean mViewSpacingSpecified = false;
      public int mViewSpacingTop;

      public AlertParams(Context var1) {
         this.mContext = var1;
         this.mCancelable = true;
         this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
      }

      private void createListView(final AlertController var1) {
         final ListView var4 = (ListView)this.mInflater.inflate(var1.mListLayout, (ViewGroup)null);
         Object var3;
         if(this.mIsMultiChoice) {
            if(this.mCursor == null) {
               var3 = new ArrayAdapter(this.mContext, var1.mMultiChoiceItemLayout, 16908308, this.mItems) {
                  public View getView(int var1, View var2, ViewGroup var3) {
                     var2 = super.getView(var1, var2, var3);
                     if(AlertParams.this.mCheckedItems != null && AlertParams.this.mCheckedItems[var1]) {
                        var4.setItemChecked(var1, true);
                     }

                     return var2;
                  }
               };
            } else {
               var3 = new CursorAdapter(this.mContext, this.mCursor, false) {
                  private final int mIsCheckedIndex;
                  private final int mLabelIndex;

                  {
                     Cursor var7 = this.getCursor();
                     this.mLabelIndex = var7.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                     this.mIsCheckedIndex = var7.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                  }

                  public void bindView(View var1x, Context var2, Cursor var3) {
                     boolean var5 = true;
                     ((CheckedTextView)var1x.findViewById(16908308)).setText(var3.getString(this.mLabelIndex));
                     ListView var6 = var4;
                     int var4x = var3.getPosition();
                     if(var3.getInt(this.mIsCheckedIndex) != 1) {
                        var5 = false;
                     }

                     var6.setItemChecked(var4x, var5);
                  }

                  public View newView(Context var1x, Cursor var2, ViewGroup var3) {
                     return AlertParams.this.mInflater.inflate(var1.mMultiChoiceItemLayout, var3, false);
                  }
               };
            }
         } else {
            int var2;
            if(this.mIsSingleChoice) {
               var2 = var1.mSingleChoiceItemLayout;
            } else {
               var2 = var1.mListItemLayout;
            }

            if(this.mCursor != null) {
               var3 = new SimpleCursorAdapter(this.mContext, var2, this.mCursor, new String[]{this.mLabelColumn}, new int[]{16908308});
            } else if(this.mAdapter != null) {
               var3 = this.mAdapter;
            } else {
               var3 = new AlertController.CheckedItemAdapter(this.mContext, var2, 16908308, this.mItems);
            }
         }

         if(this.mOnPrepareListViewListener != null) {
            this.mOnPrepareListViewListener.onPrepareListView(var4);
         }

         var1.mAdapter = (ListAdapter)var3;
         var1.mCheckedItem = this.mCheckedItem;
         if(this.mOnClickListener != null) {
            var4.setOnItemClickListener(new OnItemClickListener() {
               public void onItemClick(AdapterView var1x, View var2, int var3, long var4) {
                  AlertParams.this.mOnClickListener.onClick(var1.mDialog, var3);
                  if(!AlertParams.this.mIsSingleChoice) {
                     var1.mDialog.dismiss();
                  }

               }
            });
         } else if(this.mOnCheckboxClickListener != null) {
            var4.setOnItemClickListener(new OnItemClickListener() {
               public void onItemClick(AdapterView var1x, View var2, int var3, long var4x) {
                  if(AlertParams.this.mCheckedItems != null) {
                     AlertParams.this.mCheckedItems[var3] = var4.isItemChecked(var3);
                  }

                  AlertParams.this.mOnCheckboxClickListener.onClick(var1.mDialog, var3, var4.isItemChecked(var3));
               }
            });
         }

         if(this.mOnItemSelectedListener != null) {
            var4.setOnItemSelectedListener(this.mOnItemSelectedListener);
         }

         if(this.mIsSingleChoice) {
            var4.setChoiceMode(1);
         } else if(this.mIsMultiChoice) {
            var4.setChoiceMode(2);
         }

         var1.mListView = var4;
      }

      public void apply(AlertController var1) {
         if(this.mCustomTitleView != null) {
            var1.setCustomTitle(this.mCustomTitleView);
         } else {
            if(this.mTitle != null) {
               var1.setTitle(this.mTitle);
            }

            if(this.mIcon != null) {
               var1.setIcon(this.mIcon);
            }

            if(this.mIconId != 0) {
               var1.setIcon(this.mIconId);
            }

            if(this.mIconAttrId != 0) {
               var1.setIcon(var1.getIconAttributeResId(this.mIconAttrId));
            }
         }

         if(this.mMessage != null) {
            var1.setMessage(this.mMessage);
         }

         if(this.mPositiveButtonText != null) {
            var1.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, (Message)null);
         }

         if(this.mNegativeButtonText != null) {
            var1.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, (Message)null);
         }

         if(this.mNeutralButtonText != null) {
            var1.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, (Message)null);
         }

         if(this.mItems != null || this.mCursor != null || this.mAdapter != null) {
            this.createListView(var1);
         }

         if(this.mView != null) {
            if(this.mViewSpacingSpecified) {
               var1.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            } else {
               var1.setView(this.mView);
            }
         } else if(this.mViewLayoutResId != 0) {
            var1.setView(this.mViewLayoutResId);
         }

      }
   }

   public interface OnPrepareListViewListener {
      void onPrepareListView(ListView var1);
   }

   private static final class ButtonHandler extends Handler {
      private static final int MSG_DISMISS_DIALOG = 1;
      private WeakReference mDialog;

      public ButtonHandler(DialogInterface var1) {
         this.mDialog = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         switch(var1.what) {
         case -3:
         case -2:
         case -1:
            ((android.content.DialogInterface.OnClickListener)var1.obj).onClick((DialogInterface)this.mDialog.get(), var1.what);
         case 0:
         default:
            break;
         case 1:
            ((DialogInterface)var1.obj).dismiss();
         }

      }
   }

   private static class CheckedItemAdapter extends ArrayAdapter {
      public CheckedItemAdapter(Context var1, int var2, int var3, CharSequence[] var4) {
         super(var1, var2, var3, var4);
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public boolean hasStableIds() {
         return true;
      }
   }
}
