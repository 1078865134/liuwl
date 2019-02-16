package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.redis.RedisProperties;
import com.neuedu.redis.Redisapi;
import com.neuedu.service.IUserService;
import com.neuedu.utils.IpUtils;
import com.neuedu.utils.MD5Utils;
import com.sun.deploy.net.HttpResponse;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 后台用户管理控制类
 */
@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    IUserService userService;


    /**
     * 用户登录
     */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpServletResponse response, HttpServletRequest request, HttpSession session, String username, String password){

        ServerResponse serverResponse = userService.login(username, password);
        if(serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo)serverResponse.getDate();
            if(userInfo.getRole()==Const.RoleEnum.ROLE_COSTOMER.getCode()){
                return ServerResponse.serverResponseByERROR("无权限登录");

            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
            //自动登录
            String ip = IpUtils.getRemoteAddress(request);
            String mac = null;
            try {
                mac = IpUtils.getMACAddress(ip);
                String token = MD5Utils.getMD5Code(mac);
                //保存到硬盘中
                userService.updateTokenByUserid(userInfo.getId(),token);
                Cookie cookie = new Cookie(Const.AUTOLOGINKEN, token);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*7);//7天免登录
                response.addCookie(cookie);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return serverResponse;
    }
}
