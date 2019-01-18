package com.neuedu.service.impl;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class IUserServiceimpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 登录
     */
    @Override
    public ServerResponse login(String username, String password) {

        //参数非空校验
        if (username==null||username.equals("")){
            return ServerResponse.serverResponseByERROR("用户名不能为空");
        }
        if (password==null||password.equals("")){
            return ServerResponse.serverResponseByERROR("密码不能为空");
        }

        //检验用户名是否存在
        int i = userInfoMapper.checkusername(username);
        if (i==0) {
            return ServerResponse.serverResponseByERROR("用户名不存在");
        }

        //查询用户信息
        UserInfo userInfo = userInfoMapper.selectuserInfoByusernameAndpassword(username,MD5Utils.getMD5Code(password));

        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("密码错误");
        }

        //返回结果
        userInfo.setPassword("");
        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    /**
     * 注册
     */
    @Override
    public ServerResponse register(UserInfo userInfo) {

        //参数非空校验
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("参数必须存在");
        }

        //校验用户名
        int i = userInfoMapper.checkusername(userInfo.getUsername());
        if(i>0){
            return ServerResponse.serverResponseByERROR("用户名已存在");
        }

        //校验邮箱
        int checkEmail = userInfoMapper.checkEmail(userInfo.getEmail());
        if(checkEmail>0){
            return ServerResponse.serverResponseByERROR("邮箱已存在");
        }

        //用户身份
        userInfo.setRole(Const.RoleEnum.ROLE_COSTOMER.getCode());
        //密码加密
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));

        //注册
        int insert = userInfoMapper.insert(userInfo);
        if(insert>0){
            return ServerResponse.createServerResponseBySuccess("注册成功");
        }
        return ServerResponse.serverResponseByERROR("注册失败");
    }

    /**
     * 找回密码（获取密保问题）
     */
    @Override
    public ServerResponse forget_get_question(String username) {

        //参数校验
        if (username==null||username.equals("")){
            return ServerResponse.serverResponseByERROR("用户名不能为空");
        }

        //检验用户名
        int i = userInfoMapper.checkusername(username);
        if (i==0) {
            return ServerResponse.serverResponseByERROR("用户名不存在,请重新输入");
        }

        //查找密保问题
        String question = userInfoMapper.selectQuestionByUsername(username);
        if(question==null||question.equals("")){
            return ServerResponse.serverResponseByERROR("密保问题为空");
        }
        return ServerResponse.createServerResponseBySuccess(question);
    }

    /**
     * 找回密码（提交答案）
     */
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {

        //参数校验
        if (username==null||username.equals("")){
            return ServerResponse.serverResponseByERROR("用户名不能为空");
        }
        if (question==null||question.equals("")){
            return ServerResponse.serverResponseByERROR("问题不能为空");
        }
        if (answer==null||answer.equals("")){
            return ServerResponse.serverResponseByERROR("答案不能为空");
        }

        //根据用户名、问题、答案查找
        int answers = userInfoMapper.selectByUsernameQuestionAnswer(username,question,answer);
        if(answers==0){
            return ServerResponse.serverResponseByERROR("答案错误");
        }
        //服务端生成token保存并返回给客户端
        String forgetToken = UUID.randomUUID().toString();
        //guava cache
        TokenCache.set(username,forgetToken);

        return ServerResponse.createServerResponseBySuccess(forgetToken);
    }

    /**
     * 找回密码（重置密码）
     */
    @Override
    public ServerResponse forget_reset_password(String username, String passwordnew, String forgetToken) {

        //参数校验
        if (username==null||username.equals("")){
            return ServerResponse.serverResponseByERROR("用户名不能为空");
        }
        if (passwordnew==null||passwordnew.equals("")){
            return ServerResponse.serverResponseByERROR("密码不能为空");
        }
        if (forgetToken==null||forgetToken.equals("")){
            return ServerResponse.serverResponseByERROR("答案不能为空");
        }

        //token校验
        String token = TokenCache.get(username);
        if(token==null){
            return ServerResponse.serverResponseByERROR("token过期");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.serverResponseByERROR("无效token");
        }

        //修改密码
        int i = userInfoMapper.updateUsernamePassword(username,MD5Utils.getMD5Code(passwordnew));
        System.out.println(i);
        if(i>0){
            return ServerResponse.createServerResponseBySuccess("修改成功");
        }
        return ServerResponse.serverResponseByERROR("修改失败");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //参数校验
        if (str==null||str.equals("")){
            return ServerResponse.serverResponseByERROR("用户名或邮箱不能为空");
        }
        if (type==null||type.equals("")){
            return ServerResponse.serverResponseByERROR("校验类型不能为空");
        }
        //校验用户名和邮箱是否存在
        if(type.equals("username")){
            int checkusername = userInfoMapper.checkusername(str);
            if(checkusername>0){
                return ServerResponse.serverResponseByERROR("用户名已存在");
            }else{
                return ServerResponse.createServerResponseBySuccess();
            }
        }else if(type.equals("email")){
            int checkEmail = userInfoMapper.checkEmail(str);
            if(checkEmail>0){
                return ServerResponse.serverResponseByERROR("邮箱已存在");
            }else{
                return ServerResponse.createServerResponseBySuccess();
            }
        }else {
            return ServerResponse.serverResponseByERROR("参数类型错误");
        }
    }

    @Override
    public ServerResponse reset_password(String username,String passwordold, String passwordnew) {
        //参数校验
        if (passwordold==null||passwordold.equals("")){
            return ServerResponse.serverResponseByERROR("原密码不能为空");
        }
        if (passwordnew==null||passwordnew.equals("")){
            return ServerResponse.serverResponseByERROR("新密码不能为空");
        }
        //查找用户名和原密码
        UserInfo userInfo = userInfoMapper.selectuserInfoByusernameAndpassword(username, MD5Utils.getMD5Code(passwordold));
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("原密码错误");
        }
        //修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordnew));
        int i = userInfoMapper.updateByPrimaryKey(userInfo);
        if(i>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("修改密码失败");
    }

    @Override
    public ServerResponse update_information(UserInfo user) {

        //参数校验
        if (user==null||user.equals("")){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        //修改用户信息
        int i = userInfoMapper.updateUserByEmailPhoneQuestionAnswer(user);
        if(i>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("修改信息失败");
    }
}
