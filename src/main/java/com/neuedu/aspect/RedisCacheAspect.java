package com.neuedu.aspect;

import com.neuedu.common.ServerResponse;
import com.neuedu.json.ObjectMapperApi;
import com.neuedu.redis.Redisapi;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * redis缓存
 */
@Component
@Aspect
public class RedisCacheAspect {

    @Pointcut("execution(* com.neuedu.service.impl.IProductServiceimpl.list(..))")
    public void pointcut(){

    }

    @Autowired
    Redisapi redisapi;
    @Autowired
    ObjectMapperApi objectMapperApi;
    @Around("pointcut()")
    public Object arround(ProceedingJoinPoint joinPoint){

        Object o =null;
        try {
            StringBuffer keyBuffer=new StringBuffer();
            //全类名+方法名+参数
            String className = joinPoint.getTarget().getClass().getName();
            keyBuffer.append(className);
            String methodName = joinPoint.getSignature().getName();
            keyBuffer.append(methodName);
            Object[] objects = joinPoint.getArgs();
            if(objects!=null) {
                for (Object arg : objects) {
                    System.out.println("arg" + arg);
                    keyBuffer.append(arg);
                }
            }
            String key=keyBuffer.toString();
            String json = redisapi.get(key);
            if(json!=null&&!json.equals("")){
                System.out.println("读取到了缓存");
                return objectMapperApi.str2Obj(json, ServerResponse.class);
            }
            //执行目标方法
            o = joinPoint.proceed();
            System.out.println("读取数据库");
            if(o!=null){
                String jsoncache = objectMapperApi.obj2str(o);
                redisapi.set(key,jsoncache);
                System.out.println("数据库内容写入缓存");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return o;
    }
}
