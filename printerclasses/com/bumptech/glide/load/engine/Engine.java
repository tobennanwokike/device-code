package com.bumptech.glide.load.engine;

import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.EngineJob;
import com.bumptech.glide.load.engine.EngineJobListener;
import com.bumptech.glide.load.engine.EngineKey;
import com.bumptech.glide.load.engine.EngineKeyFactory;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.EngineRunnable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.ResourceRecycler;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
   private static final String TAG = "Engine";
   private final Map activeResources;
   private final MemoryCache cache;
   private final Engine.LazyDiskCacheProvider diskCacheProvider;
   private final Engine.EngineJobFactory engineJobFactory;
   private final Map jobs;
   private final EngineKeyFactory keyFactory;
   private final ResourceRecycler resourceRecycler;
   private ReferenceQueue resourceReferenceQueue;

   public Engine(MemoryCache var1, DiskCache.Factory var2, ExecutorService var3, ExecutorService var4) {
      this(var1, var2, var3, var4, (Map)null, (EngineKeyFactory)null, (Map)null, (Engine.EngineJobFactory)null, (ResourceRecycler)null);
   }

   Engine(MemoryCache var1, DiskCache.Factory var2, ExecutorService var3, ExecutorService var4, Map var5, EngineKeyFactory var6, Map var7, Engine.EngineJobFactory var8, ResourceRecycler var9) {
      this.cache = var1;
      this.diskCacheProvider = new Engine.LazyDiskCacheProvider(var2);
      Object var10 = var7;
      if(var7 == null) {
         var10 = new HashMap();
      }

      this.activeResources = (Map)var10;
      EngineKeyFactory var11 = var6;
      if(var6 == null) {
         var11 = new EngineKeyFactory();
      }

      this.keyFactory = var11;
      var10 = var5;
      if(var5 == null) {
         var10 = new HashMap();
      }

      this.jobs = (Map)var10;
      Engine.EngineJobFactory var12 = var8;
      if(var8 == null) {
         var12 = new Engine.EngineJobFactory(var3, var4, this);
      }

      this.engineJobFactory = var12;
      ResourceRecycler var13 = var9;
      if(var9 == null) {
         var13 = new ResourceRecycler();
      }

      this.resourceRecycler = var13;
      var1.setResourceRemovedListener(this);
   }

   private EngineResource getEngineResourceFromCache(Key var1) {
      Resource var2 = this.cache.remove(var1);
      EngineResource var3;
      if(var2 == null) {
         var3 = null;
      } else if(var2 instanceof EngineResource) {
         var3 = (EngineResource)var2;
      } else {
         var3 = new EngineResource(var2, true);
      }

      return var3;
   }

   private ReferenceQueue getReferenceQueue() {
      if(this.resourceReferenceQueue == null) {
         this.resourceReferenceQueue = new ReferenceQueue();
         Looper.myQueue().addIdleHandler(new Engine.RefQueueIdleHandler(this.activeResources, this.resourceReferenceQueue));
      }

      return this.resourceReferenceQueue;
   }

   private EngineResource loadFromActiveResources(Key var1, boolean var2) {
      EngineResource var3;
      if(!var2) {
         var3 = null;
      } else {
         var3 = null;
         WeakReference var4 = (WeakReference)this.activeResources.get(var1);
         if(var4 != null) {
            var3 = (EngineResource)var4.get();
            if(var3 != null) {
               var3.acquire();
            } else {
               this.activeResources.remove(var1);
            }
         }
      }

      return var3;
   }

   private EngineResource loadFromCache(Key var1, boolean var2) {
      EngineResource var3;
      if(!var2) {
         var3 = null;
      } else {
         EngineResource var4 = this.getEngineResourceFromCache(var1);
         var3 = var4;
         if(var4 != null) {
            var4.acquire();
            this.activeResources.put(var1, new Engine.ResourceWeakReference(var1, var4, this.getReferenceQueue()));
            var3 = var4;
         }
      }

      return var3;
   }

   private static void logWithTimeAndKey(String var0, long var1, Key var3) {
      Log.v("Engine", var0 + " in " + LogTime.getElapsedMillis(var1) + "ms, key: " + var3);
   }

   public void clearDiskCache() {
      this.diskCacheProvider.getDiskCache().clear();
   }

   public Engine.LoadStatus load(Key var1, int var2, int var3, DataFetcher var4, DataLoadProvider var5, Transformation var6, ResourceTranscoder var7, Priority var8, boolean var9, DiskCacheStrategy var10, ResourceCallback var11) {
      Util.assertMainThread();
      long var12 = LogTime.getLogTime();
      String var14 = var4.getId();
      EngineKey var15 = this.keyFactory.buildKey(var14, var1, var2, var3, var5.getCacheDecoder(), var5.getSourceDecoder(), var6, var5.getEncoder(), var7, var5.getSourceEncoder());
      EngineResource var18 = this.loadFromCache(var15, var9);
      Engine.LoadStatus var16;
      if(var18 != null) {
         var11.onResourceReady(var18);
         if(Log.isLoggable("Engine", 2)) {
            logWithTimeAndKey("Loaded resource from cache", var12, var15);
         }

         var16 = null;
      } else {
         var18 = this.loadFromActiveResources(var15, var9);
         if(var18 != null) {
            var11.onResourceReady(var18);
            if(Log.isLoggable("Engine", 2)) {
               logWithTimeAndKey("Loaded resource from active resources", var12, var15);
            }

            var16 = null;
         } else {
            EngineJob var19 = (EngineJob)this.jobs.get(var15);
            if(var19 != null) {
               var19.addCallback(var11);
               if(Log.isLoggable("Engine", 2)) {
                  logWithTimeAndKey("Added to existing load", var12, var15);
               }

               var16 = new Engine.LoadStatus(var11, var19);
            } else {
               var19 = this.engineJobFactory.build(var15, var9);
               EngineRunnable var17 = new EngineRunnable(var19, new DecodeJob(var15, var2, var3, var4, var5, var6, var7, this.diskCacheProvider, var10, var8), var8);
               this.jobs.put(var15, var19);
               var19.addCallback(var11);
               var19.start(var17);
               if(Log.isLoggable("Engine", 2)) {
                  logWithTimeAndKey("Started new load", var12, var15);
               }

               var16 = new Engine.LoadStatus(var11, var19);
            }
         }
      }

      return var16;
   }

   public void onEngineJobCancelled(EngineJob var1, Key var2) {
      Util.assertMainThread();
      if(var1.equals((EngineJob)this.jobs.get(var2))) {
         this.jobs.remove(var2);
      }

   }

   public void onEngineJobComplete(Key var1, EngineResource var2) {
      Util.assertMainThread();
      if(var2 != null) {
         var2.setResourceListener(var1, this);
         if(var2.isCacheable()) {
            this.activeResources.put(var1, new Engine.ResourceWeakReference(var1, var2, this.getReferenceQueue()));
         }
      }

      this.jobs.remove(var1);
   }

   public void onResourceReleased(Key var1, EngineResource var2) {
      Util.assertMainThread();
      this.activeResources.remove(var1);
      if(var2.isCacheable()) {
         this.cache.put(var1, var2);
      } else {
         this.resourceRecycler.recycle(var2);
      }

   }

   public void onResourceRemoved(Resource var1) {
      Util.assertMainThread();
      this.resourceRecycler.recycle(var1);
   }

   public void release(Resource var1) {
      Util.assertMainThread();
      if(var1 instanceof EngineResource) {
         ((EngineResource)var1).release();
      } else {
         throw new IllegalArgumentException("Cannot release anything but an EngineResource");
      }
   }

   static class EngineJobFactory {
      private final ExecutorService diskCacheService;
      private final EngineJobListener listener;
      private final ExecutorService sourceService;

      public EngineJobFactory(ExecutorService var1, ExecutorService var2, EngineJobListener var3) {
         this.diskCacheService = var1;
         this.sourceService = var2;
         this.listener = var3;
      }

      public EngineJob build(Key var1, boolean var2) {
         return new EngineJob(var1, this.diskCacheService, this.sourceService, var2, this.listener);
      }
   }

   private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
      private volatile DiskCache diskCache;
      private final DiskCache.Factory factory;

      public LazyDiskCacheProvider(DiskCache.Factory var1) {
         this.factory = var1;
      }

      public DiskCache getDiskCache() {
         // $FF: Couldn't be decompiled
      }
   }

   public static class LoadStatus {
      private final ResourceCallback cb;
      private final EngineJob engineJob;

      public LoadStatus(ResourceCallback var1, EngineJob var2) {
         this.cb = var1;
         this.engineJob = var2;
      }

      public void cancel() {
         this.engineJob.removeCallback(this.cb);
      }
   }

   private static class RefQueueIdleHandler implements IdleHandler {
      private final Map activeResources;
      private final ReferenceQueue queue;

      public RefQueueIdleHandler(Map var1, ReferenceQueue var2) {
         this.activeResources = var1;
         this.queue = var2;
      }

      public boolean queueIdle() {
         Engine.ResourceWeakReference var1 = (Engine.ResourceWeakReference)this.queue.poll();
         if(var1 != null) {
            this.activeResources.remove(var1.key);
         }

         return true;
      }
   }

   private static class ResourceWeakReference extends WeakReference {
      private final Key key;

      public ResourceWeakReference(Key var1, EngineResource var2, ReferenceQueue var3) {
         super(var2, var3);
         this.key = var1;
      }
   }
}
