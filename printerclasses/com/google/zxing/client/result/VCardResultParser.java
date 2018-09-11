package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ResultParser;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

final class VCardResultParser extends ResultParser {
   private static String decodeQuotedPrintable(String var0, String var1) {
      int var6 = var0.length();
      StringBuffer var7 = new StringBuffer(var6);
      ByteArrayOutputStream var8 = new ByteArrayOutputStream();

      int var5;
      for(int var4 = 0; var4 < var6; var4 = var5 + 1) {
         char var2 = var0.charAt(var4);
         var5 = var4;
         switch(var2) {
         case '\n':
         case '\r':
            break;
         case '=':
            var5 = var4;
            if(var4 < var6 - 2) {
               char var3 = var0.charAt(var4 + 1);
               var5 = var4;
               if(var3 != 13) {
                  var5 = var4;
                  if(var3 != 10) {
                     var2 = var0.charAt(var4 + 2);

                     try {
                        var8.write(toHexValue(var3) * 16 + toHexValue(var2));
                     } catch (IllegalArgumentException var10) {
                        ;
                     }

                     var5 = var4 + 2;
                  }
               }
            }
            break;
         default:
            maybeAppendFragment(var8, var1, var7);
            var7.append(var2);
            var5 = var4;
         }
      }

      maybeAppendFragment(var8, var1, var7);
      return var7.toString();
   }

   private static String formatAddress(String var0) {
      if(var0 == null) {
         var0 = null;
      } else {
         int var3 = var0.length();
         StringBuffer var4 = new StringBuffer(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            char var1 = var0.charAt(var2);
            if(var1 == 59) {
               var4.append(' ');
            } else {
               var4.append(var1);
            }
         }

         var0 = var4.toString().trim();
      }

      return var0;
   }

