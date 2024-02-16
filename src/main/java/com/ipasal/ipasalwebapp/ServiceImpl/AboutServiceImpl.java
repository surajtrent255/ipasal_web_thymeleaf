package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.AboutService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.AboutDTO;
import com.ipasal.ipasalwebapp.dto.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by aevin on Feb 14, 2019
 **/
@Service
public class AboutServiceImpl implements AboutService {
    private RestClientService restClientService;

    private static final String ABOUT_BASE_URL = "/about";

    public AboutServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> getAboutInfo() {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().
        		constructParametricType(Response.class,
        				objectMapper.getTypeFactory().constructCollectionType(List.class, AboutDTO.class)
        				);
        response = restClientService.getData(ABOUT_BASE_URL, responseType);
        return response;
    }

    @Override
    public Response<?> addAboutInfo(AboutDTO aboutDTO) {
    	ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        response = restClientService.postData("/about/", aboutDTO, responseType);
        return response;
    }

    @Override
    public Response<?> uploadAboutImages(MultipartFile[] file, Integer aboutId) {
        return null;
    }

	@Override
	public Response<?> updateAboutInfo(AboutDTO aboutUs) {
		ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        response = restClientService.putData("/about/", aboutUs, responseType);
        return response;
	}


	@Override
	public Response<?> uploadBannerImage(MultipartFile[] files) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
		String url = ABOUT_BASE_URL + "/upload/bannerImage" ;
		
		List<MultipartFile> newFiles = Arrays.asList(files);
		List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
		String[] extractedFileNames = new String[fileNames.size()];
		extractedFileNames = fileNames.toArray(extractedFileNames);
		Response<?> response = restClientService.postMultipartFiles(url, files,
				extractedFileNames, responseType);
		return response;
	}

	@Override
	public Response<?> uploadStoryImage(MultipartFile[] files) {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
		String url = ABOUT_BASE_URL + "/upload/storyImage" ;
		
		List<MultipartFile> newFiles = Arrays.asList(files);
		List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
		String[] extractedFileNames = new String[fileNames.size()];
		extractedFileNames = fileNames.toArray(extractedFileNames);
		Response<?> response = restClientService.postMultipartFiles(url, files,
				extractedFileNames, responseType);
		return response;
	}
}
