/**
 * 
 */
package sk.seges.sesam.remote.jmx;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

/**
 * @author LGazo
 *
 */
public class JMXSupport {
    private static final SimpleType[] supportedSimpleTypes = { SimpleType.BIGDECIMAL, SimpleType.BIGINTEGER, SimpleType.BOOLEAN,
        SimpleType.BYTE, SimpleType.CHARACTER, SimpleType.DATE, SimpleType.DOUBLE, SimpleType.FLOAT,
        SimpleType.INTEGER, SimpleType.LONG, SimpleType.OBJECTNAME, SimpleType.SHORT, SimpleType.STRING,
        SimpleType.VOID };
    private final Map<String, SimpleType> classForSimpleTypeMap;

    public JMXSupport() {
        classForSimpleTypeMap = new HashMap<String, SimpleType>();
        for(SimpleType type : supportedSimpleTypes) {
            classForSimpleTypeMap.put(type.getClassName(), type);
        }
    }
    
    private OpenType resolveToOpenType(Object object) {
        if(object instanceof OpenType)
            return (OpenType)object;
        
        Class<?> clz = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        if(clz.isPrimitive()) {
            clz = convertPrimitive(clz);
        }
        SimpleType type = classForSimpleTypeMap.get(clz.getName());
        if(type != null)
            return type;
        throw new RuntimeException("Unsupported type for resolving = " + clz); 
    }
    
    private Class<?> convertPrimitive(Class<?> clz) {
        if("int".equals(clz.getName())) {
            return Integer.class;
        } else if("byte".equals(clz.getName())) {
            return Byte.class;
        } else if("boolean".equals(clz.getName())) {
            return Boolean.class;
        } else if("short".equals(clz.getName())) {
            return Short.class;
        } else if("float".equals(clz.getName())) {
            return Float.class;
        } else if("double".equals(clz.getName())) {
            return double.class;
        } else if("long".equals(clz.getName())) {
            return Long.class;
        }
        
        throw new RuntimeException("Unsupported type for converting = " + clz); 
    }
    
    private List<Field> createSortedFieldList(Class<?> clz) {
        Field[] fields = clz.getDeclaredFields();
        List<Field> fieldList = Arrays.asList(fields);
        Collections.sort(fieldList, new Comparator<Field>() {
            public int compare(Field o1, Field o2) {
                FieldExport fe1 = o1.getAnnotation(FieldExport.class);
                FieldExport fe2 = o2.getAnnotation(FieldExport.class);
                Integer i1 = Integer.valueOf(fe1 != null ? fe1.index() : FieldExport.DEFAULT_INDEX);
                Integer i2 = Integer.valueOf(fe2 != null ? fe2.index() : FieldExport.DEFAULT_INDEX);
                return i1.compareTo(i2);
            }
        });
        
        return fieldList;
    }
    
    public String[] constructFieldNames(Class<?> clz) {
        List<String> names = new LinkedList<String>();

        for (Field field : createSortedFieldList(clz)) {
            if (field.getAnnotation(FieldExport.class) != null)
                names.add(field.getName());
        }
        String[] strings = new String[names.size()];
        names.toArray(strings);
        return strings;
    }
    
    public Object[] constructFieldValues(Object object) {
        List<Object> objectList = new LinkedList<Object>();
        
        for (Field field : createSortedFieldList(object.getClass())) {
            FieldExport export = field.getAnnotation(FieldExport.class);
            if (export != null) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (export.convertToString()) {
                        value = (value == null ? "N/A" : value.toString());
                    }
                    objectList.add(value);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Cannot get value for field = " + field, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access field = " + field, e);
                }
            }
        }
        
        Object[] objects = new Object[objectList.size()];
        objectList.toArray(objects);
        return objects;
    }
    
    public String[] constructFieldDescriptions(Class<?> clz) {
        List<String> names = new LinkedList<String>();

        for (Field field : createSortedFieldList(clz)) {
            FieldExport export = field.getAnnotation(FieldExport.class);
            if (export != null) {
                if("".equals(export.description())) {
                    names.add(field.getName());
                } else {
                    names.add(export.description());
                }
            }
        }
        String[] strings = new String[names.size()];
        names.toArray(strings);
        return strings;
    }
    
    public OpenType[] constructFieldTypes(Class<?> clz) {
        List<OpenType> names = new LinkedList<OpenType>();

        for (Field field : createSortedFieldList(clz)) {
            FieldExport export = field.getAnnotation(FieldExport.class);
            if (export != null) {
                if(!(Throwable.class.equals(export.type()))) {
                    names.add(resolveToOpenType(export.type()));
                } else {
                    names.add(resolveToOpenType(field.getType()));
                }
            }
        }
        OpenType[] strings = new OpenType[names.size()];
        names.toArray(strings);
        return strings;
    }
}
