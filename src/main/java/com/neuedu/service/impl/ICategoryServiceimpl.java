package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
@Service
public class ICategoryServiceimpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse get_category(Integer categoryid) {
        //非空校验
        if(categoryid==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        //根据id查询类别
        Category category = categoryMapper.selectByPrimaryKey(categoryid);
        if(category==null){
            return ServerResponse.serverResponseByERROR("查询类别不存在");
        }
        //查询子类别
        List<Category> categories = categoryMapper.fingchildCategory(categoryid);
        //返回结果
        return ServerResponse.createServerResponseBySuccess(categories);
    }

    @Override
    public ServerResponse add_category(Integer parentid, String categoryname) {
        //非空校验
        if(categoryname==null){
            return ServerResponse.serverResponseByERROR("类别名称不能为空");
        }
        //增加节点
        Category category = new Category();
        category.setParentId(parentid);
        category.setName(categoryname);
        //设置类别状态（1正常2废弃）
        category.setStatus(1);
        int insert = categoryMapper.insert(category);
        if(insert>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("添加失败");
    }

    @Override
    public ServerResponse set_category_name(Integer categoryid, String categoryname) {
        //非空校验
        if(categoryid==null){
            return ServerResponse.serverResponseByERROR("类别id不能为空");
        }
        if(categoryname==null){
            return ServerResponse.serverResponseByERROR("类别名称不能为空");
        }
        //修改节点
        Category category = categoryMapper.selectByPrimaryKey(categoryid);
        if(category==null){
            return ServerResponse.serverResponseByERROR("修改的类不存在");
        }
        category.setName(categoryname);
        int update = categoryMapper.updateByPrimaryKey(category);
        if(update>0){
            //修改成功
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("修改失败");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryid) {
        //非空校验
        if(categoryid==null){
            return ServerResponse.serverResponseByERROR("类别id不能为空");
        }
        //查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet= findAllChildCategory(categorySet,categoryid);

        Set<Integer> integerSet = Sets.newHashSet();

        Iterator<Category> iterator = categorySet.iterator();
        while (iterator.hasNext()){
            Category category = iterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.createServerResponseBySuccess(integerSet);
    }
    private Set<Category> findAllChildCategory(Set<Category> categoryset,Integer categoryid){
        Category category = categoryMapper.selectByPrimaryKey(categoryid);
        if(category!=null){
            categoryset.add(category);
        }
        List<Category> categoryList = categoryMapper.fingchildCategory(categoryid);
        if(categoryList!=null&&categoryList.size()>0){
            for(Category category1:categoryList){
                findAllChildCategory(categoryset,category1.getId());//递归
            }
        }
        return categoryset;
    }
}
