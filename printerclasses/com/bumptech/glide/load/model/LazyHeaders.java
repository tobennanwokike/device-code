package com.bumptech.glide.load.model;

import android.text.TextUtils;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class LazyHeaders implements Headers {
   private volatile Map combinedHeaders;
   private final Map headers;

   LazyHeaders(Map var1) {
      this.headers = Collections.unmodifiableMap(var1);
   }

   private Map generateHeaders() {
      HashMap var5 = new HashMap();
      Iterator var3 = this.headers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var6 = (Entry)var3.next();
         StringBuilder var4 = new StringBuilder();
         List var2 = (List)var6.getValue();

         for(int var1 = 0; var1 < var2.size(); ++var1) {
            var4.append(((LazyHeaderFactory)var2.get(var1)).buildHeader());
            if(var1 != var2.size() - 1) {
               var4.append(',');
            }
         }

         var5.put(var6.getKey(), var4.toString());
      }

      return var5;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if(var1 instanceof LazyHeaders) {
         LazyHeaders var3 = (LazyHeaders)var1;
         var2 = this.headers.equals(var3.headers);
      } else {
         var2 = false;
      }

      return var2;
   }

   public Map getHeaders() {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return this.headers.hashCode();
   }

   public String toString() {
      return "LazyHeaders{headers=" + this.headers + '}';
   }

   public static final class Builder {
      private static final String DEFAULT_ENCODING = "identity";
      private static final Map DEFAULT_HEADERS;
      private static final String DEFAULT_USER_AGENT = System.getProperty("http.agent");
      private static final String ENCODING_HEADER = "Accept-Encoding";
      private static final String USER_AGENT_HEADER = "User-Agent";
      private boolean copyOnModify = true;
      private Map headers;
      private boolean isEncodingDefault;
      private boolean isUserAgentDefault;

      static {
         HashMap var0 = new HashMap(2);
         if(!TextUtils.isEmpty(DEFAULT_USER_AGENT)) {
            var0.put("User-Agent", Collections.singletonList(new LazyHeaders.StringHeaderFactory(DEFAULT_USER_AGENT)));
         }

         var0.put("Accept-Encoding", Collections.singletonList(new LazyHeaders.StringHeaderFactory("identity")));
         DEFAULT_HEADERS = Collections.unmodifiableMap(var0);
      }

      public Builder() {
         this.headers = DEFAULT_HEADERS;
         this.isEncodingDefault = true;
         this.isUserAgentDefault = true;
      }

      private Map copyHeaders() {
         HashMap var2 = new HashMap(this.headers.size());
         Iterator var1 = this.headers.entrySet().iterator();

         while(var1.hasNext()) {
            Entry var3 = (Entry)var1.next();
            var2.put(var3.getKey(), new ArrayList((Collection)var3.getValue()));
         }

         return var2;
      }

      private void copyIfNecessary() {
         if(this.copyOnModify) {
            this.copyOnModify = false;
            this.headers = this.copyHeaders();
         }

      }

      private List getFactories(String var1) {
         List var3 = (List)this.headers.get(var1);
         Object var2 = var3;
         if(var3 == null) {
            var2 = new ArrayList();
            this.headers.put(var1, var2);
         }

         return (List)var2;
      }

      public LazyHeaders.Builder addHeader(String var1, LazyHeaderFactory var2) {
         LazyHeaders.Builder var3;
         if((!this.isEncodingDefault || !"Accept-Encoding".equalsIgnoreCase(var1)) && (!this.isUserAgentDefault || !"User-Agent".equalsIgnoreCase(var1))) {
            this.copyIfNecessary();
            this.getFactories(var1).add(var2);
            var3 = this;
         } else {
            var3 = this.setHeader(var1, var2);
         }

         return var3;
      }

      public LazyHeaders.Builder addHeader(String var1, String var2) {
         return this.addHeader(var1, (LazyHeaderFactory)(new LazyHeaders.StringHeaderFactory(var2)));
      }

      public LazyHeaders build() {
         this.copyOnModify = true;
         return new LazyHeaders(this.headers);
      }

      public LazyHeaders.Builder setHeader(String var1, LazyHeaderFactory var2) {
         this.copyIfNecessary();
         if(var2 == null) {
            this.headers.remove(var1);
         } else {
            List var3 = this.getFactories(var1);
            var3.clear();
            var3.add(var2);
         }

         if(this.isEncodingDefault && "Accept-Encoding".equalsIgnoreCase(var1)) {
            this.isEncodingDefault = false;
         }

         if(this.isUserAgentDefault && "User-Agent".equalsIgnoreCase(var1)) {
            this.isUserAgentDefault = false;
         }

         return this;
      }

      public LazyHeaders.Builder setHeader(String var1, String var2) {
         LazyHeaders.StringHeaderFactory var3;
         if(var2 == null) {
            var3 = null;
         } else {
            var3 = new LazyHeaders.StringHeaderFactory(var2);
         }

         return this.setHeader(var1, (LazyHeaderFactory)var3);
      }
   }

   static final class StringHeaderFactory implements LazyHeaderFactory {
      private final String value;

      StringHeaderFactory(String var1) {
         this.value = var1;
      }

      public String buildHeader() {
         return this.value;
      }

      public boolean equals(Object var1) {
         boolean var2;
         if(var1 instanceof LazyHeaders.StringHeaderFactory) {
            LazyHeaders.StringHeaderFactory var3 = (LazyHeaders.StringHeaderFactory)var1;
            var2 = this.value.equals(var3.value);
         } else {
            var2 = false;
         }

         return var2;
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      public String toString() {
         return "StringHeaderFactory{value=\'" + this.value + '\'' + '}';
      }
   }
}
