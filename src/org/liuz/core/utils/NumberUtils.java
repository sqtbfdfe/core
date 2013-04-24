 package org.liuz.core.utils;

 import java.math.BigDecimal;

 public class NumberUtils
 {
   public static double add(double v1, double v2)
   {
     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.add(b2).doubleValue();
   }

   public static double subtract(double v1, double v2)
   {
     BigDecimal b1 = new BigDecimal(Double.toString(v1));
     BigDecimal b2 = new BigDecimal(Double.toString(v2));
     return b1.subtract(b2).doubleValue();
   }

   public static double multiply(double v1, double v2)
   {
     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.multiply(b2).doubleValue();
   }

   public static double multiply(double v1, double v2, int scale)
   {
     if (scale < 0) {
       throw new IllegalArgumentException("The scale must be a positive integer or zero");
     }
     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.multiply(b2).setScale(scale, 4).doubleValue();
   }

   public static double divide(double v1, double v2)
   {
     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.divide(b2).doubleValue();
   }

   public static double divide(double v1, double v2, int scale)
   {
     if (scale < 0) {
       throw new IllegalArgumentException("The scale must be a positive integer or zero");
     }

     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.divide(b2, scale, 4).doubleValue();
   }

   public static double round(double v, int scale)
   {
     if (scale < 0) {
       throw new IllegalArgumentException("The scale must be a positive integer or zero");
     }
     BigDecimal b = new BigDecimal(v);
     return b.setScale(scale, 4).doubleValue();
   }
 }
