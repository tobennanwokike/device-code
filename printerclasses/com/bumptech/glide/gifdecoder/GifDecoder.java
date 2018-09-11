package com.bumptech.glide.gifdecoder;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifFrame;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class GifDecoder {
   private static final Config BITMAP_CONFIG;
   private static final int DISPOSAL_BACKGROUND = 2;
   private static final int DISPOSAL_NONE = 1;
   private static final int DISPOSAL_PREVIOUS = 3;
   private static final int DISPOSAL_UNSPECIFIED = 0;
   private static final int INITIAL_FRAME_POINTER = -1;
   private static final int MAX_STACK_SIZE = 4096;
   private static final int NULL_CODE = -1;
   public static final int STATUS_FORMAT_ERROR = 1;
   public static final int STATUS_OK = 0;
   public static final int STATUS_OPEN_ERROR = 2;
   public static final int STATUS_PARTIAL_DECODE = 3;
   private static final String TAG = GifDecoder.class.getSimpleName();
   private int[] act;
   private GifDecoder.BitmapProvider bitmapProvider;
   private final byte[] block = new byte[256];
   private byte[] data;
   private int framePointer;
   private GifHeader header;
   private byte[] mainPixels;
   private int[] mainScratch;
   private GifHeaderParser parser;
   private byte[] pixelStack;
   private short[] prefix;
   private Bitmap previousImage;
   private ByteBuffer rawData;
   private boolean savePrevious;
   private int status;
   private byte[] suffix;

   static {
      BITMAP_CONFIG = Config.ARGB_8888;
   }

   public GifDecoder(GifDecoder.BitmapProvider var1) {
      this.bitmapProvider = var1;
      this.header = new GifHeader();
   }

   private void decodeBitmapData(GifFrame var1) {
      if(var1 != null) {
         this.rawData.position(var1.bufferFrameStart);
      }

      int var12;
      if(var1 == null) {
         var12 = this.header.width * this.header.height;
      } else {
         var12 = var1.iw * var1.ih;
      }

      if(this.mainPixels == null || this.mainPixels.length < var12) {
         this.mainPixels = new byte[var12];
      }

      if(this.prefix == null) {
         this.prefix = new short[4096];
      }

      if(this.suffix == null) {
         this.suffix = new byte[4096];
      }

      if(this.pixelStack == null) {
         this.pixelStack = new byte[4097];
      }

      int var21 = this.read();
      int var20 = 1 << var21;
      int var16 = var20 + 2;
      int var3 = -1;
      int var8 = var21 + 1;
      int var11 = (1 << var8) - 1;

      int var2;
      for(var2 = 0; var2 < var20; ++var2) {
         this.prefix[var2] = 0;
         this.suffix[var2] = (byte)var2;
      }

      int var9 = 0;
      int var6 = 0;
      var2 = 0;
      int var10 = 0;
      int var7 = 0;
      int var4 = 0;
      int var5 = 0;
      int var13 = 0;

      label108:
      while(var13 < var12) {
         int var14 = var9;
         var9 = var7;
         if(var7 == 0) {
            var9 = this.readBlock();
            if(var9 <= 0) {
               this.status = 3;
               break;
            }

            var14 = 0;
         }

         var5 += (this.block[var14] & 255) << var4;
         var4 += 8;
         int var18 = var14 + 1;
         int var17 = var9 - 1;
         var7 = var11;
         var11 = var4;
         var9 = var16;

         while(true) {
            while(var11 >= var8) {
               var4 = var5 & var7;
               var14 = var5 >> var8;
               int var15 = var11 - var8;
               if(var4 != var20) {
                  if(var4 > var9) {
                     this.status = 3;
                     var16 = var9;
                     var9 = var18;
                     var4 = var15;
                     var11 = var7;
                     var7 = var17;
                     var5 = var14;
                     continue label108;
                  }

                  if(var4 == var20 + 1) {
                     var16 = var9;
                     var9 = var18;
                     var4 = var15;
                     var11 = var7;
                     var7 = var17;
                     var5 = var14;
                     continue label108;
                  }

                  if(var3 == -1) {
                     this.pixelStack[var2] = this.suffix[var4];
                     var3 = var4;
                     ++var2;
                     var11 = var15;
                     var5 = var14;
                     var10 = var4;
                  } else {
                     var5 = var4;
                     var11 = var2;
                     if(var4 >= var9) {
                        this.pixelStack[var2] = (byte)var10;
                        var5 = var3;
                        var11 = var2 + 1;
                     }

                     while(var5 >= var20) {
                        this.pixelStack[var11] = this.suffix[var5];
                        var5 = this.prefix[var5];
                        ++var11;
                     }

                     int var19 = this.suffix[var5] & 255;
                     byte[] var22 = this.pixelStack;
                     var16 = var11 + 1;
                     var22[var11] = (byte)var19;
                     var11 = var9;
                     var10 = var7;
                     var5 = var8;
                     if(var9 < 4096) {
                        this.prefix[var9] = (short)var3;
                        this.suffix[var9] = (byte)var19;
                        var2 = var9 + 1;
                        var11 = var2;
                        var10 = var7;
                        var5 = var8;
                        if((var2 & var7) == 0) {
                           var11 = var2;
                           var10 = var7;
                           var5 = var8;
                           if(var2 < 4096) {
                              var5 = var8 + 1;
                              var10 = var7 + var2;
                              var11 = var2;
                           }
                        }
                     }

                     var2 = var6;

                     for(var3 = var16; var3 > 0; ++var2) {
                        --var3;
                        this.mainPixels[var2] = this.pixelStack[var3];
                        ++var13;
                     }

                     var16 = var3;
                     var9 = var11;
                     var11 = var15;
                     var7 = var10;
                     var8 = var5;
                     var5 = var14;
                     var10 = var19;
                     var3 = var4;
                     var6 = var2;
                     var2 = var16;
                  }
               } else {
                  var8 = var21 + 1;
                  var7 = (1 << var8) - 1;
                  var9 = var20 + 2;
                  var3 = -1;
                  var11 = var15;
                  var5 = var14;
               }
            }

            var16 = var9;
            var9 = var18;
            var4 = var11;
            var11 = var7;
            var7 = var17;
            break;
         }
      }

      for(var2 = var6; var2 < var12; ++var2) {
         this.mainPixels[var2] = 0;
      }

   }

   private GifHeaderParser getHeaderParser() {
      if(this.parser == null) {
         this.parser = new GifHeaderParser();
      }

      return this.parser;
   }

   private Bitmap getNextBitmap() {
      Bitmap var2 = this.bitmapProvider.obtain(this.header.width, this.header.height, BITMAP_CONFIG);
      Bitmap var1 = var2;
      if(var2 == null) {
         var1 = Bitmap.createBitmap(this.header.width, this.header.height, BITMAP_CONFIG);
      }

      setAlpha(var1);
      return var1;
   }

   private int read() {
      int var1 = 0;

      byte var2;
      try {
         var2 = this.rawData.get();
      } catch (Exception var4) {
         this.status = 1;
         return var1;
      }

      var1 = var2 & 255;
      return var1;
   }

   private int readBlock() {
      int var3 = this.read();
      int var2 = 0;
      int var1 = 0;
      if(var3 > 0) {
         while(true) {
            var2 = var1;
            if(var1 >= var3) {
               break;
            }

            var2 = var3 - var1;

            try {
               this.rawData.get(this.block, var1, var2);
            } catch (Exception var5) {
               Log.w(TAG, "Error Reading Block", var5);
               this.status = 1;
               var2 = var1;
               break;
            }

            var1 += var2;
         }
      }

      return var2;
   }

   @TargetApi(12)
   private static void setAlpha(Bitmap var0) {
      if(VERSION.SDK_INT >= 12) {
         var0.setHasAlpha(true);
      }

   }

   private Bitmap setPixels(GifFrame var1, GifFrame var2) {
      int var12 = this.header.width;
      int var11 = this.header.height;
      int[] var13 = this.mainScratch;
      int var3;
      if(var2 != null && var2.dispose > 0) {
         if(var2.dispose == 2) {
            var3 = 0;
            if(!var1.transparency) {
               var3 = this.header.bgColor;
            }

            Arrays.fill(var13, var3);
         } else if(var2.dispose == 3 && this.previousImage != null) {
            this.previousImage.getPixels(var13, 0, var12, 0, 0, var12, var11);
         }
      }

      this.decodeBitmapData(var1);
      int var7 = 1;
      byte var6 = 8;
      int var8 = 0;

      int var9;
      for(int var5 = 0; var5 < var1.ih; var7 = var9) {
         int var10 = var5;
         var3 = var8;
         byte var4 = var6;
         var9 = var7;
         if(var1.interlace) {
            var3 = var8;
            var4 = var6;
            var9 = var7;
            if(var8 >= var1.ih) {
               var9 = var7 + 1;
               switch(var9) {
               case 2:
                  var3 = 4;
                  var4 = var6;
                  break;
               case 3:
                  var3 = 2;
                  var4 = 4;
                  break;
               case 4:
                  var3 = 1;
                  var4 = 2;
                  break;
               default:
                  var4 = var6;
                  var3 = var8;
               }
            }

            var10 = var3;
            var3 += var4;
         }

         int var15 = var10 + var1.iy;
         if(var15 < this.header.height) {
            var10 = var15 * this.header.width;
            var8 = var10 + var1.ix;
            var7 = var8 + var1.iw;
            var15 = var7;
            if(this.header.width + var10 < var7) {
               var15 = var10 + this.header.width;
            }

            for(var7 = var5 * var1.iw; var8 < var15; ++var7) {
               byte var16 = this.mainPixels[var7];
               var10 = this.act[var16 & 255];
               if(var10 != 0) {
                  var13[var8] = var10;
               }

               ++var8;
            }
         }

         ++var5;
         var8 = var3;
         var6 = var4;
      }

      if(this.savePrevious && (var1.dispose == 0 || var1.dispose == 1)) {
         if(this.previousImage == null) {
            this.previousImage = this.getNextBitmap();
         }

         this.previousImage.setPixels(var13, 0, var12, 0, 0, var12, var11);
      }

      Bitmap var14 = this.getNextBitmap();
      var14.setPixels(var13, 0, var12, 0, 0, var12, var11);
      return var14;
   }

   public void advance() {
      this.framePointer = (this.framePointer + 1) % this.header.frameCount;
   }

   public void clear() {
      this.header = null;
      this.data = null;
      this.mainPixels = null;
      this.mainScratch = null;
      if(this.previousImage != null) {
         this.bitmapProvider.release(this.previousImage);
      }

      this.previousImage = null;
      this.rawData = null;
   }

   public int getCurrentFrameIndex() {
      return this.framePointer;
   }

   public byte[] getData() {
      return this.data;
   }

   public int getDelay(int var1) {
      byte var3 = -1;
      int var2 = var3;
      if(var1 >= 0) {
         var2 = var3;
         if(var1 < this.header.frameCount) {
            var2 = ((GifFrame)this.header.frames.get(var1)).delay;
         }
      }

      return var2;
   }

   public int getFrameCount() {
      return this.header.frameCount;
   }

   public int getHeight() {
      return this.header.height;
   }

   public int getLoopCount() {
      return this.header.loopCount;
   }

   public int getNextDelay() {
      int var1;
      if(this.header.frameCount > 0 && this.framePointer >= 0) {
         var1 = this.getDelay(this.framePointer);
      } else {
         var1 = -1;
      }

      return var1;
   }

   public Bitmap getNextFrame() {
      // $FF: Couldn't be decompiled
   }

   public int getStatus() {
      return this.status;
   }

   public int getWidth() {
      return this.header.width;
   }

   public int read(InputStream param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public int read(byte[] var1) {
      this.data = var1;
      this.header = this.getHeaderParser().setData(var1).parseHeader();
      if(var1 != null) {
         this.rawData = ByteBuffer.wrap(var1);
         this.rawData.rewind();
         this.rawData.order(ByteOrder.LITTLE_ENDIAN);
         this.mainPixels = new byte[this.header.width * this.header.height];
         this.mainScratch = new int[this.header.width * this.header.height];
         this.savePrevious = false;
         Iterator var2 = this.header.frames.iterator();

         while(var2.hasNext()) {
            if(((GifFrame)var2.next()).dispose == 3) {
               this.savePrevious = true;
               break;
            }
         }
      }

      return this.status;
   }

   public void resetFrameIndex() {
      this.framePointer = -1;
   }

   public void setData(GifHeader var1, byte[] var2) {
      this.header = var1;
      this.data = var2;
      this.status = 0;
      this.framePointer = -1;
      this.rawData = ByteBuffer.wrap(var2);
      this.rawData.rewind();
      this.rawData.order(ByteOrder.LITTLE_ENDIAN);
      this.savePrevious = false;
      Iterator var3 = var1.frames.iterator();

      while(var3.hasNext()) {
         if(((GifFrame)var3.next()).dispose == 3) {
            this.savePrevious = true;
            break;
         }
      }

      this.mainPixels = new byte[var1.width * var1.height];
      this.mainScratch = new int[var1.width * var1.height];
   }

   public interface BitmapProvider {
      Bitmap obtain(int var1, int var2, Config var3);

      void release(Bitmap var1);
   }
}
