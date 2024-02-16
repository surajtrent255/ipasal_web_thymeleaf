package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.OfferProductDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by aevin on Feb 06, 2019
 **/
public interface OfferProductService {
    Response<?> addOfferProductDetail(OfferProductDTO offerProductDTO);
    Response<?> getOfferProduct();
    Response<?> uploadOfferProductImage(MultipartFile[] files, Integer offerId);
}
