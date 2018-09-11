package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import java.io.InputStream;

public class StreamBitmapDecoder implements ResourceDecoder {
   private static final String ID = "StreamBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
   private BitmapPool bitmapPool;
   private DecodeFormat decodeFormat;
   private final Downsampler downsampler;
   private String id;

   public StreamBitmapDecoder(Context var1) {
      this(Glide.get(var1).getBitmapPool());
   }

   public StreamBitmapDecoder(Context var1, DecodeFormat var2) {
      this(Glide.get(var1).getBitmapPool(), var2);
   }

   public StreamBitmapDecoder(BitmapPool var1) {
      this(var1, DecodeFormat.DEFAULT);
   }

   public StreamBitmapDecoder(BitmapPool var1, DecodeFormat var2) {
      this(Downsampler.AT_LEAST, var1, var2);
   }

   public StreamBitmapDecoder(Downsampler var1, BitmapPool var2, DecodeFormat var3) {
      this.downsampler = var1;
      this.bitmapPool = var2;
      this.decodeFormat = var3;
   }

   public Resource decode(InputStream var1, int var2, int var3) {
      return BitmapResource.obtain(this.downsampler.decode(var1, this.bitmapPool, var2, var3, this.decodeFormat), this.bitmapPool);
   }

   public String getId() {
      if(this.id == null) {
         this.id = "StreamBitmapDecoder.com.bumptech.glide.load.resource.bitmap" + this.downsampler.getId() + this.decodeFormat.name();
      }

      return this.id;
   }
}
