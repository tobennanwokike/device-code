package com.google.zxing.common;

import com.google.zxing.DecodeHintType;
import java.util.Hashtable;

public final class StringUtils {
   private static final boolean ASSUME_SHIFT_JIS;
   private static final String EUC_JP = "EUC_JP";
   private static final String ISO88591 = "ISO8859_1";
   private static final String PLATFORM_DEFAULT_ENCODING = System.getProperty("file.encoding");
   public static final String SHIFT_JIS = "SJIS";
   private static final String UTF8 = "UTF8";

   static {
      boolean var0;
      if(!"SJIS".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING) && !"EUC_JP".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING)) {
         var0 = false;
      } else {
         var0 = true;
      }

      ASSUME_SHIFT_JIS = var0;
   }

   public static String guessEncoding(byte[] var0, Hashtable var1) {
      String var17;
      if(var1 != null) {
         String var18 = (String)var1.get(DecodeHintType.CHARACTER_SET);
         if(var18 != null) {
            var17 = var18;
            return var17;
         }
      }

      if(var0.length > 3 && var0[0] == -17 && var0[1] == -69 && var0[2] == -65) {
         var17 = "UTF8";
      } else {
         int var16 = var0.length;
         int var7 = 0;
         int var12 = 0;
         boolean var11 = false;
         boolean var14 = false;
         int var8 = 0;
         boolean var10 = false;
         int var2 = 0;
         boolean var3 = true;
         boolean var4 = true;

         int var6;
         boolean var13;
         for(var13 = true; var8 < var16 && (var13 || var4 || var3); var7 = var6) {
            int var15 = var0[var8] & 255;
            boolean var5;
            boolean var9;
            if(var15 >= 128 && var15 <= 191) {
               if(var7 > 0) {
                  var6 = var7 - 1;
                  var5 = var3;
                  var9 = var14;
               } else {
                  var9 = var14;
                  var6 = var7;
                  var5 = var3;
               }
            } else {
               if(var7 > 0) {
                  var3 = false;
               }

               var9 = var14;
               var6 = var7;
               var5 = var3;
               if(var15 >= 192) {
                  var9 = var14;
                  var6 = var7;
                  var5 = var3;
                  if(var15 <= 253) {
                     int var22 = var7;

                     for(var6 = var15; (var6 & 64) != 0; ++var22) {
                        var6 <<= 1;
                     }

                     var9 = true;
                     var6 = var22;
                     var5 = var3;
                  }
               }
            }

            int var20;
            boolean var23;
            label209: {
               if(var15 != 194) {
                  var23 = var11;
                  if(var15 != 195) {
                     break label209;
                  }
               }

               var23 = var11;
               if(var8 < var16 - 1) {
                  var20 = var0[var8 + 1] & 255;
                  var23 = var11;
                  if(var20 <= 191) {
                     label230: {
                        if(var15 != 194 || var20 < 160) {
                           var23 = var11;
                           if(var15 != 195) {
                              break label230;
                           }

                           var23 = var11;
                           if(var20 < 128) {
                              break label230;
                           }
                        }

                        var23 = true;
                     }
                  }
               }
            }

            var11 = var13;
            if(var15 >= 127) {
               var11 = var13;
               if(var15 <= 159) {
                  var11 = false;
               }
            }

            int var24 = var12;
            if(var15 >= 161) {
               var24 = var12;
               if(var15 <= 223) {
                  var24 = var12;
                  if(!var10) {
                     var24 = var12 + 1;
                  }
               }
            }

            if(var10 || (var15 < 240 || var15 > 255) && var15 != 128 && var15 != 160) {
               var3 = var4;
            } else {
               var3 = false;
            }

            boolean var19;
            if((var15 < 129 || var15 > 159) && (var15 < 224 || var15 > 239)) {
               var4 = var3;
               var20 = var2;
               var19 = false;
            } else if(var10) {
               var10 = false;
               var4 = var3;
               var20 = var2;
               var19 = var10;
            } else if(var8 >= var0.length - 1) {
               var4 = false;
               var20 = var2;
               var19 = true;
            } else {
               int var21 = var0[var8 + 1] & 255;
               if(var21 >= 64 && var21 <= 252) {
                  ++var2;
               } else {
                  var3 = false;
               }

               var4 = var3;
               var20 = var2;
               var19 = true;
            }

            ++var8;
            var10 = var19;
            var2 = var20;
            var3 = var5;
            var12 = var24;
            var13 = var11;
            var11 = var23;
            var14 = var9;
         }

         if(var7 > 0) {
            var3 = false;
         }

         if(var4 && ASSUME_SHIFT_JIS) {
            var17 = "SJIS";
         } else if(var3 && var14) {
            var17 = "UTF8";
         } else if(var4 && (var2 >= 3 || var12 * 20 > var16)) {
            var17 = "SJIS";
         } else if(!var11 && var13) {
            var17 = "ISO8859_1";
         } else {
            var17 = PLATFORM_DEFAULT_ENCODING;
         }
      }

      return var17;
   }
}
