package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.error.CommonException;

import java.util.List;

/**
 * FileName: MerchantService
 * Author: Jiac
 * Date: 2020/10/16 8:42
 */
public interface MerchantService {
    Merchant login(String email, String password) throws CommonException;

    void modifyPass(String email, String oldPass, String newPass, String qualifyPass) throws CommonException;

    void getbackPass(String email, Integer id) throws CommonException;

    Merchant register(String password, String email) throws CommonException;

    void findByMerchantId(Integer merchantId) throws CommonException;

    String getCode(String email) throws CommonException;

    boolean judgeMerchantIsExistByEmail(String email) throws CommonException;
}
