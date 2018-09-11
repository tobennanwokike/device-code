package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.EAN13Reader;
import com.google.zxing.oned.EAN8Reader;
import com.google.zxing.oned.OneDReader;
import com.google.zxing.oned.UPCAReader;
import com.google.zxing.oned.UPCEANReader;
import com.google.zxing.oned.UPCEReader;
import java.util.Hashtable;
import java.util.Vector;

public final class MultiFormatUPCEANReader extends OneDReader {
   private final Vector readers;

   public MultiFormatUPCEANReader(Hashtable var1) {
      Vector var2;
      if(var1 == null) {
         var2 = null;
      } else {
         var2 = (Vector)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      this.readers = new Vector();
      if(var2 != null) {
         if(var2.contains(BarcodeFormat.EAN_13)) {
            this.readers.addElement(new EAN13Reader());
         } else if(var2.contains(BarcodeFormat.UPC_A)) {
            this.readers.addElement(new UPCAReader());
         }

         if(var2.contains(BarcodeFormat.EAN_8)) {
            this.readers.addElement(new EAN8Reader());
         }

         if(var2.contains(BarcodeFormat.UPC_E)) {
            this.readers.addElement(new UPCEReader());
         }
      }

      if(this.readers.isEmpty()) {
         this.readers.addElement(new EAN13Reader());
         this.readers.addElement(new EAN8Reader());
         this.readers.addElement(new UPCEReader());
      }

   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException {
      int[] var7 = UPCEANReader.findStartGuardPattern(var2);
      int var5 = this.readers.size();
      int var4 = 0;

      while(true) {
         if(var4 < var5) {
            UPCEANReader var6 = (UPCEANReader)this.readers.elementAt(var4);

            Result var13;
            try {
               var13 = var6.decodeRow(var1, var2, var7, var3);
            } catch (ReaderException var8) {
               ++var4;
               continue;
            }

            boolean var9;
            if(BarcodeFormat.EAN_13.equals(var13.getBarcodeFormat()) && var13.getText().charAt(0) == 48) {
               var9 = true;
            } else {
               var9 = false;
            }

            Vector var10;
            if(var3 == null) {
               var10 = null;
            } else {
               var10 = (Vector)var3.get(DecodeHintType.POSSIBLE_FORMATS);
            }

            boolean var12;
            if(var10 != null && !var10.contains(BarcodeFormat.UPC_A)) {
               var12 = false;
            } else {
               var12 = true;
            }

            Result var11;
            if(var9 && var12) {
               var11 = new Result(var13.getText().substring(1), (byte[])null, var13.getResultPoints(), BarcodeFormat.UPC_A);
            } else {
               var11 = var13;
            }

            return var11;
         }

         throw NotFoundException.getNotFoundInstance();
      }
   }

   public void reset() {
      int var2 = this.readers.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((Reader)this.readers.elementAt(var1)).reset();
      }

   }
}
