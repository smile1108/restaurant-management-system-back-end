package com.jiac.restaurantsystem.error;

/**
 * FileName: CommonError
 * Author: Jiac
 * Date: 2020/10/14 19:10
 */
public interface CommonError {
    String getErrCode();
    String getErrMsg();

    void setErrMsg(String msg);
}
