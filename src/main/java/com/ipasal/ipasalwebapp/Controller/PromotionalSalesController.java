package com.ipasal.ipasalwebapp.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.ipasal.ipasalwebapp.Services.PromotionalSalesService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.PromotionalSalesDTO;
import com.ipasal.ipasalwebapp.dto.Response;

/**
 * 
 * @author Tanchhowpa
 * Sep 20, 2019, 12:28:56 PM
 *
 */

@Controller
@RequestMapping("/promotionalSales")
public class PromotionalSalesController {
	private PromotionalSalesService promotionalSalesService;

	
	public PromotionalSalesController(PromotionalSalesService promotionalSalesService) {
		this.promotionalSalesService = promotionalSalesService;
	}
	
    @PutMapping("/{promotionalSalesId}")
    public @ResponseBody ResponseEntity<String> updatePromotionById(@PathVariable("promotionalSalesId") Integer promotionalSalesId,
    		@RequestBody PromotionalSalesDTO promotion) {
    	Response<?> response = promotionalSalesService.updatePromotionById(promotionalSalesId, promotion);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }
    
    
    //Gets the the sales products
    @GetMapping("/pr/{promotionalSalesId}")
    public String getPromotionalSaleProductsById(@PathVariable("promotionalSalesId") Integer promotionalSalesId ,Model model, HttpServletRequest request) {
    	Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) promotionalSalesService.getPromotionalSaleProductsById(promotionalSalesId, request);
    	if(response.getData() != null && response.getData().size() > 0) {
    		model.addAttribute("allSales", response.getData());
    		
    	} else {
//    		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    		model.addAttribute("allSalesError", response.getMessage());
    	}
    	return "promotional-sales";
    }
    
	@PostMapping("/setActive/{promotionalSalesId}")
	public @ResponseBody ResponseEntity<String> setPromotionActive(@PathVariable("promotionalSalesId") Integer promotionalSalesId) {
		Response<?> response = promotionalSalesService.setPromotionActive(promotionalSalesId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping("/setInactive/{promotionalSalesId}")
	public @ResponseBody ResponseEntity<String> setPromotionInactive(@PathVariable("promotionalSalesId") Integer promotionalSalesId) {
		Response<?> response = promotionalSalesService.setPromotionInactive(promotionalSalesId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
	
    @PutMapping("/list/{promotionalSalesId}")
    public @ResponseBody ResponseEntity<String> updatePromotionProductsById(@PathVariable("promotionalSalesId") Integer promotionalSalesId,
    		@RequestBody PromotionalSalesDTO promotion) {
    	Response<?> response = promotionalSalesService.updatePromotionProductsById(promotionalSalesId, promotion);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }
	
}
