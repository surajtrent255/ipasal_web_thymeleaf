package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.CategoryServices;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.CategoryDTO;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@Service
public class CategoryServiceImpl implements CategoryServices {
    private RestClientService restClientService;
	private static final String CATEGORY_BASE_URL = "/category";
	
    @Autowired
    public CategoryServiceImpl(RestClientService restClientService){
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> getCategoryByParentId(Integer parentId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory()
                .constructParametricType(
                        Response.class,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDTO.class)
                );
        Response<?> response = restClientService.getData("/category/parent/"+parentId, responseType);
        return response;
    }

    @Override
    public Response<?> getFeaturedCategories() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory()
                .constructParametricType(
                        Response.class,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDTO.class)
                );
        Response<?> response = null;
        response = restClientService.getData("/category/featured", responseType);
        return response;
    }
    
	@Override
	public Response<?> getOfferedCategories() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDTO.class));
		Response<?> response = null;
		response =restClientService.getData("/category/offered", responseType);
		return response;
	}

	@Override
    public Response<?> getAllCategories() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory()
            .constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, CategoryDTO.class));
        Response<?> response = null;
        response = restClientService.getData("/category", responseType);
        return response;
    }

	@Override
	public Response<?> getCategoryImages(Integer parentId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(
						Response.class,
						objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDTO.class)
				);
		Response<?> response = null;
		response = restClientService.getData("/category/image/"+parentId, responseType);
		return response;
	}

	@Override
    public Response<?> addCategory(CategoryDTO category) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        response = restClientService.postData("/category/", category, responseType);
        return response;
    }

    /**
     * @param searchKey String
     * @param request HttpServletRequest object
     * @return Returns List of productdto object which is wrapped inside ResponseObject
     */
    
	@Override
	public Response<?> searchProductInCategory(int categoryId, String searchKey, HttpServletRequest request) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory()
									.constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
		String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);
		String url = CATEGORY_BASE_URL + "/" + categoryId + "/product?searchKey=";
		try {
			url += URLEncoder.encode(searchKey, StandardCharsets.UTF_8.name()) + "&" + queryParams;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Response<?> response = restClientService.getData(url, responseType);
		return response;
	}

	@Override
	public Response<?> getProductsBetweenRange(Integer categoryId, Float minRate, Float maxRate) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
		Response<?> response = null;
		response = restClientService.getData("/category/"+categoryId+"/product/rate?"+"min="+minRate+"&max="+maxRate, responseType);
		return response;
	}

	@Override
	public Response<?> uploadCategoryImage(MultipartFile[] files, Integer categoryId) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
		String url = CATEGORY_BASE_URL + "/featured/upload/" + categoryId;

		List<MultipartFile> newFiles = Arrays.asList(files);

		List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
		String[] extractedFileNames = new String[fileNames.size()];
		extractedFileNames = fileNames.toArray(extractedFileNames);
		Response<?> response = restClientService.postMultipartFiles(url, files,
				extractedFileNames, responseType);
		return response;
	}

	// Retrieving all parent category
	@Override
	public Response<?> getAllParentCategory() {
		ObjectMapper objMapper = new ObjectMapper();
		JavaType typeResponse = objMapper.getTypeFactory()
				.constructParametricType(Response.class, objMapper.getTypeFactory().constructParametricType(List.class, CategoryDTO.class));
		Response<?> response = null;
		response = restClientService.getData(CATEGORY_BASE_URL+"/parent/all", typeResponse);
		return response;
	}
		
		@Override
		public Response<?> getCategoryByCategoryId(Integer categoryId) {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, CategoryDTO.class));
			Response<?> response = null;
			response = restClientService.getData(CATEGORY_BASE_URL + "/" + categoryId, responseType);
			return response;
		}

		@Override
		public Response<?> deleteParentandChildCategory(List<Integer> categoryIds) {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
			Response<?> response = restClientService.deleteData(CATEGORY_BASE_URL + "/delete", categoryIds, responseType);
			return response;
		}


}

