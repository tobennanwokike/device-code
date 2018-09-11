package com.google.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.Hashtable;
import java.util.Vector;

public final class MultiFormatReader implements Reader {
   private Hashtable hints;
   private Vector readers;

   private Result decodeInternal(BinaryBitmap var1) throws NotFoundException {
      int var3 = this.readers.size();
      int var2 = 0;

      while(var2 < var3) {
         Reader var4 = (Reader)this.readers.elementAt(var2);

         try {
            Result var6 = var4.decode(var1, this.hints);
            return var6;
         } catch (ReaderException var5) {
            ++var2;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException {
      this.setHints((Hashtable)null);
      return this.decodeInternal(var1);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException {
      this.setHints(var2);
      return this.decodeInternal(var1);
   }

   public Result decodeWithState(BinaryBitmap var1) throws NotFoundException {
      if(this.readers == null) {
         this.setHints((Hashtable)null);
      }

      return this.decodeInternal(var1);
   }

   public void reset() {
      int var2 = this.readers.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((Reader)this.readers.elementAt(var1)).reset();
      }

   }

   public void setHints(Hashtable var1) {
      boolean var3 = false;
      this.hints = var1;
      boolean var2;
      if(var1 != null && var1.containsKey(DecodeHintType.TRY_HARDER)) {
         var2 = true;
      } else {
         var2 = false;
      }

      Vector var4;
      if(var1 == null) {
         var4 = null;
      } else {
         var4 = (Vector)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      this.readers = new Vector();
      if(var4 != null) {
         if(var4.contains(BarcodeFormat.UPC_A) || var4.contains(BarcodeFormat.UPC_E) || var4.contains(BarcodeFormat.EAN_13) || var4.contains(BarcodeFormat.EAN_8) || var4.contains(BarcodeFormat.CODE_39) || var4.contains(BarcodeFormat.CODE_93) || var4.contains(BarcodeFormat.CODE_128) || var4.contains(BarcodeFormat.ITF) || var4.contains(BarcodeFormat.RSS14) || var4.contains(BarcodeFormat.RSS_EXPANDED)) {
            var3 = true;
         }

         if(var3 && !var2) {
            this.readers.addElement(new MultiFormatOneDReader(var1));
         }

         if(var4.contains(BarcodeFormat.QR_CODE)) {
            this.readers.addElement(new QRCodeReader());
         }

         if(var4.contains(BarcodeFormat.DATA_MATRIX)) {
            this.readers.addElement(new DataMatrixReader());
         }

         if(var4.contains(BarcodeFormat.PDF417)) {
            this.readers.addElement(new PDF417Reader());
         }

         if(var3 && var2) {
            this.readers.addElement(new MultiFormatOneDReader(var1));
         }
      }

      if(this.readers.isEmpty()) {
         if(!var2) {
            this.readers.addElement(new MultiFormatOneDReader(var1));
         }

         this.readers.addElement(new QRCodeReader());
         this.readers.addElement(new DataMatrixReader());
         if(var2) {
            this.readers.addElement(new MultiFormatOneDReader(var1));
         }
      }

   }
}
