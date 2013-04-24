 package org.liuz.core.orm;

 import java.io.Serializable;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.MappedSuperclass;

 @MappedSuperclass
 public class BaseIncEntity
   implements Serializable
 {

   @Id
   @GeneratedValue
   private Long id;

   public Long getId()
   {
     return this.id;
   }

   public void setId(Long id) {
     this.id = id;
   }
 }
