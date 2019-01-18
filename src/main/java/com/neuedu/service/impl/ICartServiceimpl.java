package com.neuedu.service.impl;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecialUtils;
import com.neuedu.vi.CartProductVO;
import com.neuedu.vi.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class ICartServiceimpl implements ICartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;
    /**
     * 购物车添加和更新商品
     */
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {

        if(productId==null||count==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseByERROR("添加商品不存在");
        }
        Cart cart = cartMapper.selectCartByUseridProductid(userId, productId);
        if(cart==null){
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    /**
     *购物车列表
     */
    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }
    private CartVO getCartVOLimit(Integer userId){

        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserid(userId);
        ArrayList<CartProductVO> cartProductVOList = Lists.newArrayList();
        BigDecimal carttotalprice = new BigDecimal("0");
        if(cartList!=null&&cartList.size()>0){
            for(Cart cart:cartList){
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(cart.getUserId());
                cartProductVO.setProductChecked(cart.getChecked());
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());

                    //定义库存量
                    int stock=product.getStock();
                    //定义每页商品量
                    int limitProductCount=0;
                    //判断库存量stock和购物车商品数量Quantity
                    if(stock>=cart.getQuantity()){
                        limitProductCount=cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");//有货
                    }else {
                        limitProductCount=stock;
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");//无货
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecialUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));

                }
                if(cartProductVO.getProductChecked()==Const.CartCheckEnum.PRODUCT_CHECKED.getCode()){
                    carttotalprice=BigDecialUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCarttotalprice(carttotalprice);
        int checkedAll = cartMapper.isCheckedAll(userId);
//        1选中0未选中
        if(checkedAll>0){
            cartVO.setIsallchecked(false);
        }else {
            cartVO.setIsallchecked(true);
        }
        return cartVO;
    }
    /**
     * 购物车商品数量
     */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        if(productId==null||count==null){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        Cart cart = cartMapper.selectCartByUseridProductid(userId, productId);
        if(cart!=null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     * 购物车移除商品
     */
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        if(productIds==null||productIds.equals("")){
            return ServerResponse.serverResponseByERROR("参数不能为空");
        }
        ArrayList<Integer> productIdList = Lists.newArrayList();
        String[] productIdsArr = productIds.split(",");
        if(productIdsArr!=null&&productIdsArr.length>0){
            for (String productIdstr:productIdsArr){
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        cartMapper.deleteByUseridProductid(userId,productIdList);
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     * 购物车商品是否选中
     */
    @Override
    public ServerResponse select(Integer userId,Integer productId,Integer check) {

        cartMapper.selectOrUnselectProduct(userId,productId,check);
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     * 统计商品数量
     * @param userId
     * @return
     */
    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity = cartMapper.get_cart_product_count(userId);

        return ServerResponse.createServerResponseBySuccess(quantity);
    }
}
