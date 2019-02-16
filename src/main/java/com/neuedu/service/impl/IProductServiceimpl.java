package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.FTPUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vi.ProductDetailVO;
import com.neuedu.vi.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class IProductServiceimpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryService categoryService;


    /**
     * 添加和更新商品
     */
    @Override
    public ServerResponse saveOrUpdate(Product product) {

        //非空校验
        if(product==null){
            return ServerResponse.serverResponseByERROR("参数为空");
        }
        //设置主图
        String subImages = product.getSubImages();
        if(subImages!=null&&subImages.equals("")){
            String[] subImagesarr = subImages.split(",");
            if(subImagesarr.length>0){
                product.setMainImage(subImagesarr[0]);
            }
        }
        if(product.getId()==null){
            int insert = productMapper.insert(product);
            if(insert>0){
                return ServerResponse.createServerResponseBySuccess();
            }else{
                return ServerResponse.serverResponseByERROR("添加失败");
            }
        }else{
            int i = productMapper.updateByPrimaryKey(product);
            if(i>0){
                return ServerResponse.createServerResponseBySuccess();
            }else{
                return ServerResponse.serverResponseByERROR("更新失败");
            }
        }
    }

    /**
     * 产品上架
     */
    @Override
    public ServerResponse set_sale_status(Integer id,Integer status) {
        //非空校验
        if(id==null){
            return ServerResponse.serverResponseByERROR("id不能为空");
        }
        if(status==null){
            return ServerResponse.serverResponseByERROR("商品状态不能为空");
        }
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        int statusByid = productMapper.updateProductKeySelective(product);
        if(statusByid!=0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.serverResponseByERROR("产品上架失败");
    }

    /**
     * 商品详情
     */
    @Override
    public ServerResponse detail(Integer id) {
        if(id==null){
            return ServerResponse.serverResponseByERROR("id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if(product==null){
            return ServerResponse.serverResponseByERROR("商品不存在");
        }
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }
    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        //date转换成string（工具类）
        productDetailVO.setCreateTime(DateUtils.dateToString(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        //读取配置文件工具类
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        //date转换成string（工具类）
        productDetailVO.setUpdateTime(DateUtils.dateToString(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else{
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }

    /**
     * 分页查询
     */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectAll();
        ArrayList<ProductListVO> productListVOList = Lists.newArrayList();
        if(products!=null&&products.size()>0){
            for (Product product:products){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setId(product.getId());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    /**
     * 产品搜索
     */
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(productName!=null&&productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> products = productMapper.findProductByProductIdAndProductname(productId,productName);
        ArrayList<ProductListVO> productListVOList = Lists.newArrayList();
        if(products!=null&&products.size()>0){
            for (Product product:products){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if(file==null){
            return ServerResponse.serverResponseByERROR("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String exName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + exName;
        File pathFile = new File(path);
        if(!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }
        File file1 = new File(path, newFileName);
        try {
            file.transferTo(file1);
            //上传图片到服务器
            FTPUtil.uploadFile(Lists.newArrayList(file1));
            HashMap<String, String> map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            file1.delete();
            return ServerResponse.createServerResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *前台查询商品详情
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        //非空判断
        if(productId==null){
            return ServerResponse.serverResponseByERROR("id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseByERROR("商品不存在");
        }
        //判断是否是在售状态
        if(product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.serverResponseByERROR("商品已下架或删除");
        }
        //获取productDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    /**
     * 产品搜索及动态排序List
     */
    @Override
    public ServerResponse list_protal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if(categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.serverResponseByERROR("参数错误");
        }
        Set<Integer> integerSet = Sets.newHashSet();
        if(categoryId!=null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && (keyword == null || keyword.equals(""))) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySuccess(pageInfo);
            }
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);
            if (serverResponse.isSuccess()) {
                integerSet = (Set<Integer>) serverResponse.getDate();
            }
        }
        if (keyword != null && keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        if(orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else{
            String[] orderByArr = orderBy.split("_");
            if(orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else {
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        List<Product> productList = productMapper.searchProduct(integerSet, keyword);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList != null && productList.size() > 0) {
            for (Product product : productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
