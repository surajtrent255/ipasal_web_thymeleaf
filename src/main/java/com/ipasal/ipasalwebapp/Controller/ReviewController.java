package com.ipasal.ipasalwebapp.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipasal.ipasalwebapp.Services.ReviewService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ReviewDTO;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;
import com.ipasal.ipasalwebapp.utilities.UserDetailsUtil;


@Controller
@RequestMapping("/review")
public class ReviewController {
	private ReviewService reviewService;
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	/**
	 * @author Yoomes <yoomesbhujel@gmail.com>
	 * @return
	 */
	//Gets all types of review from review controller
	@Secured("ROLE_ADMIN")
	@GetMapping
	public @ResponseBody Response<List<ReviewDTO>> getAllReviews(HttpServletRequest request) {
		Response<List<ReviewDTO>> reviewResponse = (Response<List<ReviewDTO>>) reviewService.getAllReviews(request);
		if(reviewResponse != null) {
			return reviewResponse;
		}
		
		throw CustomExceptionThrowerUtil.throwException(reviewResponse.getStatus(), reviewResponse.getMessage());
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/approved")
	public @ResponseBody Response<List<ReviewDTO>> getAllApprovedReviews(HttpServletRequest request) {
		Response<List<ReviewDTO>> reviewResponse = (Response<List<ReviewDTO>>) reviewService.getAllApprovedReviews(request);
		if(reviewResponse != null) {
			return reviewResponse;
		}
		
		throw CustomExceptionThrowerUtil.throwException(reviewResponse.getStatus(), reviewResponse.getMessage());
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/processing")
	public @ResponseBody Response<List<ReviewDTO>> getAllUnApprovedReviews(HttpServletRequest request) {
		Response<List<ReviewDTO>> reviewResponse = (Response<List<ReviewDTO>>) reviewService.getAllProcessingReviews(request);
		if(reviewResponse != null) {
			return reviewResponse;
		}
		
		throw CustomExceptionThrowerUtil.throwException(reviewResponse.getStatus(), reviewResponse.getMessage());
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/rejected")
	public @ResponseBody Response<List<ReviewDTO>> getAllRejectedReviews(HttpServletRequest request) {
		Response<List<ReviewDTO>> reviewResponse = (Response<List<ReviewDTO>>) reviewService.getAllRejectedReviews(request);
		if(reviewResponse != null) {
			return reviewResponse;
		}
		
		throw CustomExceptionThrowerUtil.throwException(reviewResponse.getStatus(), reviewResponse.getMessage());
	}
	
	@SuppressWarnings("unchecked")
	@Secured("ROLE_CUSTOMER")
	@PostMapping("/add")
	public @ResponseBody int addReview(@RequestBody ReviewDTO review) {
		int userId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
		review.setUserId(userId);
		Response<Integer> result = (Response<Integer>) reviewService.addReview(review);
		return result.getData();
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/{reviewId}")
	public String getReviewByReviewId(Model model, @PathVariable("reviewId") Integer reviewId) {
		Response<List<ReviewDTO>> response = (Response<List<ReviewDTO>>) reviewService.getReviewByReviewId(reviewId);
		if(response.getData() != null && response.getData().size() > 0) {
			ReviewDTO reviewDetails = response.getData().get(0);
			model.addAttribute("review", reviewDetails);
		}
		return "review-details";
	}
	
	
	@DeleteMapping("/{reviewId}")
	public @ResponseBody ResponseEntity<String> deleteReview(@PathVariable("reviewId") Integer reviewId) {
		Response<?> response = reviewService.deleteReviewByReviewId(reviewId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	
	@PostMapping("/status/2/reviewId/{reviewId}")
	public @ResponseBody ResponseEntity<String> approveReviewByReviewId(@PathVariable("reviewId") Integer reviewId) {
		Response<?> response = reviewService.approveReviewByReviewId(reviewId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping("/status/1/reviewId/{reviewId}")
	public @ResponseBody ResponseEntity<String> processReviewByReviewId(@PathVariable("reviewId") Integer reviewId) {
		Response<?> response = reviewService.processReviewByReviewId(reviewId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping("/status/3/reviewId/{reviewId}")
	public @ResponseBody ResponseEntity<String> rejectReviewByReviewId(@PathVariable("reviewId") Integer reviewId) {
		Response<?> response = reviewService.rejectReviewByReviewId(reviewId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PutMapping("/{reviewId}")
	public @ResponseBody ResponseEntity<String> updateReview(@PathVariable ("reviewId") Integer reviewId, @RequestBody ReviewDTO review) {
		Response<?> response = reviewService.updateReviewByReviewId(reviewId, review);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	
} 
