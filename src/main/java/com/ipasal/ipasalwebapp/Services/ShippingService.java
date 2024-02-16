package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;

/**
 * @author 'Pujan K.C.'
 * email: pujanov69@gmail.com
 * created on Aug 22, 2019
 **/
public interface ShippingService {
	Response<?> getShippingDetailsByOrderId(Integer orderId);
}
