package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import java.io.IOException;

public class FileDescriptorBitmapDecoder implements ResourceDecoder {
   private final VideoBitmapDecoder bitmapDecoder;
   private final BitmapPool bitmapPool;
   private DecodeFormat decodeFormat;

   public FileDescriptorBitmapDecoder(Context var1) {
      this(Glide.get(var1).getBitmapPool(), DecodeFormat.DEFAULT);
   }

   public FileDescriptorBitmapDecoder(Context var1, DecodeFormat var2) {
      this(Glide.get(var1).getBitmapPool(), var2);
   }

   public FileDescriptorBitmapDecoder(BitmapPool var1, DecodeFormat var2) {
      this(new VideoBitmapDecoder(), var1, var2);
   }

   public FileDescriptorBitmapDecoder(VideoBitmapDecoder var1, BitmapPool var2, DecodeFormat var3) {
      this.bitmapDecoder = var1;
      this.bitmapPool = var2;
      this.decodeFormat = var3;
   }

   public Resource decode(ParcelFileDescriptor var1, int var2, int var3) throws IOException {
      return BitmapResource.obtain(this.bitmapDecoder.decode(var1, this.bitmapPool, var2, var3, this.decodeFormat), this.bitmapPool);
   }

   public String getId() {
      return "FileDescriptorBitmapDecoder.com.bumptech.glide.load.data.bitmap";
   }
}
