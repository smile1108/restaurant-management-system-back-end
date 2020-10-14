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

    @Override
    public String getErrCode() {
        return this.error.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.error.getErrMsg();
    }
}
