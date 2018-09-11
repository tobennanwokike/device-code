package com.bumptech.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;

public class FileDescriptorResourceLoader extends ResourceLoader implements FileDescriptorModelLoader {
   public FileDescriptorResourceLoader(Context var1) {
      this(var1, Glide.buildFileDescriptorModelLoader(Uri.class, var1));
   }

   public FileDescriptorResourceLoader(Context var1, ModelLoader var2) {
      super(var1, var2);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new FileDescriptorResourceLoader(var1, var2.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
      }

      public void teardown() {
      }
   }
}
