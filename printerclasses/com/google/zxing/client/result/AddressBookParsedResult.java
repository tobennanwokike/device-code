package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class AddressBookParsedResult extends ParsedResult {
   private final String[] addresses;
   private final String birthday;
   private final String[] emails;
   private final String[] names;
   private final String note;
   private final String org;
   private final String[] phoneNumbers;
   private final String pronunciation;
   private final String title;
   private final String url;

   public AddressBookParsedResult(String[] var1, String var2, String[] var3, String[] var4, String var5, String[] var6, String var7, String var8, String var9, String var10) {
      super(ParsedResultType.ADDRESSBOOK);
      this.names = var1;
      this.pronunciation = var2;
      this.phoneNumbers = var3;
      this.emails = var4;
      this.note = var5;
      this.addresses = var6;
      this.org = var7;
      this.birthday = var8;
      this.title = var9;
      this.url = var10;
   }

   public String[] getAddresses() {
      return this.addresses;
   }

   public String getBirthday() {
      return this.birthday;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(100);
      maybeAppend(this.names, var1);
      maybeAppend(this.pronunciation, var1);
      maybeAppend(this.title, var1);
      maybeAppend(this.org, var1);
      maybeAppend(this.addresses, var1);
      maybeAppend(this.phoneNumbers, var1);
      maybeAppend(this.emails, var1);
      maybeAppend(this.url, var1);
      maybeAppend(this.birthday, var1);
      maybeAppend(this.note, var1);
      return var1.toString();
   }

   public String[] getEmails() {
      return this.emails;
   }

   public String[] getNames() {
      return this.names;
   }

   public String getNote() {
      return this.note;
   }

   public String getOrg() {
      return this.org;
   }

   public String[] getPhoneNumbers() {
      return this.phoneNumbers;
   }

   public String getPronunciation() {
      return this.pronunciation;
   }

   public String getTitle() {
      return this.title;
   }

   public String getURL() {
      return this.url;
   }
}
