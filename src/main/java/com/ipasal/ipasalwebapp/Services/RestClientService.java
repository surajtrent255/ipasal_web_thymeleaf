package com.ipasal.ipasalwebapp.Services;

import com.fasterxml.jackson.databind.JavaType;
import com.ipasal.ipasalwebapp.dto.KhaltiResponse;
import com.ipasal.ipasalwebapp.dto.Response;

import org.springframework.web.multipart.MultipartFile;

public interface RestClientService {
    Response<?> login(Object requestObject, JavaType responseType);
    Response<?> getData(String url, JavaType responseType);
    Response<?> postData(String url, Object body, JavaType responseType);
    Response<?> putData(String url, Object body, JavaType responseType);
    Response<?> postMultipartFiles(String url, MultipartFile[] files, String[] fileNames, JavaType responseType);
    Response<?> deleteData(String url, Object body, JavaType responseType);
    KhaltiResponse postKhaltiData(Object body);
    //Resources<?> getPaginationDatas(String url, ParameterizedTypeReference<Resources<NewOrderDto>> responseType, String followAttr);
}
