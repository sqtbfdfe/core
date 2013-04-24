 package org.liuz.core.orm.hibernate;

 import java.lang.reflect.Field;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedHashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import javax.annotation.Resource;
 import javax.persistence.ManyToOne;
 import javax.persistence.OneToOne;
 import org.apache.commons.lang.StringEscapeUtils;
 import org.apache.commons.lang.StringUtils;
 import org.hibernate.Criteria;
 import org.hibernate.Hibernate;
 import org.hibernate.Query;
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 import org.hibernate.criterion.CriteriaSpecification;
 import org.hibernate.criterion.Criterion;
 import org.hibernate.criterion.Order;
 import org.hibernate.criterion.Projection;
 import org.hibernate.criterion.Projections;
 import org.hibernate.engine.SessionFactoryImplementor;
 import org.hibernate.hql.ast.QueryTranslatorImpl;
 import org.hibernate.impl.CriteriaImpl;
 import org.hibernate.transform.ResultTransformer;

 public class HibernateGenericDao
 {
   protected Class entityClass;

   @Resource(name="sessionFactory")
   protected SessionFactory sessionFactory;

   public Session getSession()
   {
     return this.sessionFactory.getCurrentSession();
   }

   public SessionFactory getSessionFactory()
   {
     return this.sessionFactory;
   }

   public void setEntityClass(Class entityClass) {
     this.entityClass = entityClass;
   }

   public void flush()
   {
     getSession().flush();
   }

   public void clear()
   {
     getSession().clear();
   }

   public Query createSqlQuery(String queryString, Object[] values)
   {
     Query queryObject = getSession().createSQLQuery(queryString);
     if (values != null) {
       for (int i = 0; i < values.length; i++) {
         queryObject.setParameter(i, values[i]);
       }
     }
     return queryObject;
   }

   public Query createQuery(String queryString, Object[] values)
   {
     Query queryObject = getSession().createQuery(queryString);
     if (values != null) {
       for (int i = 0; i < values.length; i++) {
         queryObject.setParameter(i, values[i]);
       }
     }
     return queryObject;
   }

   public Criteria createCriteria(Criterion[] criterions)
   {
     Criteria criteria = getSession().createCriteria(this.entityClass);
     Map aliasMap = new HashMap();
     for (Criterion c : criterions) {
       String propertyName = null;
       try {
         propertyName = getFieldValue(c, "propertyName").toString();
         if (propertyName != null) {
           String[] paramStrs = propertyName.split("\\.");
           if (paramStrs.length > 1) {
             String alias = paramStrs[0];
             Field f = this.entityClass.getDeclaredField(alias);
             if (isRelatingObject(f)) {
               String param = paramStrs[1];
               String identifier = getIdentifierName(f.getType());
               if ((!identifier.equals(param)) &&
                 ((criteria instanceof CriteriaImpl))) {
                 aliasMap.put(alias, alias);
               }
             }
           }
         }
       }
       catch (NoSuchFieldException localNoSuchFieldException)
       {
       }
       criteria.add(c);
     }
     Iterator iterator = aliasMap.values().iterator();
     while (iterator.hasNext()) {
       String alias = (String)iterator.next();
       criteria.createAlias(alias, alias);
     }
     return criteria;
   }

   public Criteria createCriteria(List<Criterion> criterions)
   {
     Criteria criteria = getSession().createCriteria(this.entityClass);
     Map aliasMap = new HashMap();
     for (Criterion c : criterions) {
       String propertyName = null;
       try {
         propertyName = getFieldValue(c, "propertyName").toString();
         if (propertyName != null) {
           String[] paramStrs = propertyName.split("\\.");
           if (paramStrs.length > 1) {
             String alias = paramStrs[0];
             Field f = this.entityClass.getDeclaredField(alias);
             if (isRelatingObject(f)) {
               String param = paramStrs[1];
               String identifier = getIdentifierName(f.getType());
               if ((!identifier.equals(param)) &&
                 ((criteria instanceof CriteriaImpl))) {
                 aliasMap.put(alias, alias);
               }
             }
           }
         }
       }
       catch (NoSuchFieldException localNoSuchFieldException)
       {
       }
       criteria.add(c);
     }
     Iterator iterator = aliasMap.values().iterator();
     while (iterator.hasNext()) {
       String alias = (String)iterator.next();
       criteria.createAlias(alias, alias);
     }
     return criteria;
   }

   public List findByOrder(String orderby, String order)
   {
     Criteria c = getSession().createCriteria(this.entityClass);
     if (StringUtils.lowerCase(order).equals("asc")) {
       c.addOrder(Order.asc(orderby));
     }
     if (StringUtils.lowerCase(order).equals("desc")) {
       c.addOrder(Order.desc(orderby));
     }
     return c.list();
   }

   public List findByHql(String hql, Object[] values)
   {
     return createQuery(hql, values).list();
   }

   public List findByCriteria(Criterion[] criterion)
   {
     return createCriteria(criterion).list();
   }

   public Object findUnique(String hql, Object[] values)
   {
     return createQuery(hql, values).uniqueResult();
   }

   public Object findUnique(Criterion[] criterion)
   {
     return createCriteria(criterion).uniqueResult();
   }

   public Integer findUniqueInt(String hql, Object[] values)
   {
     return (Integer)findUnique(hql, values);
   }

   public Long findUniqueLong(String hql, Object[] values)
   {
     return (Long)findUnique(hql, values);
   }

   public void initObject(Object object)
   {
     Hibernate.initialize(object);
   }

   public void initObjects(List list)
   {
     for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object object = localIterator.next();
       Hibernate.initialize(object);
     }
   }

   public <X> List<X> distinct(List<X> list)
   {
     Set set = new LinkedHashSet(list);
     return new ArrayList(set);
   }

   public Query distinct(Query query) {
     query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
     return query;
   }

   public Criteria distinct(Criteria c) {
     c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
     return c;
   }

   public String getIdentifierName()
   {
     return getSessionFactory().getClassMetadata(this.entityClass).getIdentifierPropertyName();
   }

   public String getIdentifierName(Class entityClass)
   {
     return getSessionFactory().getClassMetadata(entityClass).getIdentifierPropertyName();
   }

   public String removeSelect(String hql)
   {
     int beginPos = hql.toLowerCase().indexOf("from");
     return hql.substring(beginPos);
   }

   public String removeOrders(String hql)
   {
     Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
     Matcher m = p.matcher(hql);
     StringBuffer sb = new StringBuffer();
     while (m.find()) {
       m.appendReplacement(sb, "");
     }
     m.appendTail(sb);
     return sb.toString();
   }

   public String replaceInject(String hql)
   {
     return StringEscapeUtils.escapeSql(hql);
   }

   public String replaceCountHql(String hql)
   {
     return "select count(*) " + removeOrders(removeSelect(hql));
   }

   public String getCountSql(String originalHql)
   {
     QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(originalHql, originalHql,
       Collections.EMPTY_MAP, (SessionFactoryImplementor)getSessionFactory());
     queryTranslator.compile(Collections.EMPTY_MAP, false);
     return "select count(*) from (" + queryTranslator.getSQLString() + ") tmp_count_t";
   }

   public long getCountByCriteria(Criteria c)
   {
     CriteriaImpl impl = (CriteriaImpl)c;

     Projection projection = impl.getProjection();
     ResultTransformer transformer = impl.getResultTransformer();

     List orderEntries = null;
     try {
       orderEntries = (List)getFieldValue(impl, "orderEntries");
       setFieldValue(impl, "orderEntries", new ArrayList());
     } catch (NoSuchFieldException e) {
       e.printStackTrace();
     }

     long totalCount = ((Long)c.setProjection(Projections.rowCount()).uniqueResult()).longValue();
     if (totalCount < 0L) {
       return -1L;
     }

     c.setProjection(projection);

     if (projection == null) {
       c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
     }
     if (transformer != null) {
       c.setResultTransformer(transformer);
     }
     try
     {
       setFieldValue(impl, "orderEntries", orderEntries);
     } catch (NoSuchFieldException e) {
       e.printStackTrace();
     }
     return totalCount;
   }

   protected Object getFieldValue(Object object, String fieldName)
     throws NoSuchFieldException
   {
     Field field = getDeclaredField(object, fieldName);
     if (!field.isAccessible()) {
       field.setAccessible(true);
     }
     Object result = null;
     try {
       result = field.get(object);
     } catch (Exception e) {
       e.printStackTrace();
     }
     return result;
   }

   protected Field getDeclaredField(Object object, String fieldName)
     throws NoSuchFieldException
   {
     return getDeclaredField(object.getClass(), fieldName);
   }

   protected Field getDeclaredField(Class clazz, String fieldName)
     throws NoSuchFieldException
   {
     for (Class superClass = clazz; superClass != Object.class; ) {
       try {
         return superClass.getDeclaredField(fieldName);
       }
       catch (NoSuchFieldException localNoSuchFieldException)
       {
         superClass = superClass.getSuperclass();
       }

     }

     throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + fieldName);
   }

   protected void setFieldValue(Object object, String fieldName, Object value)
     throws NoSuchFieldException
   {
     Field field = getDeclaredField(object, fieldName);
     if (!field.isAccessible())
       field.setAccessible(true);
     try
     {
       field.set(object, value);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   protected boolean isRelatingObject(Field field)
   {
     return (field.isAnnotationPresent(ManyToOne.class)) || (field.isAnnotationPresent(OneToOne.class));
   }
 }
