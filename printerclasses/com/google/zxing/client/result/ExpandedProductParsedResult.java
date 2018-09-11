package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import java.util.Hashtable;

public class ExpandedProductParsedResult extends ParsedResult {
   public static final String KILOGRAM = "KG";
   public static final String POUND = "LB";
   private final String bestBeforeDate;
   private final String expirationDate;
   private final String lotNumber;
   private final String packagingDate;
   private final String price;
   private final String priceCurrency;
   private final String priceIncrement;
   private final String productID;
   private final String productionDate;
   private final String sscc;
   private final Hashtable uncommonAIs;
   private final String weight;
   private final String weightIncrement;
   private final String weightType;

   ExpandedProductParsedResult() {
      super(ParsedResultType.PRODUCT);
      this.productID = "";
      this.sscc = "";
      this.lotNumber = "";
      this.productionDate = "";
      this.packagingDate = "";
      this.bestBeforeDate = "";
      this.expirationDate = "";
      this.weight = "";
      this.weightType = "";
      this.weightIncrement = "";
      this.price = "";
      this.priceIncrement = "";
      this.priceCurrency = "";
      this.uncommonAIs = new Hashtable();
   }

   public ExpandedProductParsedResult(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, Hashtable var14) {
      super(ParsedResultType.PRODUCT);
      this.productID = var1;
      this.sscc = var2;
      this.lotNumber = var3;
      this.productionDate = var4;
      this.packagingDate = var5;
      this.bestBeforeDate = var6;
      this.expirationDate = var7;
      this.weight = var8;
      this.weightType = var9;
      this.weightIncrement = var10;
      this.price = var11;
      this.priceIncrement = var12;
      this.priceCurrency = var13;
      this.uncommonAIs = var14;
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      boolean var2;
      if(!(var1 instanceof ExpandedProductParsedResult)) {
         var2 = var3;
      } else {
         ExpandedProductParsedResult var4 = (ExpandedProductParsedResult)var1;
         var2 = var3;
         if(this.productID.equals(var4.productID)) {
            var2 = var3;
            if(this.sscc.equals(var4.sscc)) {
               var2 = var3;
               if(this.lotNumber.equals(var4.lotNumber)) {
                  var2 = var3;
                  if(this.productionDate.equals(var4.productionDate)) {
                     var2 = var3;
                     if(this.bestBeforeDate.equals(var4.bestBeforeDate)) {
                        var2 = var3;
                        if(this.expirationDate.equals(var4.expirationDate)) {
                           var2 = var3;
                           if(this.weight.equals(var4.weight)) {
                              var2 = var3;
                              if(this.weightType.equals(var4.weightType)) {
                                 var2 = var3;
                                 if(this.weightIncrement.equals(var4.weightIncrement)) {
                                    var2 = var3;
                                    if(this.price.equals(var4.price)) {
                                       var2 = var3;
                                       if(this.priceIncrement.equals(var4.priceIncrement)) {
                                          var2 = var3;
                                          if(this.priceCurrency.equals(var4.priceCurrency)) {
                                             var2 = var3;
                                             if(this.uncommonAIs.equals(var4.uncommonAIs)) {
                                                var2 = true;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   public String getBestBeforeDate() {
      return this.bestBeforeDate;
   }

   public String getDisplayResult() {
      return this.productID;
   }

   public String getExpirationDate() {
      return this.expirationDate;
   }

   public String getLotNumber() {
      return this.lotNumber;
   }

   public String getPackagingDate() {
      return this.packagingDate;
   }

   public String getPrice() {
      return this.price;
   }

   public String getPriceCurrency() {
      return this.priceCurrency;
   }

   public String getPriceIncrement() {
      return this.priceIncrement;
   }

   public String getProductID() {
      return this.productID;
   }

   public String getProductionDate() {
      return this.productionDate;
   }

   public String getSscc() {
      return this.sscc;
   }

   public Hashtable getUncommonAIs() {
      return this.uncommonAIs;
   }

   public String getWeight() {
      return this.weight;
   }

   public String getWeightIncrement() {
      return this.weightIncrement;
   }

   public String getWeightType() {
      return this.weightType;
   }

   public int hashCode() {
      return (((((this.productID.hashCode() * 31 + this.sscc.hashCode()) * 31 + this.lotNumber.hashCode()) * 31 + this.productionDate.hashCode()) * 31 + this.bestBeforeDate.hashCode()) * 31 + this.expirationDate.hashCode()) * 31 + this.weight.hashCode() ^ ((((this.weightType.hashCode() * 31 + this.weightIncrement.hashCode()) * 31 + this.price.hashCode()) * 31 + this.priceIncrement.hashCode()) * 31 + this.priceCurrency.hashCode()) * 31 + this.uncommonAIs.hashCode();
   }
}
