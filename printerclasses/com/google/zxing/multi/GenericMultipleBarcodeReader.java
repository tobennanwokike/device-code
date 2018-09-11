package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.multi.MultipleBarcodeReader;
import java.util.Hashtable;
import java.util.Vector;

public final class GenericMultipleBarcodeReader implements MultipleBarcodeReader {
   private static final int MIN_DIMENSION_TO_RECUR = 100;
   private final Reader delegate;

   public GenericMultipleBarcodeReader(Reader var1) {
      this.delegate = var1;
   }

   private void doDecodeMultiple(BinaryBitmap var1, Hashtable var2, Vector var3, int var4, int var5) {
      Result var15;
      try {
         var15 = this.delegate.decode(var1, var2);
      } catch (ReaderException var17) {
         return;
      }

      int var12 = 0;

      boolean var18;
      while(true) {
         if(var12 >= var3.size()) {
            var18 = false;
            break;
         }

         if(((Result)var3.elementAt(var12)).getText().equals(var15.getText())) {
            var18 = true;
            break;
         }

         ++var12;
      }

      if(!var18) {
         var3.addElement(translateResultPoints(var15, var4, var5));
         ResultPoint[] var16 = var15.getResultPoints();
         if(var16 != null && var16.length != 0) {
            int var14 = var1.getWidth();
            int var13 = var1.getHeight();
            float var6 = (float)var14;
            float var9 = (float)var13;
            var12 = 0;
            float var7 = 0.0F;

            float var8;
            for(var8 = 0.0F; var12 < var16.length; ++var12) {
               ResultPoint var19 = var16[var12];
               float var11 = var19.getX();
               float var10 = var19.getY();
               if(var11 < var6) {
                  var6 = var11;
               }

               if(var10 < var9) {
                  var9 = var10;
               }

               if(var11 > var8) {
                  var8 = var11;
               }

               if(var10 > var7) {
                  var7 = var10;
               }
            }

            if(var6 > 100.0F) {
               this.doDecodeMultiple(var1.crop(0, 0, (int)var6, var13), var2, var3, var4, var5);
            }

            if(var9 > 100.0F) {
               this.doDecodeMultiple(var1.crop(0, 0, var14, (int)var9), var2, var3, var4, var5);
            }

            if(var8 < (float)(var14 - 100)) {
               this.doDecodeMultiple(var1.crop((int)var8, 0, var14 - (int)var8, var13), var2, var3, var4 + (int)var8, var5);
            }

            if(var7 < (float)(var13 - 100)) {
               this.doDecodeMultiple(var1.crop(0, (int)var7, var14, var13 - (int)var7), var2, var3, var4, var5 + (int)var7);
            }
         }
      }

   }

   private static Result translateResultPoints(Result var0, int var1, int var2) {
      ResultPoint[] var5 = var0.getResultPoints();
      ResultPoint[] var4 = new ResultPoint[var5.length];

      for(int var3 = 0; var3 < var5.length; ++var3) {
         ResultPoint var6 = var5[var3];
         var4[var3] = new ResultPoint(var6.getX() + (float)var1, var6.getY() + (float)var2);
      }

      return new Result(var0.getText(), var0.getRawBytes(), var4, var0.getBarcodeFormat());
   }

   public Result[] decodeMultiple(BinaryBitmap var1) throws NotFoundException {
      return this.decodeMultiple(var1, (Hashtable)null);
   }

   public Result[] decodeMultiple(BinaryBitmap var1, Hashtable var2) throws NotFoundException {
      int var3 = 0;
      Vector var5 = new Vector();
      this.doDecodeMultiple(var1, var2, var5, 0, 0);
      if(var5.isEmpty()) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var4 = var5.size();

         Result[] var6;
         for(var6 = new Result[var4]; var3 < var4; ++var3) {
            var6[var3] = (Result)var5.elementAt(var3);
         }

         return var6;
      }
   }
}
