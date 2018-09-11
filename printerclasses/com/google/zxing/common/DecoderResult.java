package com.google.zxing.common;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Vector;

public final class DecoderResult {
   private final Vector byteSegments;
   private final ErrorCorrectionLevel ecLevel;
   private final byte[] rawBytes;
   private final String text;

   public DecoderResult(byte[] var1, String var2, Vector var3, ErrorCorrectionLevel var4) {
      if(var1 == null && var2 == null) {
         throw new IllegalArgumentException();
      } else {
         this.rawBytes = var1;
         this.text = var2;
         this.byteSegments = var3;
         this.ecLevel = var4;
      }
   }

   public Vector getByteSegments() {
      return this.byteSegments;
   }

   public ErrorCorrectionLevel getECLevel() {
      return this.ecLevel;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public String getText() {
      return this.text;
   }
}
