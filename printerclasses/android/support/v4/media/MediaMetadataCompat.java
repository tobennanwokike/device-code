package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompatApi21;
import android.support.v4.media.RatingCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Set;

public final class MediaMetadataCompat implements Parcelable {
   public static final Creator CREATOR;
   static final ArrayMap METADATA_KEYS_TYPE = new ArrayMap();
   public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
   public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
   public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
   public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
   public static final String METADATA_KEY_ART = "android.media.metadata.ART";
   public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
   public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
   public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
   public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
   public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
   public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
   public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
   public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
   public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
   public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
   public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
   public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
   public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
   public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
   public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
   public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
   public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
   public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
   public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
   public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
   public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
   public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
   public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
   public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
   static final int METADATA_TYPE_BITMAP = 2;
   static final int METADATA_TYPE_LONG = 0;
   static final int METADATA_TYPE_RATING = 3;
   static final int METADATA_TYPE_TEXT = 1;
   private static final String[] PREFERRED_BITMAP_ORDER;
   private static final String[] PREFERRED_DESCRIPTION_ORDER;
   private static final String[] PREFERRED_URI_ORDER;
   private static final String TAG = "MediaMetadata";
   final Bundle mBundle;
   private MediaDescriptionCompat mDescription;
   private Object mMetadataObj;

   static {
      METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
      METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
      METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
      METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
      METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
      METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", Integer.valueOf(0));
      METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", Integer.valueOf(1));
      PREFERRED_DESCRIPTION_ORDER = new String[]{"android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER"};
      PREFERRED_BITMAP_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART"};
      PREFERRED_URI_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI"};
      CREATOR = new Creator() {
         public MediaMetadataCompat createFromParcel(Parcel var1) {
            return new MediaMetadataCompat(var1);
         }

         public MediaMetadataCompat[] newArray(int var1) {
            return new MediaMetadataCompat[var1];
         }
      };
   }

   MediaMetadataCompat(Bundle var1) {
      this.mBundle = new Bundle(var1);
   }

   MediaMetadataCompat(Parcel var1) {
      this.mBundle = var1.readBundle();
   }

   public static MediaMetadataCompat fromMediaMetadata(Object var0) {
      MediaMetadataCompat var3;
      if(var0 != null && VERSION.SDK_INT >= 21) {
         Parcel var2 = Parcel.obtain();
         MediaMetadataCompatApi21.writeToParcel(var0, var2, 0);
         var2.setDataPosition(0);
         MediaMetadataCompat var1 = (MediaMetadataCompat)CREATOR.createFromParcel(var2);
         var2.recycle();
         var1.mMetadataObj = var0;
         var3 = var1;
      } else {
         var3 = null;
      }

      return var3;
   }

   public boolean containsKey(String var1) {
      return this.mBundle.containsKey(var1);
   }

   public int describeContents() {
      return 0;
   }

   public Bitmap getBitmap(String var1) {
      Object var2 = null;

      Bitmap var4;
      try {
         var4 = (Bitmap)this.mBundle.getParcelable(var1);
      } catch (Exception var3) {
         Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", var3);
         var4 = (Bitmap)var2;
      }

      return var4;
   }

   public Bundle getBundle() {
      return this.mBundle;
   }

   public MediaDescriptionCompat getDescription() {
      MediaDescriptionCompat var4;
      if(this.mDescription != null) {
         var4 = this.mDescription;
      } else {
         String var8 = this.getString("android.media.metadata.MEDIA_ID");
         CharSequence[] var7 = new CharSequence[3];
         Uri var5 = null;
         Uri var6 = null;
         CharSequence var10 = this.getText("android.media.metadata.DISPLAY_TITLE");
         int var1;
         if(!TextUtils.isEmpty(var10)) {
            var7[0] = var10;
            var7[1] = this.getText("android.media.metadata.DISPLAY_SUBTITLE");
            var7[2] = this.getText("android.media.metadata.DISPLAY_DESCRIPTION");
         } else {
            int var3 = 0;

            int var2;
            for(var1 = 0; var3 < var7.length && var1 < PREFERRED_DESCRIPTION_ORDER.length; var3 = var2) {
               var10 = this.getText(PREFERRED_DESCRIPTION_ORDER[var1]);
               var2 = var3;
               if(!TextUtils.isEmpty(var10)) {
                  var7[var3] = var10;
                  var2 = var3 + 1;
               }

               ++var1;
            }
         }

         var1 = 0;

         Bitmap var11;
         while(true) {
            var11 = var5;
            if(var1 >= PREFERRED_BITMAP_ORDER.length) {
               break;
            }

            var11 = this.getBitmap(PREFERRED_BITMAP_ORDER[var1]);
            if(var11 != null) {
               break;
            }

            ++var1;
         }

         var1 = 0;

         while(true) {
            var5 = var6;
            if(var1 >= PREFERRED_URI_ORDER.length) {
               break;
            }

            String var12 = this.getString(PREFERRED_URI_ORDER[var1]);
            if(!TextUtils.isEmpty(var12)) {
               var5 = Uri.parse(var12);
               break;
            }

            ++var1;
         }

         var6 = null;
         String var9 = this.getString("android.media.metadata.MEDIA_URI");
         if(!TextUtils.isEmpty(var9)) {
            var6 = Uri.parse(var9);
         }

         MediaDescriptionCompat.Builder var14 = new MediaDescriptionCompat.Builder();
         var14.setMediaId(var8);
         var14.setTitle(var7[0]);
         var14.setSubtitle(var7[1]);
         var14.setDescription(var7[2]);
         var14.setIconBitmap(var11);
         var14.setIconUri(var5);
         var14.setMediaUri(var6);
         if(this.mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE")) {
            Bundle var13 = new Bundle();
            var13.putLong("android.media.extra.BT_FOLDER_TYPE", this.getLong("android.media.metadata.BT_FOLDER_TYPE"));
            var14.setExtras(var13);
         }

         this.mDescription = var14.build();
         var4 = this.mDescription;
      }

      return var4;
   }

