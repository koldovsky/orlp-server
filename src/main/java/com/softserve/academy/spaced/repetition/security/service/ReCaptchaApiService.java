package com.softserve.academy.spaced.repetition.security.service;

import com.softserve.academy.spaced.repetition.security.DTO.ReCaptchaResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaApiService {
    @Value("${app.reCaptcha.secretKey}")
    private String secretKey;

    @Value("${app.reCaptcha.url}")
    private String url;

    private RestTemplate restTemplate = new RestTemplate();

    public ReCaptchaResponseDto verify(String reCaptchaResponse){
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secretKey);
        form.add("response", reCaptchaResponse);
        return restTemplate.postForObject(url, form, ReCaptchaResponseDto.class);
    }
}
