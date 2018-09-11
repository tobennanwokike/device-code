package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookAUResultParser;
import com.google.zxing.client.result.AddressBookDoCoMoResultParser;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.BizcardResultParser;
import com.google.zxing.client.result.BookmarkDoCoMoResultParser;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.EmailAddressResultParser;
import com.google.zxing.client.result.EmailDoCoMoResultParser;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ExpandedProductResultParser;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.GeoResultParser;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ISBNResultParser;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ProductResultParser;
import com.google.zxing.client.result.SMSMMSResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.SMSTOMMSTOResultParser;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.TelResultParser;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.URIResultParser;
import com.google.zxing.client.result.URLTOResultParser;
import com.google.zxing.client.result.VCardResultParser;
import com.google.zxing.client.result.VEventResultParser;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.client.result.WifiResultParser;
import java.util.Hashtable;
import java.util.Vector;

public abstract class ResultParser {
   private static void appendKeyValue(String var0, int var1, int var2, Hashtable var3) {
      int var4 = var0.indexOf(61, var1);
      if(var4 >= 0) {
         var3.put(var0.substring(var1, var4), urlDecode(var0.substring(var4 + 1, var2)));
      }

   }

   private static int findFirstEscape(char[] var0) {
      int var3 = var0.length;
      int var1 = 0;

      int var2;
      while(true) {
         if(var1 >= var3) {
            var2 = -1;
            break;
         }

         char var4 = var0[var1];
         var2 = var1;
         if(var4 == 43) {
            break;
         }

         if(var4 == 37) {
            var2 = var1;
            break;
         }

         ++var1;
      }

      return var2;
   }

   protected static boolean isStringOfDigits(String var0, int var1) {
      boolean var5 = false;
      boolean var4;
      if(var0 == null) {
         var4 = var5;
      } else {
         var4 = var5;
         if(var1 == var0.length()) {
            int var2 = 0;

            while(true) {
               if(var2 >= var1) {
                  var4 = true;
                  break;
               }

               char var3 = var0.charAt(var2);
               var4 = var5;
               if(var3 < 48) {
                  break;
               }

               var4 = var5;
               if(var3 > 57) {
                  break;
               }

               ++var2;
            }
         }
      }

      return var4;
   }

   protected static boolean isSubstringOfDigits(String var0, int var1, int var2) {
      boolean var5 = false;
      boolean var4;
      if(var0 == null) {
         var4 = var5;
      } else {
         int var3 = var0.length();
         var2 += var1;
         var4 = var5;
         if(var3 >= var2) {
            while(true) {
               if(var1 >= var2) {
                  var4 = true;
                  break;
               }

               char var6 = var0.charAt(var1);
               var4 = var5;
               if(var6 < 48) {
                  break;
               }

               var4 = var5;
               if(var6 > 57) {
                  break;
               }

               ++var1;
            }
         }
      }

      return var4;
   }

   static String[] matchPrefixedField(String var0, String var1, char var2, boolean var3) {
      int var7 = var1.length();
      int var4 = 0;
      Vector var8 = null;

      while(var4 < var7) {
         var4 = var1.indexOf(var0, var4);
         if(var4 < 0) {
            break;
         }

         int var6 = var4 + var0.length();
         boolean var5 = false;
         var4 = var6;

         while(!var5) {
            var4 = var1.indexOf(var2, var4);
            if(var4 < 0) {
               var4 = var1.length();
               var5 = true;
            } else if(var1.charAt(var4 - 1) == 92) {
               ++var4;
            } else {
               Vector var9 = var8;
               if(var8 == null) {
                  var9 = new Vector(3);
               }

               String var10 = unescapeBackslash(var1.substring(var6, var4));
               String var12 = var10;
               if(var3) {
                  var12 = var10.trim();
               }

               var9.addElement(var12);
               ++var4;
               var5 = true;
               var8 = var9;
            }
         }
      }

      String[] var11;
      if(var8 != null && !var8.isEmpty()) {
         var11 = toStringArray(var8);
      } else {
         var11 = null;
      }

      return var11;
   }

   static String matchSinglePrefixedField(String var0, String var1, char var2, boolean var3) {
      String[] var4 = matchPrefixedField(var0, var1, var2, var3);
      if(var4 == null) {
         var0 = null;
      } else {
         var0 = var4[0];
      }

      return var0;
   }

   protected static void maybeAppend(String var0, StringBuffer var1) {
      if(var0 != null) {
         var1.append('\n');
         var1.append(var0);
      }

   }

