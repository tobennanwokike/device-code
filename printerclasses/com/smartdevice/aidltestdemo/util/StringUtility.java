package com.smartdevice.aidltestdemo.util;

import android.text.TextUtils;

public class StringUtility {
   public static String ByteArrayToString(byte[] var0, int var1) {
      String var3 = new String();

      for(int var2 = 0; var2 < var1; ++var2) {
         var3 = var3 + String.format("%02X ", new Object[]{Byte.valueOf(var0[var2])});
      }

      return var3;
   }

   protected static boolean CheckByte(byte var0) {
      boolean var2 = true;
      boolean var1;
      if(var0 <= 57 && var0 >= 48) {
         var1 = var2;
      } else {
         if(var0 <= 70) {
            var1 = var2;
            if(var0 >= 65) {
               return var1;
            }
         }

         if(var0 <= 102) {
            var1 = var2;
            if(var0 >= 97) {
               return var1;
            }
         }

         var1 = false;
      }

      return var1;
   }

   protected static boolean CheckString(String var0) {
      boolean var3 = false;
      var0 = var0.trim();
      boolean var2;
      if(var0.length() != 2) {
         var2 = var3;
      } else {
         byte[] var4 = var0.getBytes();
         int var1 = 0;

         while(true) {
            if(var1 >= 2) {
               var2 = true;
               break;
            }

            var2 = var3;
            if(!CheckByte(var4[var1])) {
               break;
            }

            ++var1;
         }
      }

      return var2;
   }

   protected static byte StringToByte(String var0) {
      byte[] var2 = var0.getBytes();

      for(int var1 = 0; var1 < 2; ++var1) {
         if(var2[var1] <= 57 && var2[var1] >= 48) {
            var2[var1] = (byte)(var2[var1] - 48);
         } else if(var2[var1] <= 70 && var2[var1] >= 65) {
            var2[var1] = (byte)(var2[var1] - 55);
         } else if(var2[var1] <= 102 && var2[var1] >= 97) {
            var2[var1] = (byte)(var2[var1] - 87);
         }
      }

      return (byte)(var2[0] << 4 | var2[1] & 15);
   }

   public static byte[] StringToByteArray(String var0) {
      int var2 = var0.length() / 2;
      byte[] var3 = new byte[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = Integer.valueOf(var0.substring(var1 * 2, var1 * 2 + 2), 16).byteValue();
      }

      return var3;
   }

   public static String bytesToHexString(byte[] var0) {
      return bytesToHexString(var0, true);
   }

   public static String bytesToHexString(byte[] var0, boolean var1) {
      StringBuilder var3 = new StringBuilder();
      if(var1) {
         var3.append("0x");
      }

      String var5;
      if(var0 != null && var0.length > 0) {
         char[] var4 = new char[2];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var4[0] = Character.toUpperCase(Character.forDigit(var0[var2] >>> 4 & 15, 16));
            var4[1] = Character.toUpperCase(Character.forDigit(var0[var2] & 15, 16));
            System.out.println(var4);
            var3.append(var4);
            var3.append("  ");
         }

         var5 = var3.toString();
      } else {
         var5 = null;
      }

      return var5;
   }

   public static String getStringFormat(byte[] var0) {
      String var4 = "";
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         byte var1 = var0[var2];
         var4 = var4 + String.format("%02X ", new Object[]{Byte.valueOf(var1)});
      }

      return var4;
   }

   public static boolean isEmpty(String var0) {
      return TextUtils.isEmpty(var0);
   }

   public static String[] spiltStrings(String var0, String var1) {
      return var0.split(var1);
   }
}
