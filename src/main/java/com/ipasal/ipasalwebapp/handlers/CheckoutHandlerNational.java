/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Sep 6, 2019
 */
package com.ipasal.ipasalwebapp.handlers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.OrderService;
import com.ipasal.ipasalwebapp.Services.UserService;
import com.ipasal.ipasalwebapp.dto.CheckoutDTO;
import com.ipasal.ipasalwebapp.dto.KhaltiPayloadDTO;
import com.ipasal.ipasalwebapp.dto.KhaltiResponse;
import com.ipasal.ipasalwebapp.dto.NewOrderDto;
import com.ipasal.ipasalwebapp.dto.PaymentDTO;
import com.ipasal.ipasalwebapp.dto.PaymentMethodDTO;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ShippingRateDTO;
import com.ipasal.ipasalwebapp.dto.UserDTO;
import com.ipasal.ipasalwebapp.exception.ResourceNotFoundException;

@Profile("national")
@Component("checkoutHandlerNational")
public class CheckoutHandlerNational {
	@Autowired
	RestTemplate restClient; //will use it to call payment gateways.
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	public CheckoutDTO init() {
		return new CheckoutDTO();
	}
	
	public void addUserInfo(CheckoutDTO checkout, UserDTO userInfo) {
		checkout.setUserInfo(userInfo);
	}
	
	public String validateUserInfo(UserDTO userInfo, MessageContext errors) {
		return "success";
	}
	
	public void addPaymentInfo(CheckoutDTO checkout, PaymentDTO paymentInfo) {
		checkout.setPaymentInfo(paymentInfo);
	}
	
	public ShippingRateDTO addShippingRateInfo(UserDTO userInfo, List<ShippingRateDTO> shippingRates) {
		return shippingRates.stream()
				.filter(shippingRate -> userInfo.getCity().equalsIgnoreCase(shippingRate.getLocation()))
				.findAny()
				.orElse(null);
	}
	
	public String checkPayment(HttpSession session, CheckoutDTO checkout, List<PaymentMethodDTO> paymentMethod) {
		String paymentStatus = "unpaid";
		
		PaymentDTO paymentInfo = checkout.getPaymentInfo();
		paymentInfo.setPaymentDate(new Date());
		
		NewOrderDto order = new NewOrderDto();
		order.setOrderDate(new Date());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//check if the sent user is logged in usr or not
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDTO userDTO = (UserDTO) authentication.getPrincipal();
			order.setOrderedBy(userDTO.getUserId()); //must set this value for loggedin users.
			order.setUser(userDTO);
		} else {
			order.setUser(checkout.getUserInfo());
		}
		
		
		order.setPaymentDetail(paymentInfo);
		//order.setShippingAddress(checkout.getBillingShippingInfo().getShippingAddress());
		order.setUniqueOrderIdentifier(paymentInfo.getUniqueOrderIdentifier());
		List<ProductDTO> products = (List<ProductDTO>) session.getAttribute("cart");
		List<Integer> productIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
		List<Integer> quantities = products.stream().map(ProductDTO::getQuantity).collect(Collectors.toList());
		order.setQuantities(quantities);
		order.setProductIds(productIds);
		
		switch(checkout.getPaymentInfo().getPaymentMethodId()) {
		case 1:
				paymentStatus = "paid";
				break;
		case 2:
			paymentInfo.setStatus(true);
			KhaltiPayloadDTO khaltiPayload = new KhaltiPayloadDTO();
			khaltiPayload.setAmount(paymentInfo.getAmount().intValue() * 100);
			khaltiPayload.setToken(paymentInfo.getToken());
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", "Key " + paymentMethod.get(1).getPrivateApiKey());
			header.setContentType(MediaType.APPLICATION_JSON);
			String payload = "";
			try {
				payload = objectMapper.writeValueAsString(khaltiPayload);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			HttpEntity<String> entity = new HttpEntity<>(payload, header);
			ResponseEntity<KhaltiResponse> khaltiResponse = null;
			try {
				khaltiResponse = restClient.exchange("https://khalti.com/api/v2/payment/verify/", HttpMethod.POST, entity, KhaltiResponse.class);
	        } catch(HttpStatusCodeException sce) {
	        } catch(RestClientException rce) {
	        	System.out.println(rce.getMessage());
	        }
			
			
			if(khaltiResponse.getBody().getState().getName().equalsIgnoreCase("completed")) {
				//now complete the users information entry process here.
				
				Response<?> response = orderService.confirmOrder(order);
				HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatus());
				if(HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
					throw new ResourceNotFoundException(response.getMessage());
				}
				session.removeAttribute("cart");
				paymentStatus = "paid";
			}
			
			return paymentStatus;
		default:
			//Default payment method  is cash on delivery payment method.
			//Complete user information entry process here.
			paymentInfo.setStatus(false);
			Response<?> response = orderService.confirmOrder(order);
			HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatus());
			if(HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
				throw new ResourceNotFoundException(response.getMessage());
			}
			session.removeAttribute("cart");
			paymentStatus = "paid";
			break;
		}
		
		return paymentStatus;
		
	}
}
