package com.bumptech.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.ChildLoadProvider;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.GenericRequest;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.NoAnimation;
import com.bumptech.glide.request.animation.ViewAnimationFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.Util;

public class GenericRequestBuilder implements Cloneable {
   private GlideAnimationFactory animationFactory;
   protected final Context context;
   private DiskCacheStrategy diskCacheStrategy;
   private int errorId;
   private Drawable errorPlaceholder;
   private Drawable fallbackDrawable;
   private int fallbackResource;
   protected final Glide glide;
   private boolean isCacheable;
   private boolean isModelSet;
   private boolean isThumbnailBuilt;
   private boolean isTransformationSet;
   protected final Lifecycle lifecycle;
   private ChildLoadProvider loadProvider;
   private Object model;
   protected final Class modelClass;
   private int overrideHeight;
   private int overrideWidth;
   private Drawable placeholderDrawable;
   private int placeholderId;
   private Priority priority;
   private RequestListener requestListener;
   protected final RequestTracker requestTracker;
   private Key signature;
   private Float sizeMultiplier;
   private Float thumbSizeMultiplier;
   private GenericRequestBuilder thumbnailRequestBuilder;
   protected final Class transcodeClass;
   private Transformation transformation;

   GenericRequestBuilder(Context var1, Class var2, LoadProvider var3, Class var4, Glide var5, RequestTracker var6, Lifecycle var7) {
      Object var8 = null;
      super();
      this.signature = EmptySignature.obtain();
      this.sizeMultiplier = Float.valueOf(1.0F);
      this.priority = null;
      this.isCacheable = true;
      this.animationFactory = NoAnimation.getFactory();
      this.overrideHeight = -1;
      this.overrideWidth = -1;
      this.diskCacheStrategy = DiskCacheStrategy.RESULT;
      this.transformation = UnitTransformation.get();
      this.context = var1;
      this.modelClass = var2;
      this.transcodeClass = var4;
      this.glide = var5;
      this.requestTracker = var6;
      this.lifecycle = var7;
      ChildLoadProvider var9 = (ChildLoadProvider)var8;
      if(var3 != null) {
         var9 = new ChildLoadProvider(var3);
      }

      this.loadProvider = var9;
      if(var1 == null) {
         throw new NullPointerException("Context can\'t be null");
      } else if(var2 != null && var3 == null) {
         throw new NullPointerException("LoadProvider must not be null");
      }
   }

   GenericRequestBuilder(LoadProvider var1, Class var2, GenericRequestBuilder var3) {
      this(var3.context, var3.modelClass, var1, var2, var3.glide, var3.requestTracker, var3.lifecycle);
      this.model = var3.model;
      this.isModelSet = var3.isModelSet;
      this.signature = var3.signature;
      this.diskCacheStrategy = var3.diskCacheStrategy;
      this.isCacheable = var3.isCacheable;
   }

   private Request buildRequest(Target var1) {
      if(this.priority == null) {
         this.priority = Priority.NORMAL;
      }

      return this.buildRequestRecursive(var1, (ThumbnailRequestCoordinator)null);
   }

   private Request buildRequestRecursive(Target var1, ThumbnailRequestCoordinator var2) {
      Object var5;
      if(this.thumbnailRequestBuilder != null) {
         if(this.isThumbnailBuilt) {
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
         }

         if(this.thumbnailRequestBuilder.animationFactory.equals(NoAnimation.getFactory())) {
            this.thumbnailRequestBuilder.animationFactory = this.animationFactory;
         }

         if(this.thumbnailRequestBuilder.priority == null) {
            this.thumbnailRequestBuilder.priority = this.getThumbnailPriority();
         }

         if(Util.isValidDimensions(this.overrideWidth, this.overrideHeight) && !Util.isValidDimensions(this.thumbnailRequestBuilder.overrideWidth, this.thumbnailRequestBuilder.overrideHeight)) {
            this.thumbnailRequestBuilder.override(this.overrideWidth, this.overrideHeight);
         }

         var2 = new ThumbnailRequestCoordinator(var2);
         Request var3 = this.obtainRequest(var1, this.sizeMultiplier.floatValue(), this.priority, var2);
         this.isThumbnailBuilt = true;
         Request var4 = this.thumbnailRequestBuilder.buildRequestRecursive(var1, var2);
         this.isThumbnailBuilt = false;
         var2.setRequests(var3, var4);
         var5 = var2;
      } else if(this.thumbSizeMultiplier != null) {
         var2 = new ThumbnailRequestCoordinator(var2);
         var2.setRequests(this.obtainRequest(var1, this.sizeMultiplier.floatValue(), this.priority, var2), this.obtainRequest(var1, this.thumbSizeMultiplier.floatValue(), this.getThumbnailPriority(), var2));
         var5 = var2;
      } else {
         var5 = this.obtainRequest(var1, this.sizeMultiplier.floatValue(), this.priority, var2);
      }

      return (Request)var5;
   }

