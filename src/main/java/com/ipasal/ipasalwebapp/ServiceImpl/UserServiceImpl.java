package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.UserService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.UserDTO;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private RestClientService restClientService;
    public static final String USER_BASE_URL =  "/users";
    public UserServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> registerUser(UserDTO user) {
        ObjectMapper ObjectMapper = new ObjectMapper();
        JavaType responseType = ObjectMapper.getTypeFactory().constructType(Response.class);
        Response<?> response = null;
        response = (Response<?>) restClientService.postData("/users/register", user, responseType);
		return response;
	}

    @Override
    public Response<?> checkUserName(String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Boolean.class);
        Response<?> response = null;
        response = (Response<?>) restClientService.getData("/users/checkUserName?username=" + username, responseType);
        return response;
    }

    @Override
    public Response<?> checkDuplicateEmail(String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Boolean.class);
        Response<?> response = null;
        response = (Response<?>) restClientService.getData("/users/checkDuplicateEmail?email=" + email, responseType);
        return response;
    }

    @Override
    public Response<?> activateUserAccount(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        Response<?> response = null;
        response = restClientService.getData("/users/activateUser?token=" + token, responseType);
        return response;
    }

    //Validating token for resetting the password
    @Override
    public Response<?> verifyPasswordResetToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = restClientService.getData("/users/pw-reset-token-verify?token=" + token, responseType);
        logger.info("The response is ->"+response.getData());
        return response;
    }

    // Sending email to the user for password reset
    @Override
    public Response<?> sendPasswordResetEmail(String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        Response<?> response = null;
        response = (Response<?>) restClientService.getData("/users/reset-user?email=" + email, responseType);
        return response;
    }

    // Creating new password ->Resetting old password
    @Override
    public Response<?> resetPasswordConfirm(String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        Response<?> response = null;
        response = (Response<?>) restClientService.getData("/users/reset-user?email=" + password, responseType);
        return response;
    }


    //Updating the user password, by sending the user dto with the new password
    public Response<?> updateUserPassword(Integer userId, String password){
        Response<?> removeResponse = null;
        ObjectMapper removeObjectMapper = new ObjectMapper();
        JavaType removeResponseType = removeObjectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        String updateUrl =  "/users/update-password/" + userId+"?password="+password;
        removeResponse = restClientService.getData(updateUrl, removeResponseType);
        return removeResponse;
    }

    // Retrieving all whole seller user details
    @Override
    public Response<?> getAllWholeSeller() {
        Response<?> wholeSellerResponse = null;
        ObjectMapper wsObjectMapper = new ObjectMapper();
        JavaType responseType = wsObjectMapper.getTypeFactory().constructParametricType(Response.class,
                wsObjectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));
        String wholeSellerURL = "/users/whole-seller";
        wholeSellerResponse = restClientService.getData(wholeSellerURL, responseType);
        return wholeSellerResponse;
    }

    // Search the wholeseller with the given id
    @Override
    public Response<?> searchWholeSeller(Integer id) {
        Response<?> wholeSellerResponse = null;
        ObjectMapper wsObjectMapper = new ObjectMapper();
        JavaType responseType = wsObjectMapper.getTypeFactory().constructParametricType(Response.class, UserDTO.class);
        String wholeSellerURL = "/users/whole-seller/"+id;
        wholeSellerResponse = restClientService.getData(wholeSellerURL, responseType);
        return wholeSellerResponse;
    }

    @Override
    public Response<?> getAllCustomers() {
        Response<?> customersResponse = null;
        ObjectMapper customerObjectMapper = new ObjectMapper();
        JavaType cResponseType = customerObjectMapper.getTypeFactory().constructParametricType(Response.class,
                customerObjectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));
        String customerListUrl = USER_BASE_URL+"/customers";
        customersResponse = restClientService.getData(customerListUrl, cResponseType);
        return customersResponse;
    }

	@Override
	public Response<?> updateUserInfoById(Integer userId, UserDTO user) {
		 ObjectMapper objectMapper = new ObjectMapper();
	        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
	        String url = USER_BASE_URL + "/" + userId;
	        Response<?> response = restClientService.putData(url, user, responseType);
	        return response;
	}

}