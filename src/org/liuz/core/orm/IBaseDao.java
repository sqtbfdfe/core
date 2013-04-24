package org.liuz.core.orm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.liuz.core.search.Search;

public abstract interface IBaseDao<T, PK extends Serializable>
{
  public abstract T findById(PK paramPK);

  public abstract T findByIdForLock(PK paramPK);

  public abstract void save(T paramT);

  public abstract void update(T paramT);

  public abstract void saveOrUpdate(T paramT);

  public abstract void delete(T paramT);

  public abstract int execute(String paramString, Object[] paramArrayOfObject);

  public abstract void deleteById(PK paramPK);

  public abstract List<T> findAll();

  public abstract List<T> findAllLike(String paramString, Object paramObject);

  public abstract List<T> findAllEq(String paramString, Object paramObject);

  public abstract Page<T> findPage(Page<T> paramPage);

  public abstract T findUniqueByProperty(String paramString, Object paramObject);

  public abstract Page findPageByPropertyExact(Page paramPage, String paramString, Object paramObject);

  public abstract Page findPageByPropertyLike(Page paramPage, String paramString, Object paramObject);

  public abstract Page findPageByQuerysBlur(Page paramPage, T paramT);

  public abstract Page findPageByQuerysExact(Page paramPage, T paramT);

  public abstract Page findPageByMap(Page paramPage, Map paramMap);

  public abstract List search(Search paramSearch);

  public abstract Page search(Page paramPage, Search paramSearch);
}


