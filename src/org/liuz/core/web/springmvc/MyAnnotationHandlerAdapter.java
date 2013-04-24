 package org.liuz.core.web.springmvc;

 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;

 import org.springframework.ui.ModelMap;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
 import org.springframework.web.servlet.view.RedirectView;

 public class MyAnnotationHandlerAdapter extends AnnotationMethodHandlerAdapter
 {
   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
     throws Exception
   {
     unpackFlashState(request);

     ModelAndView modelAndView = super.handle(request, response, handler);

     if (modelAndView == null) return null;

     ModelMap modelMap = modelAndView.getModelMap();

     mergeFlashState(request, modelMap);

     setFlashState(request, modelMap);

     boolean isRedirect = isRedirect(modelAndView);

     if (!isRedirect) {
       setParameters(request, modelMap);
     }
     return modelAndView;
   }

   boolean isRedirect(ModelAndView modelAndView)
   {
     boolean isRedirect = false;
     if (((modelAndView.getView() instanceof RedirectView)) || (
       (modelAndView.getViewName() != null) &&
       (modelAndView.getViewName().startsWith("redirect:")))) {
       isRedirect = true;
     }
     return isRedirect;
   }

   void setParameters(HttpServletRequest request, ModelMap modelMap)
   {
     Enumeration parameterNames = request.getParameterNames();
     while (parameterNames.hasMoreElements()) {
       String parameterName = (String)parameterNames.nextElement();
       if (!modelMap.containsKey(parameterName)) {
         String value = request.getParameter(parameterName);
         modelMap.put(parameterName, value);

         if (this.logger.isDebugEnabled())
           this.logger.debug("Adding parameter " + parameterName + ": " + value);
       }
     }
   }

   void unpackFlashState(HttpServletRequest request)
   {
     HttpSession session = request.getSession(false);
     if (session != null) {
       Map flashState = (Map)session.getAttribute("flashState");
       if (flashState != null) {
         request.setAttribute("flashState", flashState);
         session.removeAttribute("flashState");

         Set<String> flashKeys = flashState.keySet();
         for (String flashKey : flashKeys) {
           Object currentRequestAttribute = request.getAttribute(flashKey);
           if (currentRequestAttribute == null)
             request.setAttribute(flashKey, flashState.get(flashKey));
         }
       }
     }
   }

   void mergeFlashState(HttpServletRequest request, ModelMap modelMap)
   {
     Map flashState = (Map)request.getAttribute("flashState");
     if (flashState != null) {
       modelMap.mergeAttributes(flashState);
       request.removeAttribute("flashState");
     }
   }

   void setFlashState(HttpServletRequest request, ModelMap modelMap)
   {
     Set keys = modelMap.keySet();
     Map flashState = null;

     for (Iterator localIterator = keys.iterator(); localIterator.hasNext(); ) { Object object = localIterator.next();
       String key = object.toString();
       if (key.startsWith("flash:")) {
         String realKey = key.substring("flash:".length());
         if (flashState == null) flashState = new HashMap();
         flashState.put(realKey, modelMap.get(key));
       }
     }

     if (flashState != null)
       request.getSession().setAttribute("flashState", flashState);
   }
 }
