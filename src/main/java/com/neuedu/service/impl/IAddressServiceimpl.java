package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public class IAddressServiceimpl implements IAddressService {

    @Autowired
    ShippingMapper shippingMapper;

    /**
     * 添加订单
     */
    @Override
    public ServerResponse add(Integer userId,Shipping shipping) {
        if(shipping==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        HashMap<String, Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess(map);
    }

    /**
     * 删除订单
     */
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        int i = shippingMapper.deleteByUserIdshipping(userId,shippingId);
        if(i>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("删除失败");
    }

    /**
     * 修改订单信息
     */
    @Override
    public ServerResponse update(Shipping shipping) {
        if(shipping==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        int i = shippingMapper.updateBySelectKey(shipping);
        if(i>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("更新失败");
    }

    /**
     * 查询订单信息
     */
    @Override
    public ServerResponse select(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        return ServerResponse.createServerResponseBySuccess(shipping);
    }

    /**
     * 查询订单信息
     */
    @Override
    public ServerResponse list(Integer pagenum, Integer pagesize) {
        PageHelper.startPage(pagenum,pagesize);
        List<Shipping> shippings = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
