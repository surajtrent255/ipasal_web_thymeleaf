package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.ShippingRateService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ShippingRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 'Azens Eklak'
 * email: azens1995@gmail.com
 * created on Mar 01, 2019
 * since 2017
 **/
@Service
public class ShippingRateServiceImpl implements ShippingRateService {

    public static final String SHIPPING_RATE_INFO_URL = "/shipping-rate";
    private RestClientService restClientService;

    @Autowired
    public ShippingRateServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> addShippingRateInfo(ShippingRateDTO shippingRateDTO) {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType addResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        response = restClientService.postData(SHIPPING_RATE_INFO_URL, shippingRateDTO, addResponseType);
        return response;
    }

    @Override
    public Response<?> getShippingRateInfoByLocation(String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, ShippingRateDTO.class);
        String url = SHIPPING_RATE_INFO_URL+"/"+location;
        Response<?> retrieveResponse = restClientService.getData(url, responseType);
        return retrieveResponse;
    }

    @Override
    public Response<?> getAllShippingRateInfo() {
        Response<?> retrieveResponse = null;
        ObjectMapper om = new ObjectMapper();
        JavaType retrieveResponseType = om.getTypeFactory().constructParametricType(Response.class,
                om.getTypeFactory().constructCollectionType(List.class, ShippingRateDTO.class));
        retrieveResponse = restClientService.getData(SHIPPING_RATE_INFO_URL, retrieveResponseType);
        return retrieveResponse;
    }

    @Override
    public Response<?> updateShippingRateInfo(Integer amount, Integer id) {
        return null;
    }
}
