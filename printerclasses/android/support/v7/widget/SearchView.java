package android.support.v7.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SuggestionsAdapter;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.KeyEvent.DispatcherState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
   static final boolean DBG = false;
   static final SearchView.AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new SearchView.AutoCompleteTextViewReflector();
   private static final String IME_OPTION_NO_MICROPHONE = "nm";
   static final String LOG_TAG = "SearchView";
   private Bundle mAppSearchData;
   private boolean mClearingFocus;
   final ImageView mCloseButton;
   private final ImageView mCollapsedIcon;
   private int mCollapsedImeOptions;
   private final CharSequence mDefaultQueryHint;
   private final View mDropDownAnchor;
   private boolean mExpandedInActionView;
   final ImageView mGoButton;
   private boolean mIconified;
   private boolean mIconifiedByDefault;
   private int mMaxWidth;
   private CharSequence mOldQueryText;
   private final OnClickListener mOnClickListener;
   private SearchView.OnCloseListener mOnCloseListener;
   private final OnEditorActionListener mOnEditorActionListener;
   private final OnItemClickListener mOnItemClickListener;
   private final OnItemSelectedListener mOnItemSelectedListener;
   private SearchView.OnQueryTextListener mOnQueryChangeListener;
   OnFocusChangeListener mOnQueryTextFocusChangeListener;
   private OnClickListener mOnSearchClickListener;
   private SearchView.OnSuggestionListener mOnSuggestionListener;
   private final WeakHashMap mOutsideDrawablesCache;
   private CharSequence mQueryHint;
   private boolean mQueryRefinement;
   private Runnable mReleaseCursorRunnable;
   final ImageView mSearchButton;
   private final View mSearchEditFrame;
   private final Drawable mSearchHintIcon;
   private final View mSearchPlate;
   final SearchView.SearchAutoComplete mSearchSrcTextView;
   private Rect mSearchSrcTextViewBounds;
   private Rect mSearchSrtTextViewBoundsExpanded;
   SearchableInfo mSearchable;
   private Runnable mShowImeRunnable;
   private final View mSubmitArea;
   private boolean mSubmitButtonEnabled;
   private final int mSuggestionCommitIconResId;
   private final int mSuggestionRowLayout;
   CursorAdapter mSuggestionsAdapter;
   private int[] mTemp;
   private int[] mTemp2;
   OnKeyListener mTextKeyListener;
   private TextWatcher mTextWatcher;
   private SearchView.UpdatableTouchDelegate mTouchDelegate;
   private final Runnable mUpdateDrawableStateRunnable;
   private CharSequence mUserQuery;
   private final Intent mVoiceAppSearchIntent;
   final ImageView mVoiceButton;
   private boolean mVoiceButtonEnabled;
   private final Intent mVoiceWebSearchIntent;

   public SearchView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SearchView(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.searchViewStyle);
   }

   public SearchView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSearchSrcTextViewBounds = new Rect();
      this.mSearchSrtTextViewBoundsExpanded = new Rect();
      this.mTemp = new int[2];
      this.mTemp2 = new int[2];
      this.mShowImeRunnable = new Runnable() {
         public void run() {
            InputMethodManager var1 = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
            if(var1 != null) {
               SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(var1, SearchView.this, 0);
            }

         }
      };
      this.mUpdateDrawableStateRunnable = new Runnable() {
         public void run() {
            SearchView.this.updateFocusedState();
         }
      };
      this.mReleaseCursorRunnable = new Runnable() {
         public void run() {
            if(SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
               SearchView.this.mSuggestionsAdapter.changeCursor((Cursor)null);
            }

         }
      };
      this.mOutsideDrawablesCache = new WeakHashMap();
      this.mOnClickListener = new OnClickListener() {
         public void onClick(View var1) {
            if(var1 == SearchView.this.mSearchButton) {
               SearchView.this.onSearchClicked();
            } else if(var1 == SearchView.this.mCloseButton) {
               SearchView.this.onCloseClicked();
            } else if(var1 == SearchView.this.mGoButton) {
               SearchView.this.onSubmitQuery();
            } else if(var1 == SearchView.this.mVoiceButton) {
               SearchView.this.onVoiceClicked();
            } else if(var1 == SearchView.this.mSearchSrcTextView) {
               SearchView.this.forceSuggestionQuery();
            }

         }
      };
      this.mTextKeyListener = new OnKeyListener() {
         public boolean onKey(View var1, int var2, KeyEvent var3) {
            boolean var5 = false;
            boolean var4;
            if(SearchView.this.mSearchable == null) {
               var4 = var5;
            } else if(SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
               var4 = SearchView.this.onSuggestionsKey(var1, var2, var3);
            } else {
               var4 = var5;
               if(!SearchView.this.mSearchSrcTextView.isEmpty()) {
                  var4 = var5;
                  if(KeyEventCompat.hasNoModifiers(var3)) {
                     var4 = var5;
                     if(var3.getAction() == 1) {
                        var4 = var5;
                        if(var2 == 66) {
                           var1.cancelLongPress();
                           SearchView.this.launchQuerySearch(0, (String)null, SearchView.this.mSearchSrcTextView.getText().toString());
                           var4 = true;
                        }
                     }
                  }
               }
            }

            return var4;
         }
      };
      this.mOnEditorActionListener = new OnEditorActionListener() {
         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            SearchView.this.onSubmitQuery();
            return true;
         }
      };
      this.mOnItemClickListener = new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemClicked(var3, 0, (String)null);
         }
      };
      this.mOnItemSelectedListener = new OnItemSelectedListener() {
         public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemSelected(var3);
         }

         public void onNothingSelected(AdapterView var1) {
         }
      };
      this.mTextWatcher = new TextWatcher() {
         public void afterTextChanged(Editable var1) {
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            SearchView.this.onTextChanged(var1);
         }
      };
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.SearchView, var3, 0);
      LayoutInflater.from(var1).inflate(var4.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), this, true);
      this.mSearchSrcTextView = (SearchView.SearchAutoComplete)this.findViewById(R.id.search_src_text);
      this.mSearchSrcTextView.setSearchView(this);
      this.mSearchEditFrame = this.findViewById(R.id.search_edit_frame);
      this.mSearchPlate = this.findViewById(R.id.search_plate);
      this.mSubmitArea = this.findViewById(R.id.submit_area);
      this.mSearchButton = (ImageView)this.findViewById(R.id.search_button);
      this.mGoButton = (ImageView)this.findViewById(R.id.search_go_btn);
      this.mCloseButton = (ImageView)this.findViewById(R.id.search_close_btn);
      this.mVoiceButton = (ImageView)this.findViewById(R.id.search_voice_btn);
      this.mCollapsedIcon = (ImageView)this.findViewById(R.id.search_mag_icon);
      ViewCompat.setBackground(this.mSearchPlate, var4.getDrawable(R.styleable.SearchView_queryBackground));
      ViewCompat.setBackground(this.mSubmitArea, var4.getDrawable(R.styleable.SearchView_submitBackground));
      this.mSearchButton.setImageDrawable(var4.getDrawable(R.styleable.SearchView_searchIcon));
      this.mGoButton.setImageDrawable(var4.getDrawable(R.styleable.SearchView_goIcon));
      this.mCloseButton.setImageDrawable(var4.getDrawable(R.styleable.SearchView_closeIcon));
      this.mVoiceButton.setImageDrawable(var4.getDrawable(R.styleable.SearchView_voiceIcon));
      this.mCollapsedIcon.setImageDrawable(var4.getDrawable(R.styleable.SearchView_searchIcon));
      this.mSearchHintIcon = var4.getDrawable(R.styleable.SearchView_searchHintIcon);
      this.mSuggestionRowLayout = var4.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
      this.mSuggestionCommitIconResId = var4.getResourceId(R.styleable.SearchView_commitIcon, 0);
      this.mSearchButton.setOnClickListener(this.mOnClickListener);
      this.mCloseButton.setOnClickListener(this.mOnClickListener);
      this.mGoButton.setOnClickListener(this.mOnClickListener);
      this.mVoiceButton.setOnClickListener(this.mOnClickListener);
      this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
      this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
      this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
      this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
      this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
      this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
      this.mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
         public void onFocusChange(View var1, boolean var2) {
            if(SearchView.this.mOnQueryTextFocusChangeListener != null) {
               SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, var2);
            }

         }
      });
      this.setIconifiedByDefault(var4.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
      var3 = var4.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
      if(var3 != -1) {
         this.setMaxWidth(var3);
      }

      this.mDefaultQueryHint = var4.getText(R.styleable.SearchView_defaultQueryHint);
      this.mQueryHint = var4.getText(R.styleable.SearchView_queryHint);
      var3 = var4.getInt(R.styleable.SearchView_android_imeOptions, -1);
      if(var3 != -1) {
         this.setImeOptions(var3);
      }

      var3 = var4.getInt(R.styleable.SearchView_android_inputType, -1);
      if(var3 != -1) {
         this.setInputType(var3);
      }

      this.setFocusable(var4.getBoolean(R.styleable.SearchView_android_focusable, true));
      var4.recycle();
      this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
      this.mVoiceWebSearchIntent.addFlags(268435456);
      this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
      this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
      this.mVoiceAppSearchIntent.addFlags(268435456);
      this.mDropDownAnchor = this.findViewById(this.mSearchSrcTextView.getDropDownAnchor());
      if(this.mDropDownAnchor != null) {
         if(VERSION.SDK_INT >= 11) {
            this.addOnLayoutChangeListenerToDropDownAnchorSDK11();
         } else {
            this.addOnLayoutChangeListenerToDropDownAnchorBase();
         }
      }

      this.updateViewsVisibility(this.mIconifiedByDefault);
      this.updateQueryHint();
   }

   private void addOnLayoutChangeListenerToDropDownAnchorBase() {
      this.mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
         public void onGlobalLayout() {
            SearchView.this.adjustDropDownSizeAndPosition();
         }
      });
   }

   @TargetApi(11)
   private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
      this.mDropDownAnchor.addOnLayoutChangeListener(new OnLayoutChangeListener() {
         public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
            SearchView.this.adjustDropDownSizeAndPosition();
         }
      });
   }

   private Intent createIntent(String var1, Uri var2, String var3, String var4, int var5, String var6) {
      Intent var7 = new Intent(var1);
      var7.addFlags(268435456);
      if(var2 != null) {
         var7.setData(var2);
      }

      var7.putExtra("user_query", this.mUserQuery);
      if(var4 != null) {
         var7.putExtra("query", var4);
      }

      if(var3 != null) {
         var7.putExtra("intent_extra_data_key", var3);
      }

      if(this.mAppSearchData != null) {
         var7.putExtra("app_data", this.mAppSearchData);
      }

      if(var5 != 0) {
         var7.putExtra("action_key", var5);
         var7.putExtra("action_msg", var6);
      }

      var7.setComponent(this.mSearchable.getSearchActivity());
      return var7;
   }

   private Intent createIntentFromSuggestion(Cursor param1, int param2, String param3) {
      // $FF: Couldn't be decompiled
   }

   private Intent createVoiceAppSearchIntent(Intent var1, SearchableInfo var2) {
      ComponentName var9 = var2.getSearchActivity();
      Intent var4 = new Intent("android.intent.action.SEARCH");
      var4.setComponent(var9);
      PendingIntent var7 = PendingIntent.getActivity(this.getContext(), 0, var4, 1073741824);
      Bundle var6 = new Bundle();
      if(this.mAppSearchData != null) {
         var6.putParcelable("app_data", this.mAppSearchData);
      }

      Intent var8 = new Intent(var1);
      String var11 = "free_form";
      String var12 = null;
      String var5 = null;
      int var3 = 1;
      Resources var10 = this.getResources();
      if(var2.getVoiceLanguageModeId() != 0) {
         var11 = var10.getString(var2.getVoiceLanguageModeId());
      }

      if(var2.getVoicePromptTextId() != 0) {
         var12 = var10.getString(var2.getVoicePromptTextId());
      }

      if(var2.getVoiceLanguageId() != 0) {
         var5 = var10.getString(var2.getVoiceLanguageId());
      }

      if(var2.getVoiceMaxResults() != 0) {
         var3 = var2.getVoiceMaxResults();
      }

      var8.putExtra("android.speech.extra.LANGUAGE_MODEL", var11);
      var8.putExtra("android.speech.extra.PROMPT", var12);
      var8.putExtra("android.speech.extra.LANGUAGE", var5);
      var8.putExtra("android.speech.extra.MAX_RESULTS", var3);
      if(var9 == null) {
         var11 = null;
      } else {
         var11 = var9.flattenToShortString();
      }

      var8.putExtra("calling_package", var11);
      var8.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", var7);
      var8.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", var6);
      return var8;
   }

   private Intent createVoiceWebSearchIntent(Intent var1, SearchableInfo var2) {
      Intent var3 = new Intent(var1);
      ComponentName var4 = var2.getSearchActivity();
      String var5;
      if(var4 == null) {
         var5 = null;
      } else {
         var5 = var4.flattenToShortString();
      }

      var3.putExtra("calling_package", var5);
      return var3;
   }

   private void dismissSuggestions() {
      this.mSearchSrcTextView.dismissDropDown();
   }

   private void getChildBoundsWithinSearchView(View var1, Rect var2) {
      var1.getLocationInWindow(this.mTemp);
      this.getLocationInWindow(this.mTemp2);
      int var3 = this.mTemp[1] - this.mTemp2[1];
      int var4 = this.mTemp[0] - this.mTemp2[0];
      var2.set(var4, var3, var1.getWidth() + var4, var1.getHeight() + var3);
   }

   private CharSequence getDecoratedHint(CharSequence var1) {
      if(this.mIconifiedByDefault && this.mSearchHintIcon != null) {
         int var2 = (int)((double)this.mSearchSrcTextView.getTextSize() * 1.25D);
         this.mSearchHintIcon.setBounds(0, 0, var2, var2);
         SpannableStringBuilder var3 = new SpannableStringBuilder("   ");
         var3.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
         var3.append((CharSequence)var1);
         var1 = var3;
      }

      return (CharSequence)var1;
   }

   private int getPreferredHeight() {
      return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
   }

   private int getPreferredWidth() {
      return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
   }

   private boolean hasVoiceSearch() {
      boolean var2 = false;
      boolean var1 = var2;
      if(this.mSearchable != null) {
         var1 = var2;
         if(this.mSearchable.getVoiceSearchEnabled()) {
            Intent var3 = null;
            if(this.mSearchable.getVoiceSearchLaunchWebSearch()) {
               var3 = this.mVoiceWebSearchIntent;
            } else if(this.mSearchable.getVoiceSearchLaunchRecognizer()) {
               var3 = this.mVoiceAppSearchIntent;
            }

            var1 = var2;
            if(var3 != null) {
               var1 = var2;
               if(this.getContext().getPackageManager().resolveActivity(var3, 65536) != null) {
                  var1 = true;
               }
            }
         }
      }

      return var1;
   }

   static boolean isLandscapeMode(Context var0) {
      boolean var1;
      if(var0.getResources().getConfiguration().orientation == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isSubmitAreaEnabled() {
      boolean var1;
      if((this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void launchIntent(Intent var1) {
      if(var1 != null) {
         try {
            this.getContext().startActivity(var1);
         } catch (RuntimeException var3) {
            Log.e("SearchView", "Failed launch activity: " + var1, var3);
         }
      }

   }

   private boolean launchSuggestion(int var1, int var2, String var3) {
      Cursor var5 = this.mSuggestionsAdapter.getCursor();
      boolean var4;
      if(var5 != null && var5.moveToPosition(var1)) {
         this.launchIntent(this.createIntentFromSuggestion(var5, var2, var3));
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   private void postUpdateFocusedState() {
      this.post(this.mUpdateDrawableStateRunnable);
   }

   private void rewriteQueryFromSuggestion(int var1) {
      Editable var2 = this.mSearchSrcTextView.getText();
      Cursor var3 = this.mSuggestionsAdapter.getCursor();
      if(var3 != null) {
         if(var3.moveToPosition(var1)) {
            CharSequence var4 = this.mSuggestionsAdapter.convertToString(var3);
            if(var4 != null) {
               this.setQuery(var4);
            } else {
               this.setQuery(var2);
            }
         } else {
            this.setQuery(var2);
         }
      }

   }

   private void setQuery(CharSequence var1) {
      this.mSearchSrcTextView.setText(var1);
      SearchView.SearchAutoComplete var3 = this.mSearchSrcTextView;
      int var2;
      if(TextUtils.isEmpty(var1)) {
         var2 = 0;
      } else {
         var2 = var1.length();
      }

      var3.setSelection(var2);
   }

   private void updateCloseButton() {
      boolean var4 = true;
      byte var3 = 0;
      boolean var1;
      if(!TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2 = var4;
      if(!var1) {
         if(this.mIconifiedByDefault && !this.mExpandedInActionView) {
            var2 = var4;
         } else {
            var2 = false;
         }
      }

      ImageView var5 = this.mCloseButton;
      byte var7;
      if(var2) {
         var7 = var3;
      } else {
         var7 = 8;
      }

      var5.setVisibility(var7);
      Drawable var6 = this.mCloseButton.getDrawable();
      if(var6 != null) {
         int[] var8;
         if(var1) {
            var8 = ENABLED_STATE_SET;
         } else {
            var8 = EMPTY_STATE_SET;
         }

         var6.setState(var8);
      }

   }

   private void updateQueryHint() {
      CharSequence var2 = this.getQueryHint();
      SearchView.SearchAutoComplete var3 = this.mSearchSrcTextView;
      Object var1 = var2;
      if(var2 == null) {
         var1 = "";
      }

      var3.setHint(this.getDecoratedHint((CharSequence)var1));
   }

   private void updateSearchAutoComplete() {
      byte var2 = 1;
      this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
      this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
      int var3 = this.mSearchable.getInputType();
      int var1 = var3;
      if((var3 & 15) == 1) {
         var3 &= -65537;
         var1 = var3;
         if(this.mSearchable.getSuggestAuthority() != null) {
            var1 = var3 | 65536 | 524288;
         }
      }

      this.mSearchSrcTextView.setInputType(var1);
      if(this.mSuggestionsAdapter != null) {
         this.mSuggestionsAdapter.changeCursor((Cursor)null);
      }

      if(this.mSearchable.getSuggestAuthority() != null) {
         this.mSuggestionsAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
         this.mSearchSrcTextView.setAdapter(this.mSuggestionsAdapter);
         SuggestionsAdapter var4 = (SuggestionsAdapter)this.mSuggestionsAdapter;
         byte var5 = var2;
         if(this.mQueryRefinement) {
            var5 = 2;
         }

         var4.setQueryRefinement(var5);
      }

   }

   private void updateSubmitArea() {
      byte var2 = 8;
      byte var1 = var2;
      if(this.isSubmitAreaEnabled()) {
         label12: {
            if(this.mGoButton.getVisibility() != 0) {
               var1 = var2;
               if(this.mVoiceButton.getVisibility() != 0) {
                  break label12;
               }
            }

            var1 = 0;
         }
      }

      this.mSubmitArea.setVisibility(var1);
   }

   private void updateSubmitButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if(this.mSubmitButtonEnabled) {
         var2 = var3;
         if(this.isSubmitAreaEnabled()) {
            var2 = var3;
            if(this.hasFocus()) {
               label14: {
                  if(!var1) {
                     var2 = var3;
                     if(this.mVoiceButtonEnabled) {
                        break label14;
                     }
                  }

                  var2 = 0;
               }
            }
         }
      }

      this.mGoButton.setVisibility(var2);
   }

   private void updateViewsVisibility(boolean var1) {
      byte var3 = 8;
      boolean var5 = true;
      this.mIconified = var1;
      byte var2;
      if(var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      boolean var4;
      if(!TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mSearchButton.setVisibility(var2);
      this.updateSubmitButton(var4);
      View var6 = this.mSearchEditFrame;
      if(var1) {
         var2 = var3;
      } else {
         var2 = 0;
      }

      var6.setVisibility(var2);
      if(this.mCollapsedIcon.getDrawable() != null && !this.mIconifiedByDefault) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      this.mCollapsedIcon.setVisibility(var2);
      this.updateCloseButton();
      if(!var4) {
         var1 = var5;
      } else {
         var1 = false;
      }

      this.updateVoiceButton(var1);
      this.updateSubmitArea();
   }

   private void updateVoiceButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if(this.mVoiceButtonEnabled) {
         var2 = var3;
         if(!this.isIconified()) {
            var2 = var3;
            if(var1) {
               var2 = 0;
               this.mGoButton.setVisibility(8);
            }
         }
      }

      this.mVoiceButton.setVisibility(var2);
   }

   void adjustDropDownSizeAndPosition() {
      if(this.mDropDownAnchor.getWidth() > 1) {
         Resources var7 = this.getContext().getResources();
         int var3 = this.mSearchPlate.getPaddingLeft();
         Rect var8 = new Rect();
         boolean var6 = ViewUtils.isLayoutRtl(this);
         int var1;
         if(this.mIconifiedByDefault) {
            var1 = var7.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + var7.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left);
         } else {
            var1 = 0;
         }

         this.mSearchSrcTextView.getDropDownBackground().getPadding(var8);
         int var2;
         if(var6) {
            var2 = -var8.left;
         } else {
            var2 = var3 - (var8.left + var1);
         }

         this.mSearchSrcTextView.setDropDownHorizontalOffset(var2);
         int var5 = this.mDropDownAnchor.getWidth();
         int var4 = var8.left;
         var2 = var8.right;
         this.mSearchSrcTextView.setDropDownWidth(var5 + var4 + var2 + var1 - var3);
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void clearFocus() {
      this.mClearingFocus = true;
      this.setImeVisibility(false);
      super.clearFocus();
      this.mSearchSrcTextView.clearFocus();
      this.mClearingFocus = false;
   }

   void forceSuggestionQuery() {
      HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
      HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
   }

   public int getImeOptions() {
      return this.mSearchSrcTextView.getImeOptions();
   }

   public int getInputType() {
      return this.mSearchSrcTextView.getInputType();
   }

   public int getMaxWidth() {
      return this.mMaxWidth;
   }

   public CharSequence getQuery() {
      return this.mSearchSrcTextView.getText();
   }

   @Nullable
   public CharSequence getQueryHint() {
      CharSequence var1;
      if(this.mQueryHint != null) {
         var1 = this.mQueryHint;
      } else if(this.mSearchable != null && this.mSearchable.getHintId() != 0) {
         var1 = this.getContext().getText(this.mSearchable.getHintId());
      } else {
         var1 = this.mDefaultQueryHint;
      }

      return var1;
   }

   int getSuggestionCommitIconResId() {
      return this.mSuggestionCommitIconResId;
   }

   int getSuggestionRowLayout() {
      return this.mSuggestionRowLayout;
   }

   public CursorAdapter getSuggestionsAdapter() {
      return this.mSuggestionsAdapter;
   }

   public boolean isIconfiedByDefault() {
      return this.mIconifiedByDefault;
   }

   public boolean isIconified() {
      return this.mIconified;
   }

   public boolean isQueryRefinementEnabled() {
      return this.mQueryRefinement;
   }

   public boolean isSubmitButtonEnabled() {
      return this.mSubmitButtonEnabled;
   }

   void launchQuerySearch(int var1, String var2, String var3) {
      Intent var4 = this.createIntent("android.intent.action.SEARCH", (Uri)null, (String)null, var3, var1, var2);
      this.getContext().startActivity(var4);
   }

   public void onActionViewCollapsed() {
      this.setQuery("", false);
      this.clearFocus();
      this.updateViewsVisibility(true);
      this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
      this.mExpandedInActionView = false;
   }

   public void onActionViewExpanded() {
      if(!this.mExpandedInActionView) {
         this.mExpandedInActionView = true;
         this.mCollapsedImeOptions = this.mSearchSrcTextView.getImeOptions();
         this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions | 33554432);
         this.mSearchSrcTextView.setText("");
         this.setIconified(false);
      }

   }

   void onCloseClicked() {
      if(TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
         if(this.mIconifiedByDefault && (this.mOnCloseListener == null || !this.mOnCloseListener.onClose())) {
            this.clearFocus();
            this.updateViewsVisibility(true);
         }
      } else {
         this.mSearchSrcTextView.setText("");
         this.mSearchSrcTextView.requestFocus();
         this.setImeVisibility(true);
      }

   }

   protected void onDetachedFromWindow() {
      this.removeCallbacks(this.mUpdateDrawableStateRunnable);
      this.post(this.mReleaseCursorRunnable);
      super.onDetachedFromWindow();
   }

   boolean onItemClicked(int var1, int var2, String var3) {
      boolean var4 = false;
      if(this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionClick(var1)) {
         this.launchSuggestion(var1, 0, (String)null);
         this.setImeVisibility(false);
         this.dismissSuggestions();
         var4 = true;
      }

      return var4;
   }

   boolean onItemSelected(int var1) {
      boolean var2;
      if(this.mOnSuggestionListener != null && this.mOnSuggestionListener.onSuggestionSelect(var1)) {
         var2 = false;
      } else {
         this.rewriteQueryFromSuggestion(var1);
         var2 = true;
      }

      return var2;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if(var1) {
         this.getChildBoundsWithinSearchView(this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
         this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, var5 - var3);
         if(this.mTouchDelegate == null) {
            this.mTouchDelegate = new SearchView.UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, this.mSearchSrcTextView);
            this.setTouchDelegate(this.mTouchDelegate);
         } else {
            this.mTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      if(this.isIconified()) {
         super.onMeasure(var1, var2);
      } else {
         int var4 = MeasureSpec.getMode(var1);
         int var3 = MeasureSpec.getSize(var1);
         switch(var4) {
         case Integer.MIN_VALUE:
            if(this.mMaxWidth > 0) {
               var1 = Math.min(this.mMaxWidth, var3);
            } else {
               var1 = Math.min(this.getPreferredWidth(), var3);
            }
            break;
         case 0:
            if(this.mMaxWidth > 0) {
               var1 = this.mMaxWidth;
            } else {
               var1 = this.getPreferredWidth();
            }
            break;
         case 1073741824:
            var1 = var3;
            if(this.mMaxWidth > 0) {
               var1 = Math.min(this.mMaxWidth, var3);
            }
            break;
         default:
            var1 = var3;
         }

         var3 = MeasureSpec.getMode(var2);
         var2 = MeasureSpec.getSize(var2);
         switch(var3) {
         case Integer.MIN_VALUE:
            var2 = Math.min(this.getPreferredHeight(), var2);
            break;
         case 0:
            var2 = this.getPreferredHeight();
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
      }

   }

   void onQueryRefine(CharSequence var1) {
      this.setQuery(var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if(!(var1 instanceof SearchView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         SearchView.SavedState var2 = (SearchView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.updateViewsVisibility(var2.isIconified);
         this.requestLayout();
      }

   }

   protected Parcelable onSaveInstanceState() {
      SearchView.SavedState var1 = new SearchView.SavedState(super.onSaveInstanceState());
      var1.isIconified = this.isIconified();
      return var1;
   }

   void onSearchClicked() {
      this.updateViewsVisibility(false);
      this.mSearchSrcTextView.requestFocus();
      this.setImeVisibility(true);
      if(this.mOnSearchClickListener != null) {
         this.mOnSearchClickListener.onClick(this);
      }

   }

   void onSubmitQuery() {
      Editable var1 = this.mSearchSrcTextView.getText();
      if(var1 != null && TextUtils.getTrimmedLength(var1) > 0 && (this.mOnQueryChangeListener == null || !this.mOnQueryChangeListener.onQueryTextSubmit(var1.toString()))) {
         if(this.mSearchable != null) {
            this.launchQuerySearch(0, (String)null, var1.toString());
         }

         this.setImeVisibility(false);
         this.dismissSuggestions();
      }

   }

   boolean onSuggestionsKey(View var1, int var2, KeyEvent var3) {
      boolean var5 = false;
      boolean var4;
      if(this.mSearchable == null) {
         var4 = var5;
      } else {
         var4 = var5;
         if(this.mSuggestionsAdapter != null) {
            var4 = var5;
            if(var3.getAction() == 0) {
               var4 = var5;
               if(KeyEventCompat.hasNoModifiers(var3)) {
                  if(var2 != 66 && var2 != 84 && var2 != 61) {
                     if(var2 != 21 && var2 != 22) {
                        var4 = var5;
                        if(var2 == 19) {
                           var4 = var5;
                           if(this.mSearchSrcTextView.getListSelection() == 0) {
                              var4 = var5;
                           }
                        }
                     } else {
                        if(var2 == 21) {
                           var2 = 0;
                        } else {
                           var2 = this.mSearchSrcTextView.length();
                        }

                        this.mSearchSrcTextView.setSelection(var2);
                        this.mSearchSrcTextView.setListSelection(0);
                        this.mSearchSrcTextView.clearListSelection();
                        HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
                        var4 = true;
                     }
                  } else {
                     var4 = this.onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, (String)null);
                  }
               }
            }
         }
      }

      return var4;
   }

   void onTextChanged(CharSequence var1) {
      boolean var3 = true;
      Editable var4 = this.mSearchSrcTextView.getText();
      this.mUserQuery = var4;
      boolean var2;
      if(!TextUtils.isEmpty(var4)) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.updateSubmitButton(var2);
      if(!var2) {
         var2 = var3;
      } else {
         var2 = false;
      }

      this.updateVoiceButton(var2);
      this.updateCloseButton();
      this.updateSubmitArea();
      if(this.mOnQueryChangeListener != null && !TextUtils.equals(var1, this.mOldQueryText)) {
         this.mOnQueryChangeListener.onQueryTextChange(var1.toString());
      }

      this.mOldQueryText = var1.toString();
   }

   void onTextFocusChanged() {
      this.updateViewsVisibility(this.isIconified());
      this.postUpdateFocusedState();
      if(this.mSearchSrcTextView.hasFocus()) {
         this.forceSuggestionQuery();
      }

   }

   void onVoiceClicked() {
      if(this.mSearchable != null) {
         SearchableInfo var1 = this.mSearchable;

         try {
            Intent var3;
            if(var1.getVoiceSearchLaunchWebSearch()) {
               var3 = this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, var1);
               this.getContext().startActivity(var3);
            } else if(var1.getVoiceSearchLaunchRecognizer()) {
               var3 = this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, var1);
               this.getContext().startActivity(var3);
            }
         } catch (ActivityNotFoundException var2) {
            Log.w("SearchView", "Could not find voice search activity");
         }
      }

   }

   public void onWindowFocusChanged(boolean var1) {
      super.onWindowFocusChanged(var1);
      this.postUpdateFocusedState();
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean requestFocus(int var1, Rect var2) {
      boolean var3;
      if(this.mClearingFocus) {
         var3 = false;
      } else if(!this.isFocusable()) {
         var3 = false;
      } else if(!this.isIconified()) {
         boolean var4 = this.mSearchSrcTextView.requestFocus(var1, var2);
         var3 = var4;
         if(var4) {
            this.updateViewsVisibility(false);
            var3 = var4;
         }
      } else {
         var3 = super.requestFocus(var1, var2);
      }

      return var3;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setAppSearchData(Bundle var1) {
      this.mAppSearchData = var1;
   }

   public void setIconified(boolean var1) {
      if(var1) {
         this.onCloseClicked();
      } else {
         this.onSearchClicked();
      }

   }

   public void setIconifiedByDefault(boolean var1) {
      if(this.mIconifiedByDefault != var1) {
         this.mIconifiedByDefault = var1;
         this.updateViewsVisibility(var1);
         this.updateQueryHint();
      }

   }

   public void setImeOptions(int var1) {
      this.mSearchSrcTextView.setImeOptions(var1);
   }

   void setImeVisibility(boolean var1) {
      if(var1) {
         this.post(this.mShowImeRunnable);
      } else {
         this.removeCallbacks(this.mShowImeRunnable);
         InputMethodManager var2 = (InputMethodManager)this.getContext().getSystemService("input_method");
         if(var2 != null) {
            var2.hideSoftInputFromWindow(this.getWindowToken(), 0);
         }
      }

   }

   public void setInputType(int var1) {
      this.mSearchSrcTextView.setInputType(var1);
   }

   public void setMaxWidth(int var1) {
      this.mMaxWidth = var1;
      this.requestLayout();
   }

   public void setOnCloseListener(SearchView.OnCloseListener var1) {
      this.mOnCloseListener = var1;
   }

   public void setOnQueryTextFocusChangeListener(OnFocusChangeListener var1) {
      this.mOnQueryTextFocusChangeListener = var1;
   }

   public void setOnQueryTextListener(SearchView.OnQueryTextListener var1) {
      this.mOnQueryChangeListener = var1;
   }

   public void setOnSearchClickListener(OnClickListener var1) {
      this.mOnSearchClickListener = var1;
   }

   public void setOnSuggestionListener(SearchView.OnSuggestionListener var1) {
      this.mOnSuggestionListener = var1;
   }

   public void setQuery(CharSequence var1, boolean var2) {
      this.mSearchSrcTextView.setText(var1);
      if(var1 != null) {
         this.mSearchSrcTextView.setSelection(this.mSearchSrcTextView.length());
         this.mUserQuery = var1;
      }

      if(var2 && !TextUtils.isEmpty(var1)) {
         this.onSubmitQuery();
      }

   }

   public void setQueryHint(@Nullable CharSequence var1) {
      this.mQueryHint = var1;
      this.updateQueryHint();
   }

   public void setQueryRefinementEnabled(boolean var1) {
      this.mQueryRefinement = var1;
      if(this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
         SuggestionsAdapter var3 = (SuggestionsAdapter)this.mSuggestionsAdapter;
         byte var2;
         if(var1) {
            var2 = 2;
         } else {
            var2 = 1;
         }

         var3.setQueryRefinement(var2);
      }

   }

   public void setSearchableInfo(SearchableInfo var1) {
      this.mSearchable = var1;
      if(this.mSearchable != null) {
         this.updateSearchAutoComplete();
         this.updateQueryHint();
      }

      this.mVoiceButtonEnabled = this.hasVoiceSearch();
      if(this.mVoiceButtonEnabled) {
         this.mSearchSrcTextView.setPrivateImeOptions("nm");
      }

      this.updateViewsVisibility(this.isIconified());
   }

   public void setSubmitButtonEnabled(boolean var1) {
      this.mSubmitButtonEnabled = var1;
      this.updateViewsVisibility(this.isIconified());
   }

   public void setSuggestionsAdapter(CursorAdapter var1) {
      this.mSuggestionsAdapter = var1;
      this.mSearchSrcTextView.setAdapter(this.mSuggestionsAdapter);
   }

   void updateFocusedState() {
      int[] var1;
      if(this.mSearchSrcTextView.hasFocus()) {
         var1 = FOCUSED_STATE_SET;
      } else {
         var1 = EMPTY_STATE_SET;
      }

      Drawable var2 = this.mSearchPlate.getBackground();
      if(var2 != null) {
         var2.setState(var1);
      }

      var2 = this.mSubmitArea.getBackground();
      if(var2 != null) {
         var2.setState(var1);
      }

      this.invalidate();
   }

   private static class AutoCompleteTextViewReflector {
      private Method doAfterTextChanged;
      private Method doBeforeTextChanged;
      private Method ensureImeVisible;
      private Method showSoftInputUnchecked;

      AutoCompleteTextViewReflector() {
         try {
            this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
            this.doBeforeTextChanged.setAccessible(true);
         } catch (NoSuchMethodException var5) {
            ;
         }

         try {
            this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
            this.doAfterTextChanged.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            ;
         }

         try {
            this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", new Class[]{Boolean.TYPE});
            this.ensureImeVisible.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            ;
         }

         try {
            this.showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", new Class[]{Integer.TYPE, ResultReceiver.class});
            this.showSoftInputUnchecked.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            ;
         }

      }

      void doAfterTextChanged(AutoCompleteTextView var1) {
         if(this.doAfterTextChanged != null) {
            try {
               this.doAfterTextChanged.invoke(var1, new Object[0]);
            } catch (Exception var2) {
               ;
            }
         }

      }

      void doBeforeTextChanged(AutoCompleteTextView var1) {
         if(this.doBeforeTextChanged != null) {
            try {
               this.doBeforeTextChanged.invoke(var1, new Object[0]);
            } catch (Exception var2) {
               ;
            }
         }

      }

      void ensureImeVisible(AutoCompleteTextView var1, boolean var2) {
         if(this.ensureImeVisible != null) {
            try {
               this.ensureImeVisible.invoke(var1, new Object[]{Boolean.valueOf(var2)});
            } catch (Exception var3) {
               ;
            }
         }

      }

      void showSoftInputUnchecked(InputMethodManager var1, View var2, int var3) {
         if(this.showSoftInputUnchecked != null) {
            try {
               this.showSoftInputUnchecked.invoke(var1, new Object[]{Integer.valueOf(var3), null});
               return;
            } catch (Exception var5) {
               ;
            }
         }

         var1.showSoftInput(var2, var3);
      }
   }

   public interface OnCloseListener {
      boolean onClose();
   }

   public interface OnQueryTextListener {
      boolean onQueryTextChange(String var1);

      boolean onQueryTextSubmit(String var1);
   }

   public interface OnSuggestionListener {
      boolean onSuggestionClick(int var1);

      boolean onSuggestionSelect(int var1);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public SearchView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new SearchView.SavedState(var1, var2);
         }

         public SearchView.SavedState[] newArray(int var1) {
            return new SearchView.SavedState[var1];
         }
      });
      boolean isIconified;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.isIconified = ((Boolean)var1.readValue((ClassLoader)null)).booleanValue();
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.isIconified + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeValue(Boolean.valueOf(this.isIconified));
      }
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
      private SearchView mSearchView;
      private int mThreshold;

      public SearchAutoComplete(Context var1) {
         this(var1, (AttributeSet)null);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2) {
         this(var1, var2, R.attr.autoCompleteTextViewStyle);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2, int var3) {
         super(var1, var2, var3);
         this.mThreshold = this.getThreshold();
      }

      private int getSearchViewTextMinWidthDp() {
         Configuration var3 = this.getResources().getConfiguration();
         int var2 = ConfigurationHelper.getScreenWidthDp(this.getResources());
         int var1 = ConfigurationHelper.getScreenHeightDp(this.getResources());
         short var4;
         if(var2 >= 960 && var1 >= 720 && var3.orientation == 2) {
            var4 = 256;
         } else if(var2 < 600 && (var2 < 640 || var1 < 480)) {
            var4 = 160;
         } else {
            var4 = 192;
         }

         return var4;
      }

      private boolean isEmpty() {
         boolean var1;
         if(TextUtils.getTrimmedLength(this.getText()) == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean enoughToFilter() {
         boolean var1;
         if(this.mThreshold > 0 && !super.enoughToFilter()) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      protected void onFinishInflate() {
         super.onFinishInflate();
         DisplayMetrics var1 = this.getResources().getDisplayMetrics();
         this.setMinWidth((int)TypedValue.applyDimension(1, (float)this.getSearchViewTextMinWidthDp(), var1));
      }

      protected void onFocusChanged(boolean var1, int var2, Rect var3) {
         super.onFocusChanged(var1, var2, var3);
         this.mSearchView.onTextFocusChanged();
      }

      public boolean onKeyPreIme(int var1, KeyEvent var2) {
         boolean var4 = true;
         boolean var3;
         if(var1 == 4) {
            DispatcherState var5;
            if(var2.getAction() == 0 && var2.getRepeatCount() == 0) {
               var5 = this.getKeyDispatcherState();
               var3 = var4;
               if(var5 != null) {
                  var5.startTracking(var2, this);
                  var3 = var4;
               }

               return var3;
            }

            if(var2.getAction() == 1) {
               var5 = this.getKeyDispatcherState();
               if(var5 != null) {
                  var5.handleUpEvent(var2);
               }

               if(var2.isTracking() && !var2.isCanceled()) {
                  this.mSearchView.clearFocus();
                  this.mSearchView.setImeVisibility(false);
                  var3 = var4;
                  return var3;
               }
            }
         }

         var3 = super.onKeyPreIme(var1, var2);
         return var3;
      }

      public void onWindowFocusChanged(boolean var1) {
         super.onWindowFocusChanged(var1);
         if(var1 && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
            ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput(this, 0);
            if(SearchView.isLandscapeMode(this.getContext())) {
               SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
            }
         }

      }

      public void performCompletion() {
      }

      protected void replaceText(CharSequence var1) {
      }

      void setSearchView(SearchView var1) {
         this.mSearchView = var1;
      }

      public void setThreshold(int var1) {
         super.setThreshold(var1);
         this.mThreshold = var1;
      }
   }

   private static class UpdatableTouchDelegate extends TouchDelegate {
      private final Rect mActualBounds;
      private boolean mDelegateTargeted;
      private final View mDelegateView;
      private final int mSlop;
      private final Rect mSlopBounds;
      private final Rect mTargetBounds;

      public UpdatableTouchDelegate(Rect var1, Rect var2, View var3) {
         super(var1, var3);
         this.mSlop = ViewConfiguration.get(var3.getContext()).getScaledTouchSlop();
         this.mTargetBounds = new Rect();
         this.mSlopBounds = new Rect();
         this.mActualBounds = new Rect();
         this.setBounds(var1, var2);
         this.mDelegateView = var3;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var4 = (int)var1.getX();
         int var5 = (int)var1.getY();
         boolean var6 = false;
         boolean var3 = true;
         boolean var7 = false;
         boolean var2;
         switch(var1.getAction()) {
         case 0:
            var2 = var3;
            if(this.mTargetBounds.contains(var4, var5)) {
               this.mDelegateTargeted = true;
               var6 = true;
               var2 = var3;
            }
            break;
         case 1:
         case 2:
            boolean var8 = this.mDelegateTargeted;
            var2 = var3;
            var6 = var8;
            if(var8) {
               var2 = var3;
               var6 = var8;
               if(!this.mSlopBounds.contains(var4, var5)) {
                  var2 = false;
                  var6 = var8;
               }
            }
            break;
         case 3:
            var6 = this.mDelegateTargeted;
            this.mDelegateTargeted = false;
            var2 = var3;
            break;
         default:
            var2 = var3;
         }

         if(var6) {
            if(var2 && !this.mActualBounds.contains(var4, var5)) {
               var1.setLocation((float)(this.mDelegateView.getWidth() / 2), (float)(this.mDelegateView.getHeight() / 2));
            } else {
               var1.setLocation((float)(var4 - this.mActualBounds.left), (float)(var5 - this.mActualBounds.top));
            }

            var7 = this.mDelegateView.dispatchTouchEvent(var1);
         }

         return var7;
      }

      public void setBounds(Rect var1, Rect var2) {
         this.mTargetBounds.set(var1);
         this.mSlopBounds.set(var1);
         this.mSlopBounds.inset(-this.mSlop, -this.mSlop);
         this.mActualBounds.set(var2);
      }
   }
}
