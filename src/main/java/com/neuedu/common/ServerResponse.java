package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 定义响应前端的高复用对象
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    private Integer status;//状态码
    private T date;//状态码为0是成功
    private String msg;//提示信息

    private ServerResponse() {
    }

    private ServerResponse(Integer status) {
        this.status = status;
    }

    public ServerResponse(Integer status, T date) {
        this.status = status;
        this.date = date;
    }

    private ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(Integer status,  String msg,T date) {
        this.status = status;
        this.date = date;
        this.msg = msg;
    }

    /**
     * 返回状态码
     * @return
     */
    public static ServerResponse createServerResponseBySuccess(){
        return new ServerResponse(Responsecode.SUCCESS);
    }

    /**
     * 返回状态码，成功数据
     */
    public static <T> ServerResponse createServerResponseBySuccess(T date){
        return new ServerResponse(Responsecode.SUCCESS,date);
    }

    /**
     * 返回状态码，信息和成功数据
     */
    public static <T> ServerResponse createServerResponseBySuccess(String msg,T date){
        return new ServerResponse(Responsecode.SUCCESS,msg,date);
    }

    /**
     * 返回状态码
     * @param status
     * @return
     */
    public static ServerResponse serverResponseByERROR(Integer status){
        return new ServerResponse(status);
    }

    /**
     * 返回提示信息
     * @param msg
     * @return
     */
    public static ServerResponse serverResponseByERROR(String msg){
        return new ServerResponse(Responsecode.ERROR,msg);
    }

    /**
     * 返回自定义状态码和提示信息
     * @param status
     * @param msg
     * @return
     */
    public static ServerResponse serverResponseByERROR(Integer status,String msg){
        return new ServerResponse(status,msg);
    }

    /**
     * 判断接口是否访问成功
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==Responsecode.SUCCESS;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
