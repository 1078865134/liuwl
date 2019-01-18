package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    public ServerResponse login(HttpSession session, String username, String password){

        ServerResponse serverResponse = userService.login(username, password);
        if(serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo)serverResponse.getDate();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }


}
