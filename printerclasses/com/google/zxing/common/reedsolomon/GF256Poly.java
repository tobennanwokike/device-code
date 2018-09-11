package com.google.zxing.common.reedsolomon;

import com.google.zxing.common.reedsolomon.GF256;

final class GF256Poly {
   private final int[] coefficients;
   private final GF256 field;

   GF256Poly(GF256 var1, int[] var2) {
      int var3 = 1;
      super();
      if(var2 != null && var2.length != 0) {
         this.field = var1;
         int var4 = var2.length;
         if(var4 > 1 && var2[0] == 0) {
            while(var3 < var4 && var2[var3] == 0) {
               ++var3;
            }

            if(var3 == var4) {
               this.coefficients = var1.getZero().coefficients;
            } else {
               this.coefficients = new int[var4 - var3];
               System.arraycopy(var2, var3, this.coefficients, 0, this.coefficients.length);
            }
         } else {
            this.coefficients = var2;
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   GF256Poly addOrSubtract(GF256Poly var1) {
      if(!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
      } else {
         if(!this.isZero()) {
            if(var1.isZero()) {
               var1 = this;
            } else {
               int[] var4 = this.coefficients;
               int[] var5 = var1.coefficients;
               int[] var6;
               if(var4.length > var5.length) {
                  var6 = var5;
               } else {
                  var6 = var4;
                  var4 = var5;
               }

               var5 = new int[var4.length];
               int var3 = var4.length - var6.length;
               System.arraycopy(var4, 0, var5, 0, var3);

               for(int var2 = var3; var2 < var4.length; ++var2) {
                  var5[var2] = GF256.addOrSubtract(var6[var2 - var3], var4[var2]);
               }

               var1 = new GF256Poly(this.field, var5);
            }
         }

         return var1;
      }
   }

   GF256Poly[] divide(GF256Poly var1) {
      if(!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
      } else if(var1.isZero()) {
         throw new IllegalArgumentException("Divide by 0");
      } else {
         GF256Poly var5 = this.field.getZero();
         int var2 = var1.getCoefficient(var1.getDegree());
         var2 = this.field.inverse(var2);

         GF256Poly var6;
         GF256Poly var7;
         for(var6 = this; var6.getDegree() >= var1.getDegree() && !var6.isZero(); var6 = var6.addOrSubtract(var7)) {
            int var4 = var6.getDegree() - var1.getDegree();
            int var3 = this.field.multiply(var6.getCoefficient(var6.getDegree()), var2);
            var7 = var1.multiplyByMonomial(var4, var3);
            var5 = var5.addOrSubtract(this.field.buildMonomial(var4, var3));
         }

         return new GF256Poly[]{var5, var6};
      }
   }

   int evaluateAt(int var1) {
      int var3 = 0;
      int var2;
      if(var1 == 0) {
         var2 = this.getCoefficient(0);
      } else {
         int var5 = this.coefficients.length;
         if(var1 == 1) {
            var1 = 0;

            while(true) {
               var2 = var1;
               if(var3 >= var5) {
                  break;
               }

               var1 = GF256.addOrSubtract(var1, this.coefficients[var3]);
               ++var3;
            }
         } else {
            var3 = this.coefficients[0];
            int var4 = 1;

            while(true) {
               var2 = var3;
               if(var4 >= var5) {
                  break;
               }

               var3 = GF256.addOrSubtract(this.field.multiply(var1, var3), this.coefficients[var4]);
               ++var4;
            }
         }
      }

      return var2;
   }

   int getCoefficient(int var1) {
      return this.coefficients[this.coefficients.length - 1 - var1];
   }

   int[] getCoefficients() {
      return this.coefficients;
   }

   int getDegree() {
      return this.coefficients.length - 1;
   }

   boolean isZero() {
      boolean var1 = false;
      if(this.coefficients[0] == 0) {
         var1 = true;
      }

      return var1;
   }

   GF256Poly multiply(int var1) {
      GF256Poly var4;
      if(var1 == 0) {
         var4 = this.field.getZero();
      } else {
         var4 = this;
         if(var1 != 1) {
            int var3 = this.coefficients.length;
            int[] var5 = new int[var3];

            for(int var2 = 0; var2 < var3; ++var2) {
               var5[var2] = this.field.multiply(this.coefficients[var2], var1);
            }

            var4 = new GF256Poly(this.field, var5);
         }
      }

      return var4;
   }

   GF256Poly multiply(GF256Poly var1) {
      if(!this.field.equals(var1.field)) {
         throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
      } else {
         if(!this.isZero() && !var1.isZero()) {
            int[] var7 = this.coefficients;
            int var4 = var7.length;
            int[] var9 = var1.coefficients;
            int var5 = var9.length;
            int[] var8 = new int[var4 + var5 - 1];

            for(int var2 = 0; var2 < var4; ++var2) {
               int var6 = var7[var2];

               for(int var3 = 0; var3 < var5; ++var3) {
                  var8[var2 + var3] = GF256.addOrSubtract(var8[var2 + var3], this.field.multiply(var6, var9[var3]));
               }
            }

            var1 = new GF256Poly(this.field, var8);
         } else {
            var1 = this.field.getZero();
         }

         return var1;
      }
   }

   GF256Poly multiplyByMonomial(int var1, int var2) {
      if(var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         GF256Poly var4;
         if(var2 == 0) {
            var4 = this.field.getZero();
         } else {
            int var3 = this.coefficients.length;
            int[] var5 = new int[var3 + var1];

            for(var1 = 0; var1 < var3; ++var1) {
               var5[var1] = this.field.multiply(this.coefficients[var1], var2);
            }

            var4 = new GF256Poly(this.field, var5);
         }

         return var4;
      }
   }

   public String toString() {
      StringBuffer var4 = new StringBuffer(this.getDegree() * 8);

      for(int var1 = this.getDegree(); var1 >= 0; --var1) {
         int var3 = this.getCoefficient(var1);
         if(var3 != 0) {
            int var2;
            if(var3 < 0) {
               var4.append(" - ");
               var2 = -var3;
            } else {
               var2 = var3;
               if(var4.length() > 0) {
                  var4.append(" + ");
                  var2 = var3;
               }
            }

            if(var1 == 0 || var2 != 1) {
               var2 = this.field.log(var2);
               if(var2 == 0) {
                  var4.append('1');
               } else if(var2 == 1) {
                  var4.append('a');
               } else {
                  var4.append("a^");
                  var4.append(var2);
               }
            }

            if(var1 != 0) {
               if(var1 == 1) {
                  var4.append('x');
               } else {
                  var4.append("x^");
                  var4.append(var1);
               }
            }
         }
      }

      return var4.toString();
   }
}
