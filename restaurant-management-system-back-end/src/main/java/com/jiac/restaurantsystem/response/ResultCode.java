package com.jiac.restaurantsystem.response;

/**
 * FileName: ResultCode
 * Author: Jiac
 * Date: 2020/10/9 14:27
 */
public enum ResultCode {
    // 200代表成功
    SUCCESS("200", "成功"),

    //3开头代表参数有问题
    PARAMETER_IS_BLANK("30001", "参数不能为空"),

    //4开头代表认证错误
    AUTH_FAILED("40001", "用户名或密码错误");
    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
