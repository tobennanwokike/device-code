package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.data.LocalUriFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher {
   public FileDescriptorLocalUriFetcher(Context var1, Uri var2) {
      super(var1, var2);
   }

   protected void close(ParcelFileDescriptor var1) throws IOException {
      var1.close();
   }

   protected ParcelFileDescriptor loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException {
      return var2.openAssetFileDescriptor(var1, "r").getParcelFileDescriptor();
   }
}
