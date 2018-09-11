package android.support.v4.content;

import android.content.Context;
import java.io.File;

class ContextCompatApi24 {
   public static Context createDeviceProtectedStorageContext(Context var0) {
      return var0.createDeviceProtectedStorageContext();
   }

   public static File getDataDir(Context var0) {
      return var0.getDataDir();
   }

   public static boolean isDeviceProtectedStorage(Context var0) {
      return var0.isDeviceProtectedStorage();
   }
}
