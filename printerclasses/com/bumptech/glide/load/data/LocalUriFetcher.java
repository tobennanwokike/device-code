package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LocalUriFetcher implements DataFetcher {
   private static final String TAG = "LocalUriFetcher";
   private final Context context;
   private Object data;
   private final Uri uri;

   public LocalUriFetcher(Context var1, Uri var2) {
      this.context = var1.getApplicationContext();
      this.uri = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
      if(this.data != null) {
         try {
            this.close(this.data);
         } catch (IOException var2) {
            if(Log.isLoggable("LocalUriFetcher", 2)) {
               Log.v("LocalUriFetcher", "failed to close data", var2);
            }
         }
      }

   }

   protected abstract void close(Object var1) throws IOException;

   public String getId() {
      return this.uri.toString();
   }

   public final Object loadData(Priority var1) throws Exception {
      ContentResolver var2 = this.context.getContentResolver();
      this.data = this.loadResource(this.uri, var2);
      return this.data;
   }

   protected abstract Object loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException;
}
