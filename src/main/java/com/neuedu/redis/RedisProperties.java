package com.neuedu.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 连接池配置
 */
@Data
@Component
@ConfigurationProperties
public class RedisProperties {


    @Value("${redis.max.total}")
    private int maxTotal;//最大连接数

    @Value("${redis.max.idle}")
    private int maxIdle;//最大空闲数

    @Value("${redis.max.total}")
    private int minIdle;//最小空闲数

    @Value("${redis.ip}")
    private String redisIp;//ip

    @Value("${redis.port}")
    private int redisPort;//port

    @Value("${redis.test.borrow}")
    private boolean testBorrow;//在获取实例时，校验实例是否有效

    @Value("${redis.test.return}")
    private boolean testReturn;//在把jedis实例放回到连接池是，检查实例是否有效

    @Value("${redis.password}")
    private String redisPassword;//密码
}
