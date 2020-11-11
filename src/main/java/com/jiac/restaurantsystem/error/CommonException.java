package com.jiac.restaurantsystem.error;

/**
 * FileName: CommonException
 * Author: Jiac
 * Date: 2020/10/14 19:25
 */
public class CommonException extends Exception implements CommonError {

    private CommonError error;

    public CommonException(CommonError error) {
        this.error = error;
    }

    public CommonException(CommonError error, String errorMsg){
        this.error = error;
        this.error.setMsg(errorMsg);
    }

    @Override
    public String getCode() {
        return this.error.getCode();
    }

    @Override
    public String getMsg() {
        return this.error.getMsg();
    }

    @Override
    public void setMsg(String msg) {
        this.error.setMsg(msg);
    }
}
