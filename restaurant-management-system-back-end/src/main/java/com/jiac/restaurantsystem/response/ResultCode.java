package com.jiac.restaurantsystem.response;

import com.jiac.restaurantsystem.error.CommonError;

/**
 * FileName: ResultCode
 * Author: Jiac
 * Date: 2020/10/9 14:27
 */
public enum ResultCode implements CommonError {
    // 200代表成功
    SUCCESS("200", "成功"),

    //3开头代表参数有问题
    PARAMETER_IS_BLANK("301", "参数不能为空"),
    PASSWORD_NOT_EQUAL("302", "两次密码输入不一致"),
    HAVE_NOT_THIS_WINDOW("303", "没有这个窗口"),
    USER_IS_NOT_EXIST("304", "用户不存在"),
    EMAIL_NOT_TRUE("305", "输入邮箱不正确"),
    MERCHANT_HAVE_EXISTED("306", "商家已经存在"),
    WINDOW_HAVE_OPENED("307", "窗口已经开通过, 不可以再次开通"),
    WINDOW_IS_NOT_OPEN("308", "窗口还未开通"),
    CODE_IS_EXPIRED("309", "验证码过期"),
    CODE_IS_NOT_RIGHT("310", "验证码不正确"),
    IS_NOT_LOGIN("311", "还未登录,不能访问对应资源"),

    //4开头代表认证错误
    AUTH_FAILED("401", "用户名或密码错误"),
    IS_LOGINED("402", "已经登录,不能重复登录"),

    //5开头权限错误
    HAVE_NOT_ACCESS("501", "没有权限"),

    //60001未知错误
    UNKNOWN_ERROR("60001", "未知错误");
    private String code;


    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getErrCode() {
        return code;
    }

    @Override
    public String getErrMsg() {
        return msg;
    }

    @Override
    public void setErrMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
