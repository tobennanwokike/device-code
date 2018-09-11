package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

class PrettyPrintTreeMap extends TreeMap {
   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("( ");
      Iterator var3 = this.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var1 = (Entry)var3.next();
         var2.append('{').append(var1.getKey()).append(':').append(var1.getValue()).append("}, ");
      }

      if(!this.isEmpty()) {
         var2.replace(var2.length() - 2, var2.length(), "");
      }

      return var2.append(" )").toString();
   }
}
