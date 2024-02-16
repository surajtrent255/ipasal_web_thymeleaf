package com.ipasal.ipasalwebapp.ServiceImpl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.SliderService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SliderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aevin on Feb, 2019
 **/
@Service
public class SliderServiceImpl implements SliderService {

    private final String SLIDER_BASE_URL = "/slider";
    private RestClientService restClientService;

    @Autowired
    public SliderServiceImpl(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Override
    public Response<?> addSlider(SliderDTO sliderDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        response = restClientService.postData(SLIDER_BASE_URL, sliderDTO, responseType);
        return response;
    }

    @Override
    public Response<?> getAllActiveSliders() {
        Response<?> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
                objectMapper.getTypeFactory().constructParametricType(List.class, SliderDTO.class));
        response = restClientService.getData(SLIDER_BASE_URL, responseType);
        return response;
    }

	@Override
	public Response<?> getAllSliders() {
		Response<?> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class,
				objectMapper.getTypeFactory().constructParametricType(List.class, SliderDTO.class));
		response = restClientService.getData(SLIDER_BASE_URL + "/all", responseType);
		return response;
	}
    
    @Override
    public Response<?> updateSliderStatus(Integer id, Boolean sliderShow) {
        return null;
    }

    @Override
    public Response<?> addSliderImage(MultipartFile[] files, Integer sliderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, String.class);
        String url = SLIDER_BASE_URL + "/upload/" + sliderId;

        List<MultipartFile> newFiles = Arrays.asList(files);

        List<String> fileNames = newFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
        String[] extractedFileNames = new String[fileNames.size()];
        extractedFileNames = fileNames.toArray(extractedFileNames);
        Response<?> response = restClientService.postMultipartFiles(url, files,
                extractedFileNames, responseType);
        return response;
    }

	@Override
	public Response<?> deleteSliderData(Integer sliderId) {
		ObjectMapper objectMapper = new ObjectMapper();
        JavaType responseType =
                objectMapper.getTypeFactory().constructParametricType(Response.class, List.class);

        return restClientService.deleteData(SLIDER_BASE_URL + "/" + sliderId, null, responseType);
	}




}
