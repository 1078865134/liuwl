package com.neuedu.interceptor;

import com.neuedu.interceptor.AutoLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册拦截器类
 */
@SpringBootApplication
public class RegisterInterceptor implements WebMvcConfigurer {

    @Autowired//注入拦截器
    AutoLoginInterceptor autoLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建放行集合
        List<String> excludeList = new ArrayList<>();
        excludeList.add("/manage/user/login.do");
        excludeList.add("/manage/user/register.do");
        excludeList.add("/manage/user/logout.do");
        //依次是注册拦截器、拦截什么、放行什么
        registry.addInterceptor(autoLoginInterceptor).addPathPatterns("/manage/**").excludePathPatterns(excludeList);
    }
}
