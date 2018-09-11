package com.bumptech.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;

public class FileDescriptorFileLoader extends FileLoader implements FileDescriptorModelLoader {
   public FileDescriptorFileLoader(Context var1) {
      this(Glide.buildFileDescriptorModelLoader(Uri.class, var1));
   }

   public FileDescriptorFileLoader(ModelLoader var1) {
      super(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new FileDescriptorFileLoader(var2.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
      }

      public void teardown() {
      }
   }
}
