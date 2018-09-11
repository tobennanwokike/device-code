package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class StreamBitmapDataLoadProvider implements DataLoadProvider {
   private final FileToStreamDecoder cacheDecoder;
   private final StreamBitmapDecoder decoder;
   private final BitmapEncoder encoder;
   private final StreamEncoder sourceEncoder = new StreamEncoder();

   public StreamBitmapDataLoadProvider(BitmapPool var1, DecodeFormat var2) {
      this.decoder = new StreamBitmapDecoder(var1, var2);
      this.encoder = new BitmapEncoder();
      this.cacheDecoder = new FileToStreamDecoder(this.decoder);
   }

   public ResourceDecoder getCacheDecoder() {
      return this.cacheDecoder;
   }

   public ResourceEncoder getEncoder() {
      return this.encoder;
   }

   public ResourceDecoder getSourceDecoder() {
      return this.decoder;
   }

   public Encoder getSourceEncoder() {
      return this.sourceEncoder;
   }
}
