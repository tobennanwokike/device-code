package android.support.v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.GrowingArrayUtils;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class AppCompatColorStateListInflater {
   private static final int DEFAULT_COLOR = -65536;

   @NonNull
   public static ColorStateList createFromXml(@NonNull Resources var0, @NonNull XmlPullParser var1, @Nullable Theme var2) throws XmlPullParserException, IOException {
      AttributeSet var4 = Xml.asAttributeSet(var1);

      int var3;
      do {
         var3 = var1.next();
      } while(var3 != 2 && var3 != 1);

      if(var3 != 2) {
         throw new XmlPullParserException("No start tag found");
      } else {
         return createFromXmlInner(var0, var1, var4, var2);
      }
   }

   @NonNull
   private static ColorStateList createFromXmlInner(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
      String var4 = var1.getName();
      if(!var4.equals("selector")) {
         throw new XmlPullParserException(var1.getPositionDescription() + ": invalid color state list tag " + var4);
      } else {
         return inflate(var0, var1, var2, var3);
      }
   }

   private static ColorStateList inflate(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
      int var10 = var1.getDepth() + 1;
      int[][] var13 = new int[20][];
      int[] var14 = new int[var13.length];
      int var6 = 0;

      while(true) {
         int var5 = var1.next();
         if(var5 == 1) {
            break;
         }

         int var7 = var1.getDepth();
         if(var7 < var10 && var5 == 3) {
            break;
         }

         if(var5 == 2 && var7 <= var10 && var1.getName().equals("item")) {
            TypedArray var15 = obtainAttributes(var0, var3, var2, R.styleable.ColorStateListItem);
            int var12 = var15.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float var4 = 1.0F;
            if(var15.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
               var4 = var15.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
            } else if(var15.hasValue(R.styleable.ColorStateListItem_alpha)) {
               var4 = var15.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
            }

            var15.recycle();
            int var11 = var2.getAttributeCount();
            int[] var18 = new int[var11];
            var7 = 0;

            for(var5 = 0; var7 < var11; ++var7) {
               int var8 = var2.getAttributeNameResource(var7);
               if(var8 != 16843173 && var8 != 16843551 && var8 != R.attr.alpha) {
                  int var9 = var5 + 1;
                  if(!var2.getAttributeBooleanValue(var7, false)) {
                     var8 = -var8;
                  }

                  var18[var5] = var8;
                  var5 = var9;
               }
            }

            var18 = StateSet.trimStateSet(var18, var5);
            var5 = modulateColorAlpha(var12, var4);
            if(var6 != 0 && var18.length == 0) {
               ;
            }

            var14 = GrowingArrayUtils.append(var14, var6, var5);
            var13 = (int[][])GrowingArrayUtils.append(var13, var6, var18);
            ++var6;
         }
      }

      int[] var16 = new int[var6];
      int[][] var17 = new int[var6][];
      System.arraycopy(var14, 0, var16, 0, var6);
      System.arraycopy(var13, 0, var17, 0, var6);
      return new ColorStateList(var17, var16);
   }

   private static int modulateColorAlpha(int var0, float var1) {
      return ColorUtils.setAlphaComponent(var0, Math.round((float)Color.alpha(var0) * var1));
   }

   private static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      TypedArray var4;
      if(var1 == null) {
         var4 = var0.obtainAttributes(var2, var3);
      } else {
         var4 = var1.obtainStyledAttributes(var2, var3, 0, 0);
      }

      return var4;
   }
}
