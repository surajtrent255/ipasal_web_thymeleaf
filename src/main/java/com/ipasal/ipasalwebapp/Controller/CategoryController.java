package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.CategoryServices;
import com.ipasal.ipasalwebapp.Services.ProductServices;
import com.ipasal.ipasalwebapp.dto.CategoryDTO;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("parent")
@RequestMapping("/category")
public class CategoryController {
    private CategoryServices categoryServices;
    private ProductServices productServices;


    @Autowired
    public CategoryController(CategoryServices categoryServices, ProductServices productServices) {
        this.categoryServices = categoryServices;
        this.productServices = productServices;
    }


    @SuppressWarnings({"unchecked"})
    @PostMapping
    public @ResponseBody
    int addCategory(@RequestBody CategoryDTO category) {
        Response<Integer> response = (Response<Integer>) categoryServices.addCategory(category);
        return response.getData();
    }
    
//  Getting the products in category between the two price rate
    @SuppressWarnings("unchecked")
	@GetMapping("/parent/{categoryId}/product/rate")
    public String getProductBetweenRate(Model model,
                                        @PathVariable("categoryId") Integer categoryId,
                                        @RequestParam("min") Float rate1,
                                        @RequestParam("max") Float rate2,
                                        HttpServletRequest request) throws ParseException {
        Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryByParentId(categoryId); // getting subcategories
        Response<List<CategoryDTO>> categoryImageResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryImages(categoryId);
        if (categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
            List<CategoryDTO> subCategories = categoryResponse.getData();
            List<CategoryDTO> categoryImage = categoryImageResponse.getData();
            model.addAttribute("subCategories", subCategories);
            CategoryDTO parentCategory = new CategoryDTO();
            parentCategory.setCategoryId(categoryId);
            parentCategory.setCategoryName(categoryImage.get(0).getCategoryName());
            parentCategory.setImage(categoryImage.get(0).getImage());
            model.addAttribute("parentC", parentCategory);
        } else {
            //throw CustomExceptionThrowerUtil.throwException(categoryResponse.getStatus(), categoryResponse.getMessage());
            model.addAttribute("subCategories", new ArrayList<>());
        }

        Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) categoryServices.getProductsBetweenRange(categoryId, rate1, rate2);
        if (productResponse != null && productResponse.getData() != null && productResponse.getData().size() > 0) {
            List<ProductDTO> productListWithRange = productResponse.getData();
            /*
             * Comparing the current date with the entry date
             * If the difference is less than 5, the item is the new product
             * else the item is not new.
             * */
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date presentDate = new Date();
//            String todayDate = simpleDateFormat.format(presentDate);
//            Date currentDate = simpleDateFormat.parse(todayDate);
//            for (ProductDTO products: productListWithRange){
//                if (products.getEntryDate() != null || !products.getEntryDate().isEmpty()) {
//                    Date entryDate = simpleDateFormat.parse(products.getEntryDate());
//                    int diffInDays = (int)( (currentDate.getTime() - entryDate.getTime())
//                            / (1000 * 60 * 60 * 24) );
//                    if (diffInDays <= 5) {
//                        products.setNewProduct(true);
//                    } else {
//                    	products.setNewProduct(false);
//                    }
//                }
//            }
            model.addAttribute("products", productListWithRange);
            return "product";
        }
        throw CustomExceptionThrowerUtil.throwException(productResponse.getStatus(), productResponse.getMessage());
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/parent/{categoryId}/product/sort")
    public String getCategoryDetails(Model model, @PathVariable("categoryId") Integer categoryId,
                                     @RequestParam(value = "rate", required = true, defaultValue = "none") String value,
                                     HttpServletRequest request) throws ParseException {

        Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryByParentId(categoryId); // getting subcategories
        Response<List<CategoryDTO>> categoryImageResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryImages(categoryId);
        
        if (categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
            List<CategoryDTO> subCategories = categoryResponse.getData();
            List<CategoryDTO> categoryImage = categoryImageResponse.getData();
            model.addAttribute("subCategories", subCategories);
            
            CategoryDTO parentCategory = new CategoryDTO();
            parentCategory.setCategoryId(categoryId);
            parentCategory.setCategoryName(categoryImage.get(0).getCategoryName());
            parentCategory.setImage(categoryImage.get(0).getImage());
            model.addAttribute("parentC", parentCategory);
        }

        Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) productServices.getProductsByParentCategoryId(request, categoryId);
        if (productResponse.getData() != null && productResponse.getData().size() > 0) {
            List<ProductDTO> productListUnsorted = productResponse.getData();
            /*
             * Comparing the current date with the entry date
             * If the difference is less than 5, the item is the new product
             * else the item is not new.
             * */
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date presentDate = new Date();
//            String todayDate = simpleDateFormat.format(presentDate);
//            Date currentDate = simpleDateFormat.parse(todayDate);
//            for (ProductDTO products: productListUnsorted){
//                if (products.getEntryDate() != null || !products.getEntryDate().isEmpty()) {
//                    Date entryDate = simpleDateFormat.parse(products.getEntryDate());
//                    int diffInDays = (int)( (currentDate.getTime() - entryDate.getTime())
//                            / (1000 * 60 * 60 * 24) );
//                    if (diffInDays <= 5) {
//                        products.setNewProduct(true);
//                    } else {
//                    	products.setNewProduct(false);
//                    }
//                }
//            }
            if (value.equalsIgnoreCase("asc")) {
                productListUnsorted.sort(Comparator.comparing(ProductDTO::getRate));
                model.addAttribute("products", productListUnsorted);
                for (ProductDTO pro: productListUnsorted){
                }
            }else if (value.equalsIgnoreCase("desc")){
                productListUnsorted.sort(Comparator.comparing(ProductDTO::getRate).reversed());
                model.addAttribute("products", productListUnsorted);
                for (ProductDTO pro: productListUnsorted){
                }
            }else {
                for (ProductDTO pro: productListUnsorted){
                }
                model.addAttribute("products", productListUnsorted);
            }
            return "product";
        }

