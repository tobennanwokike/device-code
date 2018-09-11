package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.executor.Prioritized;
import com.bumptech.glide.request.ResourceCallback;

class EngineRunnable implements Runnable, Prioritized {
   private static final String TAG = "EngineRunnable";
   private final DecodeJob decodeJob;
   private volatile boolean isCancelled;
   private final EngineRunnable.EngineRunnableManager manager;
   private final Priority priority;
   private EngineRunnable.Stage stage;

   public EngineRunnable(EngineRunnable.EngineRunnableManager var1, DecodeJob var2, Priority var3) {
      this.manager = var1;
      this.decodeJob = var2;
      this.stage = EngineRunnable.Stage.CACHE;
      this.priority = var3;
   }

   private Resource decode() throws Exception {
      Resource var1;
      if(this.isDecodingFromCache()) {
         var1 = this.decodeFromCache();
      } else {
         var1 = this.decodeFromSource();
      }

      return var1;
   }

   private Resource decodeFromCache() throws Exception {
      Resource var2 = null;

      Resource var1;
      try {
         var1 = this.decodeJob.decodeResultFromCache();
      } catch (Exception var4) {
         var1 = var2;
         if(Log.isLoggable("EngineRunnable", 3)) {
            Log.d("EngineRunnable", "Exception decoding result from cache: " + var4);
            var1 = var2;
         }
      }

      var2 = var1;
      if(var1 == null) {
         var2 = this.decodeJob.decodeSourceFromCache();
      }

      return var2;
   }

   private Resource decodeFromSource() throws Exception {
      return this.decodeJob.decodeFromSource();
   }

   private boolean isDecodingFromCache() {
      boolean var1;
      if(this.stage == EngineRunnable.Stage.CACHE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void onLoadComplete(Resource var1) {
      this.manager.onResourceReady(var1);
   }

   private void onLoadFailed(Exception var1) {
      if(this.isDecodingFromCache()) {
         this.stage = EngineRunnable.Stage.SOURCE;
         this.manager.submitForSource(this);
      } else {
         this.manager.onException(var1);
      }

   }

   public void cancel() {
      this.isCancelled = true;
      this.decodeJob.cancel();
   }

   public int getPriority() {
      return this.priority.ordinal();
   }

   public void run() {
      if(!this.isCancelled) {
         Exception var1 = null;
         Resource var2 = null;

         label27: {
            Resource var3;
            try {
               var3 = this.decode();
            } catch (Exception var4) {
               var1 = var4;
               if(Log.isLoggable("EngineRunnable", 2)) {
                  Log.v("EngineRunnable", "Exception decoding", var4);
               }
               break label27;
            }

            var2 = var3;
         }

         if(this.isCancelled) {
            if(var2 != null) {
               var2.recycle();
            }
         } else if(var2 == null) {
            this.onLoadFailed(var1);
         } else {
            this.onLoadComplete(var2);
         }
      }

   }

   interface EngineRunnableManager extends ResourceCallback {
      void submitForSource(EngineRunnable var1);
   }

   private static enum Stage {
      CACHE,
      SOURCE;
   }
}