   public long getLong(String var1) {
      return this.mBundle.getLong(var1, 0L);
   }

   public Object getMediaMetadata() {
      Object var1;
      if(this.mMetadataObj == null && VERSION.SDK_INT >= 21) {
         Parcel var2 = Parcel.obtain();
         this.writeToParcel(var2, 0);
         var2.setDataPosition(0);
         this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(var2);
         var2.recycle();
         var1 = this.mMetadataObj;
      } else {
         var1 = this.mMetadataObj;
      }

      return var1;
   }

   public RatingCompat getRating(String var1) {
      Object var2 = null;

      RatingCompat var4;
      try {
         if(VERSION.SDK_INT >= 19) {
            var4 = RatingCompat.fromRating(this.mBundle.getParcelable(var1));
         } else {
            var4 = (RatingCompat)this.mBundle.getParcelable(var1);
         }
      } catch (Exception var3) {
         Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", var3);
         var4 = (RatingCompat)var2;
      }

      return var4;
   }

   public String getString(String var1) {
      CharSequence var2 = this.mBundle.getCharSequence(var1);
      if(var2 != null) {
         var1 = var2.toString();
      } else {
         var1 = null;
      }

      return var1;
   }

   public CharSequence getText(String var1) {
      return this.mBundle.getCharSequence(var1);
   }

   public Set keySet() {
      return this.mBundle.keySet();
   }

   public int size() {
      return this.mBundle.size();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeBundle(this.mBundle);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface BitmapKey {
   }

   public static final class Builder {
      private final Bundle mBundle;

      public Builder() {
         this.mBundle = new Bundle();
      }

      public Builder(MediaMetadataCompat var1) {
         this.mBundle = new Bundle(var1.mBundle);
      }

      @RestrictTo({RestrictTo.Scope.GROUP_ID})
      public Builder(MediaMetadataCompat var1, int var2) {
         this(var1);
         Iterator var3 = this.mBundle.keySet().iterator();

         while(true) {
            String var5;
            Bitmap var6;
            do {
               do {
                  while(true) {
                     Object var4;
                     do {
                        do {
                           if(!var3.hasNext()) {
                              return;
                           }

                           var5 = (String)var3.next();
                           var4 = this.mBundle.get(var5);
                        } while(var4 == null);
                     } while(!(var4 instanceof Bitmap));

                     var6 = (Bitmap)var4;
                     if(var6.getHeight() <= var2 && var6.getWidth() <= var2) {
                        break;
                     }

                     this.putBitmap(var5, this.scaleBitmap(var6, var2));
                  }
               } while(VERSION.SDK_INT < 14);
            } while(!var5.equals("android.media.metadata.ART") && !var5.equals("android.media.metadata.ALBUM_ART"));

            this.putBitmap(var5, var6.copy(var6.getConfig(), false));
         }
      }

      private Bitmap scaleBitmap(Bitmap var1, int var2) {
         float var3 = (float)var2;
         var3 = Math.min(var3 / (float)var1.getWidth(), var3 / (float)var1.getHeight());
         var2 = (int)((float)var1.getHeight() * var3);
         return Bitmap.createScaledBitmap(var1, (int)((float)var1.getWidth() * var3), var2, true);
      }

      public MediaMetadataCompat build() {
         return new MediaMetadataCompat(this.mBundle);
      }

      public MediaMetadataCompat.Builder putBitmap(String var1, Bitmap var2) {
         if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1)).intValue() != 2) {
            throw new IllegalArgumentException("The " + var1 + " key cannot be used to put a Bitmap");
         } else {
            this.mBundle.putParcelable(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putLong(String var1, long var2) {
         if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1)).intValue() != 0) {
            throw new IllegalArgumentException("The " + var1 + " key cannot be used to put a long");
         } else {
            this.mBundle.putLong(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putRating(String var1, RatingCompat var2) {
         if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1)).intValue() != 3) {
            throw new IllegalArgumentException("The " + var1 + " key cannot be used to put a Rating");
         } else {
            if(VERSION.SDK_INT >= 19) {
               this.mBundle.putParcelable(var1, (Parcelable)var2.getRating());
            } else {
               this.mBundle.putParcelable(var1, var2);
            }

            return this;
         }
      }

      public MediaMetadataCompat.Builder putString(String var1, String var2) {
         if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1)).intValue() != 1) {
            throw new IllegalArgumentException("The " + var1 + " key cannot be used to put a String");
         } else {
            this.mBundle.putCharSequence(var1, var2);
            return this;
         }
      }

      public MediaMetadataCompat.Builder putText(String var1, CharSequence var2) {
         if(MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(var1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(var1)).intValue() != 1) {
            throw new IllegalArgumentException("The " + var1 + " key cannot be used to put a CharSequence");
         } else {
            this.mBundle.putCharSequence(var1, var2);
            return this;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface LongKey {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface RatingKey {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface TextKey {
   }
}
