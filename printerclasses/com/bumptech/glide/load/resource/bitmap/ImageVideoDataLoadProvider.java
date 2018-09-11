package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class ImageVideoDataLoadProvider implements DataLoadProvider {
   private final ResourceDecoder cacheDecoder;
   private final ResourceEncoder encoder;
   private final ImageVideoBitmapDecoder sourceDecoder;
   private final ImageVideoWrapperEncoder sourceEncoder;

   public ImageVideoDataLoadProvider(DataLoadProvider var1, DataLoadProvider var2) {
      this.encoder = var1.getEncoder();
      this.sourceEncoder = new ImageVideoWrapperEncoder(var1.getSourceEncoder(), var2.getSourceEncoder());
      this.cacheDecoder = var1.getCacheDecoder();
      this.sourceDecoder = new ImageVideoBitmapDecoder(var1.getSourceDecoder(), var2.getSourceDecoder());
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
