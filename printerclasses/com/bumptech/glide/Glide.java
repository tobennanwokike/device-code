package com.bumptech.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorFileLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorResourceLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorStringLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorUriLoader;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamByteArrayLoader;
import com.bumptech.glide.load.model.stream.StreamFileLoader;
import com.bumptech.glide.load.model.stream.StreamResourceLoader;
import com.bumptech.glide.load.model.stream.StreamStringLoader;
import com.bumptech.glide.load.model.stream.StreamUriLoader;
import com.bumptech.glide.load.model.stream.StreamUrlLoader;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.ImageVideoDataLoadProvider;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.file.StreamFileDataLoadProvider;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableLoadProvider;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.bumptech.glide.load.resource.gifbitmap.ImageVideoGifDrawableLoadProvider;
import com.bumptech.glide.load.resource.transcode.GifBitmapWrapperDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.GlideBitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.DataLoadProviderRegistry;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class Glide {
   private static final String TAG = "Glide";
   private static volatile Glide glide;
   private final CenterCrop bitmapCenterCrop;
   private final FitCenter bitmapFitCenter;
   private final BitmapPool bitmapPool;
   private final BitmapPreFiller bitmapPreFiller;
   private final DataLoadProviderRegistry dataLoadProviderRegistry;
   private final DecodeFormat decodeFormat;
   private final GifBitmapWrapperTransformation drawableCenterCrop;
   private final GifBitmapWrapperTransformation drawableFitCenter;
   private final Engine engine;
   private final ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
   private final GenericLoaderFactory loaderFactory;
   private final Handler mainHandler;
   private final MemoryCache memoryCache;
   private final TranscoderRegistry transcoderRegistry = new TranscoderRegistry();

   Glide(Engine var1, MemoryCache var2, BitmapPool var3, Context var4, DecodeFormat var5) {
      this.engine = var1;
      this.bitmapPool = var3;
      this.memoryCache = var2;
      this.decodeFormat = var5;
      this.loaderFactory = new GenericLoaderFactory(var4);
      this.mainHandler = new Handler(Looper.getMainLooper());
      this.bitmapPreFiller = new BitmapPreFiller(var2, var3, var5);
      this.dataLoadProviderRegistry = new DataLoadProviderRegistry();
      StreamBitmapDataLoadProvider var6 = new StreamBitmapDataLoadProvider(var3, var5);
      this.dataLoadProviderRegistry.register(InputStream.class, Bitmap.class, var6);
      FileDescriptorBitmapDataLoadProvider var8 = new FileDescriptorBitmapDataLoadProvider(var3, var5);
      this.dataLoadProviderRegistry.register(ParcelFileDescriptor.class, Bitmap.class, var8);
      ImageVideoDataLoadProvider var7 = new ImageVideoDataLoadProvider(var6, var8);
      this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, Bitmap.class, var7);
      GifDrawableLoadProvider var9 = new GifDrawableLoadProvider(var4, var3);
      this.dataLoadProviderRegistry.register(InputStream.class, GifDrawable.class, var9);
      this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, GifBitmapWrapper.class, new ImageVideoGifDrawableLoadProvider(var7, var9, var3));
      this.dataLoadProviderRegistry.register(InputStream.class, File.class, new StreamFileDataLoadProvider());
      this.register(File.class, ParcelFileDescriptor.class, new FileDescriptorFileLoader.Factory());
      this.register(File.class, InputStream.class, new StreamFileLoader.Factory());
      this.register(Integer.TYPE, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
      this.register(Integer.TYPE, InputStream.class, new StreamResourceLoader.Factory());
      this.register(Integer.class, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
      this.register(Integer.class, InputStream.class, new StreamResourceLoader.Factory());
      this.register(String.class, ParcelFileDescriptor.class, new FileDescriptorStringLoader.Factory());
      this.register(String.class, InputStream.class, new StreamStringLoader.Factory());
      this.register(Uri.class, ParcelFileDescriptor.class, new FileDescriptorUriLoader.Factory());
      this.register(Uri.class, InputStream.class, new StreamUriLoader.Factory());
      this.register(URL.class, InputStream.class, new StreamUrlLoader.Factory());
      this.register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());
      this.register(byte[].class, InputStream.class, new StreamByteArrayLoader.Factory());
      this.transcoderRegistry.register(Bitmap.class, GlideBitmapDrawable.class, new GlideBitmapDrawableTranscoder(var4.getResources(), var3));
      this.transcoderRegistry.register(GifBitmapWrapper.class, GlideDrawable.class, new GifBitmapWrapperDrawableTranscoder(new GlideBitmapDrawableTranscoder(var4.getResources(), var3)));
      this.bitmapCenterCrop = new CenterCrop(var3);
      this.drawableCenterCrop = new GifBitmapWrapperTransformation(var3, this.bitmapCenterCrop);
      this.bitmapFitCenter = new FitCenter(var3);
      this.drawableFitCenter = new GifBitmapWrapperTransformation(var3, this.bitmapFitCenter);
   }

   public static ModelLoader buildFileDescriptorModelLoader(Class var0, Context var1) {
      return buildModelLoader(var0, ParcelFileDescriptor.class, var1);
   }

   public static ModelLoader buildFileDescriptorModelLoader(Object var0, Context var1) {
      return buildModelLoader(var0, ParcelFileDescriptor.class, var1);
   }

   public static ModelLoader buildModelLoader(Class var0, Class var1, Context var2) {
      ModelLoader var3;
      if(var0 == null) {
         if(Log.isLoggable("Glide", 3)) {
            Log.d("Glide", "Unable to load null model, setting placeholder only");
         }

         var3 = null;
      } else {
         var3 = get(var2).getLoaderFactory().buildModelLoader(var0, var1);
      }

      return var3;
   }

   public static ModelLoader buildModelLoader(Object var0, Class var1, Context var2) {
      Class var3;
      if(var0 != null) {
         var3 = var0.getClass();
      } else {
         var3 = null;
      }

      return buildModelLoader(var3, var1, var2);
   }

   public static ModelLoader buildStreamModelLoader(Class var0, Context var1) {
      return buildModelLoader(var0, InputStream.class, var1);
   }

   public static ModelLoader buildStreamModelLoader(Object var0, Context var1) {
      return buildModelLoader(var0, InputStream.class, var1);
   }

   public static void clear(View var0) {
      clear((Target)(new Glide.ClearTarget(var0)));
   }

   public static void clear(FutureTarget var0) {
      var0.clear();
   }

   public static void clear(Target var0) {
      Util.assertMainThread();
      Request var1 = var0.getRequest();
      if(var1 != null) {
         var1.clear();
         var0.setRequest((Request)null);
      }

   }

   public static Glide get(Context param0) {
      // $FF: Couldn't be decompiled
   }

   private GenericLoaderFactory getLoaderFactory() {
      return this.loaderFactory;
   }

   public static File getPhotoCacheDir(Context var0) {
      return getPhotoCacheDir(var0, "image_manager_disk_cache");
   }

   public static File getPhotoCacheDir(Context var0, String var1) {
      File var2 = var0.getCacheDir();
      if(var2 != null) {
         File var3 = new File(var2, var1);
         var2 = var3;
         if(!var3.mkdirs()) {
            if(var3.exists()) {
               var2 = var3;
               if(var3.isDirectory()) {
                  return var2;
               }
            }

            var2 = null;
         }
      } else {
         if(Log.isLoggable("Glide", 6)) {
            Log.e("Glide", "default disk cache dir is null");
         }

         var2 = null;
      }

      return var2;
   }

   @Deprecated
   public static boolean isSetup() {
      boolean var0;
      if(glide != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   @Deprecated
   public static void setup(GlideBuilder var0) {
      if(isSetup()) {
         throw new IllegalArgumentException("Glide is already setup, check with isSetup() first");
      } else {
         glide = var0.createGlide();
      }
   }

   static void tearDown() {
      glide = null;
   }

   public static RequestManager with(Activity var0) {
      return RequestManagerRetriever.get().get(var0);
   }

   @TargetApi(11)
   public static RequestManager with(Fragment var0) {
      return RequestManagerRetriever.get().get(var0);
   }

   public static RequestManager with(Context var0) {
      return RequestManagerRetriever.get().get(var0);
   }

   public static RequestManager with(android.support.v4.app.Fragment var0) {
      return RequestManagerRetriever.get().get(var0);
   }

   public static RequestManager with(FragmentActivity var0) {
      return RequestManagerRetriever.get().get(var0);
   }

   DataLoadProvider buildDataProvider(Class var1, Class var2) {
      return this.dataLoadProviderRegistry.get(var1, var2);
   }

   Target buildImageViewTarget(ImageView var1, Class var2) {
      return this.imageViewTargetFactory.buildTarget(var1, var2);
   }

   ResourceTranscoder buildTranscoder(Class var1, Class var2) {
      return this.transcoderRegistry.get(var1, var2);
   }

   public void clearDiskCache() {
      Util.assertBackgroundThread();
      this.getEngine().clearDiskCache();
   }

   public void clearMemory() {
      Util.assertMainThread();
      this.memoryCache.clearMemory();
      this.bitmapPool.clearMemory();
   }

   CenterCrop getBitmapCenterCrop() {
      return this.bitmapCenterCrop;
   }

   FitCenter getBitmapFitCenter() {
      return this.bitmapFitCenter;
   }

   public BitmapPool getBitmapPool() {
      return this.bitmapPool;
   }

   DecodeFormat getDecodeFormat() {
      return this.decodeFormat;
   }

   GifBitmapWrapperTransformation getDrawableCenterCrop() {
      return this.drawableCenterCrop;
   }

   GifBitmapWrapperTransformation getDrawableFitCenter() {
      return this.drawableFitCenter;
   }

   Engine getEngine() {
      return this.engine;
   }

   Handler getMainHandler() {
      return this.mainHandler;
   }

   public void preFillBitmapPool(PreFillType.Builder... var1) {
      this.bitmapPreFiller.preFill(var1);
   }

   public void register(Class var1, Class var2, ModelLoaderFactory var3) {
      ModelLoaderFactory var4 = this.loaderFactory.register(var1, var2, var3);
      if(var4 != null) {
         var4.teardown();
      }

   }

   public void setMemoryCategory(MemoryCategory var1) {
      Util.assertMainThread();
      this.memoryCache.setSizeMultiplier(var1.getMultiplier());
      this.bitmapPool.setSizeMultiplier(var1.getMultiplier());
   }

   public void trimMemory(int var1) {
      Util.assertMainThread();
      this.memoryCache.trimMemory(var1);
      this.bitmapPool.trimMemory(var1);
   }

   @Deprecated
   public void unregister(Class var1, Class var2) {
      ModelLoaderFactory var3 = this.loaderFactory.unregister(var1, var2);
      if(var3 != null) {
         var3.teardown();
      }

   }

   private static class ClearTarget extends ViewTarget {
      public ClearTarget(View var1) {
         super(var1);
      }

      public void onLoadCleared(Drawable var1) {
      }

      public void onLoadFailed(Exception var1, Drawable var2) {
      }

      public void onLoadStarted(Drawable var1) {
      }

      public void onResourceReady(Object var1, GlideAnimation var2) {
      }
   }
}
