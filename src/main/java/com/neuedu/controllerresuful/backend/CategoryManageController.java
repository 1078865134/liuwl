package com.neuedu.controllerresuful.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台类别类
 */
@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    ICategoryService categoryService;

    /**
     * 获取品类子节点（平级）
     */
    @RequestMapping(value = "/get_category.do/{categoryid}")
    public ServerResponse get_category(HttpSession session,
                                       @PathVariable Integer categoryid){

        return categoryService.get_category(categoryid);
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_category.do/{parentid}/{categoryname}")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue ="0" )
                                       @PathVariable Integer parentid,
                                       @PathVariable String categoryname){

        return categoryService.add_category(parentid,categoryname);
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "/set_category_name.do/{categoryid}/{categoryname}")
    public ServerResponse set_category_name(HttpSession session,
                                            @PathVariable Integer categoryid,
                                            @PathVariable String categoryname){

        return categoryService.set_category_name(categoryid,categoryname);
    }
    /**
     * 获取当前分类id及递归子节点categoryId
     */
    @RequestMapping(value = "/get_deep_category.do/{categoryid}")
    public ServerResponse get_deep_category(HttpSession session,
                                            @PathVariable Integer categoryid){

        return categoryService.get_deep_category(categoryid);
    }
}
