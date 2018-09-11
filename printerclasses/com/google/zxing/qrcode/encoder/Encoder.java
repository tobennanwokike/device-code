package com.google.zxing.qrcode.encoder;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.ECI;
import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.BlockPair;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.MaskUtil;
import com.google.zxing.qrcode.encoder.MatrixUtil;
import com.google.zxing.qrcode.encoder.QRCode;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

public final class Encoder {
   private static final int[] ALPHANUMERIC_TABLE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
   static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";

   static void append8BitBytes(String var0, BitArray var1, String var2) throws WriterException {
      byte[] var5;
      try {
         var5 = var0.getBytes(var2);
      } catch (UnsupportedEncodingException var4) {
         throw new WriterException(var4.toString());
      }

      for(int var3 = 0; var3 < var5.length; ++var3) {
         var1.appendBits(var5[var3], 8);
      }

   }

   static void appendAlphanumericBytes(String var0, BitArray var1) throws WriterException {
      int var3 = var0.length();
      int var2 = 0;

      while(var2 < var3) {
         int var4 = getAlphanumericCode(var0.charAt(var2));
         if(var4 == -1) {
            throw new WriterException();
         }

         if(var2 + 1 < var3) {
            int var5 = getAlphanumericCode(var0.charAt(var2 + 1));
            if(var5 == -1) {
               throw new WriterException();
            }

            var1.appendBits(var4 * 45 + var5, 11);
            var2 += 2;
         } else {
            var1.appendBits(var4, 6);
            ++var2;
         }
      }

   }

   static void appendBytes(String var0, Mode var1, BitArray var2, String var3) throws WriterException {
      if(var1.equals(Mode.NUMERIC)) {
         appendNumericBytes(var0, var2);
      } else if(var1.equals(Mode.ALPHANUMERIC)) {
         appendAlphanumericBytes(var0, var2);
      } else if(var1.equals(Mode.BYTE)) {
         append8BitBytes(var0, var2, var3);
      } else {
         if(!var1.equals(Mode.KANJI)) {
            throw new WriterException("Invalid mode: " + var1);
         }

         appendKanjiBytes(var0, var2);
      }

   }

   private static void appendECI(ECI var0, BitArray var1) {
      var1.appendBits(Mode.ECI.getBits(), 4);
      var1.appendBits(var0.getValue(), 8);
   }

   static void appendKanjiBytes(String var0, BitArray var1) throws WriterException {
      byte[] var6;
      try {
         var6 = var0.getBytes("Shift_JIS");
      } catch (UnsupportedEncodingException var5) {
         throw new WriterException(var5.toString());
      }

      int var4 = var6.length;

      for(int var3 = 0; var3 < var4; var3 += 2) {
         int var2 = (var6[var3] & 255) << 8 | var6[var3 + 1] & 255;
         if(var2 >= '腀' && var2 <= '\u9ffc') {
            var2 -= '腀';
         } else if(var2 >= '\ue040' && var2 <= '\uebbf') {
            var2 -= '셀';
         } else {
            var2 = -1;
         }

         if(var2 == -1) {
            throw new WriterException("Invalid byte sequence");
         }

         var1.appendBits((var2 & 255) + (var2 >> 8) * 192, 13);
      }

   }

   static void appendLengthInfo(int var0, int var1, Mode var2, BitArray var3) throws WriterException {
      var1 = var2.getCharacterCountBits(Version.getVersionForNumber(var1));
      if(var0 > (1 << var1) - 1) {
         throw new WriterException(var0 + "is bigger than" + ((1 << var1) - 1));
      } else {
         var3.appendBits(var0, var1);
      }
   }

   static void appendModeInfo(Mode var0, BitArray var1) {
      var1.appendBits(var0.getBits(), 4);
   }

   static void appendNumericBytes(String var0, BitArray var1) {
      int var3 = var0.length();
      int var2 = 0;

      while(var2 < var3) {
         int var4 = var0.charAt(var2) - 48;
         if(var2 + 2 < var3) {
            var1.appendBits(var4 * 100 + (var0.charAt(var2 + 1) - 48) * 10 + (var0.charAt(var2 + 2) - 48), 10);
            var2 += 3;
         } else if(var2 + 1 < var3) {
            var1.appendBits(var4 * 10 + (var0.charAt(var2 + 1) - 48), 7);
            var2 += 2;
         } else {
            var1.appendBits(var4, 4);
            ++var2;
         }
      }

   }

   private static int calculateMaskPenalty(ByteMatrix var0) {
      return 0 + MaskUtil.applyMaskPenaltyRule1(var0) + MaskUtil.applyMaskPenaltyRule2(var0) + MaskUtil.applyMaskPenaltyRule3(var0) + MaskUtil.applyMaskPenaltyRule4(var0);
   }

   private static int chooseMaskPattern(BitArray var0, ErrorCorrectionLevel var1, int var2, ByteMatrix var3) throws WriterException {
      int var5 = Integer.MAX_VALUE;
      int var6 = -1;

      for(int var4 = 0; var4 < 8; ++var4) {
         MatrixUtil.buildMatrix(var0, var1, var2, var4, var3);
         int var7 = calculateMaskPenalty(var3);
         if(var7 < var5) {
            var6 = var4;
            var5 = var7;
         }
      }

      return var6;
   }

