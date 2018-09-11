package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Vector;

final class DecodedBitStreamParser {
   private static final int AL = 28;
   private static final int ALPHA = 0;
   private static final int AS = 27;
   private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
   private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
   private static final int BYTE_COMPACTION_MODE_LATCH = 901;
   private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
   private static final String[] EXP900 = new String[]{"000000000000000000000000000000000000000000001", "000000000000000000000000000000000000000000900", "000000000000000000000000000000000000000810000", "000000000000000000000000000000000000729000000", "000000000000000000000000000000000656100000000", "000000000000000000000000000000590490000000000", "000000000000000000000000000531441000000000000", "000000000000000000000000478296900000000000000", "000000000000000000000430467210000000000000000", "000000000000000000387420489000000000000000000", "000000000000000348678440100000000000000000000", "000000000000313810596090000000000000000000000", "000000000282429536481000000000000000000000000", "000000254186582832900000000000000000000000000", "000228767924549610000000000000000000000000000", "205891132094649000000000000000000000000000000"};
   private static final int LL = 27;
   private static final int LOWER = 1;
   private static final int MACRO_PDF417_TERMINATOR = 922;
   private static final int MAX_NUMERIC_CODEWORDS = 15;
   private static final int MIXED = 2;
   private static final char[] MIXED_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', '\r', '\t', ',', ':', '#', '-', '.', '$', '/', '+', '%', '*', '=', '^'};
   private static final int ML = 28;
   private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
   private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
   private static final int PAL = 29;
   private static final int PL = 25;
   private static final int PS = 29;
   private static final int PUNCT = 3;
   private static final char[] PUNCT_CHARS = new char[]{';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', '\r', '\t', ',', ':', '\n', '-', '.', '$', '/', '\"', '|', '*', '(', ')', '?', '{', '}', '\''};
   private static final int PUNCT_SHIFT = 4;
   private static final int TEXT_COMPACTION_MODE_LATCH = 900;

   private static StringBuffer add(String var0, String var1) {
      StringBuffer var9 = new StringBuffer(5);
      StringBuffer var8 = new StringBuffer(5);
      StringBuffer var7 = new StringBuffer(var0.length());

      int var2;
      for(var2 = 0; var2 < var0.length(); ++var2) {
         var7.append('0');
      }

      var2 = var0.length() - 3;

      for(int var3 = 0; var2 > -1; var2 -= 3) {
         var9.setLength(0);
         var9.append(var0.charAt(var2));
         var9.append(var0.charAt(var2 + 1));
         var9.append(var0.charAt(var2 + 2));
         var8.setLength(0);
         var8.append(var1.charAt(var2));
         var8.append(var1.charAt(var2 + 1));
         var8.append(var1.charAt(var2 + 2));
         int var5 = Integer.parseInt(var9.toString());
         int var6 = Integer.parseInt(var8.toString());
         int var4 = (var5 + var6 + var3) % 1000;
         var3 = (var3 + var5 + var6) / 1000;
         var7.setCharAt(var2 + 2, (char)(var4 % 10 + 48));
         var7.setCharAt(var2 + 1, (char)(var4 / 10 % 10 + 48));
         var7.setCharAt(var2, (char)(var4 / 100 + 48));
      }

      return var7;
   }

