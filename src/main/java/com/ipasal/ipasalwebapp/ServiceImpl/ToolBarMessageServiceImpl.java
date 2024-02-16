/**
 * 
 */
package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.Services.ToolBarMessageService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SocialMediaDTO;
import com.ipasal.ipasalwebapp.dto.ToolBarMessageDTO;

/**
 * @author Pujan K.C. <pujanov69@gmail.com>
 *
 * Created on Sep 6, 2019
 */
@Service
public class ToolBarMessageServiceImpl implements ToolBarMessageService {

	private final String TOOLBAR_BASE_URL = "/toolbarMessage";
	private RestClientService restClientService;
	
	@Autowired
    ObjectMapper objectMapper;
	
	public ToolBarMessageServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}
	
	@Override
	public Response<?> getToolBarMessage() {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, ToolBarMessageDTO.class);
		Response<?> response = null;
		response = restClientService.getData(TOOLBAR_BASE_URL , responseType);
		return response;
	}

	@Override
	public Response<?> updateToolBarMessage(ToolBarMessageDTO toolBarMessageDTO) {
        JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, Integer.class);
        Response<?> response = null;
        response = restClientService.putData(TOOLBAR_BASE_URL , toolBarMessageDTO, responseType);
        return response;
	}

}
