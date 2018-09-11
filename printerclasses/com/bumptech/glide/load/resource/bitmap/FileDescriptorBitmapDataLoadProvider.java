package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.NullEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class FileDescriptorBitmapDataLoadProvider implements DataLoadProvider {
   private final ResourceDecoder cacheDecoder;
   private final BitmapEncoder encoder;
   private final FileDescriptorBitmapDecoder sourceDecoder;
   private final Encoder sourceEncoder;

   public FileDescriptorBitmapDataLoadProvider(BitmapPool var1, DecodeFormat var2) {
      this.cacheDecoder = new FileToStreamDecoder(new StreamBitmapDecoder(var1, var2));
      this.sourceDecoder = new FileDescriptorBitmapDecoder(var1, var2);
      this.encoder = new BitmapEncoder();
      this.sourceEncoder = NullEncoder.get();
   }

   public ResourceDecoder getCacheDecoder() {
      return this.cacheDecoder;
   }

   public ResourceEncoder getEncoder() {
      return this.encoder;
   }

   public ResourceDecoder getSourceDecoder() {
      return this.sourceDecoder;
   }

   public Encoder getSourceEncoder() {
      return this.sourceEncoder;
   }
}
