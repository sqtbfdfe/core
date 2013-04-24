 package org.liuz.core.orm.hibernate;

 import org.hibernate.cfg.Configuration;
 import org.hibernate.cfg.Settings;
 import org.hibernate.engine.SessionFactoryImplementor;
 import org.hibernate.tool.hbm2ddl.SchemaExport;
 import org.springframework.context.ApplicationContext;
 import org.springframework.context.support.ClassPathXmlApplicationContext;
 import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

 public class EntityToDatabase
 {
   public static void exportToDataBase()
   {
     exportToDataBase("applicationContext.xml", "sessionFactory");
   }

   public static void exportToDataBase(String sessionFactory)
   {
     exportToDataBase("applicationContext.xml", sessionFactory);
   }

   public static void exportToDataBase(String contextName, String sessionFactoryName)
   {
     try
     {
       ApplicationContext context = new ClassPathXmlApplicationContext(contextName);
       LocalSessionFactoryBean localSessionFactory = (LocalSessionFactoryBean)context
         .getBean("&" + sessionFactoryName);
       SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor)context
         .getBean(sessionFactoryName);
       Settings settings = sessionFactory.getSettings();
       Configuration config = localSessionFactory.getConfiguration();
       SchemaExport export = new SchemaExport(config, settings);
       export.create(true, true);

       System.out.println("OK");
     }
     catch (Exception e) {
       e.printStackTrace();
       System.err.println("error");
     }
   }

   public static void main(String[] args) {
     exportToDataBase("applicationContext.xml", "sessionFactory");
   }
 }
