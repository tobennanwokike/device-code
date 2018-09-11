package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.EngineKey;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.util.LogTime;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class DecodeJob {
   private static final DecodeJob.FileOpener DEFAULT_FILE_OPENER = new DecodeJob.FileOpener();
   private static final String TAG = "DecodeJob";
   private final DecodeJob.DiskCacheProvider diskCacheProvider;
   private final DiskCacheStrategy diskCacheStrategy;
   private final DataFetcher fetcher;
   private final DecodeJob.FileOpener fileOpener;
   private final int height;
   private volatile boolean isCancelled;
   private final DataLoadProvider loadProvider;
   private final Priority priority;
   private final EngineKey resultKey;
   private final ResourceTranscoder transcoder;
   private final Transformation transformation;
   private final int width;

   public DecodeJob(EngineKey var1, int var2, int var3, DataFetcher var4, DataLoadProvider var5, Transformation var6, ResourceTranscoder var7, DecodeJob.DiskCacheProvider var8, DiskCacheStrategy var9, Priority var10) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, DEFAULT_FILE_OPENER);
   }

   DecodeJob(EngineKey var1, int var2, int var3, DataFetcher var4, DataLoadProvider var5, Transformation var6, ResourceTranscoder var7, DecodeJob.DiskCacheProvider var8, DiskCacheStrategy var9, Priority var10, DecodeJob.FileOpener var11) {
      this.resultKey = var1;
      this.width = var2;
      this.height = var3;
      this.fetcher = var4;
      this.loadProvider = var5;
      this.transformation = var6;
      this.transcoder = var7;
      this.diskCacheProvider = var8;
      this.diskCacheStrategy = var9;
      this.priority = var10;
      this.fileOpener = var11;
   }

   // $FF: synthetic method
   static DecodeJob.FileOpener access$000(DecodeJob var0) {
      return var0.fileOpener;
   }

   private Resource cacheAndDecodeSourceData(Object var1) throws IOException {
      long var2 = LogTime.getLogTime();
      DecodeJob.SourceWriter var4 = new DecodeJob.SourceWriter(this.loadProvider.getSourceEncoder(), var1);
      this.diskCacheProvider.getDiskCache().put(this.resultKey.getOriginalKey(), var4);
      if(Log.isLoggable("DecodeJob", 2)) {
         this.logWithTimeAndKey("Wrote source to cache", var2);
      }

      var2 = LogTime.getLogTime();
      Resource var5 = this.loadFromCache(this.resultKey.getOriginalKey());
      if(Log.isLoggable("DecodeJob", 2) && var5 != null) {
         this.logWithTimeAndKey("Decoded source from cache", var2);
      }

      return var5;
   }

   private Resource decodeFromSourceData(Object var1) throws IOException {
      Resource var5;
      if(this.diskCacheStrategy.cacheSource()) {
         var5 = this.cacheAndDecodeSourceData(var1);
      } else {
         long var2 = LogTime.getLogTime();
         Resource var4 = this.loadProvider.getSourceDecoder().decode(var1, this.width, this.height);
         var5 = var4;
         if(Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded from source", var2);
            var5 = var4;
         }
      }

      return var5;
   }

   private Resource decodeSource() throws Exception {
      // $FF: Couldn't be decompiled
   }

   private Resource loadFromCache(Key var1) throws IOException {
      File var2 = this.diskCacheProvider.getDiskCache().get(var1);
      Resource var3;
      if(var2 == null) {
         var3 = null;
      } else {
         boolean var5 = false;

         Resource var7;
         try {
            var5 = true;
            var7 = this.loadProvider.getCacheDecoder().decode(var2, this.width, this.height);
            var5 = false;
         } finally {
            if(var5) {
               if(true) {
                  this.diskCacheProvider.getDiskCache().delete(var1);
               }

            }
         }

         var3 = var7;
         if(var7 == null) {
            this.diskCacheProvider.getDiskCache().delete(var1);
            var3 = var7;
         }
      }

      return var3;
   }

   private void logWithTimeAndKey(String var1, long var2) {
      Log.v("DecodeJob", var1 + " in " + LogTime.getElapsedMillis(var2) + ", key: " + this.resultKey);
   }

   private Resource transcode(Resource var1) {
      if(var1 == null) {
         var1 = null;
      } else {
         var1 = this.transcoder.transcode(var1);
      }

      return var1;
   }

   private Resource transform(Resource var1) {
      Resource var2;
      if(var1 == null) {
         var2 = null;
      } else {
         Resource var3 = this.transformation.transform(var1, this.width, this.height);
         var2 = var3;
         if(!var1.equals(var3)) {
            var1.recycle();
            var2 = var3;
         }
      }

      return var2;
   }

   private Resource transformEncodeAndTranscode(Resource var1) {
      long var2 = LogTime.getLogTime();
      var1 = this.transform(var1);
      if(Log.isLoggable("DecodeJob", 2)) {
         this.logWithTimeAndKey("Transformed resource from source", var2);
      }

      this.writeTransformedToCache(var1);
      var2 = LogTime.getLogTime();
      var1 = this.transcode(var1);
      if(Log.isLoggable("DecodeJob", 2)) {
         this.logWithTimeAndKey("Transcoded transformed from source", var2);
      }

      return var1;
   }

   private void writeTransformedToCache(Resource var1) {
      if(var1 != null && this.diskCacheStrategy.cacheResult()) {
         long var2 = LogTime.getLogTime();
         DecodeJob.SourceWriter var4 = new DecodeJob.SourceWriter(this.loadProvider.getEncoder(), var1);
         this.diskCacheProvider.getDiskCache().put(this.resultKey, var4);
         if(Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Wrote transformed from source to cache", var2);
         }
      }

   }

   public void cancel() {
      this.isCancelled = true;
      this.fetcher.cancel();
   }

   public Resource decodeFromSource() throws Exception {
      return this.transformEncodeAndTranscode(this.decodeSource());
   }

   public Resource decodeResultFromCache() throws Exception {
      Resource var3;
      if(!this.diskCacheStrategy.cacheResult()) {
         var3 = null;
      } else {
         long var1 = LogTime.getLogTime();
         var3 = this.loadFromCache(this.resultKey);
         if(Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded transformed from cache", var1);
         }

         var1 = LogTime.getLogTime();
         Resource var4 = this.transcode(var3);
         var3 = var4;
         if(Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Transcoded transformed from cache", var1);
            var3 = var4;
         }
      }

      return var3;
   }

   public Resource decodeSourceFromCache() throws Exception {
      Resource var3;
      if(!this.diskCacheStrategy.cacheSource()) {
         var3 = null;
      } else {
         long var1 = LogTime.getLogTime();
         var3 = this.loadFromCache(this.resultKey.getOriginalKey());
         if(Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded source from cache", var1);
         }

         var3 = this.transformEncodeAndTranscode(var3);
      }

      return var3;
   }

   interface DiskCacheProvider {
      DiskCache getDiskCache();
   }

   static class FileOpener {
      public OutputStream open(File var1) throws FileNotFoundException {
         return new BufferedOutputStream(new FileOutputStream(var1));
      }
   }

   class SourceWriter implements DiskCache.Writer {
      private final Object data;
      private final Encoder encoder;

      public SourceWriter(Encoder var2, Object var3) {
         this.encoder = var2;
         this.data = var3;
      }

      public boolean write(File param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
