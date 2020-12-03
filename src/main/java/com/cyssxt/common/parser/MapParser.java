package com.cyssxt.common.parser;

import com.cyssxt.common.reflect.ReflectBean;
import com.cyssxt.common.reflect.ReflectUtils;
import org.springframework.util.StringUtils;
import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapParser {

    public String getKeyName(String name){
        return name;
    }
    public Map<String,String> toMap(){
        return toMap(null);
    }
    public Map<String,String> toMap(NameParser parser)  {
        Map<String,String> result = new HashMap<>();
        try {
            Map<String, Method> map = ReflectUtils.getReadMapper(this.getClass());
            Iterator<Map.Entry<String,Method>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,Method> entry = iterator.next();
                String name = entry.getKey();
                Method method = entry.getValue();
                try {
                    Object value = method.invoke(this);

                    result.put(parseField(parser, name), value.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Object parseMap(Map<String, String> params){
        return parseMap(params,null);
    }
    public Object parseMap(Map<String,String> params,NameParser parser){
//        Map<String,String> result = new HashMap<>();
        try {
            Map<String, Method> readMap = ReflectUtils.getWriteMap(this.getClass());
            Iterator<Map.Entry<String,Method>> iterator = readMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,Method> entry = iterator.next();
                String name = entry.getKey();
                Method method = entry.getValue();
                Field field = ReflectUtils.getField(this.getClass(),name);
                MapParserField mapParserField = field.getAnnotation(MapParserField.class);
                String realName;
                if(mapParserField!=null && !StringUtils.isEmpty(mapParserField.value())){
                    realName = mapParserField.value();
                }else{
                    realName = parser!=null?parser.getName(name):getKeyName(name);
                }
                Class type = field.getType();
                if(method!=null) {
                    ReflectUtils.copyValue(method, type, params.get(realName), this);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return this;
    }

    private String parseField(NameParser parser, String name) {
        Field field = ReflectUtils.getField(this.getClass(),name);
        MapParserField mapParserField = field.getAnnotation(MapParserField.class);
        String realName;
        if(mapParserField!=null && !StringUtils.isEmpty(mapParserField.value())){
            realName = mapParserField.value();
        }else{
            realName = parser!=null?parser.getName(name):getKeyName(name);
        }
        return realName;
    }

}
