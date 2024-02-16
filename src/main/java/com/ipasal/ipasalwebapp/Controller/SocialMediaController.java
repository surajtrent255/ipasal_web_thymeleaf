package com.ipasal.ipasalwebapp.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipasal.ipasalwebapp.Services.SocialMediaService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;

/**
 * 
 * @author Tanchhowpa
 * Sep 5, 2019, 1:59:28 PM
 *
 */

@Controller
@RequestMapping("/social")
public class SocialMediaController {
	
	private SocialMediaService socialMediaService;

	private Logger logger = LoggerFactory.getLogger(SocialMediaController.class);
	
	public SocialMediaController(SocialMediaService socialMediaService) {
		this.socialMediaService = socialMediaService;
	}

	
	@PostMapping("/add")
	public @ResponseBody ResponseEntity<String> addSocialMedia(@RequestBody SocialMediaDTO socialMedia) {
		Response<Integer> response = (Response<Integer>) socialMediaService.addSocialMedia(socialMedia);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PutMapping("/update/{socialMediaId}")
	public @ResponseBody ResponseEntity<String> updateSocialMedia(@RequestBody SocialMediaDTO socialMedia, @PathVariable("socialMediaId") Integer socialMediaId) {
		Response<?> response =  socialMediaService.updateSocialMedia(socialMediaId, socialMedia);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@GetMapping("/{socialMediaId}")
	public @ResponseBody Response<List<SocialMediaDTO>> getSocialMediaById(Model model, HttpServletRequest request, @PathVariable("socialMediaId") Integer socialMediaId ) {
		Response<List<SocialMediaDTO>> response = (Response<List<SocialMediaDTO>>) socialMediaService.getSocialMediaById(socialMediaId);
		return response;
	}
	
	@PostMapping("/setActive/{socialMediaId}")
	public @ResponseBody ResponseEntity<String> setSocialMediaActive(@PathVariable("socialMediaId") Integer socialMediaId) {
		Response<?> response = socialMediaService.setSocialMediaActive(socialMediaId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping("/setInactive/{socialMediaId}")
	public @ResponseBody ResponseEntity<String> setSocialMediaInactive(@PathVariable("socialMediaId") Integer socialMediaId) {
		Response<?> response = socialMediaService.setSocialMediaInactive(socialMediaId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
}
