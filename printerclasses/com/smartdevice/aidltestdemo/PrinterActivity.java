package com.smartdevice.aidltestdemo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Media;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.smartdevice.aidltestdemo.BaseActivity;
import com.smartdevice.aidltestdemo.common.CmdDialog;
import com.smartdevice.aidltestdemo.language.LanguageModel;
import com.smartdevice.aidltestdemo.language.SpinnerAdapterLanguage;
import com.smartdevice.aidltestdemo.printer.PrinterHelper;
import com.smartdevice.aidltestdemo.printer.entity.SupermakerBill;
import com.smartdevice.aidltestdemo.util.ExecutorFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrinterActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
   private static final int REQUEST_EX = 1;
   public static final String TAG = "PrinterActivity";
   private float PINTER_LINK_TIMEOUT_MAX = 30000.0F;
   private boolean autoOutputPaper = false;
   SupermakerBill bill = null;
   private Button btnBarCode;
   private Button btnClosePower;
   private Button btnMutiThreadPrint;
   private Button btnOpenPower;
   private Button btnPrint;
   private Button btnPrintModelOne;
   private Button btnPrintModelThree;
   private Button btnPrintModelTwo;
   private Button btnPrintPic;
   private Button btnPrintPicGray;
   private Button btnPrintPicRaster;
   private Button btnPrintUnicode1F30;
   private Button btnQrCode;
   private Button btnSelectPic;
   private Button btnUnicode;
   private Button btn_amaplify;
   private Button btn_hAmaplify;
   private Button btn_normal;
   private Button btn_picAlginLeft;
   private Button btn_picAlginMiddle;
   private Button btn_picAlginRight;
   private Button btn_vAmaplify;
   private Button btn_wordAlginLeft;
   private Button btn_wordAlginMiddle;
   private Button btn_wordAlginRight;
   private Button btn_wordToPic;
   private CheckBox cbAutoPrint;
   private boolean checkedPicFlag = false;
   private boolean detectFlag = false;
   private AlertDialog dialog;
   long endTimes = 0L;
   private EditText et_printText;
   private int fontType = 0;
   int imageType = 0;
   final String[] imageTypeArray = new String[]{"POINT", "GRAY", "RASTER"};
   boolean isPrint = true;
   private ImageView iv_printPic;
   private CheckBox mAutoOutputPaper;
   private PrinterActivity.AutoPrintThread mAutoPrintThread = null;
   private Bitmap mBitmap = null;
   PrinterActivity.DetectPrinterThread mDetectPrinterThread;
   private String printTextString = "";
   private RadioGroup radio_cut;
   RadioGroup rg_fontGroup;
   boolean run;
   private boolean runFlag = true;
   private Spinner spinnerLanguage;
   private Spinner spinner_pic_style;
   long startTimes = 0L;
   String text;
   long timeSpace = 0L;
   private TextView tv_printStatus;
   private TextView tv_printer_soft_version;

   private void MutiThreadPrint() {
      this.showStopMutiThreadPrintDialog();
      this.run = true;
      ExecutorFactory.executeThread(new Runnable() {
         public void run() {
            while(true) {
               if(PrinterActivity.this.run) {
                  try {
                     Log.d("PrinterActivity", "run: MutiThreadPrint,Thread 1");
                     BaseActivity.mIzkcService.printGBKText("Thread 1\n");
                     PrinterActivity.this.printPurcase(true, true);
                     Thread.sleep(1L);
                     continue;
                  } catch (InterruptedException var2) {
                     ;
                  } catch (RemoteException var3) {
                     var3.printStackTrace();
                     continue;
                  }
               }

               return;
            }
         }
      });
      ExecutorFactory.executeThread(new Runnable() {
         public void run() {
            while(true) {
               if(PrinterActivity.this.run) {
                  try {
                     Log.d("PrinterActivity", "run: MutiThreadPrint,Thread 2");
                     BaseActivity.mIzkcService.printGBKText("Thread 2\n");
                     PrinterActivity.this.printPurcase(true, true);
                     Thread.sleep(1L);
                     continue;
                  } catch (InterruptedException var2) {
                     ;
                  } catch (RemoteException var3) {
                     var3.printStackTrace();
                     continue;
                  }
               }

               return;
            }
         }
      });
      ExecutorFactory.executeThread(new Runnable() {
         public void run() {
            while(true) {
               if(PrinterActivity.this.run) {
                  try {
                     Log.d("PrinterActivity", "run: MutiThreadPrint,Thread 3");
                     BaseActivity.mIzkcService.printGBKText("Thread 3\n");
                     PrinterActivity.this.printPurcase(true, true);
                     Thread.sleep(1L);
                     continue;
                  } catch (InterruptedException var2) {
                     ;
                  } catch (RemoteException var3) {
                     var3.printStackTrace();
                     continue;
                  }
               }

               return;
            }
         }
      });
      ExecutorFactory.executeThread(new Runnable() {
         public void run() {
            while(true) {
               if(PrinterActivity.this.run) {
                  try {
                     Log.d("PrinterActivity", "run: MutiThreadPrint,Thread 4");
                     BaseActivity.mIzkcService.printGBKText("Thread 4\n");
                     PrinterActivity.this.printPurcase(true, true);
                     Thread.sleep(1L);
                     continue;
                  } catch (InterruptedException var2) {
                     ;
                  } catch (RemoteException var3) {
                     var3.printStackTrace();
                     continue;
                  }
               }

               return;
            }
         }
      });
   }

   // $FF: synthetic method
   static CheckBox access$800(PrinterActivity var0) {
      return var0.cbAutoPrint;
   }

   // $FF: synthetic method
   static RadioGroup access$900(PrinterActivity var0) {
      return var0.radio_cut;
   }

   private static byte charToByte(char var0) {
      return (byte)"0123456789abcdef".indexOf(var0);
   }

   private void checkPrintStateAndDisplayPrinterInfo(String var1) {
      this.enableOrDisEnableKey(true);
      if(DEVICE_MODEL == 800) {
         this.radio_cut.setVisibility(0);
      }

      if(!this.checkedPicFlag) {
         this.generateBarCode();
      }

      try {
         mIzkcService.getPrinterStatus();
         String var2 = mIzkcService.getPrinterStatus();
         this.tv_printStatus.setText(var2);
         String var4 = mIzkcService.getServiceVersion();
         TextView var3 = this.tv_printer_soft_version;
         StringBuilder var6 = new StringBuilder();
         var3.setText(var6.append(var1).append("\nAIDL Service Version:").append(var4).toString());
      } catch (RemoteException var5) {
         var5.printStackTrace();
      }

   }

   private void enableOrDisEnableKey(boolean var1) {
      this.btnPrint.setEnabled(var1);
      this.btnUnicode.setEnabled(var1);
      this.btnPrintPic.setEnabled(var1);
      this.btnPrintModelOne.setEnabled(var1);
      this.btnPrintModelTwo.setEnabled(var1);
      this.btnPrintModelThree.setEnabled(var1);
      this.spinner_pic_style.setEnabled(var1);
      this.spinnerLanguage.setEnabled(var1);
   }

   private void generateBarCode() {
      synchronized(this){}

      try {
         this.mBitmap = mIzkcService.createBarCode("4333333367", 1, 384, 120, true);
         this.iv_printPic.setImageBitmap(this.mBitmap);
      } catch (RemoteException var4) {
         Log.e("", "远程服务未连接...");
         var4.printStackTrace();
      } finally {
         ;
      }

   }

   private void generateQrCode() {
      synchronized(this){}

      try {
         this.mBitmap = mIzkcService.createQRCode("http://www.sznewbest.com", 384, 384);
         this.iv_printPic.setImageBitmap(this.mBitmap);
      } catch (RemoteException var4) {
         Log.e("", "远程服务未连接...");
         var4.printStackTrace();
      } finally {
         ;
      }

   }

   private List getData() {
      String[] var3 = this.getResources().getStringArray(2130837505);
      ArrayList var4 = new ArrayList();

      for(int var1 = 0; var1 < var3.length; ++var1) {
         String[] var2 = var3[var1].split(",");
         if(var2.length == 3) {
            LanguageModel var5 = new LanguageModel();
            var5.code = Integer.parseInt(var2[0]);
            var5.language = var2[1];
            var5.description = var2[1] + " " + var2[2];
            var4.add(var5);
         }
      }

      return var4;
   }

   private void getOverflowMenu() {
      // $FF: Couldn't be decompiled
   }

   public static byte[] hexStringToBytes(String param0) {
      // $FF: Couldn't be decompiled
   }

   private void initEvent() {
      this.btnBarCode.setOnClickListener(this);
      this.btnQrCode.setOnClickListener(this);
      this.btnPrintPic.setOnClickListener(this);
      this.btnPrint.setOnClickListener(this);
      this.btnUnicode.setOnClickListener(this);
      this.btnSelectPic.setOnClickListener(this);
      this.btn_wordToPic.setOnClickListener(this);
      this.btn_normal.setOnClickListener(this);
      this.btn_vAmaplify.setOnClickListener(this);
      this.btn_hAmaplify.setOnClickListener(this);
      this.btn_amaplify.setOnClickListener(this);
      this.rg_fontGroup.setOnCheckedChangeListener(this);
      this.btn_wordAlginLeft.setOnClickListener(this);
      this.btn_wordAlginMiddle.setOnClickListener(this);
      this.btn_wordAlginRight.setOnClickListener(this);
      this.btn_picAlginLeft.setOnClickListener(this);
      this.btn_picAlginMiddle.setOnClickListener(this);
      this.btn_picAlginRight.setOnClickListener(this);
      this.btnPrintModelOne.setOnClickListener(this);
      this.btnPrintModelTwo.setOnClickListener(this);
      this.btnPrintModelThree.setOnClickListener(this);
      this.btnMutiThreadPrint.setOnClickListener(this);
      this.btnPrintUnicode1F30.setOnClickListener(this);
      this.btnPrintPicRaster.setOnClickListener(this);
      this.btnPrintPicGray.setOnClickListener(this);
      this.btnClosePower.setOnClickListener(this);
      this.btnOpenPower.setOnClickListener(this);
   }

   private void initView() {
      this.btnBarCode = (Button)this.findViewById(2131230747);
      this.btnBarCode.requestFocus();
      this.btnQrCode = (Button)this.findViewById(2131230764);
      this.btnPrintPic = (Button)this.findViewById(2131230760);
      this.btnPrint = (Button)this.findViewById(2131230753);
      this.btnUnicode = (Button)this.findViewById(2131230769);
      this.btn_wordToPic = (Button)this.findViewById(2131230798);
      this.btnSelectPic = (Button)this.findViewById(2131230767);
      this.btn_normal = (Button)this.findViewById(2131230779);
      this.btn_amaplify = (Button)this.findViewById(2131230770);
      this.btn_vAmaplify = (Button)this.findViewById(2131230794);
      this.btn_hAmaplify = (Button)this.findViewById(2131230774);
      this.rg_fontGroup = (RadioGroup)this.findViewById(2131230918);
      this.btn_wordAlginLeft = (Button)this.findViewById(2131230795);
      this.btn_wordAlginMiddle = (Button)this.findViewById(2131230796);
      this.btn_wordAlginRight = (Button)this.findViewById(2131230797);
      this.btn_picAlginLeft = (Button)this.findViewById(2131230782);
      this.btn_picAlginMiddle = (Button)this.findViewById(2131230783);
      this.btn_picAlginRight = (Button)this.findViewById(2131230784);
      this.btnPrintModelOne = (Button)this.findViewById(2131230757);
      this.btnPrintModelTwo = (Button)this.findViewById(2131230759);
      this.btnPrintModelThree = (Button)this.findViewById(2131230758);
      this.btnMutiThreadPrint = (Button)this.findViewById(2131230750);
      this.btnPrintPicGray = (Button)this.findViewById(2131230761);
      this.btnPrintPicRaster = (Button)this.findViewById(2131230762);
      this.btnPrintUnicode1F30 = (Button)this.findViewById(2131230763);
      this.btnOpenPower = (Button)this.findViewById(2131230752);
      this.btnClosePower = (Button)this.findViewById(2131230749);
      this.tv_printer_soft_version = (TextView)this.findViewById(2131230976);
      this.spinnerLanguage = (Spinner)this.findViewById(2131230945);
      this.spinner_pic_style = (Spinner)this.findViewById(2131230946);
      this.tv_printStatus = (TextView)this.findViewById(2131230975);
      this.et_printText = (EditText)this.findViewById(2131230853);
      this.iv_printPic = (ImageView)this.findViewById(2131230871);
      this.printTextString = this.getString(2131558486);
      this.et_printText.setText(this.printTextString);
      this.et_printText.setSelection(this.et_printText.getText().toString().length());
      this.mAutoOutputPaper = (CheckBox)this.findViewById(2131230817);
      this.radio_cut = (RadioGroup)this.findViewById(2131230910);
      this.radio_cut.setVisibility(8);
      this.cbAutoPrint = (CheckBox)this.findViewById(2131230818);
      this.cbAutoPrint.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            if(var2) {
               PrinterActivity.this.isPrint = true;
               if(PrinterActivity.this.mAutoPrintThread != null) {
                  PrinterActivity.this.mAutoPrintThread.interrupt();
                  PrinterActivity.this.mAutoPrintThread = null;
               }

               PrinterActivity.this.mAutoPrintThread = PrinterActivity.this.new AutoPrintThread(null);
               PrinterActivity.this.mAutoPrintThread.start();
            } else {
               PrinterActivity.this.isPrint = false;
            }

         }
      });
      this.mAutoOutputPaper.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            if(var2) {
               PrinterActivity.this.autoOutputPaper = true;
            } else {
               PrinterActivity.this.autoOutputPaper = false;
            }

         }
      });
      this.radio_cut.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(RadioGroup param1, int param2) {
            // $FF: Couldn't be decompiled
         }
      });
      SpinnerAdapterLanguage var1 = new SpinnerAdapterLanguage(this, 2131361836, this.getData());
      this.spinnerLanguage.setAdapter(var1);
      this.spinnerLanguage.setOnItemSelectedListener(this);
      this.spinner_pic_style.setAdapter(new ArrayAdapter(this, 2131361836, 2131230965, this.imageTypeArray));
      this.spinner_pic_style.setOnItemSelectedListener(this);
      this.spinner_pic_style.setSelection(2);
   }

   private void printBitmapAlgin(int var1) {
      synchronized(this){}

      try {
         Bitmap var2 = BitmapFactory.decodeResource(this.getResources(), 2131165299);

         try {
            mIzkcService.printBitmapAlgin(var2, 376, 120, var1);
            this.printGapLine();
         } catch (RemoteException var5) {
            var5.printStackTrace();
         }
      } finally {
         ;
      }

   }

   private void printBitmapGray() {
      synchronized(this){}

      try {
         if(this.mBitmap != null) {
            mIzkcService.printImageGray(this.mBitmap);
            if(this.autoOutputPaper) {
               mIzkcService.generateSpace();
            }
         }
      } catch (RemoteException var4) {
         Log.e("", "远程服务未连接...");
         var4.printStackTrace();
      } finally {
         ;
      }

   }

   private void printBitmapRaster() {
      synchronized(this){}

      try {
         if(this.mBitmap != null) {
            mIzkcService.printRasterImage(this.mBitmap);
            if(this.autoOutputPaper) {
               mIzkcService.generateSpace();
            }
         }
      } catch (RemoteException var4) {
         Log.e("", "远程服务未连接...");
         var4.printStackTrace();
      } finally {
         ;
      }

   }

   private void printBitmapUnicode1F30() {
      synchronized(this){}

      try {
         StringBuilder var1 = new StringBuilder();
         this.text = var1.append(this.et_printText.getText().toString()).append("\n").toString();

         try {
            mIzkcService.printUnicode_1F30(this.text);
            if(this.autoOutputPaper) {
               mIzkcService.generateSpace();
            }
         } catch (RemoteException var4) {
            Log.e("", "远程服务未连接...");
            var4.printStackTrace();
         }
      } finally {
         ;
      }

   }

   private void printFont(int var1) {
      synchronized(this){}

      try {
         mIzkcService.printTextWithFont(this.text, var1, 0);
         this.printGapLine();
      } catch (RemoteException var5) {
         Log.e("", "远程服务未连接...");
         var5.printStackTrace();
      } finally {
         ;
      }

   }

   private void printGBKText() {
      synchronized(this){}

      try {
         this.text = this.et_printText.getText().toString();

         try {
            mIzkcService.printGBKText(this.text);
            this.printGapLine();
            if(this.autoOutputPaper) {
               mIzkcService.generateSpace();
            }
         } catch (RemoteException var4) {
            Log.e("", "远程服务未连接...");
            var4.printStackTrace();
         }
      } finally {
         ;
      }

   }

   private void printGapLine() {
      try {
         mIzkcService.printGBKText("\n\n\n");
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   private void printPic() {
      // $FF: Couldn't be decompiled
   }

   private void printPurcase(boolean var1, boolean var2) {
      synchronized(this){}

      try {
         this.bill = PrinterHelper.getInstance(this).getSupermakerBill(mIzkcService, var1, var2);
         PrinterHelper.getInstance(this).printPurchaseBillModelOne(mIzkcService, this.bill, this.imageType);
         this.printGapLine();
      } finally {
         ;
      }

   }

   private void printTextAlgin(int var1) {
      synchronized(this){}

      try {
         mIzkcService.printTextAlgin("智能打印\n", 0, 1, var1);
         this.printGapLine();
      } catch (RemoteException var5) {
         var5.printStackTrace();
      } finally {
         ;
      }

   }

   private void printUnicode() {
      synchronized(this){}

      try {
         StringBuilder var1 = new StringBuilder();
         this.text = var1.append(this.et_printText.getText().toString()).append("\n").toString();

         try {
            mIzkcService.printUnicodeText(this.text);
            this.printGapLine();
            if(this.autoOutputPaper) {
               mIzkcService.generateSpace();
            }
         } catch (RemoteException var4) {
            Log.e("", "远程服务未连接...");
            var4.printStackTrace();
         }
      } finally {
         ;
      }

   }

   private void printVamplify(int var1) {
      synchronized(this){}

      try {
         StringBuilder var2 = new StringBuilder();
         this.text = var2.append(this.et_printText.getText().toString()).append("\n").toString();
         mIzkcService.printTextWithFont(this.text, this.fontType, var1);
         this.printGapLine();
      } catch (RemoteException var5) {
         Log.e("", "远程服务未连接...");
         var5.printStackTrace();
      } finally {
         ;
      }

   }

   public static Bitmap resizeImage(Bitmap param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   private void selectPic() {
      this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), 1);
   }

   private void setPrinterLanguage(int var1) {
      synchronized(this){}

      try {
         LanguageModel var4 = (LanguageModel)this.spinnerLanguage.getItemAtPosition(var1);
         String var2 = var4.language;
         String var3 = var4.description;
         var1 = var4.code;
         StringBuilder var9 = new StringBuilder();
         Log.d("PrinterActivity", var9.append("onItemClick: spinner_language=").append(var3).append(",").append(var1).toString());

         try {
            if(mIzkcService != null) {
               mIzkcService.setPrinterLanguage(var2, var1);
            }
         } catch (RemoteException var7) {
            var7.printStackTrace();
         }
      } finally {
         ;
      }

   }

   private void showCmd() {
      synchronized(this){}

      try {
         StringBuilder var1 = new StringBuilder();
         String var2 = var1.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator).append("cmd.txt").toString();
         File var5 = new File(var2);
         if(!var5.exists()) {
            Toast.makeText(this, "请按规定格式将指令保存在名为cmd.txt文件中，并复制在终端根目录下", 0).show();
         } else {
            CmdDialog.DialogCallBack var7 = new CmdDialog.DialogCallBack() {
               public void submit(String var1) {
                  if(BaseActivity.mIzkcService != null) {
                     byte[] var3 = PrinterActivity.hexStringToBytes(var1);

                     try {
                        BaseActivity.mIzkcService.sendRAWData("printer", var3);
                     } catch (RemoteException var2) {
                        var2.printStackTrace();
                     }
                  }

               }
            };
            CmdDialog var6 = new CmdDialog(this, var7);
            var6.show();
         }
      } finally {
         ;
      }

   }

   private void showStopMutiThreadPrintDialog() {
      Builder var1 = new Builder(this);
      var1.setMessage("是否停止多线程打印......");
      var1.setCancelable(false);
      var1.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            PrinterActivity.this.run = false;
            PrinterActivity.this.btnMutiThreadPrint.setKeepScreenOn(false);
         }
      });
      this.dialog = var1.create();
      this.dialog.show();
   }

   private void wordToPic() {
      synchronized(this){}

      try {
         String var3 = this.et_printText.getText().toString();
         this.mBitmap = Bitmap.createBitmap(384, 30, Config.ARGB_8888);
         Canvas var1 = new Canvas(this.mBitmap);
         var1.drawColor(-1);
         TextPaint var4 = new TextPaint();
         var4.setStyle(Style.FILL);
         var4.setColor(-16777216);
         var4.setTextSize(25.0F);
         StaticLayout var2 = new StaticLayout(var3, var4, this.mBitmap.getWidth(), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
         var2.draw(var1);
         this.iv_printPic.setImageBitmap(this.mBitmap);
      } finally {
         ;
      }

   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         this.mDetectPrinterThread = new PrinterActivity.DetectPrinterThread();
         this.mDetectPrinterThread.start();
      case 18:
      case 19:
      case 20:
      default:
         break;
      case 21:
         this.checkPrintStateAndDisplayPrinterInfo((String)var1.obj);
         break;
      case 22:
         Toast.makeText(this, this.getString(2131558470), 0).show();
      }

   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      this.checkedPicFlag = true;
      if(var1 == 1 && var2 == -1 && var3 != null) {
         Uri var5 = var3.getData();
         String[] var6 = new String[]{"_data"};
         Cursor var4 = this.getContentResolver().query(var5, var6, (String)null, (String[])null, (String)null);
         var4.moveToFirst();
         String var7 = var4.getString(var4.getColumnIndex(var6[0]));
         this.iv_printPic.setImageURI(var5);
         this.mBitmap = BitmapFactory.decodeFile(var7);
         this.iv_printPic.setImageBitmap(this.mBitmap);
         if(this.mBitmap.getHeight() > 384) {
            this.iv_printPic.setImageBitmap(resizeImage(this.mBitmap, 384, 384));
         }

         var4.close();
      }

      super.onActivityResult(var1, var2, var3);
   }

   public void onCheckedChanged(RadioGroup var1, int var2) {
      if(var2 == 2131230914) {
         this.fontType = 0;
      } else if(var2 == 2131230915) {
         this.fontType = 1;
      }

   }

   public void onClick(View param1) {
      // $FF: Couldn't be decompiled
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361825);
      this.initView();
      this.initEvent();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      var1.add(0, 1, 1, "发送指令");
      return super.onCreateOptionsMenu(var1);
   }

   protected void onDestroy() {
      if(mIzkcService != null) {
         try {
            mIzkcService.sendRAWData("printer", new byte[]{(byte)30, (byte)3, (byte)0, (byte)-65, (byte)-40, (byte)-42, (byte)-58});
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
      }

      if(this.mAutoPrintThread != null) {
         this.mAutoPrintThread.interrupt();
         this.mAutoPrintThread = null;
      }

      if(this.mDetectPrinterThread != null) {
         this.runFlag = false;
         this.mDetectPrinterThread.interrupt();
         this.mDetectPrinterThread = null;
      }

      super.onDestroy();
   }

   public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
      switch(var1.getId()) {
      case 2131230945:
         this.setPrinterLanguage(var3);
         break;
      case 2131230946:
         this.imageType = var3;
      }

   }

   public void onNothingSelected(AdapterView var1) {
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      if(var1.getItemId() == 1) {
         this.showCmd();
      }

      return super.onOptionsItemSelected(var1);
   }

   protected void onPause() {
      super.onPause();
      this.run = false;
      if(this.dialog != null) {
         this.dialog.dismiss();
      }

   }

   protected void onResume() {
      this.detectFlag = true;
      this.enableOrDisEnableKey(false);
      super.onResume();
   }

   protected void onStop() {
      super.onStop();
   }

   private class AutoPrintThread extends Thread {
      private AutoPrintThread() {
      }

      // $FF: synthetic method
      AutoPrintThread(Object var2) {
         this();
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   class DetectPrinterThread extends Thread {
      public void run() {
         super.run();

         for(; PrinterActivity.this.runFlag; SystemClock.sleep(1L)) {
            float var1 = (float)SystemClock.currentThreadTimeMillis();
            if(PrinterActivity.this.detectFlag) {
               try {
                  if(BaseActivity.mIzkcService != null) {
                     String var2 = BaseActivity.mIzkcService.getFirmwareVersion1();
                     if(TextUtils.isEmpty(var2)) {
                        BaseActivity.mIzkcService.setModuleFlag(BaseActivity.module_flag);
                        if((float)SystemClock.currentThreadTimeMillis() - var1 > PrinterActivity.this.PINTER_LINK_TIMEOUT_MAX) {
                           PrinterActivity.this.detectFlag = false;
                           PrinterActivity.this.sendEmptyMessage(22);
                        }
                     } else {
                        PrinterActivity.this.sendMessage(21, var2);
                        PrinterActivity.this.detectFlag = false;
                     }
                  }
               } catch (RemoteException var3) {
                  var3.printStackTrace();
               }
            }
         }

      }
   }
}
