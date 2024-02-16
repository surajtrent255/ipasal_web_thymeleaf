package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.PaymentMethodService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.KhaltiResponse;
import com.ipasal.ipasalwebapp.dto.KhaltiPayloadDTO;
import com.ipasal.ipasalwebapp.dto.PaymentMethodDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Azens Eklak
 * Created On 2019-03-22
 */
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    public static final String BASE_PAYMENT_METHOD_URL = "/payment-methods";

    private RestClientService restClientService;
    
    @Autowired
    Environment environment;
    
    @Autowired
    public PaymentMethodServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> insertPaymentMethods(PaymentMethodDTO paymentMethodDTO) {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType insertResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        response = restClientService.postData(BASE_PAYMENT_METHOD_URL, paymentMethodDTO, insertResponseType);
        return response;
    }

    @Override
    public Response<?> getAllPaymentMethods() {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType retrieveAllResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PaymentMethodDTO.class));
        String retrieveAllURL = BASE_PAYMENT_METHOD_URL +"/all";
        response = restClientService.getData(retrieveAllURL, retrieveAllResponseType);
        return response;
    }

    @Override
    public Response<?> getAllActivePaymentMethods() {
        Response<List<PaymentMethodDTO>> activeResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType retrieveActiveResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PaymentMethodDTO.class));
        String retrieveActiveURL = BASE_PAYMENT_METHOD_URL + "/active";
        activeResponse = (Response<List<PaymentMethodDTO>>)restClientService.getData(retrieveActiveURL, retrieveActiveResponseType);
        List<PaymentMethodDTO> paymentMethodList = new ArrayList<>();
        
        //Gets payment api keys for all payment method.
        if(activeResponse.getData() != null && activeResponse.getData().size() > 0) {
        	for(PaymentMethodDTO paymentMethod: activeResponse.getData()) {
        		if( environment.getProperty("payment-api." + paymentMethod.getPaymentName() + "_PRIVATE_API_KEY") != null) {
        			String privateApiKey = environment.getProperty("payment-api." + paymentMethod.getPaymentName() + "_PRIVATE_API_KEY");
        			paymentMethod.setPrivateApiKey(privateApiKey);
        		}
        		
        		if(environment.getProperty("payment-api." + paymentMethod.getPaymentName() + "_PUBLIC_API_KEY") != null) {
        			String publicApiKey = environment.getProperty("payment-api." + paymentMethod.getPaymentName() + "_PUBLIC_API_KEY");
                    paymentMethod.setPublicApiKey(publicApiKey);
        		}
                paymentMethodList.add(paymentMethod);
            }
        }
        activeResponse.setData(paymentMethodList);
        return activeResponse;
    }

    @Override
    public Response<?> getPaymentMethodById(Integer id) {
        Response<?> paymentResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType singlePaymentResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, PaymentMethodDTO.class);
        String url = BASE_PAYMENT_METHOD_URL+"/"+id;
        paymentResponse = restClientService.getData(url, singlePaymentResponseType);
        return paymentResponse;
    }

    @Override
    public Response<?> updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO) {
        Response<?> updateResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType updateResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        String url = BASE_PAYMENT_METHOD_URL+"/update/"+id;
        updateResponse = restClientService.putData(url, paymentMethodDTO, updateResponseType);
        return updateResponse;
    }

    @Override
    public KhaltiResponse verifyKhalitPayment(KhaltiPayloadDTO payloadDTO) {
        KhaltiResponse updateResponse = null;
        //JavaType updateResponseType = objectMapper.getTypeFactory().constructParametricType(Response.class, KhaltiResponse.class);
        updateResponse = restClientService.postKhaltiData(payloadDTO);
        return updateResponse;
    }

}
