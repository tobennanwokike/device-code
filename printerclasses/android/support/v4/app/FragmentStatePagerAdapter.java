package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FragmentStatePagerAdapter extends PagerAdapter {
   private static final boolean DEBUG = false;
   private static final String TAG = "FragmentStatePagerAdapter";
   private FragmentTransaction mCurTransaction = null;
   private Fragment mCurrentPrimaryItem = null;
   private final FragmentManager mFragmentManager;
   private ArrayList mFragments = new ArrayList();
   private ArrayList mSavedState = new ArrayList();

   public FragmentStatePagerAdapter(FragmentManager var1) {
      this.mFragmentManager = var1;
   }

   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      Fragment var6 = (Fragment)var3;
      if(this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      while(this.mSavedState.size() <= var2) {
         this.mSavedState.add((Object)null);
      }

      ArrayList var4 = this.mSavedState;
      Fragment.SavedState var5;
      if(var6.isAdded()) {
         var5 = this.mFragmentManager.saveFragmentInstanceState(var6);
      } else {
         var5 = null;
      }

      var4.set(var2, var5);
      this.mFragments.set(var2, (Object)null);
      this.mCurTransaction.remove(var6);
   }

   public void finishUpdate(ViewGroup var1) {
      if(this.mCurTransaction != null) {
         this.mCurTransaction.commitNowAllowingStateLoss();
         this.mCurTransaction = null;
      }

   }

   public abstract Fragment getItem(int var1);

   public Object instantiateItem(ViewGroup var1, int var2) {
      Fragment var3;
      Fragment var5;
      if(this.mFragments.size() > var2) {
         var3 = (Fragment)this.mFragments.get(var2);
         if(var3 != null) {
            var5 = var3;
            return var5;
         }
      }

      if(this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      var3 = this.getItem(var2);
      if(this.mSavedState.size() > var2) {
         Fragment.SavedState var4 = (Fragment.SavedState)this.mSavedState.get(var2);
         if(var4 != null) {
            var3.setInitialSavedState(var4);
         }
      }

      while(this.mFragments.size() <= var2) {
         this.mFragments.add((Object)null);
      }

      var3.setMenuVisibility(false);
      var3.setUserVisibleHint(false);
      this.mFragments.set(var2, var3);
      this.mCurTransaction.add(var1.getId(), var3);
      var5 = var3;
      return var5;
   }

   public boolean isViewFromObject(View var1, Object var2) {
      boolean var3;
      if(((Fragment)var2).getView() == var1) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void restoreState(Parcelable var1, ClassLoader var2) {
      if(var1 != null) {
         Bundle var6 = (Bundle)var1;
         var6.setClassLoader(var2);
         Parcelable[] var7 = var6.getParcelableArray("states");
         this.mSavedState.clear();
         this.mFragments.clear();
         int var3;
         if(var7 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               this.mSavedState.add((Fragment.SavedState)var7[var3]);
            }
         }

         Iterator var5 = var6.keySet().iterator();

         while(true) {
            while(true) {
               String var8;
               do {
                  if(!var5.hasNext()) {
                     return;
                  }

                  var8 = (String)var5.next();
               } while(!var8.startsWith("f"));

               var3 = Integer.parseInt(var8.substring(1));
               Fragment var4 = this.mFragmentManager.getFragment(var6, var8);
               if(var4 != null) {
                  while(this.mFragments.size() <= var3) {
                     this.mFragments.add((Object)null);
                  }

                  var4.setMenuVisibility(false);
                  this.mFragments.set(var3, var4);
               } else {
                  Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + var8);
               }
            }
         }
      }
   }

   public Parcelable saveState() {
      Bundle var3 = null;
      if(this.mSavedState.size() > 0) {
         var3 = new Bundle();
         Fragment.SavedState[] var2 = new Fragment.SavedState[this.mSavedState.size()];
         this.mSavedState.toArray(var2);
         var3.putParcelableArray("states", var2);
      }

      Bundle var5;
      for(int var1 = 0; var1 < this.mFragments.size(); var3 = var5) {
         Fragment var4 = (Fragment)this.mFragments.get(var1);
         var5 = var3;
         if(var4 != null) {
            var5 = var3;
            if(var4.isAdded()) {
               var5 = var3;
               if(var3 == null) {
                  var5 = new Bundle();
               }

               String var6 = "f" + var1;
               this.mFragmentManager.putFragment(var5, var6, var4);
            }
         }

         ++var1;
      }

      return var3;
   }

   public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      if(var4 != this.mCurrentPrimaryItem) {
         if(this.mCurrentPrimaryItem != null) {
            this.mCurrentPrimaryItem.setMenuVisibility(false);
            this.mCurrentPrimaryItem.setUserVisibleHint(false);
         }

         if(var4 != null) {
            var4.setMenuVisibility(true);
            var4.setUserVisibleHint(true);
         }

         this.mCurrentPrimaryItem = var4;
      }

   }

   public void startUpdate(ViewGroup var1) {
      if(var1.getId() == -1) {
         throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
      }
   }
}
