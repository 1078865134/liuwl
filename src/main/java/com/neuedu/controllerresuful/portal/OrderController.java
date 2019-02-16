package com.neuedu.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * 前台订单接口
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    /**
     * 创建订单
     */
    @RequestMapping("/create.do")
    public ServerResponse create(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.create(userInfo.getId(),shippingId);
    }

    /**
     * 取消订单
     */
    @RequestMapping("/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     * 获取订单的商品信息
     */
    @RequestMapping("/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     * 订单分页
     */
    @RequestMapping("/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pagenum,
                               @RequestParam(required = false,defaultValue = "10")Integer pagesize){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.list(userInfo.getId(),pagenum,pagesize);
    }

    /**
     * 订单详情
     */
    @RequestMapping("/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.detail(userInfo.getId(),orderNo);
    }

    /**
     * 支付
     */
    @RequestMapping("/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.pay(userInfo.getId(),orderNo);
    }

    /**
     * 查询订单支付状态
     */
    @RequestMapping("/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByERROR("需要登录");
        }
        return orderService.query_order_pay_status(orderNo);
    }

    /**
     * 支付宝回调
     */
    @RequestMapping("/alipay_callback.do")
    public ServerResponse alipay_callback(HttpServletRequest request){
//        public String alipay_callback(){

        System.out.println("======支付宝服务器回调应用服务器接口=======");
        Map<String,String[]> params=request.getParameterMap();
        Map<String,String> requestparams= Maps.newHashMap();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String[] strArr = params.get(key);
            String value="";
            for(int i=0;i<strArr.length;i++) {
                value=(i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";
            }
            requestparams.put(key,value);
        }

        try {
            requestparams.remove("sign_type");
            boolean result= false;
            result = AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!result){
                return ServerResponse.serverResponseByERROR("非法请求,验证不通过");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return orderService.alipay_callback(requestparams);
//        return null;
    }
}
