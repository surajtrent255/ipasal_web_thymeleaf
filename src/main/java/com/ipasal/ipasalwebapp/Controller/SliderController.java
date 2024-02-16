package com.ipasal.ipasalwebapp.Controller;

import com.ipasal.ipasalwebapp.Services.SliderService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.SliderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.util.List;

/**
 * Created by aevin on Feb 04, 2019
 **/
@Controller
@RequestMapping("/sliders")
public class SliderController {
    private SliderService sliderService;

    @Autowired
    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public @ResponseBody int addSliderData(@RequestBody SliderDTO sliderData){
        Response<Integer> result = (Response<Integer>) sliderService.addSlider(sliderData);

        return result.getData();
    }

    @RequestMapping(value = "/uploadSliderImage", method = RequestMethod.POST)
    public @ResponseBody String uploadSliderImage(MultipartHttpServletRequest request){
        List<MultipartFile> images = request.getFiles("images");
        MultipartFile[] files = new MultipartFile[images.size()];
        files = images.toArray(files);

        int sliderId = Integer.parseInt(request.getParameter("sliderId"));
        String resultString = ((Response<String>)sliderService.addSliderImage(files, sliderId)).getData();
        return resultString;
    }
    
    @DeleteMapping("/{sliderId}")
	public @ResponseBody ResponseEntity<String> deleteSlider(@PathVariable("sliderId") Integer sliderId) {
		Response<?> response = sliderService.deleteSliderData(sliderId);
		return new ResponseEntity<>(response.getMessage(), HttpStatus.valueOf(response.getStatus()));
	}
}