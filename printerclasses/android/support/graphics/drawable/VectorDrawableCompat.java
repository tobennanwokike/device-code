package android.support.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AndroidResources;
import android.support.graphics.drawable.PathParser;
import android.support.graphics.drawable.TypedArrayUtils;
import android.support.graphics.drawable.VectorDrawableCommon;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@TargetApi(21)
public class VectorDrawableCompat extends VectorDrawableCommon {
   private static final boolean DBG_VECTOR_DRAWABLE = false;
   static final Mode DEFAULT_TINT_MODE;
   private static final int LINECAP_BUTT = 0;
   private static final int LINECAP_ROUND = 1;
   private static final int LINECAP_SQUARE = 2;
   private static final int LINEJOIN_BEVEL = 2;
   private static final int LINEJOIN_MITER = 0;
   private static final int LINEJOIN_ROUND = 1;
   static final String LOGTAG = "VectorDrawableCompat";
   private static final int MAX_CACHED_BITMAP_SIZE = 2048;
   private static final String SHAPE_CLIP_PATH = "clip-path";
   private static final String SHAPE_GROUP = "group";
   private static final String SHAPE_PATH = "path";
   private static final String SHAPE_VECTOR = "vector";
   private boolean mAllowCaching = true;
   private ConstantState mCachedConstantStateDelegate;
   private ColorFilter mColorFilter;
   private boolean mMutated;
   private PorterDuffColorFilter mTintFilter;
   private final Rect mTmpBounds = new Rect();
   private final float[] mTmpFloats = new float[9];
   private final Matrix mTmpMatrix = new Matrix();
   private VectorDrawableCompat.VectorDrawableCompatState mVectorState;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   VectorDrawableCompat() {
      this.mVectorState = new VectorDrawableCompat.VectorDrawableCompatState();
   }

   VectorDrawableCompat(@NonNull VectorDrawableCompat.VectorDrawableCompatState var1) {
      this.mVectorState = var1;
      this.mTintFilter = this.updateTintFilter(this.mTintFilter, var1.mTint, var1.mTintMode);
   }

   static int applyAlpha(int var0, float var1) {
      return var0 & 16777215 | (int)((float)Color.alpha(var0) * var1) << 24;
   }

   @Nullable
   public static VectorDrawableCompat create(@NonNull Resources param0, @DrawableRes int param1, @Nullable Theme param2) {
      // $FF: Couldn't be decompiled
   }

   public static VectorDrawableCompat createFromXmlInner(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws XmlPullParserException, IOException {
      VectorDrawableCompat var4 = new VectorDrawableCompat();
      var4.inflate(var0, var1, var2, var3);
      return var4;
   }

   private void inflateInternal(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      VectorDrawableCompat.VectorDrawableCompatState var10 = this.mVectorState;
      VectorDrawableCompat.VPathRenderer var9 = var10.mVPathRenderer;
      boolean var6 = true;
      Stack var11 = new Stack();
      var11.push(var9.mRootGroup);
      int var7 = var2.getEventType();

      boolean var5;
      for(int var8 = var2.getDepth(); var7 != 1 && (var2.getDepth() >= var8 + 1 || var7 != 3); var6 = var5) {
         if(var7 == 2) {
            String var13 = var2.getName();
            VectorDrawableCompat.VGroup var12 = (VectorDrawableCompat.VGroup)var11.peek();
            if("path".equals(var13)) {
               VectorDrawableCompat.VFullPath var15 = new VectorDrawableCompat.VFullPath();
               var15.inflate(var1, var3, var4, var2);
               var12.mChildren.add(var15);
               if(var15.getPathName() != null) {
                  var9.mVGTargetsMap.put(var15.getPathName(), var15);
               }

               var5 = false;
               var10.mChangingConfigurations |= var15.mChangingConfigurations;
            } else if("clip-path".equals(var13)) {
               VectorDrawableCompat.VClipPath var16 = new VectorDrawableCompat.VClipPath();
               var16.inflate(var1, var3, var4, var2);
               var12.mChildren.add(var16);
               if(var16.getPathName() != null) {
                  var9.mVGTargetsMap.put(var16.getPathName(), var16);
               }

               var10.mChangingConfigurations |= var16.mChangingConfigurations;
               var5 = var6;
            } else {
               var5 = var6;
               if("group".equals(var13)) {
                  VectorDrawableCompat.VGroup var17 = new VectorDrawableCompat.VGroup();
                  var17.inflate(var1, var3, var4, var2);
                  var12.mChildren.add(var17);
                  var11.push(var17);
                  if(var17.getGroupName() != null) {
                     var9.mVGTargetsMap.put(var17.getGroupName(), var17);
                  }

                  var10.mChangingConfigurations |= var17.mChangingConfigurations;
                  var5 = var6;
               }
            }
         } else {
            var5 = var6;
            if(var7 == 3) {
               var5 = var6;
               if("group".equals(var2.getName())) {
                  var11.pop();
                  var5 = var6;
               }
            }
         }

         var7 = var2.next();
      }

      if(var6) {
         StringBuffer var14 = new StringBuffer();
         if(var14.length() > 0) {
            var14.append(" or ");
         }

         var14.append("path");
         throw new XmlPullParserException("no " + var14 + " defined");
      }
   }

   private boolean needMirroring() {
      boolean var1 = true;
      boolean var2 = false;
      if(VERSION.SDK_INT < 17) {
         var1 = var2;
      } else if(!this.isAutoMirrored() || this.getLayoutDirection() != 1) {
         var1 = false;
      }

      return var1;
   }

   private static Mode parseTintModeCompat(int var0, Mode var1) {
      Mode var2 = var1;
      switch(var0) {
      case 3:
         var2 = Mode.SRC_OVER;
      case 4:
      case 6:
      case 7:
      case 8:
      case 10:
      case 11:
      case 12:
      case 13:
         break;
      case 5:
         var2 = Mode.SRC_IN;
         break;
      case 9:
         var2 = Mode.SRC_ATOP;
         break;
      case 14:
         var2 = Mode.MULTIPLY;
         break;
      case 15:
         var2 = Mode.SCREEN;
         break;
      case 16:
         var2 = Mode.ADD;
         break;
      default:
         var2 = var1;
      }

      return var2;
   }

   private void printGroupTree(VectorDrawableCompat.VGroup var1, int var2) {
      String var4 = "";

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = var4 + "    ";
      }

      Log.v("VectorDrawableCompat", var4 + "current group is :" + var1.getGroupName() + " rotation is " + var1.mRotate);
      Log.v("VectorDrawableCompat", var4 + "matrix is :" + var1.getLocalMatrix().toString());

      for(var3 = 0; var3 < var1.mChildren.size(); ++var3) {
         Object var5 = var1.mChildren.get(var3);
         if(var5 instanceof VectorDrawableCompat.VGroup) {
            this.printGroupTree((VectorDrawableCompat.VGroup)var5, var2 + 1);
         } else {
            ((VectorDrawableCompat.VPath)var5).printVPath(var2 + 1);
         }
      }

   }

