package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public final class GenericRequest implements Request, SizeReadyCallback, ResourceCallback {
   private static final Queue REQUEST_POOL = Util.createQueue(0);
   private static final String TAG = "GenericRequest";
   private static final double TO_MEGABYTE = 9.5367431640625E-7D;
   private GlideAnimationFactory animationFactory;
   private Context context;
   private DiskCacheStrategy diskCacheStrategy;
   private Engine engine;
   private Drawable errorDrawable;
   private int errorResourceId;
   private Drawable fallbackDrawable;
   private int fallbackResourceId;
   private boolean isMemoryCacheable;
   private LoadProvider loadProvider;
   private Engine.LoadStatus loadStatus;
   private boolean loadedFromMemoryCache;
   private Object model;
   private int overrideHeight;
   private int overrideWidth;
   private Drawable placeholderDrawable;
   private int placeholderResourceId;
   private Priority priority;
   private RequestCoordinator requestCoordinator;
   private RequestListener requestListener;
   private Resource resource;
   private Key signature;
   private float sizeMultiplier;
   private long startTime;
   private GenericRequest.Status status;
   private final String tag = String.valueOf(this.hashCode());
   private Target target;
   private Class transcodeClass;
   private Transformation transformation;

   private boolean canNotifyStatusChanged() {
      boolean var1;
      if(this.requestCoordinator != null && !this.requestCoordinator.canNotifyStatusChanged(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean canSetResource() {
      boolean var1;
      if(this.requestCoordinator != null && !this.requestCoordinator.canSetImage(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static void check(String var0, Object var1, String var2) {
      if(var1 == null) {
         StringBuilder var3 = new StringBuilder(var0);
         var3.append(" must not be null");
         if(var2 != null) {
            var3.append(", ");
            var3.append(var2);
         }

         throw new NullPointerException(var3.toString());
      }
   }

   private Drawable getErrorDrawable() {
      if(this.errorDrawable == null && this.errorResourceId > 0) {
         this.errorDrawable = this.context.getResources().getDrawable(this.errorResourceId);
      }

      return this.errorDrawable;
   }

   private Drawable getFallbackDrawable() {
      if(this.fallbackDrawable == null && this.fallbackResourceId > 0) {
         this.fallbackDrawable = this.context.getResources().getDrawable(this.fallbackResourceId);
      }

      return this.fallbackDrawable;
   }

   private Drawable getPlaceholderDrawable() {
      if(this.placeholderDrawable == null && this.placeholderResourceId > 0) {
         this.placeholderDrawable = this.context.getResources().getDrawable(this.placeholderResourceId);
      }

      return this.placeholderDrawable;
   }

   private void init(LoadProvider var1, Object var2, Key var3, Context var4, Priority var5, Target var6, float var7, Drawable var8, int var9, Drawable var10, int var11, Drawable var12, int var13, RequestListener var14, RequestCoordinator var15, Engine var16, Transformation var17, Class var18, boolean var19, GlideAnimationFactory var20, int var21, int var22, DiskCacheStrategy var23) {
      this.loadProvider = var1;
      this.model = var2;
      this.signature = var3;
      this.fallbackDrawable = var12;
      this.fallbackResourceId = var13;
      this.context = var4.getApplicationContext();
      this.priority = var5;
      this.target = var6;
      this.sizeMultiplier = var7;
      this.placeholderDrawable = var8;
      this.placeholderResourceId = var9;
      this.errorDrawable = var10;
      this.errorResourceId = var11;
      this.requestListener = var14;
      this.requestCoordinator = var15;
      this.engine = var16;
      this.transformation = var17;
      this.transcodeClass = var18;
      this.isMemoryCacheable = var19;
      this.animationFactory = var20;
      this.overrideWidth = var21;
      this.overrideHeight = var22;
      this.diskCacheStrategy = var23;
      this.status = GenericRequest.Status.PENDING;
      if(var2 != null) {
         check("ModelLoader", var1.getModelLoader(), "try .using(ModelLoader)");
         check("Transcoder", var1.getTranscoder(), "try .as*(Class).transcode(ResourceTranscoder)");
         check("Transformation", var17, "try .transform(UnitTransformation.get())");
         if(var23.cacheSource()) {
            check("SourceEncoder", var1.getSourceEncoder(), "try .sourceEncoder(Encoder) or .diskCacheStrategy(NONE/RESULT)");
         } else {
            check("SourceDecoder", var1.getSourceDecoder(), "try .decoder/.imageDecoder/.videoDecoder(ResourceDecoder) or .diskCacheStrategy(ALL/SOURCE)");
         }

         if(var23.cacheSource() || var23.cacheResult()) {
            check("CacheDecoder", var1.getCacheDecoder(), "try .cacheDecoder(ResouceDecoder) or .diskCacheStrategy(NONE)");
         }

         if(var23.cacheResult()) {
            check("Encoder", var1.getEncoder(), "try .encode(ResourceEncoder) or .diskCacheStrategy(NONE/SOURCE)");
         }
      }

   }

   private boolean isFirstReadyResource() {
      boolean var1;
      if(this.requestCoordinator != null && this.requestCoordinator.isAnyResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void logV(String var1) {
      Log.v("GenericRequest", var1 + " this: " + this.tag);
   }

   private void notifyLoadSuccess() {
      if(this.requestCoordinator != null) {
         this.requestCoordinator.onRequestSuccess(this);
      }

   }

   public static GenericRequest obtain(LoadProvider var0, Object var1, Key var2, Context var3, Priority var4, Target var5, float var6, Drawable var7, int var8, Drawable var9, int var10, Drawable var11, int var12, RequestListener var13, RequestCoordinator var14, Engine var15, Transformation var16, Class var17, boolean var18, GlideAnimationFactory var19, int var20, int var21, DiskCacheStrategy var22) {
      GenericRequest var24 = (GenericRequest)REQUEST_POOL.poll();
      GenericRequest var23 = var24;
      if(var24 == null) {
         var23 = new GenericRequest();
      }

      var23.init(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22);
      return var23;
   }

   private void onResourceReady(Resource var1, Object var2) {
      boolean var3 = this.isFirstReadyResource();
      this.status = GenericRequest.Status.COMPLETE;
      this.resource = var1;
      if(this.requestListener == null || !this.requestListener.onResourceReady(var2, this.model, this.target, this.loadedFromMemoryCache, var3)) {
         GlideAnimation var4 = this.animationFactory.build(this.loadedFromMemoryCache, var3);
         this.target.onResourceReady(var2, var4);
      }

      this.notifyLoadSuccess();
      if(Log.isLoggable("GenericRequest", 2)) {
         this.logV("Resource ready in " + LogTime.getElapsedMillis(this.startTime) + " size: " + (double)var1.getSize() * 9.5367431640625E-7D + " fromCache: " + this.loadedFromMemoryCache);
      }

   }

   private void releaseResource(Resource var1) {
      this.engine.release(var1);
      this.resource = null;
   }

   private void setErrorPlaceholder(Exception var1) {
      if(this.canNotifyStatusChanged()) {
         Drawable var3;
         if(this.model == null) {
            var3 = this.getFallbackDrawable();
         } else {
            var3 = null;
         }

         Drawable var2 = var3;
         if(var3 == null) {
            var2 = this.getErrorDrawable();
         }

         var3 = var2;
         if(var2 == null) {
            var3 = this.getPlaceholderDrawable();
         }

         this.target.onLoadFailed(var1, var3);
      }

   }

   public void begin() {
      this.startTime = LogTime.getLogTime();
      if(this.model == null) {
         this.onException((Exception)null);
      } else {
         this.status = GenericRequest.Status.WAITING_FOR_SIZE;
         if(Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            this.onSizeReady(this.overrideWidth, this.overrideHeight);
         } else {
            this.target.getSize(this);
         }

         if(!this.isComplete() && !this.isFailed() && this.canNotifyStatusChanged()) {
            this.target.onLoadStarted(this.getPlaceholderDrawable());
         }

         if(Log.isLoggable("GenericRequest", 2)) {
            this.logV("finished run method in " + LogTime.getElapsedMillis(this.startTime));
         }
      }

   }

   void cancel() {
      this.status = GenericRequest.Status.CANCELLED;
      if(this.loadStatus != null) {
         this.loadStatus.cancel();
         this.loadStatus = null;
      }

   }

   public void clear() {
      Util.assertMainThread();
      if(this.status != GenericRequest.Status.CLEARED) {
         this.cancel();
         if(this.resource != null) {
            this.releaseResource(this.resource);
         }

         if(this.canNotifyStatusChanged()) {
            this.target.onLoadCleared(this.getPlaceholderDrawable());
         }

         this.status = GenericRequest.Status.CLEARED;
      }

   }

   public boolean isCancelled() {
      boolean var1;
      if(this.status != GenericRequest.Status.CANCELLED && this.status != GenericRequest.Status.CLEARED) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isComplete() {
      boolean var1;
      if(this.status == GenericRequest.Status.COMPLETE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isFailed() {
      boolean var1;
      if(this.status == GenericRequest.Status.FAILED) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isPaused() {
      boolean var1;
      if(this.status == GenericRequest.Status.PAUSED) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isResourceSet() {
      return this.isComplete();
   }

   public boolean isRunning() {
      boolean var1;
      if(this.status != GenericRequest.Status.RUNNING && this.status != GenericRequest.Status.WAITING_FOR_SIZE) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void onException(Exception var1) {
      if(Log.isLoggable("GenericRequest", 3)) {
         Log.d("GenericRequest", "load failed", var1);
      }

      this.status = GenericRequest.Status.FAILED;
      if(this.requestListener == null || !this.requestListener.onException(var1, this.model, this.target, this.isFirstReadyResource())) {
         this.setErrorPlaceholder(var1);
      }

   }

   public void onResourceReady(Resource var1) {
      if(var1 == null) {
         this.onException(new Exception("Expected to receive a Resource<R> with an object of " + this.transcodeClass + " inside, but instead got null."));
      } else {
         Object var3 = var1.get();
         if(var3 != null && this.transcodeClass.isAssignableFrom(var3.getClass())) {
            if(!this.canSetResource()) {
               this.releaseResource(var1);
               this.status = GenericRequest.Status.COMPLETE;
            } else {
               this.onResourceReady(var1, var3);
            }
         } else {
            this.releaseResource(var1);
            StringBuilder var4 = (new StringBuilder()).append("Expected to receive an object of ").append(this.transcodeClass).append(" but instead got ");
            Object var2;
            if(var3 != null) {
               var2 = var3.getClass();
            } else {
               var2 = "";
            }

            StringBuilder var6 = var4.append(var2).append("{").append(var3).append("}").append(" inside Resource{").append(var1).append("}.");
            String var5;
            if(var3 != null) {
               var5 = "";
            } else {
               var5 = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
            }

            this.onException(new Exception(var6.append(var5).toString()));
         }
      }

   }

   public void onSizeReady(int var1, int var2) {
      if(Log.isLoggable("GenericRequest", 2)) {
         this.logV("Got onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
      }

      if(this.status == GenericRequest.Status.WAITING_FOR_SIZE) {
         this.status = GenericRequest.Status.RUNNING;
         var1 = Math.round(this.sizeMultiplier * (float)var1);
         var2 = Math.round(this.sizeMultiplier * (float)var2);
         DataFetcher var5 = this.loadProvider.getModelLoader().getResourceFetcher(this.model, var1, var2);
         if(var5 == null) {
            this.onException(new Exception("Failed to load model: \'" + this.model + "\'"));
         } else {
            ResourceTranscoder var4 = this.loadProvider.getTranscoder();
            if(Log.isLoggable("GenericRequest", 2)) {
               this.logV("finished setup for calling load in " + LogTime.getElapsedMillis(this.startTime));
            }

            this.loadedFromMemoryCache = true;
            this.loadStatus = this.engine.load(this.signature, var1, var2, var5, this.loadProvider, this.transformation, var4, this.priority, this.isMemoryCacheable, this.diskCacheStrategy, this);
            boolean var3;
            if(this.resource != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.loadedFromMemoryCache = var3;
            if(Log.isLoggable("GenericRequest", 2)) {
               this.logV("finished onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
            }
         }
      }

   }

   public void pause() {
      this.clear();
      this.status = GenericRequest.Status.PAUSED;
   }

   public void recycle() {
      this.loadProvider = null;
      this.model = null;
      this.context = null;
      this.target = null;
      this.placeholderDrawable = null;
      this.errorDrawable = null;
      this.fallbackDrawable = null;
      this.requestListener = null;
      this.requestCoordinator = null;
      this.transformation = null;
      this.animationFactory = null;
      this.loadedFromMemoryCache = false;
      this.loadStatus = null;
      REQUEST_POOL.offer(this);
   }

   private static enum Status {
      CANCELLED,
      CLEARED,
      COMPLETE,
      FAILED,
      PAUSED,
      PENDING,
      RUNNING,
      WAITING_FOR_SIZE;
   }
}
