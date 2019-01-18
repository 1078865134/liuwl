package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface IProductService {

    ServerResponse saveOrUpdate(Product product);

    ServerResponse set_sale_status(Integer id,Integer status);

    ServerResponse detail(Integer id);

    ServerResponse list(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                        @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize);

    ServerResponse search(@RequestParam(value = "productId",required = false) Integer productId,
                          @RequestParam(value = "productName",required = false) String productName,
                          @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize);

    ServerResponse upload(MultipartFile file,String path);

    ServerResponse detail_portal (Integer productId);

    ServerResponse list_protal(Integer categoryId,
                        String keyword,
                        Integer pageNum,
                        Integer pageSize,
                        String orderBy);
}