   private Priority getThumbnailPriority() {
      Priority var1;
      if(this.priority == Priority.LOW) {
         var1 = Priority.NORMAL;
      } else if(this.priority == Priority.NORMAL) {
         var1 = Priority.HIGH;
      } else {
         var1 = Priority.IMMEDIATE;
      }

      return var1;
   }

   private Request obtainRequest(Target var1, float var2, Priority var3, RequestCoordinator var4) {
      return GenericRequest.obtain(this.loadProvider, this.model, this.signature, this.context, var3, var1, var2, this.placeholderDrawable, this.placeholderId, this.errorPlaceholder, this.errorId, this.fallbackDrawable, this.fallbackResource, this.requestListener, var4, this.glide.getEngine(), this.transformation, this.transcodeClass, this.isCacheable, this.animationFactory, this.overrideWidth, this.overrideHeight, this.diskCacheStrategy);
   }

   public GenericRequestBuilder animate(int var1) {
      return this.animate((GlideAnimationFactory)(new ViewAnimationFactory(this.context, var1)));
   }

   @Deprecated
   public GenericRequestBuilder animate(Animation var1) {
      return this.animate((GlideAnimationFactory)(new ViewAnimationFactory(var1)));
   }

   GenericRequestBuilder animate(GlideAnimationFactory var1) {
      if(var1 == null) {
         throw new NullPointerException("Animation factory must not be null!");
      } else {
         this.animationFactory = var1;
         return this;
      }
   }

   public GenericRequestBuilder animate(ViewPropertyAnimation.Animator var1) {
      return this.animate((GlideAnimationFactory)(new ViewPropertyAnimationFactory(var1)));
   }

   void applyCenterCrop() {
   }

   void applyFitCenter() {
   }

   public GenericRequestBuilder cacheDecoder(ResourceDecoder var1) {
      if(this.loadProvider != null) {
         this.loadProvider.setCacheDecoder(var1);
      }

      return this;
   }

   public GenericRequestBuilder clone() {
      // $FF: Couldn't be decompiled
   }

   public GenericRequestBuilder decoder(ResourceDecoder var1) {
      if(this.loadProvider != null) {
         this.loadProvider.setSourceDecoder(var1);
      }

      return this;
   }

   public GenericRequestBuilder diskCacheStrategy(DiskCacheStrategy var1) {
      this.diskCacheStrategy = var1;
      return this;
   }

   public GenericRequestBuilder dontAnimate() {
      return this.animate(NoAnimation.getFactory());
   }

   public GenericRequestBuilder dontTransform() {
      return this.transform(new Transformation[]{UnitTransformation.get()});
   }

   public GenericRequestBuilder encoder(ResourceEncoder var1) {
      if(this.loadProvider != null) {
         this.loadProvider.setEncoder(var1);
      }

      return this;
   }

   public GenericRequestBuilder error(int var1) {
      this.errorId = var1;
      return this;
   }

   public GenericRequestBuilder error(Drawable var1) {
      this.errorPlaceholder = var1;
      return this;
   }

   public GenericRequestBuilder fallback(int var1) {
      this.fallbackResource = var1;
      return this;
   }

   public GenericRequestBuilder fallback(Drawable var1) {
      this.fallbackDrawable = var1;
      return this;
   }

   public FutureTarget into(int var1, int var2) {
      final RequestFutureTarget var3 = new RequestFutureTarget(this.glide.getMainHandler(), var1, var2);
      this.glide.getMainHandler().post(new Runnable() {
         public void run() {
            if(!var3.isCancelled()) {
               GenericRequestBuilder.this.into((Target)var3);
            }

         }
      });
      return var3;
   }

