package com.ipasal.ipasalwebapp.exception;

/**
 * 
 * @author Yoomes <yoomesbhujel@gmail.com>
 * Custom Exception thrower utility class created for throwing custom exceptions.
 * It throws exception based on status code. 
 * Status code are retrieved from com.ipasal.ipasalwebapp.dto.Response<T> object.
 * 
 */

public class CustomExceptionThrowerUtil {
	
	/**
	 * 
	 * @param statusCode
	 * @param message
	 * @return <CustomBaseException.class> 's sub class.
	 */
	
	public static CustomBaseException throwException(int statusCode, String message) {
		switch(statusCode) {
		case 400: 
			return new BadRequestException(message);
		case 401:
			return new UnauthorizedException(message);
		case 403:
			return new ForbiddenException(message);
		case 404:
			return new ResourceNotFoundException(message);
		case 405:
			return new MethodNotSupportedException(message);
		case 415:
			return new UnsupportedTypeException(message);
		default: 
			return new CustomInternalServerErrorException(message);
		}
	}
}