   private static int byteCompaction(int var0, int[] var1, int var2, StringBuffer var3) {
      int var4;
      boolean var6;
      long var8;
      long var10;
      int var15;
      if(var0 == 901) {
         byte var14 = 0;
         var8 = 0L;
         char[] var13 = new char[6];
         int[] var12 = new int[6];
         boolean var5 = false;
         var4 = var2;
         var2 = var14;

         while(true) {
            int var7;
            do {
               do {
                  if(var4 >= var1[0] || var5) {
                     var0 = var2 / 5 * 5;

                     while(true) {
                        var15 = var4;
                        if(var0 >= var2) {
                           return var15;
                        }

                        var3.append((char)var12[var0]);
                        ++var0;
                     }
                  }

                  var0 = var4 + 1;
                  var4 = var1[var4];
                  if(var4 < 900) {
                     var12[var2] = var4;
                     var7 = var2 + 1;
                     var10 = var8 * 900L + (long)var4;
                     var6 = var5;
                  } else if(var4 != 900 && var4 != 901 && var4 != 902 && var4 != 924 && var4 != 928 && var4 != 923 && var4 != 922) {
                     var6 = var5;
                     var7 = var2;
                     var10 = var8;
                  } else {
                     --var0;
                     var6 = true;
                     var7 = var2;
                     var10 = var8;
                  }

                  var5 = var6;
                  var2 = var7;
                  var8 = var10;
                  var4 = var0;
               } while(var7 % 5 != 0);

               var5 = var6;
               var2 = var7;
               var8 = var10;
               var4 = var0;
            } while(var7 <= 0);

            var2 = 0;

            for(var8 = var10; var2 < 6; ++var2) {
               var13[5 - var2] = (char)((int)(var8 % 256L));
               var8 >>= 8;
            }

            var3.append(var13);
            var2 = 0;
            var5 = var6;
            var4 = var0;
         }
      } else {
         var15 = var2;
         if(var0 == 924) {
            var4 = 0;
            var10 = 0L;
            var6 = false;

            while(true) {
               boolean var16;
               do {
                  do {
                     var15 = var2;
                     if(var2 >= var1[0]) {
                        return var15;
                     }

                     var15 = var2;
                     if(var6) {
                        return var15;
                     }

                     var0 = var2 + 1;
                     var2 = var1[var2];
                     if(var2 < 900) {
                        var15 = var4 + 1;
                        var8 = var10 * 900L + (long)var2;
                        var16 = var6;
                     } else if(var2 != 900 && var2 != 901 && var2 != 902 && var2 != 924 && var2 != 928 && var2 != 923 && var2 != 922) {
                        var16 = var6;
                        var15 = var4;
                        var8 = var10;
                     } else {
                        --var0;
                        var16 = true;
                        var15 = var4;
                        var8 = var10;
                     }

                     var6 = var16;
                     var4 = var15;
                     var10 = var8;
                     var2 = var0;
                  } while(var15 % 5 != 0);

                  var6 = var16;
                  var4 = var15;
                  var10 = var8;
                  var2 = var0;
               } while(var15 <= 0);

               char[] var17 = new char[6];

               for(var2 = 0; var2 < 6; var8 >>= 8) {
                  var17[5 - var2] = (char)((int)(255L & var8));
                  ++var2;
               }

               var3.append(var17);
               var6 = var16;
               var4 = var15;
               var10 = var8;
               var2 = var0;
            }
         } else {
            return var15;
         }
      }
   }

   static DecoderResult decode(int[] var0) throws FormatException {
      StringBuffer var3 = new StringBuffer(100);
      int var2 = 2;

      for(int var1 = var0[1]; var2 < var0[0]; var1 = var0[var1]) {
         switch(var1) {
         case 900:
            var1 = textCompaction(var0, var2, var3);
            break;
         case 901:
            var1 = byteCompaction(var1, var0, var2, var3);
            break;
         case 902:
            var1 = numericCompaction(var0, var2, var3);
            break;
         case 913:
            var1 = byteCompaction(var1, var0, var2, var3);
            break;
         case 924:
            var1 = byteCompaction(var1, var0, var2, var3);
            break;
         default:
            var1 = textCompaction(var0, var2 - 1, var3);
         }

         if(var1 >= var0.length) {
            throw FormatException.getFormatInstance();
         }

         var2 = var1 + 1;
      }

      return new DecoderResult((byte[])null, var3.toString(), (Vector)null, (ErrorCorrectionLevel)null);
   }

   private static String decodeBase900toBase10(int[] var0, int var1) {
      int var2 = 0;

      StringBuffer var3;
      for(var3 = null; var2 < var1; ++var2) {
         StringBuffer var4 = multiply(EXP900[var1 - var2 - 1], var0[var2]);
         if(var3 == null) {
            var3 = var4;
         } else {
            var3 = add(var3.toString(), var4.toString());
         }
      }

      var1 = 0;

      String var5;
      while(true) {
         if(var1 >= var3.length()) {
            var5 = null;
            break;
         }

         if(var3.charAt(var1) == 49) {
            var5 = var3.toString().substring(var1 + 1);
            break;
         }

         ++var1;
      }

      String var6 = var5;
      if(var5 == null) {
         var6 = var3.toString();
      }

      return var6;
   }

