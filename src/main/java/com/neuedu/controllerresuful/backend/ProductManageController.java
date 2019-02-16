package com.neuedu.controllerresuful.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;
    /**
     * 新增或更新产品
     */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate(HttpSession session, Product product){

        return productService.saveOrUpdate(product);
    }

    /**
     * 产品上架
     */
    @RequestMapping(value = "/set_sale_status.do/{id}/{status}")
    public ServerResponse set_sale_status(HttpSession session,
                                          @PathVariable Integer id,
                                          @PathVariable Integer status){

        return productService.set_sale_status(id,status);
    }

    /**
     * 查询商品详情
     */
    @RequestMapping(value = "/detail.do/{id}")
    public ServerResponse detail(HttpSession session,
                                 @PathVariable Integer id){

        return productService.detail(id);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/list.do/{pageNum}/{pageSize}")
    public ServerResponse list(HttpSession session,
                                 @RequestParam(value = "pageNum",required = false,defaultValue = "1")
                                 @PathVariable Integer pageNum,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "10")
                                 @PathVariable Integer pageSize){


        return productService.list(pageNum,pageSize);
    }

    /**
     *产品搜索
     */
    @RequestMapping(value = "/search.do/{productId}/{productName}/{pageNum}/{pageSize}")
    public ServerResponse search(HttpSession session,
                               @RequestParam(value = "productId",required = false)
                               @PathVariable Integer productId,
                               @RequestParam(value = "productName",required = false)
                               @PathVariable String productName,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")
                               @PathVariable Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")
                               @PathVariable Integer pageSize){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NEED_LOGIN.getCode(),Const.ResponceCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否有权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByERROR(Const.ResponceCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponceCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return productService.search(productId,productName,pageNum,pageSize);
    }
}
