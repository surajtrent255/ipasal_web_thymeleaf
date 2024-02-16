package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.HotDealsDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by aevin on Feb 05, 2019
 **/
public interface HotDealsService {
    Response<?> addHotDealData(HotDealsDTO hotDealsDTO);
    Response<?> getActiveHotDeal();
    Response<?> getAllHotDeal();
    Response<?> uploadHotDealImage(MultipartFile[] files, Integer hotDealId);
	Response<?> editHotDealData(HotDealsDTO hotDealsDTO);
	Response<?> deleteHotDealData(Integer hotDealId);

}
