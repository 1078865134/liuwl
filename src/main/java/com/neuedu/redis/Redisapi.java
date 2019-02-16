package com.neuedu.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis重要方法
 */
@Component
public class Redisapi {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 设置set（key，value）
     */
    public String set(String key,String value){
        String result=null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            result=jedis.set(key,value);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 设置setex（key,second,value）过期时间
     */
    public String setex(String key,Integer second,String value){
        String result=null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            result=jedis.setex(key,second,value);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 获取get（key）
     */
    public String get(String key){
        String result=null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            result=jedis.get(key);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 删除 del（key）
     */
    public Long del(String key){
        Long result=null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            result=jedis.del(key);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 设置 expire（key）有效时间
     */
    public Long expire(String key,int second){
        Long result=null;
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            result=jedis.expire(key,second);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

}
