package com.google.zxing.client.result.optional;

import com.google.zxing.Result;
import com.google.zxing.client.result.optional.AbstractNDEFResultParser;
import com.google.zxing.client.result.optional.NDEFRecord;
import com.google.zxing.client.result.optional.NDEFSmartPosterParsedResult;
import com.google.zxing.client.result.optional.NDEFTextResultParser;
import com.google.zxing.client.result.optional.NDEFURIResultParser;

final class NDEFSmartPosterResultParser extends AbstractNDEFResultParser {
   public static NDEFSmartPosterParsedResult parse(Result var0) {
      Object var8 = null;
      byte[] var11 = var0.getRawBytes();
      NDEFSmartPosterParsedResult var6;
      if(var11 == null) {
         var6 = (NDEFSmartPosterParsedResult)var8;
      } else {
         NDEFRecord var12 = NDEFRecord.readRecord(var11, 0);
         var6 = (NDEFSmartPosterParsedResult)var8;
         if(var12 != null) {
            var6 = (NDEFSmartPosterParsedResult)var8;
            if(var12.isMessageBegin()) {
               var6 = (NDEFSmartPosterParsedResult)var8;
               if(var12.isMessageEnd()) {
                  var6 = (NDEFSmartPosterParsedResult)var8;
                  if(var12.getType().equals("Sp")) {
                     byte[] var9 = var12.getPayload();
                     byte var3 = -1;
                     String var5 = null;
                     String var4 = null;
                     var12 = null;
                     int var2 = 0;
                     int var1 = 0;

                     NDEFRecord var7;
                     while(true) {
                        var7 = var12;
                        if(var1 >= var9.length) {
                           break;
                        }

                        var12 = NDEFRecord.readRecord(var9, var1);
                        var7 = var12;
                        if(var12 == null) {
                           break;
                        }

                        if(var2 == 0) {
                           var6 = (NDEFSmartPosterParsedResult)var8;
                           if(!var12.isMessageBegin()) {
                              return var6;
                           }
                        }

                        String var10 = var12.getType();
                        String var13;
                        String var14;
                        if("T".equals(var10)) {
                           var14 = NDEFTextResultParser.decodeTextPayload(var12.getPayload())[1];
                           var13 = var5;
                        } else if("U".equals(var10)) {
                           var13 = NDEFURIResultParser.decodeURIPayload(var12.getPayload());
                           var14 = var4;
                        } else {
                           var13 = var5;
                           var14 = var4;
                           if("act".equals(var10)) {
                              var3 = var12.getPayload()[0];
                              var13 = var5;
                              var14 = var4;
                           }
                        }

                        ++var2;
                        var1 += var12.getTotalRecordLength();
                        var5 = var13;
                        var4 = var14;
                     }

                     var6 = (NDEFSmartPosterParsedResult)var8;
                     if(var2 != 0) {
                        if(var7 != null) {
                           var6 = (NDEFSmartPosterParsedResult)var8;
                           if(!var7.isMessageEnd()) {
                              return var6;
                           }
                        }

                        var6 = new NDEFSmartPosterParsedResult(var3, var5, var4);
                     }
                  }
               }
            }
         }
      }

      return var6;
   }
}
