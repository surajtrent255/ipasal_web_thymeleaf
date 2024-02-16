package com.ipasal.ipasalwebapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipasal.ipasalwebapp.Services.RestClientService;
import com.ipasal.ipasalwebapp.dto.LoginDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.UserDTO;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private RestClientService restClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LoginDTO loginDTO = new LoginDTO();
		String email = authentication.getName();
		loginDTO.setEmail(email);
		loginDTO.setPassword(authentication.getCredentials().toString());
		ObjectMapper mapper = new ObjectMapper();
		Response<UserDTO> loggedInUser = null;
		JavaType responseType = mapper.getTypeFactory().constructParametricType(Response.class, UserDTO.class);
		loggedInUser = (Response<UserDTO>) restClient.login(loginDTO, responseType);
		
		if((loggedInUser != null) && (loggedInUser.getData() != null)){
			authentication = new UsernamePasswordAuthenticationToken(loggedInUser.getData(), null , loggedInUser.getData().getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return authentication;
		} 
		
		throw new BadCredentialsException("Invalid Credentials");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
	
}
