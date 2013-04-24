 package org.liuz.core.security.springsecurity.cxf;

 import java.util.Iterator;
 import java.util.Vector;
 import javax.servlet.http.HttpServletRequest;
 import org.apache.cxf.interceptor.Fault;
 import org.apache.cxf.message.Message;
 import org.apache.cxf.phase.AbstractPhaseInterceptor;
 import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
 import org.apache.ws.security.WSSecurityEngineResult;
 import org.apache.ws.security.WSUsernameTokenPrincipal;
 import org.apache.ws.security.handler.WSHandlerResult;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.liuz.core.security.springsecurity.SpringSecurityUtils;

 public class SpringSecurityInInterceptor extends AbstractPhaseInterceptor<Message>
 {
   private UserDetailsService userDetailsService;

   public void setUserDetailsService(UserDetailsService userDetailsService)
   {
     this.userDetailsService = userDetailsService;
   }

   public SpringSecurityInInterceptor() {
     super("pre-protocol");
     addAfter(WSS4JInInterceptor.class.getName());
   }

   public void handleMessage(Message message) throws Fault {
     String userName = getUserNameFromWSS4JResult(message);
     HttpServletRequest request = (HttpServletRequest)message.get("HTTP.REQUEST");

     UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
     SpringSecurityUtils.saveUserDetailsToContext(userDetails, request);
   }

   private String getUserNameFromWSS4JResult(Message message)
   {
     Vector results = (Vector)message.getContextualProperty("RECV_RESULTS");
     if ((results != null) && (!results.isEmpty()))
     {
       Iterator localIterator2;
       for (Iterator localIterator1 = results.iterator(); localIterator1.hasNext();
         localIterator2.hasNext())
       {
         WSHandlerResult result = (WSHandlerResult)localIterator1.next();
         localIterator2 = result.getResults().iterator();
         WSSecurityEngineResult securityResult = (WSSecurityEngineResult)localIterator2.next();
         int action = ((Integer)securityResult.get("action")).intValue();
         if ((action & 0x1) > 0) {
           WSUsernameTokenPrincipal token =
             (WSUsernameTokenPrincipal)securityResult
             .get("principal");
           return token.getName();
         }
       }
     }

     return null;
   }
 }
