package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.ShippingRateService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ShippingRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 'Azens Eklak'
 * email: azens1995@gmail.com
 * created on Mar 08, 2019
 * since 2017
 **/
// 1.
@Controller
// 2.
@RequestMapping("/shipping-rate")
public class ShippingDetailController {
    // 3.
    private ShippingRateService shippingRateService;

    // 4.
    @Autowired
    public ShippingDetailController(ShippingRateService shippingRateService) {
        this.shippingRateService = shippingRateService;
    }

    // 6.
    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public @ResponseBody int addShippingDetails(@RequestBody ShippingRateDTO shippingRateDTO){
        Response<Integer> result = (Response<Integer>) shippingRateService.addShippingRateInfo(shippingRateDTO);
        return result.getData();
    }


}
