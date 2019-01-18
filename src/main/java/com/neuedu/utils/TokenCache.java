package com.neuedu.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * 基于guava cache的缓存工具类
 */
public class TokenCache {

    //构建本地缓存
    private static LoadingCache<String,String> loadingCache= CacheBuilder.newBuilder()
            .initialCapacity(1000)//初始化缓存项为1000
            .maximumSize(10000)//设置缓存最大值不超过10000
            .expireAfterAccess(12, TimeUnit.HOURS)//定时回收（最大过期时间12小时）
            .build(new CacheLoader<String, String>() {
                //当缓存没有值时执行load方法
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void set(String key,String value){
        loadingCache.put(key, value);
    }
    public static String get(String key){

        try {
            String value = loadingCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
