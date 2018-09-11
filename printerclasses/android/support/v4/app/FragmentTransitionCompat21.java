package android.support.v4.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.Transition.EpicenterCallback;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class FragmentTransitionCompat21 {
   public static void addTargets(Object var0, ArrayList var1) {
      Transition var4 = (Transition)var0;
      int var2;
      int var3;
      if(var4 instanceof TransitionSet) {
         TransitionSet var5 = (TransitionSet)var4;
         var3 = var5.getTransitionCount();

         for(var2 = 0; var2 < var3; ++var2) {
            addTargets(var5.getTransitionAt(var2), var1);
         }
      } else if(!hasSimpleTarget(var4) && isNullOrEmpty(var4.getTargets())) {
         var3 = var1.size();

         for(var2 = 0; var2 < var3; ++var2) {
            var4.addTarget((View)var1.get(var2));
         }
      }

   }

   public static void addTransitionTargets(Object var0, Object var1, Object var2, final View var3, final FragmentTransitionCompat21.ViewRetriever var4, final View var5, FragmentTransitionCompat21.EpicenterView var6, final Map var7, final ArrayList var8, ArrayList var9, Map var10, final Map var11, ArrayList var12) {
      final Transition var13 = (Transition)var0;
      final Transition var14 = (Transition)var2;
      Transition var15 = (Transition)var1;
      excludeViews(var13, var14, var9, true);
      if(var0 != null || var1 != null) {
         if(var13 != null) {
            var13.addTarget(var5);
         }

         if(var1 != null) {
            setSharedElementTargets(var15, var5, var10, var12);
            excludeViews(var13, var15, var12, true);
            excludeViews(var14, var15, var12, true);
         }

         var3.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               var3.getViewTreeObserver().removeOnPreDrawListener(this);
               if(var13 != null) {
                  var13.removeTarget(var5);
               }

               if(var4 != null) {
                  View var1 = var4.getView();
                  if(var1 != null) {
                     if(!var7.isEmpty()) {
                        FragmentTransitionCompat21.findNamedViews(var11, var1);
                        var11.keySet().retainAll(var7.values());
                        Iterator var3x = var7.entrySet().iterator();

                        while(var3x.hasNext()) {
                           Entry var2 = (Entry)var3x.next();
                           String var4x = (String)var2.getValue();
                           View var5x = (View)var11.get(var4x);
                           if(var5x != null) {
                              var5x.setTransitionName((String)var2.getKey());
                           }
                        }
                     }

                     if(var13 != null) {
                        FragmentTransitionCompat21.captureTransitioningViews(var8, var1);
                        var8.removeAll(var11.values());
                        var8.add(var5);
                        FragmentTransitionCompat21.addTargets(var13, var8);
                     }
                  }
               }

               FragmentTransitionCompat21.excludeViews(var14, var13, var8, true);
               return true;
            }
         });
         setSharedElementEpicenter(var13, var6);
      }

   }

   public static void beginDelayedTransition(ViewGroup var0, Object var1) {
      TransitionManager.beginDelayedTransition(var0, (Transition)var1);
   }

   private static void bfsAddViewChildren(List var0, View var1) {
      int var4 = var0.size();
      if(!containedBeforeIndex(var0, var1, var4)) {
         var0.add(var1);

         for(int var2 = var4; var2 < var0.size(); ++var2) {
            var1 = (View)var0.get(var2);
            if(var1 instanceof ViewGroup) {
               ViewGroup var7 = (ViewGroup)var1;
               int var5 = var7.getChildCount();

               for(int var3 = 0; var3 < var5; ++var3) {
                  View var6 = var7.getChildAt(var3);
                  if(!containedBeforeIndex(var0, var6, var4)) {
                     var0.add(var6);
                  }
               }
            }
         }
      }

   }

   public static Object captureExitingViews(Object var0, View var1, ArrayList var2, Map var3, View var4) {
      Object var5 = var0;
      if(var0 != null) {
         captureTransitioningViews(var2, var1);
         if(var3 != null) {
            var2.removeAll(var3.values());
         }

         if(var2.isEmpty()) {
            var5 = null;
         } else {
            var2.add(var4);
            addTargets((Transition)var0, var2);
            var5 = var0;
         }
      }

      return var5;
   }

   private static void captureTransitioningViews(ArrayList var0, View var1) {
      if(var1.getVisibility() == 0) {
         if(var1 instanceof ViewGroup) {
            ViewGroup var4 = (ViewGroup)var1;
            if(var4.isTransitionGroup()) {
               var0.add(var4);
            } else {
               int var3 = var4.getChildCount();

               for(int var2 = 0; var2 < var3; ++var2) {
                  captureTransitioningViews(var0, var4.getChildAt(var2));
               }
            }
         } else {
            var0.add(var1);
         }
      }

   }

   public static void cleanupTransitions(final View var0, final View var1, Object var2, final ArrayList var3, Object var4, final ArrayList var5, Object var6, final ArrayList var7, Object var8, final ArrayList var9, final Map var10) {
      final Transition var11 = (Transition)var2;
      final Transition var12 = (Transition)var4;
      final Transition var13 = (Transition)var6;
      final Transition var14 = (Transition)var8;
      if(var14 != null) {
         var0.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               var0.getViewTreeObserver().removeOnPreDrawListener(this);
               if(var11 != null) {
                  FragmentTransitionCompat21.removeTargets(var11, var3);
                  FragmentTransitionCompat21.excludeViews(var11, var12, var5, false);
                  FragmentTransitionCompat21.excludeViews(var11, var13, var7, false);
               }

               if(var12 != null) {
                  FragmentTransitionCompat21.removeTargets(var12, var5);
                  FragmentTransitionCompat21.excludeViews(var12, var11, var3, false);
                  FragmentTransitionCompat21.excludeViews(var12, var13, var7, false);
               }

               if(var13 != null) {
                  FragmentTransitionCompat21.removeTargets(var13, var7);
               }

               Iterator var3x = var10.entrySet().iterator();

               while(var3x.hasNext()) {
                  Entry var4 = (Entry)var3x.next();
                  ((View)var4.getValue()).setTransitionName((String)var4.getKey());
               }

               int var2 = var9.size();

               for(int var1x = 0; var1x < var2; ++var1x) {
                  var14.excludeTarget((View)var9.get(var1x), false);
               }

               var14.excludeTarget(var1, false);
               return true;
            }
         });
      }

   }

   public static Object cloneTransition(Object var0) {
      Object var1 = var0;
      if(var0 != null) {
         var1 = ((Transition)var0).clone();
      }

      return var1;
   }

   private static boolean containedBeforeIndex(List var0, View var1, int var2) {
      int var3 = 0;

      boolean var4;
      while(true) {
         if(var3 >= var2) {
            var4 = false;
            break;
         }

         if(var0.get(var3) == var1) {
            var4 = true;
            break;
         }

         ++var3;
      }

      return var4;
   }

   public static void excludeSharedElementViews(Object var0, Object var1, Object var2, ArrayList var3, boolean var4) {
      Transition var5 = (Transition)var0;
      Transition var6 = (Transition)var1;
      Transition var7 = (Transition)var2;
      excludeViews(var5, var7, var3, var4);
      excludeViews(var6, var7, var3, var4);
   }

   public static void excludeTarget(Object var0, View var1, boolean var2) {
      ((Transition)var0).excludeTarget(var1, var2);
   }

   private static void excludeViews(Transition var0, Transition var1, ArrayList var2, boolean var3) {
      if(var0 != null) {
         int var4;
         if(var1 == null) {
            var4 = 0;
         } else {
            var4 = var2.size();
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            var0.excludeTarget((View)var2.get(var5), var3);
         }
      }

   }

   public static void findNamedViews(Map var0, View var1) {
      if(var1.getVisibility() == 0) {
         String var4 = var1.getTransitionName();
         if(var4 != null) {
            var0.put(var4, var1);
         }

         if(var1 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var1;
            int var3 = var5.getChildCount();

            for(int var2 = 0; var2 < var3; ++var2) {
               findNamedViews(var0, var5.getChildAt(var2));
            }
         }
      }

   }

   private static Rect getBoundsOnScreen(View var0) {
      Rect var1 = new Rect();
      int[] var2 = new int[2];
      var0.getLocationOnScreen(var2);
      var1.set(var2[0], var2[1], var2[0] + var0.getWidth(), var2[1] + var0.getHeight());
      return var1;
   }

   public static String getTransitionName(View var0) {
      return var0.getTransitionName();
   }

   private static boolean hasSimpleTarget(Transition var0) {
      boolean var1;
      if(isNullOrEmpty(var0.getTargetIds()) && isNullOrEmpty(var0.getTargetNames()) && isNullOrEmpty(var0.getTargetTypes())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isNullOrEmpty(List var0) {
      boolean var1;
      if(var0 != null && !var0.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static Object mergeTransitions(Object var0, Object var1, Object var2, boolean var3) {
      boolean var5 = true;
      Transition var6 = (Transition)var0;
      var0 = (Transition)var1;
      Transition var8 = (Transition)var2;
      boolean var4 = var5;
      if(var6 != null) {
         var4 = var5;
         if(var0 != null) {
            var4 = var3;
         }
      }

      TransitionSet var7;
      if(var4) {
         var7 = new TransitionSet();
         if(var6 != null) {
            var7.addTransition(var6);
         }

         if(var0 != null) {
            var7.addTransition((Transition)var0);
         }

         if(var8 != null) {
            var7.addTransition(var8);
         }

         var0 = var7;
      } else {
         var1 = null;
         if(var0 != null && var6 != null) {
            var0 = (new TransitionSet()).addTransition((Transition)var0).addTransition(var6).setOrdering(1);
         } else if(var0 == null) {
            var0 = var1;
            if(var6 != null) {
               var0 = var6;
            }
         }

         if(var8 != null) {
            var7 = new TransitionSet();
            if(var0 != null) {
               var7.addTransition((Transition)var0);
            }

            var7.addTransition(var8);
            var0 = var7;
         }
      }

      return var0;
   }

   public static void removeTargets(Object var0, ArrayList var1) {
      Transition var5 = (Transition)var0;
      int var2;
      if(var5 instanceof TransitionSet) {
         TransitionSet var6 = (TransitionSet)var5;
         int var3 = var6.getTransitionCount();

         for(var2 = 0; var2 < var3; ++var2) {
            removeTargets(var6.getTransitionAt(var2), var1);
         }
      } else if(!hasSimpleTarget(var5)) {
         List var4 = var5.getTargets();
         if(var4 != null && var4.size() == var1.size() && var4.containsAll(var1)) {
            for(var2 = var1.size() - 1; var2 >= 0; --var2) {
               var5.removeTarget((View)var1.get(var2));
            }
         }
      }

   }

   public static void setEpicenter(Object var0, View var1) {
      ((Transition)var0).setEpicenterCallback(new EpicenterCallback() {
         // $FF: synthetic field
         final Rect val$epicenter;

         {
            this.val$epicenter = var1;
         }

         public Rect onGetEpicenter(Transition var1) {
            return this.val$epicenter;
         }
      });
   }

   private static void setSharedElementEpicenter(Transition var0, final FragmentTransitionCompat21.EpicenterView var1) {
      if(var0 != null) {
         var0.setEpicenterCallback(new EpicenterCallback() {
            private Rect mEpicenter;

            public Rect onGetEpicenter(Transition var1x) {
               if(this.mEpicenter == null && var1.epicenter != null) {
                  this.mEpicenter = FragmentTransitionCompat21.getBoundsOnScreen(var1.epicenter);
               }

               return this.mEpicenter;
            }
         });
      }

   }

   public static void setSharedElementTargets(Object var0, View var1, Map var2, ArrayList var3) {
      TransitionSet var6 = (TransitionSet)var0;
      var3.clear();
      var3.addAll(var2.values());
      List var7 = var6.getTargets();
      var7.clear();
      int var5 = var3.size();

      for(int var4 = 0; var4 < var5; ++var4) {
         bfsAddViewChildren(var7, (View)var3.get(var4));
      }

      var3.add(var1);
      addTargets(var6, var3);
   }

   public static Object wrapSharedElementTransition(Object var0) {
      Object var1 = null;
      TransitionSet var3;
      if(var0 == null) {
         var3 = (TransitionSet)var1;
      } else {
         Transition var2 = (Transition)var0;
         var3 = (TransitionSet)var1;
         if(var2 != null) {
            var3 = new TransitionSet();
            var3.addTransition(var2);
         }
      }

      return var3;
   }

   public static class EpicenterView {
      public View epicenter;
   }

   public interface ViewRetriever {
      View getView();
   }
}
