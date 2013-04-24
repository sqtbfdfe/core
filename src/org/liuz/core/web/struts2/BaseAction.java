 package org.liuz.core.web.struts2;

 import com.opensymphony.xwork2.ActionSupport;

 public class BaseAction extends ActionSupport
 {
   private String resultUrl = null;

   public String redirect(String resultUrl) {
     this.resultUrl = resultUrl;
     if (resultUrl.contains(".jsp")) {
       return "redirect-jsp";
     }
     return "redirect-action";
   }

   public String dispatcher(String resultUrl)
   {
     this.resultUrl = resultUrl;
     if (resultUrl.contains(".jsp")) {
       return "redirect-jsp";
     }
     return "redirect-action";
   }

   public String getResultUrl()
   {
     return this.resultUrl;
   }
 }
