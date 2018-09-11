package android.support.graphics.drawable;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

class PathParser {
   private static final String LOGTAG = "PathParser";

   private static void addNode(ArrayList var0, char var1, float[] var2) {
      var0.add(new PathParser.PathDataNode(var1, var2));
   }

   public static boolean canMorph(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      boolean var4 = false;
      boolean var3 = var4;
      if(var0 != null) {
         if(var1 == null) {
            var3 = var4;
         } else {
            var3 = var4;
            if(var0.length == var1.length) {
               int var2 = 0;

               while(true) {
                  if(var2 >= var0.length) {
                     var3 = true;
                     break;
                  }

                  var3 = var4;
                  if(var0[var2].type != var1[var2].type) {
                     break;
                  }

                  var3 = var4;
                  if(var0[var2].params.length != var1[var2].params.length) {
                     break;
                  }

                  ++var2;
               }
            }
         }
      }

      return var3;
   }

   static float[] copyOfRange(float[] var0, int var1, int var2) {
      if(var1 > var2) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.length;
         if(var1 >= 0 && var1 <= var3) {
            var2 -= var1;
            var3 = Math.min(var2, var3 - var1);
            float[] var4 = new float[var2];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         } else {
            throw new ArrayIndexOutOfBoundsException();
         }
      }
   }

   public static PathParser.PathDataNode[] createNodesFromPathData(String var0) {
      PathParser.PathDataNode[] var6;
      if(var0 == null) {
         var6 = null;
      } else {
         int var1 = 0;
         int var2 = 1;

         ArrayList var3;
         for(var3 = new ArrayList(); var2 < var0.length(); var1 = var2++) {
            var2 = nextStart(var0, var2);
            String var4 = var0.substring(var1, var2).trim();
            if(var4.length() > 0) {
               float[] var5 = getFloats(var4);
               addNode(var3, var4.charAt(0), var5);
            }
         }

         if(var2 - var1 == 1 && var1 < var0.length()) {
            addNode(var3, var0.charAt(var1), new float[0]);
         }

         var6 = (PathParser.PathDataNode[])var3.toArray(new PathParser.PathDataNode[var3.size()]);
      }

      return var6;
   }

   public static Path createPathFromPathData(String var0) {
      Path var1 = new Path();
      PathParser.PathDataNode[] var2 = createNodesFromPathData(var0);
      Path var4;
      if(var2 != null) {
         try {
            PathParser.PathDataNode.nodesToPath(var2, var1);
         } catch (RuntimeException var3) {
            throw new RuntimeException("Error in parsing " + var0, var3);
         }

         var4 = var1;
      } else {
         var4 = null;
      }

      return var4;
   }

   public static PathParser.PathDataNode[] deepCopyNodes(PathParser.PathDataNode[] var0) {
      PathParser.PathDataNode[] var2;
      if(var0 == null) {
         var2 = null;
      } else {
         PathParser.PathDataNode[] var3 = new PathParser.PathDataNode[var0.length];
         int var1 = 0;

         while(true) {
            var2 = var3;
            if(var1 >= var0.length) {
               break;
            }

            var3[var1] = new PathParser.PathDataNode(var0[var1]);
            ++var1;
         }
      }

      return var2;
   }

   private static void extract(String var0, int var1, PathParser.ExtractFloatResult var2) {
      int var6 = var1;
      boolean var7 = false;
      var2.mEndWithNegOrDot = false;
      boolean var5 = false;

      boolean var9;
      for(boolean var8 = false; var6 < var0.length(); var5 = var9) {
         boolean var10 = false;
         boolean var3;
         boolean var4;
         switch(var0.charAt(var6)) {
         case ' ':
         case ',':
            var3 = true;
            var4 = var10;
            var9 = var5;
            break;
         case '-':
            var3 = var7;
            var4 = var10;
            var9 = var5;
            if(var6 != var1) {
               var3 = var7;
               var4 = var10;
               var9 = var5;
               if(!var8) {
                  var3 = true;
                  var2.mEndWithNegOrDot = true;
                  var4 = var10;
                  var9 = var5;
               }
            }
            break;
         case '.':
            if(!var5) {
               var9 = true;
               var3 = var7;
               var4 = var10;
            } else {
               var3 = true;
               var2.mEndWithNegOrDot = true;
               var4 = var10;
               var9 = var5;
            }
            break;
         case 'E':
         case 'e':
            var4 = true;
            var3 = var7;
            var9 = var5;
            break;
         default:
            var9 = var5;
            var4 = var10;
            var3 = var7;
         }

         if(var3) {
            break;
         }

         ++var6;
         var7 = var3;
         var8 = var4;
      }

      var2.mEndPosition = var6;
   }

   private static float[] getFloats(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static int nextStart(String var0, int var1) {
      while(var1 < var0.length()) {
         char var2 = var0.charAt(var1);
         if(((var2 - 65) * (var2 - 90) <= 0 || (var2 - 97) * (var2 - 122) <= 0) && var2 != 101 && var2 != 69) {
            break;
         }

         ++var1;
      }

      return var1;
   }

   public static void updateNodes(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var0[var2].type = var1[var2].type;

         for(int var3 = 0; var3 < var1[var2].params.length; ++var3) {
            var0[var2].params[var3] = var1[var2].params[var3];
         }
      }

   }

   private static class ExtractFloatResult {
      int mEndPosition;
      boolean mEndWithNegOrDot;
   }

   public static class PathDataNode {
      float[] params;
      char type;

      PathDataNode(char var1, float[] var2) {
         this.type = var1;
         this.params = var2;
      }

      PathDataNode(PathParser.PathDataNode var1) {
         this.type = var1.type;
         this.params = PathParser.copyOfRange(var1.params, 0, var1.params.length);
      }

      private static void addCommand(Path var0, float[] var1, char var2, char var3, float[] var4) {
         byte var14 = 2;
         float var5 = var1[0];
         float var6 = var1[1];
         float var8 = var1[2];
         float var10 = var1[3];
         float var7 = var1[4];
         float var9 = var1[5];
         switch(var3) {
         case 'A':
         case 'a':
            var14 = 7;
            break;
         case 'C':
         case 'c':
            var14 = 6;
            break;
         case 'H':
         case 'V':
         case 'h':
         case 'v':
            var14 = 1;
            break;
         case 'L':
         case 'M':
         case 'T':
         case 'l':
         case 'm':
         case 't':
            var14 = 2;
            break;
         case 'Q':
         case 'S':
         case 'q':
         case 's':
            var14 = 4;
            break;
         case 'Z':
         case 'z':
            var0.close();
            var5 = var7;
            var6 = var9;
            var8 = var7;
            var10 = var9;
            var0.moveTo(var7, var9);
         }

         int var15 = 0;
         float var11 = var9;
         var9 = var7;
         float var12 = var10;

         float var13;
         for(var13 = var8; var15 < var4.length; var11 = var10) {
            boolean var16;
            boolean var17;
            switch(var3) {
            case 'A':
               var12 = var4[var15 + 5];
               var7 = var4[var15 + 6];
               var13 = var4[var15 + 0];
               var10 = var4[var15 + 1];
               var8 = var4[var15 + 2];
               if(var4[var15 + 3] != 0.0F) {
                  var16 = true;
               } else {
                  var16 = false;
               }

               if(var4[var15 + 4] != 0.0F) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               drawArc(var0, var5, var6, var12, var7, var13, var10, var8, var16, var17);
               var5 = var4[var15 + 5];
               var6 = var4[var15 + 6];
               var7 = var5;
               var8 = var6;
               var10 = var11;
               break;
            case 'C':
               var0.cubicTo(var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3], var4[var15 + 4], var4[var15 + 5]);
               var5 = var4[var15 + 4];
               var6 = var4[var15 + 5];
               var7 = var4[var15 + 2];
               var8 = var4[var15 + 3];
               var10 = var11;
               break;
            case 'H':
               var0.lineTo(var4[var15 + 0], var6);
               var5 = var4[var15 + 0];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            case 'L':
               var0.lineTo(var4[var15 + 0], var4[var15 + 1]);
               var5 = var4[var15 + 0];
               var6 = var4[var15 + 1];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            case 'M':
               var5 = var4[var15 + 0];
               var6 = var4[var15 + 1];
               if(var15 > 0) {
                  var0.lineTo(var4[var15 + 0], var4[var15 + 1]);
                  var7 = var13;
                  var8 = var12;
                  var10 = var11;
               } else {
                  var0.moveTo(var4[var15 + 0], var4[var15 + 1]);
                  var9 = var5;
                  var10 = var6;
                  var7 = var13;
                  var8 = var12;
               }
               break;
            case 'Q':
               var0.quadTo(var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3]);
               var7 = var4[var15 + 0];
               var8 = var4[var15 + 1];
               var5 = var4[var15 + 2];
               var6 = var4[var15 + 3];
               var10 = var11;
               break;
            case 'S':
               var8 = var5;
               var7 = var6;
               if(var2 == 99 || var2 == 115 || var2 == 67 || var2 == 83) {
                  var8 = 2.0F * var5 - var13;
                  var7 = 2.0F * var6 - var12;
               }

               var0.cubicTo(var8, var7, var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3]);
               var7 = var4[var15 + 0];
               var8 = var4[var15 + 1];
               var5 = var4[var15 + 2];
               var6 = var4[var15 + 3];
               var10 = var11;
               break;
            case 'T':
               var8 = var5;
               var7 = var6;
               if(var2 == 113 || var2 == 116 || var2 == 81 || var2 == 84) {
                  var8 = 2.0F * var5 - var13;
                  var7 = 2.0F * var6 - var12;
               }

               var0.quadTo(var8, var7, var4[var15 + 0], var4[var15 + 1]);
               var5 = var8;
               var8 = var7;
               var12 = var4[var15 + 0];
               var6 = var4[var15 + 1];
               var7 = var5;
               var10 = var11;
               var5 = var12;
               break;
            case 'V':
               var0.lineTo(var5, var4[var15 + 0]);
               var6 = var4[var15 + 0];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            case 'a':
               var7 = var4[var15 + 5];
               var10 = var4[var15 + 6];
               var12 = var4[var15 + 0];
               var8 = var4[var15 + 1];
               var13 = var4[var15 + 2];
               if(var4[var15 + 3] != 0.0F) {
                  var16 = true;
               } else {
                  var16 = false;
               }

               if(var4[var15 + 4] != 0.0F) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               drawArc(var0, var5, var6, var7 + var5, var10 + var6, var12, var8, var13, var16, var17);
               var5 += var4[var15 + 5];
               var6 += var4[var15 + 6];
               var7 = var5;
               var8 = var6;
               var10 = var11;
               break;
            case 'c':
               var0.rCubicTo(var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3], var4[var15 + 4], var4[var15 + 5]);
               var7 = var5 + var4[var15 + 2];
               var8 = var6 + var4[var15 + 3];
               var5 += var4[var15 + 4];
               var6 += var4[var15 + 5];
               var10 = var11;
               break;
            case 'h':
               var0.rLineTo(var4[var15 + 0], 0.0F);
               var5 += var4[var15 + 0];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            case 'l':
               var0.rLineTo(var4[var15 + 0], var4[var15 + 1]);
               var5 += var4[var15 + 0];
               var6 += var4[var15 + 1];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            case 'm':
               var5 += var4[var15 + 0];
               var6 += var4[var15 + 1];
               if(var15 > 0) {
                  var0.rLineTo(var4[var15 + 0], var4[var15 + 1]);
                  var7 = var13;
                  var8 = var12;
                  var10 = var11;
               } else {
                  var0.rMoveTo(var4[var15 + 0], var4[var15 + 1]);
                  var9 = var5;
                  var10 = var6;
                  var7 = var13;
                  var8 = var12;
               }
               break;
            case 'q':
               var0.rQuadTo(var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3]);
               var7 = var5 + var4[var15 + 0];
               var8 = var6 + var4[var15 + 1];
               var5 += var4[var15 + 2];
               var6 += var4[var15 + 3];
               var10 = var11;
               break;
            case 's':
               var8 = 0.0F;
               var7 = 0.0F;
               if(var2 == 99 || var2 == 115 || var2 == 67 || var2 == 83) {
                  var8 = var5 - var13;
                  var7 = var6 - var12;
               }

               var0.rCubicTo(var8, var7, var4[var15 + 0], var4[var15 + 1], var4[var15 + 2], var4[var15 + 3]);
               var7 = var5 + var4[var15 + 0];
               var8 = var6 + var4[var15 + 1];
               var5 += var4[var15 + 2];
               var6 += var4[var15 + 3];
               var10 = var11;
               break;
            case 't':
               var8 = 0.0F;
               var7 = 0.0F;
               if(var2 == 113 || var2 == 116 || var2 == 81 || var2 == 84) {
                  var8 = var5 - var13;
                  var7 = var6 - var12;
               }

               var0.rQuadTo(var8, var7, var4[var15 + 0], var4[var15 + 1]);
               var8 += var5;
               var10 = var6 + var7;
               var5 += var4[var15 + 0];
               var6 += var4[var15 + 1];
               var7 = var8;
               var8 = var10;
               var10 = var11;
               break;
            case 'v':
               var0.rLineTo(0.0F, var4[var15 + 0]);
               var6 += var4[var15 + 0];
               var7 = var13;
               var8 = var12;
               var10 = var11;
               break;
            default:
               var10 = var11;
               var8 = var12;
               var7 = var13;
            }

            var2 = var3;
            var15 += var14;
            var13 = var7;
            var12 = var8;
         }

         var1[0] = var5;
         var1[1] = var6;
         var1[2] = var13;
         var1[3] = var12;
         var1[4] = var9;
         var1[5] = var11;
      }

      private static void arcToBezier(Path var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         int var38 = (int)Math.ceil(Math.abs(4.0D * var17 / 3.141592653589793D));
         double var29 = Math.cos(var13);
         double var31 = Math.sin(var13);
         double var19 = Math.cos(var15);
         double var21 = Math.sin(var15);
         var13 = -var5 * var29 * var21 - var7 * var31 * var19;
         var19 = -var5 * var31 * var21 + var7 * var29 * var19;
         double var33 = var17 / (double)var38;
         int var37 = 0;
         var17 = var15;

         for(var15 = var19; var37 < var38; ++var37) {
            double var27 = var17 + var33;
            double var35 = Math.sin(var27);
            var21 = Math.cos(var27);
            double var25 = var5 * var29 * var21 + var1 - var7 * var31 * var35;
            double var23 = var5 * var31 * var21 + var3 + var7 * var29 * var35;
            var19 = -var5 * var29 * var35 - var7 * var31 * var21;
            var21 = -var5 * var31 * var35 + var7 * var29 * var21;
            var35 = Math.tan((var27 - var17) / 2.0D);
            var17 = Math.sin(var27 - var17) * (Math.sqrt(4.0D + 3.0D * var35 * var35) - 1.0D) / 3.0D;
            var0.rCubicTo((float)(var9 + var17 * var13) - (float)var9, (float)(var11 + var17 * var15) - (float)var11, (float)(var25 - var17 * var19) - (float)var9, (float)(var23 - var17 * var21) - (float)var11, (float)var25 - (float)var9, (float)var23 - (float)var11);
            var17 = var27;
            var9 = var25;
            var11 = var23;
            var13 = var19;
            var15 = var21;
         }

      }

      private static void drawArc(Path var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
         double var18 = Math.toRadians((double)var7);
         double var22 = Math.cos(var18);
         double var20 = Math.sin(var18);
         double var24 = ((double)var1 * var22 + (double)var2 * var20) / (double)var5;
         double var26 = ((double)(-var1) * var20 + (double)var2 * var22) / (double)var6;
         double var16 = ((double)var3 * var22 + (double)var4 * var20) / (double)var5;
         double var10 = ((double)(-var3) * var20 + (double)var4 * var22) / (double)var6;
         double var30 = var24 - var16;
         double var28 = var26 - var10;
         double var14 = (var24 + var16) / 2.0D;
         double var12 = (var26 + var10) / 2.0D;
         double var34 = var30 * var30 + var28 * var28;
         if(var34 == 0.0D) {
            Log.w("PathParser", " Points are coincident");
         } else {
            double var32 = 1.0D / var34 - 0.25D;
            if(var32 < 0.0D) {
               Log.w("PathParser", "Points are too far apart " + var34);
               float var36 = (float)(Math.sqrt(var34) / 1.99999D);
               drawArc(var0, var1, var2, var3, var4, var5 * var36, var6 * var36, var7, var8, var9);
            } else {
               var32 = Math.sqrt(var32);
               var30 = var32 * var30;
               var28 = var32 * var28;
               if(var8 == var9) {
                  var14 -= var28;
                  var12 += var30;
               } else {
                  var14 += var28;
                  var12 -= var30;
               }

               var24 = Math.atan2(var26 - var12, var24 - var14);
               var16 = Math.atan2(var10 - var12, var16 - var14) - var24;
               if(var16 >= 0.0D) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var10 = var16;
               if(var9 != var8) {
                  if(var16 > 0.0D) {
                     var10 = var16 - 6.283185307179586D;
                  } else {
                     var10 = var16 + 6.283185307179586D;
                  }
               }

               var14 *= (double)var5;
               var12 *= (double)var6;
               arcToBezier(var0, var14 * var22 - var12 * var20, var14 * var20 + var12 * var22, (double)var5, (double)var6, (double)var1, (double)var2, var18, var24, var10);
            }
         }

      }

      public static void nodesToPath(PathParser.PathDataNode[] var0, Path var1) {
         float[] var4 = new float[6];
         char var2 = 109;

         for(int var3 = 0; var3 < var0.length; ++var3) {
            addCommand(var1, var4, var2, var0[var3].type, var0[var3].params);
            var2 = var0[var3].type;
         }

      }

      public void interpolatePathDataNode(PathParser.PathDataNode var1, PathParser.PathDataNode var2, float var3) {
         for(int var4 = 0; var4 < var1.params.length; ++var4) {
            this.params[var4] = var1.params[var4] * (1.0F - var3) + var2.params[var4] * var3;
         }

      }
   }
}
