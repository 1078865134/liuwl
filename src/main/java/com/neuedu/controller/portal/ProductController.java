package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 前台查询商品详情
     */
    @RequestMapping("/detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detail_portal(productId);
    }

    /**
     *产品搜索及动态排序List
     * 排序参数：例如price_desc，price_asc
     */
    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false ,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false ,defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false ,defaultValue = "") String orderBy){

        return productService.list_protal(categoryId,keyword,pageNum,pageSize,orderBy);
    }
}
