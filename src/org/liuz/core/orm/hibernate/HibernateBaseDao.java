 package org.liuz.core.orm.hibernate;

 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;

 import org.apache.commons.lang.StringUtils;
 import org.hibernate.Criteria;
 import org.hibernate.LockOptions;
 import org.hibernate.Query;
 import org.hibernate.criterion.Criterion;
 import org.hibernate.criterion.Example;
 import org.hibernate.criterion.Junction;
 import org.hibernate.criterion.MatchMode;
 import org.hibernate.criterion.Order;
 import org.hibernate.criterion.Restrictions;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.util.Assert;
 import org.liuz.core.orm.IBaseDao;
 import org.liuz.core.orm.Page;
 import org.liuz.core.search.Filter;
 import org.liuz.core.search.Search;
 import org.liuz.core.search.Sort;

 public class HibernateBaseDao<T, PK extends Serializable> extends HibernateGenericDao
   implements IBaseDao<T, PK>
 {
   protected Logger logger = LoggerFactory.getLogger(getClass());

   public HibernateBaseDao() {
     if (this.entityClass == null)
       this.entityClass =
         ((Class)((java.lang.reflect.ParameterizedType)getClass()
         .getGenericSuperclass()).getActualTypeArguments()[0]);
   }

   protected Class<T> getEntityClass()
   {
     return this.entityClass;
   }

   public T findById(PK id)
   {
     Assert.notNull(id, "id不能为空");
     return (T) getSession().load(getEntityClass(), id);
   }

   public T findByIdForLock(PK id)
   {
     Assert.notNull(id, "id不能为空");
     return (T) getSession().load(getEntityClass(), id, LockOptions.UPGRADE);
   }

   public void save(T entity)
   {
     Assert.notNull(entity, "entity不能为空");
     getSession().save(entity);
     this.logger.info("save entity: {}", entity);
   }

   public void update(T entity)
   {
     Assert.notNull(entity, "entity不能为空");
     getSession().update(entity);
     this.logger.info("update entity: {}", entity);
   }

   public void saveOrUpdate(T entity) {
     Assert.notNull(entity, "entity不能为空");
     getSession().saveOrUpdate(entity);
     this.logger.debug("save entity: {}", entity);
   }

   public void delete(T entity)
   {
     Assert.notNull(entity, "entity不能为空");
     getSession().delete(entity);
     this.logger.info("delete entity: {}", entity);
   }

   public void deleteById(PK id)
   {
     Assert.notNull(id, "id不能为空");
     delete(findById(id));
     this.logger.info("delete entity {}, id is {}", this.entityClass.getSimpleName(), id);
   }

   public int execute(String queryString, Object[] values)
   {
     Assert.hasText(queryString, "queryString不能为空");
     return createQuery(queryString, values).executeUpdate();
   }

   public List<T> findAll()
   {
     return findByCriteria(new Criterion[0]);
   }

   public List<T> findAllLike(String queryName, Object queryValue) {
     Criterion criterion = Restrictions.like(queryName, queryValue);
     return findAll(new Criterion[] { criterion });
   }

   public List<T> findAllEq(String queryName, Object queryValue) {
     Criterion criterion = Restrictions.eq(queryName, queryValue);
     return findAll(new Criterion[] { criterion });
   }

   public List<T> findAll(Criterion[] criterion)
   {
     return findByCriteria(criterion);
   }

   public List<T> findAllSort(String orderby, String order) {
     return findByOrder(orderby, order);
   }

   public Page findPage(Page<T> page)
   {
     Assert.notNull(page, "page不能为空");
     return findPageByCriteria(page, new Criterion[0]);
   }

   public T findUniqueByProperty(String propertyName, Object value)
   {
     Assert.hasText(propertyName, "propertyName不能为空");
     return (T) createCriteria(new Criterion[] { Restrictions.eq(propertyName, value) }).uniqueResult();
   }

   public Page findPageByHql(Page<T> page, String hql, Object[] values)
   {
     Assert.notNull(page, "page不能为空");
     Assert.hasText(hql, "hql不能为空");
     long count = findCountByHql(hql, values);
     if (count > -1L)
       page.setTotalCount((int)count);
     Query q = createQuery(hql, values);
     q.setFirstResult(page.getFirst());
     q.setMaxResults(page.getPageSize());

     page.setResult(q.list());
     return page;
   }

   public long findCountByHql(String hql, Object[] values)
   {
     if ((hql.toLowerCase().indexOf("group by") > 0) || (hql.toLowerCase().indexOf(" distinct ") > 0)) {
       String sql = getCountSql(removeOrders(hql));
       Query query = createSqlQuery(sql, values);
       return Long.parseLong(query.uniqueResult().toString());
     }
     return findUniqueLong(replaceCountHql(hql), values).longValue();
   }

   public Page findPageByQuerys(Page page, T entity, MatchMode matchMode, Criterion[] criterion)
   {
     Assert.notNull(page, "page不能为空");
     Criteria c = createCriteria(criterion);
     if (entity != null) {
       Example example = Example.create(entity);
       example.enableLike(matchMode);
       c.add(example);
     }

     page.setTotalCount(getCountByCriteria(c));
     c.setFirstResult(page.getFirst());
     c.setMaxResults(page.getPageSize());
     if (page.isOrderBySetted()) {
       String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
       String[] orderArray = StringUtils.split(page.getOrder(), ',');
       Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

       for (int i = 0; i < orderByArray.length; i++) {
         if ("asc".equals(orderArray[i]))
           c.addOrder(Order.asc(orderByArray[i]));
         else {
           c.addOrder(Order.desc(orderByArray[i]));
         }
       }
     }
     page.setResult(c.list());
     return page;
   }

   public Page findPageByQuerys(Page page, T entity, MatchMode matchMode, List<Criterion> criterion)
   {
     Assert.notNull(page, "page不能为空");
     Criteria c = createCriteria(criterion);
     if (entity != null) {
       Example example = Example.create(entity);
       example.enableLike(matchMode);
       c.add(example);
     }
     page.setTotalCount(getCountByCriteria(c));
     c.setFirstResult(page.getFirst());
     c.setMaxResults(page.getPageSize());
     if (page.isOrderBySetted()) {
       String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
       String[] orderArray = StringUtils.split(page.getOrder(), ',');

       Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

       for (int i = 0; i < orderByArray.length; i++) {
         if ("asc".equals(orderArray[i]))
           c.addOrder(Order.asc(orderByArray[i]));
         else {
           c.addOrder(Order.desc(orderByArray[i]));
         }
       }
     }
     page.setResult(c.list());
     return page;
   }

   public Page findPageByCriteria(Page<T> page, Criterion[] criterion)
   {
     return findPageByQuerys(page, null, null, criterion);
   }

   public Page findPageByCriteria(Page<T> page, List<Criterion> criterion)
   {
     return findPageByQuerys(page, null, null, criterion);
   }

   public long findCountByCriteria(Criterion[] criterion)
   {
     Criteria c = createCriteria(criterion);
     return getCountByCriteria(c);
   }

   public Page findPageByQueryMatch(Page page, T entity, MatchMode matchMode)
   {
     return findPageByQuerys(page, entity, matchMode, new Criterion[0]);
   }

   public Page findPageByQuerysBlur(Page page, T entity)
   {
     return findPageByQuerys(page, entity, MatchMode.ANYWHERE, new Criterion[0]);
   }

   public Page findPageByQuerysExact(Page page, T entity)
   {
     return findPageByQuerys(page, entity, MatchMode.EXACT, new Criterion[0]);
   }

   public Page findPageByPropertyExact(Page page, String queryName, Object queryValue)
   {
     Criterion criterion = Restrictions.eq(queryName, queryValue);
     return findPageByCriteria(page, new Criterion[] { criterion });
   }

   public Page findPageByPropertyLike(Page page, String queryName, Object queryValue)
   {
     Criterion criterion = Restrictions.like(queryName, "%" + queryValue + "%");
     return findPageByCriteria(page, new Criterion[]{criterion});
   }

   public Page findPageByMap(Page page, Map queryMap)
   {
     Assert.notNull(page, "page不能为空");
     Assert.notNull(queryMap, "queryMap不能为空");
     StringBuffer hql = new StringBuffer("from ");
     hql.append(getEntityClass().getName()).append(" where 1=1 ");
     Iterator it = queryMap.keySet().iterator();
     while (it.hasNext()) {
       Object key = it.next();
       hql.append(" and ").append(key.toString()).append(" like '%").append(replaceInject(String.valueOf(queryMap.get(key)))).append("%'");
     }
     return findPageByHql(page, hql.toString(), new Object[0]);
   }

   public List search(Search search)
   {
     Assert.notNull(search, "search不能为空");
     List criterionList = new ArrayList();
     List<Filter> filters = search.getFilters();
     for (Filter filter : filters) {
       if ((!filter.getValue().equals("")) && (filter.getValue() != null))
         criterionList.add(filter.isSimpleFilter() ?
           buildCriterionBySimFilter(filter) : buildCriterionByConnFilter(filter));
     }
     Criteria c = createCriteria(criterionList);
     List<Sort> sorts = search.getSorts();
     for (Sort sort : sorts) {
       if (sort.getOrder().equals("asc")) {
         c.addOrder(Order.asc(sort.getProperty()));
       }
       if (sort.getOrder().equals("desc")) {
         c.addOrder(Order.desc(sort.getProperty()));
       }
     }
     return c.list();
   }

   public Page search(Page page, Search search)
   {
     Assert.notNull(search, "search不能为空");
     List criterionList = new ArrayList();
     List<Filter> filters = search.getFilters();
     for (Filter filter : filters) {
       if ((!"".equals(filter.getValue())) && (filter.getValue() != null))
         criterionList.add(filter.isSimpleFilter() ?
           buildCriterionBySimFilter(filter) : buildCriterionByConnFilter(filter));
     }
     Criteria c = createCriteria(criterionList);
     page.setTotalCount(getCountByCriteria(c));
     c.setFirstResult(page.getFirst());
     c.setMaxResults(page.getPageSize());
     List<Sort> sorts = search.getSorts();
     for (Sort sort : sorts) {
       if (sort.getOrder().equals("asc")) {
         c.addOrder(Order.asc(sort.getProperty()));
       }
       if (sort.getOrder().equals("desc")) {
         c.addOrder(Order.desc(sort.getProperty()));
       }
     }
     page.setResult(c.list());
     return page;
   }

   public Criterion buildCriterionBySimFilter(Filter filter)
   {
     String propertyName = filter.getProperty();
     Object value = filter.getValue();
     int operator = filter.getOperator();
     Criterion criterion = null;
     switch (operator) {
     case 12:
       criterion = Restrictions.isEmpty(propertyName);
       break;
     case 0:
       criterion = Restrictions.eq(propertyName, value);
       break;
     case 5:
       criterion = Restrictions.ge(propertyName, value);
       break;
     case 3:
       criterion = Restrictions.gt(propertyName, value);
       break;
     case 7:
       criterion = Restrictions.ilike(propertyName, "%" + value + "%");
       break;
     case 8:
       if ((value instanceof Object[]))
         criterion = Restrictions.in(propertyName, (Object[])value);
       if ((value instanceof Collection))
         criterion = Restrictions.in(propertyName, (Collection)value);
       break;
     case 4:
       criterion = Restrictions.le(propertyName, value);
       break;
     case 2:
       criterion = Restrictions.lt(propertyName, value);
       break;
     case 6:
       criterion = Restrictions.like(propertyName, "%" + value + "%");
       break;
     case 13:
       criterion = Restrictions.isNotEmpty(propertyName);
       break;
     case 1:
       criterion = Restrictions.ne(propertyName, value);
       break;
     case 9:
       if ((value instanceof Object[]))
         criterion = Restrictions.in(propertyName, (Object[])value);
       if ((value instanceof Collection))
         criterion = Restrictions.in(propertyName, (Collection)value);
       criterion = Restrictions.not(criterion);
       break;
     case 11:
       criterion = Restrictions.isNotNull(propertyName);
       break;
     case 10:
       criterion = Restrictions.isNull(propertyName);
       break;
     case 14:
       Filter filterNot = (Filter)filter.getValue();
       criterion = Restrictions.not(filterNot.isSimpleFilter() ?
         buildCriterionBySimFilter(filterNot) : buildCriterionByConnFilter(filterNot));
     }

     return criterion;
   }

   public Criterion buildCriterionByConnFilter(Filter filter)
   {
     Criterion criterion = null;
     switch (filter.getOperator()) {
     case 100:
       Junction andCri = Restrictions.conjunction();
       List<Filter> andList = (List<Filter>)filter.getValue();
       for (Filter f : andList) {
         andCri.add(f.isSimpleFilter() ?
           buildCriterionBySimFilter(f) : buildCriterionByConnFilter(f));
       }
       criterion = andCri;
       break;
     case 101:
       Junction orCri = Restrictions.disjunction();
       List<Filter> orList = (List<Filter>)filter.getValue();
       for (Filter f : orList) {
         orCri.add(f.isSimpleFilter() ?
           buildCriterionBySimFilter(f) : buildCriterionByConnFilter(f));
       }
       criterion = orCri;
     }

     return criterion;
   }
 }
