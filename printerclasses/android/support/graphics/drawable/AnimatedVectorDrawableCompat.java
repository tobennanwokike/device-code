package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AndroidResources;
import android.support.graphics.drawable.VectorDrawableCommon;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@TargetApi(21)
public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable {
   private static final String ANIMATED_VECTOR = "animated-vector";
   private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
   private static final String LOGTAG = "AnimatedVDCompat";
   private static final String TARGET = "target";
   private AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState mAnimatedVectorState;
   private ArgbEvaluator mArgbEvaluator;
   AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
   final Callback mCallback;
   private Context mContext;

   AnimatedVectorDrawableCompat() {
      this((Context)null, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(@Nullable Context var1) {
      this(var1, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(@Nullable Context var1, @Nullable AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, @Nullable Resources var3) {
      this.mArgbEvaluator = null;
      this.mCallback = new Callback() {
         public void invalidateDrawable(Drawable var1) {
            AnimatedVectorDrawableCompat.this.invalidateSelf();
         }

         public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
            AnimatedVectorDrawableCompat.this.scheduleSelf(var2, var3);
         }

         public void unscheduleDrawable(Drawable var1, Runnable var2) {
            AnimatedVectorDrawableCompat.this.unscheduleSelf(var2);
         }
      };
      this.mContext = var1;
      if(var2 != null) {
         this.mAnimatedVectorState = var2;
      } else {
         this.mAnimatedVectorState = new AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState(var1, var2, this.mCallback, var3);
      }

   }

   @Nullable
   public static AnimatedVectorDrawableCompat create(@NonNull Context param0, @DrawableRes int param1) {
      // $FF: Couldn't be decompiled
   }

   public static AnimatedVectorDrawableCompat createFromXmlInner(Context var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      AnimatedVectorDrawableCompat var5 = new AnimatedVectorDrawableCompat(var0);
      var5.inflate(var1, var2, var3, var4);
      return var5;
   }

   private boolean isStarted() {
      boolean var4 = false;
      ArrayList var5 = this.mAnimatedVectorState.mAnimators;
      boolean var3;
      if(var5 == null) {
         var3 = var4;
      } else {
         int var2 = var5.size();
         int var1 = 0;

         while(true) {
            var3 = var4;
            if(var1 >= var2) {
               break;
            }

            if(((Animator)var5.get(var1)).isRunning()) {
               var3 = true;
               break;
            }

            ++var1;
         }
      }

      return var3;
   }

   static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      TypedArray var4;
      if(var1 == null) {
         var4 = var0.obtainAttributes(var2, var3);
      } else {
         var4 = var1.obtainStyledAttributes(var2, var3, 0, 0);
      }

      return var4;
   }

   private void setupAnimatorsForTarget(String var1, Animator var2) {
      var2.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(var1));
      if(VERSION.SDK_INT < 21) {
         this.setupColorAnimator(var2);
      }

      if(this.mAnimatedVectorState.mAnimators == null) {
         this.mAnimatedVectorState.mAnimators = new ArrayList();
         this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
      }

      this.mAnimatedVectorState.mAnimators.add(var2);
      this.mAnimatedVectorState.mTargetNameMap.put(var2, var1);
   }

   private void setupColorAnimator(Animator var1) {
      if(var1 instanceof AnimatorSet) {
         ArrayList var3 = ((AnimatorSet)var1).getChildAnimations();
         if(var3 != null) {
            for(int var2 = 0; var2 < var3.size(); ++var2) {
               this.setupColorAnimator((Animator)var3.get(var2));
            }
         }
      }

      if(var1 instanceof ObjectAnimator) {
         ObjectAnimator var5 = (ObjectAnimator)var1;
         String var4 = var5.getPropertyName();
         if("fillColor".equals(var4) || "strokeColor".equals(var4)) {
            if(this.mArgbEvaluator == null) {
               this.mArgbEvaluator = new ArgbEvaluator();
            }

            var5.setEvaluator(this.mArgbEvaluator);
         }
      }

   }

   public void applyTheme(Theme var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.applyTheme(this.mDelegateDrawable, var1);
      }

   }

   public boolean canApplyTheme() {
      boolean var1;
      if(this.mDelegateDrawable != null) {
         var1 = DrawableCompat.canApplyTheme(this.mDelegateDrawable);
      } else {
         var1 = false;
      }

      return var1;
   }

   public void draw(Canvas var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.draw(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.draw(var1);
         if(this.isStarted()) {
            this.invalidateSelf();
         }
      }

   }

   public int getAlpha() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = DrawableCompat.getAlpha(this.mDelegateDrawable);
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.getAlpha();
      }

      return var1;
   }

   public int getChangingConfigurations() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getChangingConfigurations();
      } else {
         var1 = super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
      }

      return var1;
   }

   public ConstantState getConstantState() {
      AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState var1;
      if(this.mDelegateDrawable != null) {
         var1 = new AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
      } else {
         var1 = null;
      }

      return var1;
   }

   public int getIntrinsicHeight() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getIntrinsicHeight();
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
      }

      return var1;
   }

   public int getIntrinsicWidth() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getIntrinsicWidth();
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
      }

      return var1;
   }

   public int getOpacity() {
      int var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.getOpacity();
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.getOpacity();
      }

      return var1;
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      this.inflate(var1, var2, var3, (Theme)null);
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.inflate(this.mDelegateDrawable, var1, var2, var3, var4);
      } else {
         int var5 = var2.getEventType();

         for(int var6 = var2.getDepth(); var5 != 1 && (var2.getDepth() >= var6 + 1 || var5 != 3); var5 = var2.next()) {
            if(var5 == 2) {
               String var7 = var2.getName();
               if("animated-vector".equals(var7)) {
                  TypedArray var8 = obtainAttributes(var1, var4, var3, AndroidResources.styleable_AnimatedVectorDrawable);
                  var5 = var8.getResourceId(0, 0);
                  if(var5 != 0) {
                     VectorDrawableCompat var9 = VectorDrawableCompat.create(var1, var5, var4);
                     var9.setAllowCaching(false);
                     var9.setCallback(this.mCallback);
                     if(this.mAnimatedVectorState.mVectorDrawable != null) {
                        this.mAnimatedVectorState.mVectorDrawable.setCallback((Callback)null);
                     }

                     this.mAnimatedVectorState.mVectorDrawable = var9;
                  }

                  var8.recycle();
               } else if("target".equals(var7)) {
                  TypedArray var10 = var1.obtainAttributes(var3, AndroidResources.styleable_AnimatedVectorDrawableTarget);
                  String var11 = var10.getString(0);
                  var5 = var10.getResourceId(1, 0);
                  if(var5 != 0) {
                     if(this.mContext == null) {
                        throw new IllegalStateException("Context can\'t be null when inflating animators");
                     }

                     this.setupAnimatorsForTarget(var11, AnimatorInflater.loadAnimator(this.mContext, var5));
                  }

                  var10.recycle();
               }
            }
         }
      }

   }

   public boolean isAutoMirrored() {
      boolean var1;
      if(this.mDelegateDrawable != null) {
         var1 = DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
      }

      return var1;
   }

   public boolean isRunning() {
      boolean var3;
      if(this.mDelegateDrawable != null) {
         var3 = ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning();
      } else {
         ArrayList var4 = this.mAnimatedVectorState.mAnimators;
         int var2 = var4.size();
         int var1 = 0;

         while(true) {
            if(var1 >= var2) {
               var3 = false;
               break;
            }

            if(((Animator)var4.get(var1)).isRunning()) {
               var3 = true;
               break;
            }

            ++var1;
         }
      }

      return var3;
   }

   public boolean isStateful() {
      boolean var1;
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.isStateful();
      } else {
         var1 = this.mAnimatedVectorState.mVectorDrawable.isStateful();
      }

      return var1;
   }

   public Drawable mutate() {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.mutate();
         return this;
      } else {
         throw new IllegalStateException("Mutate() is not supported for older platform");
      }
   }

   protected void onBoundsChange(Rect var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setBounds(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setBounds(var1);
      }

   }

   protected boolean onLevelChange(int var1) {
      boolean var2;
      if(this.mDelegateDrawable != null) {
         var2 = this.mDelegateDrawable.setLevel(var1);
      } else {
         var2 = this.mAnimatedVectorState.mVectorDrawable.setLevel(var1);
      }

      return var2;
   }

   protected boolean onStateChange(int[] var1) {
      boolean var2;
      if(this.mDelegateDrawable != null) {
         var2 = this.mDelegateDrawable.setState(var1);
      } else {
         var2 = this.mAnimatedVectorState.mVectorDrawable.setState(var1);
      }

      return var2;
   }

   public void setAlpha(int var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setAlpha(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAlpha(var1);
      }

   }

   public void setAutoMirrored(boolean var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setAutoMirrored(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(var1);
      }

   }

   public void setColorFilter(ColorFilter var1) {
      if(this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setColorFilter(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setColorFilter(var1);
      }

   }

   public void setTint(int var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTint(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTint(var1);
      }

   }

   public void setTintList(ColorStateList var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTintList(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintList(var1);
      }

   }

   public void setTintMode(Mode var1) {
      if(this.mDelegateDrawable != null) {
         DrawableCompat.setTintMode(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintMode(var1);
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      if(this.mDelegateDrawable != null) {
         var1 = this.mDelegateDrawable.setVisible(var1, var2);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setVisible(var1, var2);
         var1 = super.setVisible(var1, var2);
      }

      return var1;
   }

   public void start() {
      if(this.mDelegateDrawable != null) {
         ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
      } else if(!this.isStarted()) {
         ArrayList var3 = this.mAnimatedVectorState.mAnimators;
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ((Animator)var3.get(var1)).start();
         }

         this.invalidateSelf();
      }

   }

   public void stop() {
      if(this.mDelegateDrawable != null) {
         ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
      } else {
         ArrayList var3 = this.mAnimatedVectorState.mAnimators;
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ((Animator)var3.get(var1)).end();
         }
      }

   }

   private static class AnimatedVectorDrawableCompatState extends ConstantState {
      ArrayList mAnimators;
      int mChangingConfigurations;
      ArrayMap mTargetNameMap;
      VectorDrawableCompat mVectorDrawable;

      public AnimatedVectorDrawableCompatState(Context var1, AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, Callback var3, Resources var4) {
         if(var2 != null) {
            this.mChangingConfigurations = var2.mChangingConfigurations;
            if(var2.mVectorDrawable != null) {
               ConstantState var7 = var2.mVectorDrawable.getConstantState();
               if(var4 != null) {
                  this.mVectorDrawable = (VectorDrawableCompat)var7.newDrawable(var4);
               } else {
                  this.mVectorDrawable = (VectorDrawableCompat)var7.newDrawable();
               }

               this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate();
               this.mVectorDrawable.setCallback(var3);
               this.mVectorDrawable.setBounds(var2.mVectorDrawable.getBounds());
               this.mVectorDrawable.setAllowCaching(false);
            }

            if(var2.mAnimators != null) {
               int var6 = var2.mAnimators.size();
               this.mAnimators = new ArrayList(var6);
               this.mTargetNameMap = new ArrayMap(var6);

               for(int var5 = 0; var5 < var6; ++var5) {
                  Animator var9 = (Animator)var2.mAnimators.get(var5);
                  Animator var8 = var9.clone();
                  String var10 = (String)var2.mTargetNameMap.get(var9);
                  var8.setTarget(this.mVectorDrawable.getTargetByName(var10));
                  this.mAnimators.add(var8);
                  this.mTargetNameMap.put(var8, var10);
               }
            }
         }

      }

      public int getChangingConfigurations() {
         return this.mChangingConfigurations;
      }

      public Drawable newDrawable() {
         throw new IllegalStateException("No constant state support for SDK < 24.");
      }

      public Drawable newDrawable(Resources var1) {
         throw new IllegalStateException("No constant state support for SDK < 24.");
      }
   }

   private static class AnimatedVectorDrawableDelegateState extends ConstantState {
      private final ConstantState mDelegateState;

      public AnimatedVectorDrawableDelegateState(ConstantState var1) {
         this.mDelegateState = var1;
      }

      public boolean canApplyTheme() {
         return this.mDelegateState.canApplyTheme();
      }

      public int getChangingConfigurations() {
         return this.mDelegateState.getChangingConfigurations();
      }

      public Drawable newDrawable() {
         AnimatedVectorDrawableCompat var1 = new AnimatedVectorDrawableCompat();
         var1.mDelegateDrawable = this.mDelegateState.newDrawable();
         var1.mDelegateDrawable.setCallback(var1.mCallback);
         return var1;
      }

      public Drawable newDrawable(Resources var1) {
         AnimatedVectorDrawableCompat var2 = new AnimatedVectorDrawableCompat();
         var2.mDelegateDrawable = this.mDelegateState.newDrawable(var1);
         var2.mDelegateDrawable.setCallback(var2.mCallback);
         return var2;
      }

      public Drawable newDrawable(Resources var1, Theme var2) {
         AnimatedVectorDrawableCompat var3 = new AnimatedVectorDrawableCompat();
         var3.mDelegateDrawable = this.mDelegateState.newDrawable(var1, var2);
         var3.mDelegateDrawable.setCallback(var3.mCallback);
         return var3;
      }
   }
}
