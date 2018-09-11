package com.bumptech.glide.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class LZWEncoder {
   static final int BITS = 12;
   private static final int EOF = -1;
   static final int HSIZE = 5003;
   int ClearCode;
   int EOFCode;
   int a_count;
   byte[] accum = new byte[256];
   boolean clear_flg = false;
   int[] codetab = new int[5003];
   private int curPixel;
   int cur_accum = 0;
   int cur_bits = 0;
   int free_ent = 0;
   int g_init_bits;
   int hsize = 5003;
   int[] htab = new int[5003];
   private int imgH;
   private int imgW;
   private int initCodeSize;
   int[] masks = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, '\uffff'};
   int maxbits = 12;
   int maxcode;
   int maxmaxcode = 4096;
   int n_bits;
   private byte[] pixAry;
   private int remaining;

   LZWEncoder(int var1, int var2, byte[] var3, int var4) {
      this.imgW = var1;
      this.imgH = var2;
      this.pixAry = var3;
      this.initCodeSize = Math.max(2, var4);
   }

   private int nextPixel() {
      int var1;
      if(this.remaining == 0) {
         var1 = -1;
      } else {
         --this.remaining;
         byte[] var2 = this.pixAry;
         var1 = this.curPixel;
         this.curPixel = var1 + 1;
         var1 = var2[var1] & 255;
      }

      return var1;
   }

   final int MAXCODE(int var1) {
      return (1 << var1) - 1;
   }

   void char_out(byte var1, OutputStream var2) throws IOException {
      byte[] var4 = this.accum;
      int var3 = this.a_count;
      this.a_count = var3 + 1;
      var4[var3] = var1;
      if(this.a_count >= 254) {
         this.flush_char(var2);
      }

   }

   void cl_block(OutputStream var1) throws IOException {
      this.cl_hash(this.hsize);
      this.free_ent = this.ClearCode + 2;
      this.clear_flg = true;
      this.output(this.ClearCode, var1);
   }

   void cl_hash(int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.htab[var2] = -1;
      }

   }

   void compress(int var1, OutputStream var2) throws IOException {
      this.g_init_bits = var1;
      this.clear_flg = false;
      this.n_bits = this.g_init_bits;
      this.maxcode = this.MAXCODE(this.n_bits);
      this.ClearCode = 1 << var1 - 1;
      this.EOFCode = this.ClearCode + 1;
      this.free_ent = this.ClearCode + 2;
      this.a_count = 0;
      int var3 = this.nextPixel();
      int var4 = 0;

      for(var1 = this.hsize; var1 < 65536; var1 *= 2) {
         ++var4;
      }

      int var8 = this.hsize;
      this.cl_hash(var8);
      this.output(this.ClearCode, var2);
      var1 = var3;

      while(true) {
         label42:
         while(true) {
            int var7 = this.nextPixel();
            if(var7 == -1) {
               this.output(var1, var2);
               this.output(this.EOFCode, var2);
               return;
            }

            int var9 = (var7 << this.maxbits) + var1;
            var3 = var7 << 8 - var4 ^ var1;
            if(this.htab[var3] == var9) {
               var1 = this.codetab[var3];
            } else {
               int var5 = var3;
               if(this.htab[var3] >= 0) {
                  var5 = var8 - var3;
                  int var6 = var3;
                  if(var3 == 0) {
                     var5 = 1;
                     var6 = var3;
                  }

                  do {
                     var6 -= var5;
                     var3 = var6;
                     if(var6 < 0) {
                        var3 = var6 + var8;
                     }

                     if(this.htab[var3] == var9) {
                        var1 = this.codetab[var3];
                        continue label42;
                     }

                     var6 = var3;
                  } while(this.htab[var3] >= 0);

                  var5 = var3;
               }

               this.output(var1, var2);
               var1 = var7;
               if(this.free_ent < this.maxmaxcode) {
                  int[] var10 = this.codetab;
                  var3 = this.free_ent;
                  this.free_ent = var3 + 1;
                  var10[var5] = var3;
                  this.htab[var5] = var9;
               } else {
                  this.cl_block(var2);
               }
            }
         }
      }
   }

   void encode(OutputStream var1) throws IOException {
      var1.write(this.initCodeSize);
      this.remaining = this.imgW * this.imgH;
      this.curPixel = 0;
      this.compress(this.initCodeSize + 1, var1);
      var1.write(0);
   }

   void flush_char(OutputStream var1) throws IOException {
      if(this.a_count > 0) {
         var1.write(this.a_count);
         var1.write(this.accum, 0, this.a_count);
         this.a_count = 0;
      }

   }

   void output(int var1, OutputStream var2) throws IOException {
      this.cur_accum &= this.masks[this.cur_bits];
      if(this.cur_bits > 0) {
         this.cur_accum |= var1 << this.cur_bits;
      } else {
         this.cur_accum = var1;
      }

      for(this.cur_bits += this.n_bits; this.cur_bits >= 8; this.cur_bits -= 8) {
         this.char_out((byte)(this.cur_accum & 255), var2);
         this.cur_accum >>= 8;
      }

      if(this.free_ent > this.maxcode || this.clear_flg) {
         if(this.clear_flg) {
            int var3 = this.g_init_bits;
            this.n_bits = var3;
            this.maxcode = this.MAXCODE(var3);
            this.clear_flg = false;
         } else {
            ++this.n_bits;
            if(this.n_bits == this.maxbits) {
               this.maxcode = this.maxmaxcode;
            } else {
               this.maxcode = this.MAXCODE(this.n_bits);
            }
         }
      }

      if(var1 == this.EOFCode) {
         while(this.cur_bits > 0) {
            this.char_out((byte)(this.cur_accum & 255), var2);
            this.cur_accum >>= 8;
            this.cur_bits -= 8;
         }

         this.flush_char(var2);
      }

   }
}
