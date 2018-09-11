package android.support.v4.util;

import android.support.v4.util.ContainerHelpers;

public class SimpleArrayMap {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean DEBUG = false;
   private static final String TAG = "ArrayMap";
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   Object[] mArray;
   int[] mHashes;
   int mSize;

   public SimpleArrayMap() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public SimpleArrayMap(int var1) {
      if(var1 == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public SimpleArrayMap(SimpleArrayMap var1) {
      this();
      if(var1 != null) {
         this.putAll(var1);
      }

   }

   private void allocArrays(int param1) {
      // $FF: Couldn't be decompiled
   }

   private static void freeArrays(int[] param0, Object[] param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void clear() {
      if(this.mSize != 0) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      }

   }

   public boolean containsKey(Object var1) {
      boolean var2;
      if(this.indexOfKey(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      boolean var2;
      if(this.indexOfValue(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void ensureCapacity(int var1) {
      if(this.mHashes.length < var1) {
         int[] var3 = this.mHashes;
         Object[] var2 = this.mArray;
         this.allocArrays(var1);
         if(this.mSize > 0) {
            System.arraycopy(var3, 0, this.mHashes, 0, this.mSize);
            System.arraycopy(var2, 0, this.mArray, 0, this.mSize << 1);
         }

         freeArrays(var3, var2, this.mSize);
      }

   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public Object get(Object var1) {
      int var2 = this.indexOfKey(var1);
      if(var2 >= 0) {
         var1 = this.mArray[(var2 << 1) + 1];
      } else {
         var1 = null;
      }

      return var1;
   }

   public int hashCode() {
      int[] var9 = this.mHashes;
      Object[] var7 = this.mArray;
      int var2 = 0;
      int var3 = 0;
      int var1 = 1;

      for(int var5 = this.mSize; var3 < var5; var1 += 2) {
         Object var8 = var7[var1];
         int var6 = var9[var3];
         int var4;
         if(var8 == null) {
            var4 = 0;
         } else {
            var4 = var8.hashCode();
         }

         var2 += var4 ^ var6;
         ++var3;
      }

      return var2;
   }

   int indexOf(Object var1, int var2) {
      int var5 = this.mSize;
      int var3;
      if(var5 == 0) {
         var3 = -1;
      } else {
         int var4 = ContainerHelpers.binarySearch(this.mHashes, var5, var2);
         var3 = var4;
         if(var4 >= 0) {
            var3 = var4;
            if(!var1.equals(this.mArray[var4 << 1])) {
               for(var3 = var4 + 1; var3 < var5 && this.mHashes[var3] == var2; ++var3) {
                  if(var1.equals(this.mArray[var3 << 1])) {
                     return var3;
                  }
               }

               --var4;

               while(var4 >= 0 && this.mHashes[var4] == var2) {
                  if(var1.equals(this.mArray[var4 << 1])) {
                     var3 = var4;
                     return var3;
                  }

                  --var4;
               }

               var3 = ~var3;
            }
         }
      }

      return var3;
   }

   public int indexOfKey(Object var1) {
      int var2;
      if(var1 == null) {
         var2 = this.indexOfNull();
      } else {
         var2 = this.indexOf(var1, var1.hashCode());
      }

      return var2;
   }

   int indexOfNull() {
      int var3 = this.mSize;
      int var1;
      if(var3 == 0) {
         var1 = -1;
      } else {
         int var2 = ContainerHelpers.binarySearch(this.mHashes, var3, 0);
         var1 = var2;
         if(var2 >= 0) {
            var1 = var2;
            if(this.mArray[var2 << 1] != null) {
               for(var1 = var2 + 1; var1 < var3 && this.mHashes[var1] == 0; ++var1) {
                  if(this.mArray[var1 << 1] == null) {
                     return var1;
                  }
               }

               --var2;

               while(var2 >= 0 && this.mHashes[var2] == 0) {
                  if(this.mArray[var2 << 1] == null) {
                     var1 = var2;
                     return var1;
                  }

                  --var2;
               }

               var1 = ~var1;
            }
         }
      }

      return var1;
   }

   int indexOfValue(Object var1) {
      int var3 = this.mSize * 2;
      Object[] var4 = this.mArray;
      int var2;
      if(var1 == null) {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if(var4[var2] == null) {
               var2 >>= 1;
               return var2;
            }
         }
      } else {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if(var1.equals(var4[var2])) {
               var2 >>= 1;
               return var2;
            }
         }
      }

      var2 = -1;
      return var2;
   }

   public boolean isEmpty() {
      boolean var1;
      if(this.mSize <= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Object keyAt(int var1) {
      return this.mArray[var1 << 1];
   }

   public Object put(Object var1, Object var2) {
      byte var5 = 8;
      int var3;
      int var4;
      if(var1 == null) {
         var4 = 0;
         var3 = this.indexOfNull();
      } else {
         var4 = var1.hashCode();
         var3 = this.indexOf(var1, var4);
      }

      if(var3 >= 0) {
         var3 = (var3 << 1) + 1;
         var1 = this.mArray[var3];
         this.mArray[var3] = var2;
      } else {
         int var6 = ~var3;
         if(this.mSize >= this.mHashes.length) {
            if(this.mSize >= 8) {
               var3 = this.mSize + (this.mSize >> 1);
            } else {
               var3 = var5;
               if(this.mSize < 4) {
                  var3 = 4;
               }
            }

            int[] var8 = this.mHashes;
            Object[] var7 = this.mArray;
            this.allocArrays(var3);
            if(this.mHashes.length > 0) {
               System.arraycopy(var8, 0, this.mHashes, 0, var8.length);
               System.arraycopy(var7, 0, this.mArray, 0, var7.length);
            }

            freeArrays(var8, var7, this.mSize);
         }

         if(var6 < this.mSize) {
            System.arraycopy(this.mHashes, var6, this.mHashes, var6 + 1, this.mSize - var6);
            System.arraycopy(this.mArray, var6 << 1, this.mArray, var6 + 1 << 1, this.mSize - var6 << 1);
         }

         this.mHashes[var6] = var4;
         this.mArray[var6 << 1] = var1;
         this.mArray[(var6 << 1) + 1] = var2;
         ++this.mSize;
         var1 = null;
      }

      return var1;
   }

   public void putAll(SimpleArrayMap var1) {
      int var3 = var1.mSize;
      this.ensureCapacity(this.mSize + var3);
      if(this.mSize == 0) {
         if(var3 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var3);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var3 << 1);
            this.mSize = var3;
         }
      } else {
         for(int var2 = 0; var2 < var3; ++var2) {
            this.put(var1.keyAt(var2), var1.valueAt(var2));
         }
      }

   }

   public Object remove(Object var1) {
      int var2 = this.indexOfKey(var1);
      if(var2 >= 0) {
         var1 = this.removeAt(var2);
      } else {
         var1 = null;
      }

      return var1;
   }

   public Object removeAt(int var1) {
      int var2 = 8;
      Object var3 = this.mArray[(var1 << 1) + 1];
      if(this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      } else if(this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
         if(this.mSize > 8) {
            var2 = this.mSize + (this.mSize >> 1);
         }

         int[] var5 = this.mHashes;
         Object[] var4 = this.mArray;
         this.allocArrays(var2);
         --this.mSize;
         if(var1 > 0) {
            System.arraycopy(var5, 0, this.mHashes, 0, var1);
            System.arraycopy(var4, 0, this.mArray, 0, var1 << 1);
         }

         if(var1 < this.mSize) {
            System.arraycopy(var5, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(var4, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
         }
      } else {
         --this.mSize;
         if(var1 < this.mSize) {
            System.arraycopy(this.mHashes, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(this.mArray, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
         }

         this.mArray[this.mSize << 1] = null;
         this.mArray[(this.mSize << 1) + 1] = null;
      }

      return var3;
   }

   public Object setValueAt(int var1, Object var2) {
      var1 = (var1 << 1) + 1;
      Object var3 = this.mArray[var1];
      this.mArray[var1] = var2;
      return var3;
   }

   public int size() {
      return this.mSize;
   }

   public String toString() {
      String var2;
      if(this.isEmpty()) {
         var2 = "{}";
      } else {
         StringBuilder var4 = new StringBuilder(this.mSize * 28);
         var4.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if(var1 > 0) {
               var4.append(", ");
            }

            Object var3 = this.keyAt(var1);
            if(var3 != this) {
               var4.append(var3);
            } else {
               var4.append("(this Map)");
            }

            var4.append('=');
            var3 = this.valueAt(var1);
            if(var3 != this) {
               var4.append(var3);
            } else {
               var4.append("(this Map)");
            }
         }

         var4.append('}');
         var2 = var4.toString();
      }

      return var2;
   }

   public Object valueAt(int var1) {
      return this.mArray[(var1 << 1) + 1];
   }
}
