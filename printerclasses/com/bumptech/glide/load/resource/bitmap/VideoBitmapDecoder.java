package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import java.io.IOException;

public class VideoBitmapDecoder implements BitmapDecoder {
   private static final VideoBitmapDecoder.MediaMetadataRetrieverFactory DEFAULT_FACTORY = new VideoBitmapDecoder.MediaMetadataRetrieverFactory();
   private static final int NO_FRAME = -1;
   private VideoBitmapDecoder.MediaMetadataRetrieverFactory factory;
   private int frame;

   public VideoBitmapDecoder() {
      this(DEFAULT_FACTORY, -1);
   }

   public VideoBitmapDecoder(int var1) {
      this(DEFAULT_FACTORY, checkValidFrame(var1));
   }

   VideoBitmapDecoder(VideoBitmapDecoder.MediaMetadataRetrieverFactory var1) {
      this(var1, -1);
   }

   VideoBitmapDecoder(VideoBitmapDecoder.MediaMetadataRetrieverFactory var1, int var2) {
      this.factory = var1;
      this.frame = var2;
   }

   private static int checkValidFrame(int var0) {
      if(var0 < 0) {
         throw new IllegalArgumentException("Requested frame must be non-negative");
      } else {
         return var0;
      }
   }

   public Bitmap decode(ParcelFileDescriptor var1, BitmapPool var2, int var3, int var4, DecodeFormat var5) throws IOException {
      MediaMetadataRetriever var6 = this.factory.build();
      var6.setDataSource(var1.getFileDescriptor());
      Bitmap var7;
      if(this.frame >= 0) {
         var7 = var6.getFrameAtTime((long)this.frame);
      } else {
         var7 = var6.getFrameAtTime();
      }

      var6.release();
      var1.close();
      return var7;
   }

   public String getId() {
      return "VideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
   }

   static class MediaMetadataRetrieverFactory {
      public MediaMetadataRetriever build() {
         return new MediaMetadataRetriever();
      }
   }
}
