package com.ipasal.ipasalwebapp.Services;



import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.MerchantDTO;
import com.ipasal.ipasalwebapp.dto.Response;

public interface MerchantServices {

	Response<?> addMerchant(MerchantDTO merchant);
	Response<?> getAllMerchant();
	Response<?> getMerchantById(Integer merchantId);
	Response<?> updateMercantById(Integer merchantId, MerchantDTO merchant);
	Response<?> deleteMerchantById(Integer merchantId);
	Response<?> searchMerchantBySearchKey(String searchKey, HttpServletRequest request);
}
