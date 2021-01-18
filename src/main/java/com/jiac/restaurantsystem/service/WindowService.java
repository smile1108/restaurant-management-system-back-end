package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.error.CommonException;

import java.util.List;

/**
 * FileName: WindowService
 * Author: Jiac
 * Date: 2020/10/16 10:35
 */
public interface WindowService {
    Window open(Integer windowNumber, Integer floor, Integer merchantId) throws CommonException;

    Window findWindowByNumberAndFloor(Integer windowNumber, Integer floor) throws CommonException;

    boolean judgeFloorIsExist(Integer floor) throws CommonException;

    Window selectWindowById(Integer wicketId) throws CommonException;
}
