package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;

import javax.servlet.http.HttpSession;

public interface ICategoryService {

    /**
     * 获取品类子节点(平级)
     */
    public ServerResponse get_category(Integer categoryid);

    /**
     * 增加节点
     */
    public ServerResponse add_category(Integer parentid,String categoryname);

    /**
     * 修改节点
     */
    public ServerResponse set_category_name(Integer categoryid,String categoryname);

    /**
     * 获取当前分类id及递归子节点categoryId
     */
    public ServerResponse get_deep_category(Integer categoryid);

}
