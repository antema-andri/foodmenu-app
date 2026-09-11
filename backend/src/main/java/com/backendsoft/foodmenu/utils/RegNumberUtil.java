package com.backendsoft.foodmenu.utils;

public class RegNumberUtil {
   public static String generate(String prefix, int lastNumber) {
      String result = "";
      if (lastNumber > 0 && lastNumber < 10) {
         result = prefix + "00" + lastNumber;
      }

      if (lastNumber >= 10 && lastNumber < 100) {
         result = prefix + "0" + lastNumber;
      }

      if (lastNumber >= 100) {
         result = prefix + lastNumber;
      }

      return result;
   }
}