   public Target into(ImageView var1) {
      Util.assertMainThread();
      if(var1 == null) {
         throw new IllegalArgumentException("You must pass in a non null View");
      } else {
         if(!this.isTransformationSet && var1.getScaleType() != null) {
            switch(null.$SwitchMap$android$widget$ImageView$ScaleType[var1.getScaleType().ordinal()]) {
            case 1:
               this.applyCenterCrop();
               break;
            case 2:
            case 3:
            case 4:
               this.applyFitCenter();
            }
         }

         return this.into(this.glide.buildImageViewTarget(var1, this.transcodeClass));
      }
   }

   public Target into(Target var1) {
      Util.assertMainThread();
      if(var1 == null) {
         throw new IllegalArgumentException("You must pass in a non null Target");
      } else if(!this.isModelSet) {
         throw new IllegalArgumentException("You must first set a model (try #load())");
      } else {
         Request var2 = var1.getRequest();
         if(var2 != null) {
            var2.clear();
            this.requestTracker.removeRequest(var2);
            var2.recycle();
         }

         var2 = this.buildRequest(var1);
         var1.setRequest(var2);
         this.lifecycle.addListener(var1);
         this.requestTracker.runRequest(var2);
         return var1;
      }
   }

   public GenericRequestBuilder listener(RequestListener var1) {
      this.requestListener = var1;
      return this;
   }

   public GenericRequestBuilder load(Object var1) {
      this.model = var1;
      this.isModelSet = true;
      return this;
   }

   public GenericRequestBuilder override(int var1, int var2) {
      if(!Util.isValidDimensions(var1, var2)) {
         throw new IllegalArgumentException("Width and height must be Target#SIZE_ORIGINAL or > 0");
      } else {
         this.overrideWidth = var1;
         this.overrideHeight = var2;
         return this;
      }
   }

   public GenericRequestBuilder placeholder(int var1) {
      this.placeholderId = var1;
      return this;
   }

   public GenericRequestBuilder placeholder(Drawable var1) {
      this.placeholderDrawable = var1;
      return this;
   }

   public Target preload() {
      return this.preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public Target preload(int var1, int var2) {
      return this.into((Target)PreloadTarget.obtain(var1, var2));
   }

   public GenericRequestBuilder priority(Priority var1) {
      this.priority = var1;
      return this;
   }

   public GenericRequestBuilder signature(Key var1) {
      if(var1 == null) {
         throw new NullPointerException("Signature must not be null");
      } else {
         this.signature = var1;
         return this;
      }
   }

   public GenericRequestBuilder sizeMultiplier(float var1) {
      if(var1 >= 0.0F && var1 <= 1.0F) {
         this.sizeMultiplier = Float.valueOf(var1);
         return this;
      } else {
         throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
      }
   }

   public GenericRequestBuilder skipMemoryCache(boolean var1) {
      if(!var1) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.isCacheable = var1;
      return this;
   }

   public GenericRequestBuilder sourceEncoder(Encoder var1) {
      if(this.loadProvider != null) {
         this.loadProvider.setSourceEncoder(var1);
      }

      return this;
   }

   public GenericRequestBuilder thumbnail(float var1) {
      if(var1 >= 0.0F && var1 <= 1.0F) {
         this.thumbSizeMultiplier = Float.valueOf(var1);
         return this;
      } else {
         throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
      }
   }

   public GenericRequestBuilder thumbnail(GenericRequestBuilder var1) {
      if(this.equals(var1)) {
         throw new IllegalArgumentException("You cannot set a request as a thumbnail for itself. Consider using clone() on the request you are passing to thumbnail()");
      } else {
         this.thumbnailRequestBuilder = var1;
         return this;
      }
   }

   public GenericRequestBuilder transcoder(ResourceTranscoder var1) {
      if(this.loadProvider != null) {
         this.loadProvider.setTranscoder(var1);
      }

      return this;
   }

   public GenericRequestBuilder transform(Transformation... var1) {
      this.isTransformationSet = true;
      if(var1.length == 1) {
         this.transformation = var1[0];
      } else {
         this.transformation = new MultiTransformation(var1);
      }

      return this;
   }
}
