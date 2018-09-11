package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;

public class ImageVideoModelLoader implements ModelLoader {
   private static final String TAG = "IVML";
   private final ModelLoader fileDescriptorLoader;
   private final ModelLoader streamLoader;

   public ImageVideoModelLoader(ModelLoader var1, ModelLoader var2) {
      if(var1 == null && var2 == null) {
         throw new NullPointerException("At least one of streamLoader and fileDescriptorLoader must be non null");
      } else {
         this.streamLoader = var1;
         this.fileDescriptorLoader = var2;
      }
   }

   public DataFetcher getResourceFetcher(Object var1, int var2, int var3) {
      DataFetcher var4 = null;
      if(this.streamLoader != null) {
         var4 = this.streamLoader.getResourceFetcher(var1, var2, var3);
      }

      DataFetcher var5 = null;
      if(this.fileDescriptorLoader != null) {
         var5 = this.fileDescriptorLoader.getResourceFetcher(var1, var2, var3);
      }

      ImageVideoModelLoader.ImageVideoFetcher var6;
      if(var4 == null && var5 == null) {
         var6 = null;
      } else {
         var6 = new ImageVideoModelLoader.ImageVideoFetcher(var4, var5);
      }

      return var6;
   }

   static class ImageVideoFetcher implements DataFetcher {
      private final DataFetcher fileDescriptorFetcher;
      private final DataFetcher streamFetcher;

      public ImageVideoFetcher(DataFetcher var1, DataFetcher var2) {
         this.streamFetcher = var1;
         this.fileDescriptorFetcher = var2;
      }

      public void cancel() {
         if(this.streamFetcher != null) {
            this.streamFetcher.cancel();
         }

         if(this.fileDescriptorFetcher != null) {
            this.fileDescriptorFetcher.cancel();
         }

      }

      public void cleanup() {
         if(this.streamFetcher != null) {
            this.streamFetcher.cleanup();
         }

         if(this.fileDescriptorFetcher != null) {
            this.fileDescriptorFetcher.cleanup();
         }

      }

      public String getId() {
         String var1;
         if(this.streamFetcher != null) {
            var1 = this.streamFetcher.getId();
         } else {
            var1 = this.fileDescriptorFetcher.getId();
         }

         return var1;
      }

      public ImageVideoWrapper loadData(Priority var1) throws Exception {
         ParcelFileDescriptor var3 = null;
         InputStream var2 = var3;
         if(this.streamFetcher != null) {
            try {
               var2 = (InputStream)this.streamFetcher.loadData(var1);
            } catch (Exception var6) {
               if(Log.isLoggable("IVML", 2)) {
                  Log.v("IVML", "Exception fetching input stream, trying ParcelFileDescriptor", var6);
               }

               var2 = var3;
               if(this.fileDescriptorFetcher == null) {
                  throw var6;
               }
            }
         }

         Object var4 = null;
         var3 = (ParcelFileDescriptor)var4;
         if(this.fileDescriptorFetcher != null) {
            try {
               var3 = (ParcelFileDescriptor)this.fileDescriptorFetcher.loadData(var1);
            } catch (Exception var5) {
               if(Log.isLoggable("IVML", 2)) {
                  Log.v("IVML", "Exception fetching ParcelFileDescriptor", var5);
               }

               var3 = (ParcelFileDescriptor)var4;
               if(var2 == null) {
                  throw var5;
               }
            }
         }

         return new ImageVideoWrapper(var2, var3);
      }
   }
}
