 package org.liuz.core.orm;

 import java.io.Serializable;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.MappedSuperclass;
 import org.hibernate.annotations.GenericGenerator;

 @MappedSuperclass
 public class BaseUidEntity
   implements Serializable
 {

   @Id
   @GenericGenerator(name="uu_id", strategy="uuid")
   @GeneratedValue(generator="uu_id")
   private String id;

   public String getId()
   {
     return this.id;
   }

   public void setId(String id) {
     this.id = id;
   }
 }




