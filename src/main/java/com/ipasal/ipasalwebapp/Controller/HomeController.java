package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.*;
import com.ipasal.ipasalwebapp.dto.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @authors 
 * Yoomes <yoomesbhujel@gmail.com>
 * Azens Eklak
 * 
 */

@Controller
@RequestMapping(value = {"/", "/index"})
public class HomeController {
    private CategoryServices categoryServices;
    private ProductServices productServices;
    private SliderService sliderService;
    private HotDealsService hotDealsService;
    private OfferProductService offerProductService;
    private OrderService orderService;
    private PromotionalSalesService promotionalSalesService;

    Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    public HomeController(CategoryServices categoryServices, ProductServices productServices,
                          SliderService sliderService, HotDealsService hotDealsService,
                          OfferProductService offerProductService, OrderService orderService, PromotionalSalesService promotionalSalesService){
        this.categoryServices = categoryServices;
        this.productServices = productServices;
        this.sliderService = sliderService;
        this.hotDealsService = hotDealsService;
        this.offerProductService = offerProductService;
        this.orderService = orderService;
        this.promotionalSalesService = promotionalSalesService;
    }

    @SuppressWarnings("unchecked")
	@GetMapping
    public String getIndexpage(Model model) throws ParseException {
    	
    	//Responses from rest api....
        Response<List<ProductDTO>> featuredProducts = (Response<List<ProductDTO>>) productServices.getFeaturedProducts();
        Response<List<CategoryDTO>> featuredCategories = (Response<List<CategoryDTO>>) categoryServices.getFeaturedCategories();
        Response<List<SliderDTO>> activeSliders = (Response<List< SliderDTO>>) sliderService.getAllActiveSliders();
        Response<List<HotDealsDTO>> hotDeals = (Response<List<HotDealsDTO>>) hotDealsService.getActiveHotDeal();
        Response<List<CategoryDTO>> categoryOffers = (Response<List<CategoryDTO>>) categoryServices.getOfferedCategories();
        Response<List<ProductDTO>> mostBoughtProducts = (Response<List<ProductDTO>>) orderService.getMostBoughtProducts();
        Response<List<ProductDTO>> saleProducts = (Response<List<ProductDTO>>) productServices.getSaleProductsforIndex();
        Response<List<PromotionalSalesDTO>> promotionalSales = (Response<List<PromotionalSalesDTO>>) promotionalSalesService.getAllActivePrmotions();
        
        if(promotionalSales.getData() != null && promotionalSales.getData().size() > 0) {
        	List<PromotionalSalesDTO> promotionalSalesList = (List<PromotionalSalesDTO>) promotionalSales.getData();
            model.addAttribute("promo", promotionalSalesList);
        } else {
        	model.addAttribute("promoError", promotionalSales.getMessage());
        }
        
        if(saleProducts.getData() != null && saleProducts.getData().size() > 0) {
        	List<ProductDTO> productList = (List<ProductDTO>) saleProducts.getData();
            model.addAttribute("saleProducts", productList);
        } else {
        	model.addAttribute("saleProductsError", saleProducts.getMessage());
        }
        
        if(featuredProducts.getData() != null && featuredProducts.getData().size() > 0) {
        	List<ProductDTO> productList = (List<ProductDTO>) featuredProducts.getData();
            model.addAttribute("featuredProducts", productList);
        } else {
        	model.addAttribute("featuredProductsError", featuredProducts.getMessage());
        }
            
            if (activeSliders != null && activeSliders.getData() != null && activeSliders.getData().size() > 0){
                List<SliderDTO> sliderData = activeSliders.getData();
                model.addAttribute("sliderData", sliderData);
            } else {
            	model.addAttribute("sliderDataError", activeSliders.getMessage());
            	//throw CustomExceptionThrowerUtil.throwException(activeSliders.getStatus(), activeSliders.getMessage());
            }

            
            if (categoryOffers.getData() != null && categoryOffers.getData().size() > 0) {
                List<CategoryDTO> offerCategoryDetail = categoryOffers.getData();
                model.addAttribute("offerCategory", offerCategoryDetail);
            } else {
            	model.addAttribute("offerCategoryError", categoryOffers.getMessage());
            	logger.info("----> No categories with offer" );
            }
            
            if (mostBoughtProducts != null && mostBoughtProducts.getData().size() > 0){
//                List<Integer> productIdList = mostBoughtProductIds.getData();
                List<ProductDTO> mostBoughtProductList = mostBoughtProducts.getData();
//                for (Integer id : productIdList){
//                    Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) productServices.getProductById(id);
//                    if (productResponse != null && productResponse.getData().size() > 0){
//                        ProductDTO product = productResponse.getData().get(0);
//                        mostBoughtProductList.add(product);
//                    }
//                }
                /*
                 * Comparing the current date with the entry date
                 * If the difference is less than 5, the item is the new product
                 * else the item is not new.
                 * */
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date presentDate = new Date();
//                String todayDate = simpleDateFormat.format(presentDate);
//                Date currentDate = simpleDateFormat.parse(todayDate);
//                for (ProductDTO products: mostBoughtProductList){
//                    if (products.getEntryDate() != null || !products.getEntryDate().isEmpty()) {
//                        Date entryDate = simpleDateFormat.parse(products.getEntryDate());
//                        int diffInDays = (int)( (currentDate.getTime() - entryDate.getTime())
//                                / (1000 * 60 * 60 * 24) );
//                        if (diffInDays <= 5) {
//                            products.setNewProduct(true);
//                        }
//                    }
//                }
                
                if(mostBoughtProductList.size() > 0 && mostBoughtProductList != null) {
                	model.addAttribute("mostBoughtProduct", mostBoughtProductList);
                } else {
                	model.addAttribute("mostBoughtProductError", "Just started selling products. We'll update this section later.");
                }
                
            } else {
            	model.addAttribute("mostBoughtProductIdsError", mostBoughtProducts.getMessage());
            }
            
            if (hotDeals.getData() != null && hotDeals.getData().size() > 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                List<HotDealsDTO> hotDealsData = hotDeals.getData();
                String endDate = hotDealsData.get(0).getFinishDate();
                model.addAttribute("endDate", endDate);
                model.addAttribute("hotDealsData", hotDealsData);
            } else {
            	model.addAttribute("hotDealsDataError", hotDeals.getMessage());
            }
            
            if(featuredCategories.getData() != null && featuredCategories.getData().size() > 0) {
            	List<CategoryDTO> categoryList = (List<CategoryDTO>) featuredCategories.getData();// parentId = 0 gets all the categories
            	model.addAttribute("featuredCategories",categoryList);
            	if(categoryList.size() % 6 == 0) {
            		model.addAttribute("rows", categoryList.size()/6);
            	} else {
            		model.addAttribute("rows", 0);
            	}
            	return "index";
            } else {
            	model.addAttribute("featuredCategoriesError", featuredCategories.getMessage());
            	model.addAttribute("rows", 0);
            	return "index";
            }
    }

}
