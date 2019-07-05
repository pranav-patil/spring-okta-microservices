package com.emprovise.service.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
class FinanceController {

    @GetMapping("/welcome/user")
    @PreAuthorize("hasAnyAuthority('READ_STOCKS')")
    @ResponseBody
    public String welcomeUser(@RequestParam(name="name", required=false, defaultValue="Java Fan") String name, HttpServletRequest httpRequest) {
        String response = getServiceResponse("http://localhost:8083/welcome", getAccessToken(httpRequest));
        return String.format("%s \nHello %s, you are welcome in Finance Service !!", response, name);
    }

    private String getServiceResponse(String serviceURL, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(getHttpHeaders(accessToken));
        ResponseEntity<String> responseEntity = restTemplate.exchange(serviceURL, HttpMethod.GET, request, String.class);

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }
        return null;
    }

    private HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if(accessToken != null) {
            httpHeaders.setBearerAuth(accessToken);
        }

        return httpHeaders;
    }

    private String getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}