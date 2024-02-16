package com.ipasal.ipasalwebapp.Controller.Admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import com.ipasal.ipasalwebapp.Services.*;
import com.ipasal.ipasalwebapp.dto.*;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private CategoryServices categoryService;
	private OrderService orderService;
	private ProductServices productService;
	private UserService userService;
	private WishListService wishListService;
	private PaymentMethodService paymentMethodService;
	private MerchantServices merchantServices;
	private ReviewService reviewService;
	private AboutService aboutService;
	private ShippingService shippingService;
	private ReportService reportService;
	private SocialMediaService socialMediaService;
	private ToolBarMessageService toolBarMessageService;
	private HotDealsService hotDealsService;
	private PromotionalSalesService promotionalSalesService;
	private SliderService sliderService;
	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	public AdminController(CategoryServices categoryService, OrderService orderService,
						   ProductServices productService, UserService userService, AboutService aboutService,
						   WishListService wishListService, PaymentMethodService paymentMethodService, MerchantServices merchantServices, ReviewService reviewService, ShippingService shippingService, ReportService reportService, SocialMediaService socialMediaService, ToolBarMessageService toolBarMessageService, HotDealsService hotDealsService, PromotionalSalesService promotionalSalesService, SliderService sliderService) {
		this.categoryService = categoryService;
		this.orderService = orderService;
		this.productService = productService;
		this.userService = userService;
		this.wishListService = wishListService;
		this.paymentMethodService = paymentMethodService;
		this.merchantServices = merchantServices;
		this.reviewService = reviewService;
		this.aboutService = aboutService;
		this.shippingService = shippingService;
		this.reportService = reportService;
		this.socialMediaService = socialMediaService;
		this.toolBarMessageService = toolBarMessageService;
		this.hotDealsService = hotDealsService;
		this.promotionalSalesService = promotionalSalesService;
		this.sliderService = sliderService;
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
		UserDTO loggedInUser = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		loggedInUser = (UserDTO) authentication.getPrincipal();
		model.addAttribute("user", loggedInUser);
		return "admin/account-settings";
	}
	
	@GetMapping
	public String getAdminPanelView(Model model) {
		Integer totalOrders = 0;
		totalOrders = orderService.getTotalOrders().getData();
		model.addAttribute("totalOrders", totalOrders);
		
		Integer totalUnapprovedReviews = 0;
		totalUnapprovedReviews = (Integer) reviewService.totalProcessingReviews().getData();
		model.addAttribute("unapprovedReviews", totalUnapprovedReviews);
		
		return "admin/dashboard";
	}
	
	@GetMapping("/aboutUsEntry")
	public String getAboutUsEntryView(Model model) {
		Response<List<AboutDTO>> abtDTO = (Response<List<AboutDTO>>) aboutService.getAboutInfo();
		model.addAttribute("aboutInfo", abtDTO);
		return "admin/about-us-entry";
	}
	
	@GetMapping("/toolbarEntry")
	public String getToolBarEntryView(Model model) {
		Response<List<SocialMediaDTO>> socialMediass = (Response<List<SocialMediaDTO>>) socialMediaService.getAllSocialMedia();
		List<SocialMediaDTO> socialMedias = socialMediass.getData();
		model.addAttribute("socialMedias", socialMedias);
		Response<ToolBarMessageDTO> toolbarMessagez = (Response<ToolBarMessageDTO>) toolBarMessageService.getToolBarMessage();
		ToolBarMessageDTO toolbarMessage = toolbarMessagez.getData();
		model.addAttribute("toolbarMessage", toolbarMessage);
		return "admin/toolbarEntry";
	}
	
	@GetMapping("/hotDealsEntry")
	public String getHotDealsEntryView(Model model) {
		Response<List<HotDealsDTO>> hotDealsList = (Response<List<HotDealsDTO>>) hotDealsService.getAllHotDeal();
		List<HotDealsDTO> hotDeals = new ArrayList<>();
		if(hotDealsList.getData() != null && hotDealsList.getData().size() > 0) {
			hotDeals = hotDealsList.getData();
			model.addAttribute("hotDeals", hotDeals);
		}
		return "admin/hot-deals-entry";
	}
	
	@GetMapping("/sliderEntry")
	public String getSliderEntryView(Model model) {
		Response<List<CategoryDTO>> categoriesList = (Response<List<CategoryDTO>>) categoryService.getFeaturedCategories();
		Response<List<CategoryDTO>> parentCategoryResponse = (Response<List<CategoryDTO>>) categoryService.getAllParentCategory();
		Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryService.getAllCategories();
		Response<List<SliderDTO>> slidersList = (Response<List<SliderDTO>>) sliderService.getAllSliders();
		List<CategoryDTO> categories = new ArrayList<>();
		if(categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
			categories = categoryResponse.getData();
			//Following code filters category list based on root parent id
			//and returns top level categories.
			categories = categories.stream().filter(category -> (category.getParentId() == 0)).collect(Collectors.toList());
			model.addAttribute("categories", categories);
			List<SliderDTO> sliders = slidersList.getData();
			model.addAttribute("sliders", sliders);
			return "admin/slider-entry";
		}

		throw CustomExceptionThrowerUtil.throwException(categoryResponse.getStatus(), categoryResponse.getMessage());

	}

	/*
	* Controller for shipping details entry
	* Retrieving the shipping detail entry page
	* */
	@GetMapping("/shipping-rate")
	public String getShippingDetailEntryPage(Model model){
		return "admin/shipping-entry";
	}
	
	
	@GetMapping("/orders") 
	public String getOrders(Model model, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = orderService.getAllOrders(request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("orders", response);
		return "admin/orders";
	}
	
	@GetMapping("/ordersreport")
	public String getOrdersReport(Model model, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = orderService.getAllOrdersReport(request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("orders", response);
		return "admin/ordersreport";
	}

	@GetMapping("/delivered")
	public String getDeliveredOrders(Model model, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = orderService.getDeliveredOrders(request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("orders", response);
		return "admin/orders-delivered";
	}
	
	@GetMapping("/cancelled")
	public String getCancelledOrders(Model model, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = orderService.getCancelledOrders(request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("orders", response);
		return "admin/orders-cancelled";
	}
	
	@RequestMapping(value = "order/{orderId}", method = RequestMethod.GET)
	public String getOrderDetailsByOrderId(@PathVariable("orderId") Integer orderId, Model model) {
		Response<List<ProductDTO>> orderDetail = (Response<List<ProductDTO>>) orderService.getOrderDetailsByOrderId(orderId);
		Response<List<PaymentDTO>> paymentInfo = (Response<List<PaymentDTO>>) orderService.getPaymentInfoFromOrderId(orderId);
		Response<UserDTO> userInfoOfOrder = orderService.getUserInfoOFOrder(orderId);
		Response<List<ShippingDTO>> shippingInfo = (Response<List<ShippingDTO>>) shippingService.getShippingDetailsByOrderId(orderId);

		
		if (userInfoOfOrder != null){
			model.addAttribute("userInfo", userInfoOfOrder.getData());
		}
		
		if(paymentInfo != null && paymentInfo.getData() != null && paymentInfo.getData().size() > 0) {
			model.addAttribute("paymentInfo", paymentInfo.getData().get(0));
		}
		
		if (shippingInfo != null){
			model.addAttribute("shippingInfo", shippingInfo.getData().get(0));
		}
		
		if(orderDetail.getData() != null && orderDetail.getData().size() > 0) {
			model.addAttribute("orderDetail", orderDetail.getData());
			model.addAttribute("orderId", orderId);
			return "admin/order-details";
		}
	

		throw CustomExceptionThrowerUtil.throwException(orderDetail.getStatus(), orderDetail.getMessage());

	}
	
	@RequestMapping(value = "order/{orderId}/updates", method = RequestMethod.GET)
	public String getOrderUpdatesByOrderId(@PathVariable("orderId") Integer orderId, Model model) {
			model.addAttribute("orderId", orderId);
			Response<List<OrderUpdateDTO>> orderUpdates = (Response<List<OrderUpdateDTO>>) orderService.getOrderUpdateByOrderId(orderId);
			if (orderUpdates != null) {
				List<OrderUpdateDTO> orderUpdateDTO = orderUpdates.getData();
				model.addAttribute("orderUpdates", orderUpdateDTO);
			}
			Response<NewOrderDto> orderDto = orderService.getSpecificOrderByOrderId(orderId);
			NewOrderDto order = orderDto.getData();
			model.addAttribute("specificOrder", order);
			return "admin/order-updates";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/productEntry")
	public String getProductEntryPage(Model model) {
		Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryService.getAllCategories();
		List<CategoryDTO> categories = new ArrayList<>();
		if(categoryResponse != null && categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
			categories = categoryResponse.getData();
			//Following code filters category list based on root parent id 
			//and returns top level categories.
			categories = categories.stream().filter(category -> (category.getParentId() == 0)).collect(Collectors.toList());
			model.addAttribute("categories", categories);
			
			Response<List<MerchantDTO>> merchantResponse = (Response<List<MerchantDTO>>) merchantServices.getAllMerchant();
			List<MerchantDTO> merchants = merchantResponse.getData();
			model.addAttribute("merchants", merchants);
			
			return "admin/product-entry";
			
		}
		
		throw CustomExceptionThrowerUtil.throwException(categoryResponse.getStatus(), categoryResponse.getMessage());
		
		
	}
	
	@GetMapping("/categoryEntry")
	public String getCategoryEntryPage(Model model) {
		return "admin/category-entry";
	}

	@GetMapping("/stocks")
	public String getStocks() {
		return "admin/stocks";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/manageStock/{productId}")
	public String manageStock(@PathVariable("productId") Integer productId, Model model) {
		Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productService.getProductById(productId);
		if(response.getData() != null && response.getData().size() > 0) {
			model.addAttribute("product", response.getData().get(0)); //why get(0)? because it always contains one DTO
			return "admin/manage-stock";
		}
		
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/editItem/{productId}")
	public String editProduct(@PathVariable("productId") Integer productId, Model model) {
		ProductDTO product = null;
		Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productService.getProductById(productId);
		if(response.getData() != null && response.getData().size() > 0) {
			product = response.getData().get(0);
			model.addAttribute("product", product);
			return "admin/edit-item";
		}
		
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
	}

	// Retrieving all whole seller list
	@GetMapping("/whole-seller")
	public String getAllWholeSeller(Model model){
		Response<List<UserDTO>> wholeSellerResponse = (Response<List<UserDTO>>) userService.getAllWholeSeller();
		if (wholeSellerResponse.getData() != null && wholeSellerResponse.getData().size() > 0){
			model.addAttribute("wholeSellerList", wholeSellerResponse.getData());
			return "admin/whole-seller";
		}
		
		throw CustomExceptionThrowerUtil.throwException(wholeSellerResponse.getStatus(), wholeSellerResponse.getMessage());
	}

    // Retrieving all whole seller list
    @GetMapping("/whole-seller/{Id}")
    public @ResponseBody
    UserDTO
    getAllWholeSeller(@PathVariable Integer Id, Model model){
        UserDTO wholeSeller = null;
        Response<UserDTO> wholeSellerResponse = (Response<UserDTO>) userService.searchWholeSeller(Id);
        if (wholeSellerResponse != null){
            wholeSeller = wholeSellerResponse.getData();
            return wholeSeller;
        }else {
			return null;
		}
    }

    //Retrieving the product list of the specific whole-seller
	@GetMapping("/whole-seller/{id}/all")
	public String getWishListOfWholeSeller(@PathVariable Integer id, Model model){
		List<ProductDTO> wishProductList = null;
		Response<List<ProductDTO>> wishProductResponse = (Response<List<ProductDTO>>) wishListService.getWishListOfUser(id) ;
		if (wishProductResponse != null && wishProductResponse.getData().size() >0){
			wishProductList = wishProductResponse.getData();
			for (ProductDTO productDTO: wishProductList){
				logger.info("User ID for whole seller is ->"+productDTO.toString());
			}
			model.addAttribute("productList", wishProductList);
			return "admin/whole-seller-wish-list";
		}else {
			return "error";
		}
	}

	@GetMapping("/payment-methods")
	public String getPaymentMethods(Model model){
		Response<List<PaymentMethodDTO>> paymentMethodResponse = (Response<List<PaymentMethodDTO>>) paymentMethodService.getAllPaymentMethods();
		if (paymentMethodResponse != null && paymentMethodResponse.getData().size() >0){
			model.addAttribute("allPaymentMethods", paymentMethodResponse.getData());
		}
		return "admin/payment-methods";
	}

	@GetMapping("/payment-methods/entry")
	public String getPaymentMethodsEntry(Model model){
		List<CategoryDTO> categories = new ArrayList<>();
		Response<List<CategoryDTO>> response = (Response<List<CategoryDTO>>) categoryService.getAllCategories();
		if(response.getData() != null && response.getData().size() > 0) {
			categories = response.getData();
			categories = categories.stream().filter(category -> (category.getParentId() == 0)).collect(Collectors.toList());
			model.addAttribute("categories", categories);

		} else {
			model.addAttribute("categories", null);

		}
		return "admin/payment-entry";
	}

	@PostMapping("/payment-methods/add")
	public String addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO, Model model){
		Response<Integer> insertPaymentResult = (Response<Integer>) paymentMethodService.insertPaymentMethods(paymentMethodDTO);
		return "admin/payment-methods";
	}

	@GetMapping("payment-methods/edit/{id}")
	public String editPaymentMethods(Model model, @PathVariable("id") Integer id){
		Response<PaymentMethodDTO> paymentMethodDTOResponse = (Response<PaymentMethodDTO>) paymentMethodService.getPaymentMethodById(id);
		if (paymentMethodDTOResponse.getData() != null){
			PaymentMethodDTO paymentMethodDTO = paymentMethodDTOResponse.getData();
			model.addAttribute("paymentMethodDetails", paymentMethodDTO);
		}
		return "admin/payment-edit";
	}

	@PostMapping("/payment-methods/update/{id}")
	public @ResponseBody
	ResponseEntity<String> updatePaymentMethod(@PathVariable Integer id,
									   @RequestBody PaymentMethodDTO paymentMethodDTO, Model model){
		Response<List> updatePaymentResponse = (Response<List>) paymentMethodService.updatePaymentMethod(id,paymentMethodDTO);
		return new ResponseEntity<>(updatePaymentResponse.getMessage(), HttpStatus.valueOf(updatePaymentResponse.getStatus()));
	}

	/*
	* Listing all the parent and sub-categories in a table
	* returns List of CategoryDTO whose parent Id is 0, and deleted status is false
	* */
	@GetMapping("/category/parent/all")
	public String getAllParentCategoryList(Model model){
		Response<List<CategoryDTO>> parentCategoryListResponse = (Response<List<CategoryDTO>>) categoryService.getAllParentCategory();
		List<CategoryDTO> allParentCategories = parentCategoryListResponse.getData();
		model.addAttribute("parentCategoryList", allParentCategories);
		return "admin/category-list";
	}

	/*
	 * Listing all the parent and sub-categories in a table
	 * returns List of CategoryDTO whose parent Id is 0, and deleted status is false
	 * */
	@GetMapping("/category/parent/{parentId}")
	public String getAllChildCategoryList(@PathVariable Integer parentId, Model model){
		Response<List<CategoryDTO>> subCategoryListResponse = (Response<List<CategoryDTO>>) categoryService.getCategoryByParentId(parentId);
		List<CategoryDTO> allSubCategories = subCategoryListResponse.getData();
		model.addAttribute("subCategoryList", allSubCategories);
		return "admin/sub-category-list";
	}

	/*
	* Listing all the customers in a table
	* @return List of UserDTO
	* */
	@GetMapping("/customers")
	public String getAllCustomers(Model model){
		Response<List<UserDTO>> customersListResponse = (Response<List<UserDTO>>) userService.getAllCustomers();
		List<UserDTO> allCustomers = customersListResponse.getData();
		model.addAttribute("customersList", allCustomers);
		return "admin/customers-list";
	}
	
	
	/** This is the merchant part of the program **/
	
	@GetMapping("/merchantEntry")
	public String getMerchantEntryPage(Model model) {
		return "admin/merchant-entry";
	}
	
	
	// Retrieving all merchant list
	
	@GetMapping("/merchantList")
	public String getAllMerchant(Model model){
		Response<List<MerchantDTO>> merchantListResponse = (Response<List<MerchantDTO>>) merchantServices.getAllMerchant();
		List<MerchantDTO> allMerchants = merchantListResponse.getData();
		model.addAttribute("merchantList", allMerchants);
		return "admin/merchant-list";
	}
	
	@GetMapping("/editMerchant/{merchantId}")
	public String editMerchant(@PathVariable("merchantId") Integer merchantId, Model model) {
		MerchantDTO merchant = null;
		Response<List<MerchantDTO>> response = (Response<List<MerchantDTO>>) merchantServices.getMerchantById(merchantId);
		if(response.getData() != null && response.getData().size() > 0) {
			merchant = response.getData().get(0); //Get 0 because there is only one merchant
			model.addAttribute("merchant", merchant);
			return "admin/edit-merchant";
		}
		
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
	}
	

	@SuppressWarnings("unchecked")
	@GetMapping("/reviewList")
	public String getAllReviews(HttpServletRequest request, Model model){
		Response<List<ReviewDTO>> reviewListResponse = (Response<List<ReviewDTO>>) reviewService.getAllReviews(request);
		List<ReviewDTO> allReviews = reviewListResponse.getData();
		Integer countAllReviews = (Integer) reviewService.totalReviews().getData();
		model.addAttribute("totalReviews", countAllReviews);
		model.addAttribute("reviewList", allReviews);
		return "admin/review-list";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/processing")
	public String getAllProcessingReviews(HttpServletRequest request, Model model) {
		Response<List<ReviewDTO>> reviewListResponse = (Response<List<ReviewDTO>>) reviewService.getAllProcessingReviews(request);
		List<ReviewDTO> processingReviews = reviewListResponse.getData();
		Integer countProcessingReviews = (Integer) reviewService.totalProcessingReviews().getData();
		model.addAttribute("processingReviews", processingReviews);
		model.addAttribute("totalProcessingReviews", countProcessingReviews);
		return "admin/review-list-processing";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/rejected")
	public String getAllRejectedReviews(HttpServletRequest request, Model model) {
		Response<List<ReviewDTO>> reviewListResponse = (Response<List<ReviewDTO>>) reviewService.getAllRejectedReviews(request);
		List<ReviewDTO> rejectedReviews = reviewListResponse.getData();
		Integer countRejectedReviews = (Integer) reviewService.totalRejectedReviews().getData();
		model.addAttribute("rejectedReviews", rejectedReviews);
		model.addAttribute("totalRejectedReviews", countRejectedReviews);
		return "admin/review-list-rejected";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/approved")
	public String getAllApprovedReviews(HttpServletRequest request, Model model) {
		Response<List<ReviewDTO>> reviewListResponse = (Response<List<ReviewDTO>>) reviewService.getAllApprovedReviews(request);
		List<ReviewDTO> approvedReviews = reviewListResponse.getData();
		Integer countApprovedReviews = (Integer) reviewService.totalApprovedReviews().getData();
		model.addAttribute("approvedReviews", approvedReviews);
		model.addAttribute("totalApprovedReviews", countApprovedReviews);
		return "admin/review-list-approved";
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/review/{reviewId}")
	public String getReviewByReviewId(@PathVariable("reviewId") Integer reviewId, Model model) {
		Response<ReviewDTO> reviewDetail = (Response<ReviewDTO>) reviewService.getReviewByReviewId(reviewId);
		if(reviewDetail.getData() != null) {
			ReviewDTO review = ((List<ReviewDTO>) reviewDetail.getData()).get(0);
			model.addAttribute("review", review);
			return "admin/review-details";
		}
		throw CustomExceptionThrowerUtil.throwException(reviewDetail.getStatus(), reviewDetail.getMessage());
	}
/*
 * Response<List<NewOrderDto>> response = orderService.getCancelledOrders(request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("orders", response);
		return "admin/orders-cancelled"	
 */
	
	@GetMapping("/salesreport")
	public String getReports(Model model, HttpServletRequest request) {
		Response<List<ProductDTO>> response = reportService.getTopSales(request);
		model.addAttribute("topSales", response.getData());
		return "admin/salesreport";
	}
	
	@GetMapping("/salesReport/range")
	public String getReportsBetweenRange(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		Response<List<ProductDTO>> reportResponse = (Response<List<ProductDTO>>) reportService.getReportsBetweenRange(startDate, endDate); 
		model.addAttribute("topSales", reportResponse.getData());
		return "admin/salesreport";
	}
	
	@GetMapping("/orderReport/range")
	public String getOrderReportsBetweenRange(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrders(startDate, endDate, request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("orders", response);
		return "admin/ordersreport";	}
	
	@GetMapping("/cancelledOrderReport/range")
	public String getCancelledOrderReportsBetweenRange(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("status") Integer status, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersCancelled(startDate, endDate, request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("orders", response);
		return "admin/orders-cancelled";	}
	
	@GetMapping("/deliveredOrderReport/range")
	public String getDeliveredOrderReportsBetweenRange(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("status") Integer status, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersDelivered(startDate, endDate, request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("orders", response);
		return "admin/orders-delivered";	}
	
	@GetMapping("/processingOrderReport/range")
	public String getProcessingOrderReportsBetweenRange(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("status") Integer status, HttpServletRequest request) {
		Response<List<NewOrderDto>> response = (Response<List<NewOrderDto>>) orderService.getAllFilteredOrdersProcessing(startDate, endDate, request);
		int currentPageTotalOrders = response.getData().size();
		int lastSeenId = 0;
		if(currentPageTotalOrders > 0) {
			lastSeenId = response.getData().get(currentPageTotalOrders - 1).getOrderId();
		}
		model.addAttribute("lastSeenId", lastSeenId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("orders", response);
		return "admin/orders";	}
	
	
	@GetMapping("/promoSalesList")
	public String getPromotionalSalesListingView(Model model) {
		Response<List<PromotionalSalesDTO>> promotions = (Response<List<PromotionalSalesDTO>>) promotionalSalesService.getAllPrmotions();
		List<PromotionalSalesDTO> promo = promotions.getData(); 
		model.addAttribute("promotionals", promo);
		return "admin/promotional-sales-listing";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/promoEdit/{promotionalSalesId}")
	public String editPromotionalSales(@PathVariable("promotionalSalesId") Integer promotionalSalesId, Model model) {
		PromotionalSalesDTO promotional = null;
		Response<List<PromotionalSalesDTO>> response = (Response<List<PromotionalSalesDTO>>) promotionalSalesService.getPromotionalSalesById(promotionalSalesId);
		if(response.getData() != null && response.getData().size() > 0) {
			promotional = response.getData().get(0);
			model.addAttribute("promo", promotional);
			return "admin/promotional-edit";
		}
		
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
	}
}
