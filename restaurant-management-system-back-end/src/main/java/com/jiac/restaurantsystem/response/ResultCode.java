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
    PARAMETER_IS_BLANK("30001", "参数不能为空"),
    PASSWORD_NOT_EQUAL("30002", "两次密码输入不一致"),
    HAVE_NOT_THIS_WINDOW("30003", "没有这个窗口"),
    USER_IS_NOT_EXIST("30004", "用户不存在"),
    EMAIL_NOT_TRUE("30005", "输入邮箱不正确"),
    MERCHANT_HAVE_EXISTED("30006", "商家已经存在"),
    WINDOW_HAVE_OPENED("30007", "窗口已经开通过, 不可以再次开通"),

    //4开头代表认证错误
    AUTH_FAILED("40001", "用户名或密码错误"),

    //5开头权限错误
    HAVE_NOT_ACCESS("50001", "没有权限"),

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
}
