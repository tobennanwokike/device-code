package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableResource;
import com.bumptech.glide.util.Util;
import java.io.InputStream;
import java.util.Queue;

public class GifResourceDecoder implements ResourceDecoder {
   private static final GifResourceDecoder.GifDecoderPool DECODER_POOL = new GifResourceDecoder.GifDecoderPool();
   private static final GifResourceDecoder.GifHeaderParserPool PARSER_POOL = new GifResourceDecoder.GifHeaderParserPool();
   private static final String TAG = "GifResourceDecoder";
   private final BitmapPool bitmapPool;
   private final Context context;
   private final GifResourceDecoder.GifDecoderPool decoderPool;
   private final GifResourceDecoder.GifHeaderParserPool parserPool;
   private final GifBitmapProvider provider;

   public GifResourceDecoder(Context var1) {
      this(var1, Glide.get(var1).getBitmapPool());
   }

   public GifResourceDecoder(Context var1, BitmapPool var2) {
      this(var1, var2, PARSER_POOL, DECODER_POOL);
   }

   GifResourceDecoder(Context var1, BitmapPool var2, GifResourceDecoder.GifHeaderParserPool var3, GifResourceDecoder.GifDecoderPool var4) {
      this.context = var1;
      this.bitmapPool = var2;
      this.decoderPool = var4;
      this.provider = new GifBitmapProvider(var2);
      this.parserPool = var3;
   }

   private GifDrawableResource decode(byte[] var1, int var2, int var3, GifHeaderParser var4, GifDecoder var5) {
      Object var6 = null;
      GifHeader var7 = var4.parseHeader();
      GifDrawableResource var8 = (GifDrawableResource)var6;
      if(var7.getNumFrames() > 0) {
         if(var7.getStatus() != 0) {
            var8 = (GifDrawableResource)var6;
         } else {
            Bitmap var9 = this.decodeFirstFrame(var5, var7, var1);
            var8 = (GifDrawableResource)var6;
            if(var9 != null) {
               UnitTransformation var10 = UnitTransformation.get();
               var8 = new GifDrawableResource(new GifDrawable(this.context, this.provider, this.bitmapPool, var10, var2, var3, var7, var1, var9));
            }
         }
      }

      return var8;
   }

   private Bitmap decodeFirstFrame(GifDecoder var1, GifHeader var2, byte[] var3) {
      var1.setData(var2, var3);
      var1.advance();
      return var1.getNextFrame();
   }

   private static byte[] inputStreamToBytes(InputStream param0) {
      // $FF: Couldn't be decompiled
   }

   public GifDrawableResource decode(InputStream var1, int var2, int var3) {
      byte[] var5 = inputStreamToBytes(var1);
      GifHeaderParser var9 = this.parserPool.obtain(var5);
      GifDecoder var4 = this.decoderPool.obtain(this.provider);

      GifDrawableResource var8;
      try {
         var8 = this.decode(var5, var2, var3, var9, var4);
      } finally {
         this.parserPool.release(var9);
         this.decoderPool.release(var4);
      }

      return var8;
   }

   public String getId() {
      return "";
   }

   static class GifDecoderPool {
      private final Queue pool = Util.createQueue(0);

      public GifDecoder obtain(GifDecoder.BitmapProvider param1) {
         // $FF: Couldn't be decompiled
      }

      public void release(GifDecoder var1) {
         synchronized(this){}

         try {
            var1.clear();
            this.pool.offer(var1);
         } finally {
            ;
         }

      }
   }

   static class GifHeaderParserPool {
      private final Queue pool = Util.createQueue(0);

      public GifHeaderParser obtain(byte[] param1) {
         // $FF: Couldn't be decompiled
      }

      public void release(GifHeaderParser var1) {
         synchronized(this){}

         try {
            var1.clear();
            this.pool.offer(var1);
         } finally {
            ;
         }

      }
   }
}
