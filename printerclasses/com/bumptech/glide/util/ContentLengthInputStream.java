package com.bumptech.glide.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
   private static final String TAG = "ContentLengthStream";
   private static final int UNKNOWN = -1;
   private final long contentLength;
   private int readSoFar;

   ContentLengthInputStream(InputStream var1, long var2) {
      super(var1);
      this.contentLength = var2;
   }

   private int checkReadSoFarOrThrow(int var1) throws IOException {
      if(var1 >= 0) {
         this.readSoFar += var1;
      } else if(this.contentLength - (long)this.readSoFar > 0L) {
         throw new IOException("Failed to read all expected data, expected: " + this.contentLength + ", but read: " + this.readSoFar);
      }

      return var1;
   }

   public static InputStream obtain(InputStream var0, long var1) {
      return new ContentLengthInputStream(var0, var1);
   }

   public static InputStream obtain(InputStream var0, String var1) {
      return obtain(var0, (long)parseContentLength(var1));
   }

   private static int parseContentLength(String var0) {
      byte var2 = -1;
      int var1 = var2;
      if(!TextUtils.isEmpty(var0)) {
         try {
            var1 = Integer.parseInt(var0);
         } catch (NumberFormatException var4) {
            var1 = var2;
            if(Log.isLoggable("ContentLengthStream", 3)) {
               Log.d("ContentLengthStream", "failed to parse content length header: " + var0, var4);
               var1 = var2;
            }
         }
      }

      return var1;
   }

   public int available() throws IOException {
      synchronized(this){}
      boolean var6 = false;

      long var2;
      try {
         var6 = true;
         var2 = Math.max(this.contentLength - (long)this.readSoFar, (long)this.in.available());
         var6 = false;
      } finally {
         if(var6) {
            ;
         }
      }

      int var1 = (int)var2;
      return var1;
   }

   public int read() throws IOException {
      synchronized(this){}

      int var1;
      try {
         var1 = this.checkReadSoFarOrThrow(super.read());
      } finally {
         ;
      }

      return var1;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this){}

      try {
         var2 = this.checkReadSoFarOrThrow(super.read(var1, var2, var3));
      } finally {
         ;
      }

      return var2;
   }
}
