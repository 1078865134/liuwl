package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台类别类
 */
@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    ICategoryService categoryService;

    /**
     * 获取品类子节点（平级）
     */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryid){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NEED_LOGIN.getCode(),Const.ResponceCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
           return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponceCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return categoryService.get_category(categoryid);
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue ="0" ) Integer parentid,
                                       String categoryname){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NEED_LOGIN.getCode(),Const.ResponceCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponceCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return categoryService.add_category(parentid,categoryname);
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                            Integer categoryid,
                                            String categoryname){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NEED_LOGIN.getCode(),Const.ResponceCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponceCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return categoryService.set_category_name(categoryid,categoryname);
    }
    /**
     * 获取当前分类id及递归子节点categoryId
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,
                                            Integer categoryid){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NEED_LOGIN.getCode(),Const.ResponceCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponceCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return categoryService.get_deep_category(categoryid);
    }
}
