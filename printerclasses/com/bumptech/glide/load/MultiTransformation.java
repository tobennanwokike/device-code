package com.bumptech.glide.load;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiTransformation implements Transformation {
   private String id;
   private final Collection transformations;

   public MultiTransformation(Collection var1) {
      if(var1.size() < 1) {
         throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
      } else {
         this.transformations = var1;
      }
   }

   @SafeVarargs
   public MultiTransformation(Transformation... var1) {
      if(var1.length < 1) {
         throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
      } else {
         this.transformations = Arrays.asList(var1);
      }
   }

   public String getId() {
      if(this.id == null) {
         StringBuilder var1 = new StringBuilder();
         Iterator var2 = this.transformations.iterator();

         while(var2.hasNext()) {
            var1.append(((Transformation)var2.next()).getId());
         }

         this.id = var1.toString();
      }

      return this.id;
   }

   public Resource transform(Resource var1, int var2, int var3) {
      Resource var4 = var1;

      Resource var5;
      for(Iterator var6 = this.transformations.iterator(); var6.hasNext(); var4 = var5) {
         var5 = ((Transformation)var6.next()).transform(var4, var2, var3);
         if(var4 != null && !var4.equals(var1) && !var4.equals(var5)) {
            var4.recycle();
         }
      }

      return var4;
   }
}
