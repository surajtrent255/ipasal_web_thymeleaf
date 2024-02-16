package com.ipasal.ipasalwebapp.Services;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.CategoryDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryServices {
    Response<?> getCategoryByParentId(Integer parentId); // to get all the categories, set parentId = 0;
    Response<?> getFeaturedCategories();
    Response<?> getOfferedCategories();
    Response<?> getAllCategories();
    Response<?> getCategoryImages(Integer parentId);
    Response<?> addCategory(CategoryDTO category);
	Response<?> searchProductInCategory(int categoryId, String searchKey, HttpServletRequest request);
	Response<?> getProductsBetweenRange(Integer categoryId, Float minRate, Float maxRate);
	Response<?> uploadCategoryImage(MultipartFile[] file, Integer categoryId);
	Response<?> getAllParentCategory();
	Response<?> getCategoryByCategoryId(Integer categoryId);
	Response<?> deleteParentandChildCategory(List<Integer> categoryIds);
	
   /* List<List<CategoryDTO>> paginateCategories(List<CategoryDTO> categoryList);*/
}
