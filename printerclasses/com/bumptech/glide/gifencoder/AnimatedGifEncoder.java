package com.bumptech.glide.gifencoder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.Log;
import com.bumptech.glide.gifencoder.LZWEncoder;
import com.bumptech.glide.gifencoder.NeuQuant;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AnimatedGifEncoder {
   private static final double MIN_TRANSPARENT_PERCENTAGE = 4.0D;
   private static final String TAG = "AnimatedGifEncoder";
   private boolean closeStream = false;
   private int colorDepth;
   private byte[] colorTab;
   private int delay = 0;
   private int dispose = -1;
   private boolean firstFrame = true;
   private boolean hasTransparentPixels;
   private int height;
   private Bitmap image;
   private byte[] indexedPixels;
   private OutputStream out;
   private int palSize = 7;
   private byte[] pixels;
   private int repeat = -1;
   private int sample = 10;
   private boolean sizeSet = false;
   private boolean started = false;
   private int transIndex;
   private Integer transparent = null;
   private boolean[] usedEntry = new boolean[256];
   private int width;

   private void analyzePixels() {
      int var2 = this.pixels.length;
      int var4 = var2 / 3;
      this.indexedPixels = new byte[var4];
      NeuQuant var7 = new NeuQuant(this.pixels, var2, this.sample);
      this.colorTab = var7.process();

      for(var2 = 0; var2 < this.colorTab.length; var2 += 3) {
         byte var1 = this.colorTab[var2];
         this.colorTab[var2] = this.colorTab[var2 + 2];
         this.colorTab[var2 + 2] = var1;
         this.usedEntry[var2 / 3] = false;
      }

      var2 = 0;

      for(int var3 = 0; var2 < var4; ++var3) {
         byte[] var8 = this.pixels;
         int var5 = var3 + 1;
         byte var6 = var8[var3];
         var8 = this.pixels;
         var3 = var5 + 1;
         var5 = var7.map(var6 & 255, var8[var5] & 255, this.pixels[var3] & 255);
         this.usedEntry[var5] = true;
         this.indexedPixels[var2] = (byte)var5;
         ++var2;
      }

      this.pixels = null;
      this.colorDepth = 8;
      this.palSize = 7;
      if(this.transparent != null) {
         this.transIndex = this.findClosest(this.transparent.intValue());
      } else if(this.hasTransparentPixels) {
         this.transIndex = this.findClosest(0);
      }

   }

   private int findClosest(int var1) {
      int var3;
      if(this.colorTab == null) {
         var3 = -1;
      } else {
         int var8 = Color.red(var1);
         int var7 = Color.green(var1);
         int var10 = Color.blue(var1);
         var1 = 0;
         int var2 = 16777216;
         int var9 = this.colorTab.length;
         int var4 = 0;

         while(true) {
            var3 = var1;
            if(var4 >= var9) {
               break;
            }

            byte[] var12 = this.colorTab;
            int var5 = var4 + 1;
            var3 = var8 - (var12[var4] & 255);
            var12 = this.colorTab;
            int var11 = var5 + 1;
            var5 = var7 - (var12[var5] & 255);
            var4 = var10 - (this.colorTab[var11] & 255);
            int var6 = var3 * var3 + var5 * var5 + var4 * var4;
            var5 = var11 / 3;
            var4 = var2;
            var3 = var1;
            if(this.usedEntry[var5]) {
               var4 = var2;
               var3 = var1;
               if(var6 < var2) {
                  var4 = var6;
                  var3 = var5;
               }
            }

            var1 = var11 + 1;
            var2 = var4;
            var4 = var1;
            var1 = var3;
         }
      }

      return var3;
   }

   private void getImagePixels() {
      int var4 = this.image.getWidth();
      int var3 = this.image.getHeight();
      if(var4 != this.width || var3 != this.height) {
         Bitmap var10 = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
         (new Canvas(var10)).drawBitmap(var10, 0.0F, 0.0F, (Paint)null);
         this.image = var10;
      }

      int[] var12 = new int[var4 * var3];
      this.image.getPixels(var12, 0, var4, 0, 0, var4, var3);
      this.pixels = new byte[var12.length * 3];
      this.hasTransparentPixels = false;
      int var6 = 0;
      int var7 = var12.length;
      var3 = 0;

      for(int var5 = 0; var3 < var7; var6 = var4) {
         int var8 = var12[var3];
         var4 = var6;
         if(var8 == 0) {
            var4 = var6 + 1;
         }

         byte[] var11 = this.pixels;
         var6 = var5 + 1;
         var11[var5] = (byte)(var8 & 255);
         var11 = this.pixels;
         var5 = var6 + 1;
         var11[var6] = (byte)(var8 >> 8 & 255);
         this.pixels[var5] = (byte)(var8 >> 16 & 255);
         ++var3;
         ++var5;
      }

      double var1 = (double)(var6 * 100) / (double)var12.length;
      boolean var9;
      if(var1 > 4.0D) {
         var9 = true;
      } else {
         var9 = false;
      }

      this.hasTransparentPixels = var9;
      if(Log.isLoggable("AnimatedGifEncoder", 3)) {
         Log.d("AnimatedGifEncoder", "got pixels for frame with " + var1 + "% transparent pixels");
      }

   }

   private void writeGraphicCtrlExt() throws IOException {
      this.out.write(33);
      this.out.write(249);
      this.out.write(4);
      int var1;
      byte var2;
      if(this.transparent == null && !this.hasTransparentPixels) {
         var2 = 0;
         var1 = 0;
      } else {
         var2 = 1;
         var1 = 2;
      }

      if(this.dispose >= 0) {
         var1 = this.dispose & 7;
      }

      this.out.write(var1 << 2 | 0 | 0 | var2);
      this.writeShort(this.delay);
      this.out.write(this.transIndex);
      this.out.write(0);
   }

   private void writeImageDesc() throws IOException {
      this.out.write(44);
      this.writeShort(0);
      this.writeShort(0);
      this.writeShort(this.width);
      this.writeShort(this.height);
      if(this.firstFrame) {
         this.out.write(0);
      } else {
         this.out.write(this.palSize | 128);
      }

   }

   private void writeLSD() throws IOException {
      this.writeShort(this.width);
      this.writeShort(this.height);
      this.out.write(this.palSize | 240);
      this.out.write(0);
      this.out.write(0);
   }

   private void writeNetscapeExt() throws IOException {
      this.out.write(33);
      this.out.write(255);
      this.out.write(11);
      this.writeString("NETSCAPE2.0");
      this.out.write(3);
      this.out.write(1);
      this.writeShort(this.repeat);
      this.out.write(0);
   }

   private void writePalette() throws IOException {
      this.out.write(this.colorTab, 0, this.colorTab.length);
      int var2 = this.colorTab.length;

      for(int var1 = 0; var1 < 768 - var2; ++var1) {
         this.out.write(0);
      }

   }

   private void writePixels() throws IOException {
      (new LZWEncoder(this.width, this.height, this.indexedPixels, this.colorDepth)).encode(this.out);
   }

   private void writeShort(int var1) throws IOException {
      this.out.write(var1 & 255);
      this.out.write(var1 >> 8 & 255);
   }

   private void writeString(String var1) throws IOException {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         this.out.write((byte)var1.charAt(var2));
      }

   }

   public boolean addFrame(Bitmap var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if(var1 != null) {
         if(!this.started) {
            var2 = var3;
         } else {
            var2 = true;

            try {
               if(!this.sizeSet) {
                  this.setSize(var1.getWidth(), var1.getHeight());
               }

               this.image = var1;
               this.getImagePixels();
               this.analyzePixels();
               if(this.firstFrame) {
                  this.writeLSD();
                  this.writePalette();
                  if(this.repeat >= 0) {
                     this.writeNetscapeExt();
                  }
               }

               this.writeGraphicCtrlExt();
               this.writeImageDesc();
               if(!this.firstFrame) {
                  this.writePalette();
               }

               this.writePixels();
               this.firstFrame = false;
            } catch (IOException var4) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public boolean finish() {
      // $FF: Couldn't be decompiled
   }

   public void setDelay(int var1) {
      this.delay = Math.round((float)var1 / 10.0F);
   }

   public void setDispose(int var1) {
      if(var1 >= 0) {
         this.dispose = var1;
      }

   }

   public void setFrameRate(float var1) {
      if(var1 != 0.0F) {
         this.delay = Math.round(100.0F / var1);
      }

   }

   public void setQuality(int var1) {
      int var2 = var1;
      if(var1 < 1) {
         var2 = 1;
      }

      this.sample = var2;
   }

   public void setRepeat(int var1) {
      if(var1 >= 0) {
         this.repeat = var1;
      }

   }

   public void setSize(int var1, int var2) {
      if(!this.started || this.firstFrame) {
         this.width = var1;
         this.height = var2;
         if(this.width < 1) {
            this.width = 320;
         }

         if(this.height < 1) {
            this.height = 240;
         }

         this.sizeSet = true;
      }

   }

   public void setTransparent(int var1) {
      this.transparent = Integer.valueOf(var1);
   }

   public boolean start(OutputStream var1) {
      boolean var2;
      if(var1 == null) {
         var2 = false;
      } else {
         var2 = true;
         this.closeStream = false;
         this.out = var1;

         try {
            this.writeString("GIF89a");
         } catch (IOException var3) {
            var2 = false;
         }

         this.started = var2;
      }

      return var2;
   }

   public boolean start(String var1) {
      boolean var2;
      try {
         FileOutputStream var3 = new FileOutputStream(var1);
         BufferedOutputStream var4 = new BufferedOutputStream(var3);
         this.out = var4;
         var2 = this.start(this.out);
         this.closeStream = true;
      } catch (IOException var5) {
         var2 = false;
      }

      this.started = var2;
      return var2;
   }
}
