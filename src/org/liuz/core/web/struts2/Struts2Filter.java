 package org.liuz.core.web.struts2;

 import java.io.IOException;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.StrutsStatics;
 import org.apache.struts2.dispatcher.Dispatcher;
 import org.apache.struts2.dispatcher.mapper.ActionMapping;
 import org.apache.struts2.dispatcher.ng.ExecuteOperations;
 import org.apache.struts2.dispatcher.ng.InitOperations;
 import org.apache.struts2.dispatcher.ng.PrepareOperations;
 import org.apache.struts2.dispatcher.ng.filter.FilterHostConfig;

 public class Struts2Filter
   implements StrutsStatics, Filter
 {
   private PrepareOperations prepare;
   private ExecuteOperations execute;

   public void init(FilterConfig filterConfig)
     throws ServletException
   {
     InitOperations init = new InitOperations();
     try {
       FilterHostConfig config = new FilterHostConfig(filterConfig);
       init.initLogging(config);
       Dispatcher dispatcher = init.initDispatcher(config);
       init.initStaticContentLoader(config, dispatcher);

       this.prepare = new PrepareOperations(filterConfig.getServletContext(), dispatcher);
       this.execute = new ExecuteOperations(filterConfig.getServletContext(), dispatcher);
     } finally {
       init.cleanup();
     }
   }

   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
     throws IOException, ServletException
   {
     HttpServletRequest request = (HttpServletRequest)req;
     HttpServletResponse response = (HttpServletResponse)res;

     label132:
     try { this.prepare.setEncodingAndLocale(request, response);
       this.prepare.createActionContext(request, response);
       this.prepare.assignDispatcherToThread();
       request = this.prepare.wrapRequest(request);
       ActionMapping mapping = this.prepare.findActionMapping(request, response);
       if (mapping == null) {
         boolean handled = this.execute.executeStaticResourceRequest(request, response);
         if (!handled) {
           chain.doFilter(request, response); break label132;
         }
       } else {
         this.execute.executeAction(request, response, mapping);
       }
     } finally {
       this.prepare.cleanupRequest(request);
     }
   }

   public void destroy() {
     this.prepare.cleanupDispatcher();
   }
 }
