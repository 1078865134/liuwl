package com.neuedu.controllerresuful.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 购物车控制类
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    /**
     * 购物车添加和更新商品
     */
    @RequestMapping("/add.do/{productId}/{count}")
    public ServerResponse add(HttpSession session,
                              @PathVariable Integer productId,
                              @PathVariable Integer count){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     *购物车列表
     */
    @RequestMapping("/list.do")
    public ServerResponse list(HttpSession session){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.list(userInfo.getId());
    }

    /**
     *更新购物车产品数量
     */
    @RequestMapping("/update.do/{productId}/{count}")
    public ServerResponse update(HttpSession session,
                                 @PathVariable Integer productId,
                                 @PathVariable Integer count){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /**
     *移除购物车中商品
     */
    @RequestMapping("/delete_product.do/{productIds}")
    public ServerResponse delete_product(HttpSession session,
                                         @PathVariable String productIds){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }

    /**
     *购物车选中某个商品
     */
    @RequestMapping("/select.do/{productId}")
    public ServerResponse select(HttpSession session,
                                 @PathVariable Integer productId){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     *购物车取消选中某个商品
     */
    @RequestMapping("/un_select.do/{productId}")
    public ServerResponse un_select(HttpSession session,
                                    @PathVariable Integer productId){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     *购物车全选
     */
    @RequestMapping("/select_all.do")
    public ServerResponse select_all(HttpSession session){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     *购物车取消全选
     */
    @RequestMapping("/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     *查询在购物车里的产品数量
     */
    @RequestMapping("/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){

        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return cartService.get_cart_product_count(userInfo.getId());
    }

}
