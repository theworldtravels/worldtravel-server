package com.spring.service;

import com.base.IServiceBase;
import com.spring.entity.Text;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface OCRService extends IServiceBase<Text> {

    public Object recognizeService(MultipartFile file, RedirectAttributes redirectAttributes);

}
