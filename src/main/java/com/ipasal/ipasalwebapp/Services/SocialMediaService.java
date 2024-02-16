package com.ipasal.ipasalwebapp.Services;


import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;

/**
 * 
 * @author Tanchhowpa
 * Sep 5, 2019, 1:59:36 PM
 *
 */

public interface SocialMediaService {

	Response<?> addSocialMedia(SocialMediaDTO socialMedia);
	
	Response<?> getAllSocialMedia();

	Response<?> getAllActiveSocialMedias();
	Response<?> getSocialMediaById(Integer socialMediaId);
	Response<?> updateSocialMedia(Integer socialMediaId, SocialMediaDTO socialMedia);

	Response<?> setSocialMediaActive(Integer socialMediaId);

	Response<?> setSocialMediaInactive(Integer socialMediaId);

}
