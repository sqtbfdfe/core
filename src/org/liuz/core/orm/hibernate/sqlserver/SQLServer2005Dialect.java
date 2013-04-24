 package org.liuz.core.orm.hibernate.sqlserver;

 import org.hibernate.dialect.SQLServerDialect;

 public class SQLServer2005Dialect extends SQLServerDialect
 {
   private ThreadLocal<Boolean> supportsVariableLimit = new ThreadLocal();

   public SQLServer2005Dialect() {
     registerFunction("bitand", new BitAndFunction());
     registerFunction("bitxor", new BitXorFunction());
     registerFunction("bitor", new BitOrFunction());
     setSupportsVariableLimit(false);
   }

   private void setSupportsVariableLimit(boolean first)
   {
     this.supportsVariableLimit.set(Boolean.valueOf(first));
   }

   protected static int getSqlAfterSelectInsertPoint(String sql)
   {
     int selectIndex = sql.toLowerCase().indexOf("select");

     int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");

     return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
   }

   public boolean supportsLimitOffset() {
     return true;
   }

   public boolean supportsVariableLimit()
   {
     return ((Boolean)this.supportsVariableLimit.get()).booleanValue();
   }

   public boolean useMaxForLimit() {
     return true;
   }

   public String getLimitString(String query, int offset, int limit)
   {
     setSupportsVariableLimit(offset > 0);

     if (offset == 0) {
       return new StringBuffer(query.length() + 8).append(query).insert(getSqlAfterSelectInsertPoint(query), " top " + limit).toString();
     }

     return getLimitString(query, offset > 0);
   }

   public String getLimitString(String sql, boolean hasOffset) {
     int orderByIndex = sql.toLowerCase().lastIndexOf("order by");

     if (orderByIndex <= 0) {
       throw new UnsupportedOperationException("must specify 'order by' statement to support limit operation with offset in sql server 2005");
     }

     String sqlOrderBy = sql.substring(orderByIndex + 8);

     String sqlRemoveOrderBy = sql.substring(0, orderByIndex);

     int insertPoint = getSqlAfterSelectInsertPoint(sql);
     return
       ") select * from tempPagination where RowNumber>?  and RowNumber<=?";
   }
 }

