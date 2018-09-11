package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
   private final File mBackupName;
   private final File mBaseName;

   public AtomicFile(File var1) {
      this.mBaseName = var1;
      this.mBackupName = new File(var1.getPath() + ".bak");
   }

   static boolean sync(FileOutputStream var0) {
      boolean var1;
      if(var0 != null) {
         try {
            var0.getFD().sync();
         } catch (IOException var2) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public void delete() {
      this.mBaseName.delete();
      this.mBackupName.delete();
   }

   public void failWrite(FileOutputStream var1) {
      if(var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
         } catch (IOException var2) {
            Log.w("AtomicFile", "failWrite: Got exception:", var2);
         }
      }

   }

   public void finishWrite(FileOutputStream var1) {
      if(var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBackupName.delete();
         } catch (IOException var2) {
            Log.w("AtomicFile", "finishWrite: Got exception:", var2);
         }
      }

   }

   public File getBaseFile() {
      return this.mBaseName;
   }

   public FileInputStream openRead() throws FileNotFoundException {
      if(this.mBackupName.exists()) {
         this.mBaseName.delete();
         this.mBackupName.renameTo(this.mBaseName);
      }

      return new FileInputStream(this.mBaseName);
   }

   public byte[] readFully() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public FileOutputStream startWrite() throws IOException {
      if(this.mBaseName.exists()) {
         if(!this.mBackupName.exists()) {
            if(!this.mBaseName.renameTo(this.mBackupName)) {
               Log.w("AtomicFile", "Couldn\'t rename file " + this.mBaseName + " to backup file " + this.mBackupName);
            }
         } else {
            this.mBaseName.delete();
         }
      }

      FileOutputStream var1;
      try {
         var1 = new FileOutputStream(this.mBaseName);
      } catch (FileNotFoundException var3) {
         if(!this.mBaseName.getParentFile().mkdirs()) {
            throw new IOException("Couldn\'t create directory " + this.mBaseName);
         }

         try {
            var1 = new FileOutputStream(this.mBaseName);
         } catch (FileNotFoundException var2) {
            throw new IOException("Couldn\'t create " + this.mBaseName);
         }
      }

      return var1;
   }
}
