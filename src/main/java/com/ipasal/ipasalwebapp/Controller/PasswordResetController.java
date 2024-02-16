package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.UserService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 'Azens Eklak'
 * email: azens1995@gmail.com
 * created on Mar 11, 2019
 * since 2017
 **/

@RequestMapping("/forgot-password")
@Controller
public class PasswordResetController {
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    /*
    * 1. Get the password forget page from the Login page
    * */
    @GetMapping()
    public String getForgetPasswordView(Model model){
        return "forget-password";
    }

    /*
    * 2. Check the entered email, if it exist in the database
    * */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    public @ResponseBody boolean checkEmailValidity(@RequestParam("email") String email) {
        Response<Boolean> response = (Response<Boolean>) userService.checkDuplicateEmail(email);
        return response.getData();
    }

    /*
    * 3. Send email with the reset token to the given mail
    * */
    @RequestMapping(value = "/sendPasswordResetMail", method = RequestMethod.GET)
    public @ResponseBody String sendPasswordResetMail(@RequestParam("email") String email){
        logger.info("Password reset is being called.");
        Response<String> emailSentResponse = (Response<String>) userService.sendPasswordResetEmail(email);
        logger.info(emailSentResponse.toString());
        return emailSentResponse.getData();
    }

    /*
    * 4. Verify the token being clickec by the user from the email
    *   4.1 If token exist, get the User Id from the given token
    *   4.2 Open password reset page
    *   4.3 If token doesn't exit, open error page
    * */
    @GetMapping("/verifyPasswordToken")
    public String verifyPasswordToken(@RequestParam("token") String token, Model model){
        logger.info("Token is ->"+token);
        Response<Integer> response = (Response<Integer>) userService.verifyPasswordResetToken(token);
        logger.info("The verified token user id for password reset is ->"+response.getData());
        if(response.getData() != null) {
            model.addAttribute("userId", response.getData());
            return "password-reset";
        }
        throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    }

    /*
    * 5. Get the new password, and update the user table with the new password
    * */
    @GetMapping("/{userId}")
    public String resetUserPassword(@PathVariable("userId") Integer userId,
                                        @RequestParam("password") String password){
        logger.info("Resetting is being going on...");
        Response<List> updateResponse = (Response<List>) userService.updateUserPassword(userId, password);
        return "login";

    }
    
    


}
