 package org.liuz.core.service;

 public class ServiceException extends RuntimeException
 {
   private static final long serialVersionUID = 1650414111978589240L;

   public ServiceException()
   {
   }

   public ServiceException(String message)
   {
     super(message);
   }

   public ServiceException(Throwable cause) {
     super(cause);
   }

   public ServiceException(String message, Throwable cause) {
     super(message, cause);
   }
 }

