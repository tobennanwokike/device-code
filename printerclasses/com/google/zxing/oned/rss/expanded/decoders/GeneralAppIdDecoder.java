package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.BlockParsedResult;
import com.google.zxing.oned.rss.expanded.decoders.CurrentParsingState;
import com.google.zxing.oned.rss.expanded.decoders.DecodedChar;
import com.google.zxing.oned.rss.expanded.decoders.DecodedInformation;
import com.google.zxing.oned.rss.expanded.decoders.DecodedNumeric;
import com.google.zxing.oned.rss.expanded.decoders.FieldParser;

final class GeneralAppIdDecoder {
   private final StringBuffer buffer = new StringBuffer();
   private final CurrentParsingState current = new CurrentParsingState();
   private final BitArray information;

   GeneralAppIdDecoder(BitArray var1) {
      this.information = var1;
   }

   private DecodedChar decodeAlphanumeric(int var1) {
      int var2 = this.extractNumericValueFromBitArray(var1, 5);
      DecodedChar var3;
      if(var2 == 15) {
         var3 = new DecodedChar(var1 + 5, '$');
      } else if(var2 >= 5 && var2 < 15) {
         var3 = new DecodedChar(var1 + 5, (char)(var2 + 48 - 5));
      } else {
         var2 = this.extractNumericValueFromBitArray(var1, 6);
         if(var2 >= 32 && var2 < 58) {
            var3 = new DecodedChar(var1 + 6, (char)(var2 + 33));
         } else {
            switch(var2) {
            case 58:
               var3 = new DecodedChar(var1 + 6, '*');
               break;
            case 59:
               var3 = new DecodedChar(var1 + 6, ',');
               break;
            case 60:
               var3 = new DecodedChar(var1 + 6, '-');
               break;
            case 61:
               var3 = new DecodedChar(var1 + 6, '.');
               break;
            case 62:
               var3 = new DecodedChar(var1 + 6, '/');
               break;
            default:
               throw new RuntimeException("Decoding invalid alphanumeric value: " + var2);
            }
         }
      }

      return var3;
   }

   private DecodedChar decodeIsoIec646(int var1) {
      int var2 = this.extractNumericValueFromBitArray(var1, 5);
      DecodedChar var3;
      if(var2 == 15) {
         var3 = new DecodedChar(var1 + 5, '$');
      } else if(var2 >= 5 && var2 < 15) {
         var3 = new DecodedChar(var1 + 5, (char)(var2 + 48 - 5));
      } else {
         var2 = this.extractNumericValueFromBitArray(var1, 7);
         if(var2 >= 64 && var2 < 90) {
            var3 = new DecodedChar(var1 + 7, (char)(var2 + 1));
         } else if(var2 >= 90 && var2 < 116) {
            var3 = new DecodedChar(var1 + 7, (char)(var2 + 7));
         } else {
            var2 = this.extractNumericValueFromBitArray(var1, 8);
            switch(var2) {
            case 232:
               var3 = new DecodedChar(var1 + 8, '!');
               break;
            case 233:
               var3 = new DecodedChar(var1 + 8, '\"');
               break;
            case 234:
               var3 = new DecodedChar(var1 + 8, '%');
               break;
            case 235:
               var3 = new DecodedChar(var1 + 8, '&');
               break;
            case 236:
               var3 = new DecodedChar(var1 + 8, '\'');
               break;
            case 237:
               var3 = new DecodedChar(var1 + 8, '(');
               break;
            case 238:
               var3 = new DecodedChar(var1 + 8, ')');
               break;
            case 239:
               var3 = new DecodedChar(var1 + 8, '*');
               break;
            case 240:
               var3 = new DecodedChar(var1 + 8, '+');
               break;
            case 241:
               var3 = new DecodedChar(var1 + 8, ',');
               break;
            case 242:
               var3 = new DecodedChar(var1 + 8, '-');
               break;
            case 243:
               var3 = new DecodedChar(var1 + 8, '.');
               break;
            case 244:
               var3 = new DecodedChar(var1 + 8, '/');
               break;
            case 245:
               var3 = new DecodedChar(var1 + 8, ':');
               break;
            case 246:
               var3 = new DecodedChar(var1 + 8, ';');
               break;
            case 247:
               var3 = new DecodedChar(var1 + 8, '<');
               break;
            case 248:
               var3 = new DecodedChar(var1 + 8, '=');
               break;
            case 249:
               var3 = new DecodedChar(var1 + 8, '>');
               break;
            case 250:
               var3 = new DecodedChar(var1 + 8, '?');
               break;
            case 251:
               var3 = new DecodedChar(var1 + 8, '_');
               break;
            case 252:
               var3 = new DecodedChar(var1 + 8, ' ');
               break;
            default:
               throw new RuntimeException("Decoding invalid ISO/IEC 646 value: " + var2);
            }
         }
      }

      return var3;
   }