   public static Mode chooseMode(String var0) {
      return chooseMode(var0, (String)null);
   }

   public static Mode chooseMode(String var0, String var1) {
      int var2 = 0;
      Mode var6;
      if("Shift_JIS".equals(var1)) {
         if(isOnlyDoubleByteKanji(var0)) {
            var6 = Mode.KANJI;
         } else {
            var6 = Mode.BYTE;
         }
      } else {
         boolean var4 = false;
         boolean var3 = false;

         while(true) {
            if(var2 >= var0.length()) {
               if(var4) {
                  var6 = Mode.ALPHANUMERIC;
               } else if(var3) {
                  var6 = Mode.NUMERIC;
               } else {
                  var6 = Mode.BYTE;
               }
               break;
            }

            char var5 = var0.charAt(var2);
            if(var5 >= 48 && var5 <= 57) {
               var3 = true;
            } else {
               if(getAlphanumericCode(var5) == -1) {
                  var6 = Mode.BYTE;
                  break;
               }

               var4 = true;
            }

            ++var2;
         }
      }

      return var6;
   }

   public static void encode(String var0, ErrorCorrectionLevel var1, QRCode var2) throws WriterException {
      encode(var0, var1, (Hashtable)null, var2);
   }

   public static void encode(String var0, ErrorCorrectionLevel var1, Hashtable var2, QRCode var3) throws WriterException {
      String var10;
      if(var2 == null) {
         var10 = null;
      } else {
         var10 = (String)var2.get(EncodeHintType.CHARACTER_SET);
      }

      String var5 = var10;
      if(var10 == null) {
         var5 = "ISO-8859-1";
      }

      Mode var6 = chooseMode(var0, var5);
      BitArray var11 = new BitArray();
      appendBytes(var0, var6, var11, var5);
      initQRCode(var11.getSizeInBytes(), var1, var6, var3);
      BitArray var8 = new BitArray();
      if(var6 == Mode.BYTE && !"ISO-8859-1".equals(var5)) {
         CharacterSetECI var12 = CharacterSetECI.getCharacterSetECIByName(var5);
         if(var12 != null) {
            appendECI(var12, var8);
         }
      }

      appendModeInfo(var6, var8);
      int var4;
      if(var6.equals(Mode.BYTE)) {
         var4 = var11.getSizeInBytes();
      } else {
         var4 = var0.length();
      }

      appendLengthInfo(var4, var3.getVersion(), var6, var8);
      var8.appendBitArray(var11);
      terminateBits(var3.getNumDataBytes(), var8);
      BitArray var7 = new BitArray();
      interleaveWithECBytes(var8, var3.getNumTotalBytes(), var3.getNumDataBytes(), var3.getNumRSBlocks(), var7);
      ByteMatrix var9 = new ByteMatrix(var3.getMatrixWidth(), var3.getMatrixWidth());
      var3.setMaskPattern(chooseMaskPattern(var7, var3.getECLevel(), var3.getVersion(), var9));
      MatrixUtil.buildMatrix(var7, var3.getECLevel(), var3.getVersion(), var3.getMaskPattern(), var9);
      var3.setMatrix(var9);
      if(!var3.isValid()) {
         throw new WriterException("Invalid QR code: " + var3.toString());
      }
   }

   static byte[] generateECBytes(byte[] var0, int var1) {
      byte var3 = 0;
      int var4 = var0.length;
      int[] var5 = new int[var4 + var1];

      int var2;
      for(var2 = 0; var2 < var4; ++var2) {
         var5[var2] = var0[var2] & 255;
      }

      (new ReedSolomonEncoder(GF256.QR_CODE_FIELD)).encode(var5, var1);
      var0 = new byte[var1];

      for(var2 = var3; var2 < var1; ++var2) {
         var0[var2] = (byte)var5[var4 + var2];
      }

      return var0;
   }

   static int getAlphanumericCode(int var0) {
      if(var0 < ALPHANUMERIC_TABLE.length) {
         var0 = ALPHANUMERIC_TABLE[var0];
      } else {
         var0 = -1;
      }

      return var0;
   }

   static void getNumDataBytesAndNumECBytesForBlockID(int var0, int var1, int var2, int var3, int[] var4, int[] var5) throws WriterException {
      if(var3 >= var2) {
         throw new WriterException("Block ID too large");
      } else {
         int var7 = var0 % var2;
         int var6 = var2 - var7;
         int var10 = var0 / var2;
         int var9 = var1 / var2;
         int var8 = var9 + 1;
         var1 = var10 - var9;
         var10 = var10 + 1 - var8;
         if(var1 != var10) {
            throw new WriterException("EC bytes mismatch");
         } else if(var2 != var6 + var7) {
            throw new WriterException("RS blocks mismatch");
         } else if(var0 != var7 * (var8 + var10) + (var9 + var1) * var6) {
            throw new WriterException("Total bytes mismatch");
         } else {
            if(var3 < var6) {
               var4[0] = var9;
               var5[0] = var1;
            } else {
               var4[0] = var8;
               var5[0] = var10;
            }

         }
      }
   }

