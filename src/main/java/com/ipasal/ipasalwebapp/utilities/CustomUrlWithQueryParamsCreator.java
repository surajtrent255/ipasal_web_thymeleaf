package com.ipasal.ipasalwebapp.utilities;

import javax.servlet.http.HttpServletRequest;


public class CustomUrlWithQueryParamsCreator {
	private static final String ACTION = "first";
	private static final Integer LAST_SEEN_ID = 0;
	private static final Integer PAGE_SIZE = 9;

	public static String generateQueryParams(HttpServletRequest request, PaginationTypeClass ptc) {
		String queryParams = "";
		if (checkParameter("last_seen", request)) {
			queryParams += "last_seen="
					+ Integer.parseInt(getParameterFromRequestObject("last_seen", request));
		} else {
			queryParams += "last_seen=" + LAST_SEEN_ID;
		}

		if (checkParameter("action", request)) {
			queryParams += "&action=" + (String) getParameterFromRequestObject("action", request);
		} else {
			queryParams += "&action=" + ACTION;
		}

		if (checkParameter("pageSize", request)) {
			queryParams += "&pageSize=" + Integer.parseInt(getParameterFromRequestObject("pageSize", request));
		} else {
			queryParams += "&pageSize=" + PAGE_SIZE;
		}
		
		switch(ptc) {
			case PRODUCT:
				if (checkParameter("stockFilter", request)) {
		            queryParams += "&stockFilter=" + (String) getParameterFromRequestObject("stockFilter", request);
		        }
				break;
			case REVIEW:
				break;
			case USER:
				break;
			case ORDER:
				break;
			default:
				break;
			
		}
		

		return queryParams;
	}

	// This function checks whether there is given parameter in request object
	public static boolean checkParameter(String parameterName, HttpServletRequest request) {
		return (request.getParameter(parameterName) != null ? true : false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getParameterFromRequestObject(String parameter, HttpServletRequest request) {
		return (T) request.getParameter(parameter);
	}
}
