 package org.liuz.core.search;

 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.List;
 import org.springframework.util.Assert;

 public class Search
   implements Serializable
 {
   private static final long serialVersionUID = 4893087551093825787L;
   protected List<Filter> filters = new ArrayList();

   protected List<Sort> sorts = new ArrayList();

   public Search addFilter(Filter filter)
   {
     if (this.filters != null)
       this.filters.add(filter);
     return this;
   }

   public Search addFilterEqual(String property, Object value)
   {
     return addFilter(Filter.equal(property, value));
   }

   public Search addFilterGreaterOrEqual(String property, Object value)
   {
     return addFilter(Filter.greaterOrEqual(property, value));
   }

   public Search addFilterGreaterThan(String property, Object value)
   {
     return addFilter(Filter.greaterThan(property, value));
   }

   public Search addFilterIn(String property, Collection<?> value)
   {
     return addFilter(Filter.in(property, value));
   }

   public Search addFilterIn(String property, Object[] value)
   {
     return addFilter(Filter.in(property, value));
   }

   public Search addFilterNotIn(String property, Collection<?> value)
   {
     return addFilter(Filter.notIn(property, value));
   }

   public Search addFilterNotIn(String property, Object[] value)
   {
     return addFilter(Filter.notIn(property, value));
   }

   public Search addFilterLessOrEqual(String property, Object value)
   {
     return addFilter(Filter.lessOrEqual(property, value));
   }

   public Search addFilterLessThan(String property, Object value)
   {
     return addFilter(Filter.lessThan(property, value));
   }

   public Search addFilterLike(String property, String value)
   {
     return addFilter(Filter.like(property, value));
   }

   public Search addFilterILike(String property, String value)
   {
     return addFilter(Filter.ilike(property, value));
   }

   public Search addFilterNotEqual(String property, Object value)
   {
     return addFilter(Filter.notEqual(property, value));
   }

   public Search addFilterNull(String property)
   {
     return addFilter(Filter.isNull(property));
   }

   public Search addFilterNotNull(String property)
   {
     return addFilter(Filter.isNotNull(property));
   }

   public Search addFilterEmpty(String property)
   {
     return addFilter(Filter.isEmpty(property));
   }

   public Search addFilterNotEmpty(String property)
   {
     return addFilter(Filter.isNotEmpty(property));
   }

   public Search addFilterAnd(Filter[] filters)
   {
     return addFilter(Filter.and(filters));
   }

   public Search addFilterOr(Filter[] filters)
   {
     return addFilter(Filter.or(filters));
   }

   public Search addFilterNot(Filter filter)
   {
     return addFilter(Filter.not(filter));
   }

   public void removeFilter(Filter filter)
   {
     Assert.notNull(filter, "filter不能为空");
     if (this.filters != null)
       this.filters.remove(filter);
   }

   public void removeFiltersOnProperty(String property)
   {
     Assert.hasText(property, "property不能为空");
     Iterator itr = getFilters().iterator();
     while (itr.hasNext())
       if (property.equals(((Filter)itr.next()).getProperty()))
         itr.remove();
   }

   public void clearFilters()
   {
     if (this.filters != null)
       this.filters.clear();
   }

   public Search addSort(Sort sort)
   {
     Assert.notNull(sort, "sort不能为空");
     if (this.sorts == null)
       this.sorts = new ArrayList();
     this.sorts.add(sort);
     return this;
   }

   public Search addSort(String property, String order)
   {
     Assert.hasText(property, "property不能为空");
     Assert.hasText(order, "order不能为空");
     return addSort(new Sort(property, order));
   }

   public Search addSortAsc(String property)
   {
     Assert.hasText(property, "property不能为空");
     return addSort(Sort.asc(property));
   }

   public Search addSortDesc(String property)
   {
     Assert.hasText(property, "property不能为空");
     return addSort(Sort.desc(property));
   }

   public void removeSort(Sort sort)
   {
     if (getSorts() != null)
       getSorts().remove(sort);
   }

   public void removeSort(String property)
   {
     Assert.hasText(property, "property不能为空");
     if (this.sorts == null)
       this.sorts = new ArrayList();
     Iterator itr = this.sorts.iterator();
     while (itr.hasNext())
       if (property.equals(((Sort)itr.next()).getProperty()))
         itr.remove();
   }

   public void clearSorts()
   {
     if (this.sorts != null)
       this.sorts.clear();
   }

   public void clear()
   {
     clearFilters();
     clearSorts();
   }

   public List<Filter> getFilters() {
     return this.filters;
   }

   public Search setFilters(List<Filter> filters) {
     this.filters = filters;
     return this;
   }

   public List<Sort> getSorts() {
     return this.sorts;
   }

   public Search setSorts(List<Sort> sorts) {
     this.sorts = sorts;
     return this;
   }

   public boolean equals(Object obj)
   {
     if (this == obj)
       return true;
     Search s = (Search)obj;
     if (getFilters() == null ? s.getFilters() != null : !getFilters().equals(s.getFilters()))
       return false;
     if (getSorts() == null ? s.getSorts() != null : !getSorts().equals(s.getSorts())) {
       return false;
     }
     return true;
   }

   public int hashCode()
   {
     int hash = 1;
     hash = hash * 31 + (getFilters() == null ? 0 : getFilters().hashCode());
     hash = hash * 31 + (getSorts() == null ? 0 : getSorts().hashCode());
     return hash;
   }

   public String toString()
   {
     StringBuilder sb = new StringBuilder("Search");
     sb.append("[filters: {\n  ");
     appendList(sb, getFilters(), ",\n  ");
     sb.append("\n },\n sorts: { ");
     appendList(sb, getSorts(), ", ");
     sb.append(" }\n]");
     return sb.toString();
   }

   private void appendList(StringBuilder sb, List<?> list, String separator) {
     if (list == null) {
       sb.append("null");
       return;
     }
     boolean first = true;
     for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
       if (first)
         first = false;
       else {
         sb.append(separator);
       }
       sb.append(o);
     }
   }
 }
