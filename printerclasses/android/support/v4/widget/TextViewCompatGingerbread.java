package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatGingerbread {
   private static final int LINES = 1;
   private static final String LOG_TAG = "TextViewCompatGingerbread";
   private static Field sMaxModeField;
   private static boolean sMaxModeFieldFetched;
   private static Field sMaximumField;
   private static boolean sMaximumFieldFetched;
   private static Field sMinModeField;
   private static boolean sMinModeFieldFetched;
   private static Field sMinimumField;
   private static boolean sMinimumFieldFetched;

   static Drawable[] getCompoundDrawablesRelative(@NonNull TextView var0) {
      return var0.getCompoundDrawables();
   }

   static int getMaxLines(TextView var0) {
      if(!sMaxModeFieldFetched) {
         sMaxModeField = retrieveField("mMaxMode");
         sMaxModeFieldFetched = true;
      }

      int var1;
      if(sMaxModeField != null && retrieveIntFromField(sMaxModeField, var0) == 1) {
         if(!sMaximumFieldFetched) {
            sMaximumField = retrieveField("mMaximum");
            sMaximumFieldFetched = true;
         }

         if(sMaximumField != null) {
            var1 = retrieveIntFromField(sMaximumField, var0);
            return var1;
         }
      }

      var1 = -1;
      return var1;
   }

   static int getMinLines(TextView var0) {
      if(!sMinModeFieldFetched) {
         sMinModeField = retrieveField("mMinMode");
         sMinModeFieldFetched = true;
      }

      int var1;
      if(sMinModeField != null && retrieveIntFromField(sMinModeField, var0) == 1) {
         if(!sMinimumFieldFetched) {
            sMinimumField = retrieveField("mMinimum");
            sMinimumFieldFetched = true;
         }

         if(sMinimumField != null) {
            var1 = retrieveIntFromField(sMinimumField, var0);
            return var1;
         }
      }

      var1 = -1;
      return var1;
   }

   private static Field retrieveField(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static int retrieveIntFromField(Field var0, TextView var1) {
      int var2;
      try {
         var2 = var0.getInt(var1);
      } catch (IllegalAccessException var3) {
         Log.d("TextViewCompatGingerbread", "Could not retrieve value of " + var0.getName() + " field.");
         var2 = -1;
      }

      return var2;
   }

   static void setTextAppearance(TextView var0, int var1) {
      var0.setTextAppearance(var0.getContext(), var1);
   }
}
