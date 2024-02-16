package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.OrderService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.NewOrderDto;
import com.ipasal.ipasalwebapp.dto.OrderUpdateDTO;
import com.ipasal.ipasalwebapp.dto.PaymentDTO;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.UserDTO;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OrderServiceImpl implements OrderService{
	private static final String ORDER_BASE_URL = "/order/";
	
    private RestClientService restClientServiceImpl;
    
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    ObjectMapper objectMapper;

    public OrderServiceImpl(RestClientService restClientService) {
        this.restClientServiceImpl = restClientService;
    }

	@Override
    public Response<?> confirmOrder(NewOrderDto requestObject) {
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
			String json = mapper.writeValueAsString(requestObject);
			 logger.info("json------->" + json);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
        response = restClientServiceImpl.postData("/order/confirm", requestObject, responseType);
        return response;
    }

	@Override
    public Response<?> getOrdersByUserId(Integer userId, Pageable pageable) {
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
    	Response<?> response = null;
        response = restClientServiceImpl.getData("/order/user/" + userId + createPagealeLink(pageable), responseType);
		return response;
	}

    private String createPagealeLink(Pageable pageable) {
		String paginationRequestParam = "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
		return paginationRequestParam;
	}

	@Override
    public Response<?> cancelOrderByOrderId(Integer orderId) {
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        Response<?> response = null;
        response = restClientServiceImpl.postData("/order/cancel/" + orderId, null, responseType);
        return response;
    }
	
	@Override
	public Response<?> chageOrderStatusToDeliverByOrderId(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		Response<?> response = null;
        response = restClientServiceImpl.postData("/order/deliver/" + orderId, null, responseType);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<List<ProductDTO>> getOrderDetailsByOrderId(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ProductDTO.class));
		Response<List<ProductDTO>> response = null;
		response = (Response<List<ProductDTO>>) restClientServiceImpl.getData("/order/" + orderId, responseType);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<List<NewOrderDto>> getAllOrders(Pageable pageable) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData("/order" + "?page=" + pageable.getPageNumber() + "&size=" +pageable.getPageSize(), responseType);
		return response;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<?> getOrdersByUserId(HttpServletRequest request, Integer userId) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class,
						objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class)
				);
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = ORDER_BASE_URL+"user/"+userId + "?" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<List<NewOrderDto>> getAllOrders(HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = ORDER_BASE_URL + "?" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Integer> getTotalOrdersByUserId(Integer userId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		Response<Integer> response = null;
		response = (Response<Integer>) restClientServiceImpl.getData("/order/totalOrders/" + userId, responseType);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Integer> getTotalOrders() {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		Response<Integer> response = null;
		response = (Response<Integer>) restClientServiceImpl.getData("/order/totalOrders", responseType);
		return response;
	}

	@Override
	public Response<?> getMostBoughtProducts() {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionLikeType(List.class, ProductDTO.class));
		Response<List<ProductDTO>> response = null;
		response = (Response<List<ProductDTO>>) restClientServiceImpl.getData("/order/most-bought-product", responseType);
		return response;
	}

	//Retrieve the user information of the respective order
	@Override
	public Response<UserDTO> getUserInfoOFOrder(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, UserDTO.class);
		Response<UserDTO> response = null;
		String infoUrl = ORDER_BASE_URL+"order-info/"+orderId;
		response = (Response<UserDTO>) restClientServiceImpl.getData(infoUrl, responseType);
		return response;
	}

	@Override
	public Response<List<PaymentDTO>> getPaymentInfoFromOrderId(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class,
						objectMapper.getTypeFactory().constructCollectionType(List.class, PaymentDTO.class));
		Response<List<PaymentDTO>> paymentInfo = null;
		paymentInfo = (Response<List<PaymentDTO>>) restClientServiceImpl.getData(ORDER_BASE_URL + "paymentDetail/" + orderId, responseType);
		return paymentInfo;
	}

	@Override
	public Response<List<NewOrderDto>> getAllOrdersReport(HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = ORDER_BASE_URL + "all/" +"?" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getDeliveredOrders(HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = ORDER_BASE_URL + "delivered/" +"?" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getCancelledOrders(HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = ORDER_BASE_URL + "cancelled/" +"?" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@Override
	public Response<List<OrderUpdateDTO>> getOrderUpdateByOrderId(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderUpdateDTO.class));
		Response<List<OrderUpdateDTO>> response = null;
		String url = ORDER_BASE_URL + "orderUpdates/" + orderId;
		response = (Response<List<OrderUpdateDTO>>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@Override
	public Response<?> addOrderUpdate(OrderUpdateDTO orderUpdate) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		Response<?> response = null;
		String url = ORDER_BASE_URL + "orderUpdates/add";
		response = restClientServiceImpl.postData(url, orderUpdate, responseType);
		return response;
	}

	@Override
	public Response<NewOrderDto> getSpecificOrderByOrderId(Integer orderId) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, NewOrderDto.class);
		Response<NewOrderDto> response = null;
		String url = ORDER_BASE_URL + "specificOrder/" + orderId;
		response = (Response<NewOrderDto>) restClientServiceImpl.getData(url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getAllFilteredOrders(String startDate, String endDate, HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = "/report/allOrdersRange?startDate="+startDate+"&endDate="+endDate + "&" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData( url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getAllFilteredOrdersCancelled(String startDate, String endDate, HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = "/report/ordersRange?startDate="+startDate+"&endDate="+endDate + "&status=3" +"&" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData( url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getAllFilteredOrdersDelivered(String startDate, String endDate,
			HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = "/report/ordersRange?startDate="+startDate+"&endDate="+endDate + "&status=2" +"&" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData( url, responseType);
		return response;
	}

	@Override
	public Response<List<NewOrderDto>> getAllFilteredOrdersProcessing(String startDate, String endDate,
			HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, NewOrderDto.class));
		Response<List<NewOrderDto>> response = null;
		String queryStrings = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.ORDER);
		String url = "/report/ordersRange?startDate="+startDate+"&endDate="+endDate + "&status=1" +"&" + queryStrings;
		response = (Response<List<NewOrderDto>>) restClientServiceImpl.getData( url, responseType);
		return response;
	}
	
}