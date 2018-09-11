package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

final class DecodedBitStreamParser {
   private static final char[] ALPHANUMERIC_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '$', '%', '*', '+', '-', '.', '/', ':'};

   static DecoderResult decode(byte[] var0, Version var1, ErrorCorrectionLevel var2, Hashtable var3) throws FormatException {
      Object var8 = null;
      BitSource var11 = new BitSource(var0);
      StringBuffer var10 = new StringBuffer(50);
      boolean var5 = false;
      Vector var9 = new Vector(1);
      CharacterSetECI var6 = null;

      Mode var7;
      do {
         if(var11.available() < 4) {
            var7 = Mode.TERMINATOR;
         } else {
            try {
               var7 = Mode.forBits(var11.readBits(4));
            } catch (IllegalArgumentException var12) {
               throw FormatException.getFormatInstance();
            }
         }

         if(!var7.equals(Mode.TERMINATOR)) {
            if(!var7.equals(Mode.FNC1_FIRST_POSITION) && !var7.equals(Mode.FNC1_SECOND_POSITION)) {
               if(var7.equals(Mode.STRUCTURED_APPEND)) {
                  var11.readBits(16);
               } else if(var7.equals(Mode.ECI)) {
                  var6 = CharacterSetECI.getCharacterSetECIByValue(parseECIValue(var11));
                  if(var6 == null) {
                     throw FormatException.getFormatInstance();
                  }
               } else {
                  int var4 = var11.readBits(var7.getCharacterCountBits(var1));
                  if(var7.equals(Mode.NUMERIC)) {
                     decodeNumericSegment(var11, var10, var4);
                  } else if(var7.equals(Mode.ALPHANUMERIC)) {
                     decodeAlphanumericSegment(var11, var10, var4, var5);
                  } else if(var7.equals(Mode.BYTE)) {
                     decodeByteSegment(var11, var10, var4, var6, var9, var3);
                  } else {
                     if(!var7.equals(Mode.KANJI)) {
                        throw FormatException.getFormatInstance();
                     }

                     decodeKanjiSegment(var11, var10, var4);
                  }
               }
            } else {
               var5 = true;
            }
         }
      } while(!var7.equals(Mode.TERMINATOR));

      String var14 = var10.toString();
      Vector var13;
      if(var9.isEmpty()) {
         var13 = (Vector)var8;
      } else {
         var13 = var9;
      }

      return new DecoderResult(var0, var14, var13, var2);
   }

   private static void decodeAlphanumericSegment(BitSource var0, StringBuffer var1, int var2, boolean var3) throws FormatException {
      int var4;
      for(var4 = var1.length(); var2 > 1; var2 -= 2) {
         int var5 = var0.readBits(11);
         var1.append(toAlphaNumericChar(var5 / 45));
         var1.append(toAlphaNumericChar(var5 % 45));
      }

      if(var2 == 1) {
         var1.append(toAlphaNumericChar(var0.readBits(6)));
      }

      if(var3) {
         for(var2 = var4; var2 < var1.length(); ++var2) {
            if(var1.charAt(var2) == 37) {
               if(var2 < var1.length() - 1 && var1.charAt(var2 + 1) == 37) {
                  var1.deleteCharAt(var2 + 1);
               } else {
                  var1.setCharAt(var2, '\u001d');
               }
            }
         }
      }

   }

   private static void decodeByteSegment(BitSource var0, StringBuffer var1, int var2, CharacterSetECI var3, Vector var4, Hashtable var5) throws FormatException {
      byte[] var7 = new byte[var2];
      if(var2 << 3 > var0.available()) {
         throw FormatException.getFormatInstance();
      } else {
         for(int var6 = 0; var6 < var2; ++var6) {
            var7[var6] = (byte)var0.readBits(8);
         }

         String var9;
         if(var3 == null) {
            var9 = StringUtils.guessEncoding(var7, var5);
         } else {
            var9 = var3.getEncodingName();
         }

         try {
            String var10 = new String(var7, var9);
            var1.append(var10);
         } catch (UnsupportedEncodingException var8) {
            throw FormatException.getFormatInstance();
         }

         var4.addElement(var7);
      }
   }

   private static void decodeKanjiSegment(BitSource var0, StringBuffer var1, int var2) throws FormatException {
      byte[] var5 = new byte[var2 * 2];
      byte var4 = 0;
      int var3 = var2;

      for(var2 = var4; var3 > 0; var2 += 2) {
         int var8 = var0.readBits(13);
         var8 = var8 % 192 | var8 / 192 << 8;
         if(var8 < 7936) {
            var8 += '腀';
         } else {
            var8 += '셀';
         }

         var5[var2] = (byte)(var8 >> 8);
         var5[var2 + 1] = (byte)var8;
         --var3;
      }

      try {
         String var7 = new String(var5, "SJIS");
         var1.append(var7);
      } catch (UnsupportedEncodingException var6) {
         throw FormatException.getFormatInstance();
      }
   }

   private static void decodeNumericSegment(BitSource var0, StringBuffer var1, int var2) throws FormatException {
      while(var2 >= 3) {
         int var3 = var0.readBits(10);
         if(var3 >= 1000) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var3 / 100));
         var1.append(toAlphaNumericChar(var3 / 10 % 10));
         var1.append(toAlphaNumericChar(var3 % 10));
         var2 -= 3;
      }

      if(var2 == 2) {
         var2 = var0.readBits(7);
         if(var2 >= 100) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var2 / 10));
         var1.append(toAlphaNumericChar(var2 % 10));
      } else if(var2 == 1) {
         var2 = var0.readBits(4);
         if(var2 >= 10) {
            throw FormatException.getFormatInstance();
         }

         var1.append(toAlphaNumericChar(var2));
      }

   }

   private static int parseECIValue(BitSource var0) {
      int var1 = var0.readBits(8);
      if((var1 & 128) == 0) {
         var1 &= 127;
      } else if((var1 & 192) == 128) {
         var1 = (var1 & 63) << 8 | var0.readBits(8);
      } else {
         if((var1 & 224) != 192) {
            throw new IllegalArgumentException("Bad ECI bits starting with byte " + var1);
         }

         var1 = (var1 & 31) << 16 | var0.readBits(16);
      }

      return var1;
   }

   private static char toAlphaNumericChar(int var0) throws FormatException {
      if(var0 >= ALPHANUMERIC_CHARS.length) {
         throw FormatException.getFormatInstance();
      } else {
         return ALPHANUMERIC_CHARS[var0];
      }
   }
}
