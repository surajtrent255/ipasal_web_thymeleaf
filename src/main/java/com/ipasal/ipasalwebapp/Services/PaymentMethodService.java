package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.KhaltiResponse;
import com.ipasal.ipasalwebapp.dto.KhaltiPayloadDTO;
import com.ipasal.ipasalwebapp.dto.PaymentMethodDTO;
import com.ipasal.ipasalwebapp.dto.Response;

/**
 * @author Azens Eklak
 * Created On 2019-03-22
 */
public interface PaymentMethodService {

    Response<?> insertPaymentMethods(PaymentMethodDTO paymentMethodDTO);
    Response<?> getAllPaymentMethods();
    Response<?> getAllActivePaymentMethods();
    Response<?> getPaymentMethodById(Integer id);
    Response<?> updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO);
    KhaltiResponse verifyKhalitPayment(KhaltiPayloadDTO payloadDTO);

}
