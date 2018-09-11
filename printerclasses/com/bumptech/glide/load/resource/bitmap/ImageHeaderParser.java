package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageHeaderParser {
   private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
   private static final int EXIF_MAGIC_NUMBER = 65496;
   private static final int EXIF_SEGMENT_TYPE = 225;
   private static final int GIF_HEADER = 4671814;
   private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
   private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
   private static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
   private static final int MARKER_EOI = 217;
   private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
   private static final int ORIENTATION_TAG_TYPE = 274;
   private static final int PNG_HEADER = -1991225785;
   private static final int SEGMENT_SOS = 218;
   private static final int SEGMENT_START_ID = 255;
   private static final String TAG = "ImageHeaderParser";
   private final ImageHeaderParser.StreamReader streamReader;

   static {
      byte[] var0 = new byte[0];

      label13: {
         byte[] var1;
         try {
            var1 = "Exif\u0000\u0000".getBytes("UTF-8");
         } catch (UnsupportedEncodingException var2) {
            break label13;
         }

         var0 = var1;
      }

      JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = var0;
   }

   public ImageHeaderParser(InputStream var1) {
      this.streamReader = new ImageHeaderParser.StreamReader(var1);
   }

   private static int calcTagOffset(int var0, int var1) {
      return var0 + 2 + var1 * 12;
   }

   private byte[] getExifSegment() throws IOException {
      while(true) {
         short var1 = this.streamReader.getUInt8();
         byte[] var6;
         if(var1 != 255) {
            if(Log.isLoggable("ImageHeaderParser", 3)) {
               Log.d("ImageHeaderParser", "Unknown segmentId=" + var1);
            }

            var6 = null;
         } else {
            short var2 = this.streamReader.getUInt8();
            if(var2 == 218) {
               var6 = null;
            } else if(var2 == 217) {
               if(Log.isLoggable("ImageHeaderParser", 3)) {
                  Log.d("ImageHeaderParser", "Found MARKER_EOI in exif segment");
               }

               var6 = null;
            } else {
               int var7 = this.streamReader.getUInt16() - 2;
               if(var2 != 225) {
                  long var4 = this.streamReader.skip((long)var7);
                  if(var4 == (long)var7) {
                     continue;
                  }

                  if(Log.isLoggable("ImageHeaderParser", 3)) {
                     Log.d("ImageHeaderParser", "Unable to skip enough data, type: " + var2 + ", wanted to skip: " + var7 + ", but actually skipped: " + var4);
                  }

                  var6 = null;
               } else {
                  var6 = new byte[var7];
                  int var3 = this.streamReader.read(var6);
                  if(var3 != var7) {
                     if(Log.isLoggable("ImageHeaderParser", 3)) {
                        Log.d("ImageHeaderParser", "Unable to read segment data, type: " + var2 + ", length: " + var7 + ", actually read: " + var3);
                     }

                     var6 = null;
                  }
               }
            }
         }

         return var6;
      }
   }

   private static boolean handles(int var0) {
      boolean var1;
      if((var0 & '\uffd8') != '\uffd8' && var0 != 19789 && var0 != 18761) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static int parseExifSegment(ImageHeaderParser.RandomAccessReader var0) {
      int var1 = "Exif\u0000\u0000".length();
      short var2 = var0.getInt16(var1);
      ByteOrder var8;
      if(var2 == 19789) {
         var8 = ByteOrder.BIG_ENDIAN;
      } else if(var2 == 18761) {
         var8 = ByteOrder.LITTLE_ENDIAN;
      } else {
         if(Log.isLoggable("ImageHeaderParser", 3)) {
            Log.d("ImageHeaderParser", "Unknown endianness = " + var2);
         }

         var8 = ByteOrder.BIG_ENDIAN;
      }

      var0.order(var8);
      int var10 = var0.getInt32(var1 + 4) + var1;
      short var3 = var0.getInt16(var10);
      var1 = 0;

      short var9;
      while(true) {
         if(var1 >= var3) {
            var9 = -1;
            break;
         }

         int var5 = calcTagOffset(var10, var1);
         short var4 = var0.getInt16(var5);
         if(var4 == 274) {
            short var6 = var0.getInt16(var5 + 2);
            if(var6 >= 1 && var6 <= 12) {
               int var7 = var0.getInt32(var5 + 4);
               if(var7 < 0) {
                  if(Log.isLoggable("ImageHeaderParser", 3)) {
                     Log.d("ImageHeaderParser", "Negative tiff component count");
                  }
               } else {
                  if(Log.isLoggable("ImageHeaderParser", 3)) {
                     Log.d("ImageHeaderParser", "Got tagIndex=" + var1 + " tagType=" + var4 + " formatCode=" + var6 + " componentCount=" + var7);
                  }

                  var7 += BYTES_PER_FORMAT[var6];
                  if(var7 > 4) {
                     if(Log.isLoggable("ImageHeaderParser", 3)) {
                        Log.d("ImageHeaderParser", "Got byte count > 4, not orientation, continuing, formatCode=" + var6);
                     }
                  } else {
                     var5 += 8;
                     if(var5 >= 0 && var5 <= var0.length()) {
                        if(var7 >= 0 && var5 + var7 <= var0.length()) {
                           var9 = var0.getInt16(var5);
                           break;
                        }

                        if(Log.isLoggable("ImageHeaderParser", 3)) {
                           Log.d("ImageHeaderParser", "Illegal number of bytes for TI tag data tagType=" + var4);
                        }
                     } else if(Log.isLoggable("ImageHeaderParser", 3)) {
                        Log.d("ImageHeaderParser", "Illegal tagValueOffset=" + var5 + " tagType=" + var4);
                     }
                  }
               }
            } else if(Log.isLoggable("ImageHeaderParser", 3)) {
               Log.d("ImageHeaderParser", "Got invalid format code=" + var6);
            }
         }

         ++var1;
      }

      return var9;
   }

   public int getOrientation() throws IOException {
      byte var4 = -1;
      int var1;
      if(!handles(this.streamReader.getUInt16())) {
         var1 = var4;
      } else {
         byte[] var5 = this.getExifSegment();
         boolean var6;
         if(var5 != null && var5.length > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
            var6 = true;
         } else {
            var6 = false;
         }

         boolean var2 = var6;
         if(var6) {
            int var3 = 0;

            while(true) {
               var2 = var6;
               if(var3 >= JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
                  break;
               }

               if(var5[var3] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[var3]) {
                  var2 = false;
                  break;
               }

               ++var3;
            }
         }

         var1 = var4;
         if(var2) {
            var1 = parseExifSegment(new ImageHeaderParser.RandomAccessReader(var5));
         }
      }

      return var1;
   }

   public ImageHeaderParser.ImageType getType() throws IOException {
      int var1 = this.streamReader.getUInt16();
      ImageHeaderParser.ImageType var2;
      if(var1 == '\uffd8') {
         var2 = ImageHeaderParser.ImageType.JPEG;
      } else {
         var1 = var1 << 16 & -65536 | this.streamReader.getUInt16() & '\uffff';
         if(var1 == -1991225785) {
            this.streamReader.skip(21L);
            if(this.streamReader.getByte() >= 3) {
               var2 = ImageHeaderParser.ImageType.PNG_A;
            } else {
               var2 = ImageHeaderParser.ImageType.PNG;
            }
         } else if(var1 >> 8 == 4671814) {
            var2 = ImageHeaderParser.ImageType.GIF;
         } else {
            var2 = ImageHeaderParser.ImageType.UNKNOWN;
         }
      }

      return var2;
   }

   public boolean hasAlpha() throws IOException {
      return this.getType().hasAlpha();
   }

   public static enum ImageType {
      GIF(true),
      JPEG(false),
      PNG(false),
      PNG_A(true),
      UNKNOWN(false);

      private final boolean hasAlpha;

      private ImageType(boolean var3) {
         this.hasAlpha = var3;
      }

      public boolean hasAlpha() {
         return this.hasAlpha;
      }
   }

   private static class RandomAccessReader {
      private final ByteBuffer data;

      public RandomAccessReader(byte[] var1) {
         this.data = ByteBuffer.wrap(var1);
         this.data.order(ByteOrder.BIG_ENDIAN);
      }

      public short getInt16(int var1) {
         return this.data.getShort(var1);
      }

      public int getInt32(int var1) {
         return this.data.getInt(var1);
      }

      public int length() {
         return this.data.array().length;
      }

      public void order(ByteOrder var1) {
         this.data.order(var1);
      }
   }

   private static class StreamReader {
      private final InputStream is;

      public StreamReader(InputStream var1) {
         this.is = var1;
      }

      public int getByte() throws IOException {
         return this.is.read();
      }

      public int getUInt16() throws IOException {
         return this.is.read() << 8 & '\uff00' | this.is.read() & 255;
      }

      public short getUInt8() throws IOException {
         return (short)(this.is.read() & 255);
      }

      public int read(byte[] var1) throws IOException {
         int var2;
         int var3;
         for(var2 = var1.length; var2 > 0; var2 -= var3) {
            var3 = this.is.read(var1, var1.length - var2, var2);
            if(var3 == -1) {
               break;
            }
         }

         return var1.length - var2;
      }

      public long skip(long var1) throws IOException {
         long var3 = 0L;
         if(var1 < 0L) {
            var1 = var3;
         } else {
            var3 = var1;

            while(var3 > 0L) {
               long var5 = this.is.skip(var3);
               if(var5 > 0L) {
                  var3 -= var5;
               } else {
                  if(this.is.read() == -1) {
                     break;
                  }

                  --var3;
               }
            }

            var1 -= var3;
         }

         return var1;
      }
   }
}
