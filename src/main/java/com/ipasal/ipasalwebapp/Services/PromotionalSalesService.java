package com.ipasal.ipasalwebapp.Services;




import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.PromotionalSalesDTO;
import com.ipasal.ipasalwebapp.dto.Response;

/**
 * 
 * @author Tanchhowpa
 * Sep 23, 2019, 10:44:55 AM
 *
 */
public interface PromotionalSalesService {

	Response<?> getAllPrmotions();
	Response<?> getPromotionalSalesById(Integer promotionalSalesId);
	Response<?> updatePromotionById(Integer promotionalSalesId, PromotionalSalesDTO promotion);
	Response<?> getPromotionalSaleProductsById(Integer promotionalSalesId, HttpServletRequest request);
	Response<?> setPromotionActive(Integer promotionalSalesId);
	Response<?> setPromotionInactive(Integer promotionalSalesId);
	Response<?> getAllActivePrmotions();
	Response<?> updatePromotionProductsById(Integer promotionalSalesId, PromotionalSalesDTO promotion);

}
