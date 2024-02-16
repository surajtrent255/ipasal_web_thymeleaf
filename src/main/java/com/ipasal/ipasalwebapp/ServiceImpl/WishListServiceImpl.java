package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.WishListService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.WishListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by aevin on Feb 27, 2019
 **/

/*
* Main Step: 3
*
* Step 0: Annotate the Class as /Service/
* Step 1: Create a final string for the BASE_URL -> Matching the web service url (BASE_URL is url after 'api/v1')
* Step 2: Get the instance of the RestClientService
* Step 3: Create a constructor with restClientService as parameter
* Step 4: Annotate the Constructor as /Autowired/
* Step 5: Initialize various objects;
*       5.1: ResponseType
*       5.2 ObjectMapper
*       5.3 JavaType
* Step 6: Use restClient service to perform the POST and GET operations
* */

@Service
public class WishListServiceImpl implements WishListService {
    public static final String WISH_LIST_URL = "/wish-list";
    private RestClientService restClientService;

    @Autowired
    public WishListServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> addWishItem(WishListDTO wishListDTO) {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType insertResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        response = restClientService.postData(WISH_LIST_URL, wishListDTO, insertResponseType);
        return response;
    }

    @Override
    public Response<?> removeWishItem(Integer wishId, WishListDTO wishListDTO) {
        Response<?> removeResponse = null;
        ObjectMapper removeObjectMapper = new ObjectMapper();
        JavaType removeResponseType = removeObjectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        String updateUrl = WISH_LIST_URL + "/" + wishId;
        removeResponse = restClientService.putData(updateUrl, wishListDTO, removeResponseType);
        return removeResponse;
    }

    @Override
    public Response<?> getAllWishProductsForUser(Integer userId) {
        Response<?> retrieveResponse = null;
        ObjectMapper retrieveObjectMapper = new ObjectMapper();
        JavaType retrieveResponseType = retrieveObjectMapper.getTypeFactory().constructParametricType(Response.class,
                retrieveObjectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        String retrieveUrl = WISH_LIST_URL +"/"+ userId;
        retrieveResponse = restClientService.getData(retrieveUrl, retrieveResponseType);
        return retrieveResponse;
    }

    @Override
    public Response<?> checkProductInWishList(Integer userId, Integer productId) {
        ObjectMapper statusObjectMapper = new ObjectMapper();
        JavaType statusResponseType = statusObjectMapper.getTypeFactory().constructParametricType(Response.class, Boolean.class);
        String statusUrl = WISH_LIST_URL+"/status/"+userId+"/"+productId;
        Response<?> statusResponse = restClientService.getData(statusUrl, statusResponseType);
        return statusResponse;
    }

    @Override
    public Response<?> getWishListId(Integer userId, Integer productId) {
        ObjectMapper wishIdObjectMapper = new ObjectMapper();
        JavaType wishIdResponseType = wishIdObjectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        String wishIdsUrl = WISH_LIST_URL+"/"+userId+"/"+productId;
        Response<?> statusResponse = restClientService.getData(wishIdsUrl, wishIdResponseType);
        return statusResponse;
    }

    //Send an email to the admin for the further contact by the whole-seller
    @Override
    public Response<?> notifyAdmin(Integer userId) {
        Response<?> notifyResponse = null;
        ObjectMapper notifyObjectMapper = new ObjectMapper();
        JavaType removeResponseType = notifyObjectMapper.getTypeFactory().constructParametricType(Response.class, Boolean.class);
        String notifyUrl =  "/users/notify/" + userId;
        notifyResponse = restClientService.getData(notifyUrl, removeResponseType);
        return notifyResponse;
    }

    @Override
    public Response<?> getWishListOfUser(Integer userId) {
        Response<?> retrieveResponse = null;
        ObjectMapper retrieveObjectMapper = new ObjectMapper();
        JavaType retrieveResponseType = retrieveObjectMapper.getTypeFactory().constructParametricType(Response.class,
                retrieveObjectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        String retrieveUrl = WISH_LIST_URL +"/"+ userId+"/list";
        retrieveResponse = restClientService.getData(retrieveUrl, retrieveResponseType);
        return retrieveResponse;
    }
}
