package com.neuedu.controller.scheduler;

import com.neuedu.service.IOrderService;
import com.neuedu.utils.PropertiesUtils;
import org.apache.commons.lang.time.DateUtils;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时关闭订单
 * */
@Component
public class CloseOrder {
    @Autowired
    IOrderService iOrderService;
    //定时关闭订单
    @Scheduled(cron = "0 */1 * * * *")
    public void closeOrder()
    {
        Integer hour = Integer.parseInt(PropertiesUtils.readByKey("close.order.time"));
        //时间处理的工具类,设置订单关闭时间
        //转成字符串类型
        String date=com.neuedu.utils.DateUtils.dateToStr(DateUtils.addHours(new Date(),-hour));
        //创建时间来查询订单
        iOrderService.closeOrder(date);
    }
}
