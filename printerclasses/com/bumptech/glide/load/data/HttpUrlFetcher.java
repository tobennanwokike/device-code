package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUrlFetcher implements DataFetcher {
   private static final HttpUrlFetcher.HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new HttpUrlFetcher.DefaultHttpUrlConnectionFactory();
   private static final int MAXIMUM_REDIRECTS = 5;
   private static final String TAG = "HttpUrlFetcher";
   private final HttpUrlFetcher.HttpUrlConnectionFactory connectionFactory;
   private final GlideUrl glideUrl;
   private volatile boolean isCancelled;
   private InputStream stream;
   private HttpURLConnection urlConnection;

   public HttpUrlFetcher(GlideUrl var1) {
      this(var1, DEFAULT_CONNECTION_FACTORY);
   }

   HttpUrlFetcher(GlideUrl var1, HttpUrlFetcher.HttpUrlConnectionFactory var2) {
      this.glideUrl = var1;
      this.connectionFactory = var2;
   }

   private InputStream getStreamForSuccessfulRequest(HttpURLConnection var1) throws IOException {
      if(TextUtils.isEmpty(var1.getContentEncoding())) {
         int var2 = var1.getContentLength();
         this.stream = ContentLengthInputStream.obtain(var1.getInputStream(), (long)var2);
      } else {
         if(Log.isLoggable("HttpUrlFetcher", 3)) {
            Log.d("HttpUrlFetcher", "Got non empty content encoding: " + var1.getContentEncoding());
         }

         this.stream = var1.getInputStream();
      }

      return this.stream;
   }

   private InputStream loadDataWithRedirects(URL var1, int var2, URL var3, Map var4) throws IOException {
      if(var2 >= 5) {
         throw new IOException("Too many (> 5) redirects!");
      } else {
         if(var3 != null) {
            try {
               if(var1.toURI().equals(var3.toURI())) {
                  IOException var11 = new IOException("In re-direct loop");
                  throw var11;
               }
            } catch (URISyntaxException var7) {
               ;
            }
         }

         this.urlConnection = this.connectionFactory.build(var1);
         Iterator var6 = var4.entrySet().iterator();

         while(var6.hasNext()) {
            Entry var9 = (Entry)var6.next();
            this.urlConnection.addRequestProperty((String)var9.getKey(), (String)var9.getValue());
         }

         this.urlConnection.setConnectTimeout(2500);
         this.urlConnection.setReadTimeout(2500);
         this.urlConnection.setUseCaches(false);
         this.urlConnection.setDoInput(true);
         this.urlConnection.connect();
         InputStream var8;
         if(this.isCancelled) {
            var8 = null;
         } else {
            int var5 = this.urlConnection.getResponseCode();
            if(var5 / 100 == 2) {
               var8 = this.getStreamForSuccessfulRequest(this.urlConnection);
            } else {
               if(var5 / 100 != 3) {
                  if(var5 == -1) {
                     throw new IOException("Unable to retrieve response code from HttpUrlConnection.");
                  }

                  throw new IOException("Request failed " + var5 + ": " + this.urlConnection.getResponseMessage());
               }

               String var10 = this.urlConnection.getHeaderField("Location");
               if(TextUtils.isEmpty(var10)) {
                  throw new IOException("Received empty or null redirect url");
               }

               var8 = this.loadDataWithRedirects(new URL(var1, var10), var2 + 1, var1, var4);
            }
         }

         return var8;
      }
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public void cleanup() {
      if(this.stream != null) {
         try {
            this.stream.close();
         } catch (IOException var2) {
            ;
         }
      }

      if(this.urlConnection != null) {
         this.urlConnection.disconnect();
      }

   }

   public String getId() {
      return this.glideUrl.getCacheKey();
   }

   public InputStream loadData(Priority var1) throws Exception {
      return this.loadDataWithRedirects(this.glideUrl.toURL(), 0, (URL)null, this.glideUrl.getHeaders());
   }

   private static class DefaultHttpUrlConnectionFactory implements HttpUrlFetcher.HttpUrlConnectionFactory {
      private DefaultHttpUrlConnectionFactory() {
      }

      // $FF: synthetic method
      DefaultHttpUrlConnectionFactory(Object var1) {
         this();
      }

      public HttpURLConnection build(URL var1) throws IOException {
         return (HttpURLConnection)var1.openConnection();
      }
   }

   interface HttpUrlConnectionFactory {
      HttpURLConnection build(URL var1) throws IOException;
   }
}
