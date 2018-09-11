package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build.VERSION;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ActivityFragmentLifecycle;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@TargetApi(11)
public class RequestManagerFragment extends Fragment {
   private final HashSet childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private RequestManagerFragment rootRequestManagerFragment;

   public RequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   @SuppressLint({"ValidFragment"})
   RequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new RequestManagerFragment.FragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   @TargetApi(17)
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

   private void removeChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.remove(var1);
   }

   @TargetApi(17)
   public Set getDescendantRequestManagerFragments() {
      Set var1;
      if(this.rootRequestManagerFragment == this) {
         var1 = Collections.unmodifiableSet(this.childRequestManagerFragments);
      } else if(this.rootRequestManagerFragment != null && VERSION.SDK_INT >= 17) {
         HashSet var4 = new HashSet();
         Iterator var3 = this.rootRequestManagerFragment.getDescendantRequestManagerFragments().iterator();

         while(var3.hasNext()) {
            RequestManagerFragment var2 = (RequestManagerFragment)var3.next();
            if(this.isDescendant(var2.getParentFragment())) {
               var4.add(var2);
            }
         }

         var1 = Collections.unmodifiableSet(var4);
      } else {
         var1 = Collections.emptySet();
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
      this.rootRequestManagerFragment = RequestManagerRetriever.get().getRequestManagerFragment(this.getActivity().getFragmentManager());
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

   public void onTrimMemory(int var1) {
      if(this.requestManager != null) {
         this.requestManager.onTrimMemory(var1);
      }

   }

   public void setRequestManager(RequestManager var1) {
      this.requestManager = var1;
   }

   private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      private FragmentRequestManagerTreeNode() {
      }

      // $FF: synthetic method
      FragmentRequestManagerTreeNode(Object var2) {
         this();
      }

      public Set getDescendants() {
         Set var2 = RequestManagerFragment.this.getDescendantRequestManagerFragments();
         HashSet var1 = new HashSet(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            RequestManagerFragment var3 = (RequestManagerFragment)var4.next();
            if(var3.getRequestManager() != null) {
               var1.add(var3.getRequestManager());
            }
         }

         return var1;
      }
   }
}
