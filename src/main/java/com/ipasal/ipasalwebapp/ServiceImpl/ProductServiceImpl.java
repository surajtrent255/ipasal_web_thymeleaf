package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.ProductServices;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.utilities.CustomUrlWithQueryParamsCreator;
import com.ipasal.ipasalwebapp.utilities.PaginationTypeClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
public class ProductServiceImpl implements ProductServices {
    private final String PRODUCTS_BASE_URL = "/products";
    private RestClientService restClientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    public ProductServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> getAllProducts() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        Response<?> response = null;
        response = restClientService.getData(PRODUCTS_BASE_URL, responseType);
        return response;
    }

    @Override
    public Response<?> getProductById(Integer productId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory()
                .constructParametricType(
                        Response.class,
                        objectMapper.getTypeFactory().constructParametricType(List.class, ProductDTO.class));
        Response<?> response = null;
        response = restClientService.getData(PRODUCTS_BASE_URL + "/" + productId, responseType);
        return response;
    }

    @Override
    public Response<?> getFeaturedProducts() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        Response<?> response = null;
        response = restClientService.getData(PRODUCTS_BASE_URL + "/featured", responseType);
        return response;
    }

    @Override
    public Response<?> getProductsByCategoryId(Integer categoryId, Pageable pageable, HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        Response<?> response = null;
        String url = PRODUCTS_BASE_URL + "/category/" + categoryId + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
        String stockFilter = request.getParameter("stockFilter");
        if (stockFilter != null) {
            url += "&stockFilter=" + stockFilter;
        }
        response = restClientService.getData(url, responseType);
        return response;
    }

    @Override
    public Response<?> getProductsByParentCategoryId(Integer categoryId, Pageable pageable, HttpServletRequest request) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        Response<?> response = null;
        String url = PRODUCTS_BASE_URL + "/parentCategory/" + categoryId + "?page=" + page + "&size=" + size;
        String stockFilter = request.getParameter("stockFilter");
        if (stockFilter != null) {
            url += "&stockFilter=" + stockFilter;
        }
        response = restClientService.getData(url, responseType);
        return response;
    }

    @Override
    public Response<?> addProduct(ProductDTO product) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        try {
			String json = objectMapper.writeValueAsString(product);
			 LOGGER.info("json------->" + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        Response<?> response = null;
        response = restClientService.postData(PRODUCTS_BASE_URL, product, responseType);
        return response;
    }

    @Override
    public Response<?> uploadProductImages(MultipartFile[] files, Integer productId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        String url = PRODUCTS_BASE_URL + "/upload/" + productId;

        List<MultipartFile> newFiles = Arrays.asList(files);

        List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
        String[] extractedFileNames = new String[fileNames.size()];
        extractedFileNames = fileNames.toArray(extractedFileNames);
        Response<?> response = restClientService.postMultipartFiles(url, files,
                extractedFileNames, responseType);
        return response;
    }

    @Override
    public Response<?> searchProductBySearchKey(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructParametricType(List.class, ProductDTO.class));
        String searchKey = "";
        if (CustomUrlWithQueryParamsCreator.checkParameter("searchKey", request)) {
            searchKey = (String) CustomUrlWithQueryParamsCreator.getParameterFromRequestObject("searchKey", request);
        }
        String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);

        String url = PRODUCTS_BASE_URL + "/search?searchKey=";
        try {
            url += URLEncoder.encode(searchKey, StandardCharsets.UTF_8.name()) + "&" + queryParams;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Response<?> response = restClientService.getData(url, responseType);

        return response;
    }

    @Override
    public Response<?> updateProductInfoById(Integer productId, ProductDTO product) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        String url = PRODUCTS_BASE_URL + "/" + productId;
        Response<?> response = restClientService.putData(url, product, responseType);
        return response;
    }

    @Override
    public Response<?> deleteProductByProductId(Integer productId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);
        String url = PRODUCTS_BASE_URL + "/" + productId;
        Response<?> response = restClientService.deleteData(url, null, responseType);
        return response;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Response<List<ProductDTO>> getProductsByParentCategoryId(HttpServletRequest request, Integer parentCategoryId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);
        String url = PRODUCTS_BASE_URL + "/parentCategory/" + parentCategoryId + "?" + queryParams;
        Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) restClientService.getData(url, responseType);
        return response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<List<ProductDTO>> getProductsByChildCategoryId(HttpServletRequest request,
                                                                   Integer childCategoryId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);
        String url = PRODUCTS_BASE_URL + "/category/" + childCategoryId + "?" + queryParams;
        Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) restClientService.getData(url, responseType);
        return response;
    }

	@Override
	public Response<?> getAllSaleProducts(HttpServletRequest request) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionLikeType(List.class, ProductDTO.class));
		String queryParams = CustomUrlWithQueryParamsCreator.generateQueryParams(request, PaginationTypeClass.PRODUCT);
		String url = PRODUCTS_BASE_URL + "/allSales?" + queryParams;
		Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) restClientService.getData(url, responseType); 
		return response;
	}

	@Override
	public Response<?> getSaleProductsforIndex() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructCollectionLikeType(List.class, ProductDTO.class));
		String url = PRODUCTS_BASE_URL + "/sales";
		Response<List<ProductDTO>> response = (Response<List<ProductDTO>>) restClientService.getData(url, responseType);
		return response;
	}
}
