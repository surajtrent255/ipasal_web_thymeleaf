package com.ipasal.ipasalwebapp.Controller;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ipasal.ipasalwebapp.Services.ProductServices;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

@Controller
@RequestMapping("/featured")
public class FeaturedController {
	private final ProductServices productServices;
	public FeaturedController(ProductServices productServices) {
		this.productServices = productServices;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
    	public String getFeaturepage(Model model){
		
		Response<List<ProductDTO>> productsResponse = (Response<List<ProductDTO>>) productServices.getFeaturedProducts();
		 if(productsResponse.getData() != null && productsResponse.getData().size() > 0) {
	            List<ProductDTO> productList = (List<ProductDTO>) productsResponse.getData();
	            model.addAttribute("featuredProducts", productList);
	            return "featured";
		 	} else {
		 		throw CustomExceptionThrowerUtil.throwException(productsResponse.getStatus(), productsResponse.getMessage());
		 	}
		}
	}
