 package org.liuz.core.web.struts2;

 public class FormBean
 {
   private Long id;
   private Long[] radioGroup;
   private String queryName;
   private String queryValue;

   public Long getId()
   {
     return this.id;
   }

   public void setId(Long id) {
     this.id = id;
   }

   public Long[] getRadioGroup() {
     return this.radioGroup;
   }

   public void setRadioGroup(Long[] radioGroup) {
     this.radioGroup = radioGroup;
   }

   public String getQueryName() {
     return this.queryName;
   }

   public void setQueryName(String queryName) {
     this.queryName = queryName;
   }

   public String getQueryValue() {
     return this.queryValue;
   }

   public void setQueryValue(String queryValue) {
     this.queryValue = queryValue;
   }
 }
