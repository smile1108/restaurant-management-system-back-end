package com.jiac.restaurantsystem.service.impl;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.DO.Window;
import com.jiac.restaurantsystem.error.CommonException;
import com.jiac.restaurantsystem.mapper.MerchantMapper;
import com.jiac.restaurantsystem.mapper.WindowMapper;
import com.jiac.restaurantsystem.response.ResultCode;
import com.jiac.restaurantsystem.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FileName: WindowServiceImpl
 * Author: Jiac
 * Date: 2020/10/16 10:36
 */
@Service
public class WindowServiceImpl implements WindowService {

    private static final Logger LOG = LoggerFactory.getLogger(WindowServiceImpl.class);

    @Autowired
    private WindowMapper windowMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Window open(Integer windowNumber, Integer floor, Integer merchantId) throws CommonException {
        // 首先检查窗口是否已经被开通过
        Window window = windowMapper.selectWindowByNumberAndFloor(windowNumber, floor);
        if(window != null){
            // 表示窗口已经被开通过 不能再次被开通
            LOG.error("WindowService -> 窗口已经被开通, 不可以再次开通");
            throw new CommonException(ResultCode.WINDOW_HAVE_OPENED);
        }
        Merchant merchant = merchantMapper.selectById(merchantId);
        if(merchant == null){
            // 表示商家不存在 不允许不存在的商家开通窗口
            LOG.error("WindowService -> 商家不存在");
            throw new CommonException(ResultCode.USER_IS_NOT_EXIST);
        }
        // 如果校验完成 表示可以开通窗口
        Window window1 = new Window();
        window1.setFloor(floor);
        window1.setMerchantId(merchantId);
        window1.setWicketNumber(windowNumber);
        LOG.info("WindowService -> 窗口开通成功");
        windowMapper.insert(windowNumber, floor, merchantId);
        return window1;
    }

    @Override
    public Window findWindowByNumberAndFloor(Integer windowNumber, Integer floor) throws CommonException {
        Window window = windowMapper.selectWindowByNumberAndFloor(windowNumber, floor);
        if(window == null){
            // 表示该窗口不存在 抛出异常
            LOG.error("WindowServiceImpl -> 根据窗口号和楼层查询对应的窗口 -> 窗口不存在");
            throw new CommonException(ResultCode.WINDOW_IS_NOT_OPEN);
        }
        return window;
    }

    @Override
    public boolean judgeFloorIsExist(Integer floor) throws CommonException {
        List<Window> windows = windowMapper.selectAllWindowByFloor(floor);
        if(windows.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public Window selectWindowById(Integer wicketId) throws CommonException {
        Window window = windowMapper.selectWindowById(wicketId);
        if(window == null){
            LOG.error("该窗口不存在");
            throw new CommonException(ResultCode.WINDOW_IS_NOT_OPEN);
        }
        return window;
    }

}
