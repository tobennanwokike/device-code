package com.google.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import java.util.Enumeration;
import java.util.Hashtable;

public final class Result {
   private final BarcodeFormat format;
   private final byte[] rawBytes;
   private Hashtable resultMetadata;
   private ResultPoint[] resultPoints;
   private final String text;
   private final long timestamp;

   public Result(String var1, byte[] var2, ResultPoint[] var3, BarcodeFormat var4) {
      this(var1, var2, var3, var4, System.currentTimeMillis());
   }

   public Result(String var1, byte[] var2, ResultPoint[] var3, BarcodeFormat var4, long var5) {
      if(var1 == null && var2 == null) {
         throw new IllegalArgumentException("Text and bytes are null");
      } else {
         this.text = var1;
         this.rawBytes = var2;
         this.resultPoints = var3;
         this.format = var4;
         this.resultMetadata = null;
         this.timestamp = var5;
      }
   }

   public void addResultPoints(ResultPoint[] var1) {
      if(this.resultPoints == null) {
         this.resultPoints = var1;
      } else if(var1 != null && var1.length > 0) {
         ResultPoint[] var2 = new ResultPoint[this.resultPoints.length + var1.length];
         System.arraycopy(this.resultPoints, 0, var2, 0, this.resultPoints.length);
         System.arraycopy(var1, 0, var2, this.resultPoints.length, var1.length);
         this.resultPoints = var2;
      }

   }

   public BarcodeFormat getBarcodeFormat() {
      return this.format;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public Hashtable getResultMetadata() {
      return this.resultMetadata;
   }

   public ResultPoint[] getResultPoints() {
      return this.resultPoints;
   }

   public String getText() {
      return this.text;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void putAllMetadata(Hashtable var1) {
      if(var1 != null) {
         if(this.resultMetadata == null) {
            this.resultMetadata = var1;
         } else {
            Enumeration var3 = var1.keys();

            while(var3.hasMoreElements()) {
               ResultMetadataType var2 = (ResultMetadataType)var3.nextElement();
               Object var4 = var1.get(var2);
               this.resultMetadata.put(var2, var4);
            }
         }
      }

   }

   public void putMetadata(ResultMetadataType var1, Object var2) {
      if(this.resultMetadata == null) {
         this.resultMetadata = new Hashtable(3);
      }

      this.resultMetadata.put(var1, var2);
   }

   public String toString() {
      String var1;
      if(this.text == null) {
         var1 = "[" + this.rawBytes.length + " bytes]";
      } else {
         var1 = this.text;
      }

      return var1;
   }
}