   private static void decodeTextCompaction(int[] var0, int[] var1, int var2, StringBuffer var3) {
      int var7 = 0;
      byte var6 = 0;

      for(byte var5 = 0; var7 < var2; ++var7) {
         char var4;
         label110: {
            int var8 = var0[var7];
            byte var9;
            switch(var5) {
            case 0:
               if(var8 < 26) {
                  var4 = (char)(var8 + 65);
                  break label110;
               }

               if(var8 == 26) {
                  var4 = 32;
                  break label110;
               }

               if(var8 == 27) {
                  var5 = 1;
                  var4 = 0;
                  break label110;
               }

               if(var8 == 28) {
                  var5 = 2;
                  var4 = 0;
                  break label110;
               }

               if(var8 == 29) {
                  var4 = 0;
                  var9 = 4;
                  var6 = var5;
                  var5 = var9;
                  break label110;
               }

               if(var8 == 913) {
                  var3.append((char)var1[var7]);
                  var4 = 0;
                  break label110;
               }
               break;
            case 1:
               if(var8 < 26) {
                  var4 = (char)(var8 + 97);
                  break label110;
               }

               if(var8 == 26) {
                  var4 = 32;
                  break label110;
               }

               if(var8 == 28) {
                  var4 = 0;
                  var5 = 0;
                  break label110;
               }

               if(var8 == 28) {
                  var5 = 2;
                  var4 = 0;
                  break label110;
               }

               if(var8 == 29) {
                  var4 = 0;
                  var9 = 4;
                  var6 = var5;
                  var5 = var9;
                  break label110;
               }

               if(var8 == 913) {
                  var3.append((char)var1[var7]);
                  var4 = 0;
                  break label110;
               }
               break;
            case 2:
               if(var8 < 25) {
                  var4 = MIXED_CHARS[var8];
                  break label110;
               }

               if(var8 == 25) {
                  var5 = 3;
                  var4 = 0;
                  break label110;
               }

               if(var8 == 26) {
                  var4 = 32;
                  break label110;
               }

               if(var8 == 27) {
                  var4 = 0;
                  break label110;
               }

               if(var8 == 28) {
                  var4 = 0;
                  var5 = 0;
                  break label110;
               }

               if(var8 == 29) {
                  var4 = 0;
                  var9 = 4;
                  var6 = var5;
                  var5 = var9;
                  break label110;
               }

               if(var8 == 913) {
                  var3.append((char)var1[var7]);
                  var4 = 0;
                  break label110;
               }
               break;
            case 3:
               if(var8 < 29) {
                  var4 = PUNCT_CHARS[var8];
                  break label110;
               }

               if(var8 == 29) {
                  var4 = 0;
                  var5 = 0;
                  break label110;
               }

               if(var8 == 913) {
                  var3.append((char)var1[var7]);
                  var4 = 0;
                  break label110;
               }
               break;
            case 4:
               if(var8 < 29) {
                  var4 = PUNCT_CHARS[var8];
                  var5 = var6;
               } else if(var8 == 29) {
                  var4 = 0;
                  var5 = 0;
               } else {
                  var4 = 0;
                  var5 = var6;
               }
               break label110;
            }

            var4 = 0;
         }

         if(var4 != 0) {
            var3.append(var4);
         }
      }

   }

   private static StringBuffer multiply(String var0, int var1) {
      byte var3 = 0;
      StringBuffer var6 = new StringBuffer(var0.length());

      int var2;
      for(var2 = 0; var2 < var0.length(); ++var2) {
         var6.append('0');
      }

      int var4 = var1 / 100;
      int var5 = var1 / 10;

      for(var2 = 0; var2 < var1 % 10; ++var2) {
         var6 = add(var6.toString(), var0);
      }

      var2 = 0;

      while(true) {
         StringBuffer var7 = var6;
         var1 = var3;
         if(var2 >= var5 % 10) {
            while(var1 < var4) {
               var7 = add(var7.toString(), (var0 + "00").substring(2));
               ++var1;
            }

            return var7;
         }

         var6 = add(var6.toString(), (var0 + '0').substring(1));
         ++var2;
      }
   }

   private static int numericCompaction(int[] var0, int var1, StringBuffer var2) {
      int[] var8 = new int[15];
      boolean var5 = false;
      int var3 = 0;
      int var7 = var1;

      while(var7 < var0[0] && !var5) {
         var1 = var7 + 1;
         var7 = var0[var7];
         boolean var4 = var5;
         if(var1 == var0[0]) {
            var4 = true;
         }

         int var6;
         if(var7 < 900) {
            var8[var3] = var7;
            var6 = var3 + 1;
         } else if(var7 != 900 && var7 != 901 && var7 != 924 && var7 != 928 && var7 != 923 && var7 != 922) {
            var6 = var3;
         } else {
            --var1;
            var4 = true;
            var6 = var3;
         }

         if(var6 % 15 != 0 && var7 != 902) {
            var5 = var4;
            var3 = var6;
            var7 = var1;
            if(!var4) {
               continue;
            }
         }

         var2.append(decodeBase900toBase10(var8, var6));
         var3 = 0;
         var5 = var4;
         var7 = var1;
      }

      return var7;
   }

   private static int textCompaction(int[] var0, int var1, StringBuffer var2) {
      int[] var6 = new int[var0[0] << 1];
      int[] var7 = new int[var0[0] << 1];
      boolean var3 = false;
      int var4 = 0;

      while(var1 < var0[0] && !var3) {
         int var5 = var1 + 1;
         var1 = var0[var1];
         if(var1 < 900) {
            var6[var4] = var1 / 30;
            var6[var4 + 1] = var1 % 30;
            var4 += 2;
            var1 = var5;
         } else {
            switch(var1) {
            case 900:
               var1 = var5 - 1;
               var3 = true;
               break;
            case 901:
               var1 = var5 - 1;
               var3 = true;
               break;
            case 902:
               var1 = var5 - 1;
               var3 = true;
               break;
            case 913:
               var6[var4] = 913;
               var7[var4] = var1;
               ++var4;
               var1 = var5;
               break;
            case 924:
               var1 = var5 - 1;
               var3 = true;
               break;
            default:
               var1 = var5;
            }
         }
      }

      decodeTextCompaction(var6, var7, var4, var2);
      return var1;
   }
}
