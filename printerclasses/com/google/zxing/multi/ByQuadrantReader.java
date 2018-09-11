package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import java.util.Hashtable;

public final class ByQuadrantReader implements Reader {
   private final Reader delegate;

   public ByQuadrantReader(Reader var1) {
      this.delegate = var1;
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Hashtable)null);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, ChecksumException, FormatException {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      var3 /= 2;
      var4 /= 2;
      BinaryBitmap var5 = var1.crop(0, 0, var3, var4);

      Result var10;
      Result var11;
      try {
         var11 = this.delegate.decode(var5, var2);
      } catch (NotFoundException var9) {
         var5 = var1.crop(var3, 0, var3, var4);

         try {
            var11 = this.delegate.decode(var5, var2);
         } catch (NotFoundException var8) {
            var5 = var1.crop(0, var4, var3, var4);

            try {
               var11 = this.delegate.decode(var5, var2);
            } catch (NotFoundException var7) {
               var5 = var1.crop(var3, var4, var3, var4);

               try {
                  var11 = this.delegate.decode(var5, var2);
               } catch (NotFoundException var6) {
                  var1 = var1.crop(var3 / 2, var4 / 2, var3, var4);
                  var10 = this.delegate.decode(var1, var2);
                  return var10;
               }

               var10 = var11;
               return var10;
            }

            var10 = var11;
            return var10;
         }

         var10 = var11;
         return var10;
      }

      var10 = var11;
      return var10;
   }

   public void reset() {
      this.delegate.reset();
   }
}
