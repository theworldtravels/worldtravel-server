package com.spring.service.impl;

import com.base.ServiceBase;
import com.spring.api.OCRApi;
import com.spring.dao.TextMapper;
import com.spring.entity.Text;
import com.spring.service.OCRService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

@Service("OCRService")
public class OCRServiceImpl extends ServiceBase<Text> implements OCRService {
    @Resource
    private TextMapper dao;

    @Override
    protected Mapper<Text> getDao() {
        return dao;
    }

    @Override
    public Object recognizeService(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            OCRApi ocrApi = new OCRApi();
            ResponseEntity<String> responseEntity = ocrApi.recognizeText(file, redirectAttributes);
            String responseText = responseEntity.getBody();
            return responseText;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
