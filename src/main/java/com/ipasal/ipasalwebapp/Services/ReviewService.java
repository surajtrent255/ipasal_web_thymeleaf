package com.ipasal.ipasalwebapp.Services;




import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ReviewDTO;

public interface ReviewService {

	Response<?> addReview(ReviewDTO review);

	Response<?> getReviewByReviewId(Integer reviewId);

	Response<?> getAllReviews(HttpServletRequest request);

	Response<?> deleteReviewByReviewId(Integer reviewId);

	//Retrieving all the reviews by a certain user
	Response<?> getAllReviewsByUserId(Integer userId);

	Response<?> totalReviewsByUserId(Integer userId);
	
	Response<?> totalReviews();
	
	Response<?> totalProcessingReviews();

	Response<?> getAllProcessingReviews(HttpServletRequest request);

	Response<?> getAllRejectedReviews(HttpServletRequest request);

	Response<?> totalRejectedReviews();

	Response<?> getAllApprovedReviews(HttpServletRequest request);

	Response<?> totalApprovedReviews();

	Response<?> updateReviewByReviewId(Integer reviewId, ReviewDTO review);
	
	
	
	
//	Response<?> getAllReviewsByUserId(Integer userId, Pageable pageable);
//	Response<?> getAllReviewsByUserId(HttpServletRequest request, Integer userId);

	Response<?> approveReviewByReviewId(Integer reviewId);
	
	Response<?> processReviewByReviewId(Integer reviewId);

	Response<?> rejectReviewByReviewId(Integer reviewId);




}
