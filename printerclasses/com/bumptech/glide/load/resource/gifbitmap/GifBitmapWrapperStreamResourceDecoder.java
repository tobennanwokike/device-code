package com.bumptech.glide.load.resource.gifbitmap;

import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import java.io.IOException;
import java.io.InputStream;

public class GifBitmapWrapperStreamResourceDecoder implements ResourceDecoder {
   private final ResourceDecoder gifBitmapDecoder;

   public GifBitmapWrapperStreamResourceDecoder(ResourceDecoder var1) {
      this.gifBitmapDecoder = var1;
   }

   public Resource decode(InputStream var1, int var2, int var3) throws IOException {
      return this.gifBitmapDecoder.decode(new ImageVideoWrapper(var1, (ParcelFileDescriptor)null), var2, var3);
   }

   public String getId() {
      return this.gifBitmapDecoder.getId();
   }
}
