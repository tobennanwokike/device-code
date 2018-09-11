package com.bumptech.glide.load.resource.bitmap;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import java.io.IOException;
import java.io.InputStream;

public class ImageVideoBitmapDecoder implements ResourceDecoder {
   private static final String TAG = "ImageVideoDecoder";
   private final ResourceDecoder fileDescriptorDecoder;
   private final ResourceDecoder streamDecoder;

   public ImageVideoBitmapDecoder(ResourceDecoder var1, ResourceDecoder var2) {
      this.streamDecoder = var1;
      this.fileDescriptorDecoder = var2;
   }

   public Resource decode(ImageVideoWrapper var1, int var2, int var3) throws IOException {
      Resource var5 = null;
      InputStream var6 = var1.getStream();
      Resource var4 = var5;
      if(var6 != null) {
         try {
            var4 = this.streamDecoder.decode(var6, var2, var3);
         } catch (IOException var7) {
            var4 = var5;
            if(Log.isLoggable("ImageVideoDecoder", 2)) {
               Log.v("ImageVideoDecoder", "Failed to load image from stream, trying FileDescriptor", var7);
               var4 = var5;
            }
         }
      }

      var5 = var4;
      if(var4 == null) {
         ParcelFileDescriptor var8 = var1.getFileDescriptor();
         var5 = var4;
         if(var8 != null) {
            var5 = this.fileDescriptorDecoder.decode(var8, var2, var3);
         }
      }

      return var5;
   }

   public String getId() {
      return "ImageVideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
   }
}
