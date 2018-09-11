package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LruCache;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.ThemeUtils;
import android.support.v7.widget.TintInfo;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public final class AppCompatDrawableManager {
   private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
   private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
   private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
   private static final AppCompatDrawableManager.ColorFilterLruCache COLOR_FILTER_CACHE;
   private static final boolean DEBUG = false;
   private static final Mode DEFAULT_MODE;
   private static AppCompatDrawableManager INSTANCE;
   private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
   private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
   private static final String TAG = "AppCompatDrawableManager";
   private static final int[] TINT_CHECKABLE_BUTTON_LIST;
   private static final int[] TINT_COLOR_CONTROL_NORMAL;
   private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
   private ArrayMap mDelegates;
   private final Object mDrawableCacheLock = new Object();
   private final WeakHashMap mDrawableCaches = new WeakHashMap(0);
   private boolean mHasCheckedVectorDrawableSetup;
   private SparseArray mKnownDrawableIdTags;
   private WeakHashMap mTintLists;
   private TypedValue mTypedValue;

   static {
      DEFAULT_MODE = Mode.SRC_IN;
      COLOR_FILTER_CACHE = new AppCompatDrawableManager.ColorFilterLruCache(6);
      COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
      TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
      COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light};
      COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
      TINT_COLOR_CONTROL_STATE_LIST = new int[]{R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material};
      TINT_CHECKABLE_BUTTON_LIST = new int[]{R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material};
   }

   private void addDelegate(@NonNull String var1, @NonNull AppCompatDrawableManager.InflateDelegate var2) {
      if(this.mDelegates == null) {
         this.mDelegates = new ArrayMap();
      }

      this.mDelegates.put(var1, var2);
   }

   private boolean addDrawableToCache(@NonNull Context param1, long param2, @NonNull Drawable param4) {
      // $FF: Couldn't be decompiled
   }

   private void addTintListToCache(@NonNull Context var1, @DrawableRes int var2, @NonNull ColorStateList var3) {
      if(this.mTintLists == null) {
         this.mTintLists = new WeakHashMap();
      }

      SparseArray var5 = (SparseArray)this.mTintLists.get(var1);
      SparseArray var4 = var5;
      if(var5 == null) {
         var4 = new SparseArray();
         this.mTintLists.put(var1, var4);
      }

      var4.append(var2, var3);
   }

   private static boolean arrayContains(int[] var0, int var1) {
      boolean var5 = false;
      int var3 = var0.length;
      int var2 = 0;

      boolean var4;
      while(true) {
         var4 = var5;
         if(var2 >= var3) {
            break;
         }

         if(var0[var2] == var1) {
            var4 = true;
            break;
         }

         ++var2;
      }

      return var4;
   }

   private void checkVectorDrawableSetup(@NonNull Context var1) {
      if(!this.mHasCheckedVectorDrawableSetup) {
         this.mHasCheckedVectorDrawableSetup = true;
         Drawable var2 = this.getDrawable(var1, R.drawable.abc_vector_test);
         if(var2 == null || !isVectorDrawable(var2)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
         }
      }

   }

   private ColorStateList createBorderlessButtonColorStateList(@NonNull Context var1, @Nullable ColorStateList var2) {
      return this.createButtonColorStateList(var1, 0, (ColorStateList)null);
   }

   private ColorStateList createButtonColorStateList(@NonNull Context var1, @ColorInt int var2, @Nullable ColorStateList var3) {
      int[][] var8 = new int[4][];
      int[] var7 = new int[4];
      int var5 = ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlHighlight);
      int var4 = ThemeUtils.getDisabledThemeAttrColor(var1, R.attr.colorButtonNormal);
      var8[0] = ThemeUtils.DISABLED_STATE_SET;
      if(var3 != null) {
         var4 = var3.getColorForState(var8[0], 0);
      }

      var7[0] = var4;
      int var6 = 0 + 1;
      var8[var6] = ThemeUtils.PRESSED_STATE_SET;
      if(var3 == null) {
         var4 = var2;
      } else {
         var4 = var3.getColorForState(var8[var6], 0);
      }

      var7[var6] = ColorUtils.compositeColors(var5, var4);
      ++var6;
      var8[var6] = ThemeUtils.FOCUSED_STATE_SET;
      if(var3 == null) {
         var4 = var2;
      } else {
         var4 = var3.getColorForState(var8[var6], 0);
      }

      var7[var6] = ColorUtils.compositeColors(var5, var4);
      var4 = var6 + 1;
      var8[var4] = ThemeUtils.EMPTY_STATE_SET;
      if(var3 != null) {
         var2 = var3.getColorForState(var8[var4], 0);
      }

      var7[var4] = var2;
      return new ColorStateList(var8, var7);
   }

   private static long createCacheKey(TypedValue var0) {
      return (long)var0.assetCookie << 32 | (long)var0.data;
   }

   private ColorStateList createColoredButtonColorStateList(@NonNull Context var1, @Nullable ColorStateList var2) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R.attr.colorAccent), var2);
   }

   private ColorStateList createDefaultButtonColorStateList(@NonNull Context var1, @Nullable ColorStateList var2) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R.attr.colorButtonNormal), var2);
   }

   private Drawable createDrawableIfNeeded(@NonNull Context var1, @DrawableRes int var2) {
      if(this.mTypedValue == null) {
         this.mTypedValue = new TypedValue();
      }

      TypedValue var6 = this.mTypedValue;
      var1.getResources().getValue(var2, var6, true);
      long var3 = createCacheKey(var6);
      Object var5 = this.getCachedDrawable(var1, var3);
      if(var5 == null) {
         if(var2 == R.drawable.abc_cab_background_top_material) {
            var5 = new LayerDrawable(new Drawable[]{this.getDrawable(var1, R.drawable.abc_cab_background_internal_bg), this.getDrawable(var1, R.drawable.abc_cab_background_top_mtrl_alpha)});
         }

         if(var5 != null) {
            ((Drawable)var5).setChangingConfigurations(var6.changingConfigurations);
            this.addDrawableToCache(var1, var3, (Drawable)var5);
         }
      }

      return (Drawable)var5;
   }

   private static PorterDuffColorFilter createTintFilter(ColorStateList var0, Mode var1, int[] var2) {
      PorterDuffColorFilter var3;
      if(var0 != null && var1 != null) {
         var3 = getPorterDuffColorFilter(var0.getColorForState(var2, 0), var1);
      } else {
         var3 = null;
      }

      return var3;
   }

   public static AppCompatDrawableManager get() {
      if(INSTANCE == null) {
         INSTANCE = new AppCompatDrawableManager();
         installDefaultInflateDelegates(INSTANCE);
      }

      return INSTANCE;
   }

   private Drawable getCachedDrawable(@NonNull Context param1, long param2) {
      // $FF: Couldn't be decompiled
   }

   public static PorterDuffColorFilter getPorterDuffColorFilter(int var0, Mode var1) {
      PorterDuffColorFilter var3 = COLOR_FILTER_CACHE.get(var0, var1);
      PorterDuffColorFilter var2 = var3;
      if(var3 == null) {
         var2 = new PorterDuffColorFilter(var0, var1);
         COLOR_FILTER_CACHE.put(var0, var1, var2);
      }

      return var2;
   }

   private ColorStateList getTintListFromCache(@NonNull Context var1, @DrawableRes int var2) {
      Object var4 = null;
      ColorStateList var3 = (ColorStateList)var4;
      if(this.mTintLists != null) {
         SparseArray var5 = (SparseArray)this.mTintLists.get(var1);
         var3 = (ColorStateList)var4;
         if(var5 != null) {
            var3 = (ColorStateList)var5.get(var2);
         }
      }

      return var3;
   }

   static Mode getTintMode(int var0) {
      Mode var1 = null;
      if(var0 == R.drawable.abc_switch_thumb_material) {
         var1 = Mode.MULTIPLY;
      }

      return var1;
   }

   private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager var0) {
      int var1 = VERSION.SDK_INT;
      if(var1 < 24) {
         var0.addDelegate("vector", new AppCompatDrawableManager.VdcInflateDelegate());
         if(var1 >= 11) {
            var0.addDelegate("animated-vector", new AppCompatDrawableManager.AvdcInflateDelegate());
         }
      }

   }

   private static boolean isVectorDrawable(@NonNull Drawable var0) {
      boolean var1;
      if(!(var0 instanceof VectorDrawableCompat) && !"android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private Drawable loadDrawableFromDelegates(@NonNull Context param1, @DrawableRes int param2) {
      // $FF: Couldn't be decompiled
   }

   private void removeDelegate(@NonNull String var1, @NonNull AppCompatDrawableManager.InflateDelegate var2) {
      if(this.mDelegates != null && this.mDelegates.get(var1) == var2) {
         this.mDelegates.remove(var1);
      }

   }

   private static void setPorterDuffColorFilter(Drawable var0, int var1, Mode var2) {
      Drawable var3 = var0;
      if(DrawableUtils.canSafelyMutateDrawable(var0)) {
         var3 = var0.mutate();
      }

      Mode var4 = var2;
      if(var2 == null) {
         var4 = DEFAULT_MODE;
      }

      var3.setColorFilter(getPorterDuffColorFilter(var1, var4));
   }

   private Drawable tintDrawable(@NonNull Context var1, @DrawableRes int var2, boolean var3, @NonNull Drawable var4) {
      ColorStateList var5 = this.getTintList(var1, var2);
      Drawable var8;
      if(var5 != null) {
         Drawable var6 = var4;
         if(DrawableUtils.canSafelyMutateDrawable(var4)) {
            var6 = var4.mutate();
         }

         var6 = DrawableCompat.wrap(var6);
         DrawableCompat.setTintList(var6, var5);
         Mode var7 = getTintMode(var2);
         var8 = var6;
         if(var7 != null) {
            DrawableCompat.setTintMode(var6, var7);
            var8 = var6;
         }
      } else {
         LayerDrawable var9;
         if(var2 == R.drawable.abc_seekbar_track_material) {
            var9 = (LayerDrawable)var4;
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlActivated), DEFAULT_MODE);
            var8 = var4;
         } else if(var2 != R.drawable.abc_ratingbar_material && var2 != R.drawable.abc_ratingbar_indicator_material && var2 != R.drawable.abc_ratingbar_small_material) {
            var8 = var4;
            if(!tintDrawableUsingColorFilter(var1, var2, var4)) {
               var8 = var4;
               if(var3) {
                  var8 = null;
               }
            }
         } else {
            var9 = (LayerDrawable)var4;
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(var1, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R.attr.colorControlActivated), DEFAULT_MODE);
            var8 = var4;
         }
      }

      return var8;
   }

   static void tintDrawable(Drawable var0, TintInfo var1, int[] var2) {
      if(DrawableUtils.canSafelyMutateDrawable(var0) && var0.mutate() != var0) {
         Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
      } else {
         if(!var1.mHasTintList && !var1.mHasTintMode) {
            var0.clearColorFilter();
         } else {
            ColorStateList var3;
            if(var1.mHasTintList) {
               var3 = var1.mTintList;
            } else {
               var3 = null;
            }

            Mode var4;
            if(var1.mHasTintMode) {
               var4 = var1.mTintMode;
            } else {
               var4 = DEFAULT_MODE;
            }

            var0.setColorFilter(createTintFilter(var3, var4, var2));
         }

         if(VERSION.SDK_INT <= 23) {
            var0.invalidateSelf();
         }
      }

   }

   static boolean tintDrawableUsingColorFilter(@NonNull Context var0, @DrawableRes int var1, @NonNull Drawable var2) {
      Mode var9 = DEFAULT_MODE;
      boolean var4 = false;
      int var3 = 0;
      byte var6 = -1;
      int var5;
      Mode var8;
      if(arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, var1)) {
         var3 = R.attr.colorControlNormal;
         var4 = true;
         var8 = var9;
         var5 = var6;
      } else if(arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, var1)) {
         var3 = R.attr.colorControlActivated;
         var4 = true;
         var5 = var6;
         var8 = var9;
      } else if(arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, var1)) {
         var3 = 16842801;
         var4 = true;
         var8 = Mode.MULTIPLY;
         var5 = var6;
      } else if(var1 == R.drawable.abc_list_divider_mtrl_alpha) {
         var3 = 16842800;
         var4 = true;
         var5 = Math.round(40.8F);
         var8 = var9;
      } else {
         var5 = var6;
         var8 = var9;
         if(var1 == R.drawable.abc_dialog_material_background) {
            var3 = 16842801;
            var4 = true;
            var5 = var6;
            var8 = var9;
         }
      }

      boolean var7;
      if(var4) {
         Drawable var10 = var2;
         if(DrawableUtils.canSafelyMutateDrawable(var2)) {
            var10 = var2.mutate();
         }

         var10.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(var0, var3), var8));
         if(var5 != -1) {
            var10.setAlpha(var5);
         }

         var7 = true;
      } else {
         var7 = false;
      }

      return var7;
   }

   public Drawable getDrawable(@NonNull Context var1, @DrawableRes int var2) {
      return this.getDrawable(var1, var2, false);
   }

   Drawable getDrawable(@NonNull Context var1, @DrawableRes int var2, boolean var3) {
      this.checkVectorDrawableSetup(var1);
      Drawable var4 = this.loadDrawableFromDelegates(var1, var2);
      Drawable var5 = var4;
      if(var4 == null) {
         var5 = this.createDrawableIfNeeded(var1, var2);
      }

      var4 = var5;
      if(var5 == null) {
         var4 = ContextCompat.getDrawable(var1, var2);
      }

      var5 = var4;
      if(var4 != null) {
         var5 = this.tintDrawable(var1, var2, var3, var4);
      }

      if(var5 != null) {
         DrawableUtils.fixDrawable(var5);
      }

      return var5;
   }

   ColorStateList getTintList(@NonNull Context var1, @DrawableRes int var2) {
      return this.getTintList(var1, var2, (ColorStateList)null);
   }

   ColorStateList getTintList(@NonNull Context var1, @DrawableRes int var2, @Nullable ColorStateList var3) {
      boolean var4;
      if(var3 == null) {
         var4 = true;
      } else {
         var4 = false;
      }

      ColorStateList var5;
      if(var4) {
         var5 = this.getTintListFromCache(var1, var2);
      } else {
         var5 = null;
      }

      ColorStateList var6 = var5;
      if(var5 == null) {
         if(var2 == R.drawable.abc_edit_text_material) {
            var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_edittext);
         } else if(var2 == R.drawable.abc_switch_track_mtrl_alpha) {
            var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_switch_track);
         } else if(var2 == R.drawable.abc_switch_thumb_material) {
            var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_switch_thumb);
         } else if(var2 == R.drawable.abc_btn_default_mtrl_shape) {
            var3 = this.createDefaultButtonColorStateList(var1, var3);
         } else if(var2 == R.drawable.abc_btn_borderless_material) {
            var3 = this.createBorderlessButtonColorStateList(var1, var3);
         } else if(var2 == R.drawable.abc_btn_colored_material) {
            var3 = this.createColoredButtonColorStateList(var1, var3);
         } else if(var2 != R.drawable.abc_spinner_mtrl_am_alpha && var2 != R.drawable.abc_spinner_textfield_background_material) {
            if(arrayContains(TINT_COLOR_CONTROL_NORMAL, var2)) {
               var3 = ThemeUtils.getThemeAttrColorStateList(var1, R.attr.colorControlNormal);
            } else if(arrayContains(TINT_COLOR_CONTROL_STATE_LIST, var2)) {
               var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_default);
            } else if(arrayContains(TINT_CHECKABLE_BUTTON_LIST, var2)) {
               var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_btn_checkable);
            } else {
               var3 = var5;
               if(var2 == R.drawable.abc_seekbar_thumb_material) {
                  var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_seek_thumb);
               }
            }
         } else {
            var3 = AppCompatResources.getColorStateList(var1, R.color.abc_tint_spinner);
         }

         var6 = var3;
         if(var4) {
            var6 = var3;
            if(var3 != null) {
               this.addTintListToCache(var1, var2, var3);
               var6 = var3;
            }
         }
      }

      return var6;
   }

   public void onConfigurationChanged(@NonNull Context param1) {
      // $FF: Couldn't be decompiled
   }

   Drawable onDrawableLoadedFromResources(@NonNull Context var1, @NonNull VectorEnabledTintResources var2, @DrawableRes int var3) {
      Drawable var5 = this.loadDrawableFromDelegates(var1, var3);
      Drawable var4 = var5;
      if(var5 == null) {
         var4 = var2.superGetDrawable(var3);
      }

      Drawable var6;
      if(var4 != null) {
         var6 = this.tintDrawable(var1, var3, false, var4);
      } else {
         var6 = null;
      }

      return var6;
   }

   private static class AvdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      public Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4) {
         AnimatedVectorDrawableCompat var6;
         try {
            var6 = AnimatedVectorDrawableCompat.createFromXmlInner(var1, var1.getResources(), var2, var3, var4);
         } catch (Exception var5) {
            Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", var5);
            var6 = null;
         }

         return var6;
      }
   }

   private static class ColorFilterLruCache extends LruCache {
      public ColorFilterLruCache(int var1) {
         super(var1);
      }

      private static int generateCacheKey(int var0, Mode var1) {
         return (var0 + 31) * 31 + var1.hashCode();
      }

      PorterDuffColorFilter get(int var1, Mode var2) {
         return (PorterDuffColorFilter)this.get(Integer.valueOf(generateCacheKey(var1, var2)));
      }

      PorterDuffColorFilter put(int var1, Mode var2, PorterDuffColorFilter var3) {
         return (PorterDuffColorFilter)this.put(Integer.valueOf(generateCacheKey(var1, var2)), var3);
      }
   }

   private interface InflateDelegate {
      Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4);
   }

   private static class VdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      public Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4) {
         VectorDrawableCompat var6;
         try {
            var6 = VectorDrawableCompat.createFromXmlInner(var1.getResources(), var2, var3, var4);
         } catch (Exception var5) {
            Log.e("VdcInflateDelegate", "Exception while inflating <vector>", var5);
            var6 = null;
         }

         return var6;
      }
   }
}
