package com.bumptech.glide.gifencoder;

class NeuQuant {
   protected static final int alphabiasshift = 10;
   protected static final int alpharadbias = 262144;
   protected static final int alpharadbshift = 18;
   protected static final int beta = 64;
   protected static final int betagamma = 65536;
   protected static final int betashift = 10;
   protected static final int gamma = 1024;
   protected static final int gammashift = 10;
   protected static final int initalpha = 1024;
   protected static final int initrad = 32;
   protected static final int initradius = 2048;
   protected static final int intbias = 65536;
   protected static final int intbiasshift = 16;
   protected static final int maxnetpos = 255;
   protected static final int minpicturebytes = 1509;
   protected static final int ncycles = 100;
   protected static final int netbiasshift = 4;
   protected static final int netsize = 256;
   protected static final int prime1 = 499;
   protected static final int prime2 = 491;
   protected static final int prime3 = 487;
   protected static final int prime4 = 503;
   protected static final int radbias = 256;
   protected static final int radbiasshift = 8;
   protected static final int radiusbias = 64;
   protected static final int radiusbiasshift = 6;
   protected static final int radiusdec = 30;
   protected int alphadec;
   protected int[] bias = new int[256];
   protected int[] freq = new int[256];
   protected int lengthcount;
   protected int[] netindex = new int[256];
   protected int[][] network;
   protected int[] radpower = new int[32];
   protected int samplefac;
   protected byte[] thepicture;

   public NeuQuant(byte[] var1, int var2, int var3) {
      this.thepicture = var1;
      this.lengthcount = var2;
      this.samplefac = var3;
      this.network = new int[256][];

      for(var2 = 0; var2 < 256; ++var2) {
         this.network[var2] = new int[4];
         int[] var4 = this.network[var2];
         var3 = (var2 << 12) / 256;
         var4[2] = var3;
         var4[1] = var3;
         var4[0] = var3;
         this.freq[var2] = 256;
         this.bias[var2] = 0;
      }

   }

   protected void alterneigh(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var2 - var1;
      int var8 = var6;
      if(var6 < -1) {
         var8 = -1;
      }

      var1 += var2;
      int var9 = var1;
      if(var1 > 256) {
         var9 = 256;
      }

      byte var7 = 1;
      var6 = var2 - 1;
      var1 = var2 + 1;
      var2 = var7;

      while(true) {
         while(var1 < var9 || var6 > var8) {
            int[] var12 = this.radpower;
            int var15 = var2 + 1;
            int var11 = var12[var2];
            int[][] var16;
            if(var1 < var9) {
               label56: {
                  var16 = this.network;
                  var2 = var1 + 1;
                  var12 = var16[var1];

                  try {
                     var12[0] -= (var12[0] - var3) * var11 / 262144;
                     var12[1] -= (var12[1] - var4) * var11 / 262144;
                     var12[2] -= (var12[2] - var5) * var11 / 262144;
                  } catch (Exception var13) {
                     var1 = var2;
                     break label56;
                  }

                  var1 = var2;
               }
            }

            if(var6 > var8) {
               var16 = this.network;
               int var10 = var6 - 1;
               var12 = var16[var6];

               try {
                  var12[0] -= (var12[0] - var3) * var11 / 262144;
                  var12[1] -= (var12[1] - var4) * var11 / 262144;
                  var12[2] -= (var12[2] - var5) * var11 / 262144;
               } catch (Exception var14) {
                  var2 = var15;
                  var6 = var10;
                  continue;
               }

               var2 = var15;
               var6 = var10;
            } else {
               var2 = var15;
            }
         }

         return;
      }
   }

   protected void altersingle(int var1, int var2, int var3, int var4, int var5) {
      int[] var6 = this.network[var2];
      var6[0] -= (var6[0] - var3) * var1 / 1024;
      var6[1] -= (var6[1] - var4) * var1 / 1024;
      var6[2] -= (var6[2] - var5) * var1 / 1024;
   }

   public byte[] colorMap() {
      byte[] var6 = new byte[768];
      int[] var5 = new int[256];

      int var1;
      for(var1 = 0; var1 < 256; var5[this.network[var1][3]] = var1++) {
         ;
      }

      var1 = 0;

      for(int var2 = 0; var1 < 256; ++var2) {
         int var3 = var5[var1];
         int var4 = var2 + 1;
         var6[var2] = (byte)this.network[var3][0];
         var2 = var4 + 1;
         var6[var4] = (byte)this.network[var3][1];
         var6[var2] = (byte)this.network[var3][2];
         ++var1;
      }

      return var6;
   }

