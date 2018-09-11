package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.EngineJobListener;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.EngineRunnable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class EngineJob implements EngineRunnable.EngineRunnableManager {
   private static final EngineJob.EngineResourceFactory DEFAULT_FACTORY = new EngineJob.EngineResourceFactory();
   private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new EngineJob.MainThreadCallback());
   private static final int MSG_COMPLETE = 1;
   private static final int MSG_EXCEPTION = 2;
   private final List cbs;
   private final ExecutorService diskCacheService;
   private EngineResource engineResource;
   private final EngineJob.EngineResourceFactory engineResourceFactory;
   private EngineRunnable engineRunnable;
   private Exception exception;
   private volatile Future future;
   private boolean hasException;
   private boolean hasResource;
   private Set ignoredCallbacks;
   private final boolean isCacheable;
   private boolean isCancelled;
   private final Key key;
   private final EngineJobListener listener;
   private Resource resource;
   private final ExecutorService sourceService;

   public EngineJob(Key var1, ExecutorService var2, ExecutorService var3, boolean var4, EngineJobListener var5) {
      this(var1, var2, var3, var4, var5, DEFAULT_FACTORY);
   }

   public EngineJob(Key var1, ExecutorService var2, ExecutorService var3, boolean var4, EngineJobListener var5, EngineJob.EngineResourceFactory var6) {
      this.cbs = new ArrayList();
      this.key = var1;
      this.diskCacheService = var2;
      this.sourceService = var3;
      this.isCacheable = var4;
      this.listener = var5;
      this.engineResourceFactory = var6;
   }

   private void addIgnoredCallback(ResourceCallback var1) {
      if(this.ignoredCallbacks == null) {
         this.ignoredCallbacks = new HashSet();
      }

      this.ignoredCallbacks.add(var1);
   }

   private void handleExceptionOnMainThread() {
      if(!this.isCancelled) {
         if(this.cbs.isEmpty()) {
            throw new IllegalStateException("Received an exception without any callbacks to notify");
         }

         this.hasException = true;
         this.listener.onEngineJobComplete(this.key, (EngineResource)null);
         Iterator var2 = this.cbs.iterator();

         while(var2.hasNext()) {
            ResourceCallback var1 = (ResourceCallback)var2.next();
            if(!this.isInIgnoredCallbacks(var1)) {
               var1.onException(this.exception);
            }
         }
      }

   }

   private void handleResultOnMainThread() {
      if(this.isCancelled) {
         this.resource.recycle();
      } else {
         if(this.cbs.isEmpty()) {
            throw new IllegalStateException("Received a resource without any callbacks to notify");
         }

         this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
         this.hasResource = true;
         this.engineResource.acquire();
         this.listener.onEngineJobComplete(this.key, this.engineResource);
         Iterator var2 = this.cbs.iterator();

         while(var2.hasNext()) {
            ResourceCallback var1 = (ResourceCallback)var2.next();
            if(!this.isInIgnoredCallbacks(var1)) {
               this.engineResource.acquire();
               var1.onResourceReady(this.engineResource);
            }
         }

         this.engineResource.release();
      }

   }

   private boolean isInIgnoredCallbacks(ResourceCallback var1) {
      boolean var2;
      if(this.ignoredCallbacks != null && this.ignoredCallbacks.contains(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void addCallback(ResourceCallback var1) {
      Util.assertMainThread();
      if(this.hasResource) {
         var1.onResourceReady(this.engineResource);
      } else if(this.hasException) {
         var1.onException(this.exception);
      } else {
         this.cbs.add(var1);
      }

   }

   void cancel() {
      if(!this.hasException && !this.hasResource && !this.isCancelled) {
         this.engineRunnable.cancel();
         Future var1 = this.future;
         if(var1 != null) {
            var1.cancel(true);
         }

         this.isCancelled = true;
         this.listener.onEngineJobCancelled(this, this.key);
      }

   }

   boolean isCancelled() {
      return this.isCancelled;
   }

   public void onException(Exception var1) {
      this.exception = var1;
      MAIN_THREAD_HANDLER.obtainMessage(2, this).sendToTarget();
   }

   public void onResourceReady(Resource var1) {
      this.resource = var1;
      MAIN_THREAD_HANDLER.obtainMessage(1, this).sendToTarget();
   }

   public void removeCallback(ResourceCallback var1) {
      Util.assertMainThread();
      if(!this.hasResource && !this.hasException) {
         this.cbs.remove(var1);
         if(this.cbs.isEmpty()) {
            this.cancel();
         }
      } else {
         this.addIgnoredCallback(var1);
      }

   }

   public void start(EngineRunnable var1) {
      this.engineRunnable = var1;
      this.future = this.diskCacheService.submit(var1);
   }

   public void submitForSource(EngineRunnable var1) {
      this.future = this.sourceService.submit(var1);
   }

   static class EngineResourceFactory {
      public EngineResource build(Resource var1, boolean var2) {
         return new EngineResource(var1, var2);
      }
   }

   private static class MainThreadCallback implements Callback {
      private MainThreadCallback() {
      }

      // $FF: synthetic method
      MainThreadCallback(Object var1) {
         this();
      }

      public boolean handleMessage(Message var1) {
         boolean var2 = true;
         if(1 != var1.what && 2 != var1.what) {
            var2 = false;
         } else {
            EngineJob var3 = (EngineJob)var1.obj;
            if(1 == var1.what) {
               var3.handleResultOnMainThread();
            } else {
               var3.handleExceptionOnMainThread();
            }
         }

         return var2;
      }
   }
}
