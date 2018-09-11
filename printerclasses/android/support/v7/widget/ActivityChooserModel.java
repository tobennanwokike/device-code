package android.support.v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActivityChooserModel extends DataSetObservable {
   static final String ATTRIBUTE_ACTIVITY = "activity";
   static final String ATTRIBUTE_TIME = "time";
   static final String ATTRIBUTE_WEIGHT = "weight";
   static final boolean DEBUG = false;
   private static final int DEFAULT_ACTIVITY_INFLATION = 5;
   private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;
   public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
   public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
   private static final String HISTORY_FILE_EXTENSION = ".xml";
   private static final int INVALID_INDEX = -1;
   static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
   static final String TAG_HISTORICAL_RECORD = "historical-record";
   static final String TAG_HISTORICAL_RECORDS = "historical-records";
   private static final Map sDataModelRegistry = new HashMap();
   private static final Object sRegistryLock = new Object();
   private final List mActivities = new ArrayList();
   private ActivityChooserModel.OnChooseActivityListener mActivityChoserModelPolicy;
   private ActivityChooserModel.ActivitySorter mActivitySorter = new ActivityChooserModel.DefaultSorter();
   boolean mCanReadHistoricalData = true;
   final Context mContext;
   private final List mHistoricalRecords = new ArrayList();
   private boolean mHistoricalRecordsChanged = true;
   final String mHistoryFileName;
   private int mHistoryMaxSize = 50;
   private final Object mInstanceLock = new Object();
   private Intent mIntent;
   private boolean mReadShareHistoryCalled = false;
   private boolean mReloadActivities = false;

   private ActivityChooserModel(Context var1, String var2) {
      this.mContext = var1.getApplicationContext();
      if(!TextUtils.isEmpty(var2) && !var2.endsWith(".xml")) {
         this.mHistoryFileName = var2 + ".xml";
      } else {
         this.mHistoryFileName = var2;
      }

   }

   private boolean addHistoricalRecord(ActivityChooserModel.HistoricalRecord var1) {
      boolean var2 = this.mHistoricalRecords.add(var1);
      if(var2) {
         this.mHistoricalRecordsChanged = true;
         this.pruneExcessiveHistoricalRecordsIfNeeded();
         this.persistHistoricalDataIfNeeded();
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

      return var2;
   }

   private void ensureConsistentState() {
      boolean var1 = this.loadActivitiesIfNeeded();
      boolean var2 = this.readHistoricalDataIfNeeded();
      this.pruneExcessiveHistoricalRecordsIfNeeded();
      if(var1 | var2) {
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

   }

   public static ActivityChooserModel get(Context param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean loadActivitiesIfNeeded() {
      boolean var4 = false;
      boolean var3 = var4;
      if(this.mReloadActivities) {
         var3 = var4;
         if(this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            List var5 = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            int var2 = var5.size();

            for(int var1 = 0; var1 < var2; ++var1) {
               ResolveInfo var6 = (ResolveInfo)var5.get(var1);
               this.mActivities.add(new ActivityChooserModel.ActivityResolveInfo(var6));
            }

            var3 = true;
         }
      }

      return var3;
   }

   private void persistHistoricalDataIfNeeded() {
      if(!this.mReadShareHistoryCalled) {
         throw new IllegalStateException("No preceding call to #readHistoricalData");
      } else {
         if(this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if(!TextUtils.isEmpty(this.mHistoryFileName)) {
               AsyncTaskCompat.executeParallel(new ActivityChooserModel.PersistHistoryAsyncTask(), new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
            }
         }

      }
   }

   private void pruneExcessiveHistoricalRecordsIfNeeded() {
      int var2 = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
      if(var2 > 0) {
         this.mHistoricalRecordsChanged = true;

         for(int var1 = 0; var1 < var2; ++var1) {
            ActivityChooserModel.HistoricalRecord var3 = (ActivityChooserModel.HistoricalRecord)this.mHistoricalRecords.remove(0);
         }
      }

   }

   private boolean readHistoricalDataIfNeeded() {
      boolean var1 = true;
      if(this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty(this.mHistoryFileName)) {
         this.mCanReadHistoricalData = false;
         this.mReadShareHistoryCalled = true;
         this.readHistoricalDataImpl();
      } else {
         var1 = false;
      }

      return var1;
   }

   private void readHistoricalDataImpl() {
      // $FF: Couldn't be decompiled
   }

   private boolean sortActivitiesIfNeeded() {
      boolean var1;
      if(this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
         this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Intent chooseActivity(int param1) {
      // $FF: Couldn't be decompiled
   }

   public ResolveInfo getActivity(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int getActivityCount() {
      // $FF: Couldn't be decompiled
   }

   public int getActivityIndex(ResolveInfo param1) {
      // $FF: Couldn't be decompiled
   }

   public ResolveInfo getDefaultActivity() {
      // $FF: Couldn't be decompiled
   }

   public int getHistoryMaxSize() {
      // $FF: Couldn't be decompiled
   }

   public int getHistorySize() {
      // $FF: Couldn't be decompiled
   }

   public Intent getIntent() {
      // $FF: Couldn't be decompiled
   }

   public void setActivitySorter(ActivityChooserModel.ActivitySorter param1) {
      // $FF: Couldn't be decompiled
   }

   public void setDefaultActivity(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void setHistoryMaxSize(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void setIntent(Intent param1) {
      // $FF: Couldn't be decompiled
   }

   public void setOnChooseActivityListener(ActivityChooserModel.OnChooseActivityListener param1) {
      // $FF: Couldn't be decompiled
   }

   public interface ActivityChooserModelClient {
      void setActivityChooserModel(ActivityChooserModel var1);
   }

   public final class ActivityResolveInfo implements Comparable {
      public final ResolveInfo resolveInfo;
      public float weight;

      public ActivityResolveInfo(ResolveInfo var2) {
         this.resolveInfo = var2;
      }

      public int compareTo(ActivityChooserModel.ActivityResolveInfo var1) {
         return Float.floatToIntBits(var1.weight) - Float.floatToIntBits(this.weight);
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if(this != var1) {
            if(var1 == null) {
               var2 = false;
            } else if(this.getClass() != var1.getClass()) {
               var2 = false;
            } else {
               ActivityChooserModel.ActivityResolveInfo var3 = (ActivityChooserModel.ActivityResolveInfo)var1;
               if(Float.floatToIntBits(this.weight) != Float.floatToIntBits(var3.weight)) {
                  var2 = false;
               }
            }
         }

         return var2;
      }

      public int hashCode() {
         return Float.floatToIntBits(this.weight) + 31;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("resolveInfo:").append(this.resolveInfo.toString());
         var1.append("; weight:").append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface ActivitySorter {
      void sort(Intent var1, List var2, List var3);
   }

   private final class DefaultSorter implements ActivityChooserModel.ActivitySorter {
      private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;
      private final Map mPackageNameToActivityMap = new HashMap();

      public void sort(Intent var1, List var2, List var3) {
         Map var10 = this.mPackageNameToActivityMap;
         var10.clear();
         int var7 = var2.size();

         int var6;
         ActivityChooserModel.ActivityResolveInfo var8;
         for(var6 = 0; var6 < var7; ++var6) {
            var8 = (ActivityChooserModel.ActivityResolveInfo)var2.get(var6);
            var8.weight = 0.0F;
            var10.put(new ComponentName(var8.resolveInfo.activityInfo.packageName, var8.resolveInfo.activityInfo.name), var8);
         }

         var6 = var3.size();
         float var4 = 1.0F;
         --var6;

         while(var6 >= 0) {
            ActivityChooserModel.HistoricalRecord var9 = (ActivityChooserModel.HistoricalRecord)var3.get(var6);
            var8 = (ActivityChooserModel.ActivityResolveInfo)var10.get(var9.activity);
            float var5 = var4;
            if(var8 != null) {
               var8.weight += var9.weight * var4;
               var5 = var4 * 0.95F;
            }

            --var6;
            var4 = var5;
         }

         Collections.sort(var2);
      }
   }

   public static final class HistoricalRecord {
      public final ComponentName activity;
      public final long time;
      public final float weight;

      public HistoricalRecord(ComponentName var1, long var2, float var4) {
         this.activity = var1;
         this.time = var2;
         this.weight = var4;
      }

      public HistoricalRecord(String var1, long var2, float var4) {
         this(ComponentName.unflattenFromString(var1), var2, var4);
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if(this != var1) {
            if(var1 == null) {
               var2 = false;
            } else if(this.getClass() != var1.getClass()) {
               var2 = false;
            } else {
               ActivityChooserModel.HistoricalRecord var3 = (ActivityChooserModel.HistoricalRecord)var1;
               if(this.activity == null) {
                  if(var3.activity != null) {
                     var2 = false;
                     return var2;
                  }
               } else if(!this.activity.equals(var3.activity)) {
                  var2 = false;
                  return var2;
               }

               if(this.time != var3.time) {
                  var2 = false;
               } else if(Float.floatToIntBits(this.weight) != Float.floatToIntBits(var3.weight)) {
                  var2 = false;
               }
            }
         }

         return var2;
      }

      public int hashCode() {
         int var1;
         if(this.activity == null) {
            var1 = 0;
         } else {
            var1 = this.activity.hashCode();
         }

         return ((var1 + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("; activity:").append(this.activity);
         var1.append("; time:").append(this.time);
         var1.append("; weight:").append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface OnChooseActivityListener {
      boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
   }

   private final class PersistHistoryAsyncTask extends AsyncTask {
      public Void doInBackground(Object... param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
