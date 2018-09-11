package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.provider.DocumentsContractApi19;
import android.support.v4.provider.DocumentsContractApi21;
import android.support.v4.provider.RawDocumentFile;
import android.support.v4.provider.SingleDocumentFile;
import android.support.v4.provider.TreeDocumentFile;
import java.io.File;

public abstract class DocumentFile {
   static final String TAG = "DocumentFile";
   private final DocumentFile mParent;

   DocumentFile(DocumentFile var1) {
      this.mParent = var1;
   }

   public static DocumentFile fromFile(File var0) {
      return new RawDocumentFile((DocumentFile)null, var0);
   }

   public static DocumentFile fromSingleUri(Context var0, Uri var1) {
      SingleDocumentFile var2;
      if(VERSION.SDK_INT >= 19) {
         var2 = new SingleDocumentFile((DocumentFile)null, var0, var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public static DocumentFile fromTreeUri(Context var0, Uri var1) {
      TreeDocumentFile var2;
      if(VERSION.SDK_INT >= 21) {
         var2 = new TreeDocumentFile((DocumentFile)null, var0, DocumentsContractApi21.prepareTreeUri(var1));
      } else {
         var2 = null;
      }

      return var2;
   }

   public static boolean isDocumentUri(Context var0, Uri var1) {
      boolean var2;
      if(VERSION.SDK_INT >= 19) {
         var2 = DocumentsContractApi19.isDocumentUri(var0, var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   public abstract boolean canRead();

   public abstract boolean canWrite();

   public abstract DocumentFile createDirectory(String var1);

   public abstract DocumentFile createFile(String var1, String var2);

   public abstract boolean delete();

   public abstract boolean exists();

   public DocumentFile findFile(String var1) {
      DocumentFile[] var5 = this.listFiles();
      int var3 = var5.length;
      int var2 = 0;

      DocumentFile var6;
      while(true) {
         if(var2 >= var3) {
            var6 = null;
            break;
         }

         DocumentFile var4 = var5[var2];
         if(var1.equals(var4.getName())) {
            var6 = var4;
            break;
         }

         ++var2;
      }

      return var6;
   }

   public abstract String getName();

   public DocumentFile getParentFile() {
      return this.mParent;
   }

   public abstract String getType();

   public abstract Uri getUri();

   public abstract boolean isDirectory();

   public abstract boolean isFile();

   public abstract long lastModified();

   public abstract long length();

   public abstract DocumentFile[] listFiles();

   public abstract boolean renameTo(String var1);
}
