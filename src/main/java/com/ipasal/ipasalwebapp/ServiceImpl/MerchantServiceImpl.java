package com.ipasal.ipasalwebapp.ServiceImpl;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.MerchantServices;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.MerchantDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;

@Service
public class MerchantServiceImpl implements MerchantServices {
	private final String MERCHANTS_BASE_URL = "/merchant";
	private RestClientService restClientService;
	
	public MerchantServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}


	@Override
	public Response<?> addMerchant(MerchantDTO merchant) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		
		Response<?> response = null;
		response = restClientService.postData(MERCHANTS_BASE_URL, merchant, responseType);
		return response;
	}


	@Override
	public Response<?> getAllMerchant() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructParametricType(List.class, MerchantDTO.class));
		response = restClientService.getData(MERCHANTS_BASE_URL, responseType);
		return response;
	}


	@Override
	public Response<?> getMerchantById(Integer merchantId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, MerchantDTO.class));
		Response<?> response = null;
		response = restClientService.getData(MERCHANTS_BASE_URL + "/" + merchantId, responseType);
		return response;
	}


	@Override
	public Response<?> updateMercantById(Integer merchantId, MerchantDTO merchant) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = MERCHANTS_BASE_URL + "/" + merchantId;
		Response<?> response = restClientService.putData(url, merchant, responseType);
		return response;
	}


	@Override
	public Response<?> deleteMerchantById(Integer merchantId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = MERCHANTS_BASE_URL + "/" + merchantId;
		Response<?> response = restClientService.deleteData(url, null, responseType);
		return response;
	}


	@Override
	public Response<?> searchMerchantBySearchKey(String searchKey, HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructParametricType(List.class, MerchantDTO.class));
        if (CustomUrlWithQueryParamsCreator.checkParameter("searchKey", request)) {
            searchKey = (String) CustomUrlWithQueryParamsCreator.getParameterFromRequestObject("searchKey", request);
        }
        String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.USER);

        String url = MERCHANTS_BASE_URL + "/search?searchKey=";
        try {
            url += URLEncoder.encode(searchKey, StandardCharsets.UTF_8.name()) + "&" + queryParams;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Response<?> response = restClientService.getData(url, responseType);

        return response;
	}
}
