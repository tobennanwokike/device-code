package com.smartdevice.aidltestdemo.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.smartdevice.aidltestdemo.nfc.Nfc;
import java.util.List;

public class NFCActivity extends Activity {
   private String dataString;
   private Intent intents;
   private boolean isnews = true;
   public List list;
   private ListView listView;
   private IntentFilter[] mFilters;
   private String[][] mTechLists;
   private Nfc mynfc;
   private NfcAdapter nfcAdapter;
   private byte[] password = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1};
   private PendingIntent pendingIntent;
   private TextView promt;

   // $FF: synthetic method
   static String access$100(NFCActivity var0) {
      return var0.dataString;
   }

   // $FF: synthetic method
   static String access$102(NFCActivity var0, String var1) {
      var0.dataString = var1;
      return var1;
   }

   // $FF: synthetic method
   static Intent access$200(NFCActivity var0) {
      return var0.intents;
   }

   // $FF: synthetic method
   static void access$300(NFCActivity var0, Intent var1) {
      var0.processIntent(var1);
   }

   private String bytesToHexString(byte[] var1) {
      StringBuilder var4 = new StringBuilder("");
      String var5;
      if(var1 != null && var1.length > 0) {
         char[] var3 = new char[2];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var3[0] = Character.forDigit(var1[var2] >>> 4 & 15, 16);
            var3[1] = Character.forDigit(var1[var2] & 15, 16);
            var4.append(var3);
         }

         var5 = var4.toString();
      } else {
         var5 = null;
      }

      return var5;
   }

   private String bytesToHexString2(byte[] var1) {
      StringBuilder var3 = new StringBuilder("");
      String var5;
      if(var1 != null && var1.length > 0) {
         char[] var4 = new char[2];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var4[0] = Character.forDigit(var1[var2] >>> 4 & 15, 16);
            var4[1] = Character.forDigit(var1[var2] & 15, 16);
            var3.append(var4);
            var3.append(" ");
         }

         var5 = var3.toString();
      } else {
         var5 = null;
      }

      return var5;
   }

   private void processIntent(Intent param1) {
      // $FF: Couldn't be decompiled
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361840);
      this.listView = (ListView)this.findViewById(2131230882);
      this.promt = (TextView)this.findViewById(2131230899);
      this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
      if(this.nfcAdapter == null) {
         this.promt.setText("Device can not support NFC！");
         this.finish();
      } else if(!this.nfcAdapter.isEnabled()) {
         this.promt.setText("Please open NFC in system setting！");
         this.finish();
      } else {
         this.pendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(536870912), 0);
         IntentFilter var3 = new IntentFilter("android.nfc.action.TECH_DISCOVERED");
         var3.addCategory("*/*");
         this.mFilters = new IntentFilter[]{var3};
         String var4 = MifareClassic.class.getName();
         String var2 = NfcA.class.getName();
         this.mTechLists = new String[][]{{var4}, {var2}};
      }

   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      if("android.nfc.action.TECH_DISCOVERED".equals(var1.getAction())) {
         this.processIntent(var1);
         this.intents = var1;
      }

   }

   protected void onResume() {
      super.onResume();
      this.nfcAdapter.enableForegroundDispatch(this, this.pendingIntent, this.mFilters, this.mTechLists);
      if(this.isnews && "android.nfc.action.TECH_DISCOVERED".equals(this.getIntent().getAction())) {
         this.processIntent(this.getIntent());
         this.intents = this.getIntent();
         this.isnews = false;
      }

   }

   class ListLongClick implements OnItemLongClickListener {
      public boolean onItemLongClick(AdapterView var1, View var2, int var3, long var4) {
         NFCActivity.this.mynfc = (Nfc)NFCActivity.this.list.get(var3);
         if(NFCActivity.this.mynfc.type == 1) {
            var2 = LayoutInflater.from(NFCActivity.this).inflate(2131361842, (ViewGroup)null);
            final EditText var7 = (EditText)var2.findViewById(2131230852);
            Builder var6 = new Builder(NFCActivity.this);
            var6.setView(var2);
            var6.setTitle("Write to tag");
            var6.setPositiveButton("Write", new OnClickListener() {
               public void onClick(DialogInterface param1, int param2) {
                  // $FF: Couldn't be decompiled
               }
            }).setNegativeButton("Cancel", (OnClickListener)null);
            var6.create().show();
         }

         return false;
      }
   }
}
