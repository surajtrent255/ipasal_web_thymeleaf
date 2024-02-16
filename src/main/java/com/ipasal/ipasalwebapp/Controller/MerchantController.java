package com.ipasal.ipasalwebapp.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipasal.ipasalwebapp.Services.MerchantServices;


import com.ipasal.ipasalwebapp.dto.*;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

@Controller
@RequestMapping("/merchant")
public class MerchantController {
	private MerchantServices merchantServices;
	
	
	public MerchantController(MerchantServices merchantServices) {
		this.merchantServices = merchantServices;
	}
	
	
	@SuppressWarnings("unchecked")
	@Secured("ROLE_ADMIN")
	@PostMapping("/add")
	public @ResponseBody int addMerchant(@RequestBody MerchantDTO merchant) {
		Response<Integer> result = (Response<Integer>) merchantServices.addMerchant(merchant);
		return result.getData();
	
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/{merchantId}")
	public String getMerchantById(Model model, @PathVariable("merchantId") Integer merchantId ) {
		MerchantDTO merchant = null;
		Response<List<MerchantDTO>> response = (Response<List<MerchantDTO>>) merchantServices.getMerchantById(merchantId);
		if(response.getData() != null && response.getData().size() > 0) {
			merchant = response.getData().get(0);
			model.addAttribute("merchant", merchant);
		return "merchant-detail";
	}
		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
	}
	
	
	@PutMapping("/{merchantId}")
	public @ResponseBody ResponseEntity<String> updateMerchant(@PathVariable ("merchantId") Integer merchantId, @RequestBody MerchantDTO merchant) {
		Response<?> response = merchantServices.updateMercantById(merchantId, merchant);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@DeleteMapping("/{merchantId}")
	public @ResponseBody ResponseEntity<String> deleteMerchant(@PathVariable ("merchantId") Integer merchantId) {
		Response<?> response = merchantServices.deleteMerchantById(merchantId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/search")
    public @ResponseBody Response<List<MerchantDTO>> searchMerchant(@RequestParam("searchKey") String searchKey, HttpServletRequest request) {
    	Response<List<MerchantDTO>> response = (Response<List<MerchantDTO>>) merchantServices.searchMerchantBySearchKey(searchKey, request);
    	return response;
    }
	
	
}
