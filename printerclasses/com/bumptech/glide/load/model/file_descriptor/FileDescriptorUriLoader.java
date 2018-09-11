package com.bumptech.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;

public class FileDescriptorUriLoader extends UriLoader implements FileDescriptorModelLoader {
   public FileDescriptorUriLoader(Context var1) {
      this(var1, Glide.buildFileDescriptorModelLoader(GlideUrl.class, var1));
   }

   public FileDescriptorUriLoader(Context var1, ModelLoader var2) {
      super(var1, var2);
   }

   protected DataFetcher getAssetPathFetcher(Context var1, String var2) {
      return new FileDescriptorAssetPathFetcher(var1.getApplicationContext().getAssets(), var2);
   }

   protected DataFetcher getLocalUriFetcher(Context var1, Uri var2) {
      return new FileDescriptorLocalUriFetcher(var1, var2);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new FileDescriptorUriLoader(var1, var2.buildModelLoader(GlideUrl.class, ParcelFileDescriptor.class));
      }

      public void teardown() {
      }
   }
}
