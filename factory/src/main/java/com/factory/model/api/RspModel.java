package com.factory.model.api;

import java.util.Date;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午10:24
 * @description: 对应服务端的RspModel
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RspModel<T> {
    // 成功
    public static final int SUCCEED = 1;

    // 没有找到用户信息
    public static final int ERROR_NOT_FOUND_USER = 4041;

    // 请求参数错误
    public static final int ERROR_PARAMETERS = 4001;
    // 请求参数错误-已存在账户
    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;
    // 请求参数错误-已存在名称
    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;

    // 服务器错误
    public static final int ERROR_SERVICE = 5001;

    // 账户Token错误，需要重新登录
    public static final int ERROR_ACCOUNT_TOKEN = 2001;
    // 账户登录失败
    public static final int ERROR_ACCOUNT_LOGIN = 2002;
    // 账户注册失败
    public static final int ERROR_ACCOUNT_REGISTER = 2003;

    public static final int ERROR_GOODS_NOT_FOUND = 3001;

    private int code;
    private String message;
    private Date time;
    private T result;

    public boolean success() {
        return code == SUCCEED;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}