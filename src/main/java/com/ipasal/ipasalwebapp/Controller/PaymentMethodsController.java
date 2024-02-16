package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.ServiceImpl.StripeServiceImpl;
import com.ipasal.ipasalwebapp.Services.PaymentMethodService;
import com.ipasal.ipasalwebapp.dto.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Azens Eklak
 * Created On 2019-03-25
 */
@RequestMapping("/payment-methods")
@Controller
public class PaymentMethodsController {

    private PaymentMethodService paymentMethodService;
    private StripeServiceImpl stripeService;
    private Logger logger = LoggerFactory.getLogger(PaymentMethodsController.class);

    @Autowired
    public PaymentMethodsController(PaymentMethodService paymentMethodService, StripeServiceImpl stripeService) {
        this.paymentMethodService = paymentMethodService;
        this.stripeService = stripeService;
    }

    /*
    * Letting user choose the payment methods from the available payment options
    * */
    @GetMapping
    public String getPaymentMethods(Model model){
        /*
        * For getting all the list of the payment options
        * */
        Response<List<PaymentMethodDTO>> paymentMethodResponse = (Response<List<PaymentMethodDTO>>) paymentMethodService.getAllPaymentMethods();
        if (paymentMethodResponse != null && paymentMethodResponse.getData().size() >0){
            model.addAttribute("checkoutPayment", paymentMethodResponse.getData());
        }

        return "user-payment-methods";
    }

    @PostMapping("/verify")
    public @ResponseBody
    ResponseEntity<KhaltiResponse> makeKhaltiPayment(@RequestBody KhaltiPayloadDTO payloadDTO){
        logger.info("Khalti-Payment is being called");
        KhaltiResponse verifyPayment = paymentMethodService.verifyKhalitPayment(payloadDTO);
        logger.info("The payment method being updated is ->"+verifyPayment);
        return new ResponseEntity<>(verifyPayment, HttpStatus.OK);
    }

    @PostMapping("/stripe")
    public String charge(StripeChargeDTO chargeRequest, Model model) throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(StripeChargeDTO.Currency.EUR);
        logger.info("Charge request being sent is ->"+chargeRequest.getStripeToken()+" "+chargeRequest.getStripeToken()+" "+chargeRequest.getAmount());
        Charge charge = stripeService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        logger.info("The successful payment info is ->Id: "+charge.getId()+" status: "+charge.getStatus()
        +" balance Transaction: "+charge.getBalanceTransaction());
        return "thank-you";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
