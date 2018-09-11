package android.support.v4.text;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.ICUCompat;
import android.support.v4.text.TextUtilsCompatJellybeanMr1;
import java.util.Locale;

public final class TextUtilsCompat {
   static String ARAB_SCRIPT_SUBTAG;
   static String HEBR_SCRIPT_SUBTAG;
   private static final TextUtilsCompat.TextUtilsCompatImpl IMPL;
   public static final Locale ROOT;

   static {
      if(VERSION.SDK_INT >= 17) {
         IMPL = new TextUtilsCompat.TextUtilsCompatJellybeanMr1Impl();
      } else {
         IMPL = new TextUtilsCompat.TextUtilsCompatImpl();
      }

      ROOT = new Locale("", "");
      ARAB_SCRIPT_SUBTAG = "Arab";
      HEBR_SCRIPT_SUBTAG = "Hebr";
   }

   public static int getLayoutDirectionFromLocale(@Nullable Locale var0) {
      return IMPL.getLayoutDirectionFromLocale(var0);
   }

   @NonNull
   public static String htmlEncode(@NonNull String var0) {
      return IMPL.htmlEncode(var0);
   }

   private static class TextUtilsCompatImpl {
      private static int getLayoutDirectionFromFirstChar(@NonNull Locale var0) {
         byte var1 = 0;
         switch(Character.getDirectionality(var0.getDisplayName(var0).charAt(0))) {
         case 1:
         case 2:
            var1 = 1;
         default:
            return var1;
         }
      }

      public int getLayoutDirectionFromLocale(@Nullable Locale var1) {
         int var2;
         if(var1 != null && !var1.equals(TextUtilsCompat.ROOT)) {
            String var3 = ICUCompat.maximizeAndGetScript(var1);
            if(var3 == null) {
               var2 = getLayoutDirectionFromFirstChar(var1);
               return var2;
            }

            if(var3.equalsIgnoreCase(TextUtilsCompat.ARAB_SCRIPT_SUBTAG) || var3.equalsIgnoreCase(TextUtilsCompat.HEBR_SCRIPT_SUBTAG)) {
               var2 = 1;
               return var2;
            }
         }

         var2 = 0;
         return var2;
      }

      @NonNull
      public String htmlEncode(@NonNull String var1) {
         StringBuilder var4 = new StringBuilder();

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var2 = var1.charAt(var3);
            switch(var2) {
            case '\"':
               var4.append("&quot;");
               break;
            case '&':
               var4.append("&amp;");
               break;
            case '\'':
               var4.append("&#39;");
               break;
            case '<':
               var4.append("&lt;");
               break;
            case '>':
               var4.append("&gt;");
               break;
            default:
               var4.append(var2);
            }
         }

         return var4.toString();
      }
   }

   private static class TextUtilsCompatJellybeanMr1Impl extends TextUtilsCompat.TextUtilsCompatImpl {
      public int getLayoutDirectionFromLocale(@Nullable Locale var1) {
         return TextUtilsCompatJellybeanMr1.getLayoutDirectionFromLocale(var1);
      }

      @NonNull
      public String htmlEncode(@NonNull String var1) {
         return TextUtilsCompatJellybeanMr1.htmlEncode(var1);
      }
   }
}
