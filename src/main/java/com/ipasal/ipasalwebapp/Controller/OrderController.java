package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.Services.OrderService;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;
import com.ipasal.ipasalwebapp.utilities.UserDetailsUtil;

import org.springframework.web.bind.annotation.GetMapping;


@SessionAttributes("cart")
@RequestMapping("/order")
@Controller
public class OrderController {
	private final OrderService orderService;
	Logger logger = LoggerFactory.getLogger(OrderController.class);

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
	@GetMapping
	public @ResponseBody ResponseEntity<Response<?>> getAllOrders(HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllOrders(request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/all")
	public @ResponseBody ResponseEntity<Response<?>> getAllOrdersReport(HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllOrdersReport(request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/allFromRange")
	public @ResponseBody ResponseEntity<Response<?>> getAllOrdersRangeReport(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrders(startDate, endDate, request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/delivered")
	public @ResponseBody ResponseEntity<Response<?>> getDeliveredOrdersReport(HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getDeliveredOrders(request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/deliveredFromRange")
	public @ResponseBody ResponseEntity<Response<?>> getDeliveredOrdersRangeReport(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersDelivered(startDate, endDate, request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	
	@GetMapping("/cancelled")
	public @ResponseBody ResponseEntity<Response<?>> getCancelledOrdersReport(HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getCancelledOrders(request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/cancelledFromRange")
	public @ResponseBody ResponseEntity<Response<?>> getCancelledOrdersRangeReport(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersCancelled(startDate, endDate, request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@GetMapping("/processingFromRange")
	public @ResponseBody ResponseEntity<Response<?>> getProcessingOrdersRangeReport(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletRequest request) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersProcessing(startDate, endDate, request);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/user/{userId}")
	public @ResponseBody ResponseEntity<Response<?>> getOrdersByUserId(HttpServletRequest request, @PathVariable("userId") Integer userId) {
		Response<List<NewOrderDto>> orders = (Response<List<NewOrderDto>>) orderService.getOrdersByUserId(request, userId);
		if(orders.getData() != null && orders.getData().size() > 0) {
			return new ResponseEntity<>(orders, HttpStatus.OK);
		}
		return new ResponseEntity<>(orders, HttpStatus.valueOf(orders.getStatus()));
	}
	
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public String getOrderDetailsByOrderId(@PathVariable("orderId") Integer orderId, Model model) {
		Response<List<ProductDTO>> orderDetail = (Response<List<ProductDTO>>) orderService.getOrderDetailsByOrderId(orderId);
		if(orderDetail.getData() != null && orderDetail.getData().size() > 0) {
			model.addAttribute("orderDetail", orderDetail.getData());
			model.addAttribute("orderId", orderId);
			return "customer/order-detail";
		}
		
		throw CustomExceptionThrowerUtil.throwException(orderDetail.getStatus(), orderDetail.getMessage());
		
	}
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/confirm", method = RequestMethod.POST) 
//	public String validateOrder(@ModelAttribute("guestUser") UserDTO user, HttpServletRequest request, SessionStatus status, RedirectAttributes redirectAttribute) {
//		HttpSession session = request.getSession();
//		NewOrderDto order = new NewOrderDto();
//		ShippingDTO shippingAddress = new ShippingDTO();
//		PaymentDTO payment = new PaymentDTO();
//		List<ProductDTO> products = new ArrayList<>();
//		Response<?> response;
//		Integer orderId= null;
//		
//		if(session.getAttribute("cart") != null) {
//			products = (ArrayList<ProductDTO>) session.getAttribute("cart");
//		}
//
//		List<Integer> productIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
//		List<Integer> quantities = products.stream().map(ProductDTO::getQuantity).collect(Collectors.toList());
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		
//		if(!(authentication instanceof AnonymousAuthenticationToken)) {
//			UserDTO loggedInUser = (UserDTO) authentication.getPrincipal();
//			shippingAddress.setAddress(loggedInUser.getStreet());
//			shippingAddress.setCity(loggedInUser.getCity());
//			shippingAddress.setEmail(loggedInUser.getEmail());
//			shippingAddress.setPhone(loggedInUser.getPhone());
//			shippingAddress.setZipcode(44601);
//			//payment.setPaymentType(1);
//			payment.setPaymentMethodId(4); //default cash
//			payment.setStatus(true);
//			order.setOrderDate(new Date());
//			order.setOrderStatus(1);
//			order.setOrderedBy(loggedInUser.getUserId());
//			order.setShippingAddress(shippingAddress);
//			order.setPaymentDetail(payment);
//			order.setProductIds(productIds);
//			order.setQuantities(quantities);
//			
//			response = orderService.confirmOrder(order);
//			orderId = Integer.valueOf(response.getData().toString());
//			logger.info("The response order id is ->"+response.getData());
//			HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatus());
//			if(HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
//				throw new ResourceNotFoundException(response.getMessage());
//			}
//			//orderService.confirmOrder(order);
//			//redirectAttribute.addFlashAttribute("message", response);
//			//remove cart from session
//			//status.setComplete();
////			session.removeAttribute("cart");
//	
//		} else {
//			shippingAddress.setAddress(user.getStreet());
//			shippingAddress.setCity(user.getCity());
//			shippingAddress.setEmail(user.getEmail());
//			shippingAddress.setPhone(user.getPhone());
//			shippingAddress.setZipcode(44601);
//			payment.setPaymentMethodId(4);;
//			payment.setStatus(true);
//			order.setOrderDate(new Date());
//			order.setOrderStatus(1);
//			order.setUser(user);
//			order.setShippingAddress(shippingAddress);
//			order.setPaymentDetail(payment);
//			order.setProductIds(productIds);
//			order.setQuantities(quantities);
//
//			//orderService.confirmOrder(order);
//			//remove cart from session
//			
//
//			response = orderService.confirmOrder(order);
//			orderId = Integer.valueOf(response.getData().toString());
//			HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatus());
//			if(HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
//				throw new ResourceNotFoundException(response.getMessage());
//			}
//			//redirectAttribute.addFlashAttribute("message", response);
//			//status.setComplete();
////			session.removeAttribute("cart");
//		}
//
//		return "redirect:/cart/payment-methods/"+orderId;
//	}

	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/cancel")
	public @ResponseBody ResponseEntity<String> cancelOrderById(@RequestParam("orderId") Integer orderId) {
		Response<?> response = orderService.cancelOrderByOrderId(orderId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping(value = "/cancel/{userId}/{orderId}")
	public @ResponseBody ResponseEntity<String> cancelOrderByUserId(@PathVariable("userId") Integer userId, @PathVariable("orderId") Integer orderId) {
		int sessionUserId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
        //Comparing the sessionUserId with the userId provided in the parameter so that they match
        if(sessionUserId == userId) {
        	Response<?> response = orderService.cancelOrderByOrderId(orderId);
        	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
        }
        else {
        	return null;
        }
	}
	@PostMapping(value = "/deliver")
	public @ResponseBody ResponseEntity<String> changeOrderStatusToDeliver(@RequestParam("orderId") Integer orderId) {
		//String responseMsg = 
		Response<?> response = orderService.chageOrderStatusToDeliverByOrderId(orderId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping(value = "/order-success")
	public String getOrderSuccessPage(Model model) {
		return "thank-you";
	}
	
	
	@SuppressWarnings("unchecked")
	@Secured("ROLE_ADMIN")
	@PostMapping("/orderUpdate/add")
	public @ResponseBody int addOrderUpdate(@RequestBody OrderUpdateDTO orderUpdate) {
		int userId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
		orderUpdate.setCommentedBy(userId);
		Response<Integer> result =  (Response<Integer>) orderService.addOrderUpdate(orderUpdate);
		return result.getData();
	}
	

}
