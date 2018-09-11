package com.google.zxing.oned;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Hashtable;

public abstract class OneDReader implements Reader {
   protected static final int INTEGER_MATH_SHIFT = 8;
   protected static final int PATTERN_MATCH_RESULT_SCALE_FACTOR = 256;

   private Result doDecode(BinaryBitmap param1, Hashtable param2) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }

   protected static int patternMatchVariance(int[] var0, int[] var1, int var2) {
      int var8 = Integer.MAX_VALUE;
      int var9 = var0.length;
      int var5 = 0;
      int var4 = 0;

      int var3;
      for(var3 = 0; var5 < var9; ++var5) {
         var3 += var0[var5];
         var4 += var1[var5];
      }

      int var6;
      if(var3 < var4) {
         var6 = var8;
      } else {
         int var10 = (var3 << 8) / var4;
         var4 = 0;

         for(var5 = 0; var4 < var9; ++var4) {
            var6 = var0[var4] << 8;
            int var7 = var1[var4] * var10;
            if(var6 > var7) {
               var7 = var6 - var7;
            } else {
               var7 -= var6;
            }

            var6 = var8;
            if(var7 > var2 * var10 >> 8) {
               return var6;
            }

            var5 += var7;
         }

         var6 = var5 / var3;
      }

      return var6;
   }

   protected static void recordPattern(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var6 = var2.length;

      for(int var3 = 0; var3 < var6; ++var3) {
         var2[var3] = 0;
      }

      int var7 = var0.getSize();
      if(var1 >= var7) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         boolean var9;
         if(!var0.get(var1)) {
            var9 = true;
         } else {
            var9 = false;
         }

         byte var5 = 0;
         int var4 = var1;

         for(var1 = var5; var4 < var7; ++var4) {
            if(var0.get(var4) ^ var9) {
               ++var2[var1];
            } else {
               int var10 = var1 + 1;
               if(var10 == var6) {
                  var1 = var10;
                  break;
               }

               var2[var10] = 1;
               boolean var8;
               if(!var9) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var9 = var8;
               var1 = var10;
            }
         }

         if(var1 != var6 && (var1 != var6 - 1 || var4 != var7)) {
            throw NotFoundException.getNotFoundInstance();
         }
      }
   }

   protected static void recordPatternInReverse(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var3 = var2.length;
      boolean var5 = var0.get(var1);

      while(var1 > 0 && var3 >= 0) {
         int var4 = var1 - 1;
         var1 = var4;
         if(var0.get(var4) != var5) {
            --var3;
            if(!var5) {
               var5 = true;
               var1 = var4;
            } else {
               var5 = false;
               var1 = var4;
            }
         }
      }

      if(var3 >= 0) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         recordPattern(var0, var1 + 1, var2);
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException {
      return this.decode(var1, (Hashtable)null);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, FormatException {
      Result var7;
      Result var10;
      try {
         var10 = this.doDecode(var1, var2);
      } catch (NotFoundException var6) {
         boolean var3;
         if(var2 != null && var2.containsKey(DecodeHintType.TRY_HARDER)) {
            var3 = true;
         } else {
            var3 = false;
         }

         if(var3 && var1.isRotateSupported()) {
            BinaryBitmap var5 = var1.rotateCounterClockwise();
            var7 = this.doDecode(var5, var2);
            var2 = var7.getResultMetadata();
            int var9;
            if(var2 != null && var2.containsKey(ResultMetadataType.ORIENTATION)) {
               var9 = (((Integer)var2.get(ResultMetadataType.ORIENTATION)).intValue() + 270) % 360;
            } else {
               var9 = 270;
            }

            var7.putMetadata(ResultMetadataType.ORIENTATION, new Integer(var9));
            ResultPoint[] var8 = var7.getResultPoints();
            int var4 = var5.getHeight();

            for(var9 = 0; var9 < var8.length; ++var9) {
               var8[var9] = new ResultPoint((float)var4 - var8[var9].getY() - 1.0F, var8[var9].getX());
            }

            return var7;
         }

         throw var6;
      }

      var7 = var10;
      return var7;
   }

   public abstract Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException, ChecksumException, FormatException;

   public void reset() {
   }
}
