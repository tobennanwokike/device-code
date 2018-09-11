package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

final class DecodedBitStreamParser {
   private static final int ANSIX12_ENCODE = 4;
   private static final int ASCII_ENCODE = 1;
   private static final int BASE256_ENCODE = 6;
   private static final char[] C40_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
   private static final int C40_ENCODE = 2;
   private static final char[] C40_SHIFT2_SET_CHARS = new char[]{'!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_'};
   private static final int EDIFACT_ENCODE = 5;
   private static final int PAD_ENCODE = 0;
   private static final char[] TEXT_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
   private static final int TEXT_ENCODE = 3;
   private static final char[] TEXT_SHIFT3_SET_CHARS = new char[]{'\'', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', '\u007f'};

   static DecoderResult decode(byte[] var0) throws FormatException {
      BitSource var2 = new BitSource(var0);
      StringBuffer var4 = new StringBuffer(100);
      StringBuffer var5 = new StringBuffer(0);
      Vector var3 = new Vector(1);
      int var1 = 1;

      do {
         if(var1 == 1) {
            var1 = decodeAsciiSegment(var2, var4, var5);
         } else {
            switch(var1) {
            case 2:
               decodeC40Segment(var2, var4);
               break;
            case 3:
               decodeTextSegment(var2, var4);
               break;
            case 4:
               decodeAnsiX12Segment(var2, var4);
               break;
            case 5:
               decodeEdifactSegment(var2, var4);
               break;
            case 6:
               decodeBase256Segment(var2, var4, var3);
               break;
            default:
               throw FormatException.getFormatInstance();
            }

            var1 = 1;
         }
      } while(var1 != 0 && var2.available() > 0);

      if(var5.length() > 0) {
         var4.append(var5.toString());
      }

      String var7 = var4.toString();
      Vector var6 = var3;
      if(var3.isEmpty()) {
         var6 = null;
      }

      return new DecoderResult(var0, var7, var6, (ErrorCorrectionLevel)null);
   }

   private static void decodeAnsiX12Segment(BitSource var0, StringBuffer var1) throws FormatException {
      int[] var4 = new int[3];

      while(var0.available() != 8) {
         int var2 = var0.readBits(8);
         if(var2 == 254) {
            break;
         }

         parseTwoBytes(var2, var0.readBits(8), var4);

         for(var2 = 0; var2 < 3; ++var2) {
            int var3 = var4[var2];
            if(var3 == 0) {
               var1.append('\r');
            } else if(var3 == 1) {
               var1.append('*');
            } else if(var3 == 2) {
               var1.append('>');
            } else if(var3 == 3) {
               var1.append(' ');
            } else if(var3 < 14) {
               var1.append((char)(var3 + 44));
            } else {
               if(var3 >= 40) {
                  throw FormatException.getFormatInstance();
               }

               var1.append((char)(var3 + 51));
            }
         }

         if(var0.available() <= 0) {
            break;
         }
      }

   }

   private static int decodeAsciiSegment(BitSource var0, StringBuffer var1, StringBuffer var2) throws FormatException {
      byte var5 = 1;
      boolean var3 = false;

      byte var7;
      while(true) {
         int var6 = var0.readBits(8);
         if(var6 == 0) {
            throw FormatException.getFormatInstance();
         }

         if(var6 <= 128) {
            int var9;
            if(var3) {
               var9 = var6 + 128;
            } else {
               var9 = var6;
            }

            var1.append((char)(var9 - 1));
            var7 = var5;
            break;
         }

         if(var6 == 129) {
            var7 = 0;
            break;
         }

         boolean var8;
         if(var6 <= 229) {
            int var4 = var6 - 130;
            if(var4 < 10) {
               var1.append('0');
            }

            var1.append(var4);
            var8 = var3;
         } else {
            if(var6 == 230) {
               var7 = 2;
               break;
            }

            if(var6 == 231) {
               var7 = 6;
               break;
            }

            var8 = var3;
            if(var6 != 232) {
               var8 = var3;
               if(var6 != 233) {
                  var8 = var3;
                  if(var6 != 234) {
                     if(var6 == 235) {
                        var8 = true;
                     } else if(var6 == 236) {
                        var1.append("[)>\u001e05\u001d");
                        var2.insert(0, "\u001e\u0004");
                        var8 = var3;
                     } else if(var6 == 237) {
                        var1.append("[)>\u001e06\u001d");
                        var2.insert(0, "\u001e\u0004");
                        var8 = var3;
                     } else {
                        if(var6 == 238) {
                           var7 = 4;
                           break;
                        }

                        if(var6 == 239) {
                           var7 = 3;
                           break;
                        }

                        if(var6 == 240) {
                           var7 = 5;
                           break;
                        }

                        var8 = var3;
                        if(var6 != 241) {
                           var8 = var3;
                           if(var6 >= 242) {
                              throw FormatException.getFormatInstance();
                           }
                        }
                     }
                  }
               }
            }
         }

         var3 = var8;
         if(var0.available() <= 0) {
            var7 = var5;
            break;
         }
      }

      return var7;
   }

   private static void decodeBase256Segment(BitSource var0, StringBuffer var1, Vector var2) throws FormatException {
      int var4 = var0.readBits(8);
      int var3;
      if(var4 == 0) {
         var3 = var0.available() / 8;
      } else {
         var3 = var4;
         if(var4 >= 250) {
            var3 = (var4 - 249) * 250 + var0.readBits(8);
         }
      }

      byte[] var5 = new byte[var3];

      for(var4 = 0; var4 < var3; ++var4) {
         if(var0.available() < 8) {
            throw FormatException.getFormatInstance();
         }

         var5[var4] = unrandomize255State(var0.readBits(8), var4);
      }

      var2.addElement(var5);

      try {
         String var7 = new String(var5, "ISO8859_1");
         var1.append(var7);
      } catch (UnsupportedEncodingException var6) {
         throw new RuntimeException("Platform does not support required encoding: " + var6);
      }
   }

   private static void decodeC40Segment(BitSource var0, StringBuffer var1) throws FormatException {
      int[] var6 = new int[3];
      boolean var2 = false;

      while(var0.available() != 8) {
         int var3 = var0.readBits(8);
         if(var3 == 254) {
            break;
         }

         parseTwoBytes(var3, var0.readBits(8), var6);
         int var4 = 0;

         int var5;
         for(var3 = 0; var4 < 3; var4 = var5) {
            var5 = var6[var4];
            int var7;
            boolean var8;
            switch(var3) {
            case 0:
               if(var5 < 3) {
                  ++var5;
                  var8 = var2;
                  var7 = var5;
               } else if(var2) {
                  var1.append((char)(C40_BASIC_SET_CHARS[var5] + 128));
                  var7 = var3;
                  var8 = false;
               } else {
                  var1.append(C40_BASIC_SET_CHARS[var5]);
                  var7 = var3;
                  var8 = var2;
               }
               break;
            case 1:
               if(var2) {
                  var1.append((char)(var5 + 128));
                  var2 = false;
               } else {
                  var1.append(var5);
               }

               var8 = var2;
               var7 = 0;
               break;
            case 2:
               if(var5 < 27) {
                  if(var2) {
                     var1.append((char)(C40_SHIFT2_SET_CHARS[var5] + 128));
                     var2 = false;
                  } else {
                     var1.append(C40_SHIFT2_SET_CHARS[var5]);
                  }
               } else {
                  if(var5 == 27) {
                     throw FormatException.getFormatInstance();
                  }

                  if(var5 != 30) {
                     throw FormatException.getFormatInstance();
                  }

                  var2 = true;
               }

               var8 = var2;
               var7 = 0;
               break;
            case 3:
               if(var2) {
                  var1.append((char)(var5 + 224));
                  var2 = false;
               } else {
                  var1.append((char)(var5 + 96));
               }

               var8 = var2;
               var7 = 0;
               break;
            default:
               throw FormatException.getFormatInstance();
            }

            var5 = var4 + 1;
            var3 = var7;
            var2 = var8;
         }

         if(var0.available() <= 0) {
            break;
         }
      }

   }

   private static void decodeEdifactSegment(BitSource var0, StringBuffer var1) {
      boolean var2 = false;

      while(var0.available() > 16) {
         for(int var3 = 0; var3 < 4; ++var3) {
            int var4 = var0.readBits(6);
            if(var4 == 11111) {
               var2 = true;
            }

            if(!var2) {
               if((var4 & 32) == 0) {
                  var4 |= 64;
               }

               var1.append(var4);
            }
         }

         if(var2 || var0.available() <= 0) {
            break;
         }
      }

   }

   private static void decodeTextSegment(BitSource var0, StringBuffer var1) throws FormatException {
      int[] var6 = new int[3];
      boolean var2 = false;

      while(var0.available() != 8) {
         int var3 = var0.readBits(8);
         if(var3 == 254) {
            break;
         }

         parseTwoBytes(var3, var0.readBits(8), var6);
         int var4 = 0;

         int var5;
         for(var3 = 0; var4 < 3; var4 = var5) {
            var5 = var6[var4];
            int var7;
            boolean var8;
            switch(var3) {
            case 0:
               if(var5 < 3) {
                  ++var5;
                  var8 = var2;
                  var7 = var5;
               } else if(var2) {
                  var1.append((char)(TEXT_BASIC_SET_CHARS[var5] + 128));
                  var7 = var3;
                  var8 = false;
               } else {
                  var1.append(TEXT_BASIC_SET_CHARS[var5]);
                  var7 = var3;
                  var8 = var2;
               }
               break;
            case 1:
               if(var2) {
                  var1.append((char)(var5 + 128));
                  var2 = false;
               } else {
                  var1.append(var5);
               }

               var8 = var2;
               var7 = 0;
               break;
            case 2:
               if(var5 < 27) {
                  if(var2) {
                     var1.append((char)(C40_SHIFT2_SET_CHARS[var5] + 128));
                     var2 = false;
                  } else {
                     var1.append(C40_SHIFT2_SET_CHARS[var5]);
                  }
               } else {
                  if(var5 == 27) {
                     throw FormatException.getFormatInstance();
                  }

                  if(var5 != 30) {
                     throw FormatException.getFormatInstance();
                  }

                  var2 = true;
               }

               var8 = var2;
               var7 = 0;
               break;
            case 3:
               if(var2) {
                  var1.append((char)(TEXT_SHIFT3_SET_CHARS[var5] + 128));
                  var2 = false;
               } else {
                  var1.append(TEXT_SHIFT3_SET_CHARS[var5]);
               }

               var8 = var2;
               var7 = 0;
               break;
            default:
               throw FormatException.getFormatInstance();
            }

            var5 = var4 + 1;
            var2 = var8;
            var3 = var7;
         }

         if(var0.available() <= 0) {
            break;
         }
      }

   }

   private static void parseTwoBytes(int var0, int var1, int[] var2) {
      var1 = (var0 << 8) + var1 - 1;
      var0 = var1 / 1600;
      var2[0] = var0;
      var1 -= var0 * 1600;
      var0 = var1 / 40;
      var2[1] = var0;
      var2[2] = var1 - var0 * 40;
   }

   private static byte unrandomize255State(int var0, int var1) {
      var0 -= var1 * 149 % 255 + 1;
      if(var0 < 0) {
         var0 += 256;
      }

      return (byte)var0;
   }
}
