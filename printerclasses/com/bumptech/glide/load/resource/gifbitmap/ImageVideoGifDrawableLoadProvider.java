package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceEncoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperStreamResourceDecoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class ImageVideoGifDrawableLoadProvider implements DataLoadProvider {
   private final ResourceDecoder cacheDecoder;
   private final ResourceEncoder encoder;
   private final ResourceDecoder sourceDecoder;
   private final Encoder sourceEncoder;

   public ImageVideoGifDrawableLoadProvider(DataLoadProvider var1, DataLoadProvider var2, BitmapPool var3) {
      GifBitmapWrapperResourceDecoder var4 = new GifBitmapWrapperResourceDecoder(var1.getSourceDecoder(), var2.getSourceDecoder(), var3);
      this.cacheDecoder = new FileToStreamDecoder(new GifBitmapWrapperStreamResourceDecoder(var4));
      this.sourceDecoder = var4;
      this.encoder = new GifBitmapWrapperResourceEncoder(var1.getEncoder(), var2.getEncoder());
      this.sourceEncoder = var1.getSourceEncoder();
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
