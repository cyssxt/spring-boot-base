package com.cyssxt.common.reflect;

import com.cyssxt.common.annotations.CopyFilter;
import com.cyssxt.common.annotations.PrimaryKey;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.sql.rowset.serial.SerialBlob;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class ReflectUtils {
    private final static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    public static Field getFieldByName(Class clazz,String fieldName) {
        return getField(clazz,fieldName);
    }
    public static Annotation getAnnotation(Class clazz,String fieldName,Class<? extends Annotation> annClass) throws ValidException {
        return getAnnotation(clazz,fieldName,annClass,true);

    }

    public static Annotation getAnnotationByReadMethod(Class clazz,String fieldName,Class<? extends Annotation> annClass){
        try {
            Map<String,Method> map = getReadMapper(clazz);
            Method method = map.get(fieldName);
            if(method!=null){
                return method.getAnnotation(annClass);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Annotation getAnnotation(Class clazz,String fieldName,Class<? extends Annotation> annClass,boolean isThrow) throws ValidException {
        Field field = getField(clazz,fieldName);
        if(field==null){
            if(isThrow){
                throw new ValidException(CoreErrorMessage.CANNOT_FIND_FIELD);
            }
            return null;
        }
        Annotation annotation = field.getAnnotation(annClass);
        if(annotation==null && isThrow){
            throw new ValidException(CoreErrorMessage.CANNOT_FIND_FIELD_WITH_ANNOTATION);
        }
        return annotation;
    }


    public interface FieldListener {
        String getFieldName(String key);
    }

    private static boolean cache = true;

    public static boolean isCache() {
        return cache;
    }

    public static void setCache(boolean cache) {
        ReflectUtils.cache = cache;
    }

    static Map<String, Map<String, Method>> sourceReader = new HashMap<>();
    static Map<String, Map<String, Method>> targetWriter = new HashMap<>();
    static Map<Class, Map<String, Field>> fieldMap = new HashMap<>();
    static Map<Class, List<String>> excludeMap = new HashMap<>();
    static Map<String,Map<String,ReflectBean>> REFLECT_BEANS = new HashMap<>();
    public static final int READ = 0;//读方法
    public static final int WRITE = 1;//写方法

    public static Map<String, Method> getReadMapper(Class clazz) throws IntrospectionException {
        return getReadMapper(clazz,null);
    }

    public static Map<String, Method> getReadMapper(Class clazz,FieldListener fieldListener) throws IntrospectionException {
        return getMap(clazz, READ,fieldListener);
    }

    public static boolean hasField(Class clazz, String fieldName) {
        return getField(clazz,fieldName)!=null;
    }
    public static Field getField(Class clazz, String fieldName) {
        Map<String, Field> map = fieldMap.get(clazz);
        if (map == null) {
            map = getFields(clazz);
        }
        return map.get(fieldName);
    }
    public static Map<String,Field> getFields(Class clazz){
        Map map = new HashMap<>();
        Class supperClass = clazz.getSuperclass();
        if(supperClass!=null) {
            Map<String, Field> fieldMap = getFields(supperClass);
            map.putAll(fieldMap);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            map.put(name, field);
        }
        fieldMap.put(clazz,map);
        return map;
    }

    public static boolean exclude(Class clazz, String fieldName) {
        List list = excludeMap.get(clazz);
        if (list == null) {
            try {
                String[] excludes = (String[]) clazz.getField("excludes").get(null);
                list = Arrays.asList(excludes);
                excludeMap.put(clazz, list);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                if (list == null) {
                    excludeMap.put(clazz, new ArrayList<>());
                }
            }
        }
        boolean flag = list.indexOf(fieldName) == -1 ? false : true;
        return flag;
    }

    public static Map<String, Method> getMap(Class clazz, int type,FieldListener fieldListener) {
        Map<String, Map<String, Method>> mapper = type == READ ? sourceReader : targetWriter;
        String className = clazz.getName();
        Map<String, Method> sourceMap = mapper.get(className);
        if (sourceMap == null) {
            sourceMap = new HashMap<>();
            mapper.put(className, sourceMap);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Class dataType = field.getType();
                if (dataType == Logger.class) {
                    continue;
                }
                PropertyDescriptor pd = null;
                try {
                    pd = new PropertyDescriptor(fieldName, clazz);
                    Method read = type == READ ? pd.getReadMethod() : pd.getWriteMethod();
                    if (read == null) {
                        continue;
                    }
                    if(fieldListener!=null){
                        fieldName = fieldListener.getFieldName(fieldName);
                    }
                    sourceMap.put(fieldName, read);
                } catch (IntrospectionException e) {
                }
            }
            Class supperClass = clazz.getSuperclass();
            if (supperClass != null) {
                Map<String, Method> parentMap = getMap(supperClass, type,fieldListener);
                sourceMap.putAll(parentMap);
            }
        }

        return sourceMap;
    }

    public static Map<String, Method> getWriteMap(Class clazz) throws IntrospectionException {
        return getWriteMap(clazz,null);
    }

    public static Map<String, Method> getWriteMap(Class clazz,FieldListener fieldListener) throws IntrospectionException {
        return getMap(clazz, WRITE,fieldListener);
    }

    public static Map<String, ReflectBean> getBeanMap(Class clazz, int type, Boolean ignoreCase) {
        String className = clazz.getName();
        Map<String, ReflectBean> result = new HashMap<>();
        if(isCache()){
            Map<String,ReflectBean> tmp = REFLECT_BEANS.get(className);
            if(tmp!=null){
                result = tmp;
            }
        }
        //避免多线程导致的问题
        synchronized (result) {
            if (result == null) {
                result = new HashMap<>();
                REFLECT_BEANS.put(className, result);
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            CopyFilter copyFilter = field.getDeclaredAnnotation(CopyFilter.class);
            if(copyFilter!=null){
                continue;
            }
            String fieldName = field.getName();
            Class dataType = field.getType();
            if (dataType == Logger.class) {
                continue;
            }
            PropertyDescriptor pd = null;
            String keyName = ignoreCase ? fieldName.toLowerCase() : fieldName;
            try {
                pd = new PropertyDescriptor(fieldName, clazz);
                Method method = type == READ ? pd.getReadMethod() : pd.getWriteMethod();
                if (method == null) {
                    continue;
                }
                Class fieldType = pd.getPropertyType();
                PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
                result.put(keyName, new ReflectBean(fieldName, method, fieldType,primaryKey!=null));
            } catch (IntrospectionException e) {
                logger.error("e={}", e);
            }
        }
        Class supperClass = clazz.getSuperclass();
        if (supperClass != null) {
            Map<String, ReflectBean> parentMap = getBeanMap(supperClass, type, ignoreCase);
            result.putAll(parentMap);
        }
        return result;
    }
    public static void copyValue(ReflectBean bean, Object value, Object instance){
        copyValue(bean.getMethod(),value,instance);
    }

    public static void setValue(String fieldName,Object value,Object instance) throws ValidException {
        if(StringUtils.isEmpty(fieldName)){
            throw new ValidException(CoreErrorMessage.FIELD_IS_NULL);
        }
        try {
            Map<String,Method> methodMap = getWriteMap(instance.getClass());
            Method method = methodMap.get(fieldName);
            if(method==null){
                throw new ValidException(CoreErrorMessage.FIELD_NOT_EXIST_WRITE_METHOD);
            }
            setValue(method,value,instance);
        } catch (IntrospectionException e) {
            logger.debug("getWriteMap error={}",e);
        }
    }

    public static void setValue(ReflectBean bean,Object value,Object instance){
        Method method = bean.getMethod();
        setValue(method,value,instance);
    }

    public static void setValue(Method method,Object value,Object instance){
        Object param = null;
        try {
            Parameter[] parameters = method.getParameters();
            if(parameters!=null && parameters.length>0){
                Class type = parameters[0].getType();
                if (type.equals(String.class)) {
                    param = value+"";
                }else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                    String temp = value.toString();
                    param = !"0".equals(temp) && "false".equals(temp);// fixed boolean mapper
                } else if (type.equals(Integer.class) || type.equals(int.class)) {
                    param = Integer.valueOf(value+"");
                } else if (type.equals(Double.class) || type.equals(double.class)) {
                    param = Double.valueOf(value+"");
                } else if (type.equals(Float.class) || type.equals(float.class)) {
                    param = Float.valueOf(value+"");
                } else if (type.equals(Long.class) || type.equals(Long.class)) {
                    param = Long.valueOf(value+"");
                }else if(type.equals(BigDecimal.class)) {
                    param = new BigDecimal(value+"");
                } else if (type.equals(Date.class)) {
                    if (value instanceof Timestamp) {
                        param = new Date(((Timestamp) value).getTime());
                    } else {
                        if (CommonUtil.isInteger(value+"")) {
                            param = new Date(Long.valueOf(value+""));
                        } else {
                            param = new Date(value+"");
                        }
                    }
                } else if (type.equals(Timestamp.class)) {
                    if (CommonUtil.isInteger(value+"")) {
                        param = new Timestamp(Long.valueOf(value+""));
                    } else {
                        param = Timestamp.valueOf(value+"");
                    }
                } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                    if ("false".equals(value)) {
                        param = (byte) 0;
                    } else if ("true".equals(value)) {
                        param = (byte) 1;
                    } else {
                        param = Byte.valueOf(value+"");
                    }
                } else if (type.equals(Blob.class)) {
                    try {
                        param = new SerialBlob((byte[]) value);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            method.invoke(instance, param);
        }catch (Exception e){
            logger.error("{} parse error {} error={}",method,param,e);
        }
    }
    public static void copyValue(Method method, Object value, Object instance) {
        ReflectUtils.setValue(method,value,instance);
    }

}