        throw CustomExceptionThrowerUtil.throwException(productResponse.getStatus(), productResponse.getMessage());
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/{categoryId}/product/search")
    public @ResponseBody
    ResponseEntity<Response<List<ProductDTO>>> searchProductByCategory(@PathVariable("categoryId") Integer categoryId, @RequestParam("searchKey") String searchKey, HttpServletRequest request) {
        Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) categoryServices.searchProductInCategory(categoryId, searchKey, request);
        if (productResponse != null && productResponse.getData() != null && productResponse.getData().size() > 0) {
            return new ResponseEntity<>(productResponse, HttpStatus.valueOf(productResponse.getStatus()));
        }
        return new ResponseEntity<>(productResponse, HttpStatus.valueOf(productResponse.getStatus()));
    }

    @SuppressWarnings("unchecked")
	@GetMapping("/{parentId}/{categoryId}/product/sort")
    public String getAllProductsInChildCategoryById(Model model,
                                                    @PathVariable("parentId") Integer parentId,
                                                    @PathVariable("categoryId") Integer categoryId,
                                                    @RequestParam(value = "rate", required = true, defaultValue = "none") String value,
                                                    HttpServletRequest request) throws ParseException {
        Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryByParentId(parentId); // getting subcategories
        Response<List<CategoryDTO>> categoryImageResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryImages(categoryId);
        Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) productServices.getProductsByChildCategoryId(request, categoryId);
        CategoryDTO parentCategory = new CategoryDTO();
        parentCategory.setCategoryId(categoryId);
        List<CategoryDTO> categoryImage = categoryImageResponse.getData();
        
        if(categoryImage.size() > 0) {
        	parentCategory.setCategoryName(categoryImage.get(0).getCategoryName());
            parentCategory.setImage(categoryImage.get(0).getImage());
            parentCategory.setParentId(categoryImage.get(0).getParentId());
            parentCategory.setCategoryId(categoryImage.get(0).getCategoryId());
        } else {
        	parentCategory.setCategoryName("");
        }
        
        if (categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
            List<CategoryDTO> subCategories = categoryResponse.getData();
        
            model.addAttribute("subCategories", subCategories);
        } 
        
        model.addAttribute("parentC", parentCategory);
        
        if (productResponse.getData() != null && productResponse.getData().size() > 0) {
            List<ProductDTO> productList = productResponse.getData();
            /*
            * Comparing the current date with the entry date
            * If the difference is less than 5, the item is the new product
            * else the item is not new.
            * */
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date presentDate = new Date();
//            String todayDate = simpleDateFormat.format(presentDate);
//            Date currentDate = simpleDateFormat.parse(todayDate);
//            for (ProductDTO products: productList){
//                if (products.getEntryDate() != null || !products.getEntryDate().isEmpty()) {
//                    Date entryDate = simpleDateFormat.parse(products.getEntryDate());
//                    int diffInDays = (int)( (currentDate.getTime() - entryDate.getTime())
//                            / (1000 * 60 * 60 * 24) );
//                    if (diffInDays <= 5) {
//                        products.setNewProduct(true);
//                    } else {
//                    	products.setNewProduct(false);
//                    }
//                }
//            }
            model.addAttribute("parent", parentCategory);
            if (value.equalsIgnoreCase("asc")){
                productList.sort(Comparator.comparing(ProductDTO::getRate));
                model.addAttribute("products", productList);
            }else if (value.equalsIgnoreCase("desc")){
                productList.sort(Comparator.comparing(ProductDTO::getRate).reversed());
                model.addAttribute("products", productList);
            }else{
                model.addAttribute("products", productList);
            }


            return "product";
        }

        throw CustomExceptionThrowerUtil.throwException(productResponse.getStatus(), productResponse.getMessage());
    }


    @SuppressWarnings("unchecked")
	@GetMapping("/{parentId}/{categoryId}/product/rate")
    public String getChildCategoryProductBetweenRate(Model model,
                                        @PathVariable("parentId") Integer parentId,
                                        @PathVariable("categoryId") Integer categoryId,
                                        @RequestParam("min") Float rate1,
                                        @RequestParam("max") Float rate2,
                                        HttpServletRequest request) throws ParseException {
        Response<List<CategoryDTO>> categoryResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryByParentId(parentId); // getting subcategories
        Response<List<CategoryDTO>> categoryImageResponse = (Response<List<CategoryDTO>>) categoryServices.getCategoryImages(categoryId);
        CategoryDTO parentCategory = new CategoryDTO();
//        parentCategory.setCategoryId(categoryResponse.getData().get(0).getCategoryId());
        parentCategory.setCategoryId(categoryId);
        
        if (categoryResponse.getData() != null && categoryResponse.getData().size() > 0) {
            List<CategoryDTO> subCategories = categoryResponse.getData();
            List<CategoryDTO> categoryImage = categoryImageResponse.getData();
            model.addAttribute("subCategories", subCategories);
            

            if(categoryImage != null && categoryImage.size() > 0) {
            	parentCategory.setCategoryName(categoryImage.get(0).getCategoryName());
                parentCategory.setImage(categoryImage.get(0).getImage());
                parentCategory.setParentId(categoryImage.get(0).getParentId());
            } else {
            	parentCategory.setCategoryName("");
            }
            
            model.addAttribute("parentC", parentCategory);
        } else {
            //throw CustomExceptionThrowerUtil.throwException(categoryResponse.getStatus(), categoryResponse.getMessage());
            model.addAttribute("subCategories", new ArrayList<>());
        }

        Response<List<ProductDTO>> productResponse = (Response<List<ProductDTO>>) categoryServices.getProductsBetweenRange(categoryId, rate1, rate2);
        if (productResponse != null && productResponse.getData() != null && productResponse.getData().size() > 0) {
//            CategoryDTO parentCategory = new CategoryDTO();
//            parentCategory.setCategoryId(categoryId);
            model.addAttribute("parent", parentCategory);
            List<ProductDTO> productListWithRange = productResponse.getData();
            /*
             * Comparing the current date with the entry date
             * If the difference is less than 5, the item is the new product
             * else the item is not new.
             * */
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date presentDate = new Date();
//            String todayDate = simpleDateFormat.format(presentDate);
//            Date currentDate = simpleDateFormat.parse(todayDate);
//            for (ProductDTO products: productListWithRange){
//                if (products.getEntryDate() != null || !products.getEntryDate().isEmpty()) {
//                    Date entryDate = simpleDateFormat.parse(products.getEntryDate());
//                    int diffInDays = (int)( (currentDate.getTime() - entryDate.getTime())
//                            / (1000 * 60 * 60 * 24) );
//                    if (diffInDays <= 5) {
//                        products.setNewProduct(true);
//                    } else {
//                    	products.setNewProduct(false);
//                    }
//                }
//            }
            model.addAttribute("products", productListWithRange);
            return "product";
        }
        throw CustomExceptionThrowerUtil.throwException(productResponse.getStatus(), productResponse.getMessage());
    }


    //returns list of subCategories for product entry form.
    @SuppressWarnings("unchecked")
    @GetMapping("/productEntry/parent/{parentId}")
    public @ResponseBody
    ResponseEntity<Response<List<CategoryDTO>>> getCategoryByParentIdForProductEntry(Model model, @PathVariable("parentId") Integer parentId) {
        Response<List<CategoryDTO>> categories = (Response<List<CategoryDTO>>) categoryServices.getCategoryByParentId(parentId);

        if (categories.getData() != null && categories.getData().size() > 0) {
            return new ResponseEntity<>(categories, HttpStatus.valueOf(categories.getStatus()));
        }

        return new ResponseEntity<>(categories, HttpStatus.valueOf(categories.getStatus()));
    }

    //    Uploading category image
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadCategoryImage", method = RequestMethod.POST)
    public @ResponseBody
    String uploadCategoryImage(MultipartHttpServletRequest request) {
        List<MultipartFile> images = new ArrayList<>();
        MultipartFile bannerImage = request.getFile("imageb");
        MultipartFile featuredImage = request.getFile("imagef");
        images.add(0, featuredImage);
        images.add(1, bannerImage);
        MultipartFile[] files = new MultipartFile[images.size()];
        files = images.toArray(files);

        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String resultString = ((Response<String>) categoryServices.uploadCategoryImage(files, categoryId)).getData();
        return resultString;
    }

    /**
     * 
     * @param categoryId
     * @return CategoryDTO as response.
     */
    @RequestMapping(value="/{categoryId}", method = RequestMethod.GET)
    public @ResponseBody Response<?> getCategoryByCategoryId(@PathVariable("categoryId") Integer categoryId) {
    	Response<?> response = categoryServices.getCategoryByCategoryId(categoryId);
    	return response;
    }
    
    @RequestMapping(value="/delete", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<String> deleteParentandChildCategory(@RequestBody List<Integer> categoryIds) {
    	Response<?> response = categoryServices.deleteParentandChildCategory(categoryIds);
    	return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
    }
    
}
