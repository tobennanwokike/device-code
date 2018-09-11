package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.CodaBarReader;
import com.google.zxing.oned.Code128Reader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.Code93Reader;
import com.google.zxing.oned.ITFReader;
import com.google.zxing.oned.MultiFormatUPCEANReader;
import com.google.zxing.oned.OneDReader;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;
import java.util.Hashtable;
import java.util.Vector;

public final class MultiFormatOneDReader extends OneDReader {
   private final Vector readers;

   public MultiFormatOneDReader(Hashtable var1) {
      Vector var3;
      if(var1 == null) {
         var3 = null;
      } else {
         var3 = (Vector)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      boolean var2;
      if(var1 != null && var1.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.readers = new Vector();
      if(var3 != null) {
         if(var3.contains(BarcodeFormat.EAN_13) || var3.contains(BarcodeFormat.UPC_A) || var3.contains(BarcodeFormat.EAN_8) || var3.contains(BarcodeFormat.UPC_E)) {
            this.readers.addElement(new MultiFormatUPCEANReader(var1));
         }

         if(var3.contains(BarcodeFormat.CODE_39)) {
            this.readers.addElement(new Code39Reader(var2));
         }

         if(var3.contains(BarcodeFormat.CODE_93)) {
            this.readers.addElement(new Code93Reader());
         }

         if(var3.contains(BarcodeFormat.CODE_128)) {
            this.readers.addElement(new Code128Reader());
         }

         if(var3.contains(BarcodeFormat.ITF)) {
            this.readers.addElement(new ITFReader());
         }

         if(var3.contains(BarcodeFormat.CODABAR)) {
            this.readers.addElement(new CodaBarReader());
         }

         if(var3.contains(BarcodeFormat.RSS14)) {
            this.readers.addElement(new RSS14Reader());
         }

         if(var3.contains(BarcodeFormat.RSS_EXPANDED)) {
            this.readers.addElement(new RSSExpandedReader());
         }
      }

      if(this.readers.isEmpty()) {
         this.readers.addElement(new MultiFormatUPCEANReader(var1));
         this.readers.addElement(new Code39Reader());
         this.readers.addElement(new Code93Reader());
         this.readers.addElement(new Code128Reader());
         this.readers.addElement(new ITFReader());
         this.readers.addElement(new RSS14Reader());
         this.readers.addElement(new RSSExpandedReader());
      }

   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException {
      int var5 = this.readers.size();
      int var4 = 0;

      while(var4 < var5) {
         OneDReader var6 = (OneDReader)this.readers.elementAt(var4);

         try {
            Result var8 = var6.decodeRow(var1, var2, var3);
            return var8;
         } catch (ReaderException var7) {
            ++var4;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public void reset() {
      int var2 = this.readers.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((Reader)this.readers.elementAt(var1)).reset();
      }

   }
}
