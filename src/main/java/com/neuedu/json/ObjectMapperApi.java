package com.neuedu.json;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * json转化工具类方法
 */
@Component
public class ObjectMapperApi {
    @Autowired
    ObjectMapper objectMapper;
    /**
     * java对象转字符串未格式化
     */
    public <T> String obj2str(T t){
        if(t==null){
            return null;
        }
        try {
            return t instanceof String ? (String)t:objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * java对象转字符串已格式化后
     */
    public <T> String obj2strpretty(T t){
        if(t==null){
            return null;

        }
        try {
            return t instanceof String ?(String)t:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转成java对象string--object
     */
    public <T> T str2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str)||clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数组转java集合
     */
    public <T> T str2Objlist(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }
        try {
            return typeReference.getType().equals(String.class)?(T)str: (T) objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
