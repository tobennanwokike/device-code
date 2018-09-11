package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
   private static final String TAG = "BufferedIs";
   private volatile byte[] buf;
   private int count;
   private int marklimit;
   private int markpos = -1;
   private int pos;

   public RecyclableBufferedInputStream(InputStream var1, byte[] var2) {
      super(var1);
      if(var2 != null && var2.length != 0) {
         this.buf = var2;
      } else {
         throw new IllegalArgumentException("buffer is null or empty");
      }
   }

   private int fillbuf(InputStream var1, byte[] var2) throws IOException {
      int var3;
      if(this.markpos != -1 && this.pos - this.markpos < this.marklimit) {
         int var4;
         byte[] var5;
         if(this.markpos == 0 && this.marklimit > var2.length && this.count == var2.length) {
            var4 = var2.length * 2;
            var3 = var4;
            if(var4 > this.marklimit) {
               var3 = this.marklimit;
            }

            if(Log.isLoggable("BufferedIs", 3)) {
               Log.d("BufferedIs", "allocate buffer of length: " + var3);
            }

            var5 = new byte[var3];
            System.arraycopy(var2, 0, var5, 0, var2.length);
            this.buf = var5;
         } else {
            var5 = var2;
            if(this.markpos > 0) {
               System.arraycopy(var2, this.markpos, var2, 0, var2.length - this.markpos);
               var5 = var2;
            }
         }

         this.pos -= this.markpos;
         this.markpos = 0;
         this.count = 0;
         var4 = var1.read(var5, this.pos, var5.length - this.pos);
         if(var4 <= 0) {
            var3 = this.pos;
         } else {
            var3 = this.pos + var4;
         }

         this.count = var3;
         var3 = var4;
      } else {
         var3 = var1.read(var2);
         if(var3 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = var3;
         }
      }

      return var3;
   }

   private static IOException streamClosed() throws IOException {
      throw new IOException("BufferedInputStream is closed");
   }

   public int available() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void close() throws IOException {
      this.buf = null;
      InputStream var1 = this.in;
      this.in = null;
      if(var1 != null) {
         var1.close();
      }

   }

   public void fixMarkLimit() {
      synchronized(this){}

      try {
         this.marklimit = this.buf.length;
      } finally {
         ;
      }

   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         this.marklimit = Math.max(this.marklimit, var1);
         this.markpos = this.pos;
      } finally {
         ;
      }

   }

   public boolean markSupported() {
      return true;
   }

   public int read() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public int read(byte[] param1, int param2, int param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         if(this.buf == null) {
            IOException var4 = new IOException("Stream is closed");
            throw var4;
         }

         if(-1 == this.markpos) {
            RecyclableBufferedInputStream.InvalidMarkException var1 = new RecyclableBufferedInputStream.InvalidMarkException("Mark has been invalidated");
            throw var1;
         }

         this.pos = this.markpos;
      } finally {
         ;
      }

   }

   public long skip(long param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static class InvalidMarkException extends RuntimeException {
      private static final long serialVersionUID = -4338378848813561757L;

      public InvalidMarkException(String var1) {
         super(var1);
      }
   }
}
