package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ShippingRateDTO;

/**
 * @author 'Azens Eklak'
 * email: azens1995@gmail.com
 * created on Mar 01, 2019
 * since 2017
 **/
public interface ShippingRateService {
    Response<?> addShippingRateInfo(ShippingRateDTO shippingRateDTO);
    Response<?> getShippingRateInfoByLocation(String location);
    Response<?> getAllShippingRateInfo();
    Response<?> updateShippingRateInfo(Integer amount, Integer id);
}
