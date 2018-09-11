package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExifOrientationStream extends FilterInputStream {
   private static final byte[] EXIF_SEGMENT = new byte[]{(byte)-1, (byte)-31, (byte)0, (byte)28, (byte)69, (byte)120, (byte)105, (byte)102, (byte)0, (byte)0, (byte)77, (byte)77, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)8, (byte)0, (byte)1, (byte)1, (byte)18, (byte)0, (byte)2, (byte)0, (byte)0, (byte)0, (byte)1, (byte)0};
   private static final int ORIENTATION_POSITION;
   private static final int SEGMENT_LENGTH;
   private static final int SEGMENT_START_POSITION = 2;
   private final byte orientation;
   private int position;

   static {
      SEGMENT_LENGTH = EXIF_SEGMENT.length;
      ORIENTATION_POSITION = SEGMENT_LENGTH + 2;
   }

   public ExifOrientationStream(InputStream var1, int var2) {
      super(var1);
      if(var2 >= -1 && var2 <= 8) {
         this.orientation = (byte)var2;
      } else {
         throw new IllegalArgumentException("Cannot add invalid orientation: " + var2);
      }
   }

   public void mark(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      int var1;
      if(this.position >= 2 && this.position <= ORIENTATION_POSITION) {
         if(this.position == ORIENTATION_POSITION) {
            var1 = this.orientation;
         } else {
            var1 = EXIF_SEGMENT[this.position - 2] & 255;
         }
      } else {
         var1 = super.read();
      }

      if(var1 != -1) {
         ++this.position;
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if(this.position > ORIENTATION_POSITION) {
         var2 = super.read(var1, var2, var3);
      } else if(this.position == ORIENTATION_POSITION) {
         var1[var2] = this.orientation;
         var2 = 1;
      } else if(this.position < 2) {
         var2 = super.read(var1, var2, 2 - this.position);
      } else {
         var3 = Math.min(ORIENTATION_POSITION - this.position, var3);
         System.arraycopy(EXIF_SEGMENT, this.position - 2, var1, var2, var3);
         var2 = var3;
      }

      if(var2 > 0) {
         this.position += var2;
      }

      return var2;
   }

   public void reset() throws IOException {
      throw new UnsupportedOperationException();
   }

   public long skip(long var1) throws IOException {
      var1 = super.skip(var1);
      if(var1 > 0L) {
         this.position = (int)((long)this.position + var1);
      }

      return var1;
   }
}
