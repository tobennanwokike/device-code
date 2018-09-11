package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;

public final class PreFillType {
   static final Config DEFAULT_CONFIG;
   private final Config config;
   private final int height;
   private final int weight;
   private final int width;

   static {
      DEFAULT_CONFIG = Config.RGB_565;
   }

   PreFillType(int var1, int var2, Config var3, int var4) {
      if(var3 == null) {
         throw new NullPointerException("Config must not be null");
      } else {
         this.width = var1;
         this.height = var2;
         this.config = var3;
         this.weight = var4;
      }
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if(var1 instanceof PreFillType) {
         PreFillType var4 = (PreFillType)var1;
         var2 = var3;
         if(this.height == var4.height) {
            var2 = var3;
            if(this.width == var4.width) {
               var2 = var3;
               if(this.weight == var4.weight) {
                  var2 = var3;
                  if(this.config == var4.config) {
                     var2 = true;
                  }
               }
            }
         }
      }

      return var2;
   }

   Config getConfig() {
      return this.config;
   }

   int getHeight() {
      return this.height;
   }

   int getWeight() {
      return this.weight;
   }

   int getWidth() {
      return this.width;
   }

   public int hashCode() {
      return ((this.width * 31 + this.height) * 31 + this.config.hashCode()) * 31 + this.weight;
   }

   public String toString() {
      return "PreFillSize{width=" + this.width + ", height=" + this.height + ", config=" + this.config + ", weight=" + this.weight + '}';
   }

   public static class Builder {
      private Config config;
      private final int height;
      private int weight;
      private final int width;

      public Builder(int var1) {
         this(var1, var1);
      }

      public Builder(int var1, int var2) {
         this.weight = 1;
         if(var1 <= 0) {
            throw new IllegalArgumentException("Width must be > 0");
         } else if(var2 <= 0) {
            throw new IllegalArgumentException("Height must be > 0");
         } else {
            this.width = var1;
            this.height = var2;
         }
      }

      PreFillType build() {
         return new PreFillType(this.width, this.height, this.config, this.weight);
      }

      Config getConfig() {
         return this.config;
      }

      public PreFillType.Builder setConfig(Config var1) {
         this.config = var1;
         return this;
      }

      public PreFillType.Builder setWeight(int var1) {
         if(var1 <= 0) {
            throw new IllegalArgumentException("Weight must be > 0");
         } else {
            this.weight = var1;
            return this;
         }
      }
   }
}
