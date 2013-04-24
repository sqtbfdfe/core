 package org.liuz.core.security.springsecurity.cxf;

 import org.apache.cxf.interceptor.Fault;
 import org.apache.cxf.message.Message;
 import org.apache.cxf.phase.AbstractPhaseInterceptor;
 import org.springframework.security.core.context.SecurityContextHolder;

 public class SpringSecurityOutInterceptor extends AbstractPhaseInterceptor<Message>
 {
   public SpringSecurityOutInterceptor()
   {
     super("marshal");
   }

   public void handleMessage(Message message) throws Fault {
     SecurityContextHolder.clearContext();
   }
 }
