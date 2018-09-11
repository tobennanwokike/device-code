package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ActivityFragmentLifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SupportRequestManagerFragment extends Fragment {
   private final HashSet childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private SupportRequestManagerFragment rootRequestManagerFragment;

   public SupportRequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   @SuppressLint({"ValidFragment"})
   public SupportRequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new SupportRequestManagerFragment.SupportFragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(SupportRequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   private boolean isDescendant(Fragment var1) {
      Fragment var3 = this.getParentFragment();

      boolean var2;
      while(true) {
         if(var1.getParentFragment() == null) {
            var2 = false;
            break;
         }

         if(var1.getParentFragment() == var3) {
            var2 = true;
            break;
         }

         var1 = var1.getParentFragment();
      }

      return var2;
   }

   private void removeChildRequestManagerFragment(SupportRequestManagerFragment var1) {
      this.childRequestManagerFragments.remove(var1);
   }

   public Set getDescendantRequestManagerFragments() {
      Set var1;
      if(this.rootRequestManagerFragment == null) {
         var1 = Collections.emptySet();
      } else if(this.rootRequestManagerFragment == this) {
         var1 = Collections.unmodifiableSet(this.childRequestManagerFragments);
      } else {
         HashSet var2 = new HashSet();
         Iterator var4 = this.rootRequestManagerFragment.getDescendantRequestManagerFragments().iterator();

         while(var4.hasNext()) {
            SupportRequestManagerFragment var3 = (SupportRequestManagerFragment)var4.next();
            if(this.isDescendant(var3.getParentFragment())) {
               var2.add(var3);
            }
         }

         var1 = Collections.unmodifiableSet(var2);
      }

      return var1;
   }

   ActivityFragmentLifecycle getLifecycle() {
      return this.lifecycle;
   }

   public RequestManager getRequestManager() {
      return this.requestManager;
   }

   public RequestManagerTreeNode getRequestManagerTreeNode() {
      return this.requestManagerTreeNode;
   }

   public void onAttach(Activity var1) {
      super.onAttach(var1);
      this.rootRequestManagerFragment = RequestManagerRetriever.get().getSupportRequestManagerFragment(this.getActivity().getSupportFragmentManager());
      if(this.rootRequestManagerFragment != this) {
         this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
      }

   }

   public void onDestroy() {
      super.onDestroy();
      this.lifecycle.onDestroy();
   }

   public void onDetach() {
      super.onDetach();
      if(this.rootRequestManagerFragment != null) {
         this.rootRequestManagerFragment.removeChildRequestManagerFragment(this);
         this.rootRequestManagerFragment = null;
      }

   }

   public void onLowMemory() {
      super.onLowMemory();
      if(this.requestManager != null) {
         this.requestManager.onLowMemory();
      }

   }

   public void onStart() {
      super.onStart();
      this.lifecycle.onStart();
   }

   public void onStop() {
      super.onStop();
      this.lifecycle.onStop();
   }

   public void setRequestManager(RequestManager var1) {
      this.requestManager = var1;
   }

   private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      private SupportFragmentRequestManagerTreeNode() {
      }

      // $FF: synthetic method
      SupportFragmentRequestManagerTreeNode(Object var2) {
         this();
      }

      public Set getDescendants() {
         Set var2 = SupportRequestManagerFragment.this.getDescendantRequestManagerFragments();
         HashSet var1 = new HashSet(var2.size());
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SupportRequestManagerFragment var4 = (SupportRequestManagerFragment)var3.next();
            if(var4.getRequestManager() != null) {
               var1.add(var4.getRequestManager());
            }
         }

         return var1;
      }
   }
}
