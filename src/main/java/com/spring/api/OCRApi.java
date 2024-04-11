package com.spring.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OCRApi {
    @Value("${ocr.appId}")
    private String appId;

    @Value("${ocr.secretCode}")
    private String secretCode;

    public ResponseEntity<String> recognizeText(//@RequestParam("file")
                                                MultipartFile file,
                                                RedirectAttributes redirectAttributes) {
        try {

            byte[] imgData = file.getBytes();
            String url = "https://api.textin.com/ai/service/v2/recognize";
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", "4ed98025b30338e1312ad3340f48f61a");
            conn.setRequestProperty("x-ti-secret-code", "ab4af6702ebca095997f3423abfc6d4e");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.getOutputStream().write(imgData);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            InputStream inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            StringBuilder result = new StringBuilder();
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                result.append(new String(buffer, 0, bytesRead));
            }
            inputStream.close();
            String json = result.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.has("result")) {
                JsonNode resultNode = rootNode.get("result");
                if (resultNode.has("lines")) {
                    JsonNode linesNode = resultNode.get("lines");
                    if (linesNode.isArray()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (JsonNode lineNode : linesNode) {
                            if (lineNode.has("text")) {
                                String textValue = lineNode.get("text").asText();
                                stringBuilder.append(textValue).append("\n");
                                //System.out.println("Text: " + textValue);
                            }
                        }
                        String concatenatedText = stringBuilder.toString().trim();
                        //log.info(concatenatedText);
                        return ResponseEntity.ok(concatenatedText);
                    }
                }
            } else {
                System.out.println("JSON does not contain 'text' field.");
            }

            return ResponseEntity.ok(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "OCR识别错误。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

}
