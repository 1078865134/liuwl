package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    /**
     * 创建订单
     */
    ServerResponse create(Integer userId,Integer shippingId);

    /**
     * 取消订单
     */
    ServerResponse cancel(Integer userId,Long orderNo);

    /**
     * 获取订单的商品信息
     */
    ServerResponse get_order_cart_product(Integer userId);

    /**
     * 订单分页
     */
    ServerResponse list(Integer userId,Integer pagenum,Integer pagesize);

    /**
     * 订单详情
     */
    ServerResponse detail(Integer userId,Long orderNo);

    /**
     * 支付
     */
    ServerResponse pay(Integer userId,Long orderNo);

    /**
     * 查询订单支付状态
     */
    ServerResponse query_order_pay_status(Long orderNo);

    /**
     * 支付宝回调
     */
    ServerResponse alipay_callback(Map<String,String> map);

}
