package com.jiac.restaurantsystem.service;

import com.jiac.restaurantsystem.DO.Merchant;
import com.jiac.restaurantsystem.error.CommonException;

/**
 * FileName: MerchantService
 * Author: Jiac
 * Date: 2020/10/16 8:42
 */
public interface MerchantService {
    Merchant login(String id, String password) throws CommonException;

    void modifyPass(String id, String oldPass, String newPass, String qualifyPass) throws CommonException;
}
