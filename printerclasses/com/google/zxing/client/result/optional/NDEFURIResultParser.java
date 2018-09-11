package com.google.zxing.client.result.optional;

import com.google.zxing.Result;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.optional.AbstractNDEFResultParser;
import com.google.zxing.client.result.optional.NDEFRecord;

final class NDEFURIResultParser extends AbstractNDEFResultParser {
   private static final String[] URI_PREFIXES = new String[]{null, "http://www.", "https://www.", "http://", "https://", "tel:", "mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://", "dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://", "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:", "urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:"};

   static String decodeURIPayload(byte[] var0) {
      int var1 = var0[0] & 255;
      String var2 = null;
      if(var1 < URI_PREFIXES.length) {
         var2 = URI_PREFIXES[var1];
      }

      String var3 = bytesToString(var0, 1, var0.length - 1, "UTF8");
      if(var2 != null) {
         var3 = var2 + var3;
      }

      return var3;
   }

   public static URIParsedResult parse(Result var0) {
      Object var1 = null;
      byte[] var3 = var0.getRawBytes();
      URIParsedResult var4;
      if(var3 == null) {
         var4 = (URIParsedResult)var1;
      } else {
         NDEFRecord var2 = NDEFRecord.readRecord(var3, 0);
         var4 = (URIParsedResult)var1;
         if(var2 != null) {
            var4 = (URIParsedResult)var1;
            if(var2.isMessageBegin()) {
               var4 = (URIParsedResult)var1;
               if(var2.isMessageEnd()) {
                  var4 = (URIParsedResult)var1;
                  if(var2.getType().equals("U")) {
                     var4 = new URIParsedResult(decodeURIPayload(var2.getPayload()), (String)null);
                  }
               }
            }
         }
      }

      return var4;
   }
}
