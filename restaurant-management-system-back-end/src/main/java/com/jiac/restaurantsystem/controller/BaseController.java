package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.error.CommonError;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * FileName: BaseController
 * Author: Jiac
 * Date: 2020/10/9 8:21
 */
@Api("根接口")
@ApiOperation("所有接口的根接口")
@RestController
public class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    //定义exceptionHandler解决未被controller层吸收的exception
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        CommonError resultErr = null;
        if(ex instanceof CommonException){
            resultErr = (CommonException)ex;
        }else{
            resultErr = ResultCode.UNKNOWN_ERROR;
        }
        return CommonReturnType.create(resultErr, null);
    }


}
