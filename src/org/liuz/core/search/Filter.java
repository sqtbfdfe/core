package org.liuz.core.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

public class Filter
        implements Serializable {
    private static final long serialVersionUID = -2322605186634414935L;
    protected String property;
    protected Object value;
    protected int operator;
    public static final int OP_EQUAL = 0;
    public static final int OP_NOT_EQUAL = 1;
    public static final int OP_LESS_THAN = 2;
    public static final int OP_GREATER_THAN = 3;
    public static final int OP_LESS_OR_EQUAL = 4;
    public static final int OP_GREATER_OR_EQUAL = 5;
    public static final int OP_LIKE = 6;
    public static final int OP_ILIKE = 7;
    public static final int OP_IN = 8;
    public static final int OP_NOT_IN = 9;
    public static final int OP_NULL = 10;
    public static final int OP_NOT_NULL = 11;
    public static final int OP_EMPTY = 12;
    public static final int OP_NOT_EMPTY = 13;
    public static final int OP_NOT = 14;
    public static final int OP_AND = 100;
    public static final int OP_OR = 101;
    public static final int OP_SOME = 200;
    public static final int OP_ALL = 201;
    public static final int OP_NONE = 202;

    public Filter() {
    }

    public Filter(String property, Object value, int operator) {
        Assert.hasText(property, "property不能为空");
        Assert.notNull(Integer.valueOf(operator), "operator不能为空");
        this.property = property;
        this.value = value;
        this.operator = operator;
    }

    public Filter(String property, Object value) {
        this.property = property;
        this.value = value;
        this.operator = 0;
    }

    public static Filter equal(String property, Object value) {
        return new Filter(property, value, 0);
    }

    public static Filter lessThan(String property, Object value) {
        return new Filter(property, value, 2);
    }

    public static Filter greaterThan(String property, Object value) {
        return new Filter(property, value, 3);
    }

    public static Filter lessOrEqual(String property, Object value) {
        return new Filter(property, value, 4);
    }

    public static Filter greaterOrEqual(String property, Object value) {
        return new Filter(property, value, 5);
    }

    public static Filter in(String property, Collection<?> value) {
        return new Filter(property, value, 8);
    }

    public static Filter in(String property, Object[] value) {
        return new Filter(property, value, 8);
    }

    public static Filter notIn(String property, Collection<?> value) {
        return new Filter(property, value, 9);
    }

    public static Filter notIn(String property, Object[] value) {
        return new Filter(property, value, 9);
    }

    public static Filter like(String property, String value) {
        return new Filter(property, value, 6);
    }

    public static Filter ilike(String property, String value) {
        return new Filter(property, value, 7);
    }

    public static Filter notEqual(String property, Object value) {
        return new Filter(property, value, 1);
    }

    public static Filter isNull(String property) {
        return new Filter(property, Boolean.valueOf(true), 10);
    }

    public static Filter isNotNull(String property) {
        return new Filter(property, Boolean.valueOf(true), 11);
    }

    public static Filter isEmpty(String property) {
        return new Filter(property, Boolean.valueOf(true), 12);
    }

    public static Filter isNotEmpty(String property) {
        return new Filter(property, Boolean.valueOf(true), 13);
    }

    public static Filter and(Filter[] filters) {
        Filter filter = new Filter("AND", null, 100);
        Filter[] arrayOfFilter = filters;
        int j = filters.length;
        for (int i = 0; i < j; i++) {
            Filter f = arrayOfFilter[i];
            filter.add(f);
        }
        return filter;
    }

    public static Filter or(Filter[] filters) {
        Filter filter = and(filters);
        filter.property = "OR";
        filter.operator = 101;
        return filter;
    }

    public Filter andFilter(Filter filter) {
        add(filter);
        this.property = "AND";
        this.operator = 100;
        return this;
    }

    public Filter orFilter(Filter filter) {
        add(filter);
        this.property = "OR";
        this.operator = 101;
        return this;
    }

    public static Filter not(Filter filter) {
        return new Filter("NOT", filter, 14);
    }

    public void add(Filter filter) {
        if ((this.value == null) || (!(this.value instanceof List))) {
            this.value = new ArrayList();
        }
        ((List) this.value).add(filter);
    }

    public void remove(Filter filter) {
        if ((this.value == null) || (!(this.value instanceof List))) {
            return;
        }
        ((List) this.value).remove(filter);
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getOperator() {
        return this.operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public boolean isSimpleFilter() {
        return this.operator < 50;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.operator;
        result = 31 * result + (this.property == null ? 0 : this.property.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Filter other = (Filter) obj;
        if (this.operator != other.operator)
            return false;
        if (this.property == null) {
            if (other.property != null)
                return false;
        } else if (!this.property.equals(other.property))
            return false;
        if (this.value == null) {
            if (other.value != null)
                return false;
        } else if (!this.value.equals(other.value))
            return false;
        return true;
    }

    public String toString() {
        switch (this.operator) {
            case 8:
                return "`" + this.property + "` in (" + InternalUtil.paramDisplayString(this.value) + ")";
            case 9:
                return "`" + this.property + "` not in (" + InternalUtil.paramDisplayString(this.value) + ")";
            case 0:
                return "`" + this.property + "` = " + InternalUtil.paramDisplayString(this.value);
            case 1:
                return "`" + this.property + "` != " + InternalUtil.paramDisplayString(this.value);
            case 3:
                return "`" + this.property + "` > " + InternalUtil.paramDisplayString(this.value);
            case 2:
                return "`" + this.property + "` < " + InternalUtil.paramDisplayString(this.value);
            case 5:
                return "`" + this.property + "` >= " + InternalUtil.paramDisplayString(this.value);
            case 4:
                return "`" + this.property + "` <= " + InternalUtil.paramDisplayString(this.value);
            case 6:
                return "`" + this.property + "` LIKE " + InternalUtil.paramDisplayString(this.value);
            case 7:
                return "`" + this.property + "` ILIKE " + InternalUtil.paramDisplayString(this.value);
            case 10:
                return "`" + this.property + "` IS NULL";
            case 11:
                return "`" + this.property + "` IS NOT NULL";
            case 12:
                return "`" + this.property + "` IS EMPTY";
            case 13:
                return "`" + this.property + "` IS NOT EMPTY";
            case 100:
            case 101:
                if (!(this.value instanceof List)) {
                    return (this.operator == 100 ? "AND: " : "OR: ") + "**INVALID VALUE - NOT A LIST: (" + this.value +
                            ") **";
                }

                String op = this.operator == 100 ? " and " : " or ";

                StringBuilder sb = new StringBuilder("(");
                boolean first = true;
                for (Iterator localIterator = ((List) this.value).iterator(); localIterator.hasNext(); ) {
                    Object o = localIterator.next();
                    if (first)
                        first = false;
                    else {
                        sb.append(op);
                    }
                    if ((o instanceof Filter))
                        sb.append(o.toString());
                    else {
                        sb.append("**INVALID VALUE - NOT A FILTER: (" + o + ") **");
                    }
                }
                if (first) {
                    return (this.operator == 100 ? "AND: " : "OR: ") + "**EMPTY LIST**";
                }
                sb.append(")");
                return sb.toString();
            case 14:
                if (!(this.value instanceof Filter)) {
                    return "NOT: **INVALID VALUE - NOT A FILTER: (" + this.value + ") **";
                }
                return "not " + this.value.toString();
            case 200:
                if (!(this.value instanceof Filter)) {
                    return "SOME: **INVALID VALUE - NOT A FILTER: (" + this.value + ") **";
                }
                return "some `" + this.property + "` {" + this.value.toString() + "}";
            case 201:
                if (!(this.value instanceof Filter)) {
                    return "ALL: **INVALID VALUE - NOT A FILTER: (" + this.value + ") **";
                }
                return "all `" + this.property + "` {" + this.value.toString() + "}";
            case 202:
                if (!(this.value instanceof Filter)) {
                    return "NONE: **INVALID VALUE - NOT A FILTER: (" + this.value + ") **";
                }
                return "none `" + this.property + "` {" + this.value.toString() + "}";
        }
        return "**INVALID OPERATOR: (" + this.operator + ") - VALUE: " + InternalUtil.paramDisplayString(this.value) + " **";
    }
}
