package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SliderDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by aevin on Feb, 2019
 **/
public interface SliderService {
    Response<?> addSlider(SliderDTO sliderDTO);
    Response<?> getAllActiveSliders();
    Response<?> getAllSliders();
    Response<?> updateSliderStatus(Integer id, Boolean sliderShow);
    Response<?> addSliderImage(MultipartFile[] file, Integer sliderId);
    Response<?> deleteSliderData(Integer sliderId);
}
