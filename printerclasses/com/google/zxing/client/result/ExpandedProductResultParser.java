package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import java.util.Hashtable;

final class ExpandedProductResultParser extends ResultParser {
   private static String findAIvalue(int var0, String var1) {
      StringBuffer var3 = new StringBuffer();
      if(var1.charAt(var0) != 40) {
         var1 = "ERROR";
      } else {
         var1 = var1.substring(var0 + 1);
         var0 = 0;

         while(true) {
            if(var0 >= var1.length()) {
               var1 = var3.toString();
               break;
            }

            char var2 = var1.charAt(var0);
            switch(var2) {
            case ')':
               var1 = var3.toString();
               return var1;
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            default:
               var1 = "ERROR";
               return var1;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
               var3.append(var2);
               ++var0;
            }
         }
      }

      return var1;
   }

   private static String findValue(int var0, String var1) {
      StringBuffer var3 = new StringBuffer();
      var1 = var1.substring(var0);

      for(var0 = 0; var0 < var1.length(); ++var0) {
         char var2 = var1.charAt(var0);
         if(var2 == 40) {
            if(!"ERROR".equals(findAIvalue(var0, var1))) {
               break;
            }

            var3.append('(');
         } else {
            var3.append(var2);
         }
      }

      return var3.toString();
   }

   public static ExpandedProductParsedResult parse(Result var0) {
      BarcodeFormat var3 = var0.getBarcodeFormat();
      ExpandedProductParsedResult var19;
      if(!BarcodeFormat.RSS_EXPANDED.equals(var3)) {
         var19 = null;
      } else {
         String var16 = var0.getText();
         if(var16 == null) {
            var19 = null;
         } else {
            String var15 = "-";
            String var14 = "-";
            String var13 = "-";
            String var11 = "-";
            String var10 = "-";
            String var9 = "-";
            String var8 = "-";
            String var12 = "-";
            String var21 = "-";
            String var4 = "-";
            String var5 = "-";
            String var6 = "-";
            String var7 = "-";
            Hashtable var17 = new Hashtable();
            int var1 = 0;

            while(true) {
               if(var1 >= var16.length()) {
                  var19 = new ExpandedProductParsedResult(var15, var14, var13, var11, var10, var9, var8, var12, var21, var4, var5, var6, var7, var17);
                  break;
               }

               String var18 = findAIvalue(var1, var16);
               if("ERROR".equals(var18)) {
                  var19 = null;
                  break;
               }

               int var2 = var18.length() + 2 + var1;
               String var20 = findValue(var2, var16);
               var1 = var20.length();
               if("00".equals(var18)) {
                  var14 = var20;
                  var20 = var12;
               } else if("01".equals(var18)) {
                  var15 = var20;
                  var20 = var12;
               } else if("10".equals(var18)) {
                  var13 = var20;
                  var20 = var12;
               } else if("11".equals(var18)) {
                  var11 = var20;
                  var20 = var12;
               } else if("13".equals(var18)) {
                  var10 = var20;
                  var20 = var12;
               } else if("15".equals(var18)) {
                  var9 = var20;
                  var20 = var12;
               } else if("17".equals(var18)) {
                  var8 = var20;
                  var20 = var12;
               } else if(!"3100".equals(var18) && !"3101".equals(var18) && !"3102".equals(var18) && !"3103".equals(var18) && !"3104".equals(var18) && !"3105".equals(var18) && !"3106".equals(var18) && !"3107".equals(var18) && !"3108".equals(var18) && !"3109".equals(var18)) {
                  if(!"3200".equals(var18) && !"3201".equals(var18) && !"3202".equals(var18) && !"3203".equals(var18) && !"3204".equals(var18) && !"3205".equals(var18) && !"3206".equals(var18) && !"3207".equals(var18) && !"3208".equals(var18) && !"3209".equals(var18)) {
                     if(!"3920".equals(var18) && !"3921".equals(var18) && !"3922".equals(var18) && !"3923".equals(var18)) {
                        if(!"3930".equals(var18) && !"3931".equals(var18) && !"3932".equals(var18) && !"3933".equals(var18)) {
                           var17.put(var18, var20);
                           var20 = var12;
                        } else {
                           if(var20.length() < 4) {
                              var19 = null;
                              break;
                           }

                           var5 = var20.substring(3);
                           var7 = var20.substring(0, 3);
                           var6 = var18.substring(3);
                           var20 = var12;
                        }
                     } else {
                        var6 = var18.substring(3);
                        var5 = var20;
                        var20 = var12;
                     }
                  } else {
                     var21 = "LB";
                     var4 = var18.substring(3);
                  }
               } else {
                  var21 = "KG";
                  var4 = var18.substring(3);
               }

               var1 += var2;
               var12 = var20;
            }
         }
      }

      return var19;
   }
}