   private DecodedNumeric decodeNumeric(int var1) {
      DecodedNumeric var3;
      if(var1 + 7 > this.information.size) {
         var1 = this.extractNumericValueFromBitArray(var1, 4);
         if(var1 == 0) {
            var3 = new DecodedNumeric(this.information.size, 10, 10);
         } else {
            var3 = new DecodedNumeric(this.information.size, var1 - 1, 10);
         }
      } else {
         int var2 = this.extractNumericValueFromBitArray(var1, 7);
         var3 = new DecodedNumeric(var1 + 7, (var2 - 8) / 11, (var2 - 8) % 11);
      }

      return var3;
   }

   static int extractNumericValueFromBitArray(BitArray var0, int var1, int var2) {
      int var4 = 0;
      if(var2 > 32) {
         throw new IllegalArgumentException("extractNumberValueFromBitArray can\'t handle more than 32 bits");
      } else {
         int var5;
         for(int var3 = 0; var3 < var2; var4 = var5) {
            var5 = var4;
            if(var0.get(var1 + var3)) {
               var5 = var4 | 1 << var2 - var3 - 1;
            }

            ++var3;
         }

         return var4;
      }
   }

   private boolean isAlphaOr646ToNumericLatch(int var1) {
      boolean var4 = false;
      boolean var3;
      if(var1 + 3 > this.information.size) {
         var3 = var4;
      } else {
         int var2 = var1;

         while(true) {
            if(var2 >= var1 + 3) {
               var3 = true;
               break;
            }

            var3 = var4;
            if(this.information.get(var2)) {
               break;
            }

            ++var2;
         }
      }

      return var3;
   }

