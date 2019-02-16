package com.neuedu.controllerresuful.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class Usercontroller {

    @Autowired
    IUserService userService;
    /**
     * 用户登录
     */
    @RequestMapping(value = "/login.do/{username}/{password}")
    public ServerResponse login(HttpServletResponse response,
                                 HttpSession session,
                                @PathVariable String username,
                                @PathVariable String password){

        ServerResponse serverResponse = userService.login(username, password);
        if(serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo)serverResponse.getDate();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }


        return serverResponse;
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(HttpSession session,UserInfo userInfo){

        ServerResponse serverResponse = userService.register(userInfo);
        return serverResponse;
    }

    /**
     * 根据用户名查问题
     */
    @RequestMapping(value = "/forget_get_question.do/{username}")
    public ServerResponse forget_get_question(@PathVariable String username){

        ServerResponse serverResponse = userService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 校验答案
     */
    @RequestMapping(value = "/forget_check_answer.do/{username}/{question}/{answer}")
    public ServerResponse forget_check_answer(@PathVariable String username,
                                              @PathVariable String question,
                                              @PathVariable String answer){

        ServerResponse serverResponse = userService.forget_check_answer(username,question,answer);
        return serverResponse;
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/forget_reset_password.do/{username}/{passwordnew}/{forgetToken}")
    public ServerResponse forget_reset_password(@PathVariable String username,
                                                @PathVariable String passwordnew,
                                                @PathVariable String forgetToken){

        ServerResponse serverResponse = userService.forget_reset_password(username,passwordnew,forgetToken);
        return serverResponse;
    }

    /**
     * 校验用户名和邮箱是否存在
     */
    @RequestMapping(value = "/check_valid.do/{str}/{type}")
    public ServerResponse check_valid(@PathVariable String str,
                                      @PathVariable String type){

        ServerResponse serverResponse = userService.check_valid(str,type);
        return serverResponse;
    }

    /**
     * 获取登录用户信息
     */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null) {
            return ServerResponse.serverResponseByERROR("用户未登录");
        }
        userInfo.setPassword("");
        userInfo.setQuestion("");
        userInfo.setAnswer("");
        userInfo.setRole(null);
        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    /**
     * 登录状态修改密码
     */
    @RequestMapping(value = "/reset_password.do/{passwordold}/{passwordnew}")
    public ServerResponse reset_password(HttpSession session,
                                         @PathVariable String passwordold,
                                         @PathVariable String passwordnew){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null) {
            return ServerResponse.serverResponseByERROR("用户未登录");
        }
        return userService.reset_password(userInfo.getUsername(),passwordold,passwordnew);
    }

    /**
     * 登录状态修改用户信息
     */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null) {
            return ServerResponse.serverResponseByERROR("用户未登录");
        }
        user.setId(userInfo.getId());
        return userService.update_information(user);
    }

    /**
     *获取用户详细信息
     */
    @RequestMapping(value = "/get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null) {
            return ServerResponse.serverResponseByERROR("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createServerResponseBySuccess();
    }
}
