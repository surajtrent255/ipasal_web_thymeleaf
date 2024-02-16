package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.WishListDTO;

/**
 * Created by aevin on Feb 27, 2019
 **/

/*
* Main Step : 2
*
* Return type for all the methods is -> /Response<?>/
* */

public interface WishListService {
    Response<?> addWishItem(WishListDTO wishListDTO);
    Response<?> removeWishItem(Integer wishId, WishListDTO wishListDTO);
    Response<?> getAllWishProductsForUser(Integer userId);
    Response<?> checkProductInWishList(Integer userId, Integer productId);
    Response<?> getWishListId(Integer userId, Integer productId);
    Response<?> notifyAdmin(Integer userId);
    Response<?> getWishListOfUser(Integer userId);

}