   private static void formatNames(String[] var0) {
      if(var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            String var6 = var0[var1];
            String[] var5 = new String[5];
            int var2 = 0;
            int var3 = 0;

            while(true) {
               int var4 = var6.indexOf(59, var3);
               if(var4 <= 0) {
                  var5[var2] = var6.substring(var3);
                  StringBuffer var7 = new StringBuffer(100);
                  maybeAppendComponent(var5, 3, var7);
                  maybeAppendComponent(var5, 1, var7);
                  maybeAppendComponent(var5, 2, var7);
                  maybeAppendComponent(var5, 0, var7);
                  maybeAppendComponent(var5, 4, var7);
                  var0[var1] = var7.toString().trim();
                  break;
               }

               var5[var2] = var6.substring(var3, var4);
               ++var2;
               var3 = var4 + 1;
            }
         }
      }

   }

   private static boolean isLikeVCardDate(String var0) {
      boolean var2 = true;
      boolean var1;
      if(var0 == null) {
         var1 = var2;
      } else {
         var1 = var2;
         if(!isStringOfDigits(var0, 8)) {
            if(var0.length() == 10 && var0.charAt(4) == 45 && var0.charAt(7) == 45 && isSubstringOfDigits(var0, 0, 4) && isSubstringOfDigits(var0, 5, 2)) {
               var1 = var2;
               if(isSubstringOfDigits(var0, 8, 2)) {
                  return var1;
               }
            }

            var1 = false;
         }
      }

      return var1;
   }

   static String matchSingleVCardPrefixedField(String var0, String var1, boolean var2) {
      String[] var3 = matchVCardPrefixedField(var0, var1, var2);
      if(var3 == null) {
         var0 = null;
      } else {
         var0 = var3[0];
      }

      return var0;
   }

   private static String[] matchVCardPrefixedField(String var0, String var1, boolean var2) {
      Vector var10 = null;
      int var3 = 0;
      int var9 = var1.length();

      while(var3 < var9) {
         var3 = var1.indexOf(var0, var3);
         if(var3 < 0) {
            break;
         }

         if(var3 > 0 && var1.charAt(var3 - 1) != 10) {
            ++var3;
         } else {
            int var4 = var0.length() + var3;
            if(var1.charAt(var4) != 58) {
               var3 = var4;
               if(var1.charAt(var4) != 59) {
                  continue;
               }
            }

            int var5;
            for(var5 = var4; var1.charAt(var5) != 58; ++var5) {
               ;
            }

            boolean var6 = false;
            boolean var15 = false;
            String var12;
            String var13;
            if(var5 > var4) {
               int var16 = var4 + 1;
               int var7 = var4;
               String var11 = null;
               var4 = var16;

               while(true) {
                  var12 = var11;
                  var6 = var15;
                  if(var4 > var5) {
                     break;
                  }

                  int var8;
                  label133: {
                     if(var1.charAt(var4) != 59) {
                        var12 = var11;
                        var8 = var7;
                        var6 = var15;
                        if(var1.charAt(var4) != 58) {
                           break label133;
                        }
                     }

                     var12 = var1.substring(var7 + 1, var4);
                     var16 = var12.indexOf(61);
                     if(var16 >= 0) {
                        var13 = var12.substring(0, var16);
                        var12 = var12.substring(var16 + 1);
                        if(var13.equalsIgnoreCase("ENCODING")) {
                           if(var12.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                              var15 = true;
                           }
                        } else if(var13.equalsIgnoreCase("CHARSET")) {
                           var11 = var12;
                        }
                     }

                     var8 = var4;
                     var6 = var15;
                     var12 = var11;
                  }

                  ++var4;
                  var11 = var12;
                  var7 = var8;
                  var15 = var6;
               }
            } else {
               var12 = null;
            }

            var4 = var5 + 1;
            var3 = var4;

            while(true) {
               var5 = var1.indexOf(10, var3);
               if(var5 < 0) {
                  break;
               }

               if(var5 >= var1.length() - 1 || var1.charAt(var5 + 1) != 32 && var1.charAt(var5 + 1) != 9) {
                  if(!var6 || var1.charAt(var5 - 1) != 61 && var1.charAt(var5 - 2) != 61) {
                     break;
                  }

                  var3 = var5 + 1;
               } else {
                  var3 = var5 + 2;
               }
            }

            if(var5 < 0) {
               var3 = var9;
            } else if(var5 > var4) {
               Vector var18 = var10;
               if(var10 == null) {
                  var18 = new Vector(1);
               }

               var3 = var5;
               if(var1.charAt(var5 - 1) == 13) {
                  var3 = var5 - 1;
               }

               var13 = var1.substring(var4, var3);
               String var17 = var13;
               if(var2) {
                  var17 = var13.trim();
               }

               if(var6) {
                  var17 = decodeQuotedPrintable(var17, var12);
               } else {
                  var17 = stripContinuationCRLF(var17);
               }

               var18.addElement(var17);
               var10 = var18;
               ++var3;
            } else {
               var3 = var5 + 1;
            }
         }
      }

      String[] var14;
      if(var10 != null && !var10.isEmpty()) {
         var14 = toStringArray(var10);
      } else {
         var14 = null;
      }

      return var14;
   }

   private static void maybeAppendComponent(String[] var0, int var1, StringBuffer var2) {
      if(var0[var1] != null) {
         var2.append(' ');
         var2.append(var0[var1]);
      }

   }

   private static void maybeAppendFragment(ByteArrayOutputStream var0, String var1, StringBuffer var2) {
      if(var0.size() > 0) {
         byte[] var4 = var0.toByteArray();
         if(var1 == null) {
            var1 = new String(var4);
         } else {
            label23: {
               String var3;
               try {
                  var3 = new String(var4, var1);
               } catch (UnsupportedEncodingException var5) {
                  var1 = new String(var4);
                  break label23;
               }

               var1 = var3;
            }
         }

         var0.reset();
         var2.append(var1);
      }

   }

   public static AddressBookParsedResult parse(Result var0) {
      int var1 = 0;
      String var4 = var0.getText();
      AddressBookParsedResult var10;
      if(var4 != null && var4.startsWith("BEGIN:VCARD")) {
         String[] var2 = matchVCardPrefixedField("FN", var4, true);
         String[] var11 = var2;
         if(var2 == null) {
            var11 = matchVCardPrefixedField("N", var4, true);
            formatNames(var11);
         }

         String[] var9 = matchVCardPrefixedField("TEL", var4, true);
         String[] var6 = matchVCardPrefixedField("EMAIL", var4, true);
         String var5 = matchSingleVCardPrefixedField("NOTE", var4, false);
         String[] var7 = matchVCardPrefixedField("ADR", var4, true);
         if(var7 != null) {
            while(var1 < var7.length) {
               var7[var1] = formatAddress(var7[var1]);
               ++var1;
            }
         }

         String var8 = matchSingleVCardPrefixedField("ORG", var4, true);
         String var3 = matchSingleVCardPrefixedField("BDAY", var4, true);
         String var12 = var3;
         if(!isLikeVCardDate(var3)) {
            var12 = null;
         }

         var10 = new AddressBookParsedResult(var11, (String)null, var9, var6, var5, var7, var8, var12, matchSingleVCardPrefixedField("TITLE", var4, true), matchSingleVCardPrefixedField("URL", var4, true));
      } else {
         var10 = null;
      }

      return var10;
   }

   private static String stripContinuationCRLF(String var0) {
      int var4 = var0.length();
      StringBuffer var5 = new StringBuffer(var4);
      int var3 = 0;

      for(boolean var2 = false; var3 < var4; ++var3) {
         if(var2) {
            var2 = false;
         } else {
            char var1 = var0.charAt(var3);
            switch(var1) {
            case '\n':
               var2 = true;
               break;
            case '\u000b':
            case '\f':
            default:
               var5.append(var1);
               var2 = false;
               break;
            case '\r':
               var2 = false;
            }
         }
      }

      return var5.toString();
   }

   private static int toHexValue(char var0) {
      int var1;
      if(var0 >= 48 && var0 <= 57) {
         var1 = var0 - 48;
      } else if(var0 >= 65 && var0 <= 70) {
         var1 = var0 - 65 + 10;
      } else {
         if(var0 < 97 || var0 > 102) {
            throw new IllegalArgumentException();
         }

         var1 = var0 - 97 + 10;
      }

      return var1;
   }
}
