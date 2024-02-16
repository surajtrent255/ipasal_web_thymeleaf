package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
public interface ProductServices {
    Response<?> getAllProducts();
    Response<?> getProductById(Integer productId);
    Response<?> getFeaturedProducts();
    Response<?> getProductsByCategoryId(Integer categoryId, Pageable pageable, HttpServletRequest request);
    Response<?> getProductsByParentCategoryId(Integer categoryId, Pageable pageable, HttpServletRequest request);
    Response<?> addProduct(ProductDTO product);
    Response<?> uploadProductImages(MultipartFile[] files, Integer productId);
    Response<?> searchProductBySearchKey(HttpServletRequest request);
	Response<?> updateProductInfoById(Integer productId, ProductDTO product);
	Response<?> deleteProductByProductId(Integer productId);
	
	//optimized for pagination..
	Response<?> getProductsByParentCategoryId(HttpServletRequest request, Integer parentCategoryId);
	Response<?> getProductsByChildCategoryId(HttpServletRequest request, Integer childCategoryId);
	Response<?> getSaleProductsforIndex();
	Response<?> getAllSaleProducts(HttpServletRequest request);
 }
