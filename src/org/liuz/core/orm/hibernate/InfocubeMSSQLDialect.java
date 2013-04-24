 package org.liuz.core.orm.hibernate;

 import org.hibernate.dialect.SQLServerDialect;

 public class InfocubeMSSQLDialect extends SQLServerDialect
 {
   public InfocubeMSSQLDialect()
   {
     registerColumnType(-5, "bigint");
   }
 }

