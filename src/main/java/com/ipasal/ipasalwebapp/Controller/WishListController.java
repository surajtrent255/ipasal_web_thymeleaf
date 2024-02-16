package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.WishListService;
import com.ipasal.ipasalwebapp.dto.ProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.WishListDTO;
import com.ipasal.ipasalwebapp.utilities.UserDetailsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by aevin on Feb 27, 2019
 **/

/*
* Main Step: 4
*
* Step 1: Annotate the class with /Controller/
* Step 2: Annotate with /RequestMapping(requestPath)/ -> requestPath = "/some-path"
* Step 3: Get the respective Service Interface
* Step 4: Create a constructor with service as argument
* Step 5: Annotate the constructor with /Autowired/
* Step 6: Create method for each user action
*       6.1 : Annotate each method with various request method type /GetMapping, PutMapping, PostMapping, DeleteMapping/
*       6.2 : If the html page is returned by the method, return type must be /String/
*       6.3 : If only the json is being retrieved, return type should be /@ResponseBody/
* Step 7: In each method, get the response type using the service class methods.
* Step 8: For adding the data to the key value pairs using /model.addAttribute(key, value)/
* Step 9: Returning from the method
*       9.1 : If page is returned -> /return 'html-page-name' (without .html)
*       9.2 : If only json is being returned ->
*               /return new ResponseEntity<>(
*                   /[response type from step 7].getMessage()/,
*                   HttpStatus.valueOf([response type from step 7].getStatus()
*                )/
* */

@RequestMapping("/wish-list")
@Controller
public class WishListController {

    private Logger logger = LoggerFactory.getLogger(WishListController.class);
    private WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/{userId}")
    public String getWishListForUser(Model model, @PathVariable("userId") Integer userId){
        Response<List<ProductDTO>> response = null;
        int sessionUserId = UserDetailsUtil.getLoggedInUser(SecurityContextHolder.getContext().getAuthentication()).getUserId();
        //Comparing the sessionUserId with the userId provided in the parameter so that they match
        if(sessionUserId == userId) {
        response = (Response<List<ProductDTO>>) wishListService.getAllWishProductsForUser(userId);
        if (response.getData() != null && response.getData().size() > 0){
            List<ProductDTO> wishListProducts = response.getData();
            model.addAttribute("wishProductList", wishListProducts);
            model.addAttribute("logged-user-id", userId);
            return "wish-list";
        }else {
            return "nullwishlist";
        }}
        else {
        	return "unauthorized";
        }

    }

    /*
    * Controller for adding the wish list item
    * */
    @PostMapping("/add")
    public @ResponseBody Integer addProductToWishList(@RequestBody WishListDTO wishListDTO){
        Response<Integer> wishAddedResult = (Response<Integer>) wishListService.addWishItem(wishListDTO);
        logger.info("Added wish item id is -> "+ wishAddedResult.getData().toString());
        return wishAddedResult.getData();
    }

    /*
    * Controller for checking if the product is already present in the wish list
    * */

    @GetMapping("/status/{userId}/{productId}")
    public @ResponseBody Boolean checkProductInWishList(@PathVariable Integer userId, @PathVariable Integer productId){
        logger.info("Product check in wish list controller is called.");
        Response<Boolean> wishCheckResult = (Response<Boolean>) wishListService.checkProductInWishList(userId, productId);
        return wishCheckResult.getData();
    }

    /*
    * Controller for removing the wish list item from the wish list
    * @Parameter wishId
    * */
    @PutMapping("/{wishId}")
    public @ResponseBody
    ResponseEntity<String> removeWishItemFromWishList(@PathVariable Integer wishId,
                                              @RequestBody WishListDTO wishListDTO){
        Response<Void> removeWishItemResult =
                (Response<Void>) wishListService.removeWishItem(wishId, wishListDTO);
        return new ResponseEntity<>(removeWishItemResult.getMessage(), HttpStatus.valueOf(removeWishItemResult.getStatus()));
    }

    /*
    * Getting the wish list id for the given user id and the product id
    * */
    @GetMapping("/getId/{userId}/{productId}")
    public @ResponseBody Integer getWishListId(@PathVariable Integer userId, @PathVariable Integer productId){
        logger.info("Product check in wish list controller is called.");
        Response<Integer> wishIDResult = (Response<Integer>) wishListService.getWishListId(userId, productId);
        logger.info("Wish list id of the clicked item is -> "+wishIDResult.getData());
        return wishIDResult.getData();
    }

    /*
    * Notify admin, by sending an email
    * */
    @GetMapping("/notify/{userId}")
    public @ResponseBody Boolean notifyAdmin(@PathVariable Integer userId){
        logger.info("Notify admin is called");
        Response<Boolean> notifyResult = (Response<Boolean>) wishListService.notifyAdmin(userId);
        logger.info("The response for notifying is ->"+ notifyResult.getData());
        return notifyResult.getData();
    }
}