   private boolean isAlphaTo646ToAlphaLatch(int var1) {
      boolean var4 = false;
      boolean var3;
      if(var1 + 1 > this.information.size) {
         var3 = var4;
      } else {
         for(int var2 = 0; var2 < 5 && var2 + var1 < this.information.size; ++var2) {
            if(var2 == 2) {
               var3 = var4;
               if(!this.information.get(var1 + 2)) {
                  return var3;
               }
            } else if(this.information.get(var1 + var2)) {
               var3 = var4;
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   private boolean isNumericToAlphaNumericLatch(int var1) {
      boolean var4 = false;
      boolean var3;
      if(var1 + 1 > this.information.size) {
         var3 = var4;
      } else {
         for(int var2 = 0; var2 < 4 && var2 + var1 < this.information.size; ++var2) {
            var3 = var4;
            if(this.information.get(var1 + var2)) {
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   private boolean isStillAlpha(int var1) {
      boolean var4 = true;
      boolean var3 = false;
      if(var1 + 5 <= this.information.size) {
         int var2 = this.extractNumericValueFromBitArray(var1, 5);
         if(var2 >= 5 && var2 < 16) {
            var3 = true;
         } else if(var1 + 6 <= this.information.size) {
            var1 = this.extractNumericValueFromBitArray(var1, 6);
            if(var1 >= 16 && var1 < 63) {
               var3 = var4;
            } else {
               var3 = false;
            }
         }
      }

      return var3;
   }

   private boolean isStillIsoIec646(int var1) {
      boolean var4 = true;
      boolean var5 = false;
      boolean var3;
      if(var1 + 5 > this.information.size) {
         var3 = var5;
      } else {
         int var2 = this.extractNumericValueFromBitArray(var1, 5);
         if(var2 >= 5 && var2 < 16) {
            var3 = true;
         } else {
            var3 = var5;
            if(var1 + 7 <= this.information.size) {
               var2 = this.extractNumericValueFromBitArray(var1, 7);
               if(var2 >= 64 && var2 < 116) {
                  var3 = true;
               } else {
                  var3 = var5;
                  if(var1 + 8 <= this.information.size) {
                     var1 = this.extractNumericValueFromBitArray(var1, 8);
                     if(var1 >= 232 && var1 < 253) {
                        var3 = var4;
                     } else {
                        var3 = false;
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   private boolean isStillNumeric(int var1) {
      boolean var4 = true;
      boolean var3;
      if(var1 + 7 > this.information.size) {
         if(var1 + 4 <= this.information.size) {
            var3 = var4;
         } else {
            var3 = false;
         }
      } else {
         int var2 = var1;

         while(true) {
            if(var2 >= var1 + 3) {
               var3 = this.information.get(var1 + 3);
               break;
            }

            var3 = var4;
            if(this.information.get(var2)) {
               break;
            }

            ++var2;
         }
      }

      return var3;
   }

   private BlockParsedResult parseAlphaBlock() {
      while(true) {
         BlockParsedResult var2;
         if(this.isStillAlpha(this.current.position)) {
            DecodedChar var1 = this.decodeAlphanumeric(this.current.position);
            this.current.position = var1.getNewPosition();
            if(!var1.isFNC1()) {
               this.buffer.append(var1.getValue());
               continue;
            }

            var2 = new BlockParsedResult(new DecodedInformation(this.current.position, this.buffer.toString()), true);
         } else {
            CurrentParsingState var3;
            if(this.isAlphaOr646ToNumericLatch(this.current.position)) {
               var3 = this.current;
               var3.position += 3;
               this.current.setNumeric();
            } else if(this.isAlphaTo646ToAlphaLatch(this.current.position)) {
               if(this.current.position + 5 < this.information.size) {
                  var3 = this.current;
                  var3.position += 5;
               } else {
                  this.current.position = this.information.size;
               }

               this.current.setIsoIec646();
            }

            var2 = new BlockParsedResult(false);
         }

         return var2;
      }
   }

   private DecodedInformation parseBlocks() {
      boolean var2;
      BlockParsedResult var3;
      boolean var4;
      do {
         int var1 = this.current.position;
         if(this.current.isAlpha()) {
            var3 = this.parseAlphaBlock();
            var2 = var3.isFinished();
         } else if(this.current.isIsoIec646()) {
            var3 = this.parseIsoIec646Block();
            var2 = var3.isFinished();
         } else {
            var3 = this.parseNumericBlock();
            var2 = var3.isFinished();
         }

         if(var1 != this.current.position) {
            var4 = true;
         } else {
            var4 = false;
         }
      } while((var4 || var2) && !var2);

      return var3.getDecodedInformation();
   }

   private BlockParsedResult parseIsoIec646Block() {
      while(true) {
         BlockParsedResult var2;
         if(this.isStillIsoIec646(this.current.position)) {
            DecodedChar var1 = this.decodeIsoIec646(this.current.position);
            this.current.position = var1.getNewPosition();
            if(!var1.isFNC1()) {
               this.buffer.append(var1.getValue());
               continue;
            }

            var2 = new BlockParsedResult(new DecodedInformation(this.current.position, this.buffer.toString()), true);
         } else {
            CurrentParsingState var3;
            if(this.isAlphaOr646ToNumericLatch(this.current.position)) {
               var3 = this.current;
               var3.position += 3;
               this.current.setNumeric();
            } else if(this.isAlphaTo646ToAlphaLatch(this.current.position)) {
               if(this.current.position + 5 < this.information.size) {
                  var3 = this.current;
                  var3.position += 5;
               } else {
                  this.current.position = this.information.size;
               }

               this.current.setAlpha();
            }

            var2 = new BlockParsedResult(false);
         }

         return var2;
      }
   }

   private BlockParsedResult parseNumericBlock() {
      while(true) {
         BlockParsedResult var3;
         if(this.isStillNumeric(this.current.position)) {
            DecodedNumeric var1 = this.decodeNumeric(this.current.position);
            this.current.position = var1.getNewPosition();
            if(var1.isFirstDigitFNC1()) {
               DecodedInformation var2;
               if(var1.isSecondDigitFNC1()) {
                  var2 = new DecodedInformation(this.current.position, this.buffer.toString());
               } else {
                  var2 = new DecodedInformation(this.current.position, this.buffer.toString(), var1.getSecondDigit());
               }

               var3 = new BlockParsedResult(var2, true);
            } else {
               this.buffer.append(var1.getFirstDigit());
               if(!var1.isSecondDigitFNC1()) {
                  this.buffer.append(var1.getSecondDigit());
                  continue;
               }

               var3 = new BlockParsedResult(new DecodedInformation(this.current.position, this.buffer.toString()), true);
            }
         } else {
            if(this.isNumericToAlphaNumericLatch(this.current.position)) {
               this.current.setAlpha();
               CurrentParsingState var4 = this.current;
               var4.position += 4;
            }

            var3 = new BlockParsedResult(false);
         }

         return var3;
      }
   }

   String decodeAllCodes(StringBuffer var1, int var2) throws NotFoundException {
      String var3 = null;

      while(true) {
         DecodedInformation var4 = this.decodeGeneralPurposeField(var2, var3);
         var1.append(FieldParser.parseFieldsInGeneralPurpose(var4.getNewString()));
         if(var4.isRemaining()) {
            var3 = String.valueOf(var4.getRemainingValue());
         } else {
            var3 = null;
         }

         if(var2 == var4.getNewPosition()) {
            return var1.toString();
         }

         var2 = var4.getNewPosition();
      }
   }

   DecodedInformation decodeGeneralPurposeField(int var1, String var2) {
      this.buffer.setLength(0);
      if(var2 != null) {
         this.buffer.append(var2);
      }

      this.current.position = var1;
      DecodedInformation var3 = this.parseBlocks();
      if(var3 != null && var3.isRemaining()) {
         var3 = new DecodedInformation(this.current.position, this.buffer.toString(), var3.getRemainingValue());
      } else {
         var3 = new DecodedInformation(this.current.position, this.buffer.toString());
      }

      return var3;
   }

   int extractNumericValueFromBitArray(int var1, int var2) {
      return extractNumericValueFromBitArray(this.information, var1, var2);
   }
}
