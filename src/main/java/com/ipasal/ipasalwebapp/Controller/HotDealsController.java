/**
 * 
 */
package com.ipasal.ipasalwebapp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ipasal.ipasalwebapp.Services.HotDealsService;
import com.ipasal.ipasalwebapp.dto.HotDealsDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;

/**
 * @author Pujan K.C. <pujanov69@gmail.com>
 *
 * Created on Sep 17, 2019
 */
@Controller
@RequestMapping("/hot-deals")
public class HotDealsController {

	private HotDealsService hotDealsService;
	
	public HotDealsController(HotDealsService hotDealsService) {
		this.hotDealsService = hotDealsService;
	}
	

	@PostMapping
	public @ResponseBody ResponseEntity<String> addHotDeal(@RequestBody HotDealsDTO hotDealsDTO) {
		Response<Integer> response = (Response<Integer>) hotDealsService.addHotDealData(hotDealsDTO);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PutMapping
	public @ResponseBody ResponseEntity<String> editHotDeal(@RequestBody HotDealsDTO hotDealsDTO) {
		Response<Integer> response = (Response<Integer>) hotDealsService.editHotDealData(hotDealsDTO);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@DeleteMapping("/{hotDealId}")
	public @ResponseBody ResponseEntity<String> deleteHotDeal(@PathVariable("hotDealId") Integer hotDealId) {
		Response<?> response = hotDealsService.deleteHotDealData(hotDealId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	//upload hot deal image	
		@RequestMapping(value = "/uploadImage/{hotDealId}", method = RequestMethod.POST)
	    public @ResponseBody
	    String uploadImage(MultipartHttpServletRequest request, @PathVariable("hotDealId") Integer hotDealId) {
			String imgNm = "imageh" + hotDealId;
			MultipartFile hotDealImage = request.getFile(imgNm);
			 List<MultipartFile> images = new ArrayList<>();
			 images.add(0, hotDealImage);
			 MultipartFile[] files = new MultipartFile[images.size()];
		     files = images.toArray(files);
//	        String resultString = ((Response<String>) aboutService.uploadBannerImage(files)).getData();
		     String resultString = ((Response<String>) hotDealsService.uploadHotDealImage(files, hotDealId)).getData();
	        return resultString;
	    }
}