   private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) throws XmlPullParserException {
      VectorDrawableCompat.VectorDrawableCompatState var5 = this.mVectorState;
      VectorDrawableCompat.VPathRenderer var3 = var5.mVPathRenderer;
      var5.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(var1, var2, "tintMode", 6, -1), Mode.SRC_IN);
      ColorStateList var4 = var1.getColorStateList(1);
      if(var4 != null) {
         var5.mTint = var4;
      }

      var5.mAutoMirrored = TypedArrayUtils.getNamedBoolean(var1, var2, "autoMirrored", 5, var5.mAutoMirrored);
      var3.mViewportWidth = TypedArrayUtils.getNamedFloat(var1, var2, "viewportWidth", 7, var3.mViewportWidth);
      var3.mViewportHeight = TypedArrayUtils.getNamedFloat(var1, var2, "viewportHeight", 8, var3.mViewportHeight);
      if(var3.mViewportWidth <= 0.0F) {
         throw new XmlPullParserException(var1.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
      } else if(var3.mViewportHeight <= 0.0F) {
         throw new XmlPullParserException(var1.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
      } else {
         var3.mBaseWidth = var1.getDimension(3, var3.mBaseWidth);
         var3.mBaseHeight = var1.getDimension(2, var3.mBaseHeight);
         if(var3.mBaseWidth <= 0.0F) {
            throw new XmlPullParserException(var1.getPositionDescription() + "<vector> tag requires width > 0");
         } else if(var3.mBaseHeight <= 0.0F) {
            throw new XmlPullParserException(var1.getPositionDescription() + "<vector> tag requires height > 0");
         } else {
            var3.setAlpha(TypedArrayUtils.getNamedFloat(var1, var2, "alpha", 4, var3.getAlpha()));
            String var6 = var1.getString(0);
            if(var6 != null) {
               var3.mRootName = var6;
               var3.mVGTargetsMap.put(var6, var3);
            }

         }
      }
   }

   public boolean canApplyTheme() {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.canApplyTheme(this.mDelegateDrawable);
      }

      return false;
   }

   public void draw(Canvas var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.draw(var1);
      } else {
         this.copyBounds(this.mTmpBounds);
         if(this.mTmpBounds.width() > 0 && this.mTmpBounds.height() > 0) {
            Object var9;
            if(this.mColorFilter == null) {
               var9 = this.mTintFilter;
            } else {
               var9 = this.mColorFilter;
            }

            var1.getMatrix(this.mTmpMatrix);
            this.mTmpMatrix.getValues(this.mTmpFloats);
            float var3 = Math.abs(this.mTmpFloats[0]);
            float var2 = Math.abs(this.mTmpFloats[4]);
            float var5 = Math.abs(this.mTmpFloats[1]);
            float var4 = Math.abs(this.mTmpFloats[3]);
            if(var5 != 0.0F || var4 != 0.0F) {
               var3 = 1.0F;
               var2 = 1.0F;
            }

            int var6 = (int)((float)this.mTmpBounds.width() * var3);
            int var7 = (int)((float)this.mTmpBounds.height() * var2);
            var6 = Math.min(2048, var6);
            int var8 = Math.min(2048, var7);
            if(var6 > 0 && var8 > 0) {
               var7 = var1.save();
               var1.translate((float)this.mTmpBounds.left, (float)this.mTmpBounds.top);
               if(this.needMirroring()) {
                  var1.translate((float)this.mTmpBounds.width(), 0.0F);
                  var1.scale(-1.0F, 1.0F);
               }

               this.mTmpBounds.offsetTo(0, 0);
               this.mVectorState.createCachedBitmapIfNeeded(var6, var8);
               if(!this.mAllowCaching) {
                  this.mVectorState.updateCachedBitmap(var6, var8);
               } else if(!this.mVectorState.canReuseCache()) {
                  this.mVectorState.updateCachedBitmap(var6, var8);
                  this.mVectorState.updateCacheStates();
               }

               this.mVectorState.drawCachedBitmapWithRootAlpha(var1, (ColorFilter)var9, this.mTmpBounds);
               var1.restoreToCount(var7);
            }
         }
      }

   }

   public int getAlpha() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = DrawableCompat.getAlpha(this.mDelegateDrawable);
      } else {
         var1 = this.mVectorState.mVPathRenderer.getRootAlpha();
      }

      return var1;
   }

   public int getChangingConfigurations() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getChangingConfigurations();
      } else {
         var1 = super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
      }

      return var1;
   }

   public ConstantState getConstantState() {
      Object var1;
      if(this.mDelegateDrawable != null) {
         var1 = new VectorDrawableCompat.VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
      } else {
         this.mVectorState.mChangingConfigurations = this.getChangingConfigurations();
         var1 = this.mVectorState;
      }

      return (ConstantState)var1;
   }

   public int getIntrinsicHeight() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getIntrinsicHeight();
      } else {
         var1 = (int)this.mVectorState.mVPathRenderer.mBaseHeight;
      }

      return var1;
   }

   public int getIntrinsicWidth() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getIntrinsicWidth();
      } else {
         var1 = (int)this.mVectorState.mVPathRenderer.mBaseWidth;
      }

      return var1;
   }

   public int getOpacity() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getOpacity();
      } else {
         var1 = -3;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public float getPixelSize() {
      float var1;
      if((this.mVectorState != null || this.mVectorState.mVPathRenderer != null) && this.mVectorState.mVPathRenderer.mBaseWidth != 0.0F && this.mVectorState.mVPathRenderer.mBaseHeight != 0.0F && this.mVectorState.mVPathRenderer.mViewportHeight != 0.0F && this.mVectorState.mVPathRenderer.mViewportWidth != 0.0F) {
         var1 = this.mVectorState.mVPathRenderer.mBaseWidth;
         float var3 = this.mVectorState.mVPathRenderer.mBaseHeight;
         float var2 = this.mVectorState.mVPathRenderer.mViewportWidth;
         float var4 = this.mVectorState.mVPathRenderer.mViewportHeight;
         var1 = Math.min(var2 / var1, var4 / var3);
      } else {
         var1 = 1.0F;
      }

      return var1;
   }

   Object getTargetByName(String var1) {
      return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(var1);
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.inflate(var1, var2, var3);
      } else {
         this.inflate(var1, var2, var3, (Theme)null);
      }

   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.inflate(this.mDelegateDrawable, var1, var2, var3, var4);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var5 = this.mVectorState;
         var5.mVPathRenderer = new VectorDrawableCompat.VPathRenderer();
         TypedArray var6 = obtainAttributes(var1, var4, var3, AndroidResources.styleable_VectorDrawableTypeArray);
         this.updateStateFromTypedArray(var6, var2);
         var6.recycle();
         var5.mChangingConfigurations = this.getChangingConfigurations();
         var5.mCacheDirty = true;
         this.inflateInternal(var1, var2, var3, var4);
         this.mTintFilter = this.updateTintFilter(this.mTintFilter, var5.mTint, var5.mTintMode);
      }

   }

   public void invalidateSelf() {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.invalidateSelf();
      } else {
         super.invalidateSelf();
      }

   }

   public boolean isAutoMirrored() {
      boolean var1;
      if(this.mDelegateDrawable != null) {
         var1 = DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
      } else {
         var1 = this.mVectorState.mAutoMirrored;
      }

      return var1;
   }

   public boolean isStateful() {
      boolean var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.isStateful();
      } else if(!super.isStateful() && (this.mVectorState == null || this.mVectorState.mTint == null || !this.mVectorState.mTint.isStateful())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public Drawable mutate() {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.mutate();
      } else if(!this.mMutated && super.mutate() == this) {
         this.mVectorState = new VectorDrawableCompat.VectorDrawableCompatState(this.mVectorState);
         this.mMutated = true;
      }

      return this;
   }

   protected void onBoundsChange(Rect var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setBounds(var1);
      }

   }

   protected boolean onStateChange(int[] var1) {
      boolean var2;
      if(this.mDelegateDrawable != null) {
         var2 = this.mDelegateDrawable.setState(var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var3 = this.mVectorState;
         if(var3.mTint != null && var3.mTintMode != null) {
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var3.mTint, var3.mTintMode);
            this.invalidateSelf();
            var2 = true;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public void scheduleSelf(Runnable var1, long var2) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.scheduleSelf(var1, var2);
      } else {
         super.scheduleSelf(var1, var2);
      }

   }

   void setAllowCaching(boolean var1) {
      this.mAllowCaching = var1;
   }

   public void setAlpha(int var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setAlpha(var1);
      } else if(this.mVectorState.mVPathRenderer.getRootAlpha() != var1) {
         this.mVectorState.mVPathRenderer.setRootAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setAutoMirrored(boolean var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setAutoMirrored(this.mDelegateDrawable, var1);
      } else {
         this.mVectorState.mAutoMirrored = var1;
      }

   }

   public void setColorFilter(ColorFilter var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setColorFilter(var1);
      } else {
         this.mColorFilter = var1;
         this.invalidateSelf();
      }

   }

   public void setTint(int var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTint(this.mDelegateDrawable, var1);
      } else {
         this.setTintList(ColorStateList.valueOf(var1));
      }

   }

   public void setTintList(ColorStateList var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTintList(this.mDelegateDrawable, var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var2 = this.mVectorState;
         if(var2.mTint != var1) {
            var2.mTint = var1;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var1, var2.mTintMode);
            this.invalidateSelf();
         }
      }

   }

   public void setTintMode(Mode var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTintMode(this.mDelegateDrawable, var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var2 = this.mVectorState;
         if(var2.mTintMode != var1) {
            var2.mTintMode = var1;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var2.mTint, var1);
            this.invalidateSelf();
         }
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.setVisible(var1, var2);
      } else {
         var1 = super.setVisible(var1, var2);
      }

      return var1;
   }

   public void unscheduleSelf(Runnable var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.unscheduleSelf(var1);
      } else {
         super.unscheduleSelf(var1);
      }

   }

   PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter var1, ColorStateList var2, Mode var3) {
      if(var2 != null && var3 != null) {
         var1 = new PorterDuffColorFilter(var2.getColorForState(this.getState(), 0), var3);
      } else {
         var1 = null;
      }

      return var1;
   }

   private static class VClipPath extends VectorDrawableCompat.VPath {
      public VClipPath() {
      }

      public VClipPath(VectorDrawableCompat.VClipPath var1) {
         super(var1);
      }

      private void updateStateFromTypedArray(TypedArray var1) {
         String var2 = var1.getString(0);
         if(var2 != null) {
            this.mPathName = var2;
         }

         String var3 = var1.getString(1);
         if(var3 != null) {
            this.mNodes = PathParser.createNodesFromPathData(var3);
         }

      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         if(TypedArrayUtils.hasAttribute(var4, "pathData")) {
            TypedArray var5 = VectorDrawableCommon.obtainAttributes(var1, var3, var2, AndroidResources.styleable_VectorDrawableClipPath);
            this.updateStateFromTypedArray(var5);
            var5.recycle();
         }

      }

      public boolean isClipPath() {
         return true;
      }
   }

   private static class VFullPath extends VectorDrawableCompat.VPath {
      float mFillAlpha = 1.0F;
      int mFillColor = 0;
      int mFillRule;
      float mStrokeAlpha = 1.0F;
      int mStrokeColor = 0;
      Cap mStrokeLineCap;
      Join mStrokeLineJoin;
      float mStrokeMiterlimit;
      float mStrokeWidth = 0.0F;
      private int[] mThemeAttrs;
      float mTrimPathEnd = 1.0F;
      float mTrimPathOffset = 0.0F;
      float mTrimPathStart = 0.0F;

      public VFullPath() {
         this.mStrokeLineCap = Cap.BUTT;
         this.mStrokeLineJoin = Join.MITER;
         this.mStrokeMiterlimit = 4.0F;
      }

      public VFullPath(VectorDrawableCompat.VFullPath var1) {
         super(var1);
         this.mStrokeLineCap = Cap.BUTT;
         this.mStrokeLineJoin = Join.MITER;
         this.mStrokeMiterlimit = 4.0F;
         this.mThemeAttrs = var1.mThemeAttrs;
         this.mStrokeColor = var1.mStrokeColor;
         this.mStrokeWidth = var1.mStrokeWidth;
         this.mStrokeAlpha = var1.mStrokeAlpha;
         this.mFillColor = var1.mFillColor;
         this.mFillRule = var1.mFillRule;
         this.mFillAlpha = var1.mFillAlpha;
         this.mTrimPathStart = var1.mTrimPathStart;
         this.mTrimPathEnd = var1.mTrimPathEnd;
         this.mTrimPathOffset = var1.mTrimPathOffset;
         this.mStrokeLineCap = var1.mStrokeLineCap;
         this.mStrokeLineJoin = var1.mStrokeLineJoin;
         this.mStrokeMiterlimit = var1.mStrokeMiterlimit;
      }

      private Cap getStrokeLineCap(int var1, Cap var2) {
         switch(var1) {
         case 0:
            var2 = Cap.BUTT;
            break;
         case 1:
            var2 = Cap.ROUND;
            break;
         case 2:
            var2 = Cap.SQUARE;
         }

         return var2;
      }

      private Join getStrokeLineJoin(int var1, Join var2) {
         switch(var1) {
         case 0:
            var2 = Join.MITER;
            break;
         case 1:
            var2 = Join.ROUND;
            break;
         case 2:
            var2 = Join.BEVEL;
         }

         return var2;
      }

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) {
         this.mThemeAttrs = null;
         if(TypedArrayUtils.hasAttribute(var2, "pathData")) {
            String var3 = var1.getString(0);
            if(var3 != null) {
               this.mPathName = var3;
            }

            var3 = var1.getString(2);
            if(var3 != null) {
               this.mNodes = PathParser.createNodesFromPathData(var3);
            }

            this.mFillColor = TypedArrayUtils.getNamedColor(var1, var2, "fillColor", 1, this.mFillColor);
            this.mFillAlpha = TypedArrayUtils.getNamedFloat(var1, var2, "fillAlpha", 12, this.mFillAlpha);
            this.mStrokeLineCap = this.getStrokeLineCap(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineCap", 8, -1), this.mStrokeLineCap);
            this.mStrokeLineJoin = this.getStrokeLineJoin(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
            this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(var1, var2, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
            this.mStrokeColor = TypedArrayUtils.getNamedColor(var1, var2, "strokeColor", 3, this.mStrokeColor);
            this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(var1, var2, "strokeAlpha", 11, this.mStrokeAlpha);
            this.mStrokeWidth = TypedArrayUtils.getNamedFloat(var1, var2, "strokeWidth", 4, this.mStrokeWidth);
            this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathEnd", 6, this.mTrimPathEnd);
            this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathOffset", 7, this.mTrimPathOffset);
            this.mTrimPathStart = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathStart", 5, this.mTrimPathStart);
         }

      }

      public void applyTheme(Theme var1) {
         if(this.mThemeAttrs == null) {
            ;
         }

      }

      public boolean canApplyTheme() {
         boolean var1;
         if(this.mThemeAttrs != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      float getFillAlpha() {
         return this.mFillAlpha;
      }

      int getFillColor() {
         return this.mFillColor;
      }

      float getStrokeAlpha() {
         return this.mStrokeAlpha;
      }

      int getStrokeColor() {
         return this.mStrokeColor;
      }

      float getStrokeWidth() {
         return this.mStrokeWidth;
      }

      float getTrimPathEnd() {
         return this.mTrimPathEnd;
      }

      float getTrimPathOffset() {
         return this.mTrimPathOffset;
      }

      float getTrimPathStart() {
         return this.mTrimPathStart;
      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         TypedArray var5 = VectorDrawableCommon.obtainAttributes(var1, var3, var2, AndroidResources.styleable_VectorDrawablePath);
         this.updateStateFromTypedArray(var5, var4);
         var5.recycle();
      }

      void setFillAlpha(float var1) {
         this.mFillAlpha = var1;
      }

      void setFillColor(int var1) {
         this.mFillColor = var1;
      }

      void setStrokeAlpha(float var1) {
         this.mStrokeAlpha = var1;
      }

      void setStrokeColor(int var1) {
         this.mStrokeColor = var1;
      }

      void setStrokeWidth(float var1) {
         this.mStrokeWidth = var1;
      }

      void setTrimPathEnd(float var1) {
         this.mTrimPathEnd = var1;
      }

      void setTrimPathOffset(float var1) {
         this.mTrimPathOffset = var1;
      }

      void setTrimPathStart(float var1) {
         this.mTrimPathStart = var1;
      }
   }

   private static class VGroup {
      int mChangingConfigurations;
      final ArrayList mChildren = new ArrayList();
      private String mGroupName = null;
      private final Matrix mLocalMatrix = new Matrix();
      private float mPivotX = 0.0F;
      private float mPivotY = 0.0F;
      float mRotate = 0.0F;
      private float mScaleX = 1.0F;
      private float mScaleY = 1.0F;
      private final Matrix mStackedMatrix = new Matrix();
      private int[] mThemeAttrs;
      private float mTranslateX = 0.0F;
      private float mTranslateY = 0.0F;

      public VGroup() {
      }

      public VGroup(VectorDrawableCompat.VGroup var1, ArrayMap var2) {
         this.mRotate = var1.mRotate;
         this.mPivotX = var1.mPivotX;
         this.mPivotY = var1.mPivotY;
         this.mScaleX = var1.mScaleX;
         this.mScaleY = var1.mScaleY;
         this.mTranslateX = var1.mTranslateX;
         this.mTranslateY = var1.mTranslateY;
         this.mThemeAttrs = var1.mThemeAttrs;
         this.mGroupName = var1.mGroupName;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         if(this.mGroupName != null) {
            var2.put(this.mGroupName, this);
         }

         this.mLocalMatrix.set(var1.mLocalMatrix);
         ArrayList var4 = var1.mChildren;

         for(int var3 = 0; var3 < var4.size(); ++var3) {
            Object var5 = var4.get(var3);
            if(var5 instanceof VectorDrawableCompat.VGroup) {
               var1 = (VectorDrawableCompat.VGroup)var5;
               this.mChildren.add(new VectorDrawableCompat.VGroup(var1, var2));
            } else {
               if(var5 instanceof VectorDrawableCompat.VFullPath) {
                  var5 = new VectorDrawableCompat.VFullPath((VectorDrawableCompat.VFullPath)var5);
               } else {
                  if(!(var5 instanceof VectorDrawableCompat.VClipPath)) {
                     throw new IllegalStateException("Unknown object in the tree!");
                  }

                  var5 = new VectorDrawableCompat.VClipPath((VectorDrawableCompat.VClipPath)var5);
               }

               this.mChildren.add(var5);
               if(((VectorDrawableCompat.VPath)var5).mPathName != null) {
                  var2.put(((VectorDrawableCompat.VPath)var5).mPathName, var5);
               }
            }
         }

      }

      private void updateLocalMatrix() {
         this.mLocalMatrix.reset();
         this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
         this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
         this.mLocalMatrix.postRotate(this.mRotate, 0.0F, 0.0F);
         this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
      }

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) {
         this.mThemeAttrs = null;
         this.mRotate = TypedArrayUtils.getNamedFloat(var1, var2, "rotation", 5, this.mRotate);
         this.mPivotX = var1.getFloat(1, this.mPivotX);
         this.mPivotY = var1.getFloat(2, this.mPivotY);
         this.mScaleX = TypedArrayUtils.getNamedFloat(var1, var2, "scaleX", 3, this.mScaleX);
         this.mScaleY = TypedArrayUtils.getNamedFloat(var1, var2, "scaleY", 4, this.mScaleY);
         this.mTranslateX = TypedArrayUtils.getNamedFloat(var1, var2, "translateX", 6, this.mTranslateX);
         this.mTranslateY = TypedArrayUtils.getNamedFloat(var1, var2, "translateY", 7, this.mTranslateY);
         String var3 = var1.getString(0);
         if(var3 != null) {
            this.mGroupName = var3;
         }

         this.updateLocalMatrix();
      }

      public String getGroupName() {
         return this.mGroupName;
      }

      public Matrix getLocalMatrix() {
         return this.mLocalMatrix;
      }

      public float getPivotX() {
         return this.mPivotX;
      }

      public float getPivotY() {
         return this.mPivotY;
      }

      public float getRotation() {
         return this.mRotate;
      }

      public float getScaleX() {
         return this.mScaleX;
      }

      public float getScaleY() {
         return this.mScaleY;
      }

      public float getTranslateX() {
         return this.mTranslateX;
      }

      public float getTranslateY() {
         return this.mTranslateY;
      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         TypedArray var5 = VectorDrawableCommon.obtainAttributes(var1, var3, var2, AndroidResources.styleable_VectorDrawableGroup);
         this.updateStateFromTypedArray(var5, var4);
         var5.recycle();
      }

      public void setPivotX(float var1) {
         if(var1 != this.mPivotX) {
            this.mPivotX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setPivotY(float var1) {
         if(var1 != this.mPivotY) {
            this.mPivotY = var1;
            this.updateLocalMatrix();
         }

      }

      public void setRotation(float var1) {
         if(var1 != this.mRotate) {
            this.mRotate = var1;
            this.updateLocalMatrix();
         }

      }

      public void setScaleX(float var1) {
         if(var1 != this.mScaleX) {
            this.mScaleX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setScaleY(float var1) {
         if(var1 != this.mScaleY) {
            this.mScaleY = var1;
            this.updateLocalMatrix();
         }

      }

      public void setTranslateX(float var1) {
         if(var1 != this.mTranslateX) {
            this.mTranslateX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setTranslateY(float var1) {
         if(var1 != this.mTranslateY) {
            this.mTranslateY = var1;
            this.updateLocalMatrix();
         }

      }
   }

   private static class VPath {
      int mChangingConfigurations;
      protected PathParser.PathDataNode[] mNodes = null;
      String mPathName;

      public VPath() {
      }

      public VPath(VectorDrawableCompat.VPath var1) {
         this.mPathName = var1.mPathName;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mNodes = PathParser.deepCopyNodes(var1.mNodes);
      }

      public String NodesToString(PathParser.PathDataNode[] var1) {
         String var4 = " ";

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var4 = var4 + var1[var2].type + ":";
            float[] var5 = var1[var2].params;

            for(int var3 = 0; var3 < var5.length; ++var3) {
               var4 = var4 + var5[var3] + ",";
            }
         }

         return var4;
      }

      public void applyTheme(Theme var1) {
      }

      public boolean canApplyTheme() {
         return false;
      }

      public PathParser.PathDataNode[] getPathData() {
         return this.mNodes;
      }

      public String getPathName() {
         return this.mPathName;
      }

      public boolean isClipPath() {
         return false;
      }

      public void printVPath(int var1) {
         String var3 = "";

         for(int var2 = 0; var2 < var1; ++var2) {
            var3 = var3 + "    ";
         }

         Log.v("VectorDrawableCompat", var3 + "current path is :" + this.mPathName + " pathData is " + this.NodesToString(this.mNodes));
      }

      public void setPathData(PathParser.PathDataNode[] var1) {
         if(!PathParser.canMorph(this.mNodes, var1)) {
            this.mNodes = PathParser.deepCopyNodes(var1);
         } else {
            PathParser.updateNodes(this.mNodes, var1);
         }

      }

      public void toPath(Path var1) {
         var1.reset();
         if(this.mNodes != null) {
            PathParser.PathDataNode.nodesToPath(this.mNodes, var1);
         }

      }
   }

   private static class VPathRenderer {
      private static final Matrix IDENTITY_MATRIX = new Matrix();
      float mBaseHeight = 0.0F;
      float mBaseWidth = 0.0F;
      private int mChangingConfigurations;
      private Paint mFillPaint;
      private final Matrix mFinalPathMatrix = new Matrix();
      private final Path mPath;
      private PathMeasure mPathMeasure;
      private final Path mRenderPath;
      int mRootAlpha = 255;
      final VectorDrawableCompat.VGroup mRootGroup;
      String mRootName = null;
      private Paint mStrokePaint;
      final ArrayMap mVGTargetsMap = new ArrayMap();
      float mViewportHeight = 0.0F;
      float mViewportWidth = 0.0F;

      public VPathRenderer() {
         this.mRootGroup = new VectorDrawableCompat.VGroup();
         this.mPath = new Path();
         this.mRenderPath = new Path();
      }

      public VPathRenderer(VectorDrawableCompat.VPathRenderer var1) {
         this.mRootGroup = new VectorDrawableCompat.VGroup(var1.mRootGroup, this.mVGTargetsMap);
         this.mPath = new Path(var1.mPath);
         this.mRenderPath = new Path(var1.mRenderPath);
         this.mBaseWidth = var1.mBaseWidth;
         this.mBaseHeight = var1.mBaseHeight;
         this.mViewportWidth = var1.mViewportWidth;
         this.mViewportHeight = var1.mViewportHeight;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mRootAlpha = var1.mRootAlpha;
         this.mRootName = var1.mRootName;
         if(var1.mRootName != null) {
            this.mVGTargetsMap.put(var1.mRootName, this);
         }

      }

      private static float cross(float var0, float var1, float var2, float var3) {
         return var0 * var3 - var1 * var2;
      }

      private void drawGroupTree(VectorDrawableCompat.VGroup var1, Matrix var2, Canvas var3, int var4, int var5, ColorFilter var6) {
         var1.mStackedMatrix.set(var2);
         var1.mStackedMatrix.preConcat(var1.mLocalMatrix);
         var3.save();

         for(int var7 = 0; var7 < var1.mChildren.size(); ++var7) {
            Object var8 = var1.mChildren.get(var7);
            if(var8 instanceof VectorDrawableCompat.VGroup) {
               this.drawGroupTree((VectorDrawableCompat.VGroup)var8, var1.mStackedMatrix, var3, var4, var5, var6);
            } else if(var8 instanceof VectorDrawableCompat.VPath) {
               this.drawPath(var1, (VectorDrawableCompat.VPath)var8, var3, var4, var5, var6);
            }
         }

         var3.restore();
      }

      private void drawPath(VectorDrawableCompat.VGroup var1, VectorDrawableCompat.VPath var2, Canvas var3, int var4, int var5, ColorFilter var6) {
         float var8 = (float)var4 / this.mViewportWidth;
         float var9 = (float)var5 / this.mViewportHeight;
         float var7 = Math.min(var8, var9);
         Matrix var15 = var1.mStackedMatrix;
         this.mFinalPathMatrix.set(var15);
         this.mFinalPathMatrix.postScale(var8, var9);
         var8 = this.getMatrixScale(var15);
         if(var8 != 0.0F) {
            var2.toPath(this.mPath);
            Path var14 = this.mPath;
            this.mRenderPath.reset();
            if(var2.isClipPath()) {
               this.mRenderPath.addPath(var14, this.mFinalPathMatrix);
               var3.clipPath(this.mRenderPath);
            } else {
               VectorDrawableCompat.VFullPath var16 = (VectorDrawableCompat.VFullPath)var2;
               if(var16.mTrimPathStart != 0.0F || var16.mTrimPathEnd != 1.0F) {
                  float var12 = var16.mTrimPathStart;
                  float var13 = var16.mTrimPathOffset;
                  float var11 = var16.mTrimPathEnd;
                  float var10 = var16.mTrimPathOffset;
                  if(this.mPathMeasure == null) {
                     this.mPathMeasure = new PathMeasure();
                  }

                  this.mPathMeasure.setPath(this.mPath, false);
                  var9 = this.mPathMeasure.getLength();
                  var12 = (var12 + var13) % 1.0F * var9;
                  var10 = (var11 + var10) % 1.0F * var9;
                  var14.reset();
                  if(var12 > var10) {
                     this.mPathMeasure.getSegment(var12, var9, var14, true);
                     this.mPathMeasure.getSegment(0.0F, var10, var14, true);
                  } else {
                     this.mPathMeasure.getSegment(var12, var10, var14, true);
                  }

                  var14.rLineTo(0.0F, 0.0F);
               }

               this.mRenderPath.addPath(var14, this.mFinalPathMatrix);
               Paint var17;
               if(var16.mFillColor != 0) {
                  if(this.mFillPaint == null) {
                     this.mFillPaint = new Paint();
                     this.mFillPaint.setStyle(Style.FILL);
                     this.mFillPaint.setAntiAlias(true);
                  }

                  var17 = this.mFillPaint;
                  var17.setColor(VectorDrawableCompat.applyAlpha(var16.mFillColor, var16.mFillAlpha));
                  var17.setColorFilter(var6);
                  var3.drawPath(this.mRenderPath, var17);
               }

               if(var16.mStrokeColor != 0) {
                  if(this.mStrokePaint == null) {
                     this.mStrokePaint = new Paint();
                     this.mStrokePaint.setStyle(Style.STROKE);
                     this.mStrokePaint.setAntiAlias(true);
                  }

                  var17 = this.mStrokePaint;
                  if(var16.mStrokeLineJoin != null) {
                     var17.setStrokeJoin(var16.mStrokeLineJoin);
                  }

                  if(var16.mStrokeLineCap != null) {
                     var17.setStrokeCap(var16.mStrokeLineCap);
                  }

                  var17.setStrokeMiter(var16.mStrokeMiterlimit);
                  var17.setColor(VectorDrawableCompat.applyAlpha(var16.mStrokeColor, var16.mStrokeAlpha));
                  var17.setColorFilter(var6);
                  var17.setStrokeWidth(var16.mStrokeWidth * var7 * var8);
                  var3.drawPath(this.mRenderPath, var17);
               }
            }
         }

      }

      private float getMatrixScale(Matrix var1) {
         float[] var5 = new float[]{0.0F, 1.0F, 1.0F, 0.0F};
         var1.mapVectors(var5);
         float var2 = (float)Math.hypot((double)var5[0], (double)var5[1]);
         float var4 = (float)Math.hypot((double)var5[2], (double)var5[3]);
         float var3 = cross(var5[0], var5[1], var5[2], var5[3]);
         var4 = Math.max(var2, var4);
         var2 = 0.0F;
         if(var4 > 0.0F) {
            var2 = Math.abs(var3) / var4;
         }

         return var2;
      }

      public void draw(Canvas var1, int var2, int var3, ColorFilter var4) {
         this.drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, var1, var2, var3, var4);
      }

      public float getAlpha() {
         return (float)this.getRootAlpha() / 255.0F;
      }

      public int getRootAlpha() {
         return this.mRootAlpha;
      }

      public void setAlpha(float var1) {
         this.setRootAlpha((int)(255.0F * var1));
      }

      public void setRootAlpha(int var1) {
         this.mRootAlpha = var1;
      }
   }

   private static class VectorDrawableCompatState extends ConstantState {
      boolean mAutoMirrored;
      boolean mCacheDirty;
      boolean mCachedAutoMirrored;
      Bitmap mCachedBitmap;
      int mCachedRootAlpha;
      int[] mCachedThemeAttrs;
      ColorStateList mCachedTint;
      Mode mCachedTintMode;
      int mChangingConfigurations;
      Paint mTempPaint;
      ColorStateList mTint = null;
      Mode mTintMode;
      VectorDrawableCompat.VPathRenderer mVPathRenderer;

      public VectorDrawableCompatState() {
         this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
         this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer();
      }

      public VectorDrawableCompatState(VectorDrawableCompat.VectorDrawableCompatState var1) {
         this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
         if(var1 != null) {
            this.mChangingConfigurations = var1.mChangingConfigurations;
            this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer(var1.mVPathRenderer);
            if(var1.mVPathRenderer.mFillPaint != null) {
               this.mVPathRenderer.mFillPaint = new Paint(var1.mVPathRenderer.mFillPaint);
            }

            if(var1.mVPathRenderer.mStrokePaint != null) {
               this.mVPathRenderer.mStrokePaint = new Paint(var1.mVPathRenderer.mStrokePaint);
            }

            this.mTint = var1.mTint;
            this.mTintMode = var1.mTintMode;
            this.mAutoMirrored = var1.mAutoMirrored;
         }

      }

      public boolean canReuseBitmap(int var1, int var2) {
         boolean var3;
         if(var1 == this.mCachedBitmap.getWidth() && var2 == this.mCachedBitmap.getHeight()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public boolean canReuseCache() {
         boolean var1;
         if(!this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void createCachedBitmapIfNeeded(int var1, int var2) {
         if(this.mCachedBitmap == null || !this.canReuseBitmap(var1, var2)) {
            this.mCachedBitmap = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            this.mCacheDirty = true;
         }

      }

      public void drawCachedBitmapWithRootAlpha(Canvas var1, ColorFilter var2, Rect var3) {
         Paint var4 = this.getPaint(var2);
         var1.drawBitmap(this.mCachedBitmap, (Rect)null, var3, var4);
      }

      public int getChangingConfigurations() {
         return this.mChangingConfigurations;
      }

      public Paint getPaint(ColorFilter var1) {
         Paint var2;
         if(!this.hasTranslucentRoot() && var1 == null) {
            var2 = null;
         } else {
            if(this.mTempPaint == null) {
               this.mTempPaint = new Paint();
               this.mTempPaint.setFilterBitmap(true);
            }

            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(var1);
            var2 = this.mTempPaint;
         }

         return var2;
      }

      public boolean hasTranslucentRoot() {
         boolean var1;
         if(this.mVPathRenderer.getRootAlpha() < 255) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Drawable newDrawable() {
         return new VectorDrawableCompat(this);
      }

      public Drawable newDrawable(Resources var1) {
         return new VectorDrawableCompat(this);
      }

      public void updateCacheStates() {
         this.mCachedTint = this.mTint;
         this.mCachedTintMode = this.mTintMode;
         this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
         this.mCachedAutoMirrored = this.mAutoMirrored;
         this.mCacheDirty = false;
      }

      public void updateCachedBitmap(int var1, int var2) {
         this.mCachedBitmap.eraseColor(0);
         Canvas var3 = new Canvas(this.mCachedBitmap);
         this.mVPathRenderer.draw(var3, var1, var2, (ColorFilter)null);
      }
   }

   private static class VectorDrawableDelegateState extends ConstantState {
      private final ConstantState mDelegateState;

      public VectorDrawableDelegateState(ConstantState var1) {
         this.mDelegateState = var1;
      }

      public boolean canApplyTheme() {
         return this.mDelegateState.canApplyTheme();
      }

      public int getChangingConfigurations() {
         return this.mDelegateState.getChangingConfigurations();
      }

      public Drawable newDrawable() {
         VectorDrawableCompat var1 = new VectorDrawableCompat();
         var1.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable();
         return var1;
      }

      public Drawable newDrawable(Resources var1) {
         VectorDrawableCompat var2 = new VectorDrawableCompat();
         var2.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(var1);
         return var2;
      }

      public Drawable newDrawable(Resources var1, Theme var2) {
         VectorDrawableCompat var3 = new VectorDrawableCompat();
         var3.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(var1, var2);
         return var3;
      }
   }
}
