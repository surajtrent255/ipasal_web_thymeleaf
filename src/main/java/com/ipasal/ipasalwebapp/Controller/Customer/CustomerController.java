package com.ipasal.ipasalwebapp.Controller.Customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.Services.CategoryServices;
import com.ipasal.ipasalwebapp.Services.OrderService;
import com.ipasal.ipasalwebapp.Services.ReviewService;
import com.ipasal.ipasalwebapp.Services.UserService;
import com.ipasal.ipasalwebapp.dto.NewOrderDto;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ReviewDTO;
import com.ipasal.ipasalwebapp.dto.UserDTO;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;
import com.ipasal.ipasalwebapp.utilities.UserDetailsUtil;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/customer")
@Controller
public class CustomerController {
    private static final String CUSTOMER_URL_PREFIX = "customer";
    private OrderService orderService;
    private ReviewService reviewService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    public CustomerController(OrderService orderService, ReviewService reviewService, CategoryServices categoryService, UserService userService) {

        this.orderService = orderService;
        this.reviewService = reviewService;
        this.userService = userService;
    }
    @PutMapping("/user/{userId}")
    public @ResponseBody ResponseEntity<String> updateUser(@PathVariable("userId") Integer userId,
    		@RequestBody UserDTO user) {
    	String password;
    	//user.setAuthorities(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	password = ((UserDTO) authentication.getPrincipal()).getPassword();
    	user.setPassword(password);
    	Response<?> response = userService.updateUserInfoById(userId, user);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }

    
    @GetMapping("/accountSetting")
    public String getAccountSetting(Model model) {
    	UserDTO user = new UserDTO();
    	UserDTO loggedInUser = null;
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	loggedInUser = (UserDTO) authentication.getPrincipal();
    	model.addAttribute("user",loggedInUser);
    	String pwd = loggedInUser.getPassword();
    	model.addAttribute("pword", pwd);
    	return CUSTOMER_URL_PREFIX + "/account-settings";
    }
    
    @GetMapping
    public String getCutomerPage(@AuthenticationPrincipal UserDTO user, Model model) {
    	Integer totalOrders = orderService.getTotalOrdersByUserId(user.getUserId()).getData();
    	Integer totalReviews = (Integer) reviewService.totalReviewsByUserId(user.getUserId()).getData();
    	model.addAttribute("totalOrders", totalOrders);
    	model.addAttribute("totalReviews", totalReviews);
        return CUSTOMER_URL_PREFIX +"/dashboard";
    }

    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/order", method = RequestMethod.GET)
    public String getOrders(HttpServletRequest request, 
    		@RequestParam("userId") Integer userId,
    		Model model) {
    	Response<List<NewOrderDto>> orders = null;
    	int sessionUserId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId(); //Checking for session userId with the userId parameter sent so that it matches					
    	if(sessionUserId == userId) {
    	orders = (Response<List<NewOrderDto>>) orderService.getOrdersByUserId(request, userId);//(Response<List<NewOrderDto>>) orderService.getOrdersByUserId(userId, pageable);
    	if(orders.getData() != null && orders.getData().size() > 0) {
        	model.addAttribute("orders", orders);
        	model.addAttribute("userId", userId);
            return  CUSTOMER_URL_PREFIX +"/order";
        }
        throw CustomExceptionThrowerUtil.throwException(orders.getStatus(), orders.getMessage());
    	}
    	return "unauthorized";
    }
    
    
    @SuppressWarnings("unchecked")
	@GetMapping("/reviewList/{userId}") 
    public String getAllReviewsByUserId(Model model, @PathVariable("userId") Integer userId) {
    	Response<List<ReviewDTO>> userReview = null;
    	int sessionUserId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
    	if(sessionUserId == userId) {
    		userReview = (Response<List<ReviewDTO>>) reviewService.getAllReviewsByUserId(userId);
    			model.addAttribute("userReview", userReview);
    			return CUSTOMER_URL_PREFIX + "/customer-review-list";
    	}
    	else {
    		return "unauthorized";
    	}
    }

    
    @GetMapping("/editReview/{reviewId}")
    public String editReviewByReviewId(Model model, @PathVariable("reviewId") Integer reviewId) {
    	ReviewDTO review = null;
    	int sessionUserId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
    	Response<List<ReviewDTO>> response = (Response<List<ReviewDTO>>) reviewService.getReviewByReviewId(reviewId);
    	int userId = response.getData().get(0).getUserId();	
    	
    	if(sessionUserId == userId) {
    	if(response.getData() != null) {
    		review = response.getData().get(0);
    		model.addAttribute("review", review);
    		return "customer/edit-review";
    	}
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    	}
    	else {
    		return "unauthorized";
    	}
    }
}