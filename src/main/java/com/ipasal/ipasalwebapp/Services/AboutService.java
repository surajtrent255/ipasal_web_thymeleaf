package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.AboutDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by aevin on Feb 14, 2019
 **/
public interface AboutService {
    Response<?> getAboutInfo();
    Response<?> addAboutInfo(AboutDTO aboutDTO);
    Response<?> uploadAboutImages(MultipartFile[] file, Integer aboutId);
	Response<?> updateAboutInfo(AboutDTO aboutUs);
	Response<?> uploadBannerImage(MultipartFile[] files);
	Response<?> uploadStoryImage(MultipartFile[] files);
}
