package com.smartdevice.aidltestdemo.nfc;

public class Nfc {
   private int SectorId;
   private String content;
   private int id;
   public int type;

   public Nfc(int var1, int var2, String var3, int var4) {
      this.id = var1;
      this.type = var2;
      this.content = var3;
      this.SectorId = var4;
   }

   public String getContent() {
      return this.content;
   }

   public int getId() {
      return this.id;
   }

   public int getSectorId() {
      return this.SectorId;
   }

   public int getType() {
      return this.type;
   }

   public void setContent(String var1) {
      this.content = var1;
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public void setSectorId(int var1) {
      this.SectorId = var1;
   }

   public void setType(int var1) {
      this.type = var1;
   }
}
