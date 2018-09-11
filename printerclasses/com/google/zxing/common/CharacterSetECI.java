package com.google.zxing.common;

import com.google.zxing.common.ECI;
import java.util.Hashtable;

public final class CharacterSetECI extends ECI {
   private static Hashtable NAME_TO_ECI;
   private static Hashtable VALUE_TO_ECI;
   private final String encodingName;

   private CharacterSetECI(int var1, String var2) {
      super(var1);
      this.encodingName = var2;
   }

   private static void addCharacterSet(int var0, String var1) {
      CharacterSetECI var2 = new CharacterSetECI(var0, var1);
      VALUE_TO_ECI.put(new Integer(var0), var2);
      NAME_TO_ECI.put(var1, var2);
   }

   private static void addCharacterSet(int var0, String[] var1) {
      byte var2 = 0;
      CharacterSetECI var3 = new CharacterSetECI(var0, var1[0]);
      VALUE_TO_ECI.put(new Integer(var0), var3);

      for(var0 = var2; var0 < var1.length; ++var0) {
         NAME_TO_ECI.put(var1[var0], var3);
      }

   }

   public static CharacterSetECI getCharacterSetECIByName(String var0) {
      if(NAME_TO_ECI == null) {
         initialize();
      }

      return (CharacterSetECI)NAME_TO_ECI.get(var0);
   }

   public static CharacterSetECI getCharacterSetECIByValue(int var0) {
      if(VALUE_TO_ECI == null) {
         initialize();
      }

      if(var0 >= 0 && var0 < 900) {
         return (CharacterSetECI)VALUE_TO_ECI.get(new Integer(var0));
      } else {
         throw new IllegalArgumentException("Bad ECI value: " + var0);
      }
   }

   private static void initialize() {
      VALUE_TO_ECI = new Hashtable(29);
      NAME_TO_ECI = new Hashtable(29);
      addCharacterSet(0, (String)"Cp437");
      addCharacterSet(1, (String[])(new String[]{"ISO8859_1", "ISO-8859-1"}));
      addCharacterSet(2, (String)"Cp437");
      addCharacterSet(3, (String[])(new String[]{"ISO8859_1", "ISO-8859-1"}));
      addCharacterSet(4, (String)"ISO8859_2");
      addCharacterSet(5, (String)"ISO8859_3");
      addCharacterSet(6, (String)"ISO8859_4");
      addCharacterSet(7, (String)"ISO8859_5");
      addCharacterSet(8, (String)"ISO8859_6");
      addCharacterSet(9, (String)"ISO8859_7");
      addCharacterSet(10, (String)"ISO8859_8");
      addCharacterSet(11, (String)"ISO8859_9");
      addCharacterSet(12, (String)"ISO8859_10");
      addCharacterSet(13, (String)"ISO8859_11");
      addCharacterSet(15, (String)"ISO8859_13");
      addCharacterSet(16, (String)"ISO8859_14");
      addCharacterSet(17, (String)"ISO8859_15");
      addCharacterSet(18, (String)"ISO8859_16");
      addCharacterSet(20, (String[])(new String[]{"SJIS", "Shift_JIS"}));
   }

   public String getEncodingName() {
      return this.encodingName;
   }
}
