package com.ipasal.ipasalwebapp.Services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import com.ipasal.ipasalwebapp.dto.NewOrderDto;
import com.ipasal.ipasalwebapp.dto.OrderUpdateDTO;
import com.ipasal.ipasalwebapp.dto.PaymentDTO;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;

public interface OrderService {
    Response<?> confirmOrder(NewOrderDto requestObject);
    Response<?> getOrdersByUserId(Integer userId, Pageable pageable);
    Response<?> getOrdersByUserId(HttpServletRequest request, Integer userId);
	Response<?> cancelOrderByOrderId(Integer orderId);
	Response<List<ProductDTO>> getOrderDetailsByOrderId(Integer orderId);
	Response<List<NewOrderDto>> getAllOrders(Pageable pageable);
	Response<List<NewOrderDto>> getAllOrders(HttpServletRequest request);
	Response<?> chageOrderStatusToDeliverByOrderId(Integer orderId);
	Response<Integer> getTotalOrdersByUserId(Integer userId);
	Response<Integer> getTotalOrders();
	
//	Retrieving the list of the product ids of most bought product
	Response<?> getMostBoughtProducts();
	
	//Getting user info of the respective order
	Response<UserDTO> getUserInfoOFOrder(Integer orderId);
	
	Response<List<PaymentDTO>> getPaymentInfoFromOrderId(Integer orderId);

	Response<List<NewOrderDto>> getAllOrdersReport(HttpServletRequest request);

	Response<List<NewOrderDto>> getDeliveredOrders(HttpServletRequest request);
	
	Response<List<NewOrderDto>> getCancelledOrders(HttpServletRequest request);
	
	Response<List<OrderUpdateDTO>> getOrderUpdateByOrderId(Integer orderId);
	
	Response<?> addOrderUpdate(OrderUpdateDTO orderUpdate);

	Response<NewOrderDto> getSpecificOrderByOrderId(Integer orderId);

	Response<List<NewOrderDto>> getAllFilteredOrders(String startDate, String endDate, HttpServletRequest request);
	
	Response<List<NewOrderDto>> getAllFilteredOrdersCancelled(String startDate, String endDate, HttpServletRequest request);
	
	Response<List<NewOrderDto>> getAllFilteredOrdersDelivered(String startDate, String endDate, HttpServletRequest request);

	Response<List<NewOrderDto>> getAllFilteredOrdersProcessing(String startDate, String endDate,
			HttpServletRequest request);
}