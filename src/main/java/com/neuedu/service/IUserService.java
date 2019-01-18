package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;

public interface IUserService {

    /**
     * 登录接口
     */
    ServerResponse login(String username, String password);

    /**
     * 注册接口
     */
    ServerResponse register(UserInfo userInfo);

    /**
     * 根据用户名查问题
     */
    ServerResponse forget_get_question(String username);

    /**
     * 提交问题答案
     */
    ServerResponse forget_check_answer(@Param("username") String username,
                                       @Param("question") String question,
                                       @Param("answer") String answer);

    /**
     * 重置密码
     */
    ServerResponse forget_reset_password(String username,
                                         String passwordnew,
                                         String forgetToken);

    /**
     * 校验用户名和邮箱是否存在
     */
    ServerResponse check_valid(String str,String type);

    /**
     * 登录状态修改密码
     */
    ServerResponse reset_password(String username,String passwordold,String passwordnew);

    /**
     * 登录状态修改用户信息
     */
    ServerResponse update_information(UserInfo user);
}
