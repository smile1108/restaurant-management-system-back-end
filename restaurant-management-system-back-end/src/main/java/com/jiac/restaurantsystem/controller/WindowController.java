package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.controller.VO.WindowVO;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.response.CommonReturnType;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.WindowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: WindowController
 * Author: Jiac
 * Date: 2020/10/9 11:27
 */
@Api(value = "窗口controller", description = "窗口操作")
@RestController
@RequestMapping("/api/dbcourse/window")
public class WindowController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(WindowController.class);

    @Autowired
    private WindowService windowService;


    @ApiOperation("窗口开通操作")
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowNumber", value = "窗口号", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "floor", value = "窗口楼层", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0"),
            @ApiImplicitParam(name = "merchantId", value = "商家id", dataType = "int", paramType = "body", required = true, defaultValue = "0", example = "0")
    })
    public CommonReturnType open(Integer windowNumber, Integer floor, String merchantId) throws CommonException {
        if(windowNumber == null || merchantId == null || merchantId.trim().length() == 0){
            LOG.error("WindowController -> 开通窗口 -> 参数不能为空");
            throw new CommonException(ResultCode.PARAMETER_IS_BLANK);
        }
        Window window = windowService.open(windowNumber, floor, merchantId);
        WindowVO windowVO = convertFromWindow(window);
        return CommonReturnType.success(windowVO);
    }

    private WindowVO convertFromWindow(Window window){
        if(window == null){
            return null;
        }
        WindowVO windowVO = new WindowVO();
        BeanUtils.copyProperties(window, windowVO);
        return windowVO;
    }
}