   protected int contest(int var1, int var2, int var3) {
      int var8 = Integer.MAX_VALUE;
      int var6 = Integer.MAX_VALUE;
      int var7 = -1;
      int var5 = -1;

      int var9;
      int[] var13;
      for(int var4 = 0; var4 < 256; var8 = var9) {
         var13 = this.network[var4];
         int var10 = var13[0] - var1;
         var9 = var10;
         if(var10 < 0) {
            var9 = -var10;
         }

         int var11 = var13[1] - var2;
         var10 = var11;
         if(var11 < 0) {
            var10 = -var11;
         }

         int var12 = var13[2] - var3;
         var11 = var12;
         if(var12 < 0) {
            var11 = -var12;
         }

         var10 = var9 + var10 + var11;
         var9 = var8;
         if(var10 < var8) {
            var9 = var10;
            var7 = var4;
         }

         var10 -= this.bias[var4] >> 12;
         var8 = var6;
         if(var10 < var6) {
            var8 = var10;
            var5 = var4;
         }

         var6 = this.freq[var4] >> 10;
         var13 = this.freq;
         var13[var4] -= var6;
         var13 = this.bias;
         var13[var4] += var6 << 10;
         ++var4;
         var6 = var8;
      }

      var13 = this.freq;
      var13[var7] += 64;
      var13 = this.bias;
      var13[var7] -= 65536;
      return var5;
   }

   public void inxbuild() {
      int var5 = 0;
      int var4 = 0;

      int var1;
      int var3;
      for(var1 = 0; var1 < 256; var4 = var3) {
         int[] var8 = this.network[var1];
         int var7 = var1;
         int var2 = var8[1];

         int var6;
         int[] var9;
         for(var3 = var1 + 1; var3 < 256; var2 = var6) {
            var9 = this.network[var3];
            var6 = var2;
            if(var9[1] < var2) {
               var7 = var3;
               var6 = var9[1];
            }

            ++var3;
         }

         var9 = this.network[var7];
         if(var1 != var7) {
            var3 = var9[0];
            var9[0] = var8[0];
            var8[0] = var3;
            var3 = var9[1];
            var9[1] = var8[1];
            var8[1] = var3;
            var3 = var9[2];
            var9[2] = var8[2];
            var8[2] = var3;
            var3 = var9[3];
            var9[3] = var8[3];
            var8[3] = var3;
         }

         var6 = var5;
         var3 = var4;
         if(var2 != var5) {
            this.netindex[var5] = var4 + var1 >> 1;

            for(var3 = var5 + 1; var3 < var2; ++var3) {
               this.netindex[var3] = var1;
            }

            var3 = var1;
            var6 = var2;
         }

         ++var1;
         var5 = var6;
      }

      this.netindex[var5] = var4 + 255 >> 1;

      for(var1 = var5 + 1; var1 < 256; ++var1) {
         this.netindex[var1] = 255;
      }

   }

