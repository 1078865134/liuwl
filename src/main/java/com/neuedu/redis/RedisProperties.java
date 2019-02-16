package com.neuedu.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class RedisProperties {

    //最大连接数
    @Value("${redis.max.total")
    private int maxTotal;
    //最大空闲数
    @Value("${redis.max.idle")
    private int maxIdle;
    //最小空闲数
    @Value("${redis.max.total")
    private int minIdle;
    //ip
    @Value("${redis.ip")
    private String redisIp;
    //port
    @Value("${redis.port")
    private int redisPort;
    //在获取实例时，校验实例是否有效
    @Value("${redis.test.borrow")
    private boolean testBorrow;
    //在把jedis实例放回到连接池是，检查实例是否有效
    @Value("${redis.test.return")
    private boolean testReturn;
    //密码
    @Value("${redis.password")
    private String redisPassword;
}
