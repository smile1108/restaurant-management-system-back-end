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
        this.error.setErrMsg(errorMsg);
    }

    @Override
    public String getErrCode() {
        return this.error.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.error.getErrMsg();
    }

    @Override
    public void setErrMsg(String msg) {
        this.error.setErrMsg(msg);
    }
}
