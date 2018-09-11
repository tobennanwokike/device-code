package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.gif.GifResourceDecoder;
import com.bumptech.glide.load.resource.gif.GifResourceEncoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class GifDrawableLoadProvider implements DataLoadProvider {
   private final FileToStreamDecoder cacheDecoder;
   private final GifResourceDecoder decoder;
   private final GifResourceEncoder encoder;
   private final StreamEncoder sourceEncoder;

   public GifDrawableLoadProvider(Context var1, BitmapPool var2) {
      this.decoder = new GifResourceDecoder(var1, var2);
      this.cacheDecoder = new FileToStreamDecoder(this.decoder);
      this.encoder = new GifResourceEncoder(var2);
      this.sourceEncoder = new StreamEncoder();
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
