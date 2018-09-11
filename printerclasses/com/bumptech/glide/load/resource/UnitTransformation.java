package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;

public class UnitTransformation implements Transformation {
   private static final Transformation TRANSFORMATION = new UnitTransformation();

   public static UnitTransformation get() {
      return (UnitTransformation)TRANSFORMATION;
   }

   public String getId() {
      return "";
   }

   public Resource transform(Resource var1, int var2, int var3) {
      return var1;
   }
}