   protected static void maybeAppend(String[] var0, StringBuffer var1) {
      if(var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append('\n');
            var1.append(var0[var2]);
         }
      }

   }

   protected static String[] maybeWrap(String var0) {
      String[] var2;
      if(var0 == null) {
         var2 = null;
      } else {
         String[] var1 = new String[]{var0};
         var2 = var1;
      }

      return var2;
   }

   private static int parseHexDigit(char var0) {
      int var1;
      if(var0 >= 97) {
         if(var0 <= 102) {
            var1 = var0 - 97 + 10;
            return var1;
         }
      } else if(var0 >= 65) {
         if(var0 <= 70) {
            var1 = var0 - 65 + 10;
            return var1;
         }
      } else if(var0 >= 48 && var0 <= 57) {
         var1 = var0 - 48;
         return var1;
      }

      var1 = -1;
      return var1;
   }

   static Hashtable parseNameValuePairs(String var0) {
      int var1 = var0.indexOf(63);
      Hashtable var4;
      if(var1 < 0) {
         var4 = null;
      } else {
         Hashtable var3 = new Hashtable(3);
         ++var1;

         while(true) {
            int var2 = var0.indexOf(38, var1);
            if(var2 < 0) {
               appendKeyValue(var0, var1, var0.length(), var3);
               var4 = var3;
               break;
            }

            appendKeyValue(var0, var1, var2, var3);
            var1 = var2 + 1;
         }
      }

      return var4;
   }

   public static ParsedResult parseResult(Result var0) {
      Object var1 = BookmarkDoCoMoResultParser.parse(var0);
      if(var1 == null) {
         AddressBookParsedResult var2 = AddressBookDoCoMoResultParser.parse(var0);
         var1 = var2;
         if(var2 == null) {
            EmailAddressParsedResult var3 = EmailDoCoMoResultParser.parse(var0);
            var1 = var3;
            if(var3 == null) {
               var2 = AddressBookAUResultParser.parse(var0);
               var1 = var2;
               if(var2 == null) {
                  var2 = VCardResultParser.parse(var0);
                  var1 = var2;
                  if(var2 == null) {
                     var2 = BizcardResultParser.parse(var0);
                     var1 = var2;
                     if(var2 == null) {
                        CalendarParsedResult var4 = VEventResultParser.parse(var0);
                        var1 = var4;
                        if(var4 == null) {
                           var3 = EmailAddressResultParser.parse(var0);
                           var1 = var3;
                           if(var3 == null) {
                              TelParsedResult var5 = TelResultParser.parse(var0);
                              var1 = var5;
                              if(var5 == null) {
                                 SMSParsedResult var6 = SMSMMSResultParser.parse(var0);
                                 var1 = var6;
                                 if(var6 == null) {
                                    var6 = SMSTOMMSTOResultParser.parse(var0);
                                    var1 = var6;
                                    if(var6 == null) {
                                       GeoParsedResult var7 = GeoResultParser.parse(var0);
                                       var1 = var7;
                                       if(var7 == null) {
                                          WifiParsedResult var8 = WifiResultParser.parse(var0);
                                          var1 = var8;
                                          if(var8 == null) {
                                             URIParsedResult var9 = URLTOResultParser.parse(var0);
                                             var1 = var9;
                                             if(var9 == null) {
                                                var9 = URIResultParser.parse(var0);
                                                var1 = var9;
                                                if(var9 == null) {
                                                   ISBNParsedResult var10 = ISBNResultParser.parse(var0);
                                                   var1 = var10;
                                                   if(var10 == null) {
                                                      ProductParsedResult var11 = ProductResultParser.parse(var0);
                                                      var1 = var11;
                                                      if(var11 == null) {
                                                         ExpandedProductParsedResult var12 = ExpandedProductResultParser.parse(var0);
                                                         var1 = var12;
                                                         if(var12 == null) {
                                                            var1 = new TextParsedResult(var0.getText(), (String)null);
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return (ParsedResult)var1;
   }

   static String[] toStringArray(Vector var0) {
      int var2 = var0.size();
      String[] var3 = new String[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = (String)var0.elementAt(var1);
      }

      return var3;
   }

   protected static String unescapeBackslash(String var0) {
      String var5 = var0;
      if(var0 != null) {
         int var3 = var0.indexOf(92);
         var5 = var0;
         if(var3 >= 0) {
            int var4 = var0.length();
            StringBuffer var6 = new StringBuffer(var4 - 1);
            var6.append(var0.toCharArray(), 0, var3);

            for(boolean var2 = false; var3 < var4; ++var3) {
               char var1 = var0.charAt(var3);
               if(!var2 && var1 == 92) {
                  var2 = true;
               } else {
                  var6.append(var1);
                  var2 = false;
               }
            }

            var5 = var6.toString();
         }
      }

      return var5;
   }

   private static String urlDecode(String var0) {
      if(var0 == null) {
         var0 = null;
      } else {
         char[] var6 = var0.toCharArray();
         int var2 = findFirstEscape(var6);
         if(var2 >= 0) {
            int var3 = var6.length;
            StringBuffer var7 = new StringBuffer(var3 - 2);
            var7.append(var6, 0, var2);

            for(; var2 < var3; ++var2) {
               char var1 = var6[var2];
               if(var1 == 43) {
                  var7.append(' ');
               } else if(var1 == 37) {
                  if(var2 >= var3 - 2) {
                     var7.append('%');
                  } else {
                     ++var2;
                     int var4 = parseHexDigit(var6[var2]);
                     ++var2;
                     int var5 = parseHexDigit(var6[var2]);
                     if(var4 < 0 || var5 < 0) {
                        var7.append('%');
                        var7.append(var6[var2 - 1]);
                        var7.append(var6[var2]);
                     }

                     var7.append((char)((var4 << 4) + var5));
                  }
               } else {
                  var7.append(var1);
               }
            }

            var0 = var7.toString();
         }
      }

      return var0;
   }
}
