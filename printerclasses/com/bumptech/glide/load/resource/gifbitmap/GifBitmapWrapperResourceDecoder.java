package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResource;
import com.bumptech.glide.util.ByteArrayPool;
import java.io.IOException;
import java.io.InputStream;

public class GifBitmapWrapperResourceDecoder implements ResourceDecoder {
   private static final GifBitmapWrapperResourceDecoder.ImageTypeParser DEFAULT_PARSER = new GifBitmapWrapperResourceDecoder.ImageTypeParser();
   private static final GifBitmapWrapperResourceDecoder.BufferedStreamFactory DEFAULT_STREAM_FACTORY = new GifBitmapWrapperResourceDecoder.BufferedStreamFactory();
   static final int MARK_LIMIT_BYTES = 2048;
   private final ResourceDecoder bitmapDecoder;
   private final BitmapPool bitmapPool;
   private final ResourceDecoder gifDecoder;
   private String id;
   private final GifBitmapWrapperResourceDecoder.ImageTypeParser parser;
   private final GifBitmapWrapperResourceDecoder.BufferedStreamFactory streamFactory;

   public GifBitmapWrapperResourceDecoder(ResourceDecoder var1, ResourceDecoder var2, BitmapPool var3) {
      this(var1, var2, var3, DEFAULT_PARSER, DEFAULT_STREAM_FACTORY);
   }

   GifBitmapWrapperResourceDecoder(ResourceDecoder var1, ResourceDecoder var2, BitmapPool var3, GifBitmapWrapperResourceDecoder.ImageTypeParser var4, GifBitmapWrapperResourceDecoder.BufferedStreamFactory var5) {
      this.bitmapDecoder = var1;
      this.gifDecoder = var2;
      this.bitmapPool = var3;
      this.parser = var4;
      this.streamFactory = var5;
   }

   private GifBitmapWrapper decode(ImageVideoWrapper var1, int var2, int var3, byte[] var4) throws IOException {
      GifBitmapWrapper var5;
      if(var1.getStream() != null) {
         var5 = this.decodeStream(var1, var2, var3, var4);
      } else {
         var5 = this.decodeBitmapWrapper(var1, var2, var3);
      }

      return var5;
   }

   private GifBitmapWrapper decodeBitmapWrapper(ImageVideoWrapper var1, int var2, int var3) throws IOException {
      Object var4 = null;
      Resource var5 = this.bitmapDecoder.decode(var1, var2, var3);
      GifBitmapWrapper var6 = (GifBitmapWrapper)var4;
      if(var5 != null) {
         var6 = new GifBitmapWrapper(var5, (Resource)null);
      }

      return var6;
   }

   private GifBitmapWrapper decodeGifWrapper(InputStream var1, int var2, int var3) throws IOException {
      Object var4 = null;
      Resource var5 = this.gifDecoder.decode(var1, var2, var3);
      GifBitmapWrapper var6 = (GifBitmapWrapper)var4;
      if(var5 != null) {
         GifDrawable var7 = (GifDrawable)var5.get();
         if(var7.getFrameCount() > 1) {
            var6 = new GifBitmapWrapper((Resource)null, var5);
         } else {
            var6 = new GifBitmapWrapper(new BitmapResource(var7.getFirstFrame(), this.bitmapPool), (Resource)null);
         }
      }

      return var6;
   }

   private GifBitmapWrapper decodeStream(ImageVideoWrapper var1, int var2, int var3, byte[] var4) throws IOException {
      InputStream var6 = this.streamFactory.build(var1.getStream(), var4);
      var6.mark(2048);
      ImageHeaderParser.ImageType var5 = this.parser.parse(var6);
      var6.reset();
      GifBitmapWrapper var8 = null;
      if(var5 == ImageHeaderParser.ImageType.GIF) {
         var8 = this.decodeGifWrapper(var6, var2, var3);
      }

      GifBitmapWrapper var7 = var8;
      if(var8 == null) {
         var7 = this.decodeBitmapWrapper(new ImageVideoWrapper(var6, var1.getFileDescriptor()), var2, var3);
      }

      return var7;
   }

   public Resource decode(ImageVideoWrapper var1, int var2, int var3) throws IOException {
      ByteArrayPool var5 = ByteArrayPool.get();
      byte[] var4 = var5.getBytes();

      GifBitmapWrapper var8;
      try {
         var8 = this.decode(var1, var2, var3, var4);
      } finally {
         var5.releaseBytes(var4);
      }

      GifBitmapWrapperResource var9;
      if(var8 != null) {
         var9 = new GifBitmapWrapperResource(var8);
      } else {
         var9 = null;
      }

      return var9;
   }

   public String getId() {
      if(this.id == null) {
         this.id = this.gifDecoder.getId() + this.bitmapDecoder.getId();
      }

      return this.id;
   }

   static class BufferedStreamFactory {
      public InputStream build(InputStream var1, byte[] var2) {
         return new RecyclableBufferedInputStream(var1, var2);
      }
   }

   static class ImageTypeParser {
      public ImageHeaderParser.ImageType parse(InputStream var1) throws IOException {
         return (new ImageHeaderParser(var1)).getType();
      }
   }
}
