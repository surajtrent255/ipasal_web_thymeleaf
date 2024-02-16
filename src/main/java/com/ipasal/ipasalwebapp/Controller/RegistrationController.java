package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.UserService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.UserDTO;
import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequestMapping("/registration")
@Controller
public class RegistrationController {
    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegistrationPage(Model model){
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkUserName", method = RequestMethod.POST)
    public @ResponseBody String checkUserName(@RequestParam("username") String username) {
        Response<Boolean> response = (Response<Boolean>)userService.checkUserName(username);
        String responseMsg = "{ ";
        if(Boolean.valueOf(response.getData())) {
            responseMsg += "\"valid\": " + false + ", \"message\":" + "\"" + response.getMessage() +"\"" + " }";
        } else {
            responseMsg +=  "\"valid\": " + true + " }";
        }
        return responseMsg;
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    public @ResponseBody String checkDuplicateEmail(@RequestParam("email") String email) {
        Response<Boolean> response = (Response<Boolean>) userService.checkDuplicateEmail(email);
        String responseMsg = "{ ";
        if(Boolean.valueOf(response.getData())) {
            responseMsg += "\"valid\": " + false + ", \"message\":" + "\"" + response.getMessage() +"\"" + " }";
        } else {
            responseMsg +=  "\"valid\": " + true + " }";
        }
        return responseMsg;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") UserDTO user, RedirectAttributes redirectAttributes) {
        System.out.println(user.toString());
//        user.setRoleId(3); //roleId 3 for customer
        if (user.getRoleId() != 4){
            user.setRoleId(3);
        }
        Response<?> response = (Response<?>) userService.registerUser(user);
        if(HttpStatus.OK.value() == response.getStatus()) {
        	redirectAttributes.addFlashAttribute("message", response);
            return "redirect:/registration/success";
        }
        
        throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
        
    }

    @GetMapping("/registrationConfirm")
    public String activateAccount(@RequestParam("token") String token, Model model) {
        Response<?> response = userService.activateUserAccount(token);
        if(response.getStatus() == HttpStatus.OK.value()) {
        	model.addAttribute("response", response);
            return "account-activation";
        }
        
        throw CustomExceptionThrowerUtil.throwException(response.getStatus(), response.getMessage());
    }
    @GetMapping("/success")
    public String registrationSuccessPage() {
        return "thank-you-registration";
    }
}