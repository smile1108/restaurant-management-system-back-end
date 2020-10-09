package com.jiac.restaurantsystem.controller;

import com.jiac.restaurantsystem.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: BaseController
 * Author: Jiac
 * Date: 2020/10/9 8:21
 */
@Api("根接口")
@ApiOperation("所有接口的根接口")
@RequestMapping("/api/dbcourse")
@RestController
public class BaseController {
}
