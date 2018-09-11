package com.smartdevice.aidltestdemo.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
   public static String appendPath(String var0, String var1) {
      if(!TextUtils.isEmpty(var0)) {
         var1 = var0 + File.separator + var1;
      }

      return var1;
   }

   public static boolean checkExists(String var0) {
      return (new File(var0)).mkdir();
   }

   public static boolean closeStream(Closeable var0) {
      boolean var2 = true;
      boolean var1 = var2;
      if(var0 != null) {
         try {
            var0.close();
         } catch (IOException var3) {
            var1 = false;
            return var1;
         }

         var1 = var2;
      }

      return var1;
   }

   public static String convertCodeAndGetText(File param0) {
      // $FF: Couldn't be decompiled
   }

   public static void createDir(String var0) {
      File var1 = new File(var0);
      if(!var1.exists()) {
         var1.mkdirs();
      }

   }

   public static File createDirIfNotExist(String var0) {
      File var1 = new File(var0);
      if(!var1.isDirectory()) {
         var1.delete();
      }

      if(!var1.exists()) {
         var1.mkdirs();
      }

      return var1;
   }

   public static File createFileIfNotExist(File var0) {
      if(!var0.isFile()) {
         var0.delete();
      }

      if(!var0.exists()) {
         try {
            var0.createNewFile();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      return var0;
   }

   public static File createFileIfNotExist(String var0) {
      File var1 = new File(var0);
      createFileIfNotExist(var1);
      return var1;
   }

   public static void deleteAllFile(String var0) {
      File var4 = new File(var0);
      if(var4.exists()) {
         if(var4.isDirectory()) {
            File[] var5 = var4.listFiles();

            for(int var1 = 0; var1 < var5.length; ++var1) {
               deleteAllFile(var5[var1].getPath());
            }
         } else if(var4.isFile()) {
            try {
               var4.delete();
            } catch (Exception var3) {
               var4.deleteOnExit();
            }
         }
      }

   }

   public static boolean deleteFile(File param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean exists(String var0) {
      return (new File(var0)).exists();
   }

   public static String getContextPath(Context param0) {
      // $FF: Couldn't be decompiled
   }

   public static String getContextPath(Context var0, String var1) {
      return getContextPath(var0) + var1;
   }

   public static long getFileLength(String var0) {
      long var3 = 0L;
      long var1 = var3;
      if(var0 != null) {
         if(var0.trim().length() < 1) {
            var1 = var3;
         } else {
            try {
               File var5 = new File(var0);
               var1 = var5.length();
            } catch (Exception var6) {
               var1 = var3;
            }
         }
      }

      return var1;
   }

   public static String getFileName(String var0) {
      int var1 = var0.lastIndexOf(File.separator);
      if(var1 != -1) {
         var0 = var0.substring(var1 + 1);
      }

      return var0;
   }

   public static String getFileNameWithSuffix(String var0) {
      if(var0 != null && var0.trim().length() >= 1) {
         try {
            File var1 = new File(var0);
            var0 = var1.getName();
         } catch (Exception var2) {
            var0 = null;
         }
      } else {
         var0 = null;
      }

      return var0;
   }

   public static String getFileNameWithoutSuffix(String var0) {
      String var2;
      if(TextUtils.isEmpty(var0)) {
         var2 = "";
      } else {
         int var1 = var0.lastIndexOf(".");
         if(var1 != -1) {
            var2 = var0.substring(0, var1);
         } else {
            var2 = var0;
            if(var0.indexOf(".") != -1) {
               var2 = "";
            }
         }
      }

      return var2;
   }

   public static String getFilePath(String var0) {
      int var1 = var0.lastIndexOf(File.separator);
      String var2 = null;
      if(var1 != -1) {
         var2 = var0.substring(0, var1);
      }

      return var2;
   }

   public static String getFileSizeFromIntSize(int var0) {
      DecimalFormat var1 = new DecimalFormat("0.0");
      String var2;
      if((double)var0 >= 1048576.0D) {
         var2 = var1.format((double)var0 / 1048576.0D) + "MB";
      } else if(var0 >= 1024) {
         var2 = Integer.valueOf(var0 / 1024) + "KB";
      } else if(var0 >= 1) {
         var2 = Integer.valueOf(var0 / 1) + "B";
      } else {
         var2 = "0B";
      }

      return var2;
   }

   public static String getFileSizeFromPath(String var0) {
      DecimalFormat var3 = new DecimalFormat("0.0");
      long var1 = getFileLength(var0);
      if((double)var1 >= 1048576.0D) {
         var0 = var3.format((double)var1 / 1048576.0D) + "MB";
      } else if(var1 >= (long)1024) {
         var0 = Long.valueOf(var1 / (long)1024) + "KB";
      } else if(var1 >= (long)1) {
         var0 = Long.valueOf(var1 / (long)1) + "B";
      } else {
         var0 = "0B";
      }

      return var0;
   }

   public static String getFileTypeString(String var0) {
      if(TextUtils.isEmpty(var0)) {
         var0 = "";
      } else {
         int var1 = var0.lastIndexOf(".");
         if(var1 != -1) {
            var0 = var0.substring(var1 + 1).toLowerCase(Locale.US);
         } else {
            var0 = "";
         }
      }

      return var0;
   }

   private static String getNextPath(String var0) {
      Matcher var3 = Pattern.compile("\\(\\d{1,}\\)\\.").matcher(var0);

      String var2;
      for(var2 = null; var3.find(); var2 = var3.group(var3.groupCount())) {
         ;
      }

      int var1;
      if(var2 == null) {
         var1 = var0.lastIndexOf(".");
         if(var1 != -1) {
            var0 = var0.substring(0, var1) + "(1)" + var0.substring(var1);
         } else {
            var0 = var0 + "(1)";
         }
      } else {
         var1 = Integer.parseInt(var2.replaceAll("[^\\d]*(\\d)[^\\d]*", "$1"));
         var0 = var0.replace(var2, "(" + (var1 + 1) + ").");
      }

      return var0;
   }

   public static String getSDcardRoot() {
      String var0;
      if(hasSdcard()) {
         var0 = Environment.getExternalStorageDirectory().getAbsolutePath();
      } else {
         var0 = null;
      }

      return var0;
   }

   public static boolean hasSdcard() {
      return "mounted".equals(Environment.getExternalStorageState());
   }

   public static boolean isEmpty(Object var0) {
      boolean var2 = true;
      boolean var1 = var2;
      if(var0 != null) {
         if(var0 instanceof String) {
            var1 = var0.toString().trim().equals("");
         } else if(var0 instanceof Collection) {
            if(((Collection)var0).size() == 0) {
               var1 = true;
            } else {
               var1 = false;
            }
         } else {
            var1 = var2;
            if(var0 instanceof Object[]) {
               if(((Object[])((Object[])var0)).length == 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }
            }
         }
      }

      return var1;
   }

   private static boolean isEmulator() {
      return Build.MODEL.equals("sdk");
   }

   private static boolean isExistSdcard() {
      boolean var0;
      if(!isEmulator()) {
         var0 = Environment.getExternalStorageState().equals("mounted");
      } else {
         var0 = true;
      }

      return var0;
   }

   public static String readSDFile(File param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean setNoMediaFlag(File var0) {
      boolean var2 = false;
      var0 = new File(var0, ".nomedia");
      boolean var1 = var2;
      if(!var0.exists()) {
         try {
            var1 = var0.createNewFile();
         } catch (IOException var3) {
            var1 = var2;
         }
      }

      return var1;
   }
}
