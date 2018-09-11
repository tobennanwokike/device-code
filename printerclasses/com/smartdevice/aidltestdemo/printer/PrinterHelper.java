package com.smartdevice.aidltestdemo.printer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import com.smartdevice.aidl.IZKCService;
import com.smartdevice.aidltestdemo.printer.entity.GoodsInfo;
import com.smartdevice.aidltestdemo.printer.entity.SupermakerBill;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrinterHelper {
   private static final int GOODS_AMOUNT = 6;
   private static final int GOODS_NAME_LENGTH = 6;
   private static final int GOODS_PRICE_LENGTH = 6;
   private static final int GOODS_UNIT_PRICE_LENGTH = 6;
   private static PrinterHelper _instance;
   private static final int mIzkcService_BUFFER_FLUSH_WAITTIME = 150;
   private static final String mIzkcService_CUT_OFF_RULE = "--------------------------------\n";
   private Context mContext;

   private PrinterHelper(Context var1) {
      this.mContext = var1;
   }

   private void addGoodsInfo(ArrayList var1) {
      GoodsInfo var2 = new GoodsInfo();
      var2.goods_name = "黑人牙膏";
      var2.goods_unit_price = "14.5";
      var2.goods_amount = "2";
      var2.goods_price = "29";
      GoodsInfo var5 = new GoodsInfo();
      var5.goods_name = "啤酒";
      var5.goods_unit_price = "2.5";
      var5.goods_amount = "12";
      var5.goods_price = "30";
      GoodsInfo var7 = new GoodsInfo();
      var7.goods_name = "美的电饭煲";
      var7.goods_unit_price = "288";
      var7.goods_amount = "1";
      var7.goods_price = "288";
      GoodsInfo var6 = new GoodsInfo();
      var6.goods_name = "剃须刀";
      var6.goods_unit_price = "78";
      var6.goods_amount = "1";
      var6.goods_price = "78";
      GoodsInfo var3 = new GoodsInfo();
      var3.goods_name = "泰国进口红提";
      var3.goods_unit_price = "22";
      var3.goods_amount = "2";
      var3.goods_price = "44";
      GoodsInfo var10 = new GoodsInfo();
      var10.goods_name = "太空椒";
      var10.goods_unit_price = "4.5";
      var10.goods_amount = "2";
      var10.goods_price = "9";
      GoodsInfo var9 = new GoodsInfo();
      var9.goods_name = "进口香蕉";
      var9.goods_unit_price = "3.98";
      var9.goods_amount = "3";
      var9.goods_price = "11.86";
      GoodsInfo var8 = new GoodsInfo();
      var8.goods_name = "烟熏腊肉";
      var8.goods_unit_price = "33";
      var8.goods_amount = "2";
      var8.goods_price = "66";
      GoodsInfo var11 = new GoodsInfo();
      var11.goods_name = "长城红葡萄干酒";
      var11.goods_unit_price = "39";
      var11.goods_amount = "2";
      var11.goods_price = "78";
      GoodsInfo var13 = new GoodsInfo();
      var13.goods_name = "白人牙刷";
      var13.goods_unit_price = "14";
      var13.goods_amount = "2";
      var13.goods_price = "28";
      GoodsInfo var4 = new GoodsInfo();
      var4.goods_name = "苹果醋";
      var4.goods_unit_price = "4";
      var4.goods_amount = "5";
      var4.goods_price = "20";
      GoodsInfo var12 = new GoodsInfo();
      var12.goods_name = "这个商品名有点长有点长有点长不是一般的长";
      var12.goods_unit_price = "500";
      var12.goods_amount = "2";
      var12.goods_price = "1000";
      var1.add(var2);
      var1.add(var5);
      var1.add(var7);
      var1.add(var6);
      var1.add(var3);
      var1.add(var10);
      var1.add(var9);
      var1.add(var8);
      var1.add(var11);
      var1.add(var13);
      var1.add(var4);
      var1.add(var12);
   }

   private void generalBitmap(IZKCService var1, SupermakerBill var2, boolean var3, boolean var4) {
      if(var3) {
         var2.start_bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), 2131165299);
      }

      if(var4) {
         try {
            var2.end_bitmap = var1.createQRCode("扫描关注本店，有惊喜喔", 240, 240);
         } catch (RemoteException var5) {
            var5.printStackTrace();
         }
      }

   }

   public static PrinterHelper getInstance(Context var0) {
      synchronized(PrinterHelper.class){}

      PrinterHelper var4;
      try {
         if(_instance == null) {
            PrinterHelper var1 = new PrinterHelper(var0);
            _instance = var1;
         }

         var4 = _instance;
      } finally {
         ;
      }

      return var4;
   }

   public SupermakerBill getSupermakerBill(IZKCService var1, boolean var2, boolean var3) {
      SupermakerBill var4 = new SupermakerBill();
      String var5 = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
      var4.supermaker_name = "XXXX超市";
      var4.serial_number = System.currentTimeMillis() + "";
      var4.purchase_time = var5;
      var4.total_amount = "36";
      var4.total_cash = "1681.86";
      var4.favorable_cash = "81.86";
      var4.receipt_cash = "1600";
      var4.recived_cash = "1600";
      var4.odd_change = "0.0";
      var4.vip_number = "111111111111";
      var4.add_integral = "1600";
      var4.current_integral = "36000";
      var4.supermaker_address = "深圳市宝安区鹤州xxxxxxxx";
      var4.supermaker_call = "0755-99991668";
      this.generalBitmap(var1, var4, var2, var3);
      this.addGoodsInfo(var4.goosInfos);
      return var4;
   }

   public void printPurchaseBillModelOne(IZKCService param1, SupermakerBill param2, int param3) {
      // $FF: Couldn't be decompiled
   }
}
