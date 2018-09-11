package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.IOException;

public abstract class AssetPathFetcher implements DataFetcher {
   private static final String TAG = "AssetUriFetcher";
   private final AssetManager assetManager;
   private final String assetPath;
   private Object data;

   public AssetPathFetcher(AssetManager var1, String var2) {
      this.assetManager = var1;
      this.assetPath = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
      if(this.data != null) {
         try {
            this.close(this.data);
         } catch (IOException var2) {
            if(Log.isLoggable("AssetUriFetcher", 2)) {
               Log.v("AssetUriFetcher", "Failed to close data", var2);
            }
         }
      }

   }

   protected abstract void close(Object var1) throws IOException;

   public String getId() {
      return this.assetPath;
   }

   public Object loadData(Priority var1) throws Exception {
      this.data = this.loadResource(this.assetManager, this.assetPath);
      return this.data;
   }

   protected abstract Object loadResource(AssetManager var1, String var2) throws IOException;
}
