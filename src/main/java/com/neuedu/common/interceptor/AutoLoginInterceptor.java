package com.neuedu.common.interceptor;

import com.google.gson.Gson;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截器
 */
@Component
public class AutoLoginInterceptor implements HandlerInterceptor {

    @Autowired
    IUserService userService;
    @Override//（请求拦截器）
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        System.out.println("=======preHandle========");

//        拦截某个特定的类的方法
//        HandlerMethod handlerMethod=(HandlerMethod)handler;
////        类名
//        String className = handlerMethod.getBean().getClass().getSimpleName();
////        方法名
//        String methodname = handlerMethod.getMethod().getName();
//        if(className.equals("ProductManageController")&&methodname.equals("list")){
////            return true;
//        }


        HttpSession session=request.getSession();
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);

        if(userInfo==null){
            Cookie[] cookies = request.getCookies();
            if(cookies!=null&&cookies.length>0){
                for(Cookie cookie:cookies){
                    String cookiename = cookie.getName();
                    if(cookiename.equals(Const.AUTOLOGINKEN)){
                        String token = cookie.getValue();
                        userInfo=userService.findUserinfoByToken(token);
                        if(userInfo!=null){
                            session.setAttribute(Const.CURRENTUSER,userInfo);
                        }
                        break;
                    }
                }
            }
        }

//        重构httpserverletresponse
        if(userInfo==null||userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            response.reset();
            response.setContentType("text/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            if(userInfo==null){
                ServerResponse serverResponse = ServerResponse.serverResponseByERROR("用户未登录");
                Gson gson = new Gson();
                String json = gson.toJson(serverResponse);
                writer.write(json);
                writer.flush();//刷新
                writer.close();
            }else{
                ServerResponse serverResponse = ServerResponse.serverResponseByERROR("用户无权限");
                Gson gson = new Gson();
                String json = gson.toJson(serverResponse);
                writer.write(json);
                writer.flush();
                writer.close();
            }
            return false;
        }
        return true; //拦截请求（请求通过）

    }

    @Override//（响应拦截器）
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("=======postHandle========");
    }

    @Override//（请求并相应后执行的拦截器）
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("=======afterCompletion========");
    }
}
