package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.ShippingService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ShippingDTO;

/**
 * @author 'Pujan K.C.'
 * email: pujanov69@gmail.com
 * created on Aug 22, 2019
 **/
@Service
public class ShippingServiceImpl implements ShippingService {

	
	 	public static final String SHIPPING_INFO_URL = "/shipping";
	    private RestClientService restClientService;

	    @Autowired
	    public ShippingServiceImpl(RestClientService restClientService) {
	        this.restClientService = restClientService;
	    }
	    
	@Override
	public Response<?> getShippingDetailsByOrderId(Integer orderId) {
		 	ObjectMapper objectMapper = new ObjectMapper();
	        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionType(List.class, ShippingDTO.class));
	        String url = SHIPPING_INFO_URL+"/order/"+ orderId;
	        Response<?> retrieveResponse = restClientService.getData(url, responseType);
	        return retrieveResponse;
	}

}
