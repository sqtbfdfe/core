 package org.liuz.core.orm.hibernate;

 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import org.apache.commons.lang.StringUtils;

 public class OpenSessionInViewFilter extends org.springframework.orm.hibernate3.support.OpenSessionInViewFilter
 {
   private static final String EXCLUDE_SUFFIXS_NAME = "excludeSuffixs";
   private static final String[] DEFAULT_EXCLUDE_SUFFIXS = { ".js", ".css", ".jpg", ".gif" };
   private static final String[] DEFAULT_INCLUDE_SUFFIXS = { ".action", ".do", ".shtml" };

   private static String[] excludeSuffixs = null;

   protected boolean shouldNotFilter(HttpServletRequest request)
     throws ServletException
   {
     String path = request.getServletPath();

     for (String suffix : DEFAULT_INCLUDE_SUFFIXS) {
       if (path.endsWith(suffix)) {
         return false;
       }
     }
     for (String suffix : excludeSuffixs) {
       if (path.endsWith(suffix)) {
         return true;
       }
     }
     return false;
   }

   protected void initFilterBean()
     throws ServletException
   {
     String excludeSuffixStr = getFilterConfig().getInitParameter("excludeSuffixs");

     if (StringUtils.isNotBlank(excludeSuffixStr)) {
       excludeSuffixs = excludeSuffixStr.split(",");

       for (int i = 0; i < excludeSuffixs.length; i++)
         excludeSuffixs[i] = ("." + excludeSuffixs[i]);
     }
     else {
       excludeSuffixs = DEFAULT_EXCLUDE_SUFFIXS;
     }
   }
 }

