package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.SocialMediaService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;

/**
 * 
 * @author Tanchhowpa
 * Sep 5, 2019, 2:00:14 PM
 *
 */


@Service
public class SocialMediaServiceImpl implements SocialMediaService {

	private final String SOCIAL_BASE_URL = "/social";
	private RestClientService restClientService;
	
	
	public SocialMediaServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}


	@Override
	public Response<?> addSocialMedia(SocialMediaDTO socialMedia) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
		Response<?> response = null;
		response = restClientService.postData(SOCIAL_BASE_URL, socialMedia, responseType);
		return response;
	}


	@Override
	public Response<List<SocialMediaDTO>> getAllSocialMedia() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, SocialMediaDTO.class));
		Response<List<SocialMediaDTO>> response = null;
		response = (Response<List<SocialMediaDTO>>) restClientService.getData("/social" , responseType);
		return response;
	}


	@Override
	public Response<?> getAllActiveSocialMedias() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, SocialMediaDTO.class));
		Response<List<SocialMediaDTO>> response = null;
		response = (Response<List<SocialMediaDTO>>) restClientService.getData("/social/active" , responseType);
		return response;
	}


	@Override
	public Response<?> getSocialMediaById(Integer socialMediaId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, SocialMediaDTO.class));
		Response<?> response = null;
		response = restClientService.getData(SOCIAL_BASE_URL + "/" + socialMediaId , responseType);
		return response;
	}


	@Override
	public Response<?> updateSocialMedia(Integer socialMediaId, SocialMediaDTO socialMedia) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		Response<?> response = restClientService.putData(SOCIAL_BASE_URL + "/" + socialMediaId, socialMedia, responseType);
		return response;
	}


	@Override
	public Response<?> setSocialMediaActive(Integer socialMediaId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = SOCIAL_BASE_URL + "/setActive/" + socialMediaId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}


	@Override
	public Response<?> setSocialMediaInactive(Integer socialMediaId) {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
		String url = SOCIAL_BASE_URL + "/setInactive/" + socialMediaId;
		response = restClientService.postData(url, null, responseType);
		return response;
	}

}
