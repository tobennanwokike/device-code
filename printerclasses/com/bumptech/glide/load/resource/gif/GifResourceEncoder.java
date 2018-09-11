package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import java.io.IOException;
import java.io.OutputStream;

public class GifResourceEncoder implements ResourceEncoder {
   private static final GifResourceEncoder.Factory FACTORY = new GifResourceEncoder.Factory();
   private static final String TAG = "GifEncoder";
   private final BitmapPool bitmapPool;
   private final GifResourceEncoder.Factory factory;
   private final GifDecoder.BitmapProvider provider;

   public GifResourceEncoder(BitmapPool var1) {
      this(var1, FACTORY);
   }

   GifResourceEncoder(BitmapPool var1, GifResourceEncoder.Factory var2) {
      this.bitmapPool = var1;
      this.provider = new GifBitmapProvider(var1);
      this.factory = var2;
   }

   private GifDecoder decodeHeaders(byte[] var1) {
      GifHeaderParser var2 = this.factory.buildParser();
      var2.setData(var1);
      GifHeader var4 = var2.parseHeader();
      GifDecoder var3 = this.factory.buildDecoder(this.provider);
      var3.setData(var4, var1);
      var3.advance();
      return var3;
   }

   private Resource getTransformedFrame(Bitmap var1, Transformation var2, GifDrawable var3) {
      Resource var4 = this.factory.buildFrameResource(var1, this.bitmapPool);
      Resource var5 = var2.transform(var4, var3.getIntrinsicWidth(), var3.getIntrinsicHeight());
      if(!var4.equals(var5)) {
         var4.recycle();
      }

      return var5;
   }

   private boolean writeDataDirect(byte[] var1, OutputStream var2) {
      boolean var3 = true;

      try {
         var2.write(var1);
      } catch (IOException var4) {
         if(Log.isLoggable("GifEncoder", 3)) {
            Log.d("GifEncoder", "Failed to write data to output stream in GifResourceEncoder", var4);
         }

         var3 = false;
      }

      return var3;
   }

   public boolean encode(Resource param1, OutputStream param2) {
      // $FF: Couldn't be decompiled
   }

   public String getId() {
      return "";
   }

   static class Factory {
      public GifDecoder buildDecoder(GifDecoder.BitmapProvider var1) {
         return new GifDecoder(var1);
      }

      public AnimatedGifEncoder buildEncoder() {
         return new AnimatedGifEncoder();
      }

      public Resource buildFrameResource(Bitmap var1, BitmapPool var2) {
         return new BitmapResource(var1, var2);
      }

      public GifHeaderParser buildParser() {
         return new GifHeaderParser();
      }
   }
}
