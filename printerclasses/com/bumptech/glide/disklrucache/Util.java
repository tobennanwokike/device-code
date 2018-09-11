package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

final class Util {
   static final Charset US_ASCII = Charset.forName("US-ASCII");
   static final Charset UTF_8 = Charset.forName("UTF-8");

   static void closeQuietly(Closeable var0) {
      if(var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
            ;
         }
      }

   }

   static void deleteContents(File var0) throws IOException {
      File[] var3 = var0.listFiles();
      if(var3 == null) {
         throw new IOException("not a readable directory: " + var0);
      } else {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var0 = var3[var1];
            if(var0.isDirectory()) {
               deleteContents(var0);
            }

            if(!var0.delete()) {
               throw new IOException("failed to delete file: " + var0);
            }
         }

      }
   }

   static String readFully(Reader param0) throws IOException {
      // $FF: Couldn't be decompiled
   }
}
