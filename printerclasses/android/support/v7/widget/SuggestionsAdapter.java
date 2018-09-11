package android.support.v7.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter implements OnClickListener {
   private static final boolean DBG = false;
   static final int INVALID_INDEX = -1;
   private static final String LOG_TAG = "SuggestionsAdapter";
   private static final int QUERY_LIMIT = 50;
   static final int REFINE_ALL = 2;
   static final int REFINE_BY_ENTRY = 1;
   static final int REFINE_NONE = 0;
   private boolean mClosed = false;
   private final int mCommitIconResId;
   private int mFlagsCol = -1;
   private int mIconName1Col = -1;
   private int mIconName2Col = -1;
   private final WeakHashMap mOutsideDrawablesCache;
   private final Context mProviderContext;
   private int mQueryRefinement = 1;
   private final SearchManager mSearchManager;
   private final SearchView mSearchView;
   private final SearchableInfo mSearchable;
   private int mText1Col = -1;
   private int mText2Col = -1;
   private int mText2UrlCol = -1;
   private ColorStateList mUrlColor;

   public SuggestionsAdapter(Context var1, SearchView var2, SearchableInfo var3, WeakHashMap var4) {
      super(var1, var2.getSuggestionRowLayout(), (Cursor)null, true);
      this.mSearchManager = (SearchManager)this.mContext.getSystemService("search");
      this.mSearchView = var2;
      this.mSearchable = var3;
      this.mCommitIconResId = var2.getSuggestionCommitIconResId();
      this.mProviderContext = var1;
      this.mOutsideDrawablesCache = var4;
   }

   private Drawable checkIconCache(String var1) {
      ConstantState var2 = (ConstantState)this.mOutsideDrawablesCache.get(var1);
      Drawable var3;
      if(var2 == null) {
         var3 = null;
      } else {
         var3 = var2.newDrawable();
      }

      return var3;
   }

   private CharSequence formatUrl(CharSequence var1) {
      if(this.mUrlColor == null) {
         TypedValue var2 = new TypedValue();
         this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, var2, true);
         this.mUrlColor = this.mContext.getResources().getColorStateList(var2.resourceId);
      }

      SpannableString var3 = new SpannableString(var1);
      var3.setSpan(new TextAppearanceSpan((String)null, 0, 0, this.mUrlColor, (ColorStateList)null), 0, var1.length(), 33);
      return var3;
   }

   private Drawable getActivityIcon(ComponentName var1) {
      PackageManager var4 = this.mContext.getPackageManager();

      Drawable var3;
      ActivityInfo var6;
      try {
         var6 = var4.getActivityInfo(var1, 128);
      } catch (NameNotFoundException var5) {
         Log.w("SuggestionsAdapter", var5.toString());
         var3 = null;
         return var3;
      }

      int var2 = var6.getIconResource();
      if(var2 == 0) {
         var3 = null;
      } else {
         Drawable var7 = var4.getDrawable(var1.getPackageName(), var2, var6.applicationInfo);
         var3 = var7;
         if(var7 == null) {
            Log.w("SuggestionsAdapter", "Invalid icon resource " + var2 + " for " + var1.flattenToShortString());
            var3 = null;
         }
      }

      return var3;
   }

   private Drawable getActivityIconWithCache(ComponentName var1) {
      Drawable var2 = null;
      String var3 = var1.flattenToShortString();
      ConstantState var4;
      Drawable var5;
      if(this.mOutsideDrawablesCache.containsKey(var3)) {
         var4 = (ConstantState)this.mOutsideDrawablesCache.get(var3);
         if(var4 == null) {
            var5 = var2;
         } else {
            var5 = var4.newDrawable(this.mProviderContext.getResources());
         }
      } else {
         var2 = this.getActivityIcon(var1);
         if(var2 == null) {
            var4 = null;
         } else {
            var4 = var2.getConstantState();
         }

         this.mOutsideDrawablesCache.put(var3, var4);
         var5 = var2;
      }

      return var5;
   }

   public static String getColumnString(Cursor var0, String var1) {
      return getStringOrNull(var0, var0.getColumnIndex(var1));
   }

   private Drawable getDefaultIcon1(Cursor var1) {
      Drawable var2 = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
      if(var2 == null) {
         var2 = this.mContext.getPackageManager().getDefaultActivityIcon();
      }

      return var2;
   }

   private Drawable getDrawable(Uri param1) {
      // $FF: Couldn't be decompiled
   }

   private Drawable getDrawableFromResourceValue(String param1) {
      // $FF: Couldn't be decompiled
   }

   private Drawable getIcon1(Cursor var1) {
      Drawable var2;
      if(this.mIconName1Col == -1) {
         var2 = null;
      } else {
         Drawable var3 = this.getDrawableFromResourceValue(var1.getString(this.mIconName1Col));
         var2 = var3;
         if(var3 == null) {
            var2 = this.getDefaultIcon1(var1);
         }
      }

      return var2;
   }

   private Drawable getIcon2(Cursor var1) {
      Drawable var2;
      if(this.mIconName2Col == -1) {
         var2 = null;
      } else {
         var2 = this.getDrawableFromResourceValue(var1.getString(this.mIconName2Col));
      }

      return var2;
   }

   private static String getStringOrNull(Cursor var0, int var1) {
      Object var2 = null;
      String var4;
      if(var1 == -1) {
         var4 = (String)var2;
      } else {
         try {
            var4 = var0.getString(var1);
         } catch (Exception var3) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", var3);
            var4 = (String)var2;
         }
      }

      return var4;
   }

   private void setViewDrawable(ImageView var1, Drawable var2, int var3) {
      var1.setImageDrawable(var2);
      if(var2 == null) {
         var1.setVisibility(var3);
      } else {
         var1.setVisibility(0);
         var2.setVisible(false, false);
         var2.setVisible(true, false);
      }

   }

   private void setViewText(TextView var1, CharSequence var2) {
      var1.setText(var2);
      if(TextUtils.isEmpty(var2)) {
         var1.setVisibility(8);
      } else {
         var1.setVisibility(0);
      }

   }

   private void storeInIconCache(String var1, Drawable var2) {
      if(var2 != null) {
         this.mOutsideDrawablesCache.put(var1, var2.getConstantState());
      }

   }

   private void updateSpinnerState(Cursor var1) {
      Bundle var2;
      if(var1 != null) {
         var2 = var1.getExtras();
      } else {
         var2 = null;
      }

      if(var2 != null && var2.getBoolean("in_progress")) {
         ;
      }

   }

   public void bindView(View var1, Context var2, Cursor var3) {
      SuggestionsAdapter.ChildViewCache var7 = (SuggestionsAdapter.ChildViewCache)var1.getTag();
      int var4 = 0;
      if(this.mFlagsCol != -1) {
         var4 = var3.getInt(this.mFlagsCol);
      }

      String var5;
      if(var7.mText1 != null) {
         var5 = getStringOrNull(var3, this.mText1Col);
         this.setViewText(var7.mText1, var5);
      }

      if(var7.mText2 != null) {
         var5 = getStringOrNull(var3, this.mText2UrlCol);
         Object var6;
         if(var5 != null) {
            var6 = this.formatUrl(var5);
         } else {
            var6 = getStringOrNull(var3, this.mText2Col);
         }

         if(TextUtils.isEmpty((CharSequence)var6)) {
            if(var7.mText1 != null) {
               var7.mText1.setSingleLine(false);
               var7.mText1.setMaxLines(2);
            }
         } else if(var7.mText1 != null) {
            var7.mText1.setSingleLine(true);
            var7.mText1.setMaxLines(1);
         }

         this.setViewText(var7.mText2, (CharSequence)var6);
      }

      if(var7.mIcon1 != null) {
         this.setViewDrawable(var7.mIcon1, this.getIcon1(var3), 4);
      }

      if(var7.mIcon2 != null) {
         this.setViewDrawable(var7.mIcon2, this.getIcon2(var3), 8);
      }

      if(this.mQueryRefinement != 2 && (this.mQueryRefinement != 1 || (var4 & 1) == 0)) {
         var7.mIconRefine.setVisibility(8);
      } else {
         var7.mIconRefine.setVisibility(0);
         var7.mIconRefine.setTag(var7.mText1.getText());
         var7.mIconRefine.setOnClickListener(this);
      }

   }

   public void changeCursor(Cursor param1) {
      // $FF: Couldn't be decompiled
   }

   public void close() {
      this.changeCursor((Cursor)null);
      this.mClosed = true;
   }

   public CharSequence convertToString(Cursor var1) {
      String var2;
      if(var1 == null) {
         var2 = null;
      } else {
         String var3 = getColumnString(var1, "suggest_intent_query");
         var2 = var3;
         if(var3 == null) {
            if(this.mSearchable.shouldRewriteQueryFromData()) {
               var2 = getColumnString(var1, "suggest_intent_data");
               if(var2 != null) {
                  return var2;
               }
            }

            if(this.mSearchable.shouldRewriteQueryFromText()) {
               var2 = getColumnString(var1, "suggest_text_1");
               if(var2 != null) {
                  return var2;
               }
            }

            var2 = null;
         }
      }

      return var2;
   }

   Drawable getDrawableFromResourceUri(Uri var1) throws FileNotFoundException {
      String var5 = var1.getAuthority();
      if(TextUtils.isEmpty(var5)) {
         throw new FileNotFoundException("No authority: " + var1);
      } else {
         Resources var4;
         try {
            var4 = this.mContext.getPackageManager().getResourcesForApplication(var5);
         } catch (NameNotFoundException var7) {
            throw new FileNotFoundException("No package found for authority: " + var1);
         }

         List var3 = var1.getPathSegments();
         if(var3 == null) {
            throw new FileNotFoundException("No path: " + var1);
         } else {
            int var2 = var3.size();
            if(var2 == 1) {
               try {
                  var2 = Integer.parseInt((String)var3.get(0));
               } catch (NumberFormatException var6) {
                  throw new FileNotFoundException("Single path segment is not a resource ID: " + var1);
               }
            } else {
               if(var2 != 2) {
                  throw new FileNotFoundException("More than two path segments: " + var1);
               }

               var2 = var4.getIdentifier((String)var3.get(1), (String)var3.get(0), var5);
            }

            if(var2 == 0) {
               throw new FileNotFoundException("No resource found for: " + var1);
            } else {
               return var4.getDrawable(var2);
            }
         }
      }
   }

   public int getQueryRefinement() {
      return this.mQueryRefinement;
   }

   Cursor getSearchManagerSuggestions(SearchableInfo var1, String var2, int var3) {
      Cursor var4 = null;
      if(var1 != null) {
         String var5 = var1.getSuggestAuthority();
         if(var5 != null) {
            Builder var8 = (new Builder()).scheme("content").authority(var5).query("").fragment("");
            var5 = var1.getSuggestPath();
            if(var5 != null) {
               var8.appendEncodedPath(var5);
            }

            var8.appendPath("search_suggest_query");
            var5 = var1.getSuggestSelection();
            String[] var6 = null;
            if(var5 != null) {
               var6 = new String[]{var2};
            } else {
               var8.appendPath(var2);
            }

            if(var3 > 0) {
               var8.appendQueryParameter("limit", String.valueOf(var3));
            }

            Uri var7 = var8.build();
            var4 = this.mContext.getContentResolver().query(var7, (String[])null, var5, var6, (String)null);
         }
      }

      return var4;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      try {
         var2 = super.getView(var1, var2, var3);
      } catch (RuntimeException var5) {
         Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", var5);
         View var6 = this.newView(this.mContext, this.mCursor, var3);
         var2 = var6;
         if(var6 != null) {
            ((SuggestionsAdapter.ChildViewCache)var6.getTag()).mText1.setText(var5.toString());
            var2 = var6;
         }
      }

      return var2;
   }

   public boolean hasStableIds() {
      return false;
   }

   public View newView(Context var1, Cursor var2, ViewGroup var3) {
      View var4 = super.newView(var1, var2, var3);
      var4.setTag(new SuggestionsAdapter.ChildViewCache(var4));
      ((ImageView)var4.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
      return var4;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
      this.updateSpinnerState(this.getCursor());
   }

   public void notifyDataSetInvalidated() {
      super.notifyDataSetInvalidated();
      this.updateSpinnerState(this.getCursor());
   }

   public void onClick(View var1) {
      Object var2 = var1.getTag();
      if(var2 instanceof CharSequence) {
         this.mSearchView.onQueryRefine((CharSequence)var2);
      }

   }

   public Cursor runQueryOnBackgroundThread(CharSequence param1) {
      // $FF: Couldn't be decompiled
   }

   public void setQueryRefinement(int var1) {
      this.mQueryRefinement = var1;
   }

   private static final class ChildViewCache {
      public final ImageView mIcon1;
      public final ImageView mIcon2;
      public final ImageView mIconRefine;
      public final TextView mText1;
      public final TextView mText2;

      public ChildViewCache(View var1) {
         this.mText1 = (TextView)var1.findViewById(16908308);
         this.mText2 = (TextView)var1.findViewById(16908309);
         this.mIcon1 = (ImageView)var1.findViewById(16908295);
         this.mIcon2 = (ImageView)var1.findViewById(16908296);
         this.mIconRefine = (ImageView)var1.findViewById(R.id.edit_query);
      }
   }
}
