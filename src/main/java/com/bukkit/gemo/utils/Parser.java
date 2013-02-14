package com.bukkit.gemo.utils;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBBlockType;
import org.bukkit.util.Vector;

public abstract class Parser {

   public static boolean isIntegerOrEmpty(String line) {
      if(line.length() < 1) {
         return true;
      } else {
         try {
            Integer.valueOf(line);
            return true;
         } catch (Exception var2) {
            return false;
         }
      }
   }

   public static boolean isBoolean(String line) {
      try {
         Boolean.valueOf(line);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean getBoolean(String line, boolean defaultValue) {
      if(!isBoolean(line)) {
         return defaultValue;
      } else {
         try {
            return Boolean.valueOf(line).booleanValue();
         } catch (Exception var3) {
            return defaultValue;
         }
      }
   }

   public static boolean isInteger(String line) {
      try {
         Integer.valueOf(line);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static int getInteger(String line, int defaultValue) {
      if(!isInteger(line)) {
         return defaultValue;
      } else {
         try {
            int e = Integer.valueOf(line).intValue();
            return e;
         } catch (Exception var3) {
            return defaultValue;
         }
      }
   }

   public static int getIntegerFromOffsetLine(String line, int defaultValue) {
      String[] split = line.split("=");
      return getInteger(split[0], defaultValue);
   }

   public static boolean isDouble(String line) {
      try {
         Double.valueOf(line);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static double getDouble(String line) {
      if(!isDouble(line)) {
         return 0.0D;
      } else {
         try {
            double e = Double.valueOf(line).doubleValue();
            return e;
         } catch (Exception var3) {
            return 0.0D;
         }
      }
   }

   public static boolean isFloat(String line) {
      try {
         Float.valueOf(line);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static float getFloat(String line) {
      if(!isFloat(line)) {
         return 0.0F;
      } else {
         try {
            float e = Float.valueOf(line).floatValue();
            return e;
         } catch (Exception var2) {
            return 0.0F;
         }
      }
   }

   public static boolean isString(String line, String otherString) {
      return line.equalsIgnoreCase(otherString);
   }

   public static boolean isStringOrEmpty(String line, String otherString) {
      return line.equalsIgnoreCase(otherString) || line.length() < 1;
   }

   public static boolean isBlock(String line) {
      if(line == null) {
         return false;
      } else {
         String[] split = line.split(":");

         short Data;
         try {
            if(split.length > 2) {
               return false;
            } else {
               int e = Integer.valueOf(split[0]).intValue();
               Data = 0;
               if(split.length == 2) {
                  Data = Short.valueOf(split[1]).shortValue();
               }

               return BlockUtils.isValidItemID(e, Data);
            }
         } catch (Exception var4) {
            Data = 0;
            if(split.length == 2) {
               Data = Short.valueOf(split[1]).shortValue();
            }

            return BlockUtils.isValidItemID(split[0], Data);
         }
      }
   }

   public static FBBlockType getBlock(String line) {
      if(!isBlock(line)) {
         return null;
      } else if(line == null) {
         return null;
      } else {
         String[] split = line.split(":");

         try {
            if(split.length > 2) {
               return null;
            } else {
               FBBlockType e = new FBBlockType(Integer.valueOf(split[0]).intValue());
               if(split.length == 2) {
                  e.setItemData(Short.valueOf(split[1]).shortValue());
               }

               return e;
            }
         } catch (Exception var4) {
            FBBlockType result = new FBBlockType(split[0]);
            if(split.length == 2) {
               result.setItemData(Short.valueOf(split[1]).shortValue());
            }

            return result;
         }
      }
   }

   public static boolean isVector(String line) {
      String[] split = line.split(":");
      return split.length == 3?isInteger(split[0]) && isInteger(split[1]) && isInteger(split[2]):false;
   }

   public static Vector getVector(String line) {
      if(!isVector(line)) {
         return new Vector(0, 0, 0);
      } else {
         String[] split = line.split(":");
         return new Vector(getInteger(split[0], 0), getInteger(split[1], 0), getInteger(split[2], 0));
      }
   }

   public static Vector getVectorFromOffsetLine(String line) {
      String[] split = line.split("=");
      return split.length == 2?getVector(split[1]):new Vector(0, 0, 0);
   }

   public static boolean isIntegerWithOffset(String line) {
      String[] split = line.split("=");
      return split.length == 1?isInteger(split[0]):(split.length == 2?isInteger(split[0]) && isVector(split[1]):false);
   }
}
