package com.ipasal.ipasalwebapp.utilities;

import com.ipasal.ipasalwebapp.dto.UserDTO;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

//Utility class which is used to get loggedin user information 
public class UserDetailsUtil {

    public static UserDTO getLoggedInUser(Authentication auth) {
        return (UserDTO )auth.getPrincipal();
    }

    public static boolean isLoggedIn(Authentication auth) {
        return !(auth instanceof AnonymousAuthenticationToken);
    }
}