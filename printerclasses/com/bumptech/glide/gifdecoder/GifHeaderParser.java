package com.bumptech.glide.gifdecoder;

import com.bumptech.glide.gifdecoder.GifFrame;
import com.bumptech.glide.gifdecoder.GifHeader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
   static final int DEFAULT_FRAME_DELAY = 10;
   private static final int MAX_BLOCK_SIZE = 256;
   static final int MIN_FRAME_DELAY = 3;
   public static final String TAG = "GifHeaderParser";
   private final byte[] block = new byte[256];
   private int blockSize = 0;
   private GifHeader header;
   private ByteBuffer rawData;

   private boolean err() {
      boolean var1;
      if(this.header.status != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private int read() {
      int var1 = 0;

      byte var2;
      try {
         var2 = this.rawData.get();
      } catch (Exception var4) {
         this.header.status = 1;
         return var1;
      }

      var1 = var2 & 255;
      return var1;
   }

   private void readBitmap() {
      boolean var4 = true;
      this.header.currentFrame.ix = this.readShort();
      this.header.currentFrame.iy = this.readShort();
      this.header.currentFrame.iw = this.readShort();
      this.header.currentFrame.ih = this.readShort();
      int var2 = this.read();
      boolean var1;
      if((var2 & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      int var3 = (int)Math.pow(2.0D, (double)((var2 & 7) + 1));
      GifFrame var5 = this.header.currentFrame;
      if((var2 & 64) == 0) {
         var4 = false;
      }

      var5.interlace = var4;
      if(var1) {
         this.header.currentFrame.lct = this.readColorTable(var3);
      } else {
         this.header.currentFrame.lct = null;
      }

      this.header.currentFrame.bufferFrameStart = this.rawData.position();
      this.skipImageData();
      if(!this.err()) {
         GifHeader var6 = this.header;
         ++var6.frameCount;
         this.header.frames.add(this.header.currentFrame);
      }

   }

   private int readBlock() {
      // $FF: Couldn't be decompiled
   }

   private int[] readColorTable(int param1) {
      // $FF: Couldn't be decompiled
   }

   private void readContents() {
      boolean var1 = false;

      while(!var1 && !this.err()) {
         switch(this.read()) {
         case 33:
            switch(this.read()) {
            case 1:
               this.skip();
               continue;
            case 249:
               this.header.currentFrame = new GifFrame();
               this.readGraphicControlExt();
               continue;
            case 254:
               this.skip();
               continue;
            case 255:
               this.readBlock();
               String var3 = "";

               for(int var2 = 0; var2 < 11; ++var2) {
                  var3 = var3 + (char)this.block[var2];
               }

               if(var3.equals("NETSCAPE2.0")) {
                  this.readNetscapeExt();
               } else {
                  this.skip();
               }
               continue;
            default:
               this.skip();
               continue;
            }
         case 44:
            if(this.header.currentFrame == null) {
               this.header.currentFrame = new GifFrame();
            }

            this.readBitmap();
            break;
         case 59:
            var1 = true;
            break;
         default:
            this.header.status = 1;
         }
      }

   }

   private void readGraphicControlExt() {
      boolean var3 = true;
      this.read();
      int var1 = this.read();
      this.header.currentFrame.dispose = (var1 & 28) >> 2;
      if(this.header.currentFrame.dispose == 0) {
         this.header.currentFrame.dispose = 1;
      }

      GifFrame var4 = this.header.currentFrame;
      if((var1 & 1) == 0) {
         var3 = false;
      }

      var4.transparency = var3;
      int var2 = this.readShort();
      var1 = var2;
      if(var2 < 3) {
         var1 = 10;
      }

      this.header.currentFrame.delay = var1 * 10;
      this.header.currentFrame.transIndex = this.read();
      this.read();
   }

   private void readHeader() {
      String var2 = "";

      for(int var1 = 0; var1 < 6; ++var1) {
         var2 = var2 + (char)this.read();
      }

      if(!var2.startsWith("GIF")) {
         this.header.status = 1;
      } else {
         this.readLSD();
         if(this.header.gctFlag && !this.err()) {
            this.header.gct = this.readColorTable(this.header.gctSize);
            this.header.bgColor = this.header.gct[this.header.bgIndex];
         }
      }

   }

   private void readLSD() {
      this.header.width = this.readShort();
      this.header.height = this.readShort();
      int var1 = this.read();
      GifHeader var3 = this.header;
      boolean var2;
      if((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.gctFlag = var2;
      this.header.gctSize = 2 << (var1 & 7);
      this.header.bgIndex = this.read();
      this.header.pixelAspect = this.read();
   }

   private void readNetscapeExt() {
      do {
         this.readBlock();
         if(this.block[0] == 1) {
            byte var1 = this.block[1];
            byte var2 = this.block[2];
            this.header.loopCount = (var2 & 255) << 8 | var1 & 255;
         }
      } while(this.blockSize > 0 && !this.err());

   }

   private int readShort() {
      return this.rawData.getShort();
   }

   private void reset() {
      this.rawData = null;
      Arrays.fill(this.block, (byte)0);
      this.header = new GifHeader();
      this.blockSize = 0;
   }

   private void skip() {
      int var1;
      do {
         var1 = this.read();
         this.rawData.position(this.rawData.position() + var1);
      } while(var1 > 0);

   }

   private void skipImageData() {
      this.read();
      this.skip();
   }

   public void clear() {
      this.rawData = null;
      this.header = null;
   }

   public GifHeader parseHeader() {
      if(this.rawData == null) {
         throw new IllegalStateException("You must call setData() before parseHeader()");
      } else {
         GifHeader var1;
         if(this.err()) {
            var1 = this.header;
         } else {
            this.readHeader();
            if(!this.err()) {
               this.readContents();
               if(this.header.frameCount < 0) {
                  this.header.status = 1;
               }
            }

            var1 = this.header;
         }

         return var1;
      }
   }

   public GifHeaderParser setData(byte[] var1) {
      this.reset();
      if(var1 != null) {
         this.rawData = ByteBuffer.wrap(var1);
         this.rawData.rewind();
         this.rawData.order(ByteOrder.LITTLE_ENDIAN);
      } else {
         this.rawData = null;
         this.header.status = 2;
      }

      return this;
   }
}
