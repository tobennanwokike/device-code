package com.bumptech.glide;

import android.content.Context;
import android.os.Build.VERSION;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import java.util.concurrent.ExecutorService;

public class GlideBuilder {
   private BitmapPool bitmapPool;
   private final Context context;
   private DecodeFormat decodeFormat;
   private DiskCache.Factory diskCacheFactory;
   private ExecutorService diskCacheService;
   private Engine engine;
   private MemoryCache memoryCache;
   private ExecutorService sourceService;

   public GlideBuilder(Context var1) {
      this.context = var1.getApplicationContext();
   }

   Glide createGlide() {
      if(this.sourceService == null) {
         this.sourceService = new FifoPriorityThreadPoolExecutor(Math.max(1, Runtime.getRuntime().availableProcessors()));
      }

      if(this.diskCacheService == null) {
         this.diskCacheService = new FifoPriorityThreadPoolExecutor(1);
      }

      MemorySizeCalculator var1 = new MemorySizeCalculator(this.context);
      if(this.bitmapPool == null) {
         if(VERSION.SDK_INT >= 11) {
            this.bitmapPool = new LruBitmapPool(var1.getBitmapPoolSize());
         } else {
            this.bitmapPool = new BitmapPoolAdapter();
         }
      }

      if(this.memoryCache == null) {
         this.memoryCache = new LruResourceCache(var1.getMemoryCacheSize());
      }

      if(this.diskCacheFactory == null) {
         this.diskCacheFactory = new InternalCacheDiskCacheFactory(this.context);
      }

      if(this.engine == null) {
         this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheService, this.sourceService);
      }

      if(this.decodeFormat == null) {
         this.decodeFormat = DecodeFormat.DEFAULT;
      }

      return new Glide(this.engine, this.memoryCache, this.bitmapPool, this.context, this.decodeFormat);
   }

   public GlideBuilder setBitmapPool(BitmapPool var1) {
      this.bitmapPool = var1;
      return this;
   }

   public GlideBuilder setDecodeFormat(DecodeFormat var1) {
      this.decodeFormat = var1;
      return this;
   }

   public GlideBuilder setDiskCache(DiskCache.Factory var1) {
      this.diskCacheFactory = var1;
      return this;
   }

   @Deprecated
   public GlideBuilder setDiskCache(final DiskCache var1) {
      return this.setDiskCache(new DiskCache.Factory() {
         public DiskCache build() {
            return var1;
         }
      });
   }

   public GlideBuilder setDiskCacheService(ExecutorService var1) {
      this.diskCacheService = var1;
      return this;
   }

   GlideBuilder setEngine(Engine var1) {
      this.engine = var1;
      return this;
   }

   public GlideBuilder setMemoryCache(MemoryCache var1) {
      this.memoryCache = var1;
      return this;
   }

   public GlideBuilder setResizeService(ExecutorService var1) {
      this.sourceService = var1;
      return this;
   }
}
