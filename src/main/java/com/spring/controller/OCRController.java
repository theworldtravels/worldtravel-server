package com.spring.controller;

import com.spring.api.OCRApi;
import com.spring.dao.TextMapper;
import com.spring.entity.Text;
import com.spring.service.OCRService;
import com.spring.service.impl.OCRServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@Controller
@Slf4j
@RequestMapping("/ocr")
public class OCRController {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private TextMapper textMapper;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/photoRecognize")
    public Object recognize(@RequestParam("file")
                            MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        Text text = new Text();

        // 生成一个5位数id
        Set<Integer> generatedNumbers = new HashSet<>();
        Random random = new Random();
        while (generatedNumbers.size() < 5) {
            int number = random.nextInt(90000) + 10000; // 生成一个5位数（范围：10000-99999）
            generatedNumbers.add(number);
        }
        for (int number : generatedNumbers) {
            //System.out.println(number);
            text.setId(number);
        }

        text.setPhotoUrl(file.getOriginalFilename());
        text.setTextRecognize((String)ocrService.recognizeService(file,redirectAttributes));

        textMapper.insertText(text);
        return ResponseEntity.ok(text.getTextRecognize());
    }

}

