package com.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/ai")
public class AIController {

    //@Value("${api.url}")
    private static final String API_URL = "https://burn.hair/v1/chat/completions";
    //@Value("${api.bearerToken}")
    private static final String AUTH_TOKEN = "sk-C8WlEHsIfzr26EBdAcCe73348a734972BeA015218e757143";

    @PostMapping("/chat")
    public String sendMessage(@RequestParam String message) {
        try {

            // 创建URL对象
            URL url = new URL(API_URL);

            // 设置连接超时时间为10秒
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();;
            connection.setConnectTimeout(10000);

            // 创建HttpURLConnection对象
            connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Authorization", "Bearer " + AUTH_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");

            // 启用输出流，并设置请求体
            connection.setDoOutput(true);
            String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}], \"temperature\": 0.5}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            // 获取响应
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // 读取响应内容
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 返回响应内容
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}



