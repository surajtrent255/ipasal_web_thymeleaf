package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.AboutService;
import com.ipasal.ipasalwebapp.dto.AboutDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;

@Controller
@RequestMapping("/about")
public class AboutController {

	private AboutService aboutService;

	@Autowired
	public AboutController(AboutService aboutService) {
		this.aboutService = aboutService;
	}
	
	 @PostMapping
	    public @ResponseBody
	    int addAboutUs(@RequestBody AboutDTO aboutUs) {
	        System.out.println(aboutUs.toString());
	        Response<Integer> response = (Response<Integer>) aboutService.updateAboutInfo(aboutUs);
	        return response.getData();
	    }

	@GetMapping
	public String getAboutUsPage(Model model) {
		Response<List<AboutDTO>> aboutInfoResponse = (Response<List<AboutDTO>>) aboutService.getAboutInfo();
		if (aboutInfoResponse.getData() != null && aboutInfoResponse.getData().size() > 0) {
			AboutDTO aboutData = aboutInfoResponse.getData().get(0);
			model.addAttribute("aboutInfo", aboutData);
			return "about";
		}
		
		throw CustomExceptionThrowerUtil.throwException(aboutInfoResponse.getStatus(), aboutInfoResponse.getMessage());
	}
//upload banner image	
	@RequestMapping(value = "/uploadBannerImage", method = RequestMethod.POST)
    public @ResponseBody
    String uploadBannerImage(MultipartHttpServletRequest request) {
		MultipartFile bannerImage = request.getFile("imageb");
		 List<MultipartFile> images = new ArrayList<>();
		 images.add(0, bannerImage);
		 MultipartFile[] files = new MultipartFile[images.size()];
	     files = images.toArray(files);
        String resultString = ((Response<String>) aboutService.uploadBannerImage(files)).getData();
        return resultString;
    }

	
	//upload story image	
		@RequestMapping(value = "/uploadStoryImage", method = RequestMethod.POST)
	    public @ResponseBody
	    String uploadStoryImage(MultipartHttpServletRequest request) {
			MultipartFile storyImage = request.getFile("images");
			 List<MultipartFile> images = new ArrayList<>();
			 images.add(0, storyImage);
			 MultipartFile[] files = new MultipartFile[images.size()];
		     files = images.toArray(files);
	        String resultString = ((Response<String>) aboutService.uploadStoryImage(files)).getData();
	        return resultString;
	    }
	
}
