 package org.liuz.core.search;

 import java.io.Serializable;
 import org.apache.commons.lang.StringUtils;
 import org.springframework.util.Assert;

 public class Sort
   implements Serializable
 {
   private static final long serialVersionUID = -694409065068247317L;
   protected String property;
   protected String order = "asc";
   public static final String ASC = "asc";
   public static final String DESC = "desc";

   public Sort(String property, String order)
   {
     Assert.hasText(property, "property不能为空");
     Assert.hasText(order, "order不能为空");
     this.property = property;
     if ((!StringUtils.equals("desc", order)) && (!StringUtils.equals("asc", order)))
       throw new IllegalArgumentException("排序方向" + order + "不是合法值");
     this.order = StringUtils.lowerCase(order);
   }

   public Sort(String property)
   {
     this.property = property;
   }

   public static Sort asc(String property)
   {
     return new Sort(property);
   }

   public static Sort desc(String property)
   {
     return new Sort(property, "desc");
   }

   public String getProperty()
   {
     return this.property;
   }

   public void setProperty(String property)
   {
     this.property = property;
   }

   public String getOrder()
   {
     return this.order;
   }

   public void setOrder(String order)
   {
     if ((!StringUtils.equals("desc", order)) && (!StringUtils.equals("asc", order)))
       throw new IllegalArgumentException("排序方向" + order + "不是合法值");
     this.order = StringUtils.lowerCase(order);
   }

   public int hashCode()
   {
     int prime = 31;
     int result = 1;
     result = 31 * result + (this.property == null ? 0 : this.property.hashCode());
     result = 31 * result + (this.order == null ? 0 : this.order.hashCode());
     return result;
   }

   public boolean equals(Object obj)
   {
     if (this == obj)
       return true;
     if (obj == null)
       return false;
     if (getClass() != obj.getClass())
       return false;
     Sort other = (Sort)obj;
     if (this.order == null) {
       if (other.order != null)
         return false;
     } else if (!this.order.equals(other.order))
       return false;
     if (this.property == null) {
       if (other.property != null)
         return false;
     } else if (!this.property.equals(other.property))
       return false;
     return true;
   }

   public String toString()
   {
     StringBuilder sb = new StringBuilder();
     if (this.property == null) {
       sb.append("null");
     } else {
       sb.append("`");
       sb.append(this.property);
       sb.append("`");
     }
     if (this.property == null) {
       sb.append("null");
     } else {
       sb.append("`");
       sb.append(this.order);
       sb.append("`");
     }
     return sb.toString();
   }
 }

