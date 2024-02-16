package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.AboutService;
import com.ipasal.ipasalwebapp.Services.ProductServices;
import com.ipasal.ipasalwebapp.Services.ShippingRateService;
import com.ipasal.ipasalwebapp.dto.*;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;
import com.ipasal.ipasalwebapp.utilities.UserDetailsUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("cart")
@RequestMapping("/products")
public class ProductController {
    private ProductServices productServices;
    private AboutService aboutService;
    private ShippingRateService shippingRateService;


    @Autowired
    public ProductController(ProductServices productServices,
                             AboutService aboutService, ShippingRateService shippingRateService){
        this.productServices = productServices;
        this.aboutService = aboutService;
        this.shippingRateService = shippingRateService;
    }

    @SuppressWarnings("unchecked")
	@GetMapping("/search")
    public @ResponseBody Response<List<ProductDTO>> searchProduct(Model model,
    		HttpServletRequest request) {
    	Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productServices.searchProductBySearchKey(request);
    	return response;
    }
    
    @GetMapping("/searchResults")
    public String getSearchResult(Model model, HttpServletRequest request) {
    	Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productServices.searchProductBySearchKey(request);
    	if(response.getData() != null && response.getData().size() > 0) {
    		model.addAttribute("results", response.getData());
    	} else {
    		model.addAttribute("resultError", String.format("Nothing found for searchkey %s", request.getParameter("searchKey")));
    	}
    	
    	return "search-result";
    }
    
    @PutMapping("/{productId}")
    public @ResponseBody ResponseEntity<String> updateProduct(@PathVariable("productId") Integer productId,
    		@RequestBody ProductDTO product) {
    	Response<?> response = productServices.updateProductInfoById(productId, product);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/{productId}")
    public @ResponseBody ResponseEntity<String> deleteProduct(@PathVariable("productId") Integer productId) {
    	Response<?> response = productServices.deleteProductByProductId(productId);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }
    
    
    @SuppressWarnings("unchecked")
	@GetMapping("/{productId}")
    public String getProdctById(Model model, @PathVariable("productId") Integer productId ){
        Response<List<ProductDTO>> response = (Response<List<ProductDTO>>)productServices.getProductById(productId);
        if(response.getData() != null && response.getData().size() > 0) {
        	ProductDTO productDetails = (ProductDTO) response.getData().get(0);
        	List<CategoryDTO> ancestors = productDetails.getAncestorCategories();
            model.addAttribute("product", productDetails);
            model.addAttribute("ancestors", ancestors); //Adding the ancestor category for bread crumb
            return "product-detail";
        }
        
        throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    }



  @GetMapping("/parentCategory/{categoryId}")
  public @ResponseBody Response<List<ProductDTO>> getProductsByParentsCategoryId(@PathVariable("categoryId") Integer categoryId, HttpServletRequest request) {
	  Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productServices.getProductsByParentCategoryId(request, categoryId);
	  if(response.getData() != null && response.getData().size() > 0) {
		  return response;
	  }
	  
	  return Response.ok(new ArrayList<>(), HttpStatus.NOT_FOUND.value(), "No data found!");
  }
 
  
  
  @SuppressWarnings("unchecked")
  @GetMapping("/category/{id}")
  public @ResponseBody Response<List<ProductDTO>> getProductsByCategoryId(@PathVariable("id") Integer categoryId,
		  @PageableDefault(page = 0, size = 9) Pageable pageable, 
		  HttpServletRequest request){
      Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productServices.getProductsByCategoryId(categoryId, pageable, request);
      if(response.getData() != null && response.getData().size() > 0) {
    	  return response;
      } 
      return Response.ok(new ArrayList<>(), HttpStatus.NOT_FOUND.value(), "No data found");
  }

    //go to the cart
    @GetMapping("/cart")
    public String gotToCart(Model model, HttpServletRequest request){
        Response<List<AboutDTO>> aboutInfoResponse = (Response<List<AboutDTO>>) aboutService.getAboutInfo();
        Response<ShippingRateDTO> shippingRateInfoResponse = (Response<ShippingRateDTO>) shippingRateService.getShippingRateInfoByLocation("Kathmandu");
        HttpSession session = request.getSession();
        List<ProductDTO> products = new ArrayList<>();
        if (session.getAttribute("cart") != null) {
			products = (List<ProductDTO>) session.getAttribute("cart");
		} 
        
        if(products == null || products.size() <= 0 ) {
        	session.removeAttribute("cart");
        }
        
        if (aboutInfoResponse != null && aboutInfoResponse.getData() != null && aboutInfoResponse.getData().size() > 0) {
            AboutDTO aboutData = aboutInfoResponse.getData().get(0);
            model.addAttribute("aboutInfo", aboutData);
        }
        if (shippingRateInfoResponse != null){
            ShippingRateDTO shippingRateDTO = (ShippingRateDTO) shippingRateInfoResponse.getData();
            model.addAttribute("shippingRateInfo", shippingRateDTO);
        }
        return "cart";
    }

    @SuppressWarnings("unchecked")
	@Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public @ResponseBody int addProduct(@RequestBody ProductDTO product) {
        int userId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
        product.setUserId(userId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sCurrentDate = dateFormat.format(Calendar.getInstance().getTime());
        product.setEntryDate(sCurrentDate);
        Response<Integer> result = (Response<Integer>) productServices.addProduct(product);
        return result.getData();
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public @ResponseBody String uploadProductImage(MultipartHttpServletRequest request) {
            List<MultipartFile> images = request.getFiles("images");
            MultipartFile[] files = new MultipartFile[images.size()];
            files = images.toArray(files);

            int productId = Integer.parseInt(request.getParameter("productId"));
            System.out.println("Product Id for images is: " + productId );
            String resultString = ((Response<String>)productServices.uploadProductImages(files, productId)).getData();
            return resultString;
    }
    
    //Gets the productDTO response from Product ID
    @RequestMapping(value ="/byId/{productId}", method = RequestMethod.GET)
    public @ResponseBody Response<?> getProduct(@PathVariable("productId") Integer productId) {
    	Response<?> response = productServices.getProductById(productId);
    	return response;
    } 
    
    //Gets the the sales products
    @GetMapping("/allSales")
    public String getAllSaleProducts(Model model, HttpServletRequest request) {
    	Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) productServices.getAllSaleProducts(request);
    	if(response.getData() != null && response.getData().size() > 0) {
    		model.addAttribute("allSales", response.getData());
    		
    	} else {
//    		throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    		model.addAttribute("allSalesError", response.getMessage());
    	}
    	return "sales";
    }
}
