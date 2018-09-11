package android.support.v4.util;

import android.support.v4.util.ContainerHelpers;

public class LongSparseArray implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private long[] mKeys;
   private int mSize;
   private Object[] mValues;

   public LongSparseArray() {
      this(10);
   }

   public LongSparseArray(int var1) {
      this.mGarbage = false;
      if(var1 == 0) {
         this.mKeys = ContainerHelpers.EMPTY_LONGS;
         this.mValues = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = ContainerHelpers.idealLongArraySize(var1);
         this.mKeys = new long[var1];
         this.mValues = new Object[var1];
      }

      this.mSize = 0;
   }

   private void gc() {
      int var4 = this.mSize;
      int var2 = 0;
      long[] var5 = this.mKeys;
      Object[] var6 = this.mValues;

      int var1;
      for(int var3 = 0; var3 < var4; var2 = var1) {
         Object var7 = var6[var3];
         var1 = var2;
         if(var7 != DELETED) {
            if(var3 != var2) {
               var5[var2] = var5[var3];
               var6[var2] = var7;
               var6[var3] = null;
            }

            var1 = var2 + 1;
         }

         ++var3;
      }

      this.mGarbage = false;
      this.mSize = var2;
   }

   public void append(long var1, Object var3) {
      if(this.mSize != 0 && var1 <= this.mKeys[this.mSize - 1]) {
         this.put(var1, var3);
      } else {
         if(this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
         }

         int var5 = this.mSize;
         if(var5 >= this.mKeys.length) {
            int var4 = ContainerHelpers.idealLongArraySize(var5 + 1);
            long[] var7 = new long[var4];
            Object[] var6 = new Object[var4];
            System.arraycopy(this.mKeys, 0, var7, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
            this.mKeys = var7;
            this.mValues = var6;
         }

         this.mKeys[var5] = var1;
         this.mValues[var5] = var3;
         this.mSize = var5 + 1;
      }

   }

   public void clear() {
      int var2 = this.mSize;
      Object[] var3 = this.mValues;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = null;
      }

      this.mSize = 0;
      this.mGarbage = false;
   }

   public LongSparseArray clone() {
      // $FF: Couldn't be decompiled
   }

   public void delete(long var1) {
      int var3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if(var3 >= 0 && this.mValues[var3] != DELETED) {
         this.mValues[var3] = DELETED;
         this.mGarbage = true;
      }

   }

   public Object get(long var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      Object var5 = var3;
      if(var4 >= 0) {
         if(this.mValues[var4] == DELETED) {
            var5 = var3;
         } else {
            var5 = this.mValues[var4];
         }
      }

      return var5;
   }

   public int indexOfKey(long var1) {
      if(this.mGarbage) {
         this.gc();
      }

      return ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
   }

   public int indexOfValue(Object var1) {
      if(this.mGarbage) {
         this.gc();
      }

      int var2 = 0;

      while(true) {
         if(var2 >= this.mSize) {
            var2 = -1;
            break;
         }

         if(this.mValues[var2] == var1) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public long keyAt(int var1) {
      if(this.mGarbage) {
         this.gc();
      }

      return this.mKeys[var1];
   }

   public void put(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if(var4 >= 0) {
         this.mValues[var4] = var3;
      } else {
         int var5 = ~var4;
         if(var5 < this.mSize && this.mValues[var5] == DELETED) {
            this.mKeys[var5] = var1;
            this.mValues[var5] = var3;
         } else {
            var4 = var5;
            if(this.mGarbage) {
               var4 = var5;
               if(this.mSize >= this.mKeys.length) {
                  this.gc();
                  var4 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
               }
            }

            if(this.mSize >= this.mKeys.length) {
               var5 = ContainerHelpers.idealLongArraySize(this.mSize + 1);
               long[] var7 = new long[var5];
               Object[] var6 = new Object[var5];
               System.arraycopy(this.mKeys, 0, var7, 0, this.mKeys.length);
               System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
               this.mKeys = var7;
               this.mValues = var6;
            }

            if(this.mSize - var4 != 0) {
               System.arraycopy(this.mKeys, var4, this.mKeys, var4 + 1, this.mSize - var4);
               System.arraycopy(this.mValues, var4, this.mValues, var4 + 1, this.mSize - var4);
            }

            this.mKeys[var4] = var1;
            this.mValues[var4] = var3;
            ++this.mSize;
         }
      }

   }

   public void remove(long var1) {
      this.delete(var1);
   }

   public void removeAt(int var1) {
      if(this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public void setValueAt(int var1, Object var2) {
      if(this.mGarbage) {
         this.gc();
      }

      this.mValues[var1] = var2;
   }

   public int size() {
      if(this.mGarbage) {
         this.gc();
      }

      return this.mSize;
   }

   public String toString() {
      String var2;
      if(this.size() <= 0) {
         var2 = "{}";
      } else {
         StringBuilder var4 = new StringBuilder(this.mSize * 28);
         var4.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if(var1 > 0) {
               var4.append(", ");
            }

            var4.append(this.keyAt(var1));
            var4.append('=');
            Object var3 = this.valueAt(var1);
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
      if(this.mGarbage) {
         this.gc();
      }

      return this.mValues[var1];
   }
}
