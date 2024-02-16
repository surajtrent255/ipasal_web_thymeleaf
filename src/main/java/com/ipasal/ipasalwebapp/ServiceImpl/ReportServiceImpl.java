/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Aug 30, 2019
 */
package com.ipasal.ipasalwebapp.ServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.ReportService;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;

@Service
public class ReportServiceImpl implements ReportService {
	
	private final String BASE_URL = "/report";
	private RestClientService restClientService;
    @Autowired
    ObjectMapper objectMapper;
	
	public ReportServiceImpl(RestClientService restClientService) {
		this.restClientService = restClientService;
	}

	@Override
	public Response<?> getReport(HttpServletRequest request) {
		return null;
	}

	@Override
	public Response<List<ProductDTO>> getTopSales(HttpServletRequest request) {
		JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ProductDTO.class));
		Response<List<ProductDTO>> response = null;
		response = (Response<List<ProductDTO>>) restClientService.getData( BASE_URL + "/topSales", responseType);
		return response;
	}

	@Override
	public Response<?> getReportsBetweenRange(String startDate, String endDate) {
		JavaType responseType = objectMapper.getTypeFactory()
				.constructParametricType(Response.class, objectMapper.getTypeFactory().constructParametricType(List.class, ProductDTO.class));
		Response<?> response = null;
		response = restClientService.getData( BASE_URL + "/salesRange?"+"startDate="+startDate+"&endDate="+endDate, responseType);
		return response;
	}

}
