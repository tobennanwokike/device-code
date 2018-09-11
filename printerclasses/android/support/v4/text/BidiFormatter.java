package android.support.v4.text;

import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.text.TextUtilsCompat;
import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
   private static final int DEFAULT_FLAGS = 2;
   private static final BidiFormatter DEFAULT_LTR_INSTANCE;
   private static final BidiFormatter DEFAULT_RTL_INSTANCE;
   private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
   private static final int DIR_LTR = -1;
   private static final int DIR_RTL = 1;
   private static final int DIR_UNKNOWN = 0;
   private static final String EMPTY_STRING = "";
   private static final int FLAG_STEREO_RESET = 2;
   private static final char LRE = '\u202a';
   private static final char LRM = '\u200e';
   private static final String LRM_STRING;
   private static final char PDF = '\u202c';
   private static final char RLE = '\u202b';
   private static final char RLM = '\u200f';
   private static final String RLM_STRING;
   private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
   private final int mFlags;
   private final boolean mIsRtlContext;

   static {
      DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
      LRM_STRING = Character.toString('\u200e');
      RLM_STRING = Character.toString('\u200f');
      DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
      DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
   }

   private BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3) {
      this.mIsRtlContext = var1;
      this.mFlags = var2;
      this.mDefaultTextDirectionHeuristicCompat = var3;
   }

   // $FF: synthetic method
   BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3, Object var4) {
      this(var1, var2, var3);
   }

   private static int getEntryDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getEntryDir();
   }

   private static int getExitDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getExitDir();
   }

   public static BidiFormatter getInstance() {
      return (new BidiFormatter.Builder()).build();
   }

   public static BidiFormatter getInstance(Locale var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   public static BidiFormatter getInstance(boolean var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   private static boolean isRtlLocale(Locale var0) {
      boolean var1 = true;
      if(TextUtilsCompat.getLayoutDirectionFromLocale(var0) != 1) {
         var1 = false;
      }

      return var1;
   }

   private String markAfter(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      String var4;
      if(this.mIsRtlContext || !var3 && getExitDir(var1) != 1) {
         if(!this.mIsRtlContext || var3 && getExitDir(var1) != -1) {
            var4 = "";
         } else {
            var4 = RLM_STRING;
         }
      } else {
         var4 = LRM_STRING;
      }

      return var4;
   }

   private String markBefore(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      String var4;
      if(this.mIsRtlContext || !var3 && getEntryDir(var1) != 1) {
         if(!this.mIsRtlContext || var3 && getEntryDir(var1) != -1) {
            var4 = "";
         } else {
            var4 = RLM_STRING;
         }
      } else {
         var4 = LRM_STRING;
      }

      return var4;
   }

   public boolean getStereoReset() {
      boolean var1;
      if((this.mFlags & 2) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isRtl(CharSequence var1) {
      return this.mDefaultTextDirectionHeuristicCompat.isRtl((CharSequence)var1, 0, var1.length());
   }

   public boolean isRtl(String var1) {
      return this.isRtl((CharSequence)var1);
   }

   public boolean isRtlContext() {
      return this.mIsRtlContext;
   }

   public CharSequence unicodeWrap(CharSequence var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2, boolean var3) {
      SpannableStringBuilder var7;
      if(var1 == null) {
         var7 = null;
      } else {
         boolean var5 = var2.isRtl((CharSequence)var1, 0, var1.length());
         SpannableStringBuilder var6 = new SpannableStringBuilder();
         if(this.getStereoReset() && var3) {
            if(var5) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var6.append(this.markBefore(var1, var2));
         }

         if(var5 != this.mIsRtlContext) {
            char var4;
            if(var5) {
               var4 = 8235;
            } else {
               var4 = 8234;
            }

            var6.append(var4);
            var6.append(var1);
            var6.append('\u202c');
         } else {
            var6.append(var1);
         }

         var7 = var6;
         if(var3) {
            if(var5) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var6.append(this.markAfter(var1, var2));
            var7 = var6;
         }
      }

      return var7;
   }

   public CharSequence unicodeWrap(CharSequence var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public String unicodeWrap(String var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2, boolean var3) {
      if(var1 == null) {
         var1 = null;
      } else {
         var1 = this.unicodeWrap((CharSequence)var1, var2, var3).toString();
      }

      return var1;
   }

   public String unicodeWrap(String var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public static final class Builder {
      private int mFlags;
      private boolean mIsRtlContext;
      private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

      public Builder() {
         this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
      }

      public Builder(Locale var1) {
         this.initialize(BidiFormatter.isRtlLocale(var1));
      }

      public Builder(boolean var1) {
         this.initialize(var1);
      }

      private static BidiFormatter getDefaultInstanceFromContext(boolean var0) {
         BidiFormatter var1;
         if(var0) {
            var1 = BidiFormatter.DEFAULT_RTL_INSTANCE;
         } else {
            var1 = BidiFormatter.DEFAULT_LTR_INSTANCE;
         }

         return var1;
      }

      private void initialize(boolean var1) {
         this.mIsRtlContext = var1;
         this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
         this.mFlags = 2;
      }

      public BidiFormatter build() {
         BidiFormatter var1;
         if(this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
            var1 = getDefaultInstanceFromContext(this.mIsRtlContext);
         } else {
            var1 = new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
         }

         return var1;
      }

      public BidiFormatter.Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat var1) {
         this.mTextDirectionHeuristicCompat = var1;
         return this;
      }

      public BidiFormatter.Builder stereoReset(boolean var1) {
         if(var1) {
            this.mFlags |= 2;
         } else {
            this.mFlags &= -3;
         }

         return this;
      }
   }

   private static class DirectionalityEstimator {
      private static final byte[] DIR_TYPE_CACHE = new byte[1792];
      private static final int DIR_TYPE_CACHE_SIZE = 1792;
      private int charIndex;
      private final boolean isHtml;
      private char lastChar;
      private final int length;
      private final CharSequence text;

      static {
         for(int var0 = 0; var0 < 1792; ++var0) {
            DIR_TYPE_CACHE[var0] = Character.getDirectionality(var0);
         }

      }

      DirectionalityEstimator(CharSequence var1, boolean var2) {
         this.text = var1;
         this.isHtml = var2;
         this.length = var1.length();
      }

      private static byte getCachedDirectionality(char var0) {
         byte var1;
         if(var0 < 1792) {
            var1 = DIR_TYPE_CACHE[var0];
         } else {
            var1 = Character.getDirectionality(var0);
         }

         return var1;
      }

      private byte skipEntityBackward() {
         int var2 = this.charIndex;

         byte var1;
         while(true) {
            if(this.charIndex > 0) {
               CharSequence var4 = this.text;
               int var3 = this.charIndex - 1;
               this.charIndex = var3;
               this.lastChar = var4.charAt(var3);
               if(this.lastChar == 38) {
                  var1 = 12;
                  break;
               }

               if(this.lastChar != 59) {
                  continue;
               }
            }

            this.charIndex = var2;
            this.lastChar = 59;
            var1 = 13;
            break;
         }

         return var1;
      }

      private byte skipEntityForward() {
         while(true) {
            if(this.charIndex < this.length) {
               CharSequence var3 = this.text;
               int var2 = this.charIndex;
               this.charIndex = var2 + 1;
               char var1 = var3.charAt(var2);
               this.lastChar = var1;
               if(var1 != 59) {
                  continue;
               }
            }

            return (byte)12;
         }
      }

      private byte skipTagBackward() {
         int var3 = this.charIndex;

         byte var1;
         label30:
         while(true) {
            if(this.charIndex > 0) {
               CharSequence var6 = this.text;
               int var4 = this.charIndex - 1;
               this.charIndex = var4;
               this.lastChar = var6.charAt(var4);
               if(this.lastChar == 60) {
                  var1 = 12;
                  break;
               }

               if(this.lastChar != 62) {
                  if(this.lastChar != 34 && this.lastChar != 39) {
                     continue;
                  }

                  char var5 = this.lastChar;

                  while(true) {
                     if(this.charIndex <= 0) {
                        continue label30;
                     }

                     var6 = this.text;
                     var4 = this.charIndex - 1;
                     this.charIndex = var4;
                     char var2 = var6.charAt(var4);
                     this.lastChar = var2;
                     if(var2 == var5) {
                        continue label30;
                     }
                  }
               }
            }

            this.charIndex = var3;
            this.lastChar = 62;
            var1 = 13;
            break;
         }

         return var1;
      }

      private byte skipTagForward() {
         int var3 = this.charIndex;

         byte var1;
         label29:
         while(true) {
            if(this.charIndex < this.length) {
               CharSequence var6 = this.text;
               int var4 = this.charIndex;
               this.charIndex = var4 + 1;
               this.lastChar = var6.charAt(var4);
               if(this.lastChar == 62) {
                  var1 = 12;
                  break;
               }

               if(this.lastChar != 34 && this.lastChar != 39) {
                  continue;
               }

               char var7 = this.lastChar;

               while(true) {
                  if(this.charIndex >= this.length) {
                     continue label29;
                  }

                  var6 = this.text;
                  int var5 = this.charIndex;
                  this.charIndex = var5 + 1;
                  char var2 = var6.charAt(var5);
                  this.lastChar = var2;
                  if(var2 == var7) {
                     continue label29;
                  }
               }
            }

            this.charIndex = var3;
            this.lastChar = 60;
            var1 = 13;
            break;
         }

         return var1;
      }

      byte dirTypeBackward() {
         this.lastChar = this.text.charAt(this.charIndex - 1);
         byte var1;
         if(Character.isLowSurrogate(this.lastChar)) {
            int var3 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(var3);
            var1 = Character.getDirectionality(var3);
         } else {
            --this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            var1 = var2;
            if(this.isHtml) {
               if(this.lastChar == 62) {
                  var1 = this.skipTagBackward();
               } else {
                  var1 = var2;
                  if(this.lastChar == 59) {
                     var1 = this.skipEntityBackward();
                  }
               }
            }
         }

         return var1;
      }

      byte dirTypeForward() {
         this.lastChar = this.text.charAt(this.charIndex);
         byte var1;
         if(Character.isHighSurrogate(this.lastChar)) {
            int var3 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(var3);
            var1 = Character.getDirectionality(var3);
         } else {
            ++this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            var1 = var2;
            if(this.isHtml) {
               if(this.lastChar == 60) {
                  var1 = this.skipTagForward();
               } else {
                  var1 = var2;
                  if(this.lastChar == 38) {
                     var1 = this.skipEntityForward();
                  }
               }
            }
         }

         return var1;
      }

      int getEntryDir() {
         this.charIndex = 0;
         int var1 = 0;
         byte var3 = 0;
         int var4 = 0;

         byte var2;
         while(true) {
            if(this.charIndex >= this.length || var4 != 0) {
               if(var4 == 0) {
                  var2 = 0;
               } else {
                  var2 = var3;
                  if(var3 == 0) {
                     while(this.charIndex > 0) {
                        switch(this.dirTypeBackward()) {
                        case 14:
                        case 15:
                           if(var4 == var1) {
                              var2 = -1;
                              return var2;
                           }

                           --var1;
                           break;
                        case 16:
                        case 17:
                           if(var4 == var1) {
                              var2 = 1;
                              return var2;
                           }

                           --var1;
                           break;
                        case 18:
                           ++var1;
                        }
                     }

                     var2 = 0;
                  }
               }
               break;
            }

            switch(this.dirTypeForward()) {
            case 0:
               if(var1 == 0) {
                  var2 = -1;
                  return var2;
               }

               var4 = var1;
               break;
            case 1:
            case 2:
               if(var1 == 0) {
                  var2 = 1;
                  return var2;
               }

               var4 = var1;
               break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
               var4 = var1;
            case 9:
               break;
            case 14:
            case 15:
               ++var1;
               var3 = -1;
               break;
            case 16:
            case 17:
               ++var1;
               var3 = 1;
               break;
            case 18:
               --var1;
               var3 = 0;
            }
         }

         return var2;
      }

      int getExitDir() {
         byte var4 = -1;
         this.charIndex = this.length;
         int var1 = 0;
         int var2 = 0;

         byte var3;
         while(this.charIndex > 0) {
            switch(this.dirTypeBackward()) {
            case 0:
               if(var1 == 0) {
                  var3 = var4;
                  return var3;
               }

               if(var2 == 0) {
                  var2 = var1;
               }
               break;
            case 1:
            case 2:
               if(var1 == 0) {
                  var3 = 1;
                  return var3;
               }

               if(var2 == 0) {
                  var2 = var1;
               }
               break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
               if(var2 == 0) {
                  var2 = var1;
               }
            case 9:
               break;
            case 14:
            case 15:
               var3 = var4;
               if(var2 == var1) {
                  return var3;
               }

               --var1;
               break;
            case 16:
            case 17:
               if(var2 == var1) {
                  var3 = 1;
                  return var3;
               }

               --var1;
               break;
            case 18:
               ++var1;
            }
         }

         var3 = 0;
         return var3;
      }
   }
}
