package com.ipasal.ipasalwebapp.ServiceImpl;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.ReviewService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ReviewDTO;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;



@Service
public class ReviewServiceImpl implements ReviewService {
	
	private final String REVIEW_BASE_URL = "/review";
	private RestClientService restClientService;
	
	
	public ReviewServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}
	
	
	

	@Override
	public Response<?> addReview(ReviewDTO review) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		
		Response<?> response = null;
		response =  restClientService.postData(REVIEW_BASE_URL, review, responseType);
		return response;
	}

	
	
	@Override
	public Response<?> getReviewByReviewId(Integer reviewId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		Response<?> response = null;
		response = restClientService.getData(REVIEW_BASE_URL + "/" + reviewId, responseType);
		return response;
	}

	
	
	@Override
	public Response<?> getAllReviews(HttpServletRequest request) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
			objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		String customQuery = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.REVIEW);
		response = restClientService.getData(REVIEW_BASE_URL +"?"+ customQuery, responseType);
		return response;
	}

	
	
	@Override
	public Response<?> deleteReviewByReviewId(Integer reviewId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = REVIEW_BASE_URL + "/" + reviewId;
		Response<?> response = restClientService.deleteData(url, null, responseType);
		return response;
	}


	@Override
	public Response<?> updateReviewByReviewId(Integer reviewId, ReviewDTO review) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = REVIEW_BASE_URL + "/" + reviewId;
		Response<?> response = restClientService.putData(url, review, responseType);
		return response;
	}
	
	
	
	
	@Override
	public Response<?> getAllReviewsByUserId(Integer userId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
				objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		String url = REVIEW_BASE_URL + "/" + userId + "/user";
		response = restClientService.getData(url, responseType);
		return response;
	}
	

	
	@Override
	public Response<?> totalReviewsByUserId(Integer userId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		response = restClientService.getData(REVIEW_BASE_URL + "/totalReviews/user/" + userId, responseType);
		return response;
	}

	
	@Override
	public Response<?> totalReviews() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		response = restClientService.getData(REVIEW_BASE_URL + "/totalReviews", responseType);
		return response;
	}



	@Override
	public Response<?> totalProcessingReviews() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		response = restClientService.getData(REVIEW_BASE_URL + "/totalReviews/unapproved", responseType);
		return response;
	}
	
	
	
	@Override
	public Response<?> getAllProcessingReviews(HttpServletRequest request) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
			objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		String queryParamsForUrl = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.REVIEW);
		response = restClientService.getData(REVIEW_BASE_URL + "/processing?" + queryParamsForUrl, responseType);
		return response;
	}
	
	
	
	@Override
	public Response<?> getAllRejectedReviews(HttpServletRequest request) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		String queryParamsForUrl = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.REVIEW);
		response = restClientService.getData(REVIEW_BASE_URL + "/rejected?" + queryParamsForUrl, responseType);
		return response;
	}

	
	
	@Override
	public Response<?> totalRejectedReviews() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		response = restClientService.getData(REVIEW_BASE_URL + "/totalReviews/rejected", responseType);
		return response;
	}

	
	
	@Override
	public Response<?> getAllApprovedReviews(HttpServletRequest request) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDTO.class));
		String queryParamsForUrl = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.REVIEW);
		response = restClientService.getData(REVIEW_BASE_URL + "/approved?" + queryParamsForUrl, responseType);
		return response;
	}

	
	
	@Override
	public Response<?> totalApprovedReviews() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		response = restClientService.getData(REVIEW_BASE_URL + "/totalReviews/approved", responseType);
		return response;
	}
	
	
	
	@Override
	public Response<?> approveReviewByReviewId(Integer reviewId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = REVIEW_BASE_URL + "/approve/" + reviewId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}
	


	@Override
	public Response<?> processReviewByReviewId(Integer reviewId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = REVIEW_BASE_URL + "/process/" + reviewId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}

	

	@Override
	public Response<?> rejectReviewByReviewId(Integer reviewId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = REVIEW_BASE_URL + "/reject/" + reviewId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}
		
}
