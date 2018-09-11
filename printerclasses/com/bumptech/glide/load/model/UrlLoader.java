package com.bumptech.glide.load.model;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import java.net.URL;

public class UrlLoader implements ModelLoader {
   private final ModelLoader glideUrlLoader;

   public UrlLoader(ModelLoader var1) {
      this.glideUrlLoader = var1;
   }

   public DataFetcher getResourceFetcher(URL var1, int var2, int var3) {
      return this.glideUrlLoader.getResourceFetcher(new GlideUrl(var1), var2, var3);
   }
}
