package com.ipasal.ipasalwebapp.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ipasal.ipasalwebapp.Services.CategoryServices;
import com.ipasal.ipasalwebapp.Services.SocialMediaService;
import com.ipasal.ipasalwebapp.Services.ToolBarMessageService;
import com.ipasal.ipasalwebapp.dto.CategoryDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;
import com.ipasal.ipasalwebapp.dto.ToolBarMessageDTO;

/**
 * 
 * @author Yoomes <yoomesbhujel@gmail.com>
 * The global controller class or exception handler class.
 * Annotated with @ControllerAdvice it handles exceptions thrown from every @Controller classes.
 * Methods annotated with @ModelAttribute binds model for every controllers.
 */
@ControllerAdvice
public class GlobalErrorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);
	
	@Autowired
	CategoryServices categoryServices;
	
	@Autowired
	SocialMediaService socialMediaService;
	
	@Autowired
	ToolBarMessageService toolBarMessageService;
	
	/**
	 * @author Yoomes <yoomesbhujel@gmail.com>
	 * @param model
	 * @return Data
	 * Returns List of categories in hierarchy for every controller request
	 */
	@ModelAttribute
	public void getParentCategory(HttpServletRequest request, Model model) {
		Response<List<CategoryDTO>> parentCategories = (Response<List<CategoryDTO>>) categoryServices.getAllCategories();
		Response<List<CategoryDTO>> featuredCategories = (Response<List<CategoryDTO>>) categoryServices.getFeaturedCategories();
		Response<List<SocialMediaDTO>> socialMedias = (Response<List<SocialMediaDTO>>) socialMediaService.getAllActiveSocialMedias();
		Response<ToolBarMessageDTO> toolBarMessages = (Response<ToolBarMessageDTO>) toolBarMessageService.getToolBarMessage();
		
		if(parentCategories.getData() != null && parentCategories.getData().size() > 0) {
			List<CategoryDTO> parentCategoryList = parentCategories.getData();
			model.addAttribute("categories", parentCategoryList);
		} else {
			model.addAttribute("categoriesError", parentCategories.getMessage());
		}
		
		if(featuredCategories.getData() != null && featuredCategories.getData().size() > 0) {
			List<CategoryDTO> categoryList = (List<CategoryDTO>) featuredCategories.getData();
			model.addAttribute("featuredCategories", categoryList);
		} else {
			model.addAttribute("featuredCategoriesError", featuredCategories.getMessage());
		}
		
		if(socialMedias.getData() != null && socialMedias.getData().size() > 0) {
			List<SocialMediaDTO> socialMediaList = socialMedias.getData();
			model.addAttribute("socialTools", socialMediaList);
		} else {
			model.addAttribute("socialToolsError", socialMedias.getMessage()); 
		}
		
		if(toolBarMessages.getData() != null) {
			ToolBarMessageDTO toolbarMessage = toolBarMessages.getData();
			String abc = toolbarMessage.getMessage();
			model.addAttribute("toolBarMessage", toolbarMessage);
		} else {
			model.addAttribute("toolBarMessageError", toolBarMessages.getMessage()); 
		}
		//throw CustomExceptionThrowerUtil.throwException(parentCategories.getStatus(), parentCategories.getMessage());
	}

	/**
	 * 
	 * @param ex c
	 * @param model
	 * @return view
	 * Handles any type of custom exceptions because we extend from our custom base exception class(CustomBaseException.clss). 
	 * 
	 */
	@ExceptionHandler
	public String resolveException(CustomBaseException ex, Model model) {
		Response<List<CategoryDTO>> parentCategories = (Response<List<CategoryDTO>>) categoryServices.getAllCategories();
		Response<List<CategoryDTO>> featuredCategoriesResponse = (Response<List<CategoryDTO>>) categoryServices.getFeaturedCategories();
		Response<List<SocialMediaDTO>> socialMedias = (Response<List<SocialMediaDTO>>) socialMediaService.getAllActiveSocialMedias();
		Response<ToolBarMessageDTO> toolBarMessages = (Response<ToolBarMessageDTO>) toolBarMessageService.getToolBarMessage();

		
		if(parentCategories.getData() != null && parentCategories.getData().size() > 0) {
			model.addAttribute("categories", parentCategories.getData());
		} else {
			model.addAttribute("categoriesError", parentCategories.getMessage());
		}
		
		if(featuredCategoriesResponse.getData() != null && featuredCategoriesResponse.getData().size() > 0) {
			model.addAttribute("featuredCategories", featuredCategoriesResponse.getData());
		} else {
			model.addAttribute("featuredCategoriesError", featuredCategoriesResponse.getMessage());
		}
		
		if(socialMedias.getData() != null && socialMedias.getData().size() > 0 ) {
			model.addAttribute("socialTools", socialMedias.getData());
		} else {
			model.addAttribute("socailToolsError", socialMedias.getMessage());
		}
		
		if(toolBarMessages.getData() != null ) {
			ToolBarMessageDTO toolbarMessage = toolBarMessages.getData();
			model.addAttribute("toolBarMessage", toolbarMessage);
		} else {
			model.addAttribute("toolBarMessageError", toolBarMessages.getMessage()); 
		}
		model.addAttribute("errorMsg", ex.getMessage());
		model.addAttribute("status", ex.getStatus().value());
		return "error";
	}
}
