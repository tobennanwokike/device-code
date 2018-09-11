package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import java.io.InputStream;

public class ImageVideoWrapper {
   private final ParcelFileDescriptor fileDescriptor;
   private final InputStream streamData;

   public ImageVideoWrapper(InputStream var1, ParcelFileDescriptor var2) {
      this.streamData = var1;
      this.fileDescriptor = var2;
   }

   public ParcelFileDescriptor getFileDescriptor() {
      return this.fileDescriptor;
   }

   public InputStream getStream() {
      return this.streamData;
   }
}
