 package org.liuz.core.utils;

 import java.util.Collection;
 import java.util.Map;
 import org.springframework.util.StringUtils;

 public class ObjectUtils
 {
   public static boolean isNullOrEmptyString(Object o)
   {
     if (o == null)
       return true;
     if ((o instanceof String)) {
       return !StringUtils.hasLength((String)o);
     }
     return false;
   }

   public static boolean isEmpty(Object o)
     throws IllegalArgumentException
   {
     if (o == null) return true;

     if ((o instanceof String))
       return !StringUtils.hasLength((String)o);
     if ((o instanceof Collection)) {
       if (((Collection)o).isEmpty())
         return true;
     }
     else if (o.getClass().isArray()) {
       if (((Object[])o).length == 0)
         return true;
     }
     else if ((o instanceof Map)) {
       if (((Map)o).isEmpty())
         return true;
     }
     else {
       throw new IllegalArgumentException("Illegal argument type,must be : Map,Collection,Array,String. but was:" + o.getClass());
     }

     return false;
   }

   public static boolean isNotEmpty(Object c)
     throws IllegalArgumentException
   {
     return !isEmpty(c);
   }
 }
