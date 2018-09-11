package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public final class MaskUtil {
   public static int applyMaskPenaltyRule1(ByteMatrix var0) {
      return applyMaskPenaltyRule1Internal(var0, true) + applyMaskPenaltyRule1Internal(var0, false);
   }

   private static int applyMaskPenaltyRule1Internal(ByteMatrix var0, boolean var1) {
      byte var9 = -1;
      int var4;
      if(var1) {
         var4 = var0.getHeight();
      } else {
         var4 = var0.getWidth();
      }

      int var5;
      if(var1) {
         var5 = var0.getWidth();
      } else {
         var5 = var0.getHeight();
      }

      byte[][] var12 = var0.getArray();
      int var6 = 0;

      int var8;
      for(var8 = 0; var6 < var4; ++var6) {
         int var7 = 0;

         int var13;
         for(int var3 = 0; var7 < var5; var8 = var13) {
            byte var2;
            if(var1) {
               var2 = var12[var6][var7];
            } else {
               var2 = var12[var7][var6];
            }

            byte var10;
            if(var2 == var9) {
               int var11 = var3 + 1;
               if(var11 == 5) {
                  var13 = var8 + 3;
                  var3 = var11;
                  var10 = var9;
               } else {
                  var10 = var9;
                  var3 = var11;
                  var13 = var8;
                  if(var11 > 5) {
                     var13 = var8 + 1;
                     var10 = var9;
                     var3 = var11;
                  }
               }
            } else {
               var3 = 1;
               var10 = var2;
               var13 = var8;
            }

            ++var7;
            var9 = var10;
         }
      }

      return var8;
   }

   public static int applyMaskPenaltyRule2(ByteMatrix var0) {
      byte[][] var8 = var0.getArray();
      int var6 = var0.getWidth();
      int var5 = var0.getHeight();
      int var1 = 0;

      int var2;
      int var4;
      for(var2 = 0; var1 < var5 - 1; ++var1) {
         for(int var3 = 0; var3 < var6 - 1; var2 = var4) {
            byte var7 = var8[var1][var3];
            var4 = var2;
            if(var7 == var8[var1][var3 + 1]) {
               var4 = var2;
               if(var7 == var8[var1 + 1][var3]) {
                  var4 = var2;
                  if(var7 == var8[var1 + 1][var3 + 1]) {
                     var4 = var2 + 3;
                  }
               }
            }

            ++var3;
         }
      }

      return var2;
   }

   public static int applyMaskPenaltyRule3(ByteMatrix var0) {
      byte[][] var7 = var0.getArray();
      int var5 = var0.getWidth();
      int var6 = var0.getHeight();
      int var3 = 0;

      int var1;
      for(var1 = 0; var3 < var6; ++var3) {
         for(int var4 = 0; var4 < var5; ++var4) {
            int var2 = var1;
            if(var4 + 6 < var5) {
               var2 = var1;
               if(var7[var3][var4] == 1) {
                  var2 = var1;
                  if(var7[var3][var4 + 1] == 0) {
                     var2 = var1;
                     if(var7[var3][var4 + 2] == 1) {
                        var2 = var1;
                        if(var7[var3][var4 + 3] == 1) {
                           var2 = var1;
                           if(var7[var3][var4 + 4] == 1) {
                              var2 = var1;
                              if(var7[var3][var4 + 5] == 0) {
                                 var2 = var1;
                                 if(var7[var3][var4 + 6] == 1) {
                                    label153: {
                                       if(var4 + 10 >= var5 || var7[var3][var4 + 7] != 0 || var7[var3][var4 + 8] != 0 || var7[var3][var4 + 9] != 0 || var7[var3][var4 + 10] != 0) {
                                          var2 = var1;
                                          if(var4 - 4 < 0) {
                                             break label153;
                                          }

                                          var2 = var1;
                                          if(var7[var3][var4 - 1] != 0) {
                                             break label153;
                                          }

                                          var2 = var1;
                                          if(var7[var3][var4 - 2] != 0) {
                                             break label153;
                                          }

                                          var2 = var1;
                                          if(var7[var3][var4 - 3] != 0) {
                                             break label153;
                                          }

                                          var2 = var1;
                                          if(var7[var3][var4 - 4] != 0) {
                                             break label153;
                                          }
                                       }

                                       var2 = var1 + 40;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var1 = var2;
            if(var3 + 6 < var6) {
               var1 = var2;
               if(var7[var3][var4] == 1) {
                  var1 = var2;
                  if(var7[var3 + 1][var4] == 0) {
                     var1 = var2;
                     if(var7[var3 + 2][var4] == 1) {
                        var1 = var2;
                        if(var7[var3 + 3][var4] == 1) {
                           var1 = var2;
                           if(var7[var3 + 4][var4] == 1) {
                              var1 = var2;
                              if(var7[var3 + 5][var4] == 0) {
                                 var1 = var2;
                                 if(var7[var3 + 6][var4] == 1) {
                                    if(var3 + 10 >= var6 || var7[var3 + 7][var4] != 0 || var7[var3 + 8][var4] != 0 || var7[var3 + 9][var4] != 0 || var7[var3 + 10][var4] != 0) {
                                       var1 = var2;
                                       if(var3 - 4 < 0) {
                                          continue;
                                       }

                                       var1 = var2;
                                       if(var7[var3 - 1][var4] != 0) {
                                          continue;
                                       }

                                       var1 = var2;
                                       if(var7[var3 - 2][var4] != 0) {
                                          continue;
                                       }

                                       var1 = var2;
                                       if(var7[var3 - 3][var4] != 0) {
                                          continue;
                                       }

                                       var1 = var2;
                                       if(var7[var3 - 4][var4] != 0) {
                                          continue;
                                       }
                                    }

                                    var1 = var2 + 40;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   public static int applyMaskPenaltyRule4(ByteMatrix var0) {
      byte[][] var7 = var0.getArray();
      int var5 = var0.getWidth();
      int var6 = var0.getHeight();
      int var1 = 0;

      int var2;
      int var3;
      int var4;
      for(var2 = 0; var1 < var6; ++var1) {
         for(var3 = 0; var3 < var5; var2 = var4) {
            var4 = var2;
            if(var7[var1][var3] == 1) {
               var4 = var2 + 1;
            }

            ++var3;
         }
      }

      var3 = var0.getHeight();
      var1 = var0.getWidth();
      return Math.abs((int)((double)var2 / (double)(var3 * var1) * 100.0D - 50.0D)) / 5 * 10;
   }

   public static boolean getDataMaskBit(int var0, int var1, int var2) {
      if(!QRCode.isValidMaskPattern(var0)) {
         throw new IllegalArgumentException("Invalid mask pattern");
      } else {
         switch(var0) {
         case 0:
            var0 = var2 + var1 & 1;
            break;
         case 1:
            var0 = var2 & 1;
            break;
         case 2:
            var0 = var1 % 3;
            break;
         case 3:
            var0 = (var2 + var1) % 3;
            break;
         case 4:
            var0 = (var2 >>> 1) + var1 / 3 & 1;
            break;
         case 5:
            var0 = var2 * var1;
            var0 = var0 % 3 + (var0 & 1);
            break;
         case 6:
            var0 = var2 * var1;
            var0 = var0 % 3 + (var0 & 1) & 1;
            break;
         case 7:
            var0 = var2 * var1 % 3 + (var2 + var1 & 1) & 1;
            break;
         default:
            throw new IllegalArgumentException("Invalid mask pattern: " + var0);
         }

         boolean var3;
         if(var0 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
