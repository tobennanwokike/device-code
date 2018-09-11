package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.MediaStoreThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;

public class MediaStoreStreamLoader implements ModelLoader {
   private final Context context;
   private final ModelLoader uriLoader;

   public MediaStoreStreamLoader(Context var1, ModelLoader var2) {
      this.context = var1;
      this.uriLoader = var2;
   }

   public DataFetcher getResourceFetcher(Uri var1, int var2, int var3) {
      return new MediaStoreThumbFetcher(this.context, var1, this.uriLoader.getResourceFetcher(var1, var2, var3), var2, var3);
   }
}
