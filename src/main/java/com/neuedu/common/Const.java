package com.neuedu.common;

/**
 * 常量类
 */
public class Const {

    /**
     *当前用户的key（登录）
     */
    public static final String CURRENTUSER="current_user";

    public static final  String TRADE_SUCCESS="TRADE_SUCCESS";

    public static final  String AUTOLOGINKEN="autoLoginToken";

    /**
     * 支付类型
     */
    public enum PaymentPlatformEnum{

        ALIPAY(1,"支付宝")
        ;
        private int code;
        private String desc;

        PaymentPlatformEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

    /**
     * 支付类型
     */
    public enum PaymentEnum{

        ONLINE(1,"在线支付")
        ;
        private int code;
        private String desc;

        PaymentEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        //遍历枚举
        public static PaymentEnum codeOf(Integer code){
            for(PaymentEnum paymentEnum:values()){
                if(code==paymentEnum.getCode()){
                    return paymentEnum;
                }
            }
            return null;
        }
    }

    /**
     * 订单状态
     */
    public enum OrderStatusEnum{

        ORDER_CANCELED(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSED(60,"交易关闭"),
        ;
        private int code;
        private String desc;

        OrderStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        //遍历枚举
        public static OrderStatusEnum codeOf(Integer code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if(code==orderStatusEnum.getCode()){
                    return orderStatusEnum;
                }
            }
            return null;
        }
    }


    /**
     * 购物车选中
     */
    public enum CartCheckEnum{

        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选")
        ;
        private int code;
        private String desc;

        CartCheckEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 商品上下架状态
     */
    public enum ProductStatusEnum{

        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;
        private int code;
        private String desc;

        ProductStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 类别-用户状态码
     */
    public enum ResponceCodeEnum{

        NEED_LOGIN(2,"需要登录"),
        NO_PRIVILEGE(3,"无权限操作")
        ;
        private int code;
        private String desc;

        ResponceCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * role用户身份常量（注册）
     */
    public enum RoleEnum{

        ROLE_ADMIN(0,"管理员"),
        ROLE_COSTOMER(1,"普通用户");

        private int code;
        private String desc;

        RoleEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
