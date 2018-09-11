package com.smartdevice.aidltestdemo.common;

public class MessageType {
   public interface BaiscMessage {
      int BASE = 16;
      int DETECT_PRINTER_FAIL = 20;
      int DETECT_PRINTER_SUCCESS = 21;
      int GET_IDETIFY_INFO_SUCCESS = 19;
      int PRINTER_LINK_TIMEOUT = 22;
      int SCAN_RESULT_GET_SUCCESS = 23;
      int SEVICE_BIND_FAIL = 18;
      int SEVICE_BIND_SUCCESS = 17;
   }
}
