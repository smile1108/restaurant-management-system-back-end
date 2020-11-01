package com.jiac.restaurantsystem.response;

import com.jiac.restaurantsystem.error.CommonError;

/**
 * FileName: CommonReturnType
 * Author: Jiac
 * Date: 2020/9/23 19:52
 */
public class CommonReturnType {
    // 响应码
    private String code;

    //附加信息
    private String msg;

    //若status=success，则data内返回前端需要的json数据
    //若status=fail，则data内使用通用的错误码格式
    private Object data;


    // 不带有返回数据的success
    public static CommonReturnType success(){
        return create(ResultCode.SUCCESS, null);
    }

    // 带有返回数据的success
    public static CommonReturnType success(Object data){
        return create(ResultCode.SUCCESS, data);
    }

    public static CommonReturnType create(CommonError code, Object data) {
        CommonReturnType type = new CommonReturnType();
        type.setCode(code.getCode());
        type.setMsg(code.getMsg());
        type.setData(data);
        return type;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
