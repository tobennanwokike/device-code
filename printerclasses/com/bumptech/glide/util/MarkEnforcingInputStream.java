package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkEnforcingInputStream extends FilterInputStream {
   private static final int END_OF_STREAM = -1;
   private static final int UNSET = Integer.MIN_VALUE;
   private int availableBytes = Integer.MIN_VALUE;

   public MarkEnforcingInputStream(InputStream var1) {
      super(var1);
   }

   private long getBytesToRead(long var1) {
      long var3;
      if(this.availableBytes == 0) {
         var3 = -1L;
      } else {
         var3 = var1;
         if(this.availableBytes != Integer.MIN_VALUE) {
            var3 = var1;
            if(var1 > (long)this.availableBytes) {
               var3 = (long)this.availableBytes;
            }
         }
      }

      return var3;
   }

   private void updateAvailableBytesAfterRead(long var1) {
      if(this.availableBytes != Integer.MIN_VALUE && var1 != -1L) {
         this.availableBytes = (int)((long)this.availableBytes - var1);
      }

   }

   public int available() throws IOException {
      int var1;
      if(this.availableBytes == Integer.MIN_VALUE) {
         var1 = super.available();
      } else {
         var1 = Math.min(this.availableBytes, super.available());
      }

      return var1;
   }

   public void mark(int var1) {
      super.mark(var1);
      this.availableBytes = var1;
   }

   public int read() throws IOException {
      int var1;
      if(this.getBytesToRead(1L) == -1L) {
         var1 = -1;
      } else {
         var1 = super.read();
         this.updateAvailableBytesAfterRead(1L);
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      byte var4 = -1;
      var3 = (int)this.getBytesToRead((long)var3);
      if(var3 == -1) {
         var2 = var4;
      } else {
         var2 = super.read(var1, var2, var3);
         this.updateAvailableBytesAfterRead((long)var2);
      }

      return var2;
   }

   public void reset() throws IOException {
      super.reset();
      this.availableBytes = Integer.MIN_VALUE;
   }

   public long skip(long var1) throws IOException {
      long var3 = -1L;
      var1 = this.getBytesToRead(var1);
      if(var1 == -1L) {
         var1 = var3;
      } else {
         var1 = super.skip(var1);
         this.updateAvailableBytesAfterRead(var1);
      }

      return var1;
   }
}
