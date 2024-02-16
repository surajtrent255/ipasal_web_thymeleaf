package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.PromotionalSalesService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.PromotionalSalesDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;
/**
 * 
 * @author Tanchhowpa
 * Sep 23, 2019, 10:45:01 AM
 *
 */

@Service
public class PromotionalSalesServiceImpl implements PromotionalSalesService {
	
	private final String PROMOTIONAL_BASE_URL = "/promotionalSales";
	private RestClientService restClientService;
	
	
	public PromotionalSalesServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}

	@Override
	public Response<?> getAllPrmotions() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, PromotionalSalesDTO.class));
		Response<List<PromotionalSalesDTO>> response = null;
		response = (Response<List<PromotionalSalesDTO>>) restClientService.getData(PROMOTIONAL_BASE_URL, responseType);
		return response;
	}

	@Override
	public Response<?> getPromotionalSalesById(Integer promotionalSalesId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, PromotionalSalesDTO.class));
		Response<List<PromotionalSalesDTO>> response = null;
		response = (Response<List<PromotionalSalesDTO>>) restClientService.getData(PROMOTIONAL_BASE_URL + "/pr/" + promotionalSalesId, responseType);
		return response;
	}

	@Override
	public Response<?> updatePromotionById(Integer promotionalSalesId, PromotionalSalesDTO promotion) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = PROMOTIONAL_BASE_URL + "/" + promotionalSalesId;
		Response<?> response = restClientService.putData(url, promotion, responseType);
		return response;
	}

	@Override
	public Response<?> getPromotionalSaleProductsById(Integer promotionalSalesId, HttpServletRequest request) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionLikeType(List.class, ProductDTO.class));
		String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);
		String url = PROMOTIONAL_BASE_URL + "/prListing/" + promotionalSalesId + "?" + queryParams;
		Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) restClientService.getData(url, responseType); 
		return response;
	}

	@Override
	public Response<?> setPromotionActive(Integer promotionalSalesId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = PROMOTIONAL_BASE_URL + "/setActive/" + promotionalSalesId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}

	@Override
	public Response<?> setPromotionInactive(Integer promotionalSalesId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = PROMOTIONAL_BASE_URL + "/setInactive/" + promotionalSalesId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}

	@Override
	public Response<?> getAllActivePrmotions() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, PromotionalSalesDTO.class));
		Response<List<PromotionalSalesDTO>> response = null;
		String url = PROMOTIONAL_BASE_URL + "/active" ;
		response = (Response<List<PromotionalSalesDTO>>) restClientService.getData(url, responseType);
		return response;
	}

	@Override
	public Response<?> updatePromotionProductsById(Integer promotionalSalesId, PromotionalSalesDTO promotion) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = PROMOTIONAL_BASE_URL + "/list/" + promotionalSalesId;
		Response<?> response = restClientService.putData(url, promotion, responseType);
		return response;
	}

}