   public void learn() {
      if(this.lengthcount < 1509) {
         this.samplefac = 1;
      }

      this.alphadec = (this.samplefac - 1) / 3 + 30;
      byte[] var17 = this.thepicture;
      int var8 = 0;
      int var15 = this.lengthcount;
      int var16 = this.lengthcount / (this.samplefac * 3);
      int var7 = var16 / 100;
      int var9 = 1024;
      int var4 = 2048;
      int var1 = 2048 >> 6;
      int var2 = var1;
      if(var1 <= 1) {
         var2 = 0;
      }

      for(var1 = 0; var1 < var2; ++var1) {
         this.radpower[var1] = (var2 * var2 - var1 * var1) * 256 / (var2 * var2) * 1024;
      }

      short var18;
      if(this.lengthcount < 1509) {
         var18 = 3;
      } else if(this.lengthcount % 499 != 0) {
         var18 = 1497;
      } else if(this.lengthcount % 491 != 0) {
         var18 = 1473;
      } else if(this.lengthcount % 487 != 0) {
         var18 = 1461;
      } else {
         var18 = 1509;
      }

      int var5 = 0;
      int var6 = var2;

      while(true) {
         int var3;
         int var10;
         int var11;
         int var12;
         do {
            if(var5 >= var16) {
               return;
            }

            var10 = (var17[var8 + 0] & 255) << 4;
            var2 = (var17[var8 + 1] & 255) << 4;
            var11 = (var17[var8 + 2] & 255) << 4;
            var3 = this.contest(var10, var2, var11);
            this.altersingle(var9, var3, var10, var2, var11);
            if(var6 != 0) {
               this.alterneigh(var6, var3, var10, var2, var11);
            }

            var3 = var8 + var18;
            var2 = var3;
            if(var3 >= var15) {
               var2 = var3 - this.lengthcount;
            }

            var12 = var5 + 1;
            var10 = var7;
            if(var7 == 0) {
               var10 = 1;
            }

            var7 = var10;
            var5 = var12;
            var8 = var2;
         } while(var12 % var10 != 0);

         int var14 = var9 - var9 / this.alphadec;
         int var13 = var4 - var4 / 30;
         var4 = var13 >> 6;
         var3 = var4;
         if(var4 <= 1) {
            var3 = 0;
         }

         var11 = 0;

         while(true) {
            var9 = var14;
            var6 = var3;
            var7 = var10;
            var5 = var12;
            var8 = var2;
            var4 = var13;
            if(var11 >= var3) {
               break;
            }

            this.radpower[var11] = (var3 * var3 - var11 * var11) * 256 / (var3 * var3) * var14;
            ++var11;
         }
      }
   }

   public int map(int var1, int var2, int var3) {
      int var5 = 1000;
      int var7 = -1;
      int var10 = this.netindex[var2];
      int var4 = var10 - 1;

      while(var10 < 256 || var4 >= 0) {
         int var8 = var7;
         int var9 = var5;
         int var6 = var10;
         int var11;
         int[] var13;
         if(var10 < 256) {
            var13 = this.network[var10];
            var8 = var13[1] - var2;
            if(var8 >= var5) {
               var6 = 256;
               var9 = var5;
               var8 = var7;
            } else {
               ++var10;
               var6 = var8;
               if(var8 < 0) {
                  var6 = -var8;
               }

               var9 = var13[0] - var1;
               var8 = var9;
               if(var9 < 0) {
                  var8 = -var9;
               }

               var11 = var6 + var8;
               var8 = var7;
               var9 = var5;
               var6 = var10;
               if(var11 < var5) {
                  var8 = var13[2] - var3;
                  var6 = var8;
                  if(var8 < 0) {
                     var6 = -var8;
                  }

                  var11 += var6;
                  var8 = var7;
                  var9 = var5;
                  var6 = var10;
                  if(var11 < var5) {
                     var9 = var11;
                     var8 = var13[3];
                     var6 = var10;
                  }
               }
            }
         }

         var7 = var8;
         var5 = var9;
         var10 = var6;
         if(var4 >= 0) {
            var13 = this.network[var4];
            var5 = var2 - var13[1];
            if(var5 >= var9) {
               var4 = -1;
               var7 = var8;
               var5 = var9;
               var10 = var6;
            } else {
               var11 = var4 - 1;
               var4 = var5;
               if(var5 < 0) {
                  var4 = -var5;
               }

               var7 = var13[0] - var1;
               var5 = var7;
               if(var7 < 0) {
                  var5 = -var7;
               }

               int var12 = var4 + var5;
               var7 = var8;
               var5 = var9;
               var10 = var6;
               var4 = var11;
               if(var12 < var9) {
                  var5 = var13[2] - var3;
                  var4 = var5;
                  if(var5 < 0) {
                     var4 = -var5;
                  }

                  var12 += var4;
                  var7 = var8;
                  var5 = var9;
                  var10 = var6;
                  var4 = var11;
                  if(var12 < var9) {
                     var5 = var12;
                     var7 = var13[3];
                     var10 = var6;
                     var4 = var11;
                  }
               }
            }
         }
      }

      return var7;
   }

   public byte[] process() {
      this.learn();
      this.unbiasnet();
      this.inxbuild();
      return this.colorMap();
   }

   public void unbiasnet() {
      for(int var1 = 0; var1 < 256; this.network[var1][3] = var1++) {
         int[] var2 = this.network[var1];
         var2[0] >>= 4;
         var2 = this.network[var1];
         var2[1] >>= 4;
         var2 = this.network[var1];
         var2[2] >>= 4;
      }

   }
}
