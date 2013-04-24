package org.liuz.core.search;

import java.util.Collection;
import java.util.Iterator;

public class InternalUtil {
    public static Object convertIfNeeded(Object value, Class<?> type)
            throws ClassCastException {
        if (value == null)
            return null;
        if (type.isInstance(value)) {
            return value;
        }
        if (String.class.equals(type))
            return value.toString();
        if (Number.class.isAssignableFrom(type)) {
            if ((value instanceof Number)) {
                Number num = (Number) value;

                if (type.equals(Double.class))
                    return new Double(num.doubleValue());
                if (type.equals(Float.class))
                    return new Float(num.floatValue());
                if (type.equals(Long.class))
                    return new Long(num.longValue());
                if (type.equals(Integer.class))
                    return new Integer(num.intValue());
                if (type.equals(Short.class))
                    return new Short(num.shortValue());
                try {
                    return type.getConstructor(new Class[]{String.class}).newInstance(new Object[]{value.toString()});
                } catch (Exception localException) {
                }
            } else if ((value instanceof String)) {
                try {
                    if (type.equals(Double.class))
                        return Double.valueOf(Double.parseDouble((String) value));
                    if (type.equals(Float.class))
                        return Float.valueOf(Float.parseFloat((String) value));
                    if (type.equals(Long.class))
                        return Long.valueOf(Long.parseLong((String) value));
                    if (type.equals(Integer.class))
                        return Integer.valueOf(Integer.parseInt((String) value));
                    if (type.equals(Short.class))
                        return Short.valueOf(Short.parseShort((String) value));
                    if (!type.equals(Byte.class))
                        throw new ClassCastException("Unable to convert value of type " + value.getClass().getName() + " to type " +
                                type.getName());
                    return Byte.valueOf(Byte.parseByte((String) value));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (Class.class.equals(type)) {
            try {
                return Class.forName(value.toString());
            } catch (ClassNotFoundException e) {
                throw new ClassCastException("Unable to convert value " + value.toString() + " to type Class");
            }
        }

        throw new ClassCastException("Unable to convert value of type " + value.getClass().getName() + " to type " +
                type.getName());
    }

    public static String paramDisplayString(Object val) {
        if (val == null)
            return "null";
        if ((val instanceof String))
            return "\"" + val + "\"";
        if ((val instanceof Collection)) {
            StringBuilder sb = new StringBuilder();
            sb.append(val.getClass().getSimpleName());
            sb.append(" {");
            boolean first = true;
            for (Iterator localIterator = ((Collection) val).iterator(); localIterator.hasNext(); ) {
                Object o = localIterator.next();
                if (first)
                    first = false;
                else {
                    sb.append(", ");
                }
                sb.append(paramDisplayString(o));
            }
            sb.append("}");
            return sb.toString();
        }
        if ((val instanceof Object[])) {
            StringBuilder sb = new StringBuilder();
            sb.append(val.getClass().getComponentType().getSimpleName());
            sb.append("[] {");
            boolean first = true;
            for (Object o : (Object[]) val) {
                if (first)
                    first = false;
                else {
                    sb.append(", ");
                }
                sb.append(paramDisplayString(o));
            }
            sb.append("}");
            return sb.toString();
        }
        return val.toString();
    }
}

