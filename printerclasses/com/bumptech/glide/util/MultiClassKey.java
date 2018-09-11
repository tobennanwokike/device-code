package com.bumptech.glide.util;

public class MultiClassKey {
   private Class first;
   private Class second;

   public MultiClassKey() {
   }

   public MultiClassKey(Class var1, Class var2) {
      this.set(var1, var2);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if(this != var1) {
         if(var1 != null && this.getClass() == var1.getClass()) {
            MultiClassKey var3 = (MultiClassKey)var1;
            if(!this.first.equals(var3.first)) {
               var2 = false;
            } else if(!this.second.equals(var3.second)) {
               var2 = false;
            }
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.first.hashCode() * 31 + this.second.hashCode();
   }

   public void set(Class var1, Class var2) {
      this.first = var1;
      this.second = var2;
   }

   public String toString() {
      return "MultiClassKey{first=" + this.first + ", second=" + this.second + '}';
   }
}
