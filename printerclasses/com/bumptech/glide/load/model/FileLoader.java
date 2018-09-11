package com.bumptech.glide.load.model;

import android.net.Uri;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;

public class FileLoader implements ModelLoader {
   private final ModelLoader uriLoader;

   public FileLoader(ModelLoader var1) {
      this.uriLoader = var1;
   }

   public DataFetcher getResourceFetcher(File var1, int var2, int var3) {
      return this.uriLoader.getResourceFetcher(Uri.fromFile(var1), var2, var3);
   }
}
