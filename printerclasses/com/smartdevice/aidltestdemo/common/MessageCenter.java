package com.smartdevice.aidltestdemo.common;

import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.Vector;

public class MessageCenter {
   private static MessageCenter CENTER = null;
   private List mHandlerList = new Vector();

   public static MessageCenter getInstance() {
      if(CENTER == null) {
         CENTER = new MessageCenter();
      }

      return CENTER;
   }

   public void addHandler(Handler var1) {
      synchronized(this){}

      try {
         this.mHandlerList.add(var1);
      } finally {
         ;
      }

   }

   public void removeHandler(Handler var1) {
      synchronized(this){}

      try {
         this.mHandlerList.remove(var1);
      } finally {
         ;
      }

   }

   public void sendEmptyMessage(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void sendMessage(int var1, Object var2) {
      synchronized(this){}

      try {
         Message var3 = new Message();
         var3.obj = var2;
         var3.what = var1;
         this.sendMessage(var3);
      } finally {
         ;
      }

   }

   public void sendMessage(Message param1) {
      // $FF: Couldn't be decompiled
   }

   public void sendMessageWithPre(int var1, Object var2, int var3) {
      synchronized(this){}

      try {
         Message var4 = new Message();
         var4.obj = var2;
         var4.arg1 = var3;
         var4.what = var1;
         this.sendMessage(var4);
      } finally {
         ;
      }

   }
}
