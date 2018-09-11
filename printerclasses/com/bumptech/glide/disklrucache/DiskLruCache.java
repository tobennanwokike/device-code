package com.bumptech.glide.disklrucache;

import com.bumptech.glide.disklrucache.Util;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
   static final long ANY_SEQUENCE_NUMBER = -1L;
   private static final String CLEAN = "CLEAN";
   private static final String DIRTY = "DIRTY";
   static final String JOURNAL_FILE = "journal";
   static final String JOURNAL_FILE_BACKUP = "journal.bkp";
   static final String JOURNAL_FILE_TEMP = "journal.tmp";
   static final String MAGIC = "libcore.io.DiskLruCache";
   private static final String READ = "READ";
   private static final String REMOVE = "REMOVE";
   static final String VERSION_1 = "1";
   private final int appVersion;
   private final Callable cleanupCallable;
   private final File directory;
   final ThreadPoolExecutor executorService;
   private final File journalFile;
   private final File journalFileBackup;
   private final File journalFileTmp;
   private Writer journalWriter;
   private final LinkedHashMap lruEntries = new LinkedHashMap(0, 0.75F, true);
   private long maxSize;
   private long nextSequenceNumber = 0L;
   private int redundantOpCount;
   private long size = 0L;
   private final int valueCount;

   private DiskLruCache(File var1, int var2, int var3, long var4) {
      this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
      this.cleanupCallable = new Callable() {
         public Void call() throws Exception {
            // $FF: Couldn't be decompiled
         }
      };
      this.directory = var1;
      this.appVersion = var2;
      this.journalFile = new File(var1, "journal");
      this.journalFileTmp = new File(var1, "journal.tmp");
      this.journalFileBackup = new File(var1, "journal.bkp");
      this.valueCount = var3;
      this.maxSize = var4;
   }

   // $FF: synthetic method
   static Writer access$000(DiskLruCache var0) {
      return var0.journalWriter;
   }

   // $FF: synthetic method
   static void access$100(DiskLruCache var0) throws IOException {
      var0.trimToSize();
   }

   // $FF: synthetic method
   static boolean access$200(DiskLruCache var0) {
      return var0.journalRebuildRequired();
   }

   // $FF: synthetic method
   static void access$300(DiskLruCache var0) throws IOException {
      var0.rebuildJournal();
   }

   // $FF: synthetic method
   static int access$402(DiskLruCache var0, int var1) {
      var0.redundantOpCount = var1;
      return var1;
   }

   private void checkNotClosed() {
      if(this.journalWriter == null) {
         throw new IllegalStateException("cache is closed");
      }
   }

   private void completeEdit(DiskLruCache.Editor param1, boolean param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private static void deleteIfExists(File var0) throws IOException {
      if(var0.exists() && !var0.delete()) {
         throw new IOException();
      }
   }

   private DiskLruCache.Editor edit(String param1, long param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private static String inputStreamToString(InputStream var0) throws IOException {
      return Util.readFully(new InputStreamReader(var0, Util.UTF_8));
   }

   private boolean journalRebuildRequired() {
      boolean var1;
      if(this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static DiskLruCache open(File var0, int var1, int var2, long var3) throws IOException {
      if(var3 <= 0L) {
         throw new IllegalArgumentException("maxSize <= 0");
      } else if(var2 <= 0) {
         throw new IllegalArgumentException("valueCount <= 0");
      } else {
         File var6 = new File(var0, "journal.bkp");
         if(var6.exists()) {
            File var5 = new File(var0, "journal");
            if(var5.exists()) {
               var6.delete();
            } else {
               renameTo(var6, var5, false);
            }
         }

         DiskLruCache var9 = new DiskLruCache(var0, var1, var2, var3);
         DiskLruCache var8;
         if(var9.journalFile.exists()) {
            label41: {
               try {
                  var9.readJournal();
                  var9.processJournal();
               } catch (IOException var7) {
                  System.out.println("DiskLruCache " + var0 + " is corrupt: " + var7.getMessage() + ", removing");
                  var9.delete();
                  break label41;
               }

               var8 = var9;
               return var8;
            }
         }

         var0.mkdirs();
         var8 = new DiskLruCache(var0, var1, var2, var3);
         var8.rebuildJournal();
         return var8;
      }
   }

   private void processJournal() throws IOException {
      deleteIfExists(this.journalFileTmp);
      Iterator var3 = this.lruEntries.values().iterator();

      while(true) {
         while(var3.hasNext()) {
            DiskLruCache.Entry var2 = (DiskLruCache.Entry)var3.next();
            int var1;
            if(var2.currentEditor == null) {
               for(var1 = 0; var1 < this.valueCount; ++var1) {
                  this.size += var2.lengths[var1];
               }
            } else {
               var2.currentEditor = null;

               for(var1 = 0; var1 < this.valueCount; ++var1) {
                  deleteIfExists(var2.getCleanFile(var1));
                  deleteIfExists(var2.getDirtyFile(var1));
               }

               var3.remove();
            }
         }

         return;
      }
   }

   private void readJournal() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void readJournalLine(String var1) throws IOException {
      int var3 = var1.indexOf(32);
      if(var3 == -1) {
         throw new IOException("unexpected journal line: " + var1);
      } else {
         int var4 = var3 + 1;
         int var2 = var1.indexOf(32, var4);
         String var5;
         if(var2 == -1) {
            String var6 = var1.substring(var4);
            var5 = var6;
            if(var3 == "REMOVE".length()) {
               var5 = var6;
               if(var1.startsWith("REMOVE")) {
                  this.lruEntries.remove(var6);
                  return;
               }
            }
         } else {
            var5 = var1.substring(var4, var2);
         }

         DiskLruCache.Entry var7 = (DiskLruCache.Entry)this.lruEntries.get(var5);
         DiskLruCache.Entry var9 = var7;
         if(var7 == null) {
            var9 = new DiskLruCache.Entry(var5, null);
            this.lruEntries.put(var5, var9);
         }

         if(var2 != -1 && var3 == "CLEAN".length() && var1.startsWith("CLEAN")) {
            String[] var8 = var1.substring(var2 + 1).split(" ");
            var9.readable = true;
            var9.currentEditor = null;
            var9.setLengths(var8);
         } else if(var2 == -1 && var3 == "DIRTY".length() && var1.startsWith("DIRTY")) {
            var9.currentEditor = new DiskLruCache.Editor(var9, null);
         } else if(var2 != -1 || var3 != "READ".length() || !var1.startsWith("READ")) {
            throw new IOException("unexpected journal line: " + var1);
         }

      }
   }

   private void rebuildJournal() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private static void renameTo(File var0, File var1, boolean var2) throws IOException {
      if(var2) {
         deleteIfExists(var1);
      }

      if(!var0.renameTo(var1)) {
         throw new IOException();
      }
   }

   private void trimToSize() throws IOException {
      while(this.size > this.maxSize) {
         this.remove((String)((java.util.Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey());
      }

   }

   public void close() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void delete() throws IOException {
      this.close();
      Util.deleteContents(this.directory);
   }

   public DiskLruCache.Editor edit(String var1) throws IOException {
      return this.edit(var1, -1L);
   }

   public void flush() throws IOException {
      synchronized(this){}

      try {
         this.checkNotClosed();
         this.trimToSize();
         this.journalWriter.flush();
      } finally {
         ;
      }

   }

   public DiskLruCache.Value get(String param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public File getDirectory() {
      return this.directory;
   }

   public long getMaxSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.maxSize;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isClosed() {
      synchronized(this){}
      boolean var4 = false;

      Writer var2;
      try {
         var4 = true;
         var2 = this.journalWriter;
         var4 = false;
      } finally {
         if(var4) {
            ;
         }
      }

      boolean var1;
      if(var2 == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean remove(String param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public void setMaxSize(long var1) {
      synchronized(this){}

      try {
         this.maxSize = var1;
         this.executorService.submit(this.cleanupCallable);
      } finally {
         ;
      }

   }

   public long size() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.size;
      } finally {
         ;
      }

      return var1;
   }

   public final class Editor {
      private boolean committed;
      private final DiskLruCache.Entry entry;
      private final boolean[] written;

      private Editor(DiskLruCache.Entry var2) {
         this.entry = var2;
         boolean[] var3;
         if(var2.readable) {
            var3 = null;
         } else {
            var3 = new boolean[DiskLruCache.this.valueCount];
         }

         this.written = var3;
      }

      // $FF: synthetic method
      Editor(DiskLruCache.Entry var2, Object var3) {
         this();
      }

      // $FF: synthetic method
      static DiskLruCache.Entry access$1400(DiskLruCache.Editor var0) {
         return var0.entry;
      }

      // $FF: synthetic method
      static boolean[] access$1500(DiskLruCache.Editor var0) {
         return var0.written;
      }

      private InputStream newInputStream(int param1) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void abort() throws IOException {
         DiskLruCache.this.completeEdit(this, false);
      }

      public void abortUnlessCommitted() {
         if(!this.committed) {
            try {
               this.abort();
            } catch (IOException var2) {
               ;
            }
         }

      }

      public void commit() throws IOException {
         DiskLruCache.this.completeEdit(this, true);
         this.committed = true;
      }

      public File getFile(int param1) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public String getString(int var1) throws IOException {
         InputStream var2 = this.newInputStream(var1);
         String var3;
         if(var2 != null) {
            var3 = DiskLruCache.inputStreamToString(var2);
         } else {
            var3 = null;
         }

         return var3;
      }

      public void set(int var1, String var2) throws IOException {
         Object var3 = null;
         boolean var10 = false;

         OutputStreamWriter var4;
         try {
            var10 = true;
            FileOutputStream var5 = new FileOutputStream(this.getFile(var1));
            var4 = new OutputStreamWriter(var5, Util.UTF_8);
            var10 = false;
         } finally {
            if(var10) {
               Util.closeQuietly((Closeable)var3);
            }
         }

         try {
            var4.write(var2);
         } finally {
            ;
         }

         Util.closeQuietly(var4);
      }
   }

   private final class Entry {
      File[] cleanFiles;
      private DiskLruCache.Editor currentEditor;
      File[] dirtyFiles;
      private final String key;
      private final long[] lengths;
      private boolean readable;
      private long sequenceNumber;

      private Entry(String var2) {
         this.key = var2;
         this.lengths = new long[DiskLruCache.this.valueCount];
         this.cleanFiles = new File[DiskLruCache.this.valueCount];
         this.dirtyFiles = new File[DiskLruCache.this.valueCount];
         StringBuilder var5 = (new StringBuilder(var2)).append('.');
         int var4 = var5.length();

         for(int var3 = 0; var3 < DiskLruCache.this.valueCount; ++var3) {
            var5.append(var3);
            this.cleanFiles[var3] = new File(DiskLruCache.this.directory, var5.toString());
            var5.append(".tmp");
            this.dirtyFiles[var3] = new File(DiskLruCache.this.directory, var5.toString());
            var5.setLength(var4);
         }

      }

      // $FF: synthetic method
      Entry(String var2, Object var3) {
         this();
      }

      // $FF: synthetic method
      static String access$1100(DiskLruCache.Entry var0) {
         return var0.key;
      }

      // $FF: synthetic method
      static long access$1200(DiskLruCache.Entry var0) {
         return var0.sequenceNumber;
      }

      // $FF: synthetic method
      static long access$1202(DiskLruCache.Entry var0, long var1) {
         var0.sequenceNumber = var1;
         return var1;
      }

      private IOException invalidLengths(String[] var1) throws IOException {
         throw new IOException("unexpected journal line: " + Arrays.toString(var1));
      }

      private void setLengths(String[] var1) throws IOException {
         if(var1.length != DiskLruCache.this.valueCount) {
            throw this.invalidLengths(var1);
         } else {
            int var2 = 0;

            while(true) {
               try {
                  if(var2 >= var1.length) {
                     return;
                  }

                  this.lengths[var2] = Long.parseLong(var1[var2]);
               } catch (NumberFormatException var4) {
                  throw this.invalidLengths(var1);
               }

               ++var2;
            }
         }
      }

      public File getCleanFile(int var1) {
         return this.cleanFiles[var1];
      }

      public File getDirtyFile(int var1) {
         return this.dirtyFiles[var1];
      }

      public String getLengths() throws IOException {
         StringBuilder var5 = new StringBuilder();
         long[] var6 = this.lengths;
         int var2 = var6.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            long var3 = var6[var1];
            var5.append(' ').append(var3);
         }

         return var5.toString();
      }
   }

   public final class Value {
      private final File[] files;
      private final String key;
      private final long[] lengths;
      private final long sequenceNumber;

      private Value(String var2, long var3, File[] var5, long[] var6) {
         this.key = var2;
         this.sequenceNumber = var3;
         this.files = var5;
         this.lengths = var6;
      }

      // $FF: synthetic method
      Value(String var2, long var3, File[] var5, long[] var6, Object var7) {
         this();
      }

      public DiskLruCache.Editor edit() throws IOException {
         return DiskLruCache.this.edit(this.key, this.sequenceNumber);
      }

      public File getFile(int var1) {
         return this.files[var1];
      }

      public long getLength(int var1) {
         return this.lengths[var1];
      }

      public String getString(int var1) throws IOException {
         return DiskLruCache.inputStreamToString(new FileInputStream(this.files[var1]));
      }
   }
}
