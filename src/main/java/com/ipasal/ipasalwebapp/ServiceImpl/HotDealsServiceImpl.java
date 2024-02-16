package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.HotDealsService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.HotDealsDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aevin on Feb 05, 2019
 **/
@Service
public class HotDealsServiceImpl implements HotDealsService {

    private final String HOT_DEALS_BASE_URL = "/hot-deals";
    private RestClientService restClientService;

    public HotDealsServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    // Add Hot Deal Data
    @Override
    public Response<?> addHotDealData(HotDealsDTO hotDealsDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType =
                objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);

        return restClientService.postData(HOT_DEALS_BASE_URL, hotDealsDTO, responseType);
    }

    // Edit Hot Deal Data
	@Override
	public Response<?> editHotDealData(HotDealsDTO hotDealsDTO) {
		 ObjectMapper objectMapper = new ObjectMapper();
	        JavaType responseType =
	                objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);

	        return restClientService.putData(HOT_DEALS_BASE_URL, hotDealsDTO, responseType);
	}
    // Retrieve hot deal data
    @Override
    public Response<?> getActiveHotDeal() {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructParametricType(List.class, HotDealsDTO.class));
        response = restClientService.getData(HOT_DEALS_BASE_URL, responseType);
        return response;
    }

    // Upload Hot Deal image
    @Override
    public Response<?> uploadHotDealImage(MultipartFile[] files, Integer hotDealId) {
    	ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
		String url = HOT_DEALS_BASE_URL + "/upload/" + hotDealId ;
		
		List<MultipartFile> newFiles = Arrays.asList(files);
		List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
		String[] extractedFileNames = new String[fileNames.size()];
		extractedFileNames = fileNames.toArray(extractedFileNames);
		Response<?> response = restClientService.postMultipartFiles(url, files,
				extractedFileNames, responseType);
		return response;
    }

	@Override
	public Response<?> getAllHotDeal() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, HotDealsDTO.class));
		response = restClientService.getData(HOT_DEALS_BASE_URL + "/all", responseType);
		return response;
	}

	@Override
	public Response<?> deleteHotDealData(Integer hotDealId) {
		ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType =
                objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);

        return restClientService.deleteData(HOT_DEALS_BASE_URL + "/" + hotDealId, null, responseType);
	}

}
