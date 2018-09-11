package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.multi.qrcode.detector.MultiFinderPatternFinder;
import com.google.zxing.qrcode.detector.Detector;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.Hashtable;
import java.util.Vector;

public final class MultiDetector extends Detector {
   private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

   public MultiDetector(BitMatrix var1) {
      super(var1);
   }

   public DetectorResult[] detectMulti(Hashtable var1) throws NotFoundException {
      byte var3 = 0;
      FinderPatternInfo[] var7 = (new MultiFinderPatternFinder(this.getImage())).findMulti(var1);
      if(var7 != null && var7.length != 0) {
         Vector var4 = new Vector();

         int var2;
         for(var2 = 0; var2 < var7.length; ++var2) {
            try {
               var4.addElement(this.processFinderPatternInfo(var7[var2]));
            } catch (ReaderException var6) {
               ;
            }
         }

         DetectorResult[] var8;
         if(var4.isEmpty()) {
            var8 = EMPTY_DETECTOR_RESULTS;
         } else {
            var8 = new DetectorResult[var4.size()];

            for(var2 = var3; var2 < var4.size(); ++var2) {
               var8[var2] = (DetectorResult)var4.elementAt(var2);
            }
         }

         return var8;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }
}
