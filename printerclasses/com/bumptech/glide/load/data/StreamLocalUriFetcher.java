package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.data.LocalUriFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher {
   public StreamLocalUriFetcher(Context var1, Uri var2) {
      super(var1, var2);
   }

   protected void close(InputStream var1) throws IOException {
      var1.close();
   }

   protected InputStream loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException {
      return var2.openInputStream(var1);
   }
}
