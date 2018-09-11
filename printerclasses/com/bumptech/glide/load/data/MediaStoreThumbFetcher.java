package com.bumptech.glide.load.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.ExifOrientationStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MediaStoreThumbFetcher implements DataFetcher {
   private static final MediaStoreThumbFetcher.ThumbnailStreamOpenerFactory DEFAULT_FACTORY = new MediaStoreThumbFetcher.ThumbnailStreamOpenerFactory();
   private static final int MINI_HEIGHT = 384;
   private static final int MINI_WIDTH = 512;
   private static final String TAG = "MediaStoreThumbFetcher";
   private final Context context;
   private final DataFetcher defaultFetcher;
   private final MediaStoreThumbFetcher.ThumbnailStreamOpenerFactory factory;
   private final int height;
   private InputStream inputStream;
   private final Uri mediaStoreUri;
   private final int width;

   public MediaStoreThumbFetcher(Context var1, Uri var2, DataFetcher var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, DEFAULT_FACTORY);
   }

   MediaStoreThumbFetcher(Context var1, Uri var2, DataFetcher var3, int var4, int var5, MediaStoreThumbFetcher.ThumbnailStreamOpenerFactory var6) {
      this.context = var1;
      this.mediaStoreUri = var2;
      this.defaultFetcher = var3;
      this.width = var4;
      this.height = var5;
      this.factory = var6;
   }

   private static boolean isMediaStoreUri(Uri var0) {
      boolean var1;
      if(var0 != null && "content".equals(var0.getScheme()) && "media".equals(var0.getAuthority())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isMediaStoreVideo(Uri var0) {
      boolean var1;
      if(isMediaStoreUri(var0) && var0.getPathSegments().contains("video")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private InputStream openThumbInputStream(MediaStoreThumbFetcher.ThumbnailStreamOpener var1) {
      Object var3;
      try {
         var3 = var1.open(this.context, this.mediaStoreUri);
      } catch (FileNotFoundException var4) {
         if(Log.isLoggable("MediaStoreThumbFetcher", 3)) {
            Log.d("MediaStoreThumbFetcher", "Failed to find thumbnail file", var4);
         }

         var3 = null;
      }

      int var2 = -1;
      if(var3 != null) {
         var2 = var1.getOrientation(this.context, this.mediaStoreUri);
      }

      if(var2 != -1) {
         var3 = new ExifOrientationStream((InputStream)var3, var2);
      }

      return (InputStream)var3;
   }

   public void cancel() {
   }

   public void cleanup() {
      if(this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var2) {
            ;
         }
      }

      this.defaultFetcher.cleanup();
   }

   public String getId() {
      return this.mediaStoreUri.toString();
   }

   public InputStream loadData(Priority var1) throws Exception {
      MediaStoreThumbFetcher.ThumbnailStreamOpener var2 = this.factory.build(this.mediaStoreUri, this.width, this.height);
      if(var2 != null) {
         this.inputStream = this.openThumbInputStream(var2);
      }

      if(this.inputStream == null) {
         this.inputStream = (InputStream)this.defaultFetcher.loadData(var1);
      }

      return this.inputStream;
   }

   static class FileService {
      public boolean exists(File var1) {
         return var1.exists();
      }

      public File get(String var1) {
         return new File(var1);
      }

      public long length(File var1) {
         return var1.length();
      }
   }

   static class ImageThumbnailQuery implements MediaStoreThumbFetcher.ThumbnailQuery {
      private static final String[] PATH_PROJECTION = new String[]{"_data"};
      private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";

      public Cursor queryPath(Context var1, Uri var2) {
         String var3 = var2.getLastPathSegment();
         return var1.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND image_id = ?", new String[]{var3}, (String)null);
      }
   }

   interface ThumbnailQuery {
      Cursor queryPath(Context var1, Uri var2);
   }

   static class ThumbnailStreamOpener {
      private static final MediaStoreThumbFetcher.FileService DEFAULT_SERVICE = new MediaStoreThumbFetcher.FileService();
      private MediaStoreThumbFetcher.ThumbnailQuery query;
      private final MediaStoreThumbFetcher.FileService service;

      public ThumbnailStreamOpener(MediaStoreThumbFetcher.FileService var1, MediaStoreThumbFetcher.ThumbnailQuery var2) {
         this.service = var1;
         this.query = var2;
      }

      public ThumbnailStreamOpener(MediaStoreThumbFetcher.ThumbnailQuery var1) {
         this(DEFAULT_SERVICE, var1);
      }

      private Uri parseThumbUri(Cursor var1) {
         Object var2 = null;
         String var3 = var1.getString(0);
         Uri var4 = (Uri)var2;
         if(!TextUtils.isEmpty(var3)) {
            File var5 = this.service.get(var3);
            var4 = (Uri)var2;
            if(this.service.exists(var5)) {
               var4 = (Uri)var2;
               if(this.service.length(var5) > 0L) {
                  var4 = Uri.fromFile(var5);
               }
            }
         }

         return var4;
      }

      public int getOrientation(Context param1, Uri param2) {
         // $FF: Couldn't be decompiled
      }

      public InputStream open(Context var1, Uri var2) throws FileNotFoundException {
         Object var4 = null;
         InputStream var3 = null;
         Cursor var5 = this.query.queryPath(var1, var2);
         var2 = (Uri)var4;
         if(var5 != null) {
            var2 = (Uri)var4;

            try {
               if(var5.moveToFirst()) {
                  var2 = this.parseThumbUri(var5);
               }
            } finally {
               if(var5 != null) {
                  var5.close();
               }

            }
         }

         if(var2 != null) {
            var3 = var1.getContentResolver().openInputStream(var2);
         }

         return var3;
      }
   }

   static class ThumbnailStreamOpenerFactory {
      public MediaStoreThumbFetcher.ThumbnailStreamOpener build(Uri var1, int var2, int var3) {
         MediaStoreThumbFetcher.ThumbnailStreamOpener var4;
         if(MediaStoreThumbFetcher.isMediaStoreUri(var1) && var2 <= 512 && var3 <= 384) {
            if(MediaStoreThumbFetcher.isMediaStoreVideo(var1)) {
               var4 = new MediaStoreThumbFetcher.ThumbnailStreamOpener(new MediaStoreThumbFetcher.VideoThumbnailQuery());
            } else {
               var4 = new MediaStoreThumbFetcher.ThumbnailStreamOpener(new MediaStoreThumbFetcher.ImageThumbnailQuery());
            }
         } else {
            var4 = null;
         }

         return var4;
      }
   }

   static class VideoThumbnailQuery implements MediaStoreThumbFetcher.ThumbnailQuery {
      private static final String[] PATH_PROJECTION = new String[]{"_data"};
      private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";

      public Cursor queryPath(Context var1, Uri var2) {
         String var3 = var2.getLastPathSegment();
         return var1.getContentResolver().query(android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND video_id = ?", new String[]{var3}, (String)null);
      }
   }
}
