package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.OfferProductService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.OfferProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by aevin on Feb 06, 2019
 **/
@Service
public class OfferProductServiceImpl implements OfferProductService {

    public static final String OFFER_PRODUCT_BASE_URL = "/offer-product";
    private RestClientService restClientService;

    public OfferProductServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }
// Inserting the offer product detail to the database
    @Override
    public Response<?> addOfferProductDetail(OfferProductDTO offerProductDTO) {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responsType =
                objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        response = restClientService.postData(OFFER_PRODUCT_BASE_URL, offerProductDTO, responsType);
        return response;
    }

//    Retrieving offer product from database
    @Override
    public Response<?> getOfferProduct() {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType =
                objectMapper.getTypeFactory().constructParametricType(Response.class,
                        objectMapper.getTypeFactory().constructParametricType(List.class, OfferProductDTO.class));
        response = restClientService.getData(OFFER_PRODUCT_BASE_URL, responseType);
        return response;
    }

//    Inserting offer product image in the database
    @Override
    public Response<?> uploadOfferProductImage(MultipartFile[] files, Integer offerId) {
        return null;
    }
}
