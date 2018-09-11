package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.encoder.ByteMatrix;

public final class QRCode {
   public static final int NUM_MASK_PATTERNS = 8;
   private ErrorCorrectionLevel ecLevel = null;
   private int maskPattern = -1;
   private ByteMatrix matrix = null;
   private int matrixWidth = -1;
   private Mode mode = null;
   private int numDataBytes = -1;
   private int numECBytes = -1;
   private int numRSBlocks = -1;
   private int numTotalBytes = -1;
   private int version = -1;

   public static boolean isValidMaskPattern(int var0) {
      boolean var1;
      if(var0 >= 0 && var0 < 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int at(int var1, int var2) {
      byte var3 = this.matrix.get(var1, var2);
      if(var3 != 0 && var3 != 1) {
         throw new RuntimeException("Bad value");
      } else {
         return var3;
      }
   }

   public ErrorCorrectionLevel getECLevel() {
      return this.ecLevel;
   }

   public int getMaskPattern() {
      return this.maskPattern;
   }

   public ByteMatrix getMatrix() {
      return this.matrix;
   }

   public int getMatrixWidth() {
      return this.matrixWidth;
   }

   public Mode getMode() {
      return this.mode;
   }

   public int getNumDataBytes() {
      return this.numDataBytes;
   }

   public int getNumECBytes() {
      return this.numECBytes;
   }

   public int getNumRSBlocks() {
      return this.numRSBlocks;
   }

   public int getNumTotalBytes() {
      return this.numTotalBytes;
   }

   public int getVersion() {
      return this.version;
   }

   public boolean isValid() {
      boolean var1;
      if(this.mode != null && this.ecLevel != null && this.version != -1 && this.matrixWidth != -1 && this.maskPattern != -1 && this.numTotalBytes != -1 && this.numDataBytes != -1 && this.numECBytes != -1 && this.numRSBlocks != -1 && isValidMaskPattern(this.maskPattern) && this.numTotalBytes == this.numDataBytes + this.numECBytes && this.matrix != null && this.matrixWidth == this.matrix.getWidth() && this.matrix.getWidth() == this.matrix.getHeight()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setECLevel(ErrorCorrectionLevel var1) {
      this.ecLevel = var1;
   }

   public void setMaskPattern(int var1) {
      this.maskPattern = var1;
   }

   public void setMatrix(ByteMatrix var1) {
      this.matrix = var1;
   }

   public void setMatrixWidth(int var1) {
      this.matrixWidth = var1;
   }

   public void setMode(Mode var1) {
      this.mode = var1;
   }

   public void setNumDataBytes(int var1) {
      this.numDataBytes = var1;
   }

   public void setNumECBytes(int var1) {
      this.numECBytes = var1;
   }

   public void setNumRSBlocks(int var1) {
      this.numRSBlocks = var1;
   }

   public void setNumTotalBytes(int var1) {
      this.numTotalBytes = var1;
   }

   public void setVersion(int var1) {
      this.version = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append("<<\n");
      var1.append(" mode: ");
      var1.append(this.mode);
      var1.append("\n ecLevel: ");
      var1.append(this.ecLevel);
      var1.append("\n version: ");
      var1.append(this.version);
      var1.append("\n matrixWidth: ");
      var1.append(this.matrixWidth);
      var1.append("\n maskPattern: ");
      var1.append(this.maskPattern);
      var1.append("\n numTotalBytes: ");
      var1.append(this.numTotalBytes);
      var1.append("\n numDataBytes: ");
      var1.append(this.numDataBytes);
      var1.append("\n numECBytes: ");
      var1.append(this.numECBytes);
      var1.append("\n numRSBlocks: ");
      var1.append(this.numRSBlocks);
      if(this.matrix == null) {
         var1.append("\n matrix: null\n");
      } else {
         var1.append("\n matrix:\n");
         var1.append(this.matrix.toString());
      }

      var1.append(">>\n");
      return var1.toString();
   }
}
