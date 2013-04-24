 package org.liuz.core.service;

 import java.io.Serializable;
 import java.util.List;
 import java.util.Map;
 import org.springframework.transaction.annotation.Transactional;
 import org.liuz.core.orm.IBaseDao;
 import org.liuz.core.orm.Page;
 import org.liuz.core.search.Search;

 @Transactional
 public abstract class BaseService<T, PK extends Serializable>
 {
   protected abstract IBaseDao<T, PK> getEntityDao();

   @Transactional(readOnly=true)
   public T findById(PK id)
   {
     return getEntityDao().findById(id);
   }

   public void save(T entity)
   {
     getEntityDao().save(entity);
   }

   public void update(T entity)
   {
     getEntityDao().update(entity);
   }

   public void saveOrUpdate(T entity)
   {
     getEntityDao().saveOrUpdate(entity);
   }

   public void delete(T entity)
   {
     getEntityDao().delete(entity);
   }

   public void deleteById(PK id)
   {
     delete(findById(id));
   }

   public void deleteById(PK[] id)
   {
     for (Serializable pk : id)
       deleteById((PK) pk);
   }

   public int execute(String hql, Object[] values)
   {
     return getEntityDao().execute(hql, values);
   }

   @Transactional(readOnly=true)
   public List findAllLike(String queryName, Object queryValue)
   {
     if (("".equals(queryValue)) || (queryValue == null) || ("".equals(queryName)) || (queryName == null)) {
       return getEntityDao().findAll();
     }
     return getEntityDao().findAllLike(queryName, queryValue);
   }

   @Transactional(readOnly=true)
   public List findAllEq(String queryName, Object queryValue)
   {
     if (("".equals(queryValue)) || (queryValue == null) || ("".equals(queryName)) || (queryName == null)) {
       return getEntityDao().findAll();
     }
     return getEntityDao().findAllEq(queryName, queryValue);
   }

   @Transactional(readOnly=true)
   public List<T> findAll()
   {
     return getEntityDao().findAll();
   }

   @Transactional(readOnly=true)
   public Page<T> findPage(Page page)
   {
     return getEntityDao().findPage(page);
   }

   @Transactional(readOnly=true)
   public Page findPageByMap(Page page, Map queryMap)
   {
     return getEntityDao().findPageByMap(page, queryMap);
   }

   @Transactional(readOnly=true)
   public T findUniqueByProperty(String propertyName, Object value)
   {
     return getEntityDao().findUniqueByProperty(propertyName, value);
   }

   @Transactional(readOnly=true)
   public Page findPageByPropertyLike(Page page, String queryName, Object queryValue)
   {
     if (("".equals(queryValue)) || (queryValue == null) || ("".equals(queryName)) || (queryName == null)) {
       return getEntityDao().findPage(page);
     }

     return getEntityDao().findPageByPropertyLike(page, queryName, queryValue);
   }

   @Transactional(readOnly=true)
   public Page findPageByPropertyExact(Page page, String queryName, Object queryValue)
   {
     if (("".equals(queryValue)) || (queryValue == null) || ("".equals(queryName)) || (queryName == null)) {
       return getEntityDao().findPage(page);
     }
     return getEntityDao().findPageByPropertyExact(page, queryName, queryValue);
   }

   @Transactional(readOnly=true)
   public Page findPageByQuerysBlur(Page page, T entity)
   {
     return getEntityDao().findPageByQuerysBlur(page, entity);
   }

   @Transactional(readOnly=true)
   public Page findPageByQuerysExact(Page page, T entity)
   {
     return getEntityDao().findPageByQuerysExact(page, entity);
   }

   @Transactional(readOnly=true)
   public boolean isPropertyUnique(String propertyName, Object newValue, Object oldValue)
   {
     if ((newValue == null) || ("".equals(newValue)) || (newValue.equals(oldValue)))
       return true;
     Object object = getEntityDao().findUniqueByProperty(propertyName, newValue);
     return object == null;
   }

   @Transactional(readOnly=true)
   public List search(Search search)
   {
     return getEntityDao().search(search);
   }

   @Transactional(readOnly=true)
   public Page search(Page page, Search search)
   {
     return getEntityDao().search(page, search);
   }
 }
