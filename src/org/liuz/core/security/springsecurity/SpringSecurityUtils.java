 package org.liuz.core.security.springsecurity;

 import java.util.Collection;
 import javax.servlet.http.HttpServletRequest;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.GrantedAuthority;
 import org.springframework.security.core.context.SecurityContext;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.User;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.web.authentication.WebAuthenticationDetails;
 import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

 public class SpringSecurityUtils
 {
   public static <T extends User> T getCurrentUser()
   {
     Authentication authentication = getAuthentication();
     if (authentication != null) {
       Object principal = authentication.getPrincipal();
       if ((principal instanceof User)) {
         return (T) principal;
       }
     }
     return null;
   }

   public static String getCurrentUserName()
   {
     Authentication authentication = getAuthentication();
     if ((authentication != null) && (authentication.getPrincipal() != null)) {
       return authentication.getName();
     }
     return "";
   }

   public static String getCurrentUserIp()
   {
     Authentication authentication = getAuthentication();
     if (authentication != null) {
       Object details = authentication.getDetails();
       if ((details instanceof WebAuthenticationDetails)) {
         WebAuthenticationDetails webDetails = (WebAuthenticationDetails)details;
         return webDetails.getRemoteAddress();
       }
     }

     return "";
   }

   public static boolean hasAnyRole(String[] roles)
   {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     Collection<? extends GrantedAuthority> granteds = authentication.getAuthorities();
     String[] arrayOfString = roles; int j = roles.length; for (int i = 0; i < j; i++) { String role = arrayOfString[i];
       for (GrantedAuthority authority : granteds) {
         if (role.equals(authority.getAuthority())) {
           return true;
         }
       }
     }
     return false;
   }

   public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request)
   {
     PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,
       userDetails.getPassword(), userDetails.getAuthorities());

     authentication.setDetails(new WebAuthenticationDetails(request));

     SecurityContextHolder.getContext().setAuthentication(authentication);
   }

   private static Authentication getAuthentication()
   {
     SecurityContext context = SecurityContextHolder.getContext();
     if (context != null) {
       return context.getAuthentication();
     }
     return null;
   }
 }

