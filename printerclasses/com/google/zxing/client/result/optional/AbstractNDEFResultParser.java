package com.google.zxing.client.result.optional;

import com.google.zxing.client.result.ResultParser;
import java.io.UnsupportedEncodingException;

abstract class AbstractNDEFResultParser extends ResultParser {
   static String bytesToString(byte[] var0, int var1, int var2, String var3) {
      try {
         String var5 = new String(var0, var1, var2, var3);
         return var5;
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException("Platform does not support required encoding: " + var4);
      }
   }
}
