/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Aug 30, 2019
 */
package com.ipasal.ipasalwebapp.Services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;

public interface ReportService {
	
	public Response<?> getReport(HttpServletRequest request);

	public Response<List<ProductDTO>> getTopSales(HttpServletRequest request);

	public Response<?> getReportsBetweenRange(String startDate, String endDate);
}