   private static void initQRCode(int var0, ErrorCorrectionLevel var1, Mode var2, QRCode var3) throws WriterException {
      var3.setECLevel(var1);
      var3.setMode(var2);

      for(int var4 = 1; var4 <= 40; ++var4) {
         Version var10 = Version.getVersionForNumber(var4);
         int var8 = var10.getTotalCodewords();
         Version.ECBlocks var9 = var10.getECBlocksForLevel(var1);
         int var5 = var9.getTotalECCodewords();
         int var6 = var9.getNumBlocks();
         int var7 = var8 - var5;
         if(var7 >= var0 + 3) {
            var3.setVersion(var4);
            var3.setNumTotalBytes(var8);
            var3.setNumDataBytes(var7);
            var3.setNumRSBlocks(var6);
            var3.setNumECBytes(var5);
            var3.setMatrixWidth(var10.getDimensionForVersion());
            return;
         }
      }

      throw new WriterException("Cannot find proper rs block info (input data too big?)");
   }

   static void interleaveWithECBytes(BitArray var0, int var1, int var2, int var3, BitArray var4) throws WriterException {
      if(var0.getSizeInBytes() != var2) {
         throw new WriterException("Number of bits and data bytes does not match");
      } else {
         Vector var10 = new Vector(var3);
         int var8 = 0;
         int var5 = 0;
         int var7 = 0;

         int var6;
         int var9;
         for(var6 = 0; var8 < var3; var6 += var9) {
            int[] var11 = new int[1];
            int[] var13 = new int[1];
            getNumDataBytesAndNumECBytesForBlockID(var1, var2, var3, var8, var11, var13);
            var9 = var11[0];
            byte[] var12 = new byte[var9];
            var0.toBytes(var6 * 8, var12, 0, var9);
            byte[] var15 = generateECBytes(var12, var13[0]);
            var10.addElement(new BlockPair(var12, var15));
            var7 = Math.max(var7, var9);
            var5 = Math.max(var5, var15.length);
            var9 = var11[0];
            ++var8;
         }

         if(var2 != var6) {
            throw new WriterException("Data bytes does not match offset");
         } else {
            byte[] var14;
            for(var2 = 0; var2 < var7; ++var2) {
               for(var3 = 0; var3 < var10.size(); ++var3) {
                  var14 = ((BlockPair)var10.elementAt(var3)).getDataBytes();
                  if(var2 < var14.length) {
                     var4.appendBits(var14[var2], 8);
                  }
               }
            }

            for(var2 = 0; var2 < var5; ++var2) {
               for(var3 = 0; var3 < var10.size(); ++var3) {
                  var14 = ((BlockPair)var10.elementAt(var3)).getErrorCorrectionBytes();
                  if(var2 < var14.length) {
                     var4.appendBits(var14[var2], 8);
                  }
               }
            }

            if(var1 != var4.getSizeInBytes()) {
               throw new WriterException("Interleaving error: " + var1 + " and " + var4.getSizeInBytes() + " differ.");
            }
         }
      }
   }

   private static boolean isOnlyDoubleByteKanji(String var0) {
      boolean var5 = false;

      boolean var4;
      byte[] var7;
      try {
         var7 = var0.getBytes("Shift_JIS");
      } catch (UnsupportedEncodingException var6) {
         var4 = var5;
         return var4;
      }

      int var2 = var7.length;
      if(var2 % 2 != 0) {
         var4 = var5;
      } else {
         int var1 = 0;

         while(true) {
            if(var1 >= var2) {
               var4 = true;
               break;
            }

            int var3 = var7[var1] & 255;
            if(var3 < 129 || var3 > 159) {
               var4 = var5;
               if(var3 < 224) {
                  break;
               }

               var4 = var5;
               if(var3 > 235) {
                  break;
               }
            }

            var1 += 2;
         }
      }

      return var4;
   }

   static void terminateBits(int var0, BitArray var1) throws WriterException {
      int var4 = var0 << 3;
      if(var1.getSize() > var4) {
         throw new WriterException("data bits cannot fit in the QR Code" + var1.getSize() + " > " + var4);
      } else {
         int var2;
         for(var2 = 0; var2 < 4 && var1.getSize() < var4; ++var2) {
            var1.appendBit(false);
         }

         var2 = var1.getSize() & 7;
         if(var2 > 0) {
            while(var2 < 8) {
               var1.appendBit(false);
               ++var2;
            }
         }

         int var5 = var1.getSizeInBytes();

         for(var2 = 0; var2 < var0 - var5; ++var2) {
            short var3;
            if((var2 & 1) == 0) {
               var3 = 236;
            } else {
               var3 = 17;
            }

            var1.appendBits(var3, 8);
         }

         if(var1.getSize() != var4) {
            throw new WriterException("Bits size does not equal capacity");
         }
      }
   }
}
