package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IAddressService {

    ServerResponse add(Integer userId,Shipping shipping);

    ServerResponse del(Integer userId,Integer shippingId);

    ServerResponse update(Shipping shipping);

    ServerResponse select(Integer shippingId);

    ServerResponse list(Integer pagenum,Integer pagesize);
}
