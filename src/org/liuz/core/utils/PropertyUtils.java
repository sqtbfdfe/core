 package org.liuz.core.utils;

 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.util.Properties;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.core.io.DefaultResourceLoader;
 import org.springframework.core.io.Resource;
 import org.springframework.core.io.ResourceLoader;
 import org.springframework.util.DefaultPropertiesPersister;
 import org.springframework.util.PropertiesPersister;

 public class PropertyUtils
 {
   private static final String DEFAULT_ENCODING = "UTF-8";
   private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

   private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
   private static ResourceLoader resourceLoader = new DefaultResourceLoader();

   public static Properties loadProperties(String[] locations)
     throws IOException
   {
     Properties props = new Properties();

     String[] arrayOfString = locations; int j = locations.length; for (int i = 0; i < j; i++) { String location = arrayOfString[i];

       logger.debug("Loading properties file from classpath:" + location);

       InputStream is = null;
       try {
         Resource resource = resourceLoader.getResource(location);
         is = resource.getInputStream();
         propertiesPersister.load(props, new InputStreamReader(is, "UTF-8"));
       } catch (IOException ex) {
         logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
       } finally {
         if (is != null) {
           is.close();
         }
       }
     }
     return props;
   }
 }
